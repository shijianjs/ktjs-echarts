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
- 提交：每批一个 `feat(demo): …` 提交并推送到 `origin/main`。
- 目录：`demo/src/jsMain/kotlin/demo/`（shell/router/主题/语言/ui/examples），
  e2e 在 `demo/src/jvmTest/kotlin/demo/e2e/DemoPageTest.kt`。
- 验证：`./gradlew build`、`:ktjs-echarts:jsBrowserTest`、`:demo:e2eTest` 三条都要过；
  e2e 会打包站点并用 Playwright **逐页**打开每个示例断言画布。

### 已落地（279 个示例 / 26 个家族 + 文档示例组 + 总览页）

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

- 第三十四批（2 个，MBTI 相容矩阵 + 股市矩阵看板）：`matrix-mbti` 追加在 `examples/MatrixLongTail.kt`
  （数据与配色在 `MbtiData.kt`）：16×16 矩阵每格用两色 decal 图案区分一对类型，两层 `levels`
  （25 / 30）的组头，点击画布在明细与四组中位数汇总之间 `setOption(option, true)` 切换；上游的
  `window.innerWidth` / `innerHeight` 改成按图表尺寸推算。新建 `examples/MatrixStock.kt` 的
  `matrix-stock`：5×6 矩阵上铺价格（4 行）、MACD、成交量、买卖十档与深度图，5 个 grid 与 4 个标题用
  `coordinateSystem: 'matrix'` + `coord` 钉进格子、`mergeCells` 跨格合并、时间轴 `breaks` 折掉午休；
  `Math.random()` 换成 `Lcg`，`markPoint` 必须带 `relativeTo: 'coordinate'` 否则标签会铺满整图高度。
  配套 wrapper 补 `TitleOption.coordinateSystem` / `coord` 与 `BarDataItemOption.typedValue`，均有测试。
- 第三十三批（2 个，柱状框选 + 元素周期表）：`bar-brush` 追加在 `examples/BarExtra.kt`，工具栏切到
  矩形 / 多边形 / 轴向框选后，`brushSelected` 事件把命中的原始数据下标拼成右下角标题；上游把事件
  payload 声明成 `any`，示例侧用本地 `external interface`（`batch[].selected[].dataIndex`）强转。
  新建 `examples/MatrixLongTail.kt` 与 `PeriodicTableData.kt` 的 `matrix-periodic-table`：19×10 矩阵
  坐标上 `custom` 系列用 `api.layout` 铺 118 个元素，`api.value(2)` 的 NaN 用来区分 `La~Yb` 一类
  占位格，11 条块说明由 `graphic` 文字元素在第一帧后用 `convertToPixel` 定位；上游固定 900px 居中，
  demo 改成 `chart.getWidth() - 160` 自适应并保留左右标注位。上游 `label.textStyle` 在 ECharts 6 里
  不生效，未移植。`bar-breaks-brush` 需要 zr 级指针序列，按排除策略不做。
- 第三十二批（3 个，大数据量城市雨量 + 世界人口地图）：新建 `examples/LineRainfall.kt` 的
  `grid-multiple`（上下两个 grid 共用 3079 个逐小时刻度，上格蒸发量 0–500，下格雨量 y 轴
  `inverse`，两轴 `axisPointer.link` 联动，dataZoom 同时管两条 x 轴）与 `area-rainfall`
  （双 y 轴面积图，九月两段降雨用 `markArea` 标出、`alignTicks` 让两个刻度自动对齐）；
  两页数据体量太大，由一次性脚本从上游 `.ts` 里拆成 `resources/data/rainfall-evaporation.json`
  与 `rainfall-flow.json`（`SOURCE.md` 标为 derived）。新建 `examples/ScatterWorld.kt` 的
  `scatter-world-population`（169 个国家按 2011 年人口映射气泡直径，`geo` + `scatter` 共用
  `world.json`，隐藏 visualMap 只管 `symbolSize`），国家表由脚本生成为 `WorldPopulationData.kt`。
  顺带补上 wrapper 里 `DataZoomOption` 缺的三个上游字段（`maxValueSpan` / `realtime` / `orient`）
  并加进 `LiteralsTest`。
