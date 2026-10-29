# demo

官方 [ECharts 示例库](https://github.com/apache/echarts-examples) 的 Kotlin/JS 移植站点：每一页对应
`public/examples/ts` 里的一个示例，路由 id 与官方示例 id 完全一致（`#/bar-simple` ⇄
`https://echarts.apache.org/examples/zh/editor.html?c=bar-simple`）。这一层只消费 wrapper 的公开 API，
不导入 `ktjs-echarts` 内部实现。

示例覆盖（官方总数、明确不做与待补的逐条清单）见 [COVERAGE.md](COVERAGE.md)；跨页注意事项见
[NOTES.md](NOTES.md)。本文只保留目录结构、移植约定与各家族的具体做法。

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

## 可复用数据与素材

- 打卡表：`examples/CustomExtra.kt` 的 `punchCardValues` / `punchCardHours` / `punchCardDays`。
- 常用表：`resources/data/life-expectancy-table.json`、`nutrients.json`、`population.json`、
  `gl/flights.json`；地图：`world.json`（缩写地名，`GlGeo.kt` 的 `worldRegionNames` 负责翻译）、
  `world-population.json`、`USA.json`、`ch.geo.json`、`iceland.geo.json`。
- GL：11 色 `visualMap` 色带在 `Gl.kt` 的 `glPalette` / `glVisualMap()` 与 `GlSurfaceMaterial.kt` 的
  `surfaceRamp()`；HDR 环境图、皮革贴图、地球 / 月球 / 星空纹理在 `resources/data/img/gl/`，来源见
  `resources/data/SOURCE.md`。

## 选例范围

目标是「只依赖官方仓库里能拷过来的数据的 option 示例」，数据用静态文件解决（见上一节）。用
`scripts/demo-coverage.mjs` 按 `chart-list-data.js` 逐条判断，排除：

- 需要百度地图的（清单里 `tags: ["bmap"]`），
- 需要另一个 npm 包的（`bar-histogram` 一类要先 `echarts.registerTransform(ecStat.transform.histogram)`，
  数据在 `echarts-stat` 里，wrapper 与 demo 都还没有引这个包），
- 依赖官方站点图片素材的（`image://` 图标、纹理贴图、`data:image/...` 大块 base64），
- 依赖真实键鼠序列的（拖拽、触摸、`sendInput`）。

`setInterval` / `setTimeout` 驱动页面的限制已经解除：`ChartHolder` 会登记定时器并在卸载时清理，
`Category.interactive` 的双参重载把 holder 交给例子。

筛选后落地 **326 页示例**（覆盖 27 个家族 + 一个「文档示例」组，另有一张总览页）。按 `echarts-examples`
的官方清单统计，共 436 条：已移植 327 条，明确不做 51 条，待补 58 条；逐条清单与理由见
[COVERAGE.md](COVERAGE.md)，`node scripts/demo-coverage.mjs` 负责重算。

「文档示例」组（`Groups.DOC`）对应官方清单里的 `doc-example/*` 条目：它们不属于任何家族，一个例子只讲
一个字段。路由 id 保留 `doc-example/` 前缀，官方链接才指向同一页。这批页面按依赖分三个文件：
`examples/DocExamples.kt`（静态页，30 个）、`examples/DocMore.kt`（象形柱图教程、dataZoom 散点、
文本 / 标签、动画、旭日图 / 矩形树图入门与 `baseOption` + `media` 教程，16 个）与
`examples/DocInteractive.kt`（首次绘制后还要 `setOption` / `dispatchAction` / 起定时器，或使用 timeline
组件的 12 个）。

### 家族的剩余情况

逐条清单在 [COVERAGE.md](COVERAGE.md)，这里只记家族层面的判断：

| 家族 | 情况 |
| --- | --- |
| `bmap` | 需要百度地图 JS SDK，wrapper 没有覆盖，整个家族不做 |
| `graphic` | 关键帧动画那一档已落地（`examples/GraphicAnimations.kt`，见下），剩余示例都配合真实交互或外部素材 |
| `visualMap` / `dataZoom` | 清单里没有独立家族，这几条随所属系列落地或列入待补（`wind-barb`、`line-tooltip-touch`）；能力已由具体示例覆盖 |

`gl` 家族已经接入（`examples/Gl*.kt` 七个文件，见下文「3D 示例」），剩余页面按
[COVERAGE.md](COVERAGE.md) 的待补清单推进。

`lines` / `map` / `geo` 已经能用静态数据集（`lines-airline`、`custom-hexbin` 就是先例）：只要再拷
`public/data/asset/geo/` 下的 GeoJSON 或 SVG，就能继续补这两个家族。

竞速、下钻那一档已经落地：`bar-race`（每 3 秒加一截，定时器交给 `ChartHolder`）与
`bar-drilldown`（点击柱体换层，`universalTransition` 做形变，Back 是带 `onclick` 的 graphic
text）在 `examples/CanvasGraphics.kt`，多层版 `bar-multi-drilldown` 单独放在
`examples/BarDrilldown.kt`：上游用 `myChart.getOption().id` 记住当前层，wrapper 不建模这个写在
option 上的野字段，改成在例子侧用 `ArrayDeque` 记下钻路径。
`watermark` 同文件（canvas 画的斜向水印当
`backgroundColor` 的 pattern）。剩下的长尾里还要真实交互的（`scatter-clustering` 要 ecStat、
`line-pen` / `line-draggable` 要指针序列）仍不做。

`path://` 素材不是障碍：上游的 path 字符串原样内联即可，gauge 家族的五个面板图就是这么落地的
（`examples/GaugeLongTail.kt`，同时把 `Math.random()` 换成 `Lcg`、定时器交给 `ChartHolder`）。
`custom-gauge` 在 `examples/CustomGauge.kt`：面板 PNG 拷到 `resources/data/img/`，整只表盘由
`renderItem` 返回的一个 group 里的四张元素拼出（两张同款面板图分别用 sector / polygon 裁剪成刻度弧与
指针，再加白色圆盘和数值文本），三种过渡各挂在不同对象上——弧的 `endAngle` 在 shape 上、文本的
`opacity` 在 style 上、指针角度在 `extra` 上，`during` 里用 `getExtra` + `setShape` / `setStyle`
重算。象形柱图的四页 `pictorialBar-vehicle` / `-body-fill` / `-velocity` /
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

graph 家族里读数据文件的 6 页在 `examples/GraphData.kt`：`les-miserables.json` 的四次渲染
（`graph`、`graph-circular-layout`、`graph-force`、`graph-label-overlap`），以及
`npmdepgraph.min10.json` / `webkit-dep.json` 两张依赖图。上游在 `forEach` 里改节点字段的写法
（`label.show = symbolSize > 30`、`symbolSize = 5`、`id = idx`）改成数据项构造时的参数；
`graph-npm` 与 `graph-webkit-dep` 的 `thumbnail` 没有移植（站点没有该组件），`roamTrigger` 用
`RoamTriggers.global` 照抄。

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

`matrix-periodic-table` 在 `examples/MatrixLongTail.kt`：`custom` 系列的 `renderItem` 用 `api.layout(...)`
把 19×10 的矩阵格子铺成矩形，`api.value(2)` 对占位行返回 NaN，正好用来区分「真有元素」与 `La~Yb`
这样的整段占位格；块说明文字是 `graphic` 元素，位置要靠 `convertToPixel` 在第一帧之后才量得出来，
所以页面走 `Category.interactive`，用 `holder.setTimeout(..., 0)` 补第二张 option。上游把矩阵固定成
900px 居中，demo 容器更窄，宽度改成 `chart.getWidth() - 160`（左右各留 80px 给标注）；窄屏下左侧
`s-block` 一类的标注会略微互相压住，官方缩略图同样存在这个裁剪，属可接受偏差。上游 `label.textStyle`
在 ECharts 6 里不被读取（label 只回退到全局 `textStyle`），没有移植。

`bar-brush` 追加在 `examples/BarExtra.kt`：`brushSelected` 的 payload 上游声明成 `any`，示例侧用本地
`external interface`（`batch[].selected[].dataIndex`）强转后再拼标题。`bar-breaks-brush` 要 zr 级的
`mousedown` / `mousemove` 加 document 级 `mouseup` 才能改变断轴区间，属真实鼠标序列，按排除策略不做。

`matrix-mbti` 追加在 `examples/MatrixLongTail.kt`（数据表与配色在 `examples/MbtiData.kt`）：16×16 矩阵
每格承载一对类型，`itemStyle.decal` 用两种符号交替、两色分左右，靠 `aria.decal.show = true` 让 decal
跟着无障碍开关走；表头是两层 `levels`（`levelSize` 25 / 30）的组头，标签走 `rich` 拆上下两行。页面
`interactive` 里监听 `click`，把 `series` 换成四组的中位数汇总后再 `setOption(option, true)` 整体替换。
上游读 `window.innerWidth` / `innerHeight` 决定字号与矩阵边长，这里改成按 `chart.getWidth()` /
`getHeight()` 推，容器尺寸变化不会让页面跟着视口走。

`matrix-stock` 在 `examples/MatrixStock.kt`：一个交易日的看板铺在 5×6 矩阵上——价格占 4 行，MACD 与
成交量各占一行，右列是买卖十档与深度图。5 个 grid、4 个标题都用 `coordinateSystem: 'matrix'` + `coord`
钉进格子，跨格用 `matrix.body.data[].mergeCells` 合并；午休时段由时间轴的 `breaks` 折掉。逐分钟的价格
与成交量上游用 `Math.random()` 造，这里换成 `Lcg(20251016)`，深度图另用 `Lcg(20251017)`；
`Date.UTC` 生成的时间戳配 `useUTC = true`。价格格左右两组价格 / 涨跌幅标签是 `markPoint`，**必须写
`relativeTo: 'coordinate'`**：缺了它百分比按整个容器算，六个标签会散到整张图的高度上（这个坑一开始就
踩到了）。格线是 6 个 `graphic` line（水平 3 + 垂直 3），位置取 `chart.getWidth()`，所以页面走
`interactive`；成交量与 MACD 的时间轴柱走 `BarDataItemOption.typedValue` 传 `[time, value]`。

`data-transform-filter` 追加在 `examples/DatasetExtra.kt`：三份 dataset，`dataset_raw` 是
`life-expectancy-table.json`，另两份用内置 `filter` transform 从它派生（`and` 条件：`Year >= 1950`
且 `Country` 等于某个国家）。`config` 里的字段名 `=` 在 Kotlin 侧要写成 `@JsName("=")`。

`line-tooltip-touch` 追加在 `examples/LineExtra.kt`：移动端触摸提示，`tooltip.triggerOn = 'none'`、
`position` 回调把提示框钉在 `y = 130`，`axisPointer.label.formatter` 用 `formatTime` 取日期，
`dataZoom.inside.throttle = 50` 让拖动时提示框跟手。两天的序列由 `Lcg` 生成，先随机取起始值再随机游走，
落到非正数时重抽。

`custom-calendar-icon` 与 `flame-graph` 在 `examples/CustomMore.kt`：前者把四段内联 SVG path 按随机
icon 数摆进日历格子，`renderItem` 用 `api.font()` 取字号、`linearMap` 把 ±0.25 的偏移换算成像素；
后者读 `resources/data/stack-trace.json`（`SOURCE.md` 里有登记），把堆栈按深度铺成矩形火焰图，
点击某帧时深拷贝后只保留该帧以下的子树再 `setOption`。两者的 `textContent.style` 用到 zrender 的文本
偏移字段 `x` / `y`，这次一并补进 `ZRStyleProps`。

`doc-example/` 里还没有移植的（上游 80 个文件 = 79 个 `.js` + 1 个 `.ts`、已按 `doc-example/` id 落地
58 个、`bar-large` / `pie-highlight` 按家族 id 落地，剩 20 条）。按原因分类：

- 图标是官方站的 PNG / base64 素材，按排除策略不做：`pictorialBar-graphicType`、
  `pictorialBar-patternSize`、`pictorialBar-repeatDirection`、`pictorialBar-repeatLayout`、
  `axisPointer-handle-image`；
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
`sankey-nodeAlign-left` / `-right` 用 `energy.json` 与 `product.json`，`confidence-band` 用
`confidence-band.json`，`scatter-painter-choice` 用 `masterPainterColorChoice.json`。后者有两个坑：`x` 列
是 plotly 导出的**字符串**（照上游的一元 `+` 用 `Number(...)` 转换），`itemStyle.color` 是按 `dataIndex`
取色的回调（wrapper 的 `ZRColor(callback)` 就是为它加的）。`sankey-itemstyle` 的数据整份内联在上游
源码里（99 个节点各带一种颜色 + 109 条边），已按 `arrayOf("Werne" to "#f18bbf", …)` /
`Triple(source, target, value)` 两张表写进 `examples/Sankey.kt`。

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
- `roamTrigger`：上游 `RoamOptionMixin` 的 `'global' | 'selfRect'` 之前没有声明，补 `RoamTrigger` /
  `RoamTriggers` 与 geo、tree、treemap、map、graph、chord、sankey 上的 `roamTrigger`。
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
- `TitleOption.coordinateSystem` / `coord`：上游 `.d.ts` 只通过 `ComponentOnMatrixOptionMixin` 给了
  `matrixIndex` / `matrixId`，但标题视图实际走 `createBoxLayoutReference`，运行期读的是这两个字段
  （`matrix-stock` 把标题钉在矩阵格上就这么写），已补上并由
  `MatrixAndGraphicOptionTest.titleCanBeLaidOutOnMatrixCells` 断言。
- `BarDataItemOption.typedValue`：上游 `BarDataValue` 是 `OptionDataValue | OptionDataValue[]`，时间轴上
  的柱要把 `[time, value]` 二元组写进同一个 `value` 字段；照 line / scatter 的做法用
  `@JsName("value")` 的联合类型字段，`SeriesTypedDataTest` 断言回读。
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
- treemap 的层级与节点：上游 `TreemapSeriesLevelOption` / `TreemapSeriesNodeItemOption` 混入
  `StatesOptionMixin`，wrapper 之前没有 `emphasis` / `blur` / `select`，`treemap-visual` 与
  `treemap-show-parent` 的层级样式写不出来；两者都补了 `DataItemStateOption`。
- 自定义系列的 shape / style 过渡：上游把 shape 与 style 写成 `T & TransitionOptionMixin<T>`，
  `transition` / `enterFrom` / `leaveTo` 挂在 shape、style 自身而不是元素上。新增
  `CustomElementShapeOption` / `CustomElementStyleOption`（各自带一套过渡字段）与
  `CustomShapeTransitionOption` / `CustomStyleTransitionOption`；`CustomElementProps.style` 与
  `shape` 仍是 `ZRStyleProps` / `ZRShapeProps`，需要过渡时用窄类型建值，见 KDoc。
  `CustomElementOptionsTest.shapeAndStyleCanCarryTheirOwnTransitions` 覆盖。

## 3D 示例（echarts-gl）

3D 家族在 `examples/Gl.kt`、`examples/GlScatter.kt`、`examples/GlSurfaceMaterial.kt`、
`examples/GlGeo.kt`、`examples/GlLines.kt`、`examples/GlBarGeo.kt` 与 `examples/GlImageBars.kt`，用的是
`ktjs-echarts-gl` 模块，写法与其它家族一样走 `Category.chart`；要先取数据文件的（`bar3d-dataset` 与三页
`scatter3D`）走 `Category.interactive` + `loadJson`，要等图片加载的（`surface-leather`）走
`Category.interactive` + `listen(LOAD)`：

- **安装是全局的一次性动作**：`echarts` 的入口不带 GL 图表，`ECharts.use(...)` 注册的扩展对所有实例生
  效，所以调用点在 `main()` 里的 `useEChartsGl()`，必须在第一个 `ECharts.init` 之前。新页面需要另一种
  图表或坐标系（`scatter3D`、`geo3D`、`globe`…）时，把对应 installer 加进那个数组；`ECharts.use` 自己
  会去重，重复调用无副作用。`Mapbox3DComponent` / `Maptalks3DComponent` 不注册：它们要求页面自己先加载
  Mapbox GL / maptalks。
- **option 类型用 `EChartsGlOption`**：它继承 `EChartsOption`，`tooltip` / `visualMap` / `dataset` 这些
  2D 组件照常写，`grid3D` / `xAxis3D` / `yAxis3D` / `zAxis3D` 与 `bar3DSeries { }` / `line3DSeries { }` /
  `scatter3DSeries { }` / `surfaceSeries { }` / `lines3DSeries { }` 来自 `echartsgl.options`。这些工厂都只设
  `type`，其余字段一律走 `typed*` 视图（`typedData` / `typedLabel` / `typedItemStyle` / `typedEmphasis` /
  `typedCoordinateSystem` / `typedLineStyle`）。
- **标签照 2D 的扁平写法**：echarts-gl 2.1.0 用 ECharts 6 的 `createTextStyle` 排版，读的是
  `label.fontSize` / `label.color` 这些扁平属性（`GlLabelOption` 就是 `LabelOption` 的别名）。上游
  `backwardCompat` 只把 `axisLabel.textStyle` 展平，系列 label 不展平，所以不要再加一层 `textStyle`。
- **没作用的空组件不要写**：`bar3d-punch-card` / `simple-surface` / `surface-wave` / `sphere-parametric-surface`
  的 `viewControl` 上游就是空对象（只有一行注释掉的 `projection`），写成空 `unsafeJso` 只会让人以为投影
  生效了，直接省略；`line3d-orthographic` 才真的设了 `projection = orthographic`。
- **曲面用函数而不是数组**：`simple-surface` / `surface-wave` / `sphere-parametric-surface` 只给
  `equation` / `parametricEquation`，由 `SurfaceFunction { x, y -> ... }` 采样。原例在原点附近返回 `'-'`
  挖洞，Kotlin 侧用 `Double.NaN`：`SurfaceView` 只要有一个顶点不是数就整片四边形不画。
- **数据能复用就复用**：`bar3d-punch-card` 与 `transparent-bar3d` 的打卡表就是 `examples/CustomExtra.kt`
  里的 `punchCardValues` / `punchCardHours` / `punchCardDays`，只是把前两个坐标换成 `[hour, day, value]`；
  `bar3d-dataset` 直接用 `dataset` 的列名当三个坐标轴（`encode.z` 走 `Life Expectancy`）。
  `resources/data/life-expectancy-table.json` 同时喂给 `bar3d-dataset`、`scatter3d-scatter` 和
  `scatter3D-dataset`。
- **`scatter3D` 三页在 `GlScatter.kt`**：`scatter3d` 读 `nutrients.json`，六列数据项的前三个是坐标，后两个
  分别由 dimension 3 / 4 的两个 `visualMap` 映射成颜色和点大小；`scatter3d-scatter` 是左边 3D、右边 2×2
  二维面板（`grid3D.width = '50%'` + 四个百分比定位的 `grid` 和 `scatter` 系列）；`scatter3D-dataset` 用
  分类 x 轴 + `encode` 把三列钉到坐标轴上。上游 `scatter3d` 声明 `theme: dark` 并把坐标轴、`axisPointer`
  与 visual map 文字刷白，本站主题由自己的开关决定，这些颜色不搬（浅色下白字不可见）。
- **现实材质七页在 `GlSurfaceMaterial.kt`**：`metal-surface` / `surface-breather` /
  `surface-mollusc-shell` / `surface-golden-rose` / `surface-theme-roses` / `parametric-surface-rose` /
  `surface-leather` 都是 `shading: "realistic"` 的参数曲面，靠 `realisticMaterial` + 环境立方体贴图
  （`data/img/gl/canyon.hdr` / `pisa.hdr`）与 `postEffect` 出效果。`SSAO` / `DOF` / `SSR` 这些旧键名在
  wrapper 里是 `ssao` / `dof` / `ssr`（`@JsName` 映射回原键），官方文档名
  `screenSpaceAmbientOcclusion` 等也照样能写，两个都设时上游读旧键。
  `textureTiling` / `textureOffset` 可以写单个数字或 `[u, v]` 一对（`DoubleOrDoubleArray`），玫瑰页的
  `[200, 20]` 必须写一对。
- **纹理与素材**：HDR 环境图和皮革贴图放在 `resources/data/img/gl/`，来源与用处记在 `data/SOURCE.md`。
  `.hdr` 走 `textureUtil.loadTexture(url, {fileType:'hdr'})`（XHR + arraybuffer），静态服务器给的 MIME
  不影响加载。`surface-leather` 先把 `leather_height.jpg` 画进 2D canvas 读红通道，再把顶点按像素值膨
  胀，所以这一页必须等图片 `load` 之后才 `setOption`。
- **地理与地球九页在 `GlGeo.kt`**：`geo3d` / `geo3d-with-different-height` 用 `geo3D` 组件，
  `globe-echarts-gl-hello-world` / `globe-atmosphere` / `globe-displacement` / `globe-moon` /
  `globe-layers` / `globe-with-echarts-surface` 用 `globe` 组件，`scatter3d-globe-population` 是
  `coordinateSystem: "globe"` 的 `scatter3D`，两个 installer 已加进 `useEChartsGl()`。`geo3D` 必须先有
  地图：都在 `Category.interactive` 里 `loadJson("data/world.json")` 后
  `ECharts.registerMap("world", ...)`，`geo3d-with-different-height` 再取 `world-population.json`，把人
  口的对数线性映射到 `0..3` 当成 `regions[].height`（文档写的 `regionHeight` 是整幅地图的基准高度，两者
  不同）。`geo3d` 原例的 `lambertMaterial.baseTexture`（`woods.jpg`）在 2.1.0 里没有任何材质读
  （`setMaterialFromModel` 只看 `detailTexture`），照官方缩略图那样留白。globe 页的 `series: []` 是空操
  作，省略；`globe-displacement` 的 `debug.wireframe` 上游没写进文档但会被读取，wrapper 照样建模。球面
  纹理可以是图片 URL，也可以是一个 ECharts 实例（必须建在 canvas 上，否则控制台警告并取不到内容）。
- **两张页面靠离屏图表与人口数据取值**：`scatter3d-globe-population` 读 `population.json`
  （`[lng, lat, 人口]`，三万多行全为正，上游那条 `> 0` 过滤是空操作），把 `sqrt(人口)` 当第三列，交给
  `visualMap.inRange.symbolSize = [1, 10]` 与 `blendMode: "lighter"`；`globe-with-echarts-surface` 先用
  `ECharts.init(canvas, null, { width: 4096, height: 2048 })` 画一张 `map` 系列，再把**这个实例**作为
  `globe.baseTexture`。上游的 177 个国家值内联在示例里，与 `world-population.json` 相同，所以直接读文件。
- **`bar3D` 立在地理坐标系上（`GlBarGeo.kt`）**：`bar3d-global-population` 与
  `global-population-bar3d-on-globe` 复用同一份 `population.json`，把每格人口取平方根当柱高，
  柱宽用 `barSize`（上游只从 `bar3DLayout` 读、文档没写，`geo3D` / `globe` 下还会再除以坐标系的
  `getScale() / 16`，所以不是数据单位）。两页的系列都写了橙色 `itemStyle.color`，但只有地球页真的
  是橙的：那里 `visualMap` 只映射 `colorLightness`；`geo3D` 页的 `visualMap.inRange.color` 会被写进
  `itemStyle.color`（`visualStyleAccessPath`），橙色被 11 色带顶掉——与官方页一致，别把它「修」回来。
  `geo3D` 页的取景（`distance: 50`，默认 `alpha` / `beta`）本身就很低、地图上缘被裁掉，上游渲染同样
  如此，不是移植错误。
- **四条航线线在 `GlLines.kt`**：`lines3d-airline-on-globe` 与 `lines3d-flights-on-geo3d` 是
  `data-gl` 那份 1.7 万条航线画在地球/geo3D 上的版本（2D 的 `lines-airline` 用 6.5 万条的
  `data/flights.json`，两份表结构相同、内容不同），把每个航班的两个机场坐标拼成
  `Lines3DDataItem([[...], [...]])`；`lines3d-flights` 把同一份表**按航空公司拆成系列**，每个系列一层
  拖尾色，再补一个 `scatter3D` 把每条航线的两个端点钉在地球上；`lines3d-flights-gl` 用近乎水平视角
  （`viewControl.alpha: 89`）看 `geo3D`，拖尾设 `constantSpeed: 5`。地球页只有 `lineStyle` 一层淡色；
  geo3D 两页给 `effect` 开拖尾，并在 `window` 上挂一个 `keydown` 监听，用
  `dispatchAction(lines3DToggleEffect)` 暂停/继续——监听器交给 `ChartHolder.onUnmount` 释放，换页后不会
  再触到已销毁的实例。上游的 `geo3D.silent` 不在文档里，但 `echarts-gl` 核心会读它来关掉拾取，wrapper
  已建模。
- **`lines3d-flights` 的图例顺序不是系列顺序**：上游按航司分组后拿分组对象的 key 当 `legend.data`，顺序
  是航司在航线表里**首次出现**的顺序，而系列本身按 `airlines` 表的顺序建；`selectedMode: "single"` 下
  首个图例项就是初始选中的那一项，顺序照搬系列会让页面默认停在另一家航司上。`GlLines.kt` 因此单独记
  分组顺序，别把它「理顺」。
- **`world.json` 的地名是缩写**：本站注册的 `geo/world.json` 写 `Czech Rep.` / `Korea` / `Dem. Rep. Congo`
  等，而 `world-population.json`（以及官方站点注册的 ECharts 4.9 世界地图）写全称，`GlGeo.kt` 的
  `worldRegionNames` 表把数据名翻回地图名，`geo3D` 的区域高度与地图系列都走它。`Kosovo` /
  `Somaliland` / `West Bank` / `French Guiana` / `French Southern and Antarctic Lands` 在这份 GeoJSON 里
  没有对应区域，只影响这几块不上色。
- **不做的 GL 页**：`animating-contour-on-globe` 依赖 `d3-array` / `d3-contour` / `d3-geo` / `d3-timer`
  在页面里生成等高线，等价实现要先把 d3 的结果算出来；`*-simplex-noise`、`*-mapbox*`、`*-maptalks*`、
  `iron-globe` 分别需要外部库、地图 JS 或官方站点 `/asset/get/s/` 的纹理。`bar3d-music-visualization`
  要先播音频再取 `AnalyserNode` 的频谱，浏览器禁止无手势的自动播放；`globe-country-carousel` 靠
  `getModel().getComponent('geo').coordinateSystem.regions` 取各国中心点，而本站的 `world.json` 里
  只有 6 个国家带 `cp`，实例上的 `getModel` 也没有建模。
- **上游的死字段不要搬**：`surface-breather` 的「Export OBJ」按钮调 `echarts.exportGL2PLY`，该模块在
  `echarts-gl` 2.1.0 里已经删掉；`parametric-surface-rose` 的 `realisticMaterial.baseTexture` 没有任何
  材质读它（只有 `globe.baseTexture` 有用）；玫瑰三页的 `toolbox.iconStyle.normal.borderColor` 是 ECharts
  已废弃的状态写法。这些都略去，理由写在各文件的头注释里。
- **图片当数据用的曲面**：`image-surface-sushuang`（在 `Gl.kt`）把照片的每个像素变成顶点
  `[列, 高 - 行, 亮度]`，颜色用 `itemStyle.color` 的回调按 `params.dataIndex` 取像素 RGB
  （`GlColor(ZRColor { params -> ... })`）。原例把照片以 base64 内联在源码里，这里解码成
  `data/img/gl/sushuang.jpg` 走静态资源。
- **`bar3d-myth` 的颜色回调返回 HDR 数组**：每个像素一根 `bar3D`，颜色回调返回
  `[r, g, b, 1]`（各通道再乘 `亮度 * 2`，会超过 1，所以是 HDR）。`ZRColor` 的回调只能返回字符串，
  wrapper 因此给 `GlColor` 补了返回 `Array<Double>` 的回调重载。四万多根柱子配上 `enable: true` +
  `depthOfField`，单页要几秒才稳定，截图与 e2e 都要留足时间。
- **`GlImageBars.kt` 的两页同样是「一个像素一根柱」**：`image-to-bar3d` 把肖像的亮度取反当柱高、按像素
  RGB 上色（一万多根柱子），`bar3d-voxelize-image` 把硬币压到 80 行、柱高按 `(亮度 - 125) * 0.3 + 50`
  取、颜色返回 HDR 数组（亮度乘 1.2 会溢出 1）。两页都先把图片画进离屏 canvas 再读
  `getImageData`，像素数据在回调里通过闭包复用。
- **`bar3d-voxelize-image` 关掉了 `screenSpaceReflection`**：上游开了它，但在 Chrome 下 `echarts-gl`
  2.1.0 的 SSR pass 会画出整幅黑帧（bloom / SSAO / DOF 单独开都正常），所以这页保留其余特效、去掉反射。
  同一页上游的 `wireframe: { show: false }`（在 `image-to-bar3d` 里）是死字段：`bar3D` 根本不读
  `wireframe`，只有 surface / line3D / geo3D / globe 有。
- **WebGL 层是另一张画布**：GL 系列只画在 `echarts-gl` 插进来的 canvas 上，zrender 自己的画布层不会创
  建；没有 WebGL 时 echarts-gl 用 `.ecgl-nowebgl` 占位，页面上连 canvas 都没有。e2e 因此对 GL 页只做
  `assertChartRendered`（画布有尺寸）+ `the 3d pages paint through the webgl layer`（这张画布能取到
  WebGL 上下文、且没有占位元素）。`paintedFrame` / `paintedPixels` 走 `getContext('2d')`，对 GL 画布取
  到的是 `null`，不要把 GL 路由加进那些用例。

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

逐页遍历的用例（`every example of the catalog renders without errors` 与
`the 3d pages paint through the webgl layer`）各自用 `@Timeout(300)` 放宽到 5 分钟：目录每加一页，这两个
用例就多一次「切路由 + 等画布」，WebGL 页还要先编译着色器；上限写死在单个用例上，别把整个套件的默认预算
一起抬上去。

`page(meta)` 会给页面根节点写上 `data-route`，`assertChartRendered` / `waitForPainted` 都按
`.page[data-route='<id>'] .chart canvas` 找画布。原因是 `location.hash` 改完，`hashchange` 处理器还要
晚一个任务才跑，shell 在此之前仍挂着上一页：不限定路由的话，等待会在上一页的画布上通过，随后节点被
替换，`boundingBox()` 返回 `null`（这就是那版「chart canvas box of null」偶发失败的根因）。断言因此还
带了重试：读不到尺寸就重新等一次，最多三次，只会让真正有问题的那页慢一点，不会放过它。

浏览器上下文固定 `locale = zh-CN`（`DemoOptions`）。原因是无头容器里常常没有装 CJK 字体，界面切到中文
时字会变成豆腐块，语言切换、主题高亮、对比度三个断言就会随机失败——那不是站点的问题，是环境的字体
问题，所以把语言钉死在中文，让断言与字体无关。
