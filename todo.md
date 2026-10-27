# todo

本文件记录 **类型建模尚未完成的工作**，供跨会话继续。每轮完成后请更新本文件，并把已完成项删掉。

## 上下文（读之前先知道这些）

- 仓库：`ktjs-echarts`（`echarts` 的 Kotlin/JS wrapper），锁定上游 npm 版本见根 `package.json`（当前 `echarts` `6.1.0`）。
- 上游资料的读法见 `AGENTS.md`：**读本地克隆**，不要抓 GitHub / raw：
  - echarts 源码：`S:\workspace\github\Apache\echarts`（`6.1.0-44-g34f4d927e`）
  - 锁定版本的声明（权威）：`node_modules/echarts/types/dist/shared.d.ts`（以及 `*.d.cts`、各入口 `*.d.ts`）
  - kotlin-wrappers 参考实现：`S:\workspace\github\JetBrains\kotlin-wrappers`
- 写法约定：`TYPE-MAPPING.md`（§2 mixin 与公共基接口、§3 联合去重、§12 上游写法 Kotlin 表达不了时）。
- 验证方式：`./gradlew :ktjs-echarts:jsBrowserTest`（浏览器用例）或 `./gradlew build`（含 detekt）。

## 已完成的轮次

### ① 系列数据项类型（提交 `d60ef46`，已推送）

- mark 数据项（`MarkPointDataItemOption` / `MarkLine1D|2D…` / `MarkArea1D|2D…`）、`GaugeColorStop` 元组、
  Tree / Treemap / Sunburst / Sankey / Chord / Graph 的节点与边类型、`labelLayout`、`regions`、
  `CustomSeriesRenderItem`；选项类型拆成 `HierarchySeriesOptions.kt` / `RelationSeriesOptions.kt` /
  `MarkDataItemOptions.kt`，新增 `EChartsSeriesLiterals.kt` 与 `CommonTypes.kt` 的一批联合。
- 测试：`SeriesDataItemTest`（16 例，含真实图表用例）+ 4 组字面量断言；浏览器用例 90 个全过。

### ② symbol 联合 + 关键字字面量 + 选择模式（提交 `2dea7f1`）

- symbol 字段统一成 `SymbolShape` / `SymbolSize` / `SymbolRotate` / `SymbolOffset`，上游
  `SymbolOptionMixin` 只保留一个 Kotlin 接口并被各类型继承；mark 选项按上游拆开
  （`MarkOption` 只留真正共享的成员，symbol 分别来自 mixin 或 markLine 自身的收窄形式）。
- 新增 12 个字面量（`SeriesLayoutBy`、`FunnelSort`、`FunnelAlign`、`RadarShape`、`GraphLayout`、
  `GraphInitLayout`、`TreeLayout`、`TreeEdgeShape`、`PictorialBarSymbolPosition`、
  `PictorialBarSymbolRepeatDirection`、`TooltipTriggerOn`、`AxisPosition`）并替换对应字段。
- `SeriesOption` 补 `selectedMode`（新增 `"series"`）与 `selectedMap`；仪表盘补
  `GaugeTitleOption` / `GaugeDetailOption` / `GaugeDataItemOption`；主题河流补
  `OptionDataValueDate` 与 `ThemeRiverDataItem` 元组。
- 测试：`SymbolAndKeywordOptionsTest`（9 + 1 例）+ 4 组字面量断言；浏览器用例 103 个全过。
- 模板仓库同步 `TYPE-MAPPING.md` / `AGENTS.md`（提交 `329f0bf`、`ccc5ab8`）。

### ③ 上游单元测试翻译 + 补齐测试暴露的类型缺口

- 新增 9 个测试文件、84 个用例（总数 103 → 187，全过）：`util/NumberTest`、`util/TimeTest`、
  `api/ContainPixelTest`、`api/ConverterTest`、`api/GetVisualTest`、`model/TimelineMediaOptionsTest`、
  `component/visualMap/SetOptionTest`、`data/DataTransformTest`、`options/SeriesCoverageTest`。
  上游 26 个 `test/ut/spec/*.test.ts` 里能翻译的 8 个已全量翻译，其余原因记在 `AGENTS.md` 的
  「上游单元测试的翻译状态」表里。