- 第三十一批（5 个，象形柱图 path:// 面板图 + 身高体重分布）：新建
  `examples/PictorialBarLongTail.kt` 的 `pictorialBar-vehicle`（驯鹿 / 轮船 / 飞机 / 高铁 / 汽车的
  `path://` 剪影按 `symbolRepeat` 沿柱子平铺，2015/2016 两组并列）、`pictorialBar-body-fill`
  （五个人体剪影靠 `symbolClip` + `symbolBoundingData` 按含水量裁切，底层灰色 `full` 系列画完整身高，
  标签把数值换算成百分比，`markLine` 标出两组最大值/最小值）、`pictorialBar-velocity`
  （透明小山柱做背景，柱顶用 `symbolPosition: end` + `symbolOffset` 挂速度剪影）、
  `pictorialBar-bar-transition`（同一 option 在象形柱 / 普通柱 / 菱形柱之间切换，两个系列共用 id +
  `universalTransition` 按 dataIndex 形变，`ChartHolder.setInterval` 每 2.5 秒换一次）；新建
  `examples/ScatterWeight.kt` 的 `scatter-weight`（260 位女性 + 247 位男性身高体重散点，
  每系列带取值范围的 `markArea`、极值 `markPoint`、平均值与 160cm 的 `markLine`，
  tooltip 区分元组/单个值的写法与上游一致）。数据与 path 常量由一次性脚本生成为
  `PictorialBarPaths.kt` 与 `ScatterWeightData.kt`。上游 `pictorialBar-body-fill` 根级的
  `markLine: { z: -100 }` 没有移植（wrapper 不建模根级 mark 组件，只影响层级）。
- 第二十七批（9 个，图形元素 + 平行坐标 + 散点矩阵）：新建 `examples/CanvasGraphics.kt` 的
  `watermark`（canvas 画一块 ECHARTS 斜向水印当 `backgroundColor` 的重复 pattern，两个柱块两个饼）、
  `bar-drilldown`（点击柱体走 `universalTransition` 下钻，`event.data.groupId` 上游是 `unknown`，
  只能在 JS 侧取；Back 是带 `onclick` 的 graphic text 元素）、`bar-race`（每 3 秒加一截 +
  `realtimeSort`，随机源 `Lcg`、定时器交给 ChartHolder）；新建 `examples/ParallelAqi.kt` 的
  `parallel-aqi`（三城各 31 天的八维 AQI 表按上游 `$.get` 的数据内联，visualMap 按 PM2.5 着色）；
  新建 `examples/ScatterLongTail.kt` 的 `scatter-label-align-top` / `-right`（后者 `labelLayout`
  是读 `chart.getWidth()` 的回调，走 interactive）、`scatter-effect`、`scatter-anscombe-quartet`；
  新建 `examples/ScatterMatrix.kt` 的 `scatter-matrix`（六维散点矩阵 + 平行坐标双视图，
  piecewise visualMap 按城市分类、brush 联动全部单元格，网格位置必须是百分比字符串，写成像素数字
  会让所有单元格叠到左上角）。`scatter-matrix` 的城市名要走 `row(...)` 而不是 `cityIndex(...)`，
  否则分类 visualMap 拿不到值、所有点变灰。
  同批：e2e 的浏览器上下文加 `locale = zh-CN`，容器里没有 CJK 字体，中文界面会画成豆腐块，
  语言/主题/对比度三个断言因此随机失败。
- 第二十八批（4 个，断轴 + 大数据）：新建 `examples/IntradayBreaks.kt` 的 `intraday-breaks-1`
  （四个交易日逐分钟价格，每晚折叠成 `gap: '1%'` 的断轴，轴标签在断口上写日期，靠 `rich.weak`）、
  `intraday-breaks-2`（单日午休断轴，`breakLabelLayout.moveOverlap: false` 关掉自动避让）、
  `area-simple`（两万点渐变面积 + `sampling: 'lttb'`）与 `candlestick-large`（二十万根分钟 K 线
  走 dataset，成交量在第二个网格用 large 模式画柱，涨跌靠额外 sign 列 + 隐藏 visualMap）。
  轴标签 formatter 的第三个参数（断口信息）wrapper 没建模，用 `axisLabel(::fn)` 在 JS 侧拓宽成
  三参再收回单参。踩到的坑：第一版夜间跨天算错、`time` 不前进，页面直接卡死（Chromium
  `Page crashed`），改成按「天」拆循环。
