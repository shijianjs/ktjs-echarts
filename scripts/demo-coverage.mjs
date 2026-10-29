#!/usr/bin/env node
// 统计 demo 对官方示例清单的覆盖情况，并把结果写回 demo/COVERAGE.md。
//
// 官方清单取 echarts-examples 的 src/data/chart-list-data.js 与 chart-list-data-gl.js，
// 已移植页面取 demo/src/jsMain/kotlin/demo/examples 里 `chart("id")` / `interactive("id")` 的声明。
//
// 用法：
//   node scripts/demo-coverage.mjs            校验并打印统计
//   node scripts/demo-coverage.mjs --write    同时更新 demo/COVERAGE.md 的标记区块
//
// 上游克隆位置默认是 <repo>/../../apache/echarts-examples，可用 ECHARTS_EXAMPLES 覆盖。

import fs from 'node:fs'
import path from 'node:path'
import process from 'node:process'
import { fileURLToPath } from 'node:url'

const repoRoot = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..')
const upstream =
    process.env.ECHARTS_EXAMPLES ?? path.resolve(repoRoot, '..', '..', 'apache', 'echarts-examples')
const docPath = path.join(repoRoot, 'demo', 'COVERAGE.md')
const examplesDir = path.join(repoRoot, 'demo', 'src', 'jsMain', 'kotlin', 'demo', 'examples')

/** 官方 id 与 demo 路由 id 不同的条目：路由 id -> 它同时覆盖的官方 id。 */
const ALIASES = {
    'bar-large': ['doc-example/bar-large'],
    'pie-highlight': ['doc-example/pie-highlight'],
}

/** 明确不做。每个条目的 reason 是整组共用的理由。 */
const EXCLUDED = [
    {
        title: '百度地图（bmap）',
        reason: 'wrapper 不覆盖百度地图 SDK，按选例策略排除。',
        ids: [
            'effectScatter-bmap',
            'global-wind-visualization',
            'global-wind-visualization-2',
            'heatmap-bmap',
            'lines-bmap',
            'lines-bmap-bus',
            'lines-bmap-effect',
            'map-bin',
            'map-polygon',
        ],
    },
    {
        title: '外部 JS 库生成数据',
        reason:
            '数据由 ecStat / d3 在浏览器里算出来（GL 页经 CDN 的 `$.getScript` 加载 simplex-noise），' +
            '站点不引这些包。',
        ids: [
            'bar-histogram',
            'data-transform-aggregate',
            'scatter-clustering',
            'scatter-clustering-process',
            'scatter-exponential-regression',
            'scatter-linear-regression',
            'scatter-logarithmic-regression',
            'scatter-polynomial-regression',
            'circle-packing-with-d3',
            'animating-contour-on-globe',
            'globe-contour-paint',
            'map-usa-projection',
            'bar3d-noise-modified-from-marpi-demo',
            'bar3d-simplex-noise',
            'flowGL-noise',
            'metal-bar3d',
            'scatter3d-orthographic',
            'scatter3d-simplex-noise',
            'stacked-bar3d',
        ],
    },
    {
        title: '官方站点图片 / 纹理素材',
        reason: '图标、纹理或 base64 图只存在于官方站点，按选例策略不搬运。',
        ids: [
            'doc-example/axisPointer-handle-image',
            'doc-example/pictorialBar-graphicType',
            'doc-example/pictorialBar-patternSize',
            'doc-example/pictorialBar-repeatDirection',
            'doc-example/pictorialBar-repeatLayout',
            'pictorialBar-forest',
            'pictorialBar-hill',
            'pictorialBar-spirit',
            'pie-pattern',
            'iron-globe',
        ],
    },
    {
        title: '依赖真实指针 / 触摸交互',
        reason:
            '页面演示的就是拖拽、触摸或刷选本身，内容要用户操作后才出现；静态页面与 e2e 无法稳定复现。',
        ids: [
            'bar-breaks-brush',
            'candlestick-brush',
            'custom-gantt-flight',
            'line-draggable',
            'line-fisheye-lens',
            'line-pen',
            'scatter-map-brush',
            'doc-example/geo-roam-indicator',
            'doc-example/graph-roam-indicator',
        ],
    },
    {
        title: '站点能力未提供',
        reason: 'canvas-vs-svg 需要先给站点加渲染器开关才有意义。',
        ids: ['doc-example/canvas-vs-svg', 'doc-example/canvas-vs-svg-en'],
    },
    {
        title: '运行期远端脚本',
        reason: '示例用 `$.getScript` 拉官方站点的 timelineGDP.js 与 draggable.js。',
        ids: ['doc-example/bar-media-timeline'],
    },
    {
        title: '实例内部结构',
        reason:
            "靠 getModel().getComponent('geo').coordinateSystem.regions 取区域中心，wrapper 不建模；" +
            'world.json 也只有 6 个国家带 cp。',
        ids: ['globe-country-carousel'],
    },
]