- 测试暴露并修掉的类型缺口：
  - `BoxLayoutOptionMixin`（新增），`GeoOption` / `MapSeriesOption` / `GraphSeriesOption` 继承它；
    `MapSeriesOption` 补 `layoutCenter` / `layoutSize` / `aspectScale` / `boundingCoords`。
  - `AxisOption.gridId`、`EChartsOption.baseOption` / `options` / `media`（新增 `MediaUnit` /
    `MediaQueryOption`）。
  - `VisualOptionUnit` 的 `symbolSize` / `color` / `colorAlpha` / `opacity` / `colorLightness` /
    `colorSaturation` / `colorHue` 改成 `DoubleOrDoubleArray` / `StringOrStringArray`（上游是 `number | number[]`）。
  - `NumberNamespace.parseDate` / `isNumeric` / `numericToNumber` 与 `TimeNamespace.parse` 的参数改成
    `JsAny?`（上游是 `unknown`，需要接受 `null` / `undefined`）。
  - `TimeNamespace.format` 补第 4 个参数 `lang`（上游 `string | Model<LocaleOption>`）。
  - 坐标系 mixin 建模补齐：新增 `SeriesOnCartesianOptionMixin` / `SeriesOnPolarOptionMixin` /
    `SeriesOnSingleOptionMixin` / `SeriesOnGeoOptionMixin` / `SeriesOnRadarOptionMixin` /
    `ComponentOnCalendarOptionMixin` / `ComponentOnMatrixOptionMixin`，22 个系列接口按上游 `.d.ts` 逐个继承，
    各接口里重复的 `xAxisIndex` / `geoIndex` 等 40 余处字段删掉（改由 mixin 提供），`*Id` 字段随之补齐。

### ④ 剩余关键字字面量收尾

- 新增字面量并替换字段：`AxisNameLocation`、`TitleTarget`、`HorizontalAlign`（legend 与 visualMap 共用）、
  `LegendPageButtonPosition`、`TimelineControlPosition`、`LabelLineOverflow`、`GraphicBounding`、
  `GraphicAction`（`graphic.$action`，Kotlin 侧叫 `action` 并用 `@JsName` 映射）、`BrushType` /
  `BrushTypeUncertain` / `BrushMode` / `BrushThrottleType` / `BrushToolboxIconType`、`MatrixDimensionType`、
  `DataZoomRangeMode`、`OuterBoundsMode` / `OuterBoundsContain`、`TooltipOrder`、`AxisPointerTriggerOn` /
  `AxisPointerStatus`、`ParallelAxisExpandTriggerOn`、`ProgressiveChunkMode`、`AreaOrigin`（字符串或数字）。
- `TitleOption` 补 `subtarget`；`LegendOption` 补 `pageButtonPosition`；`LabelOption` 补 `lineOverflow`；
  `ParallelOption` 补 `axisExpand*` 系列字段；`TimelineOption.controlPosition` 与
  `TimelineControlStyle.position` 一起换掉。
- 修正插入 `MediaUnit` 时被挤到前面的 `EChartsOption` KDoc。
- 测试：`LiteralsTest` 新增 5 组字面量断言与 2 组字段读写用例（含 `graphic.$action` 的 `@JsName` 映射），
  浏览器用例 189 → 195，全过。

### ⑤ legend 细分类型

- 新增 `LegendStyleOption`（legend 与 data item 共用的成员）、`LegendItemStyleOption`、
  `LegendLineStyleOption`、`LegendPageIconsOption`；`LegendOption` / `LegendDataItemOption` 改为继承
  `LegendStyleOption`，`itemStyle` / `lineStyle` 换成细分类型，`borderWidth` / `width` 支持 `"auto"` /
  `"inherit"`。
- 补齐 scrollable 的 `pageButtonItemGap` / `pageButtonGap` / `pageIcons`，`pageTextStyle` / `textStyle`
  改成上游的 `LabelOption`。
- `ScrollableLegendOption` 仍不单独拆出：`EChartsOption.legend` 是 `Array<LegendOption>`，拆开后调用方
  拿不到分页字段；字段保留在 `LegendOption` 上属于有意的更宽建模（与上游的联合等价，见第 5 条）。
- 测试：新增 `options/LegendOptionTest`（4 例，含真实图表 round trip）；浏览器用例 195 → 199，全过。

### ⑥ 系列 `coordinateSystem` 收窄

- 新增字面量 `CartesianPolarCoordinateSystem`（line/bar：`"cartesian2d"` / `"polar"`）、
  `CartesianCoordinateSystem`（candlestick/boxplot/pictorialBar：只有 `"cartesian2d"`）、
  `HeatmapCoordinateSystem`（`"cartesian2d"` / `"geo"` / `"calendar"` / `"matrix"`）、
  `RadarCoordinateSystem`、`ChordCoordinateSystem`、`SankeyCoordinateSystem`、
  `SingleAxisCoordinateSystem` 及各自的常量对象。