- 第三十批（2 个，上证指数日 K 长表）：新建 `examples/CandlestickLongTail.kt` 的 `candlestick-sh`
  （2013 上半年 88 行日 K，四条均线 + 最高/最低/均价标记点 + 两条收盘极值标记线 + 双轴水印网格）与
  `candlestick-sh-2015`（2015 全年 246 行，上游表按倒序存储后再 `reverse()`，开头两行空记录落成两根
  零值蜡烛；自定义滑块把手、十字准星 `#376df4`、`yAxis` 从 0 起）。数据由一次性脚本转成 `OhlcDay`
  数组，`calculateMA` 用窗口内收盘价求和实现。同批补 wrapper 的
  `CandlestickSeriesOption.typedItemStyle`（上游收窄的 `itemStyle` 视图）与 mark 级的
  `MarkStatesOption`（`emphasis` / `blur` / `select`），各补一条断言。顺带删掉 `ScatterLongTail.kt` 里
  与官方 `scatter-matrix` 同 id 的自造页（目录里的两条卡片会打开同一页）。
- 第二十九批（3 个，多层下钻 + 议会图过渡 + 日历散点）：新建 `examples/BarDrilldown.kt` 的
  `bar-multi-drilldown`（十二层数据表，`encode.itemGroupId` / `itemChildGroupId` 配合
  `universalTransition` 做形变；上游把当前层写在 `myChart.getOption().id` 上，wrapper 不建模这个
  野字段，例子侧改用 `ArrayDeque<String>` 记下钻路径）、新建 `examples/CustomTransition.kt` 的
  `pie-parliament-transition`（饼图与自定义系列共用 series id，`ChartHolder.setInterval` 每两秒
  换一次 option，`renderItem` 按环半径排座位）、`examples/Calendar.kt` 追加
  `calendar-effectscatter`（上下两张半年历，365 天成交额用 `Lcg` 生成，Top 12 叠一层
  `effectScatter` 涟漪）。顺带修掉 wrapper 的一个字段名错误：`OptionEncode` 的
  `itemChildGroupId` 之前拼成 `childGroupdId`，补 `OptionEncodeTest` 钉住三个分组维度的属性名。
- 第二十六批（13 个，桑基聚合 + 散点长尾 + 富文本 + 置信带 + 双轴动态表）：新建
  `examples/ScatterExtra.kt` 的 `scatter-large`（两条各 50 万点的正弦散点，随机源换成 `Lcg`）、
  `scatter-painter-choice`（4111 位画家的 `masterPainterColorChoice.json`，`symbolSize` 与
  `itemStyle.color` 都按 `dataIndex` 取值）、`scatter-single-axis`（7 条单轴 × 24 小时的打卡表），
  新建 `examples/ExtraPages.kt` 的 `confidence-band`（`confidence-band.json` 按最小下界取整后整体上移，
  标签与 tooltip 的百分比都以这个 base 计算）与 `dynamic-data`（两条 2.1 秒滚动的随机序列，
  定时器交给 `ChartHolder`、随机源换成 `Lcg`），新建 `examples/RichText.kt` 的 `bar-rich-text` 与
  `pie-rich-text`（天气图标是 `resources/data/img/weather/*_128.png` 的图片 pattern），
  `Sankey.kt` 补 `sankey-energy` / `sankey-levels` / `sankey-nodeAlign-left` /
  `sankey-nodeAlign-right` 四条（`energy.json` / `product.json`）。
  同批补齐 wrapper 的空洞：`SankeyEdgeStyleOption` 的 `color` 上游是
  `ZRColor | "gradient" | "source" | "target"`（新增 `SankeyEdgeColor` 联合 + 同名字段
  `typedColor`），`ZRColor` 补「按 data item 参数取色」的回调工厂，`ZRColor(PatternObject)` 这一份
  重载之前在文件里漏了（图片 pattern 因此不是合法颜色）。测试新增
  `options/ZRColorAndPatternTest`（3 例），浏览器用例 259 → 262，全过。
  踩到的坑：`masterPainterColorChoice.json` 的 `x` 列是**字符串**（plotly 导出），照上游的一元
  `+` 转成 `Number(...)` 才行，直接 `as Double` 会在 4111 次循环里抛 Kotlin 的
  ClassCastException；富文本里「只有图片没有文字」的 tag 没有宽度，必须给 `width`，否则图片被画进
  零宽盒子。
