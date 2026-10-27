# demo

官方 [ECharts 示例库](https://github.com/apache/echarts-examples) 的 Kotlin/JS 移植站点：每一页对应
`public/examples/ts` 里的一个示例，路由 id 与官方示例 id 完全一致（`#/bar-simple` ⇄
`https://echarts.apache.org/examples/zh/editor.html?c=bar-simple`）。这一层只消费 wrapper 的公开 API，
不导入 `ktjs-echarts` 内部实现。

## 目录结构

```
demo/src/jsMain/kotlin/demo/
  Main.kt            入口：先定语言与主题，再建 shell，最后启动路由
  Language.kt        中英双语与 LanguageState（localStorage 记忆）
  Theme.kt           亮/暗主题（<html data-theme>，localStorage 记忆）
  Router.kt          hash 路由
  AppShell.kt        侧边栏目录 + 顶栏 + 内容区；页面缓存、图表随页面挂载/卸载
  Catalog.kt         Groups、CatalogEntry 与整份目录的汇总
  ui/Dom.kt          类型安全的 DOM 构造与小工具
  ui/Blocks.kt       页面骨架（page / pageSection / example）与 PageMeta / PageView
  ui/Chart.kt        ChartHolder：容器的持有者，负责 ECharts 实例的挂载、卸载与 resize
  examples/          一个图表家族一个文件，每份导出 <name>Entries
demo/src/jvmTest/kotlin/demo/e2e/
  DemoPageTest.kt    Playwright 端到端套件（点侧边栏、切语言/主题、过滤索引、抽样渲染）
  DemoOptions.kt     浏览器与断言超时设置
  DemoServer.kt      测试内静态服务器
```

## 移植约定

- **一页一示例、id 与官方一致**：路由 id 就是官方示例 id，页面上的「官方示例」链接由此拼出。
- **每个示例一族一文件**：`examples/Bar.kt`、`examples/Pie.kt`…；文件里先用
  `Category(Groups.X, "examples/X.kt")` 绑定分组与源码路径，再用 `chart(...)` /`interactive(...)`
  声明示例。文件内的声明顺序就是侧边栏顺序。
- **静态示例用 `chart { option }`**：选项在挂载时构造（懒构造，避免建页时白算数据）。
  **需要自己驱动图表的用 `interactive { chart -> }`**：定时动画、`getWidth()` 相关布局、事件等。
- **中文标题抄官方清单**（`echarts-examples/src/data/chart-list-data.js` 的 `titleCN`），
  英文标题抄 `title`；这样侧边栏与官方站点一致，也便于脚本核对。
- **数值数据原样搬运**：官方示例里的数组、表格直接写进 Kotlin；`Math.random()` 一律换成固定序列
  （见各文件的 KDoc），保证页面可复现、端到端断言稳定。
- **通配导入**：`examples/` 下的文件统一用 `import echarts.options.*` / `import echarts.*`。一个示例
  通常触及大半个选项包，逐个列举反而更容易漏；`detekt` 的规则集（只有 `ForbiddenImport`）不禁止它。
- **数据项名称与取值走联合工厂**：`name` 是 `string | number`、`value` 是 `string | number | Date`，
  所以写 `OptionId("x")`、`OptionDataValue(1.0)`；`examples/Items.kt` 收了这批常用构造器
  （`pieItem`、`radarItem`、`row`、`doubleRow`、`recordOf`、`toolbox`、`every`…）。
- **KDoc 里别写 `/*`**：Kotlin 的块注释可嵌套，注释里写 `doc-example/*.js` 会开一个内层注释，
  整个文件后半段都被当成注释，报错位置还不在这一行。写目录时用 `doc-example/` 就好。
- **`Array<Double>` 不能顶 `vararg Double`**：`numbers(*values)` 要求 `DoubleArray`，集合先
  `toDoubleArray()`；反过来 `Array<T?>`（`strings` / `numbers` / `dataItems` 的返回）也不能直接赋给
  需要非空元素的字段。
- **手写 `CustomSeriesOption` 必须带 `type`**：用 `customSeries { }` 时由构造器补上，直接
  `unsafeJso<CustomSeriesOption> { }` 时不会，ECharts 会静默丢掉整个系列（格子空白、label 堆在原点）。
- **矩阵表头有几层就写几层**：`MatrixDimensionOption.data` 里父单元格要用 `children` 挂出叶子，
  数据里引用的单元格名必须能落到叶子上；只写五个父单元格时 25x25 的热力图整页空白。
- **`rich` 必须与 formatter 的 `{tag|...}` 一一对应**：缺了 `rich` 的定义，ECharts 把 `{b|{b}：}` 原样
  当文本画出来（`pie-nest` 踩过）。
- **`graphic` 元素定位用 `left` / `top`**：`x` / `y` 是 zrender 原始属性，只认数字，传百分比字符串会
  落到原点（`matrix-confusion` 的「True Class」踩过）。

## 静态数据集

官方示例里有相当一部分数据是几十上百 KB 的 JSON（磁盘树、航线表、世界地图 GeoJSON、NBA 球场……）。
这些**不转成 Kotlin 常量**，而是放在 `demo/src/jsMain/resources/data/`，随 `jsProcessResources` 一起进
`dist`，页面用 `demo.ui.loadJson("data/<name>.json")` 取。目录里的 `SOURCE.md` 记来源（Apache-2.0，拷自
`apache/echarts-examples` 的 `public/data/asset/{data,geo}`）与每个文件被谁使用，`LICENSE` 是上游许可证
原文。

写这类示例的规矩：

- 用 `interactive("id", …) { chart -> loadJson(…).then { chart.setOption(…); null } }`，`then` 里返回
  `null` 以满足 `Promise<…>`；不要试图让 `chart { option }` 同步等待数据。
- 需要地图的先 `ECharts.registerMap("world", MapInput(unsafeJso<GeoJSONMapInput> { geoJSON = … }))`，
  再 `setOption`；`world.json` 是裸 GeoJSON，要包一层 `{ geoJSON: … }`。
- 数据没有静态类型，用示例文件里的 `external interface` 描述用到的字段，再 `unsafeCast` 一次；不要把
  整个 JSON 当 `dynamic` 传下去。
- e2e 里这些页面要等像素（`waitForPainted`），不能只在路由切换后采样一次。
- 文件名必须与上游一致，方便把示例源码和数据集对上。

## 选例范围

目标是「只依赖官方仓库里能拷过来的数据的 option 示例」，数据用静态文件解决（见上一节）。用脚本按
`chart-list-data.js` 逐条判断，排除：

- 需要百度地图的（清单里 `tags: ["bmap"]`），
- 需要 `echarts-gl` 的（`public/examples/ts/gl`，wrapper 没有覆盖），
- 依赖官方站点图片素材的（`image://` 图标、纹理贴图、`data:image/...` 大块 base64），
- 依赖真实键鼠序列的（拖拽、触摸、`sendInput`）。

`setInterval` / `setTimeout` 驱动页面的限制已经解除：`ChartHolder` 会登记定时器并在卸载时清理，
`Category.interactive` 的双参重载把 holder 交给例子。

筛选后仍有 250+ 个候选，目前已落地 **199 个**（覆盖 25 个家族 + 一个「文档示例」组），其余按批推进。

「文档示例」组（`Groups.DOC`）对应官方清单里的 `doc-example/*` 条目：它们不属于任何家族，一个例子只讲
一个字段。路由 id 保留 `doc-example/` 前缀，官方链接才指向同一页。这批页面按依赖分三个文件：
`examples/DocExamples.kt`（静态页，28 个）、`examples/DocMore.kt`（象形柱图教程、dataZoom 散点、
文本 / 标签、动画、旭日图 / 矩形树图入门与 `baseOption` + `media` 教程，16 个）与
`examples/DocInteractive.kt`（首次绘制后还要 `setOption` / `dispatchAction` / 起定时器，或使用 timeline
组件的 10 个）。

### 尚未移植的家族与原因

| 家族 | 情况 |
| --- | --- |
| `bmap` / `echarts-gl` | 需要百度地图或 WebGL 渲染器，wrapper 没有覆盖 |
| `graphic` | `EChartsOption.graphic` 已能表达组件与元素，但官方 `graphic` 家族的多数示例还配合
  真实交互或外部素材，未成批移植 |
| `visualMap` / `dataZoom` 单列家族 | 官方清单里这几条恰好都属其它被排除的类别；能力已由别的示例覆盖 |

`lines` / `map` / `geo` 已经能用静态数据集（`lines-airline`、`custom-hexbin` 就是先例）：只要再拷
`public/data/asset/geo/` 下的 GeoJSON 或 SVG，就能继续补这两个家族。

个别被移植家族内的长尾示例（动画竞速、下钻、真实指针交互）留待下一批，例如 `bar-race`、
`bar-drilldown`、`line-easing`、`scatter-clustering`、`custom-gantt-flight`、
`custom-gauge`（需要 `path://` 面板图）、`custom-calendar-icon`（图标是 `path://` 素材）。

`doc-example/` 里还没有移植的（24 条，其中 `bar-large` / `pie-highlight` 已按家族 id 落地）：

- 象形柱图长尾：`pictorialBar-graphicType`、`pictorialBar-patternSize`、
  `pictorialBar-repeatDirection`、`pictorialBar-repeatLayout`；
- 单字段小页：`title-block`、`bar-media-timeline`；
- 第二个渲染器 / 异步设 option：`canvas-vs-svg`（含 `-en`）、`tutorial-async`；
- 地图与 visualMap 组合：`map-example`、`map-visualMap-continuous`、`map-visualMap-continuous-text`、
  `map-visualMap-pieces`、`map-visualMap-piecewise-text`、`scatter-visualMap-categories`、
  `scatter-visualMap-piecewise`、`geo-svg-label-basic`、`geo-svg-layout-basic`、
  `geo-svg-named-basic`、`geo-roam-indicator`、`graph-roam-indicator`；
- timeline 组件：`mix-timeline-all`；
- 外部图片素材：`axisPointer-handle-image`。

## 移植过程中修掉的 wrapper 缺口

移植是 wrapper 的一次实测；以下缺口已在 `ktjs-echarts` 里补齐（见该模块的 KDoc）：

- `SplitLineOption.lineStyle` / `SplitAreaOption.areaStyle`：上游是
  `LineStyleOption<ZRColor | ZRColor[]>` / `AreaStyleOption<ZRColor[]>`，新增
  `SplitLineStyleOption` / `SplitAreaStyleOption` 与 `ZRColorOrArray`。
- `PieSeriesOption`：补 `animationType` / `animationTypeUpdate`（新增
  `PieAnimationTypeUpdate` 字面量）与 `BoxLayoutOptionMixin`（`left`/`top`/`width`…）。
- `FunnelSeriesOption` / `SankeySeriesOption` / `ChordSeriesOption` / `ThemeRiverSeriesOption`：
  补上游声明的 `BoxLayoutOptionMixin`。
- `ChordSeriesOption.radius`：上游允许 `[inner, outer]`，放宽为 `StringOrDoubleOrArray`。
- `DataZoomOption`：补 `BoxLayoutOptionMixin`（上游只有 slider 变体带盒子布局，wrapper 合成了一个）。
- `EmphasisFocuses`：补 `adjacency` / `ancestor` / `descendant`（sankey、chord、graph、tree 专用取值）。
- `OptionEncode`：上游是开放索引签名，补上文档列出的坐标维度 `x` / `y` / `value` / `radius` / `angle`。
- `TreeSeriesOption.orient`：上游是 `TreeOrient`（`LR` / `TB` / `RL` / `BT` / `horizontal` / `vertical`
  或 `number`），之前错用了通用的 `Orient`；新增 `TreeOrient` / `TreeOrients`。
- `MatrixOption` 系列：`MatrixDimensionOption.data` 改成上游的 `MatrixDimensionCellLooseOption[]`（单元格
  对象或纯字符串），补 `MatrixCellStyleOption`（`label` / `itemStyle` / `cursor` / `silent` / `z2`）、
  `MatrixDimensionOption.levelSize`、`MatrixBodyCornerCellOption.coord`（`MatrixCoordRangeOption[]`）、
  `coordClamp`、递归的 `children`。
- `GridOption`：补 `ComponentOption` 继承来的 `coordinateSystem` / `coord`（矩阵布局的网格需要）。
- `EChartsOption.graphic`：从 `Array<GraphicElement>` 改成上游的 `GraphicComponentLooseOption[]`
  （`GraphicComponentOption` 带 `elements`，或裸元素），元素补 `x` / `y` / `width` / `height`。
- `CustomSeriesRenderItemAPI`：补 `coord` / `size` / `layout`（新增 `CoordinateSystemDataCoord` 联合），
  `params.coordSys` 补矩形布局的 `x` / `y` / `width` / `height`；`CustomSeriesOption` 补 `itemPayload`。
- `GraphicNamespace.clipRectByRect` / `clipPointsByRect`：参数与返回值从 `JsAny` 收紧到 `RectLike`。
- `AngleAxisOption`：补上游声明的 `startAngle` / `endAngle` / `clockwise` / `polarId`（极坐标的三个角度
  示例就靠它们）；`RadiusAxisOption` 补 `polarId`。
- `AxisLabelOption`：补 `alignMinLabel` / `alignMaxLabel` / `verticalAlignMinLabel` /
  `verticalAlignMaxLabel`（`axis-label-align-min-max`）。
- `MatrixOption` 的单元格数据（`matrix-mini-bar-data-collection`）：`MatrixDimensionOption.data` 支持由
  `dataset` 自动收集，只需按上游写法只给 `levelSize` / `itemStyle` / `label`。
- `BarSeriesOption` / `PictorialBarSeriesOption` 的 `barGap` / `barCategoryGap`：上游
  `BaseBarSeriesOption` 声明为 `string | number`，wrapper 之前只允许 `String?`；现已放宽为
  `StringOrDouble?`（`value-animation-simple` 的 `barCategoryGap: 200` 需要）。烛台图不继承
  `BaseBarSeriesOption`，字段保持原样。
- `TimelineOption`：补 `replaceMerge`（`timeline-dynamic-series` 用 `'series'` 让切换播放点时整组
  替换系列，否则从 3 个切回 1 个会残留）与 `show`。
- `PictorialBarDataItemOption`：上游继承 `AnimationOptionMixin`，wrapper 漏了 `AnimationOption`
  （`pictorialBar-clip` 背景系列的 `animationDuration: 0` 需要）。
- `GraphicElement.rotation`：KDoc 原先写「角度」，实际 zrender 元素读的是弧度
  （`graphic-bounding` 按弧度做匀速旋转），已改正。
- `ToolboxFeatureOption`：`dataZoom` / `brush` 上游是带自己字段的子接口，wrapper 之前统一用基接口，
  现在分别改为 `ToolboxDataZoomFeatureOption`（`type` / `filterMode` / `xAxisIndex` / `yAxisIndex` /
  `xAxisId` / `yAxisId` / `brushStyle`）与 `ToolboxBrushFeatureOption`（`type: BrushToolboxIconType[]`），
  子接口仍是基接口的子类型，原有写法不受影响；配套新增 `ModelFinderIdQuery` 联合。
- `MarkLineStateOption.label`：上游是 `SeriesLineLabelOption`（`LineLabelOption` 的派生），`distance`
  被放宽成 `number | number[]`、`position` 收窄成 start / middle / end 关键字。Kotlin 不能覆盖继承来的
  `var distance`，所以新增 `MarkLineLabelOption`，数组形式经 `typedDistance` + `@JsName("distance")`
  写入同一个 JS 字段。
- `TimelineOption.data` 的数据项：补上游 `TimelineDataItemOption`（`tooltip` / `itemStyle` / `label` /
  `checkpointStyle` / `emphasis` / `progress`）与 `TimelineDataItemEmphasisOption`，配套新增
  `BooleanOrTooltipOption`（上游写 `boolean`，但 timeline 视图会解析成 option 对象，官方示例即这么传）。
- `GraphicElement`：补上游声明在元素基接口上的 `textContent` / `textConfig`，让矩形一类元素能直接挂
  文本与位置配置，不必再往 group 里塞第二个 text 元素。
- `DecalObject.rotation`：KDoc 原先写「角度」，zrender 的 `Pattern.rotation` 读的是弧度（官方 decal
  示例一律传 `Math.PI` 的倍数），已改正；`aria.decal` / `itemStyle.decal` 一族的联合
  （`DecalSetting` / `DecalList` / `DecalDashArrayX`）已补用例覆盖。

## 兜底说明：图表与主题

ECharts 实例在**页面可见时才创建**（`ChartHolder.mount()`），换页时 `dispose()`；容器节点本身不重建。
`ECharts.init` 用的是 ECharts 自带主题名：暗色模式传 `"dark"`，所以切换主题会重建当前页面。

## 端到端测试

```bash
./gradlew :demo:e2eTest
```

它打包 `jsBrowserDistribution`，用 Playwright 驱动 Chromium：走侧边栏、切换语言与主题、过滤索引，
并**逐页**打开目录里的每个示例，断言画布有实际尺寸且控制台无报错。早先按 1/6 抽样，结果漏掉了
`themeRiver-lastfm`（表比专辑名长，构造 option 时越界）这类只在个别页出现的问题。`jvmTest` 本身被
禁用，以免 `build` / `check` 依赖浏览器。
