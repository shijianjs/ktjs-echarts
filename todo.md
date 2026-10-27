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
  拿不到分页字段；字段保留在 `LegendOption` 上属于有意的更宽建模（与上游的联合等价，见第 8 条）。
- 测试：新增 `options/LegendOptionTest`（4 例，含真实图表 round trip）；浏览器用例 195 → 199，全过。

### 遗留提醒

`GraphSeriesOption` 里原 `directed` / `edgeShape` / `edgeForkPosition` / `selectedMode` 在 6.1.0 的
`.d.ts` 中查不到，已按上游删除；上游若加回来则按新 `.d.ts` 恢复。

## 待办

### 1. 系列自身的 `coordinateSystem` 收窄

上游基接口 `SeriesOption.coordinateSystem` 是 `string`，子类型收窄成 `'cartesian2d' | 'polar'`、
`'none'`、`'view'` 等（line/bar/scatter/effectScatter、chord、sankey、heatmap、parallel、custom、graphic 系列）。
Kotlin 的 `var` 覆盖要求类型完全一致，只能：

- 把基接口改成联合（子类型仍不能收窄，只是调用方拿到联合），或
- 按 `AGENTS.md` 的同名字段约定用 `@JsName("coordinateSystem")` + `Property` 后缀另起一个 Kotlin 名，
  先验证 `external interface` 继承里两个成员映射到同一 JS 属性名是否被编译器接受。

### 2. `Omit<...> & { ... }`、var 收窄带来的限制（需要新写法才解）


- series 级 `itemStyle` / `label` 上游会收窄成子类型（`TreemapSeriesItemStyleOption`、`SunburstLabelOption`、
  `ChordItemStyleOption` …），Kotlin 的 `var` 覆盖要求类型完全相同，这些字段只能保留基接口类型；
  精确类型在 `levels` / `data` 的 item 上可用，因此不算功能缺失。
- gauge `axisLabel` 的 `rotate` 是同类问题（`LabelOption.rotate: Double?` 无法收窄成
  `'tangential' | 'radial' | number`）。
- 可选方案：对同名字段用 `@JsName` + `Property` 后缀另起一个 Kotlin 名（见 `AGENTS.md` 的 DOM 同名字段约定），
  但要先验证 `external interface` 继承里两个成员映射到同一个 JS 属性名是否被编译器接受。

### 3. 自定义系列的元素选项树

- `CustomSeriesRenderItemReturn` 目前是 `echarts.options.GraphicElement?` 的近似实现。
- 上游是 `CustomRootElementOption | undefined | null`，需要新建除选项树：
  `CustomBaseElementOption`（`type` / `info` / `textContent` / `clipPath` / `tooltipDisabled` / `extra` /
  `during` / `enter|update|leaveAnimation`）、`CustomDisplayableOption`（`style` / `emphasis` / `blur` / `select` /
  `z` / `zlevel` / `z2` / `invisible`）、`CustomPathOption`（含 `type` 字面量联合与 `BuiltinShapes` 的 `shape`）、
  `CustomImageOption`、`CustomTextOption`、`CustomGroupOption`、`CustomCompoundPathOption`、
  `ElementKeyframeAnimationOption` 与 `TransitionOptionMixin`。
- 难点：这些类型引用 zrender 的 `PathProps` / `TextProps` / `GroupProps` / `Displayable` / `TransformProp`
  等内部类型，需要先决定用 `ZRShapeProps` / `ZRStyleProps` 这类现成封装顶替哪些成员。
- 顺带：`CustomSeriesRenderItemAPI.visual(type)` 的入参目前是 `String`，上游是
  `NonStyleVisualProps | StyleVisualProps`（`'color' | 'borderColor' | 'symbol' | ...`），可补字面量类型。

### 4. 其余数据项类型

- 除 gauge / theme river / 层级与关系图外，各系列的 `*DataItemOption` 都还没有单独声明，`data` 走通用联合：
  line / bar / scatter / effectScatter / radar（`*DataItemOption`）、boxplot、candlestick、heatmap、lines、
  parallel、pictorialBar、funnel、pie、map。
- `OptionDataValueDate`（`Date | string | number`）已备好，用于这类数据项。
- 前置问题见第 5 条。

### 5. 基接口 `data` 的处理（设计问题）

- 上游 `SeriesOption.data?: unknown`，各具体类型收窄；Kotlin 基接口现在是 `Array<OptionDataItem?>?`，
  节点类 item 靠继承 `OptionDataItemObject` + `items(...)` 塞进去。
- 若要完全精确，需要按 TYPE-MAPPING §12 把 `data` 从基接口下放到每个具体系列；
  代价是 `option.series[i].data` 这种基类访问会消失（`OptionModelTest` / `LiteralsTest` 里有用例依赖它）。
- 决定前先评估：下放后是否需要给 `EChartsOption.series` 之外再提供一个「只读 data」的视图。

### 6. 上游导出清单的全量复核

- 目前只补「已知空缺」，没有重新逐个核对 `echarts` 的公开导出（`EChartsType` / `ChartView` / `SeriesModel` /
  `ComponentModel` / `Payload` / 各 `*Payload` / `*Event` …）与各入口
  （`echarts/core`、`echarts/charts`、`echarts/components`、`echarts/features`、`echarts/renderers`）的差异。
- 建议单独开一轮：从 `types/dist/*.d.ts` 生成导出清单再逐项比对（参考 `custom-elements.json` 式清单的做法）。

### 7. 已确认「保持现状」的项（不要重复修）

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
