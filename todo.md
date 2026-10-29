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
  `wrapper-revisions.properties`（当前 `ktjs-echarts=11`、`ktjs-echarts-gl=9`）。
- 验证：`./gradlew build`（含 detekt）、`:ktjs-echarts:jsBrowserTest`、`:ktjs-echarts-gl:jsBrowserTest`、
  `:demo:e2eTest`；demo 覆盖统计用 `node scripts/demo-coverage.mjs`。

## 当前状态

### ktjs-echarts（6.1.0-kt.11）

`-kt.11` 补了二进制轨迹页需要的两处扁平数据视图：`LinesSeriesOption.typedFlatData`（`data` 的
`ArrayLike<number>` 分支，`polyline` 时每行 `[count, x0, y0, ...]`，配 `dimensions`）与
`AppendDataParams.typedFlatData`（同一分支的 `appendData` 入口，分片追加百万级轨迹就靠它）。
`-kt.10` 补了两处地图批次需要的公开 API：`GeoOption` 混入 `ComponentOption` 的
`coordinateSystem` / `coordinateSystemUsage` / `coord`、`BoxLayoutOptionMixin` 与
`AnimationOptionMixin`（`geo` 要能钉进矩阵格子并关掉自身动画），以及 `PieSeriesOption.typedCenter`
（`center` 的标量分支，饼图落在 `geo` 上时用区名当地心）。`-kt.9` 给 `ScatterSeriesOption` 加了 `typedFlatData`（与 `typedData` 同一 `data` 属性的扁平视图，
`ArrayLike` 分支，配 `dimensions` 使用），`scatter-nebula` 的 8 MB 浮点表就靠它直接交给 `large` 模式。
`-kt.8` 只补了 demo 需要的两处公开 API：`ZRenderType.configLayer` / `ZRLayerConfig`（尾迹模糊）和
`TimelineOption` 的 `width` / `height` / `tooltip` / `emphasis`。类型建模 10 轮收尾（联合类型、字面量、
数据项类型、`typedData` 视图、回调与事件 payload 等）已完成，**没有未完成项**。以下是已确认「保持现状」
的决定，不要重复修：

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

### ktjs-echarts-gl（2.1.0-kt.9）

`-kt.9` 给 `LinesGLSeriesOption` 与 `ScatterGLSeriesOption` 各加了 `typedFlatData`（与 `typedData`
同一 `data` 属性的 `ArrayLike<number>` 视图），`linesGL-ny` / `scatterGL-gps` 据此把百万级点与折线
按分片交给 `appendData`。

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

- 官方清单 436 条：**已移植 370 页 / 371 条，明确不做 51，待补 14**。逐条清单、理由与维护方式见
  [demo/COVERAGE.md](demo/COVERAGE.md)。
- 跨页注意事项（含 GL）见 [demo/NOTES.md](demo/NOTES.md)；移植约定、各家族做法与可复用数据见
  [demo/AGENTS.md](demo/AGENTS.md)。
- **明确不做的 51 条不再讨论**：bmap、外部 JS 库、官方站点图片素材、真实指针交互、渲染器开关、远端脚本、
  实例内部结构等，理由在 COVERAGE 里。新增排除项先更新 `scripts/demo-coverage.mjs` 的 `EXCLUDED`。

## 下一步

按 [demo/COVERAGE.md](demo/COVERAGE.md) 的「待补」清单推进（14 条），建议顺序：

1. **补 `china.json`**（一次解锁 13 条）：`doc-example/map-example`、四个 `doc-example/map-visualMap-*`、
   `doc-example/scatter-visualMap-piecewise`、`heatmap-map`、`geo-lines`、`geo-map-scatter`、`scatter-map`、
   `effectScatter-map`、`scatter-weibo`、`scattergl-weibo`。本地克隆里没有 `china.json`（上游地图数据早已
   外置，`echarts/map/js` 也没有），要么从官方站点下载一次并登记到 `data/SOURCE.md`，要么改注册
   `world.json` / 其他已入库地图的等价页面——先确认数据来源再动手。
2. **先补 wrapper 再做**（1 条）：`doc-example/scatter-visualMap-categories` 需要按类目索引的 `visualMap`
   字典与 `left: null`。

新增/移植页面后跑 `node scripts/demo-coverage.mjs --write` 更新 COVERAGE。

## 收尾清单

- `./gradlew build`、`:ktjs-echarts-gl:jsBrowserTest`、`:demo:e2eTest` 全过；截图目视至少一个新 GL 页面。
- 改了 wrapper 公开代码就提升对应模块的修订号，并跑
  `./gradlew kotlinUpgradePackageLock --rerun-tasks` 提交锁文件（增量构建下该任务会误报 UP-TO-DATE）。
- 每批一个 `feat(...)` 提交并推送到 `origin/main`。