- 上游把这 10 个系列的 `coordinateSystem` 收窄成字面量联合，而基接口 `SeriesOption` 是 `string`。
  Kotlin 不能收窄 `var`，因此沿用基接口的 `String?` 字段，并在各子接口里加
  `@JsName("coordinateSystem") var typedCoordinateSystem: …?`，两个属性在 JS 侧是同一个属性
  （写法记进 `TYPE-MAPPING.md` §12）。`RadarSeriesOption` / `PictorialBarSeriesOption` /
  `ChordSeriesOption` / `SankeySeriesOption` 原本没重声明 `coordinateSystem`，一并补上。
- `HeatmapSeriesOption.coordinateSystem` 的 KDoc 按上游改成 `"cartesian2d"` / `"geo"` / `"calendar"` /
  `"matrix"`。
- 测试：`LiteralsTest` 新增 11 条字面量断言、1 个覆盖全部 10 个系列的「两个属性指向同一 JS 属性」用例、
  1 个真实图表 round trip；浏览器用例 199 → 202，全过。

### ⑦ 自定义系列的元素选项树

- 新增 `CustomElementOptions.kt`，按上游 `shared.d.ts` 建模 `renderItem` 返回的元素树：
  `CustomBaseElementOption`（`type` / `id` / `name` / `info` / `textContent` / `clipPath` /
  `tooltipDisabled` / `extra` / `during` / 三种动画 / 变换属性 / `silent` / `ignore` / `textConfig`）、
  `CustomDisplayableOption`（`zlevel` / `z` / `z2` / `invisible` / `style` / `emphasis` / `blur` /
  `select` / `styleEmphasis`）、`CustomGroupOption`（含 `$mergeChildren`，用 `@JsName` 映射）、
  `CustomPathOption`、`CustomImageOption`、`CustomTextOption`、`CustomCompoundPathOption`、
  `CustomRootElementOption`，以及 `TransitionOptionMixin`、`TransitionBaseDuringAPI` /
  `TransitionDuringAPI`、`ElementAnimationOption`、`ElementKeyframeAnimationOption`、
  `AnimationKeyframe`、`CustomElementProps`。
- 联合类型按 §3 建不透明接口 + 工厂：`ElementKeyframeAnimationOptionOrArray`、
  `CustomTextContentOption`（`CustomTextOption | false`）、`CustomClipPathOption`、
  `CustomStyleOption`（`ZRStyleProps | false`）、`CustomMergeChildrenOption`（`false | "byName" |
  "byIndex"`）、`CustomElementFocus`（`"none" | "self" | "series" | number[]`）。
- zrender 侧补充这些类型需要的成员：`TransformProp` / `TransformProps`、`BuiltinTextPosition` /
  `BuiltinTextPositions`、`ElementTextConfig`、`ZRElement.textConfig`、`StringOrImageLike`，
  并在 `ZRStyleProps` 上补 `image` / `sx` / `sy` / `sWidth` / `sHeight`（图片元素专用）。
- `CustomSeriesRenderItemReturn` 由 `GraphicElement?` 改为上游的 `CustomRootElementOption?`；
  `CustomSeriesRenderItemAPI.visual` 的入参由 `String` 改为新增的 `VisualProp` 字面量
  （`StyleVisualProps | NonStyleVisualProps` 的合并）。
- 测试：新增 `options/CustomElementOptionsTest`（7 例，含 `$mergeChildren` / `during` 的 `@JsName`
  映射、联合工厂、关键帧动画、真实图表渲染），`LiteralsTest` 新增 3 组字面量断言；
  浏览器用例 202 → 212，全过。

### ⑧ 上游导出清单复核与类型收口（提交 `ca37b82`）

- 按 `types/dist/echarts.d.ts` 的导出清单逐个核对：`echarts/core`、`echarts/charts`、`echarts/components`、
  `echarts/features`、`echarts/renderers` 入口的安装函数已齐全；`innerDrawElementOnCanvas`、
  `color` / `format` / `number` / `graphic` / `helper` / `matrix` / `vector` 命名空间与 `connect` /
  `disconnect` / `register*` 系列都在对应对象上，这轮没有新增。
- 新增字面量：`TooltipRenderMode` / `TooltipRenderModes`、`ECUpdateMethodName` / `ECUpdateMethodNames`、
  `AxisBreakAction` / `AxisBreakActions`、`SelectChangedFromAction` / `SelectChangedFromActions`，
  `HorizontalAligns` 补 `center`（tooltip 的 `align` 用它）。
