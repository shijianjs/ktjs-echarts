#!/usr/bin/env node
// 按字段核对 wrapper 声明在浏览器用例里的覆盖情况，找出「类型写了、字段没人读过」的缺口。
//
// AGENTS.md 的「测试覆盖」要求 `src/jsMain` 里的每个声明都能被用例点到名。只看类型名不够：
// `unsafeJso { }` 的接收者类型是推断出来的，测试文件里根本不出现类型名，按名字粗筛会大量误报；
// 而一个 `external interface` 里真正需要核对的是它的成员——上游读的是键名，写错键名时类型化读写
// 依然自洽，所以只有「这个字段有没有被任何用例写过/读过」才是有效信号。
//
// 判定方式：把 jsMain 里每个 `external interface` 的成员名收集起来，再看 jsTest 全量词标记不记这个
// 名字。带 `@JsName("x")` 的成员，Kotlin 名与 JS 键名 `x` 任一出现即算覆盖；反过来，一个裸键名成员
// 只要同名的 JS 键被别的带 `@JsName` 的成员覆盖，也算覆盖——`typedCoordinateSystem` 写过，
// `coordinateSystem` 就是同一个键，不算缺口。像 `show`、`name` 这种到处都出现的成员名，在这个口径下
// 几乎总是「已覆盖」，所以输出适合用来找**从没出现过的成员**，不能当逐用例的覆盖率用。
//
// 用法：
//   node scripts/field-coverage.mjs                     所有发布模块
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


/** 成员声明行：`var` / `val` / `fun`，允许 `override`、修饰符与 `@JsName` 在前。 */
const MEMBER_PATTERN =
    /^\s*(?:@\w+(?:\([^)]*\))?\s+)*(?:external\s+|override\s+|abstract\s+|open\s+|suspend\s+)*var\s+(\w+)|^\s*(?:@\w+(?:\([^)]*\))?\s+)*(?:external\s+|override\s+|abstract\s+|open\s+|suspend\s+)*fun\s+(?:<[^>]*>\s+)?(\w+)/

/** 接口声明行，`external interface` 与内部 `interface` 都算（外部声明是审计对象）。 */
const INTERFACE_PATTERN = /^\s*(?:@\w+(?:\([^)]*\))?\s+)*(?:public\s+|private\s+|internal\s+|external\s+|sealed\s+|abstract\s+)*interface\s+(\w+)/

const FUN_PATTERN = /^\s*(?:@\w+(?:\([^)]*\))?\s+)*(?:public\s+|private\s+|internal\s+|external\s+|inline\s+)*fun\s+(?:<[^>]*>\s+)?(\w+)\s*\(/

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
 * jsTest 里出现过的全部词标记。
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
 */
function parseFile(file) {
    const source = fs.readFileSync(file, 'utf8')
    const lines = source.split('\n')
    const members = []
    let interfaceName = null
    let braceDepth = 0
    let interfaceDepth = null
    for (let index = 0; index < lines.length; index += 1) {
        const line = lines[index]
        const interfaceMatch = INTERFACE_PATTERN.exec(line)
        if (interfaceMatch && braceDepth === 0) {
            interfaceName = interfaceMatch[1]
        }
        const funMatch = FUN_PATTERN.exec(line)
        if (funMatch && braceDepth === 0) {
            // 顶层工厂函数（联合类型包装、构建器）与 object 里的静态函数一起按名字核对。
            members.push({ type: interfaceName ?? path.basename(file, '.kt'), name: funMatch[1], jsName: null, line: index + 1 })
        }
        if (!funMatch) {
            const memberMatch = MEMBER_PATTERN.exec(line)
            const name = memberMatch && (memberMatch[1] ?? memberMatch[2])
            if (name) {
                // `@JsName` 可能写在上一行，也可能与声明同一行。
                const context = `${index > 0 ? lines[index - 1] : ''} ${line}`
                const jsName = /@JsName\("([^"]+)"\)/.exec(context)
                members.push({
                    type: interfaceName ?? path.basename(file, '.kt'),
                    name,
                    jsName: jsName ? jsName[1] : null,
                    line: index + 1,
                })
            }
        }
        for (const char of line) {
            if (char === '{') braceDepth += 1
            else if (char === '}') {
                braceDepth -= 1
                if (interfaceDepth !== null && braceDepth < interfaceDepth) {
                    interfaceName = null
                    interfaceDepth = null
                }
            }
        }
        if (interfaceName !== null && interfaceDepth === null && line.includes('{')) interfaceDepth = braceDepth
    }
    return { members }
}

const argv = process.argv.slice(2)
const limitIndex = argv.indexOf('--limit')
const limit = limitIndex >= 0 ? Number(argv[limitIndex + 1]) : Infinity
const showCovered = argv.includes('--show-covered')
const moduleArgs = argv.filter((arg) => !arg.startsWith('--') && arg !== String(limit))

const published = fs
    .readdirSync(repoRoot)
    .filter((name) => name.startsWith('ktjs-echarts') && fs.statSync(path.join(repoRoot, name)).isDirectory())
const modules = moduleArgs.length > 0 ? moduleArgs : published

let totalMembers = 0
let totalMissing = 0
for (const moduleName of modules) {
    const mainDir = path.join(repoRoot, moduleName, 'src', 'jsMain', 'kotlin')
    const testDir = path.join(repoRoot, moduleName, 'src', 'jsTest', 'kotlin')
    const testFiles = kotlinFiles(testDir)
    if (testFiles.length === 0) {
        console.log(`${moduleName}: 没有 jsTest 源码，跳过`)
        continue
    }
    const tokens = tokensOf(testFiles)
    console.log(`\n## ${moduleName}（用例文件 ${testFiles.length} 个）\n`)
    const parsed = kotlinFiles(mainDir).map((file) => ({ file, members: parseFile(file).members }))
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
