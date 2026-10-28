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
- 文件名必须与上游一致，方便把示例源码和数据集对上。上游把数据内联在示例源码里、体量又太大时
  （`grid-multiple` / `area-rainfall` 各 3079 点），可以由一次性脚本拆成 `data/<name>.json`，
  在 `SOURCE.md` 里把上游目录一栏标成 derived 并写清是从哪个示例拆出来的。

## 选例范围

目标是「只依赖官方仓库里能拷过来的数据的 option 示例」，数据用静态文件解决（见上一节）。用脚本按
`chart-list-data.js` 逐条判断，排除：

- 需要百度地图的（清单里 `tags: ["bmap"]`），
- 需要 `echarts-gl` 的（`public/examples/ts/gl`，wrapper 没有覆盖），
- 需要另一个 npm 包的（`bar-histogram` 一类要先 `echarts.registerTransform(ecStat.transform.histogram)`，
  数据在 `echarts-stat` 里，wrapper 与 demo 都还没有引这个包），
- 依赖官方站点图片素材的（`image://` 图标、纹理贴图、`data:image/...` 大块 base64），
- 依赖真实键鼠序列的（拖拽、触摸、`sendInput`）。

`setInterval` / `setTimeout` 驱动页面的限制已经解除：`ChartHolder` 会登记定时器并在卸载时清理，
`Category.interactive` 的双参重载把 holder 交给例子。

筛选后仍有 250+ 个候选，目前已落地 **275 个示例**（覆盖 26 个家族 + 一个「文档示例」组，另有一张总览页），
其余按批推进。

「文档示例」组（`Groups.DOC`）对应官方清单里的 `doc-example/*` 条目：它们不属于任何家族，一个例子只讲
一个字段。路由 id 保留 `doc-example/` 前缀，官方链接才指向同一页。这批页面按依赖分三个文件：
`examples/DocExamples.kt`（静态页，29 个）、`examples/DocMore.kt`（象形柱图教程、dataZoom 散点、
文本 / 标签、动画、旭日图 / 矩形树图入门与 `baseOption` + `media` 教程，16 个）与
`examples/DocInteractive.kt`（首次绘制后还要 `setOption` / `dispatchAction` / 起定时器，或使用 timeline
组件的 12 个）。

### 尚未移植的家族与原因

| 家族 | 情况 |
| --- | --- |
| `bmap` / `echarts-gl` | 需要百度地图或 WebGL 渲染器，wrapper 没有覆盖 |
| `graphic` | 关键帧动画那一档已落地（`examples/GraphicAnimations.kt`，见下），多数剩余示例还配合真实交互或外部素材 |
| `visualMap` / `dataZoom` 单列家族 | 官方清单里这几条恰好都属其它被排除的类别；能力已由别的示例覆盖 |

`lines` / `map` / `geo` 已经能用静态数据集（`lines-airline`、`custom-hexbin` 就是先例）：只要再拷
`public/data/asset/geo/` 下的 GeoJSON 或 SVG，就能继续补这两个家族。

竞速、下钻那一档已经落地：`bar-race`（每 3 秒加一截，定时器交给 `ChartHolder`）与
`bar-drilldown`（点击柱体换层，`universalTransition` 做形变，Back 是带 `onclick` 的 graphic
text）在 `examples/CanvasGraphics.kt`，多层版 `bar-multi-drilldown` 单独放在
`examples/BarDrilldown.kt`：上游用 `myChart.getOption().id` 记住当前层，wrapper 不建模这个写在
option 上的野字段，改成在例子侧用 `ArrayDeque` 记下钻路径。
`watermark` 同文件（canvas 画的斜向水印当
`backgroundColor` 的 pattern）。剩下的长尾里还要真实交互的（`scatter-clustering` 要 ecStat、
`custom-calendar-icon` 上游只剩缩略图、`line-pen` / `line-draggable` 要指针序列）仍不做。