- 第二十五批（8 个，graphic 关键帧动画 + 大数据热力图 + 联动/动态/周期图）：新建
  `examples/GraphicAnimations.kt` 的 `graphic-stroke-animation`（文字描边靠 `lineDash` 与
  `lineDashOffset` 的关键帧走完）、`graphic-loading`（7 条柱子错开 `delay: i * 200` 循环缩放）、
  `graphic-wave-animation`（噪点场驱动的圆点阵，`delay = (rand - 1) * duration` 让波浪扫过画布，
  上游 `app.config` 面板只用于重设同一份 option，参数固定成默认值后删掉），三页都不靠定时器；
  `examples/PerlinNoise.kt` 把上游内联的 noisejs perlin 实现移植成 Kotlin（`PERMUTATION` 表与
  `GRAD_X` / `GRAD_Y` 逐值核对过，`seed()` 的 `if (seed < 256) seed |= seed << 8` 等写法照抄），
  波浪页与两张热力图共用，`Math.random()` 种子固定为 `0.42`；新建 `examples/HeatmapLongTail.kt` 的
  `heatmap-large` / `heatmap-large-piecewise`（201×101 噪点场 + `progressive: 1000` +
  `animation: false`，上游 y 轴只列 `0..99` 却采样到 `j = 100` 的毛病照原样保留）；
  `examples/CustomLongTail.kt` 的 `cycle-plot`（月份为 x 轴、每年在自己的类目带里画折线，
  `api.size([0,0])[0]` 取带宽后按 `(year - rawData.length / 2)` 平移——上游是 `5.5`，Kotlin 侧必须写
  `/ 2.0`，整数除法会变成 `5`）；`examples/DatasetExtra.kt` 的 `dataset-link`（监听
  `updateAxisPointer` 换饼图的 `encode.value`，上游该事件 payload 是 `any`，`axesInfo[0].value` 只能在
  demo 侧 `js()` 取；上游的 `setTimeout` 包装去掉）；`examples/GraphExtra.kt` 的
  `graph-force-dynamic`（`ChartHolder.setInterval` 每 200ms 长一个节点，随机源换成 `Lcg(20160613)`，
  上游节点 id 从 `"2"` 开始、`"0"` / `"1"` 从未用到的写法照抄）。同批给 wrapper 的 `GraphicElement`
  补上游 `ElementOption.keyframeAnimation`（此前只有自定义系列的 `CustomElementProps` 带它），
  `MatrixAndGraphicOptionTest.graphicElementLoopsThroughKeyframes` 断言回读并真实渲染。
  侧栏新增 `Groups.GRAPHIC`「图形元素」。e2e 新增「graphic 关键帧页会重绘、动态页会长大」用例；
  同批改掉整站遍历的偶发 `chart canvas box of null`：`location.hash` 改了但 `hashchange` 处理器要晚
  一个任务才跑，等待会先在上一页的画布上通过，随后节点被替换。`page(meta)` 因此给页面根节点写上
  `data-route`，`assertChartRendered` / `waitForPainted` 都改成按 `.page[data-route='<id>'] .chart canvas`
  定位，`assertChartRendered` 再加最多三次重试；调用点全部补上路由参数。

