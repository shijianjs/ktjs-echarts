# ktjs-echarts

[`echarts`](https://www.npmjs.com/package/echarts) 6.1.0 的 Kotlin/JS wrapper。

wrapper 覆盖 npm 包的完整公开接口：模块级 API（`init`、`use`、`registerTheme`、`registerMap`，
以及 `format` / `time` / `graphic` 等命名空间），模块化入口 `echarts/core` 与
`echarts/charts`、`echarts/components`、`echarts/renderers`、`echarts/features` 的全部安装器，
图表与组件的类型化 option，以及扩展开发所需的类。

## 安装

把版本换成需要的那一个（`{上游版本}-kt.{修订号}`）：

```kotlin
dependencies {
    implementation("io.github.shijianjs:ktjs-echarts:6.1.0-kt.1")
}
```

## 快速开始

完整包自带所有图表和组件，不需要注册安装器：

```kotlin
import echarts.AxisTypes
import echarts.ECharts
import echarts.options.EChartsOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.lineSeries
import echarts.options.numbers
import echarts.options.one
import js.objects.unsafeJso
import web.dom.document
import web.html.HtmlTagName

val element = document.createElement(HtmlTagName.div)
document.body.append(element)

val chart = ECharts.init(element)
chart.setOption(
    unsafeJso<EChartsOption> {
        xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.category })
        yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
        series = arrayOf(lineSeries { data = numbers(120.0, 200.0, 150.0) })
    },
)
```

图表不会自己观察容器尺寸，需要把 resize 事件转发给它：

```kotlin
import web.events.EventHandler
import web.window.window

window.onresize = EventHandler { chart.resize() }
```

## Option

ECharts 的 option 是普通 JavaScript 对象，wrapper 用带可空 `var` 属性的
`external interface` 建模；没设置的属性保持 ECharts 默认值。用 kotlin-js-plain-objects 的
`js.objects.unsafeJso<T> { ... }` 创建对象字面量并应用接收者代码块：

```kotlin
val title = unsafeJso<TitleOption> {
    text = "Monthly revenue"
    subtext = "In thousands"
}
```

带取值范围的字段用类型而不是裸字符串：`AxisTypes.category`、`SeriesTypes.line`、
`TooltipTriggers.axis`、`LegendTypes.scroll`、`AxisPointerTypes.cross`、`ImageTypes.png`、
`AnimationEasings.cubicOut`、`zrender.RendererTypes.svg` 都是各自 sealed 接口的常量，拼错会在编译期
报错，而不是被 ECharts 静默忽略。CSS 语义的字段直接用 kotlin-wrappers 的类型（`web.cssom.FontStyle`、
`FontWeight`、`TextAlign`、`VerticalAlign`）以及 canvas 的线端 / 拐角类型。

`echarts.options` 中的辅助函数：

- `one(value)` 把单个组件包成数组。既接受单个组件又接受列表的字段统一声明为数组，保留传入多个
  组件的可能。
- `numbers(...)`、`points(...)` 和 `items(...)` 把序列数据构造成真正的 JavaScript 数组。Kotlin 的
  `DoubleArray` 是类型化数组，ECharts 不接受它作为序列数据。
- `lineSeries`、`barSeries`、`pieSeries`、`scatterSeries`、`effectScatterSeries`、`radarSeries`、
  `mapSeries`、`treeSeries`、`treemapSeries`、`sunburstSeries`、`graphSeries`、`chordSeries`、
  `funnelSeries`、`gaugeSeries`、`pictorialBarSeries`、`themeRiverSeries`、`customSeries`、
  `boxplotSeries`、`candlestickSeries`、`heatmapSeries`、`parallelSeries`、`linesSeries` 和
  `sankeySeries` 创建已设置好 `type` 的序列对象。
- 上游的联合类型用不透明接口加同名工厂函数表示，例如 `StringOrDouble("40%")`、
  `StringOrDouble(24.0)`、`BooleanOrDouble(true)`、`DoubleOrDoubleArray(arrayOf(4.0, 8.0))`。
  显式包装让联合类型保持可检查，而不是退化成 `dynamic`。
- `linearGradient(...)` 和 `radialGradient(...)` 用 `gradientColorStop(offset, color)` 构造渐变色。

## 事件

事件名是 `EChartsEvents` 中的常量，回调参数有对应类型：

```kotlin
import echarts.EChartsEvents
import echarts.ECElementEvent

chart.on(EChartsEvents.Click) { event: ECElementEvent ->
    println("${event.seriesName}: ${event.name} = ${event.value}")
}
```

回调参数与鼠标事件不同的几个事件有专门入口：`onSelectChanged`、`onAxisBreakChanged`、
`onFinished` 和 `onRendered`。

## 模块化构建

`EChartsCore` 对应 `echarts/core` 入口。只注册页面用到的图表、组件、渲染器和特性，再通过
`EChartsCore` 初始化：

```kotlin
import echarts.EChartsCharts
import echarts.EChartsComponents
import echarts.EChartsCore
import echarts.EChartsRenderers
import echarts.EChartsInitOpts
import js.objects.unsafeJso
import zrender.RendererTypes

EChartsCore.use(
    arrayOf(
        EChartsCharts.LineChart,
        EChartsComponents.GridComponent,
        EChartsComponents.TooltipComponent,
        EChartsRenderers.SVGRenderer,
    ),
)

val chart = EChartsCore.init(element)
```

安装器分别挂在 `EChartsCharts`、`EChartsComponents`、`EChartsRenderers` 和 `EChartsFeatures`
对象上。用 `EChartsInitOpts.renderer` 选择渲染器：

```kotlin
val chart = EChartsCore.init(
    element,
    null,
    unsafeJso<EChartsInitOpts> { renderer = RendererTypes.svg },
)
```

两个对象来自同一个 npm 包，通过 `EChartsCore` 创建的实例可以传给需要 `EChartsInstance` 的辅助
函数。

按需是否生效只取决于实际引用：`EChartsCore` 与全量入口 `ECharts` 是两个独立的 `@JsModule` 声明，
只用前者不会把全量包打进 bundle；但同一 bundle 里只要有一处用到 `ECharts`，全量 `echarts` 就会被
引入，所以走按需路径时注册和工具命名空间统一用 `EChartsCore`。`ktjs-echarts-gl` 内部依赖全量
`echarts`，与按需引入不能共存。

## 扩展开发

自定义序列、组件和视图使用的类都从 `echarts/core` 导出：`Model`、`ComponentModel`、
`SeriesModel`、`ChartView`、`ComponentView`、`SeriesData`、`Axis`、`GlobalModel` 和
`ExtensionAPI`。注册函数 `extendComponentModel`、`extendComponentView`、`extendSeriesModel` 和
`extendChartView` 与上游一致：

```kotlin
import echarts.extendSeriesModel

val seriesModel = extendSeriesModel(js("({ type: 'mySeries' })"))
ECharts.use(seriesModel)
```

`SeriesData` 是上游 `List` 类的 Kotlin 名称；在 JavaScript 边界上会映射回 `List`，因此不会与
`kotlin.collections.List` 冲突。

## 命名空间与工具函数

`ECharts`（以及 `EChartsCore`）按上游声明提供类型化的辅助命名空间：

- `format` — `formatTpl`、`addCommas`、`encodeHTML`、`truncateText` 等。
- `time` — `parse`、`format`、`roundTime`、`getTimezoneOffset` 等。
- `number` — `parsePercent`、`nice`、`linearMap`、`getPrecision` 等。
- `helper` — 扩展开发使用的 model 与 data 辅助函数。
- `graphic`、`color`、`matrix`、`vector`、`util`、`zrUtil`、`zrender` 和 `dataTool`。

`registerTheme`、`registerMap`、`registerLocale`、`registerAction`、`registerCoordinateSystem`、
`registerCustomSeries`、`registerLayout`、`registerVisual` 等注册函数的重载与上游一致。

## 构建与测试

```bash
./gradlew :ktjs-echarts:jsBrowserTest
```

新增或升级 npm 依赖后，刷新共享锁文件：

```bash
./gradlew kotlinUpgradePackageLock
```