- 回调类型：`TopLevelFormatterParams`（联合，两个工厂；`TooltipPositionCallbackParams` 用 `typealias`
  指过来）、`TooltipFormatterCallback`、`TooltipPositionCallback`（`params` 从 `CallbackDataParams` 改成
  联合）、`LabelFormatterCallback`、`LabelLayoutOptionCallback` / `LabelLayoutOptionCallbackParams`。
- 动画：`AnimationDuration` / `AnimationDelay` 联合与 `AnimationDurationCallback` /
  `AnimationDelayCallback(Params)`；`AnimationOption`、`SeriesOption`、`AxisOption`、`EChartsOption` 与
  `TreeSeriesOption` 的对应字段一起从 `Double?` 放宽。
- 轴：`breaks` / `breakArea` / `breakLabelLayout`（`AxisBreakOption` / `AxisBreakAreaOption` /
  `AxisBreakLabelLayoutOption` / `AxisBreakIdentifier`）、`startValue`、`dataMin` / `dataMax`、`jitter*`、
  `deduplication`、`containShape`、`categorySortInfo`、`tooltip`，并补齐更新动画字段。
- tooltip：`renderMode` 换成 `TooltipRenderMode`，补 `align` / `verticalAlign` / `displayTransition` /
  `appendTo`（`TooltipAppendTo` 联合：选择器、元素、回调）/ `defaultBorderColor` / `borderRadius` /
  `shadow*`；`align` 从 `web.cssom.TextAlign` 改成 `HorizontalAlign`（上游是 `left|center|right`）。
- 顶层选项：新增 `thumbnail`（`ThumbnailOption`，只有 `graph` 系列会消费）、`colorLayer`、
  `legacyViewCoordSysCenterBase` / `legacyMinMaxDontInverseAxis` 与 `ECBasicOption` 别名。
- `tooltip` / `toolbox` / `axisPointer` / `brush` 由单值改成 `Array<T>`：上游是 `T | T[]`，ECharts 的
  `getOption()` 也把组件字段归一成数组；之前按单值建模时回读到的是数组本身。
- 事件与 action：`HighlightPayload` / `DownplayPayload` / `SelectChangedPayload`、
  `BaseAxisBreakPayload` 与三个具体 payload（`@JsName` 同名字段承载 `"all"` / 数组形式）、
  `AxisBreakChangedEventBreak` 补 `start` / `end` / 轴索引并把 `old` 细化成展开状态。
- 测试：新增 `options/ExportSurfaceTest`（11 例：动画回调、label / tooltip 回调、tooltip 字段、
  轴断点 action 与事件、轴扩展字段、`categorySortInfo`、thumbnail、`ECBasicOption`），`LiteralsTest`
  新增 1 组字面量断言；浏览器用例 212 → 224，全过。
- `TYPE-MAPPING.md` 补「同一个联合有两个上游名字」「`number | callback`」两节，并把 `T | T[]`
  一条改成统一用 `Array<T>`（模板仓库同步）。

### ⑨ 各系列数据项类型与 `data` 的只读视图（提交 `76fe8b0`，已推送）

- `SeriesOption.data` 改为**只读** `val data: Array<OptionDataItem?>?`；23 个系列接口各自新增
  `@JsName("data") var typedData`，类型是该系列上游收窄后的 item 联合。两个属性读写同一个 JS 属性，
  由 `SeriesTypedDataTest.typedDataAndTheReadOnlyViewShareOneJavaScriptProperty` 验证。
- 新增各系列的 data item 类型与联合：`CartesianDataItemOptions.kt`（line / bar / scatter / effectScatter /
  candlestick / boxplot / heatmap / lines / parallel）、`PartDataItemOptions.kt`（pie / funnel / gauge /
  radar / map / pictorialBar），关系系列补 `GraphDataItem` / `ChordDataItem`；层级系列与主题河流沿用已有
  item 类型。共同的三态成员抽成 `DataItemStateOption`；`PictorialBarSeriesSymbolOption` 从系列里抽成独立
  接口，由系列与 data item 共用。`EffectScatterSeriesOption` 不再继承 `ScatterSeriesOption`（上游也没有
  这个继承关系，两者的 data item 不同）。
- `value` 被收窄成数组或联合的 item 用 `@JsName("value") var typedValue`（candlestick / boxplot / heatmap /
  radar / parallel / line / scatter / effectScatter / lines），保住「写数组 value」的能力。