- 第二十四批（8 个，计算出来的柱状图长尾 + bump/graphic/graph 页）：新建 `examples/BarLongTail.kt`
  的 `bar1`（上游同一份降雨/蒸发表画三种柱）、`bar-stack-normalization`（5 渠道 × 7 天，按天合计
  归一化成百分比堆叠，标签用 `valueText` 在 JS 侧格式化，否则 Kotlin 插值会把 `30` 写成 `30.0`）、
  `bar-stack-normalization-and-variation`（同一份归一化数据 + `graphic` 多边形画出「变化」，页面尺寸
  要到 `setOption` 时才拿得到，因此走 `Category.interactive` 的双参重载读 `chart.getWidth()`）、
  `bar-stack-borderRadius`（栈顶圆角，上游判断是 `data && data !== '-'`，所以 0 也算「空」，照抄）、
  `bar-waterfall2`（透明底柱 + 正负值拼出的瀑布，tooltip 在 `params[1]` 为 `'-'` 时回退到 `params[2]`）；
  `examples/LineLongTail.kt` 的 `bump-chart`（6 年排行榜，上游 `Math.random` 换成 `Lcg`，Fisher–Yates
  的自减位置按原代码逐行对齐，`yAxis.inverse` + `interval: 1` + `min/max` 锁名次，`endLabel` 收尾）、
  `line-graphic`（`graphic` 的 `group` + `rotation` 斜置横幅与带阴影的说明面板，`overflow: 'break'`
  换行）；`examples/GraphExtra.kt` 的 `graph-grid`（`layout: 'none'` + `coordinateSystem: 'cartesian2d'`
  把 graph 节点摆在日历网格上，不需要数据文件）。同批删掉 `bar1` 根节点的 `calculable: true`：6.x 已不
  再读这个字段，wrapper 里只有 `visualMap` 还有同名成员。e2e 新增「bar 长尾画出算出来的堆叠」用例，
  用 `bar-stack-normalization` 与 `bar-stack-normalization-and-variation` 的内容像素数比较证明
  `graphic` 层真的建了出来（只看有没有像素分不出两者）；`assertChartRendered` 的等待条件从
  「canvas 存在」改成「canvas 已有非零尺寸」，整站遍历时偶发的 `chart canvas box of null` 是这里
  抢在布局之前取值，不是页面缺陷。

- 第二十三批（8 个，line 数据长尾 + 矩阵坐标系文档示例）：新建 `examples/LineLongTail.kt` 的
  `area-stack-gradient`（五个堆叠面积，`lineStyle.width: 0` + `LinearGradient(0,0,0,1)` 填充）、
  `area-pieces`（piecewise visualMap 按 x 维度涂两块面积，`markLine` 用 `symbol: ['none','none']`
  画边界，`yAxis.boundaryGap: [0,'30%']`）、`area-time-axis`（2 万点时间轴面积，`tooltip.position`
  回调固定在顶部 10%）、`line-gradient`（两个 grid + 两个 continuous visualMap，第二个按
  `dimension: 0` 沿 x 渐变）、`multiple-x-axis`（两个 category x 轴各带一年，`tooltip.trigger: 'none'`，
  `axisPointer.label.formatter` 回调读 `seriesData[0].data`）、`dynamic-data2`（1000 点滚动窗口，
  `ChartHolder.setInterval` 每秒移 5 步）、`line-easing`（31 个 easing 函数逐个建 grid/x 轴/y 轴/系列/
  标题，按 `ceil(sqrt(n))` 排成百分比网格）。同批在 `DocExamples.kt` 落地
  `doc-example/matrix-graph-box`，为此给 wrapper 补上游 `ComponentOption` 的
  `coordinateSystemUsage`（新增 `CoordinateSystemUsage` 字面量 + `CoordinateSystemUsages` 常量）与
  `coord`（复用 `CoordinateSystemDataCoord`），`MatrixAndGraphicOptionTest` 新增
  `graphSeriesCanUseTheMatrixAsABox` 覆盖 graph-on-matrix 写法；`LineExtra.kt` 头注释里
  「line-easing 约 30 系列 × 2000 点」的旧说法按上游 `N_POINT = 30` 改正。e2e 新增
  「line 长尾画出内容且动态页每秒换画面」用例，目录整站遍历仍逐页断言画布。

- 第二十二批（8 个，gauge 长尾 + line 变体 + 异步 option，gauge 家族至此 12/12 全）：新建
  `examples/GaugeLongTail.kt` 的 `gauge-speed` / `gauge-clock` / `gauge-barometer` /
  `gauge-multi-title` / `gauge-car`（面板图的 `path://` 字符串按上游原样内联，`Math.random()` 换成
  `Lcg`，指针与数值刷新走 `ChartHolder` 的定时器；上游写在 `option.animationDurationUpdate` 上的 300ms
  其实到不了 `setOption`，KDoc 记了这条差异），`LineExtra.kt` 的 `line-sections`（visualMap 分段配色 +
  markArea 早晚高峰）与 `line-marker`（markPoint / markLine 的统计与定位写法），`DocInteractive.kt` 的
  `doc-example/tutorial-async`（先画空 option，1 秒后按 `name` 合并 x 轴数据与系列数据）。同批给
  `GaugeSeriesOption` 补上游的 `clockwise`（`gauge-car` 档位盘逆序刻度），在
  `SymbolAndKeywordOptionsTest` 里断言回读；e2e 新增「仪表盘画出指针且定时器改变画面」用例，
  doc-example 定时器用例补 `tutorial-async` 的像素增长断言，取样 helper 改成经 `Number` 读回
  （Playwright 按最窄的装箱返回 JS 整数，直接 `as Double` 会 CCE）。

