# todo

本文件记录 **类型建模尚未完成的工作**，供跨会话继续。每轮完成后请更新本文件，并把已完成项删掉。

## 上下文（读之前先知道这些）

- 仓库：`ktjs-echarts`（`echarts` 的 Kotlin/JS wrapper），锁定上游 npm 版本见根 `package.json`（当前 `echarts` `6.1.0`）。
- 上游资料的读法见 `AGENTS.md`：**读本地克隆**，不要抓 GitHub / raw：
  - echarts 源码：`S:\workspace\github\Apache\echarts`（`6.1.0-44-g34f4d927e`）
  - 锁定版本的声明（权威）：`node_modules/echarts/types/dist/shared.d.ts`（以及 `*.d.cts`、各入口 `*.d.ts`）
  - kotlin-wrappers 参考实现：`S:\workspace\github\JetBrains\kotlin-wrappers`
- 写法约定：`TYPE-MAPPING.md`（尤其第 12 节「上游写法与 Kotlin 表达力不匹配时」）。
- 验证方式：`./gradlew :ktjs-echarts:jsBrowserTest`（浏览器用例）或 `./gradlew build`（含 detekt）。
- 后续处理：`Agent.md` 已指向本文件，模板仓库 `../ktjs-wrapper-template` 同步了 `TYPE-MAPPING.md` 与 `AGENTS.md` 相关改动。

## 已完成的最近一轮（2026-10-26 的「未建模字段」一轮）

- 补齐了上一轮遗留的未建模字段：
  - `MarkOption.data` → 拆到 `MarkPointOption` / `MarkLineOption` / `MarkAreaOption` 各自声明精确 `data`，基接口不再声明 `data`。
  - `GaugeAxisLineOption.lineStyle` → `GaugeAxisLineStyleOption` 改为继承更浅的 `ShadowOption` 再逐个声明，新增 `GaugeColorStop`（`[number, ColorString]` 元组）工厂。
  - `MapSeriesOption.regions` → 复用 `RegionOption`。
  - Graph：`categories` / `nodes` / `edges` / `links` + 节点、边、分类类型。
  - Tree：`leaves` / `labelLayout`（+ 节点类型）；Treemap：`breadcrumb` / `levels` / `sort` / `clipWindow` / `nodeClick` / `colorMappingBy` / `colorSaturation`；Sunburst：`sort` / `levels` / `animationType`；Sankey：`levels` / `links`；Chord：`links`。
  - `CustomSeriesOption.renderItem` → `CustomSeriesRenderItem`，参数/API 的退化一起收紧。
  - 新增 `EChartsSeriesLiterals.kt`（10 个字面量类型）与 `CommonTypes.kt`（`ScaleDataValue`、`OptionDataValueNumeric`、`DoubleArrayOrNone`、`ColorArrayOrNone`、`DecalArrayOrNone`、`BooleanOrSortOrder`）；选项类型拆成 `HierarchySeriesOptions.kt` / `RelationSeriesOptions.kt` / `MarkDataItemOptions.kt`。
  - 测试：`SeriesDataItemTest`（16 例，含真实图表 `setOption` 端到端用例）+ 4 组字面量断言，浏览器用例 90 个全过，`./gradlew build`（含 detekt、demo 打包）通过。
  - 提交：`ktjs-echarts` → `d60ef46`（已推送）；模板仓库 → `329f0bf`（已推送）。两仓库工作区干净、与远端同步。
- 遗留提醒（已记在后续项里可复核）：`GraphSeriesOption` 里原 `directed` / `edgeShape` / `edgeForkPosition` / `selectedMode` 在 6.1.0 `.d.ts` 查不到，已按上游删除，若后续上游版本再加回则按新 `.d.ts` 恢复。

## 待办

### 1. symbol 相关字段的全局 sweep（含死代码 `SymbolSize`）

- 上游 `SymbolOptionMixin` 的 `symbol` / `symbolSize` / `symbolRotate` 都允许回调形式
  （`SymbolCallback` / `SymbolSizeCallback` / `SymbolRotateCallback`）。
