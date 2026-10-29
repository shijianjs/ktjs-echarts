# todo

跨会话进度跟踪。只写**当前状态、下一步和仍然生效的约定**；历史看 `git log`。项目约定见 `AGENTS.md`、
`TYPE-MAPPING.md` 与 `demo/AGENTS.md`，跨页注意事项见 `demo/NOTES.md`，这里不重复。

## 上下文

- 仓库：`ktjs-echarts`（`echarts` 与 `echarts-gl` 的 Kotlin/JS wrapper + demo 站点）。
- 上游资料一律读本地克隆（不要抓 GitHub / raw）：
  - `S:\workspace\github\apache\echarts`（6.1.0 附近的源码树）
  - `S:\workspace\github\apache\echarts-doc`（`option-gl/` 与 `option/partial/` 是 GL 选项文档）
  - `S:\workspace\github\apache\echarts-examples`（`public/examples/ts/` 是 demo 的移植来源）
  - `S:\workspace\github\ecomfe\echarts-gl`（GL 的 `defaultOption` 与视图实现）
  - `S:\workspace\github\JetBrains\kotlin-wrappers`（写法参考）
- 锁定版本：根 `package.json`（`echarts` 6.1.0、`echarts-gl` 2.1.0）；修订号在
  `wrapper-revisions.properties`（当前 `ktjs-echarts=7`、`ktjs-echarts-gl=8`）。
- 验证：`./gradlew build`（含 detekt）、`:ktjs-echarts:jsBrowserTest`、`:ktjs-echarts-gl:jsBrowserTest`、
  `:demo:e2eTest`；demo 覆盖统计用 `node scripts/demo-coverage.mjs`。

## 当前状态

### ktjs-echarts（6.1.0-kt.7）

类型建模 10 轮收尾（联合类型、字面量、数据项类型、`typedData` 视图、回调与事件 payload 等）已完成，
**没有未完成项**。以下是已确认「保持现状」的决定，不要重复修：

- `LegendOption` 保留可滚动图例的分页字段：`EChartsOption.legend` 是 `Array<LegendOption>`，拆子类型后
  调用方拿不到分页字段；属有意的更宽建模。
- `LegendDataItemOption.tooltip`、`CustomSeriesRenderItemParams.itemPayload` / `context` 的值用 `JsAny`：
  上游就是 `unknown` / `Dictionary<unknown>`。
- `MapSeriesOption.markPoint` / `markLine` / `markArea`、`ComposeOption` / `RegisteredSeriesOption`、
  上游的 `*ComponentOption` 别名、`ActionInfo.update`、`Payload.type`：Kotlin 侧统一用具名类型 / 更宽类型，
  理由见各接口 KDoc。
- series 级 `itemStyle` / `label` 上游的 `Omit<...> & { ... }` 收窄不另加 `typedItemStyle`：精确类型本身
  是基类型的子接口，调用方 `unsafeJso<精确类型> { }` 即可写，读回来按需 `unsafeCast`。
- `coordinateSystem` 等关键字收窄、继承来的 `var` 收窄沿用 `@JsName` 同名字段（`TYPE-MAPPING.md` §12）。

### ktjs-echarts-gl（2.1.0-kt.8）

- 已建模：`grid3D` / `xAxis3D|yAxis3D|zAxis3D`、`geo3D`、`globe`、`viewControl`、`light`、`postEffect`、
  shading 与材质，全部 11 个系列的选项、数据项联合与工厂，以及 `mapbox3D` / `maptalks3D` 组件选项和
  2D/3D 坐标系统、投影、blendMode 等字面量。
- 安装器齐全（11 个 chart + 5 个 component）；`echarts-gl` 的 `sideEffects` 列不全，安装器 getter 里读取
  `echarts-gl/lib/echarts-gl.js` 来加载核心模块。
- 浏览器测试：`GlSeries2DTest`（`scatterGL` / `linesGL` / `flowGL` / `graphGL`）与 `Map3DSeriesTest`；
  `mapbox3D` / `maptalks3D` 要第三方地图 SDK 和全局 token，只做类型不做渲染用例。
- 已确认的死字段（不建模）：`graphGL` 的 `roam` / `center`、`edgeSymbol` / `edgeSymbolSize`、
  `initLayout` / `stopThreshold`；hatching 材质只读 `hatchingTextures`，`paperColor` 无人读取；mapbox3D /
  maptalks3D 文档里的 `shading` 不会被读。

### demo

- 官方清单 436 条：**已移植 337 页 / 338 条，明确不做 51，待补 47**。逐条清单、理由与维护方式见
  [demo/COVERAGE.md](demo/COVERAGE.md)。
- 跨页注意事项（含 GL）见 [demo/NOTES.md](demo/NOTES.md)；移植约定、各家族做法与可复用数据见
  [demo/AGENTS.md](demo/AGENTS.md)。
- **明确不做的 51 条不再讨论**：bmap、外部 JS 库、官方站点图片素材、真实指针交互、渲染器开关、远端脚本、
  实例内部结构等，理由在 COVERAGE 里。新增排除项先更新 `scripts/demo-coverage.mjs` 的 `EXCLUDED`。

## 下一步

按 [demo/COVERAGE.md](demo/COVERAGE.md) 的「待补」清单推进，建议顺序：

1. **补地图数据**（待补里的 26 条）：`china.json` 一次解锁 `map-example`、`heatmap-map`、`geo-lines`、
   `geo-map-scatter`、`scatter-map`、`effectScatter-map`、`scatter-weibo` / `scattergl-weibo` 与
   `doc-example/scatter-visualMap-piecewise`；`HK.json` 解锁 `map-HK` 与两页 `map-visualMap-*`；再补 Sicily
   SVG、`links_ny_*.bin` / `gps_*.bin`。`USA.json`、`ch.geo.json`、`iceland.geo.json`、`world.json` 已在仓库里。
2. **补静态数据**（待补里的 20 条）：`scatter` 家族的时间轴、营养与 AQI 页面、`graphgl-*` 三页、
   `matrix-grid-layout` 与 geo-svg 的三页 `doc-example`；`bar-race-country` 还要补 emoji-flags CDN 数据。
   数据多能从 `public/data/asset/data` 直接拷。
3. **GL 子集可以单独一批**（都在上面两组里）：`map3D` 四页（`alcohol.json` / `buildings.json` + wood /
   redbricks 贴图）、`graphgl` 三页、`scatterGL-gps` / `linesGL-ny`（二进制轨迹）。
4. **先补 wrapper 再做**（1 条）：`doc-example/scatter-visualMap-categories` 需要按类目索引的 `visualMap`
   字典与 `left: null`。

新增/移植页面后跑 `node scripts/demo-coverage.mjs --write` 更新 COVERAGE。

## 收尾清单

- `./gradlew build`、`:ktjs-echarts-gl:jsBrowserTest`、`:demo:e2eTest` 全过；截图目视至少一个新 GL 页面。
- 改了 wrapper 公开代码就提升对应模块的修订号，并跑
  `./gradlew kotlinUpgradePackageLock --rerun-tasks` 提交锁文件（增量构建下该任务会误报 UP-TO-DATE）。
- 每批一个 `feat(...)` 提交并推送到 `origin/main`。