- label 的 `rotate` 收窄用 `@JsName("rotate") var typedRotate` + 新联合 `LabelRotate`，覆盖
  `PieLabelOption`、`GaugeAxisLabelOption`（新增，配 `GaugeSeriesOption.typedAxisLabel`）与
  `SunburstLabelOption`。
- `numbers` / `strings` / `points` / `items` 改成泛型（元素类型从目标属性推断），新增 `dataItems` 用于
  联合分支；`one` 不变。
- 测试：新增 `options/SeriesTypedDataTest`（6 例）；测试与 demo 里系列 block 内的 `data =` 全部改为
  `typedData =`（脚本一次性改写 117 处 + demo 2 处）。浏览器用例 224 → 230，全过；`./gradlew build`
  （含 detekt）通过。
- `TYPE-MAPPING.md` §12 的 `data?: unknown` 一节按新写法重写，「继承来的 `var` 需要收窄」补了三种收窄
  形态，反模式速查同步（模板仓库提交 `b863493`，已推送）。
- 顺带收紧：`GaugeSeriesOption.axisLabel` 直接用 `GaugeAxisLabelOption?`（去掉中转的 `typedAxisLabel`），
  `SunburstSeriesOption` 补 `@JsName("label") var typedLabel`。

### 10. `Omit<...> & { ... }` 与 var 收窄的残留

- series 级 `itemStyle` / `label` 上游收窄成**另一套对象**（`TreemapSeriesItemStyleOption`、
  `SunburstLabelOption`、`CandlestickItemStyleOption`…）：精确类型本身就是基类型的子接口，调用方直接
  `unsafeJso<精确类型> { }` 赋给基接口字段即可写，读回来按需 `unsafeCast`，不算能力缺失。**不额外加
  `typedItemStyle` / `typedLabel` 视图**：上游的 `Omit<X, 'k'> & { … }` 与基接口并非逐字段相同的集合，
  套同名字段只能得到近似类型。
- `rotate` 的收窄已在 ⑨ 用 `typedRotate` 补齐（gauge axis label / pie / sunburst）。
- 关键字收窄（`coordinateSystem` 等）沿用 `@JsName` 同名字段，见 `TYPE-MAPPING.md` §12。

### 11. 已确认「保持现状」的项（不要重复修）

- `LegendOption` 保留 `ScrollableLegendOption` 的分页字段：`EChartsOption.legend` 是 `Array<LegendOption>`，
  拆成子类型后调用方无法直接访问分页字段；与上游的联合建模等价，属于有意的更宽类型。
- `LegendDataItemOption.tooltip` 保持 `JsAny`：上游就是 `DataItem.tooltip?: unknown`，是正确映射。
- `MapSeriesOption.markPoint` / `markLine` / `markArea`：上游声明为 `any`，Kotlin 侧继承基接口的具名类型
  （比上游更严格）属于有意选择。
- `CustomSeriesRenderItemParams.itemPayload` / `context` 的值类型：上游 `Dictionary<unknown>`，值用 `JsAny`，
  键已用 `Record<String, JsAny>`。
- `GraphSeriesOption` 里已删除的 `directed` / `edgeShape` / `edgeForkPosition` / `selectedMode`：
  6.1.0 的 `.d.ts` 里查不到（`edgeShape` / `edgeForkPosition` 只属于 tree 系列）；上游若加回来再恢复。
- `MarkAreaOption` 没有 symbol 成员、`MarkOption` 不再有 `show`：都按上游声明处理，不是遗漏。
- `ComposeOption` / `RegisteredSeriesOption`：上游用于「按需安装时裁剪选项联合」，wrapper 的
  `EChartsOption` / `SeriesOption` 已覆盖全部系列，不另建别名；调用方直接用具名选项类型。
- 上游的 `*ComponentOption` 别名（`TooltipComponentOption`、`XAxisComponentOption`、`LegendComponentOption`
  等）：同一接口的多个导出名，Kotlin 侧只保留基名，避免同一类型出现两套名字。
- `ActionInfo.update` 保持 `String?`：上游是 `string`，具体取值已由 `ECUpdateMethodNames` 提供，
  收窄成字面量反而挡住自定义方法名。
- `Payload.type` 保持 `String?`：上游每个具体 payload 才收窄成字面量，共用 `Payload` 时无法表达；
  调用方本来就要写自己的 action 名。

### 遗留提醒