`path://` 素材不是障碍：上游的 path 字符串原样内联即可，gauge 家族的五个面板图就是这么落地的
（`examples/GaugeLongTail.kt`，同时把 `Math.random()` 换成 `Lcg`、定时器交给 `ChartHolder`），
`custom-gauge` 照这个先例做。象形柱图的四页 `pictorialBar-vehicle` / `-body-fill` / `-velocity` /
`-bar-transition` 也照此落地（`examples/PictorialBarLongTail.kt`，path 常量与身高体重表分别由
一次性脚本生成到 `PictorialBarPaths.kt` / `ScatterWeightData.kt`）；上游 `pictorialBar-body-fill`
在根级写的 `markLine: { z: -100 }` 没有移植，因为 wrapper 的 `EChartsOption` 不建模根级 mark 组件，
该字段只影响层级、不影响画面。

## 数据自己算的页面

`examples/IntradayBreaks.kt` 收了四个「数据不是抄的、是算出来的」页面，它们共用一个种子固定的
`Lcg`：

- `intraday-breaks-1` / `-2`：逐分钟价格 + 断轴。时间一律用 `Date.UTC` 从「天」推，**不要**
  照抄上游那种「时间戳加加减减」的写法：第一版把夜间的跨天算错，`time` 不前进，页面直接卡死
  （Chromium 报 `Page crashed`，Playwright 看不到任何错误）。现在外层按天循环、内层按分钟循环。
  `breaks[].gap` 与 `breakLabelLayout.moveOverlap` 都是 `StringOrDouble` / `BooleanOrString`，
  传裸 `0` / `false` 编译不过，要包一层。
- `area-simple`：两万点随机游走 + `sampling: 'lttb'`。tooltip 的 `position` 回调是五参数
  （`point` / `params` / `el` / `rect` / `size`），别按一参数写。
- `candlestick-large`：二十万根 K 线走 `dataset`。`dataset.source` 必须**只算一次**：把
  `largeCandles()` 放进循环里会重算二十万行 × 二十万次。涨跌颜色靠额外的 `sign` 列 + 隐藏的
  piecewise `visualMap`，和上游一致；`itemStyle` 得用 `CandlestickItemStyleOption`
  （`color0` / `borderColor0` 只在它上面）。
- `scatter-matrix`（`examples/ScatterMatrix.kt`）：网格的 `left` / `top` / `width` / `height`
  必须是**百分比字符串**。上游算的是数字再拼 `+ '%'`，Kotlin 侧如果直接写
  `StringOrDouble(11.5)` 就是 11.5 像素，十六个单元格会全叠在左上角（画出来只有一小块，
  e2e 仍然过，只能靠截图发现）。
  分类 visualMap 的 `dimension` 指向的数据值必须是**城市名字符串**：上游把城市塞在元组里，
  Kotlin 侧要用 `row(x, y, city)` 而不是把城市换成索引，否则所有点都落到 `outOfRange` 的灰色。

## 轴标签的多参回调

轴 / 系列的 formatter 上游常带额外的第三、四个参数（断轴页的 `extra.break`、timeline 的
`extra`），而 wrapper 的 `LabelFormatterCallback` 只建模了第一个参数。写法是给回调套一层
JS 适配：Kotlin 侧写成 `(value: JsAny, extra: JsAny?) -> String`，再用
`js("(fn) => (value, index, extra) => fn(value, extra)")` 拓宽成三参（见
`examples/IntradayBreaks.kt` 的 `axisLabel(::fn)`）。时间格式化走
`ECharts.time.format(value, template, true)`，第三个参数是 UTC。

## 家族长尾的分布

line 家族的数据生成长尾在 `examples/LineLongTail.kt`：两万点的 `area-time-axis`、按 easing 函数逐个
建 grid 的 `line-easing`、每秒滚动窗口的 `dynamic-data2`、名次随机但种子固定的 `bump-chart`、用
`graphic` 浮层画横幅与说明框的 `line-graphic`，以及 piecewise visualMap 涂面积、双 x 轴、
双 continuous visualMap、渐变堆叠面积图这几条 option 组合。随机序列同样换成 `Lcg`。

