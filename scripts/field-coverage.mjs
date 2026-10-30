#!/usr/bin/env node
// 按字段核对 wrapper 声明在用例里的覆盖情况，找出「类型写了、字段没人读过」的缺口。
//
// AGENTS.md 的「测试覆盖」要求 `src/jsMain`（纯 KMP 模块是 `src/commonMain`）里的每个声明都能被用例
// 点到名。只看类型名不够：`unsafeJso { }` 的接收者类型是推断出来的，测试文件里根本不出现类型名，按
// 名字粗筛会大量误报；而一个 `external interface`（或 `@Serializable` 模型类）里真正需要核对的是它
// 的成员——上游读的是键名，写错键名时类型化读写依然自洽，所以只有「这个字段有没有被任何用例写过/读过」
// 才是有效信号。
//
// 判定方式：把主源码里每个类型（`interface` / `class` / `object`）直接声明的成员名收集起来，再看测试
// 源码全量词标记记不记这个名字。带 `@JsName("x")` 的成员，Kotlin 名与 JS 键名 `x` 任一出现即算覆盖；
// 反过来，一个裸键名成员只要同名的 JS 键被别的带 `@JsName` 的成员覆盖，也算覆盖——`typedCoordinateSystem`
// 写过，`coordinateSystem` 就是同一个键，不算缺口。像 `show`、`name` 这种到处都出现的成员名，在这个
// 口径下几乎总是「已覆盖」，所以输出适合用来找**从没出现过的成员**，不能当逐用例的覆盖率用。
//
// 用法：
//   node scripts/field-coverage.mjs                     所有带 Kotlin 源码的模块
//   node scripts/field-coverage.mjs ktjs-echarts-gl     只看某个模块
//   node scripts/field-coverage.mjs --limit 20          每个文件最多列 20 个缺口（默认全部）
//   node scripts/field-coverage.mjs --show-covered      顺带打印已覆盖的成员名
//
// 退出码始终是 0：这是给人看的清单，不是构建关卡。

import fs from 'node:fs'
import path from 'node:path'
import process from 'node:process'
import { fileURLToPath } from 'node:url'

const repoRoot = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..')

/**
 * 明确不做的成员，`Type.member` 形式，支持 `Type.*` 与 `*` 结尾的前缀。理由与 AGENTS.md 一致。
 */
const NOT_COVERED = [
    { name: 'EChartsGlCore.withEChartsGl', reason: '安装器 getter 的内部管道，InstallerTest 断言的是安装结果。' },
    { name: 'EChartsGlCore.withGeo3DShadowShader', reason: '同上，Geo3DGlobeTest 已断言它注册的着色器。' },
    { name: 'MapService3DOption.*', reason: 'mapbox3D / maptalks3D 要第三方 SDK 与全局 token，只做类型不做用例。' },
    { name: 'Mapbox3DOption.*', reason: '同上。' },
    { name: 'Maptalks3DOption.*', reason: '同上。' },
    { name: '*.mapbox3DIndex', reason: '同上：没有 SDK 就建不出那个坐标系统。' },
    { name: '*.maptalks3DIndex', reason: '同上。' },
]

/**
 * 命中「明确不做」清单。
 */
function isExcluded(label) {
    return NOT_COVERED.some(
        (item) =>
            label === item.name ||
            (item.name.endsWith('.*') && label.startsWith(item.name.slice(0, -1)) && label.includes('.')) ||
            (item.name.startsWith('*.') && label.endsWith(item.name.slice(1))),
    )
}


/**
 * 声明前的注解与修饰符。`external interface` 的成员通常没有修饰符，普通模型类/对象则有
 * `public`、`override`、`@SerialName` 之类；两种都要认。
 */
const PREFIX =
    /^\s*(?:@\w+(?:\([^)]*\))?\s+)*(?:(?:public|internal|protected|external|override|abstract|open|final|sealed|data|value|enum|annotation|inline|suspend|operator|infix|tailrec|const|lateinit)\s+)*/

/**
 * 属性声明行。只收可写的 `var`：`external interface` 的 `val` 是上游只读值，普通模型类的
 * 不可变字段也来自构造参数，与「键名有没有被写过」这个口径无关。
 */
const PROPERTY_PATTERN = new RegExp(`${PREFIX.source}var\\s+(\\w+)`)