/** 待补。按「还缺什么」分组。 */
const PENDING = [
    {
        title: '只缺静态数据 / 素材',
        reason:
            '数据是静态文件或示例内联的数组，素材大多已在 `demo/src/jsMain/resources/data/` 里，' +
            '补齐后即可移植。',
        ids: [
            'bar-race-country',
            'mix-timeline-finance',
            'mix-zoom-on-value',
            'calendar-lunar',
            'candlestick-touch',
            'custom-ohlc',
            'custom-gauge',
            'wind-barb',
            'graphgl-gpu-layout',
            'graphgl-large-internet',
            'graphgl-npm-dep',
            'line-race',
            'parallel-nutrients',
            'scatter-aqi-color',
            'scatter-life-expectancy-timeline',
            'scatter-nebula',
            'scatter-nutrients',
            'scatter-nutrients-matrix',
            'scatter-punchCard',
            'scatter-stream-visual',
            'scatter-symbol-morph',
            'scatter-aggregate-bar',
            'sankey-itemstyle',
            'treemap-drill-down',
            'treemap-show-parent',
            'treemap-sunburst-transition',
            'treemap-visual',
            'matrix-grid-layout',
            'doc-example/geo-svg-label-basic',
            'doc-example/geo-svg-layout-basic',
            'doc-example/geo-svg-named-basic',
        ],
    },
    {
        title: '缺示例自带的数据',
        reason:
            '地图本身（china / world / USA / Iceland / 瑞士 / HK / SVG）都已入库；剩下的缺示例自带的数据：' +
            '六页要上游内联的 364 城市坐标表，两页要 `weibo.json`（本地克隆里有）。',
        ids: [
            'doc-example/map-example',
            'doc-example/map-visualMap-continuous',
            'doc-example/map-visualMap-continuous-text',
            'doc-example/map-visualMap-pieces',
            'doc-example/map-visualMap-piecewise-text',
            'doc-example/scatter-visualMap-piecewise',
            'heatmap-map',
            'geo-lines',
            'geo-map-scatter',
            'scatter-map',
            'effectScatter-map',
            'geo-svg-map',
            'map-HK',
            'map-usa',
            'map-bar-morph',
            'map-iceland-pie',
            'matrix-mini-bar-geo',
            'scatter-weibo',
            'scattergl-weibo',
            'lines-ny',
            'linesGL-ny',
            'scatterGL-gps',
            'map3d-alcohol-consumption',
            'map3d-buildings',
            'map3d-wood-city',
            'map3d-wood-map',
        ],
    },
    {
        title: '需要先补 wrapper',
        reason: '`inRange.symbol` / `outOfRange.symbolSize` 是按类目索引的字典，且 `left` 允许 `null`。',
        ids: ['doc-example/scatter-visualMap-categories'],
    },
]

function fail(message) {
    console.error(`demo-coverage: ${message}`)
    process.exit(1)
}

function readManifest(name) {
    const file = path.join(upstream, 'src', 'data', name)
    if (!fs.existsSync(file)) {
        fail(`找不到 ${file}；用 ECHARTS_EXAMPLES 指向 echarts-examples 克隆。`)
    }
    const source = fs.readFileSync(file, 'utf8')
    const start = source.indexOf('[', source.indexOf('export default'))
    const end = source.lastIndexOf(']')
    if (start < 0 || end < start) fail(`${file} 不是预期的 export default [...] 格式。`)
    return JSON.parse(source.slice(start, end + 1))
}

function portedIds() {
    const ids = new Set()
    for (const name of fs.readdirSync(examplesDir)) {
        if (!name.endsWith('.kt')) continue
        const source = fs.readFileSync(path.join(examplesDir, name), 'utf8')
        for (const match of source.matchAll(/\.(?:chart|interactive)\(\s*"([^"]+)"/g)) {
            ids.add(match[1])
        }
    }
    return ids
}