`GraphSeriesOption` 里原 `directed` / `edgeShape` / `edgeForkPosition` / `selectedMode` 在 6.1.0 的
`.d.ts` 中查不到，已按上游删除；上游若加回来则按新 `.d.ts` 恢复。

## 示例站（demo）移植进度

本轮的 demo 工作（官方 ECharts 示例库的 Kotlin/JS 移植）按批推进。项目约定与目录说明见
`demo/AGENTS.md`，这里只记「还差什么」和「下次怎么接着做」。

### 状态

- 提交：`0a1f9ca`（第一批 69 个）、`6edff97`（第二批 tree / sunburst / dataset / calendar / matrix /
  custom，41 个）；`588381b`（第三批：文档示例 17 个 + `matrix-mini-bar-data-collection` + 主题按钮修复）、`4fd0dda`（三张显示修复）、
  已推送到 `origin/main`。
- **`git push` 一直未成功**：本机到 `github.com:443` 连接被重置/超时，重试多次仍失败；下一次先
  `git push`（含后面两批）。
- 目录：`demo/src/jsMain/kotlin/demo/`（shell/router/主题/语言/ui/examples），
  e2e 在 `demo/src/jvmTest/kotlin/demo/e2e/DemoPageTest.kt`。
- 验证：`./gradlew build`、`:ktjs-echarts:jsBrowserTest`、`:demo:e2eTest` 三条都要过；
  e2e 会打包站点并用 Playwright **逐页**打开每个示例断言画布。

### 已落地（156 个示例 / 25 个家族 + 文档示例组 + 总览页）

- 第一批（69 个）：bar 13、line 10、pie 10、scatter 3、candlestick 1、boxplot 3、pictorialBar 1、
  parallel 1、heatmap 1、gauge 5、funnel 4、radar 5、treemap 1、graph 2、chord 4、sankey 2、
  themeRiver 2。
- 第二批（41 个）：tree 7（`TreeData.kt` 的内联 flare 表）、sunburst 7（`SunburstData.kt`）、
  dataset 7、calendar 7、matrix 7（含 graph / confusion）、custom 6。
- 第三批（18 个）：`examples/DocExamples.kt` 的 17 个 `doc-example/*`（极坐标角度三件套、
  getting-started、aria-pie、sunburst-color、sunburst-label-align、line-stack-tiled、
  axis-label-align-min-max、tutorial-styling-step0..5、aria-decal-simple、aria-decal-newspaper），以及 matrix 的
  `matrix-mini-bar-data-collection`。文档示例单独成组（`Groups.DOC`），路由保留 `doc-example/` 前缀。
- 同批修掉：右上角亮色/暗色按钮切换后两个按钮都高亮（`syncThemeButtons` 用 `withClass` 累加，
  改成 `classList.toggle`），暗色主题下选中按钮白字压在浅蓝底上看不清（新增 `--switch-on-fg`），
  e2e 增加「只有一个按钮高亮 + 对比度 ≥ 4.5」的断言。
- 第四批（3 个 + 3 个显示修复）：`matrix-sparkline`（`MatrixExtra.kt`）、`custom-polar-heatmap`
  （`CustomExtra.kt`）、`bar-label-rotation`（`BarExtra.kt`）。同时修好三张展示异常的图：
  `matrix-confusion`（custom series 缺 `type` + graphic 用 `left/top`）、`matrix-covariance`
  （矩阵表头要两级嵌套）、`pie-nest`（label 富文本缺 `rich`）。
- 第五批（4 个）：`line-polar`、`line-polar2`、`line-function`（`LineExtra.kt`）、
  `gauge-stage`、`gauge-temperature`（`GaugeExtra.kt`，官方的 setInterval 未移植，显示初始值）。
- 第六批（2 个）：`doc-example/text-block-fragment`、`doc-example/text-options`（scatter 的 label 富文本，`rich` 用法与官方一致），以及 `dataset-encode-simple0`、`scatter-tutorial-dataZoom-1`。

- 第七批（5 个 + 静态数据基础设施）：`treemap-disk`、`lines-airline`（65k 航线 + world GeoJSON，
  新增 `Groups.LINES`）、`graph-life-expectancy`、`custom-hexbin`（d3-hexbin 分箱 + 球场图形）、
  `dataset-encode1`（四宫格编码矩阵）。数据集不再抄进 Kotlin，改为
  `demo/src/jsMain/resources/data/` 下的静态 JSON（来源与许可见该目录 `SOURCE.md`），
  页面用 `demo.ui.loadJson` 异步加载；e2e 的像素断言改成轮询等待。