- 现状：项目里同一语义的字段类型不统一，例如 `String?`（line/scatter/radar/pictorialBar/tree/graph）、
  `StringOrStringArray?`（mark/axisLine/decal/graph）、`Double?` / `DoubleOrDoubleArray?`；
  `echarts.options.SymbolSize`（已经是 `number | number[] | callback` 的联合）**目前无人引用**。
- 计划：新增 `SymbolShape`（`string | callback`）与 `SymbolRotate`（`number | callback`），
  `symbolSize` 统一改用现成的 `SymbolSize`；然后一次性替换所有 `symbol` / `symbolSize` / `symbolRotate` 字段。
- 注意：这是破坏性改动（`symbol = "circle"` 这种裸字符串赋值会编译失败），要同步改 demo 与测试，
  且 `MarkLineOption.symbol` 上游是 `string | string[]`，它和 `SymbolOptionMixin.symbol` 不是同一个联合，
  需要各自决定类型（可能要一个 `string | string[] | callback` 的三支联合）。

### 2. 字面量联合 sweep（仍是 `String?` 的字段）

上一轮补了组件级与部分系列级字面量，以下仍是裸 `String?`，需要逐个确认上游字面量并补类型（放
`EChartsLiterals.kt` 或 `EChartsSeriesLiterals.kt`，按主题）：

- `graph.layout`（`'none' | 'force' | 'circular'`）、`graph.force.initLayout`（`'circular' | 'none'`）
- `tree.layout`（`'orthogonal' | 'radial'`）、`tree.edgeShape`（`'polyline' | 'curve'`）、
  `tree.edgeForkPosition`（上游是 `string | number`）
- `funnel.sort`（`'ascending' | 'descending' | 'none'`）、`funnel.funnelAlign`
- `radar.shape`（`'polygon' | 'circle'`）
- `series.progressiveChunkMode`（`'mod'`）、`series.seriesLayoutBy`（`'column' | 'row'`）
- `axis.position`、`tooltip.triggerOn`、`areaStyle.origin`、`label.lineOverflow` 等
- `chord.coordinateSystem`（`'none'`）、`sankey.coordinateSystem`（`'view'`）

### 3. `SeriesOption.selectedMode` / `selectedMap`

- 上游基接口有 `selectedMode?: 'single' | 'multiple' | 'series' | boolean` 与
  `selectedMap?: Dictionary<boolean> | 'all'`，Kotlin 基接口目前都没有。
- 直接加会和 `PieSeriesOption.selectedMode` / `MapSeriesOption.selectedMode`（现为 `BooleanOrString?`）冲突：
  需要先把这些统一成 `SelectionMode`，并给 `SelectionModes` 补上 `series` 常量；
  `selectedMap` 需要一个 `Dictionary<boolean> | 'all'` 的联合类型。
- 加上之后要检查 `LegendOption.selectedMode` 等其它同名成员。

### 4. 自定义系列的返回值类型

- `CustomSeriesRenderItemReturn` 目前是 `echarts.options.GraphicElement?` 的近似实现。
- 上游是 `CustomRootElementOption | undefined | null`，需要新建除选项树：
  `CustomBaseElementOption`（`type` / `info` / `textContent` / `clipPath` / `tooltipDisabled` / `extra` /
  `during` / `enter|update|leaveAnimation`）、`CustomDisplayableOption`（`style` / `emphasis` / `blur` /
  `select` / `z` / `zlevel` / `z2` / `invisible`）、`CustomPathOption`（含 `type` 字面量联合与 `BuiltinShapes` 的 `shape`）、
  `CustomImageOption`、`CustomTextOption`、`CustomGroupOption`、`CustomCompoundPathOption`、
  `ElementKeyframeAnimationOption` 与 `TransitionOptionMixin`。
- 顺带：`CustomSeriesRenderItemAPI.visual(type)` 的入参目前是 `String`，上游是
  `NonStyleVisualProps | StyleVisualProps`（`'color' | 'borderColor' | 'symbol' | ...`），可补字面量类型。

### 5. 仪表盘数据项