- 第二十一批（9 个，极坐标长尾）：`examples/BarExtra.kt` 的 `bar-polar-label-radial` /
  `bar-polar-label-tangential` / `bar-polar-stack` / `bar-polar-stack-radial` /
  `bar-polar-real-estate`（numbeo 房价区间，透明底柱 + `barGap: '-100%'` 叠出区间）/
  `polar-roundCap` / `polar-endAngle`，`examples/Scatter.kt` 的 `scatter-polar-punchCard`（极坐标打卡
  气泡图，与 `custom-polar-heatmap`、`heatmap-punchCard` 共用 `CustomExtra.kt` 里改成 `internal` 的
  同一份打卡数据），`examples/DocInteractive.kt` 的 `doc-example/mix-timeline-all`（`baseOption` +
  十年 option 合并，上游 25KB 宏观表用脚本转成 `MixTimelineData.kt`，`calculable: true` 已随 6.x
  移除不再保留）。同批去掉 `Custom.kt` 里与 `CustomExtra.kt` 同路由的旧版 `custom-profile`（两条目录
  项指向同一个 id，`catalogById` 只留得下一张），并在 e2e 里断言该页时间轴自动播放后画面会变。

- 第二十批（7 个）：doc-example 的 `bar-dataZoom-filterMode` / `candlestick-axisPointer` /
  `grid-outerBounds` / `pie-media` / `aria-decal`，家族长尾 `line-markline`（12 个标签位置）与
  `pie-legend`（可滚动图例）；`ChartHolder` 新增 `onResize`。

- 第十九批（8 个，`examples/DocInteractive.kt`，文档示例的交互页）：
  `barGrid-barGap`（定时切换同轴柱间距）、`tutorial-dynamic-data`（500ms 追加一天的滚动折线）、
  `tutorial-loading`（`showLoading` + 3 秒后 `hideLoading` 并设 option）、
  `sunburst-highlight-ancestor` / `sunburst-highlight-descendant`、`graphic-bounding`（`graphic` 组按
  弧度匀速旋转，对比 `bounding: 'raw'` 与 `'all'`）、`timeline-dynamic-series`（`replaceMerge: 'series'`
  切换系列数量）、`pictorialBar-clip`。`sunburstHighlight` / `targetNodeId` 在 6.1.0 下不产生任何状态
  （官方页面同样全黄），改为按拍平后的 `dataIndex` 派发 `highlight`。同批补 wrapper 的
  `TimelineOption.replaceMerge` / `show`、`PictorialBarDataItemOption` 的 `AnimationOption`，并修正
  `GraphicElement.rotation` 的 KDoc（弧度）。

- 第十八批（13 个，`examples/DocMore.kt`，文档示例的第二批）：
  `pictorialBar-repeat` / `pictorialBar-position` / `pictorialBar-symbolSize` /
  `pictorialBar-symbolBoundingDataArray`（`convertToPixel` 定位说明文字，`updated` 里用 silent
  `setOption` 重排）、`scatter-tutorial-dataZoom-2` / `scatter-tutorial-dataZoom-3` /
  `scatter-dataZoom-all`（确定性数据替代 `Math.random`）、`sunburst-simple`、
  `text-fregment-align`、`label-position`、`custom-transition-simple`、`value-animation-simple`、
  `treemap-borderColor`。同批把 `Lcg` 提到 `Items.kt` 共用，wrapper 的 `barGap` /
  `barCategoryGap` 放宽为 `StringOrDouble?`。`custom-calendar-icon` 的源码在本地克隆里只剩缩略图，
  无法移植。