/** 类型声明行：`interface` / `class` / `object`；`companion object` 仍属于宿主类型。 */
const TYPE_PATTERN = new RegExp(`${PREFIX.source}(?:interface|class|object)\\s+(\\w+)`)

/** 顶层工厂函数与构建器（联合类型包装、`xxxSeries`）；带接收者的扩展函数不算成员。 */
const FUN_PATTERN = new RegExp(`${PREFIX.source}fun\\s+(?:<[^>]*>\\s+)?(\\w+)\\s*\\(`)

/** 一行新声明的开头；多行类型声明头里遇到它，就说明前一个类型没有类型体。 */
const DECLARATION_START =
    /^\s*(?:@\w+|(?:public|private|internal|protected|external|override|abstract|open|final|data|value|enum|sealed|annotation|fun|var|val|class|interface|object)\b)/

/**
 * 递归列出目录下的 `.kt` 文件。
 */
function kotlinFiles(dir, acc = []) {
    if (!fs.existsSync(dir)) return acc
    for (const name of fs.readdirSync(dir)) {
        const full = path.join(dir, name)
        if (fs.statSync(full).isDirectory()) kotlinFiles(full, acc)
        else if (name.endsWith('.kt')) acc.push(full)
    }
    return acc
}

/**
 * 测试源码里出现过的全部词标记。
 */
function tokensOf(files) {
    const tokens = new Set()
    for (const file of files) {
        for (const match of fs.readFileSync(file, 'utf8').matchAll(/[A-Za-z_]\w*/g)) {
            tokens.add(match[0])
        }
    }
    return tokens
}

/**
 * 一个源文件里声明的成员，按 `@JsName` 的 JS 键名和 Kotlin 名两个口径收集。
 *
 * 只收类型体或文件顶层直接声明的成员。函数体里的局部变量不算，否则 `val element = ...` 会让几乎
 * 每个模型类都凭空多出一个成员。
 */
function parseFile(file) {
    const source = fs.readFileSync(file, 'utf8')
    const lines = source.split('\n')
    const members = []
    const fallback = path.basename(file, '.kt')
    // 类型作用域栈：`{ name, depth }`，depth 是类型体所在的括号深度。
    const scopes = []
    let braceDepth = 0
    let pendingType = null
    let pendingLine = -1
    for (let index = 0; index < lines.length; index += 1) {
        const line = lines[index]
        const companion = /\bcompanion\s+object\b/.test(line)
        const typeMatch = companion ? null : TYPE_PATTERN.exec(line)
        // 类型声明可能换行写（`external interface X :` + 超类型列表 + `{`，或主构造函数参数跨行）。
        // 等到 `{` 之前，遇到新的声明就说明上一个类型没有类型体，别把它的名字套到下一个函数体上；
        // 主构造函数的 `public var` / `public val` 参数仍属于该类型，不能当新声明的开始。
        const constructorProperty = PROPERTY_PATTERN.test(line) || /^\s*(?:public|internal|protected)\s+val\s+\w+\s*:/.test(line)
        if (pendingType !== null && pendingLine !== index && DECLARATION_START.test(line) && !constructorProperty) {
            pendingType = null
        }
        const scope = scopes.length > 0 ? scopes[scopes.length - 1] : null
        const scopeDepth = scope ? scope.depth : 0
        const typeName = pendingType !== null ? pendingType : (scope ? scope.name : fallback)
        if (braceDepth === scopeDepth) {
            const funMatch = scope === null ? FUN_PATTERN.exec(line) : null
            // 顶层工厂函数（联合类型包装、构建器）与类型成员一起按名字核对。
            const name = funMatch ? funMatch[1] : (PROPERTY_PATTERN.exec(line)?.[1] ?? null)
            if (name) {
                // `@JsName` 可能写在上一行，也可能与声明同一行。
                const context = `${index > 0 ? lines[index - 1] : ''} ${line}`
                const jsName = /@JsName\("([^"]+)"\)/.exec(context)
                members.push({ type: typeName, name, jsName: jsName ? jsName[1] : null, line: index + 1 })
            }
        }
        if (companion) {
            pendingType = typeName
            pendingLine = index
        } else if (typeMatch) {
            pendingType = typeMatch[1]
            pendingLine = index
        }
        for (const char of line) {
            if (char === '{') {
                braceDepth += 1
                if (pendingType !== null) {
                    scopes.push({ name: pendingType, depth: braceDepth })
                    pendingType = null
                }
            } else if (char === '}') {
                while (scopes.length > 0 && scopes[scopes.length - 1].depth === braceDepth) scopes.pop()
                braceDepth -= 1
            }
        }
    }
    return { members }
}