function classify(officialIds, routes) {
    const official = new Set(officialIds)
    const covered = new Set()
    for (const route of routes) {
        if (official.has(route)) covered.add(route)
        for (const alias of ALIASES[route] ?? []) {
            if (official.has(alias)) covered.add(alias)
        }
    }
    const missing = officialIds.filter((id) => !covered.has(id))

    const excluded = []
    const pending = []
    const seen = new Set()
    const alreadyPorted = []
    const add = (group) => {
        const ids = []
        for (const id of group.ids) {
            if (!official.has(id)) fail(`${group.title} 里的 ${id} 不在官方清单里。`)
            if (seen.has(id)) fail(`${id} 在分类里出现了两次。`)
            if (covered.has(id)) {
                alreadyPorted.push(id)
                continue
            }
            seen.add(id)
            ids.push(id)
        }
        return { ...group, ids }
    }
    for (const group of EXCLUDED) excluded.push(add(group))
    for (const group of PENDING) pending.push(add(group))

    const unclassified = missing.filter((id) => !seen.has(id))
    if (unclassified.length > 0) {
        fail(`还有 ${unclassified.length} 条既没移植也没分类：\n  ${unclassified.join('\n  ')}`)
    }
    return { covered, missing, excluded, pending, alreadyPorted }
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

function render(official2d, officialGl, routes, result) {
    const official = [...official2d, ...officialGl]
    const excludedCount = result.excluded.reduce((sum, group) => sum + group.ids.length, 0)
    const pendingCount = result.pending.reduce((sum, group) => sum + group.ids.length, 0)
    const lines = []
    lines.push('<!-- coverage:begin -->')
    lines.push('')
    lines.push('## 统计')
    lines.push('')
    lines.push(
        table(
            ['项', '数量'],
            [
                ['官方清单', String(official.length)],
                ['　· `chart-list-data.js`', String(official2d.length)],
                ['　· `chart-list-data-gl.js`', String(officialGl.length)],
                ['已移植', `${routes.size} 页 / ${result.covered.size} 条`],
                ['明确不做', String(excludedCount)],
                ['待补', String(pendingCount)],
            ],
        ),
    )
    lines.push('')
    lines.push(
        `已移植数里的「页」不含总览页（\`#/overview\`）；「条」把 \`bar-large\` / \`pie-highlight\` ` +
            '同时算作对应 `doc-example/` 条目的移植。',
    )
    lines.push('')
    lines.push(`## 明确不做（${excludedCount}）`)
    for (const group of result.excluded) {
        lines.push('')
        lines.push(`### ${group.title}（${group.ids.length}）`)
        lines.push('')
        lines.push(group.reason)
        lines.push('')
        lines.push(table(['id'], group.ids.map((id) => [`\`${id}\``])))
    }
    lines.push('')
    lines.push(`## 待补（${pendingCount}）`)
    for (const group of result.pending) {
        // A category the ports have since emptied leaves nothing to read, so it is dropped rather
        // than rendered as a header over an empty table.
        if (group.ids.length === 0) {
            continue
        }
        lines.push('')
        lines.push(`### ${group.title}（${group.ids.length}）`)
        lines.push('')
        lines.push(group.reason)
        lines.push('')
        lines.push(table(['id'], group.ids.map((id) => [`\`${id}\``])))
    }
    lines.push('')
    lines.push('<!-- coverage:end -->')
    return lines.join('\n')
}

function main() {
    const official2d = readManifest('chart-list-data.js')
    const officialGl = readManifest('chart-list-data-gl.js')
    const officialIds = [...official2d, ...officialGl].map((entry) => entry.id)
    if (new Set(officialIds).size !== officialIds.length) fail('官方清单里有重复 id。')

    const routes = portedIds()
    const result = classify(officialIds, routes)
    const excludedCount = result.excluded.reduce((sum, group) => sum + group.ids.length, 0)
    const pendingCount = result.pending.reduce((sum, group) => sum + group.ids.length, 0)
    console.log(
        `官方 ${officialIds.length} 条，已移植 ${routes.size} 页 / ${result.covered.size} 条，` +
            `明确不做 ${excludedCount}，待补 ${pendingCount}。`,
    )
    if (result.alreadyPorted.length > 0) {
        console.log(`以下条目已经移植，已从分类里剔除：${result.alreadyPorted.join(', ')}`)
    }

    const block = render(official2d, officialGl, routes, result)
    if (process.argv.includes('--write')) {
        const doc = fs.readFileSync(docPath, 'utf8')
        const begin = doc.indexOf('<!-- coverage:begin -->')
        const end = doc.indexOf('<!-- coverage:end -->')
        if (begin < 0 || end < begin) fail(`${docPath} 缺少 coverage 标记区块。`)
        fs.writeFileSync(docPath, doc.slice(0, begin) + block + doc.slice(end + '<!-- coverage:end -->'.length))
        console.log(`已更新 ${path.relative(repoRoot, docPath)}`)
    }
}

main()