- 第十七批（13 个）：bar 长尾 `bar-gradient`（点击柱体 dispatchAction 平移 dataZoom）、`bar-large`
  （50 万条确定性数据 + `large` 模式）、`bar-breaks-simple`（轴断点 + 锯齿断口）；`pie-labelLine-adjust`；
  doc-example 的 `pie-label-margin` / `pie-label-distanceToLabelLine` / `pie-label-bleedMargin`
  （三页共用 `pieLabelPair` 双饼布局）、`radar`、`scatter-jitter-avoidOverlap`（抖动像素随画布宽度计算）、
  `parallel-all`（北京 / 上海 / 广州各 31 天的 AQI 表）、`data-transform-multiple-sort-bar`
  （`sort` 转换配置用数组 cast 成 `Record`）；`custom-spiral-race`（ChartHolder.setTimeout 驱动的螺旋竞速）。

- 第十六批（1 个 + 基础设施）：`pie-highlight`（setInterval 每秒 dispatchAction 高亮/显示提示；chart-scoped timer 由 ChartHolder 在卸载时自动清理）。同时给 `ChartHolder` 加了 timer 生命周期（`setInterval`/`setTimeout` 注册后 `unmount()` 自动清理），`Category.interactive` 新增双参重载暴露 `ChartHolder`，解锁全部 setInterval 动画类（custom-spiral-race / custom-gantt-flight / graphic-wave-animation / value-animation-* 等）。

- 第十五批（1 个）：`custom-profile`（自定义甘特/性能分析图，确定性数据，省略 clipRectByRect 裁剪）。

- 第十四批（1 个）：`geo-svg-traffic`（机场平面图 SVG + lines 迁徙，path:// 图标 + 流动特效）。

- 第十三批（1 个）：`geo-svg-custom-effect`（冰岛 SVG + custom series 画涟漪圈与跳动定位图标，keyframeAnimation 动画）。

- 第十二批（1 个）：`geo-organ`（内脏 SVG + 右侧柱状图，省去 mouseover 联动）。

- 第十一批（1 个）：`geo-seatmap-flight`（航班 SVG 座位图，regions 标记已售座位）。

- 第十批（2 个）：`line-aqi`（北京 AQI，双轴 x 类别轴旋转 + visualMap 分段配色 + markLine 阈值）、`custom-wind`（700KB 风场栅格，geo + 自定义 series 画风羽，需先 registerMap world）。

### 下一批（按可做性排序）

清单里还剩 40 个左右「无外部依赖」的候选，按可做性分三档：

1. **几乎零成本（option 组合、内联表）**：
   `intraday-breaks-*` / `parallel-aqi` / `scatter-matrix` / `pie-parliament-transition` /
   `calendar-effectscatter` / `bar-multi-drilldown` 已完成。
2. **要写数据转换脚本**：`matrix-stock` / `matrix-mbti` 已在第三十四批完成；`matrix-periodic-table`
   已在第三十三批完成；
   `grid-multiple` / `area-rainfall`（各 85KB，已拆成资源文件）、`scatter-world-population`
   已在第三十二批完成；`scatter-weight`、`pictorialBar-vehicle` / `-body-fill` / `-velocity` /
   `-bar-transition`（`path://` 面板图，先例是 `examples/GaugeLongTail.kt`）已在第三十一批完成。
   `candlestick-sh` / `candlestick-sh-2015` 已在第三十批完成。
3. **要补交互表演方式**：`bar-brush`（`brushSelected`）已在第三十三批完成，`bar-race` 也已完成；
   `bar-breaks-brush` 要 zr 级 `mousedown` / `mousemove` 加 document 级 `mouseup` 改断轴区间，
   与 `line-pen` / `line-draggable` / `candlestick-touch` 一样属真实键鼠序列，按排除策略不做；
   `line-fisheye-lens` 靠 `getZr().on('mousemove')`，同样不做。
   `geo-lines` / `geo-map-scatter` / `heatmap-map` / `effectScatter-map` / `scatter-map` 一族
   需要 GeoJSON，可按 `geo-choropleth-scatter` 的先例拷静态数据再做。