- 第八批（3 个）：`geo-choropleth-scatter`（冰岛 GeoJSON，geo 组件被 scatter 与 map 共享）、
  `geo-svg-scatter-simple`（SVG 地图 + effectScatter）、`geo-beef-cuts`（SVG 地图 + visualMap）；
  新增 `Groups.GEO`「地图」分组与 `demo.ui.loadText`（SVG 需要文本而不是 JSON）。
- 第九批（2 个）：`geo-graph`（瑞士 GeoJSON 上的 graph 系列，节点用经纬度定位）、
  `treemap-obama`（3.7 万亿预算，递归节点转成四维数据，`colorMappingBy: id`）、
  `geo-svg-lines`（楼层平面图 SVG 上的 polyline 路径）。

- 第十四批（1 个）：`geo-svg-traffic`（机场平面图 SVG + lines 迁徙，path:// 图标 + 流动特效）。

- 第十三批（1 个）：`geo-svg-custom-effect`（冰岛 SVG + custom series 画涟漪圈与跳动定位图标，keyframeAnimation 动画）。

- 第十二批（1 个）：`geo-organ`（内脏 SVG + 右侧柱状图，省去 mouseover 联动）。

- 第十一批（1 个）：`geo-seatmap-flight`（航班 SVG 座位图，regions 标记已售座位）。

- 第十批（2 个）：`line-aqi`（北京 AQI，双轴 x 类别轴旋转 + visualMap 分段配色 + markLine 阈值）、`custom-wind`（700KB 风场栅格，geo + 自定义 series 画风羽，需先 registerMap world）。

### 下一批（按可做性排序）

1. **无外部依赖、可直接做**：
   - `doc-example` 剩余（约 42 个）：`tutorial-styling` 之后的教程页、`text-*`、`title-block`、
     `label-position`、`grid-outerBounds`、`parallel-all`、`radar`、`sunburst-highlight-*`（需要
     `dispatchAction`）、`pictorialBar-*`；`map-*`/`geo-*` 除外。
   - `custom` 剩余：`custom-calendar-icon`（`params.coordSys.cellWidth/cellHeight` 已补，可直接做）、
     `custom-hexbin`（取数）、`custom-spiral-race`/`custom-gantt-flight`（长动画）、`custom-gauge`
     （`path://` 面板图）。
   - `matrix` 剩余：`matrix-mbti`、`matrix-periodic-table`、`matrix-stock`（数据表 8-17KB，
     需要先写一个把上游数组转成 Kotlin 的小脚本）、`matrix-mini-bar-geo`（需要 geo）。
   - 已移植家族的长尾里可直接做的：`line-markline`、`line-markarea`、`line-smooth` 之外的 line 变体、
     `pie-legend`、`gauge-clock`/`gauge-speed`/`gauge-barometer`（`path://` 图标，需先决定是否搬运
     几 KB 的 path 字符串）、`bar-polar-*`、`polar-*`。
   - 已移植家族的长尾：`bar-race`/`bar-race-country`/`bar-drilldown`/`bar-multi-drilldown`/`bar-gradient`/
     `bar-large`/`bar-breaks-simple`/`bar-breaks-brush`/`bar-label-rotation`/`bar-polar-*`/`polar-*`、
     `line-easing`/`line-fisheye-lens`/`line-graphic`/`line-pen`/`line-draggable`/`line-race`/`line-polar*`/
     `area-pieces`/`area-time-axis`/`dynamic-data`/`dynamic-data2`/`bump-chart`/`confidence-band`/
     `grid-multiple`/`multiple-x-axis`/`line-aqi`（需要 AQI 表，数据在官方示例文件里内联）、
     `candlestick-sh`（26KB 内联表）/`candlestick-large`/`candlestick-brush`/`intraday-breaks-1|2`、
     `graph-force2` 之外的 graph 变体、`gauge-clock`/`gauge-speed`/`gauge-stage`/`gauge-temperature`/
     `gauge-barometer`/`gauge-multi-title`、`radar` 家族已全、`treemap` 只剩需要外部数据的、
     `pie-labelLine-adjust`/`pie-legend`/`pie-pattern`（图案需要贴图）等。
2. **需要先补 wrapper 建模**：
   - `calendar-lunar`：366 行农历表（数据而非 API）。
   - 极坐标的 `renderItem`：`params.coordSys` 的 `cx` / `cy` 还没有 Kotlin 声明（`.d.ts` 只写了
     `type`），要补才能做 `custom-gauge` / `custom-polar-heatmap`。