bar 家族的计算长尾在 `examples/BarLongTail.kt`：归一化堆叠（`bar-stack-normalization`）和它的
`graphic` 多边形变体（`-and-variation`，因为要用图表实际宽高摆多边形，走 `Category.interactive`）、
只有真正封住栈顶的柱子才带圆角的 `bar-stack-borderRadius`、透明占位柱顶起的 `bar-waterfall2`，
以及带 `markPoint` / `markLine` 的 `bar1`。上游根 option 上的 `calculable: true` 是 ECharts 6
已经不读的老字段，wrapper 里也没有对应声明，移植时直接去掉。
`graph-grid`（笛卡尔坐标系上的 graph）补在 `examples/GraphExtra.kt`，同文件还有 `graph-force-dynamic`：
节点与边靠 `ChartHolder.setInterval` 每 200ms 长出来，随机源换成种子固定的 `Lcg`，节点 id 从 `"2"`
开始（上游就是这么漏掉 `"0"` / `"1"` 的，照抄）。

`graphic` 家族的关键帧动画三页在 `examples/GraphicAnimations.kt`：描边文字（`graphic-stroke-animation`）、
七条加载柱（`graphic-loading`）和噪点驱动的圆点波浪（`graphic-wave-animation`，要读 `chart.getWidth()`
所以走 `Category.interactive`）。三页都不设定时器，动画交给元素的 `keyframeAnimation`；波浪页上游的
`app.config` 面板只做「改参数再 `setOption`」，参数固定成上游默认值（含 `offsetY: 100`）后面板没有存在
意义，直接去掉。上游的 `Math.random()` 噪声种子换成 `0.42`，否则每页每次刷新都是另一张画布，e2e 的
帧比对就无从下手。noisejs 的那段 perlin 实现移植成 `examples/PerlinNoise.kt`，波浪动画与两张大数据
热力图共用它。

大数据热力图在 `examples/HeatmapLongTail.kt`：`heatmap-large`（continuous visualMap）和
`heatmap-large-piecewise`（piecewise + `splitNumber: 8`）共用一份 201×101 的噪点场，靠
`progressive: 1000` 与 `animation: false` 撑住两万格。上游的 y 轴只列 `0..99` 却采样到 `j = 100`，
最后一行没有类目、永远不显示，这一点照原样保留并在文件头注明。

`cycle-plot`（周期图）在 `examples/CustomLongTail.kt`：x 轴是月份，每年在自己的类目带里画一条折线，
`renderItem` 用 `api.size([0, 0])[0]` 取带宽再按 `(year - rawData.length / 2)` 平移，均值系列画带中央的
横线。上游 `rawData.length / 2` 是 `5.5`，Kotlin 侧要写成 `/ 2.0`，整数除法会把偏移算成 `5`。

`pie-parliament-transition` 在 `examples/CustomTransition.kt`：饼图与自定义系列共用 series id
`distribution`，`universalTransition` 把扇形拆成议会席位圆点，`ChartHolder.setInterval` 每两秒换一次
option。`renderItem` 的排座算法（环半径、按座位宽度对齐的起点）照抄上游；上游那句
`coordinateSystem: undefined` 是没有作用的死字段，移植时直接不写。

`calendar-effectscatter` 追加在 `examples/Calendar.kt`：上下两张半年历，散点用 `Lcg` 造 365 天的
成交额，Top 12 再叠一层 `effectScatter` 涟漪。日历的 `splitLine.lineStyle.color` 在 wrapper 里叫
`typedColor`（`ZRColorOrArray`，`@JsName("color")`），`type` 用 `ZRLineType`；标题与图例的
`textStyle` 是 `LabelOption`，它的 `color` 是 `String` 而不是 `ZRColor`，写 `ZRColor("#fff")` 编译不过。

dataset 家族的 `dataset-link` 补在 `examples/DatasetExtra.kt`：它监听 `updateAxisPointer` 把饼图的
`encode.value` 换成指针所在的年份，上游该事件的 payload 类型是 `any`，所以 `axesInfo[0].value` 只能在
demo 侧用 `js()` 取；上游还用一个 `setTimeout` 保证监听器在首帧之后挂上，这里直接 `chart.on(...)`。
合并选项里上游写的是 `series: {id: 'pie', ...}` 对象形式，wrapper 的 `series` 只有数组形式，而 ECharts
本来就按 `id` 合并，两者行为一致。

