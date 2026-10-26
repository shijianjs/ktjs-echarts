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
- 模板仓库同步 `TYPE-MAPPING.md` / `AGENTS.md`（提交 `329f0bf`、本轮待推）。

### 遗留提醒

`GraphSeriesOption` 里原 `directed` / `edgeShape` / `edgeForkPosition` / `selectedMode` 在 6.1.0 的
`.d.ts` 中查不到，已按上游删除；上游若加回来则按新 `.d.ts` 恢复。

## 待办

### 1. 剩下的关键字字面量字段

上一轮补齐了 series 级与部分组件级的字面量，以下仍是 `String?`（都要先查 `.d.ts` 确认取值）：

- `series.progressiveChunkMode`（`'mod'`）、各 series 自己的 `coordinateSystem`（如 line `'cartesian2d' | 'polar'`、
  chord `'none'`、sankey `'view'`；注意基接口 `SeriesOption.coordinateSystem` 是 `string`，子类型只能用
  `override` 且类型必须一致，想收窄得先把基接口改成联合或放弃收窄）
- `areaStyle.origin`（`'auto' | 'start' | 'end' | number`，line series 会再收窄）
- gauge `axisLabel`（上游 `Omit<LabelOption,'rotate'> & { formatter; rotate?: 'tangential' | 'radial' | number }`）
  与 `label.rotate`
- `label.lineOverflow`（`'truncate'`）
- `timeline.controlStyle.position`（`'left' | 'right'`）
- `title.target` / `subtarget`（`'self' | 'blank'`）
- `legend.align`（`'auto' | 'left' | 'right'`）、`legend.pageButtonPosition`（`'start' | 'end'`）
- `axis.nameLocation`（`'start' | 'middle' | 'end'`）
- `visualMap.selectedMode`（Kotlin 现在写的是「`"auto"` 或 `"all"`」，要核对上游再定）
- `graphic` 元素、`matrix`、`parallel`、`dataZoom` 里零散的 `String` 字段

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

### 6. 图例（legend）的细分类型

- 上游 `LegendOption` 由 `LegendStyleOption + BoxLayoutOptionMixin + BorderOptionMixin + ...` 组成，另有
  `ScrollableLegendOption`（分页字段）、`LegendLineStyleOption`（`inactiveColor` / `inactiveWidth`、
  `width: 'auto' | 'inherit'`）、`LegendItemStyleOption`（`borderWidth: 'auto' | 'inherit'`）。
- 现状把分页字段摊在 `LegendOption` 上、`lineStyle` 用通用 `LineStyleOption`（都更宽）；
  想把它们收窄会撞上 `var` 覆盖限制（同第 2 条），因此需要先定方案再动手。

### 7. 上游导出清单的全量复核

- 目前只补「已知空缺」，没有重新逐个核对 `echarts` 的公开导出（`EChartsType` / `ChartView` / `SeriesModel` /
  `ComponentModel` / `Payload` / 各 `*Payload` / `*Event` …）与各入口
  （`echarts/core`、`echarts/charts`、`echarts/components`、`echarts/features`、`echarts/renderers`）的差异。
- 建议单独开一轮：从 `types/dist/*.d.ts` 生成导出清单再逐项比对（参考 `custom-elements.json` 式清单的做法）。

### 8. 已确认「保持现状」的项（不要重复修）

- `LegendDataItemOption.tooltip` 保持 `JsAny`：上游就是 `DataItem.tooltip?: unknown`，是正确映射。
- `MapSeriesOption.markPoint` / `markLine` / `markArea`：上游声明为 `any`，Kotlin 侧继承基接口的具名类型
  （比上游更严格）属于有意选择。
- `CustomSeriesRenderItemParams.itemPayload` / `context` 的值类型：上游 `Dictionary<unknown>`，值用 `JsAny`，
  键已用 `Record<String, JsAny>`。
- `GraphSeriesOption` 里已删除的 `directed` / `edgeShape` / `edgeForkPosition` / `selectedMode`：
  6.1.0 的 `.d.ts` 里查不到（`edgeShape` / `edgeForkPosition` 只属于 tree 系列）；上游若加回来再恢复。
- `MarkAreaOption` 没有 symbol 成员、`MarkOption` 不再有 `show`：都按上游声明处理，不是遗漏。