3. **明确排除，不要重复尝试**（原因记在 `demo/AGENTS.md`）：
   - 地图 / `geo` / GeoJSON、百度地图（`tags: ["bmap"]`）、`echarts-gl`（`public/examples/ts/gl`）。
   - 运行时取数：`$.get` / `XMLHttpRequest` / `fetch` + `ROOT_PATH`、`*.json`、`*.bin`。
   - 官方站点图片素材：`image://` 图标、纹理贴图、大块 `data:image/...base64`。
   - 真实键鼠序列：拖拽、触摸、`sendInput`（`line-draggable`、`candlestick-touch`、`bar-brush` 之类
     需要真交互的，若要做得先想清楚怎么用 `dispatchAction` 替代表演）。

### 下次接着做的具体做法

1. 选例：读 `S:\workspace\github\apache\echarts-examples\src\data\chart-list-data.js`（清单里有
   `id` / `category` / `title` / `titleCN`），按上面的排除条件过一遍。一次性筛选脚本的思路：对
   `public/examples/ts/<id>.ts|.js` 扫 `$.get` / `XMLHttpRequest` / `fetch(` / `ROOT_PATH` /
   `registerMap` / `echarts-gl` / `.json` / `noise`，再按文件大小去掉超大内联数据的。
2. 落地：在 `demo/src/jsMain/kotlin/demo/examples/<Family>.kt` 里用
   `Category(Groups.X, "examples/X.kt")` + `chart(...)` / `interactive(...)` 写；
   中文标题抄 `titleCN`（脚本可核对），数据原样搬，`Math.random()` 换成固定序列。
   新家族要在 `Catalog.kt` 加一行 import 和一行 `listOf(...)`。
3. 常见坑：
   - 数据项 `name` 是 `string | number`、`value` 是 `string | number | Date`：用 `OptionId("x")` /
     `OptionDataValue(1.0)`，共用 helper 在 `examples/Items.kt`。
   - 轴 / 系列 `data` 用 `strings(...)` / `numbers(...)` / `points(...)` / `items(...)`：元素类型从目标
     属性推断，别自己写 `Array(...) { OptionDataValue(...) }`（得到的是 `Array<OptionDataValue>`，赋不
     进 `Array<OptionDataItem?>`）。
   - `axisTick` / `axisLine` 的 `show` 是 `BooleanOrString`，而 `alignWithLabel` 是普通 `Boolean`。
   - 回调用 `js("(params) => ...")` 适配的情况（gauge 的 detail/axisLabel formatter 上游签名是
     `(value: number) => string`、`symbolSize` 回调收的是 data item 而不是 params），
     各处 KDoc 已说明原因，别照抄模板。
   - `js(...)` 里不要写箭头函数返回对象字面量（`=> ({...})`）：Kotlin/JS 会重新打印函数体、吃掉括号，
     要写成 `function (a, b) { return {...} }`（`styledItem` 踩过）。
   - 内联示例里别把局部变量叫 `data` / `legend`：会遮蔽 `unsafeJso` 接收者的同名成员（`scatter-jitter`、
     `custom-error-scatter` 踩过）。
   - 表驱动示例要检查表长与名称表长度是否一致：`themeRiver-lastfm` 的表比专辑名长，越界会让整页不渲染。
4. 收尾：`./gradlew build` + `:demo:e2eTest` 都要过；e2e 现在逐页检查，新增页面自动覆盖；截图目视
   核对至少一两个新页面。

### 踩过的坑（跨会话提醒）

- **不要用 PowerShell 的 `Set-Content` 改仓库里的源码文件**：它按 ANSI 落盘，会把 UTF-8 的中文注释/
  字符串写坏（本轮一次批量小改就把 7 个文件写成了乱码，靠官方清单反推 `titleCN` 才修回来；
  仓库里 `ContainPixelTest.kt` 那个早已损坏的破折号也是同一个原因）。要批量改文件用 Node/Python
  读写 UTF-8，或者直接用编辑工具。

## 待办

- 类型建模：暂无未完成项（见上面各轮）。
- 示例站（demo）：见「示例站（demo）移植进度」一节，下一批是 `doc-example` 剩余约 45 页、`custom` /
  `matrix` 剩余项与各家族长尾；极坐标 `renderItem` 需要先补 `params.coordSys.cx/cy`，
  `custom-calendar-icon` 需要 `params.coordSys.cellWidth/cellHeight`。
- 提交：全部已推送到 `origin/main`（最新 `edf2a62`）。