`doc-example/` 里还没有移植的（上游 80 个文件 = 79 个 `.js` + 1 个 `.ts`、已按 `doc-example/` id 落地
57 个、`bar-large` / `pie-highlight` 按家族 id 落地，剩 21 条）。按原因分类：

- 图标是官方站的 PNG / base64 素材，按排除策略不做：`pictorialBar-graphicType`、
  `pictorialBar-patternSize`、`pictorialBar-repeatDirection`、`pictorialBar-repeatLayout`、
  `title-block`、`axisPointer-handle-image`；
- 运行期远端脚本：`bar-media-timeline`（`$.getScript` 拉 `timelineGDP.js` 与 `draggable.js`）；
- 地图与 `geo`：`map-example`、`map-visualMap-continuous`、`map-visualMap-continuous-text`、
  `map-visualMap-pieces`、`map-visualMap-piecewise-text`、`scatter-visualMap-piecewise`（系列用
  `coordinateSystem: 'geo'`）、`geo-svg-label-basic`、`geo-svg-layout-basic`、`geo-svg-named-basic`、
  `geo-roam-indicator`；
- 真实指针漫游 + 官方站的 `app.config`：`graph-roam-indicator`；
- 下一批的可移植项：`scatter-visualMap-categories`（内联 dataset + 两个 `visualMap`，但
  `inRange.symbol` / `outOfRange.symbolSize` 是按类目索引的字典、`left: null`，要先放宽 wrapper）、
  `canvas-vs-svg`（含 `-en`，只有两张画布分别用 canvas / SVG 渲染器才有意义，要先给站点加渲染器开关）。

`doc-example/` 之外的数据页也按「先拷数据再做」推进：`sankey-energy` / `sankey-levels` /
`sankey-nodeAlign-left` / `-right` 用 `energy.json` 与 `product.json`（`sankey-itemstyle` 的 3000 行表由
示例服务器现算，本仓库没有那份数据，仍不做），`confidence-band` 用 `confidence-band.json`，
`scatter-painter-choice` 用 `masterPainterColorChoice.json`。后者有两个坑：`x` 列是 plotly 导出的**字符串**
（照上游的一元 `+` 用 `Number(...)` 转换），`itemStyle.color` 是按 `dataIndex` 取色的回调（wrapper 的
`ZRColor(callback)` 就是为它加的）。

`scatter-large` 的两条 50 万点序列用 `Lcg` 生成、`dynamic-data` 的两条滚动序列同样换成 `Lcg`，
定时器交给 `ChartHolder`，所以两页每次刷新都是同一张（能稳定断言）的动画。

`bar-rich-text` 与 `pie-rich-text` 是第一次用到「图片当标签背景」：图标放在
`resources/data/img/weather/`，用 `ImagePatternObject` + `Image` 构造 pattern 再经 `ZRColor(pattern)`
包成颜色。富文本里只有图片没有文字的 tag（`{Sunny|}`）没有固有宽度，**必须给这个 tag 显式 `width`**，
否则 pattern 被画进零宽盒子、看起来像图标丢了。

candlestick 家族的两张长表在 `examples/CandlestickLongTail.kt`：`candlestick-sh`（上证指数 2013 年
上半年，88 行）与 `candlestick-sh-2015`（2015 全年，246 行）。表由一次性脚本转成 `OhlcDay` 数组，
`calculateMA` 的等价实现按 `window` 内收盘价求和，前 `window - 1` 个点写 `OptionDataValue("-")`，
均线才从窗口满格处开始画。2015 的上游表本身是「最后一天在前」再 `reverse()` 的，反过来的表格开头
多出两行空记录（`+'' === 0`），它们最终是图表最左侧那两根零值蜡烛，顺序不能重排。
`markPoint` 的 tooltip formatter 要 `unsafeCast<CallbackDataParams>` 才拿得到 `name`；`markLine` 的
标签用 `MarkLineLabelOption`（`distance` 可以是二元数组，`LabelOption` 装不下）。

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
  `itemChildGroupId` 之前拼成了 `childGroupdId`（对照 `apache/echarts/src/util/types.ts` 修正），
  `OptionEncodeTest` 用它把三个分组维度的属性名钉住——多层下钻的 `bar-multi-drilldown` 就靠它。
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
- `GaugeSeriesOption.clockwise`：上游 `GaugeSeries.ts` 声明 `clockwise?: boolean`（仪表盘指针的增数
  方向），wrapper 漏了；`gauge-car` 的档位盘用 `clockwise: false` 逆序刻度，已补上并在
  `SymbolAndKeywordOptionsTest` 里断言回读。
