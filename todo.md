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
  `wrapper-revisions.properties`（当前 `ktjs-echarts=12`、`ktjs-echarts-gl=10`）。
- 验证：`./gradlew build`（含 detekt）、`:ktjs-echarts:jsBrowserTest`、`:ktjs-echarts-gl:jsBrowserTest`、
  `:demo:e2eTest`；demo 覆盖统计用 `node scripts/demo-coverage.mjs`。

## 当前状态

### ktjs-echarts（6.1.0-kt.12）

`-kt.12` 补了类目型视觉映射缺的三处声明：`VisualMapOption.selected`（`Dictionary<boolean>`，按类目名
或分段值记录选中状态），以及 `VisualOptionUnit` 的 `categorySymbol` / `categorySymbolSize` /
`categoryColor`（`symbol` / `symbolSize` / `color` 的 `@JsName` 同类视图，对应上游
`Dictionaryable<VisualOptionUnit>` 的字典形态）。上游把 `inRange` 声明成
`{ [key in BuiltinVisualProperty]?: any }`，字典只在实现里看得见：`preprocessForSpecifiedCategory`
会把没在字典里出现的类目落到 `-1` 槽，这就是官方示例用空串 `''` 当默认值的原因。
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

### ktjs-echarts-gl（2.1.0-kt.10）

`-kt.10` 两处：`Line3DDataItemOption` 的数据项样式键名从 `itemStyle` 改成上游真正读的 `lineStyle`
（`Line3DSeries.js:11` 的 `visualStyleAccessPath: 'lineStyle'`，官方文档 `series/line3D.md` 数据项一节
同此），原来那个键写进去没人读；`Axis3DLabelOption` 改为继承 `LabelOption`，对齐 2D 的
`AxisLabelOption`——`Grid3DAxis.js:131` 直接把 axisLabel 当文字样式模型用，ECharts 又把废弃的
`textStyle` 展平到 label 本身，所以 `color` / `fontSize` 这些要在 label 层声明，调用方不必再
`unsafeCast<TextStyleOption>`。

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
  `PostEffectTest` 覆盖 `postEffect` 七个 pass、`temporalSuperSampling` 与 `environment`，
  `Axis3DOptionTest` 覆盖 `grid3D` 与 `xAxis3D` 共用的六个轴子选项，`DataItemOptionTest` 覆盖
  `bar3D` / `scatter3D` / `line3D` / `surface` 的对象形态数据项和 `surface` 用 `data` + `dataShape`
  的那条分支；这些文件都按「选项能被 `setOption` 接受、字段能从 `getOption` 读回」这个约定写，
  `SSR` 在用例里保持关掉（2.1.0 的这个 pass 在 Chrome 下画黑帧）。
  公共辅助在 `TestSupport`：`createChart` / `nextFrames` / `row` / `bar3DItems` / `hasOwn` /
  `assertWebGlCanvas` / `cartesian3DAxes`，各测试文件不再自己复制。
- 用例查出的三处上游行为，别再当 wrapper 的 bug：
  `ViewGL.setPostEffect` 先读 `DOF` / `SSAO` / `SSR` / `FXAA`，camelCase 只是回退，所以这四个键只能靠
  `@JsName` 写对——写错键名时类型化读写依然自洽，用例用 `hasOwn`（`TestSupport`）直接问 JavaScript；
  ECharts 会把 `axisLabel.textStyle` 展平到 `axisLabel` 本身并把 `textStyle` 置成 `null`，
  但 `axisPointer.label.textStyle` 原样保留，所以 3D 轴标签的文字样式在 label 层读写；
  数据项的样式键跟的是该系列的 `visualStyleAccessPath`（echarts `src/visual/style.ts` 的 `dataStyleTask`）——
  `line3D` / `lines3D` / `linesGL` 是 `lineStyle`，其余是 `itemStyle`；`Bar3DSeries.js:13` 把这个属性
  拼成了 `visualStyleAccessPathvisu`，但 echarts 的默认值本来就是 `itemStyle`（`src/model/Series.ts:220`），
  所以 `bar3D` 的数据项仍用 `itemStyle`。
- 已确认的死字段（不建模）：`graphGL` 的 `roam` / `center`、`edgeSymbol` / `edgeSymbolSize`、
  `initLayout` / `stopThreshold`；hatching 材质只读 `hatchingTextures`，`paperColor` 无人读取；mapbox3D /
  maptalks3D 文档里的 `shading` 不会被读。

### demo

- 官方清单 436 条：**已移植 384 页 / 385 条，明确不做 51，待补 0**。逐条清单、理由与维护方式见
  [demo/COVERAGE.md](demo/COVERAGE.md)。
- 跨页注意事项（含 GL）见 [demo/NOTES.md](demo/NOTES.md)；移植约定、各家族做法与可复用数据见
  [demo/AGENTS.md](demo/AGENTS.md)。
- **明确不做的 51 条不再讨论**：bmap、外部 JS 库、官方站点图片素材、真实指针交互、渲染器开关、远端脚本、
  实例内部结构等，理由在 COVERAGE 里。新增排除项先更新 `scripts/demo-coverage.mjs` 的 `EXCLUDED`。

## 下一步

**移植队列已清空**（`geo-lines` 是待补清单的最后一条）。官方清单 436 条现在要么已移植、要么在
「明确不做」里，`node scripts/demo-coverage.mjs` 输出「待补 0」。已在推进的方向是候选 3——按
`AGENTS.md`「测试覆盖」那六条复查 wrapper 的用例缺口，GL 侧已完成两批（第一批 `postEffect` /
`environment` / 轴子选项，第二批对象形态数据项与 `surface.data`，第二批查出了 `-kt.10` 那个键名 bug）。
下一批候选：

1. GL 还没被点名的分支：`Geo3DRegionOption` 的区域级 `itemStyle` / `label`（现有用例只写了 `name` /
   `height`）、`globe` 的 `layer` / 材质组合，以及 `map3D` 的 `shading` 与 `regions`。
2. 把缺口审计做成可提交的工具：这批的清单来自一次性脚本（`build/gl-coverage-gap.mjs`，未入库），它只看
   类型名有没有在测试里以字面量出现过，`unsafeJso {}` 推断出的类型会被误报成缺口，得改成按字段核对才值得
   放进 `scripts/`。
3. 2D 侧同样的复查：粗筛有 418 个 `src/jsMain` 声明没在 `src/jsTest` 里以名字出现过，真假要按字段核对，
   优先 `options/` 里那些只有类型没有用例的系列数据项联合。

已否决的方向：把 COVERAGE「清单之外」那 13 个 mapbox / maptalks / Web Audio GL 源文件与 6 个归档示例并入
`EXCLUDED`——`scripts/demo-coverage.mjs` 对不在官方清单里的 id 直接 `fail`，这些条目本来就不进账本，
继续留在 COVERAGE 的「清单之外」一节用文字说明。

新增/移植页面后跑 `node scripts/demo-coverage.mjs --write` 更新 COVERAGE。

## 收尾清单

- `./gradlew build`、`:ktjs-echarts-gl:jsBrowserTest`、`:demo:e2eTest` 全过；截图目视至少一个新 GL 页面。
- 改了 wrapper 公开代码就提升对应模块的修订号，并跑
  `./gradlew kotlinUpgradePackageLock --rerun-tasks` 提交锁文件（增量构建下该任务会误报 UP-TO-DATE）。
- 每批一个 `feat(...)` 提交并推送到 `origin/main`。
