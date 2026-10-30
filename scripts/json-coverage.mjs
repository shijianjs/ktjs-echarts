#!/usr/bin/env node
// 统计 demo 的 JSON 模式覆盖情况，并把结果写回 demo/COVERAGE-JSON.md。
//
// 目标集合是 demo 里用 `chart("id")` 声明的静态页面：这些页面能用
// `kmp-echarts-json` 序列化出的 JSON 重绘，顶栏的 Kotlin/JSON 开关对它们生效。
// `interactive("id")` 页面由回调、定时器或用户操作驱动，没有 JSON 对应物，不计入目标。
//
// 用法：
//   node scripts/json-coverage.mjs            校验并打印统计
//   node scripts/json-coverage.mjs --write    同时更新 demo/COVERAGE-JSON.md 的标记区块

import fs from 'node:fs'
import path from 'node:path'
import process from 'node:process'
import { fileURLToPath } from 'node:url'

const repoRoot = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..')
const demoExamples = path.join(repoRoot, 'demo', 'src', 'jsMain', 'kotlin', 'demo', 'examples')
const jsonExamples = path.join(repoRoot, 'demo', 'src', 'commonMain', 'kotlin', 'demo', 'json', 'examples')
const docPath = path.join(repoRoot, 'demo', 'COVERAGE-JSON.md')

/** 明确不做 JSON 的静态页面。每个条目的 reason 是整组共用的理由。 */
const EXCLUDED = [
    {
        title: 'GL / 3D',
        reason: '`echarts-gl` 的 3D 选项没有建模，JSON 模型只覆盖内置 2D 系列。',
        ids: [
            'bar3d-punch-card',
            'globe-atmosphere',
            'globe-displacement',
            'globe-echarts-gl-hello-world',
            'globe-layers',
            'globe-moon',
            'graphgl-gpu-layout',
            'line3d-orthographic',
            'metal-surface',
            'parametric-surface-rose',
            'simple-surface',
            'sphere-parametric-surface',
            'surface-breather',
            'surface-golden-rose',
            'surface-mollusc-shell',
            'surface-theme-roses',
            'surface-wave',
            'transparent-bar3d',
        ],
    },
    {
        title: 'custom 系列回调',
        reason: '`series.type = "custom"` 必须提供 `renderItem` 函数，JSON 无法表达回调。',
        ids: [
            'custom-bar-trend',
            'custom-calendar-icon',
            'custom-cartesian-polygon',
            'custom-error-bar',
            'custom-error-scatter',
            'custom-polar-heatmap',
            'custom-profit',
            'cycle-plot',
        ],
    },
    {
        title: 'matrix 组件',
        reason: 'ECharts 6 的 `matrix` 组件（x/y 轴、单元格合并）尚未建模。',
        ids: [
            'matrix-confusion',
            'matrix-correlation-heatmap',
            'matrix-correlation-scatter',
            'matrix-covariance',
            'matrix-graph',
            'matrix-grid-layout',
            'matrix-pie',
            'matrix-simple',
            'matrix-mini-bar-data-collection',
            'matrix-sparkline',
            'doc-example/matrix-graph-box',
        ],
    },
    {
        title: '时间轴 / 动画循环',
        reason: '页面由 `timeline` 或 `setInterval` 持续驱动，静态 JSON 只能画出第一帧。',
        ids: [
            'doc-example/mix-timeline-all',
            'doc-example/timeline-dynamic-series',
            'mix-timeline-finance',
            'graphic-loading',
            'graphic-stroke-animation',
            'watermark',
        ],
    },
    {
        title: '函数 / 图形回调',
        reason: '数据或图形由函数生成，没有 JSON 形式：`line-function` 用闭包算曲线，`line-graphic` 用 graphic 元素拼图。',
        ids: ['line-function', 'line-graphic', 'bar-animation-delay'],
    },
    {
        title: '坐标轴断点',
        reason: '`axis.breaks` / `breakArea` / `breakLabelLayout` 尚未建模，页面核心就是断轴。',
        ids: ['bar-breaks-simple', 'intraday-breaks-1', 'intraday-breaks-2'],
    },
    {
        title: '官方站点图片 / 纹理',
        reason: '图表依赖官方站点托管的图片或纹理资源，站点不搬运。',
        ids: ['pie-rich-text'],
    },
    {
        title: 'SVG 地图资源',
        reason: '页面先注册 SVG 地图并依赖 `geo` 的 SVG 布局，站点没有这些地图数据。',
        ids: [
            'doc-example/geo-svg-label-basic',
            'doc-example/geo-svg-layout-basic',
            'doc-example/geo-svg-named-basic',
        ],
    },
]

function fail(message) {
    console.error(`json-coverage: ${message}`)
    process.exit(1)
}

/** demo 里 `chart(...)` 与 `interactive(...)` 的声明。 */
function demoIds() {
    const chart = new Set()
    const interactive = new Set()
    for (const name of fs.readdirSync(demoExamples)) {
        if (!name.endsWith('.kt')) continue
        const source = fs.readFileSync(path.join(demoExamples, name), 'utf8')
        for (const match of source.matchAll(/\.(chart|interactive)\(\s*"([^"]+)"/g)) {
            ;(match[1] === 'chart' ? chart : interactive).add(match[2])
        }
    }
    return { chart, interactive }
}

