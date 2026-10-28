# 上游 `src/export/` 导出清单

基准：本地只读快照 `/refs/echarts`（`package.json` 版本 `6.1.0`）。上游新增文件或导出后本表会过期，重新跑一遍统计即可。

统计口径：只数 `apache/echarts` 仓库 `/refs/echarts/src/export/` 下每个 `.ts` 文件自己写出或转出的导出名——`export { a as b }` 记 `b`、`export function` / `const` / `class` / `interface` / `type` 各记 1 条、`export * as ns from` 记 `ns`；`export * from "…"` 的成员静态不可数，每条这样的语句记 1 条并注明来源；注释掉的 `export` 与只 `import` 不导出的名字不计。

| 文件 | 导出数量 | 导出名 |
| --- | ---: | --- |
| `option.ts` | 78 | `SliderDataZoomComponentOption`, `InsideDataZoomComponentOption`, `ContinuousVisualMapComponentOption`, `PiecewiseVisualMapComponentOption`, `PlainLegendComponentOption`, … |
| `components.ts` | 56 | `GridSimpleComponent`, `GridComponent`, `PolarComponent`, `RadarComponent`, `GeoComponent`, … |
| `charts.ts` | 46 | `LineChart`, `BarChart`, `PieChart`, `ScatterChart`, `RadarChart`, … |
| `api/graphic.ts` | 32 | `extendShape`, `extendPath`, `makePath`, `makeImage`, `mergePath`, … |
| `api.ts` | 29 | `zrender`, `matrix`, `vector`, `zrUtil`, `color`, … |
| `core.ts` | 22 | `Color`, `Payload`, `ECElementEvent`, `HighlightPayload`, `DownplayPayload`, … |
| `api/number.ts` | 19 | `linearMap`, `round`, `asc`, `getPrecision`, `getPrecisionSafe`, … |
| `api/util.ts` | 16 | `map`, `each`, `indexOf`, `inherits`, `reduce`, … |
| `api/format.ts` | 10 | `addCommas`, `toCamelCase`, `normalizeCssArray`, `encodeHTML`, `formatTpl`, … |
| `api/helper.ts` | 10 | `getLayoutRect`, `createDimensions`, `createSymbol`, `getECData`, `enableHoverEmphasis`, … |
| `features.ts` | 5 | `UniversalTransition`, `LabelLayout`, `AxisBreak`, `LegacyGridContainLabel`, `ScatterJitter` |
| `api/time.ts` | 3 | `parse`, `format`, `roundTime` |
| `all.ts` | 2 | `*` re-export from `./core`, `*` re-export from `./option` |
| `renderers.ts` | 2 | `SVGRenderer`, `CanvasRenderer` |

合计 330 条，其中 `export *` 形式的 re-export 4 条（`all.ts` 与 `core.ts` 的整包转发，实际名字要展开到 `./core`、`./option`、`../core/echarts`、`./api` 才数得清，本轮未展开）。

看覆盖差距：这张表是「上游暴露了哪些名字」的范围清单，不是 `ktjs-echarts` 的覆盖率——`charts.ts` / `components.ts` / `renderers.ts` / `features.ts` 是一行一个 `install as Xxx`，可以和 `EChartsCharts` / `EChartsComponents` / `EChartsRenderers` / `EChartsFeatures` 里的属性逐个对账；`option.ts` 里成排的 `XxxComponentOption` / `XxxSeriesOption` 在 Kotlin 侧按语义并成了 `XxxOption` 接口，名字对得上不代表同一个东西、名字对不上也不代表没覆盖，只适合拿来找「哪个入口值得先查」。