4. **明确排除，不要重复尝试**（原因记在 `demo/AGENTS.md`）：
   - 地图 / `geo` 的百度地图版本（`tags: ["bmap"]`）、`echarts-gl`。
   - 运行时取数（`$.get` / `XMLHttpRequest` / `fetch` + `ROOT_PATH`）而没有本地数据的。
   - 官方站点图片素材（`image://`、纹理贴图、大块 `data:image/...base64`）。
   - 真实键鼠序列（拖拽、触摸、`sendInput`）。
   - 需要另一个 npm 包的（`ecStat` 一族的回归 / 聚类示例）。
   - `sankey-itemstyle`（3000 行表由示例服务器现算，本仓库没有那份数据）、
     `custom-calendar-icon`（上游源码只剩缩略图）。

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
     `custom-error-scatter` 踩过）。类字段也一样：`DynamicDataWindow` 的 `value` 在 `unsafeJso` 里被
     `OptionDataItemObject.value` 遮住，编译器只报「类型不匹配」，改叫 `walkingValue` 就好了。
   - 表驱动示例要检查表长与名称表长度是否一致：`themeRiver-lastfm` 的表比专辑名长，越界会让整页不渲染。
   - `unsafeJso` 的隐式接收者会吃掉同名外部名字：局部变量、函数参数、类字段只要和 option 成员的
     `width` / `height` / `top` / `style` / `name` / `value` 撞名，`this.x = x` 就变成自我赋值且照样
     编译通过（`bar-stack-normalization-and-variation` 的 `panelWidth` / `captionStyle`、
     `bump-chart` 的 `lineName`、`DynamicDataWindow` 的 `walkingValue` 都是这么改名的）。
   - `numbers(...)` 收的是 `DoubleArray` 可变参数：自己 `Array(n) { ... }` 造出来的是 `Array<Double>`，
     传 `*` 展开会报「actual type is Array<Double>, but DoubleArray was expected」，用 `DoubleArray { }`。
   - `one(...)` 的界是 `T : JsAny`，可空元素类型（`MarkLineDataItem?`）传不进去，要写
     `arrayOf<MarkLineDataItem?>(...)`。
   - JSON 里「看着像数字」的列不一定是数字：`masterPainterColorChoice.json` 的 `x` 是 plotly 导出的
     字符串，按 `Double` 读会在循环里抛 ClassCastException，照上游的一元 `+` 写成
     `js("(value) => Number(value)")` 才稳。
   - 富文本里「只有图片没有文字」的 tag（`{Sunny|}`）没有宽度，图片 pattern 会被画进零宽盒子，
     必须给这个 tag 显式 `width`。
4. 收尾：`./gradlew build` + `:demo:e2eTest` 都要过；e2e 现在逐页检查，新增页面自动覆盖；截图目视
   核对至少一两个新页面。

### 踩过的坑（跨会话提醒）

- **不要用 PowerShell 的 `Set-Content` 改仓库里的源码文件**：它按 ANSI 落盘，会把 UTF-8 的中文注释/
  字符串写坏（本轮一次批量小改就把 7 个文件写成了乱码，靠官方清单反推 `titleCN` 才修回来；
  仓库里 `ContainPixelTest.kt` 那个早已损坏的破折号也是同一个原因）。要批量改文件用 Node/Python
  读写 UTF-8，或者直接用编辑工具。
- **移植新示例时顺手核对字段名**：上游的公开类型偶尔会在生成阶段被抄错（本轮
  `OptionEncode.itemChildGroupId` 被写成 `childGroupdId`，编译期完全看不出来，只有按上游
  `types.ts` 逐个对名字才会发现）。凡是上游用 `unknown` / 索引签名表达、Kotlin 侧手写成员的地方，
  都值得在示例里真正赋一次值，并补一个钉住 JS 属性名的测试。

## 待办

- 类型建模：暂无未完成项（见上面各轮）。
- 示例站（demo）：见「示例站（demo）移植进度」一节。第三十四批（`matrix-mbti` + `matrix-stock`）
  之后，「无外部依赖」的第二档已经清空：只剩 `bar-breaks-brush` 这类需要 zr 级指针序列的
  （不做）与 `custom-calendar-icon`（上游源码只剩缩略图，不移植）。继续做要按
  `geo-choropleth-scatter` 的先例补地图类静态 GeoJSON，或回到 `doc-example/` 剩余条目。
- 提交：全部已推送到 `origin/main`（最新：第三十四批 2 个示例）。