/** `JsonCategory(...).example("id")` 的声明。 */
function jsonIds() {
    const ids = new Set()
    for (const name of fs.readdirSync(jsonExamples)) {
        if (!name.endsWith('.kt')) continue
        const source = fs.readFileSync(path.join(jsonExamples, name), 'utf8')
        for (const match of source.matchAll(/\.example\(\s*"([^"]+)"/g)) ids.add(match[1])
    }
    return ids
}

function classify(chart, interactive, json) {
    const excluded = []
    const seen = new Set()
    const alreadyCovered = []
    for (const group of EXCLUDED) {
        const ids = []
        for (const id of group.ids) {
            if (seen.has(id)) fail(`${id} 在分类里出现了两次。`)
            if (!chart.has(id)) fail(`${group.title} 里的 ${id} 不是 demo 的静态图表页。`)
            if (json.has(id)) {
                alreadyCovered.push(id)
                continue
            }
            seen.add(id)
            ids.push(id)
        }
        excluded.push({ ...group, ids })
    }

    const unknown = [...json].filter((id) => !chart.has(id))
    if (unknown.length > 0) {
        fail(`这些 JSON 示例没有对应的静态页面（或 id 写错）：\n  ${unknown.join('\n  ')}`)
    }
    const missing = [...chart].filter((id) => !json.has(id) && !seen.has(id))
    if (missing.length > 0) {
        fail(`还有 ${missing.length} 个静态页面既没有 JSON 示例也没分类：\n  ${missing.join('\n  ')}`)
    }

    const covered = [...chart].filter((id) => json.has(id))
    const excludedCount = excluded.reduce((sum, group) => sum + group.ids.length, 0)
    if (covered.length + excludedCount !== chart.size) {
        fail('统计不一致：静态页面数不等于已覆盖 + 明确不做。')
    }
    return { covered, excluded, excludedCount, interactive, alreadyCovered }
}

function escapeCell(text) {
    return text.replaceAll('|', '\\|')
}

function table(headers, rows) {
    return [
        `| ${headers.join(' | ')} |`,
        `| ${headers.map(() => '---').join(' | ')} |`,
        ...rows.map((row) => `| ${row.map(escapeCell).join(' | ')} |`),
    ].join('\n')
}

function render(chart, json, result) {
    const lines = []
    lines.push('<!-- coverage-json:begin -->')
    lines.push('')
    lines.push('## 统计')
    lines.push('')
    lines.push(
        table(
            ['项', '数量'],
            [
                ['静态页面（`chart(...)`）', String(chart.size)],
                ['已提供 JSON 示例', String(result.covered.length)],
                ['明确不做', String(result.excludedCount)],
                ['交互页面（`interactive(...)`，不适用）', String(result.interactive.size)],
                ['JSON 示例总数', String(json.size)],
            ],
        ),
    )
    lines.push('')
    lines.push(
        '目标集合是 `chart(...)` 静态页面：顶栏的 Kotlin/JSON 开关只对它们生效。' +
            '`interactive(...)` 页面由回调、定时器或用户操作驱动，没有 JSON 对应物。',
    )
    lines.push('')
    lines.push(`## 明确不做（${result.excludedCount}）`)
    for (const group of result.excluded) {
        lines.push('')
        lines.push(`### ${group.title}（${group.ids.length}）`)
        lines.push('')
        lines.push(group.reason)
        lines.push('')
        lines.push(table(['id'], group.ids.map((id) => [`\`${id}\``])))
    }
    lines.push('')
    lines.push('## 待补（0）')
    lines.push('')
    lines.push('每个静态页面要么有 JSON 示例，要么在上面列了理由；出现新的 `chart(...)` 页面而没有对应示例时脚本会报错。')
    lines.push('')
    lines.push('<!-- coverage-json:end -->')
    return lines.join('\n')
}

function main() {
    const { chart, interactive } = demoIds()
    const json = jsonIds()
    const result = classify(chart, interactive, json)
    console.log(
        `静态页面 ${chart.size}，JSON 示例 ${json.size}（覆盖 ${result.covered.length}），` +
            `明确不做 ${result.excludedCount}，交互页面 ${interactive.size}。`,
    )
    if (result.alreadyCovered.length > 0) {
        console.log(`以下条目已经有 JSON 示例，已从分类里剔除：${result.alreadyCovered.join(', ')}`)
    }

    const block = render(chart, json, result)
    if (process.argv.includes('--write')) {
        const doc = fs.readFileSync(docPath, 'utf8')
        const begin = doc.indexOf('<!-- coverage-json:begin -->')
        const end = doc.indexOf('<!-- coverage-json:end -->')
        if (begin < 0 || end < begin) fail(`${docPath} 缺少 coverage-json 标记区块。`)
        fs.writeFileSync(
            docPath,
            doc.slice(0, begin) + block + doc.slice(end + '<!-- coverage-json:end -->'.length),
        )
        console.log(`已更新 ${path.relative(repoRoot, docPath)}`)
    }
}

main()