- 上游 `GaugeSeriesOption.data?: (OptionDataValueNumeric | GaugeDataItemOption)[]`，
  `GaugeDataItemOption` 支持逐项覆盖 `pointer` / `progress` / `title` / `detail`。
- 现在 `data` 走通用的 `Array<OptionDataItem?>`：数值可用，但逐项覆盖对象无法精确表达。
  （`OptionDataValueNumeric` 联合已经在 `CommonTypes.kt` 里备好。）

### 6. 主题河流与其它数据项类型

- `ThemerRiverDataItem = [OptionDataValueDate, OptionDataValueNumeric, string]`（三元组）未建模；
  需要 `OptionDataValueDate`（`Date | string | number`）与一个元组工厂（参考 `gaugeColorStop`）。
- 其余系列的 `*DataItemOption`（boxplot / candlestick / heatmap / lines / parallel / pictorialBar /
  themeRiver / funnel / pie / map / radar / line / bar / scatter）大多没有单独声明，`data` 走通用联合。
  按上游逐个补，能给 `data` 带来更精确的类型（但要先想清楚基接口 `data` 的处理，见第 8 条）。

### 7. 基接口 `data` 的处理（设计问题）

- 上游 `SeriesOption.data?: unknown`，各具体类型收窄；Kotlin 基接口现在是
  `Array<OptionDataItem?>?`，层级图节点靠继承 `OptionDataItemObject` + `items(...)` 塞进去。
- 若要完全精确，需要按 TYPE-MAPPING 第 12 节把 `data` 从基接口下放到每个具体系列；
  代价是 `option.series[i].data` 这种基类访问会消失（`OptionModelTest` / `LiteralsTest` 里有用例依赖它）。
- 决定前先评估：下放后是否需要给 `EChartsOption.series` 之外再提供一个「只读 data」的视图。

### 8. 图例（legend）的细分类型

- 上游 `LegendOption` 是 `LegendStyleOption + BoxLayoutOptionMixin + BorderOptionMixin + ...`，
  分页字段（`pageIconColor` / `pageTextStyle` / `pageFormatter` / `scrollDataIndex`）其实属于
  `ScrollableLegendOption`，`LegendLineStyleOption`（`inactiveColor` / `inactiveWidth`）与
  `LegendItemStyleOption`（`borderWidth: 'auto'` 等 `inherit` 扩展）也没有单独建模。
- 现状把分页字段都摊在 `LegendOption` 上（宽度更宽），需要复核后决定是否细分。

### 9. 已确认「保持现状」的项（不要重复修）

- `LegendDataItemOption.tooltip` 保持 `JsAny`：上游就是 `DataItem.tooltip?: unknown`，是正确映射。
- `MapSeriesOption.markPoint` / `markLine` / `markArea`：上游声明为 `any`，Kotlin 侧继承基接口的具名类型
  （比上游更严格）属于有意选择。
- `CustomSeriesRenderItemParams.itemPayload` / `context` 的值类型：上游 `Dictionary<unknown>`，
  值用 `JsAny`，键已用 `Record<String, JsAny>`。
- `GraphSeriesOption` 里已删除的 `directed` / `edgeShape` / `edgeForkPosition` / `selectedMode`：
  在 6.1.0 的 `.d.ts` 的 `GraphSeriesOption` 里查不到（`edgeShape` / `edgeForkPosition` 只属于 tree 系列）。
  如果后续上游版本又加回来，按新 `.d.ts` 恢复。

### 10. 上游导出清单的全量复核

- 本轮只补了「已知空缺」的成员，没有重新逐个核对 `echarts` 的公开导出（`EChartsType` / `ChartView` /
  `SeriesModel` / `ComponentModel` / `Payload` / 各 `*Payload` / `*Event` 等）与各入口
  （`echarts/core`、`echarts/charts`、`echarts/components`、`echarts/features`、`echarts/renderers`）的差异。
- 建议单独开一轮：按 `custom-elements.json` 式的清单方式，从 `types/dist/*.d.ts` 生成导出清单再逐项比对。