const argv = process.argv.slice(2)
const limitIndex = argv.indexOf('--limit')
const limit = limitIndex >= 0 ? Number(argv[limitIndex + 1]) : Infinity
const showCovered = argv.includes('--show-covered')
const moduleArgs = argv.filter((arg) => !arg.startsWith('--') && arg !== String(limit))

/**
 * 模块的主源码目录：wrapper 模块是 `jsMain`，纯 KMP 模块是 `commonMain`。
 */
function mainSourceFiles(moduleName) {
    return ['jsMain', 'commonMain']
        .map((set) => path.join(repoRoot, moduleName, 'src', set, 'kotlin'))
        .flatMap((dir) => kotlinFiles(dir))
}

/**
 * 模块的测试源码目录：优先与主源码同名的 source set，其次是另一套。
 */
function testSourceFiles(moduleName) {
    const sets = ['jsTest', 'commonTest']
    for (const set of sets) {
        const files = kotlinFiles(path.join(repoRoot, moduleName, 'src', set, 'kotlin'))
        if (files.length > 0) return files
    }
    return []
}

// `demo` 是演示站点、`scaffold` 是模板骨架，都不参与覆盖核对。
const published = fs.readdirSync(repoRoot).filter((name) => {
    if (name === 'demo' || name === 'scaffold') return false
    const dir = path.join(repoRoot, name)
    if (!fs.statSync(dir).isDirectory()) return false
    return mainSourceFiles(name).length > 0
})
const modules = moduleArgs.length > 0 ? moduleArgs : published

let totalMembers = 0
let totalMissing = 0
for (const moduleName of modules) {
    const mainFiles = mainSourceFiles(moduleName)
    const testFiles = testSourceFiles(moduleName)
    if (testFiles.length === 0) {
        console.log(`${moduleName}: 没有 jsTest / commonTest 源码，跳过`)
        continue
    }
    const tokens = tokensOf(testFiles)
    console.log(`\n## ${moduleName}（用例文件 ${testFiles.length} 个）\n`)
    const parsed = mainFiles.map((file) => ({ file, members: parseFile(file).members }))
    const selfCovered = (member) =>
        tokens.has(member.name) || (member.jsName !== null && tokens.has(member.jsName))
    // 带 @JsName 的成员被写过的话，它映射的那个 JS 键就算覆盖了；裸声明同一个键的成员不再单独算缺口。
    const coveredKeys = new Set(
        parsed
            .flatMap((entry) => entry.members)
            .filter((member) => member.jsName !== null && selfCovered(member))
            .map((member) => member.jsName),
    )
    let missingInModule = 0
    let membersInModule = 0
    let excludedInModule = 0
    for (const { file, members } of parsed) {
        membersInModule += members.length
        const missing = []
        const covered = []
        for (const member of members) {
            const label = `${member.type}.${member.name}`
            if (isExcluded(label)) {
                excludedInModule += 1
                continue
            }
            if (selfCovered(member) || coveredKeys.has(member.name)) covered.push(member)
            else missing.push(member)
        }
        const rel = path.relative(repoRoot, file)
        if (missing.length > 0) {
            console.log(`  ${rel}`)
            for (const member of missing.slice(0, limit)) {
                console.log(
                    `    ${member.type}.${member.name}${member.jsName ? ` (JS: ${member.jsName})` : ''} [${member.line}]`,
                )
            }
            if (missing.length > limit) console.log(`    … 另有 ${missing.length - limit} 个`)
            if (showCovered) {
                console.log(`    已覆盖: ${covered.map((m) => m.name).join(', ')}`)
            }
        }
        missingInModule += missing.length
    }
    totalMembers += membersInModule
    totalMissing += missingInModule
    console.log(
        `  小计: 声明 ${membersInModule} 个成员, 未点名 ${missingInModule} 个, 明确不做 ${excludedInModule} 个`,
    )
}

console.log(`\n合计: ${totalMembers} 个成员, 未点名 ${totalMissing} 个`)