- `SeriesOption.coordinateSystemUsage` / `SeriesOption.coord`：上游 `ComponentOption` 声明
  `coordinateSystemUsage?: 'data' | 'box'`（把系列放进宿主坐标系的哪种盒子里）与 `coord`，之前只有
  `GridOption` 带这两个字段。新增 `CoordinateSystemUsage` 字面量与 `CoordinateSystemUsages` 常量、
  复用已有的 `CoordinateSystemDataCoord`；`doc-example/matrix-graph-box` 就靠
  `coordinateSystemUsage: 'box'` + `coord: [2, 1]` 把关系图画进矩阵的某个单元格，
  `MatrixAndGraphicOptionTest.graphSeriesCanUseTheMatrixAsABox` 覆盖这一写法。
- `SankeyEdgeStyleOption.color`：上游是 `ZRColor | "gradient" | "source" | "target"`，`LineStyleOption`
  的 `color` 是窄的 `ZRColor`，Kotlin 不能加宽继承来的 `var`，所以边线样式新增 `SankeyEdgeColor` 联合与
  同名字段 `typedColor`（`TYPE-MAPPING.md` §12 的写法）。
- `ZRColor`：补「按 data item 参数取色」的回调工厂（`itemStyle.color = (params) => ...`），以及此前漏掉的
  `ZRColor(PatternObject)` 重载 —— 没有它，图片 pattern 不是合法颜色，富文本标签挂不上图标。
- `GraphicElement.keyframeAnimation`：上游 `ElementOption.keyframeAnimation` 与自定义系列的
  `CustomElementProps` 共用同一个 `KeyframeAnimationOption`，wrapper 只在后者上声明了它，所以 graphic
  家族那三页没法写动画。现已在 `GraphicElement` 上补 `ElementKeyframeAnimationOptionOrArray`，
  由 `MatrixAndGraphicOptionTest.graphicElementLoopsThroughKeyframes` 断言回读与真实渲染。
- `CandlestickSeriesOption.typedItemStyle`：上游把 `itemStyle` 收窄成 `CandlestickItemStyleOption`
  （多出 `color0` / `borderColor0` / `borderColorDoji`），基接口上的 `ItemStyleOption` 看不到这些字段。
  按 `typedData` 的先例加同名 `@JsName("itemStyle")` 视图，两个属性指向同一个对象。
- mark 数据项与 mark 组件上的 `emphasis` / `blur` / `select`：上游 `MarkPointDataItemOption` 等继承
  `StatesOptionMixin`，`MarkPointOption` 等也带自己的状态样式，wrapper 之前一个都没声明。新增
  `MarkStatesOption` 并混入三组 mark 数据项与三个 mark 组件，
  `SeriesDataItemTest.marksCarryTheGenericStates` 断言回读。

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

`page(meta)` 会给页面根节点写上 `data-route`，`assertChartRendered` / `waitForPainted` 都按
`.page[data-route='<id>'] .chart canvas` 找画布。原因是 `location.hash` 改完，`hashchange` 处理器还要
晚一个任务才跑，shell 在此之前仍挂着上一页：不限定路由的话，等待会在上一页的画布上通过，随后节点被
替换，`boundingBox()` 返回 `null`（这就是那版「chart canvas box of null」偶发失败的根因）。断言因此还
带了重试：读不到尺寸就重新等一次，最多三次，只会让真正有问题的那页慢一点，不会放过它。

浏览器上下文固定 `locale = zh-CN`（`DemoOptions`）。原因是无头容器里常常没有装 CJK 字体，界面切到中文
时字会变成豆腐块，语言切换、主题高亮、对比度三个断言就会随机失败——那不是站点的问题，是环境的字体
问题，所以把语言钉死在中文，让断言与字体无关。
