# ktjs-echarts

Kotlin/JS wrapper for [`echarts`](https://www.npmjs.com/package/echarts) 6.1.0.

The wrapper exposes the full public surface of the npm package: the module-level API (`init`,
`use`, `registerTheme`, `registerMap`, the `format` / `time` / `graphic` namespaces, ...), the
modular entry point `echarts/core` with every installer from `echarts/charts`,
`echarts/components`, `echarts/renderers` and `echarts/features`, typed option objects for the
charts and components, and the classes used by extension authors.

## Installation

Replace the version with the one you need (`{upstream version}-kt.{revision}`):

```kotlin
dependencies {
    implementation("io.github.shijianjs:ktjs-echarts:6.1.0-kt.1")
}
```

## Quick start

The full bundle registers every chart and component, so it needs no installers:

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

The chart does not observe its container, so forward resize events yourself:

```kotlin
import web.events.EventHandler
import web.window.window

window.onresize = EventHandler { chart.resize() }
```

## Options

Options are plain JavaScript objects, so they are modeled as `external interface` types with
nullable `var` properties. Omitted properties keep the ECharts default. Build them with
`js.objects.unsafeJso<T> { ... }` from kotlin-js-plain-objects, which creates a real object literal
and applies the receiver block:

```kotlin
val title = unsafeJso<TitleOption> {
    text = "Monthly revenue"
    subtext = "In thousands"
}
```

Named values are typed instead of being plain strings: `AxisTypes.category`, `SeriesTypes.line`,
`TooltipTriggers.axis`, `LegendTypes.scroll`, `AxisPointerTypes.cross`, `ImageTypes.png`,
`AnimationEasings.cubicOut` and `zrender.RendererTypes.svg` are constants of their own sealed
interfaces, so a typo is a compile error rather than a silently ignored option. Fields that take a
CSS value use the kotlin-wrappers types (`web.cssom.FontStyle`, `FontWeight`, `TextAlign`,
`VerticalAlign`) and the canvas line cap / join types.

Helpers in `echarts.options`:

- `one(value)` wraps a single component in an array. Fields that accept both one component and a
  list are typed as arrays so that multiple components stay possible.
- `numbers(...)`, `points(...)` and `items(...)` build series data as real JavaScript arrays.
  A Kotlin `DoubleArray` is a typed array, which ECharts rejects as series data.
- `lineSeries`, `barSeries`, `pieSeries`, `scatterSeries`, `effectScatterSeries`, `radarSeries`,
  `mapSeries`, `treeSeries`, `treemapSeries`, `sunburstSeries`, `graphSeries`, `chordSeries`,
  `funnelSeries`, `gaugeSeries`, `pictorialBarSeries`, `themeRiverSeries`, `customSeries`,
  `boxplotSeries`, `candlestickSeries`, `heatmapSeries`, `parallelSeries`, `linesSeries` and
  `sankeySeries` create a series object with the matching `type` already set.
- Union-typed upstream options use an opaque interface plus same-name factories, for example
  `StringOrDouble("40%")`, `StringOrDouble(24.0)`, `BooleanOrDouble(true)` and
  `DoubleOrDoubleArray(arrayOf(4.0, 8.0))`. The explicit wrapper is what keeps the union typed
  instead of falling back to `dynamic`.
- `linearGradient(...)` and `radialGradient(...)` build color gradients from
  `gradientColorStop(offset, color)` entries.

## Events

Event names are constants in `EChartsEvents`; handlers receive typed payloads:

```kotlin
import echarts.EChartsEvents
import echarts.ECElementEvent

chart.on(EChartsEvents.Click) { event: ECElementEvent ->
    println("${event.seriesName}: ${event.name} = ${event.value}")
}
```

Dedicated helpers exist for the events whose payload differs from the mouse events:
`onSelectChanged`, `onAxisBreakChanged`, `onFinished` and `onRendered`.

## Modular builds

`EChartsCore` is the `echarts/core` entry point. Register only the charts, components, renderers
and features the page uses, then initialize through `EChartsCore`:

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

Installers are exposed as properties on `EChartsCharts`, `EChartsComponents`, `EChartsRenderers`
and `EChartsFeatures`. Select the renderer with `EChartsInitOpts.renderer`:

```kotlin
val chart = EChartsCore.init(
    element,
    null,
    unsafeJso<EChartsInitOpts> { renderer = RendererTypes.svg },
)
```

Both objects come from the same package, so an instance created through `EChartsCore` can be passed
to helpers that expect the shared `EChartsInstance` type.

Whether the bundle stays modular depends only on what you reference: `EChartsCore` and the full
`ECharts` entry are separate `@JsModule` declarations, so using only the former does not pull in the
full build. A single reference to `ECharts` anywhere in the bundle does, so on the modular path use
`EChartsCore` for registration and for the helper namespaces as well. `ktjs-echarts-gl` depends on
the full `echarts` build internally and cannot be combined with modular imports.

## Extension authoring

The classes that custom series, components and views extend are available from `echarts/core`:
`Model`, `ComponentModel`, `SeriesModel`, `ChartView`, `ComponentView`, `SeriesData`, `Axis`,
`GlobalModel` and `ExtensionAPI`. The registration helpers `extendComponentModel`,
`extendComponentView`, `extendSeriesModel` and `extendChartView` mirror the upstream functions:

```kotlin
import echarts.extendSeriesModel

val seriesModel = extendSeriesModel(js("({ type: 'mySeries' })"))
ECharts.use(seriesModel)
```

`SeriesData` is the Kotlin name of the upstream `List` class; it is mapped back to `List` at the
JavaScript boundary so it never collides with `kotlin.collections.List`.

## Namespaces and utilities

`ECharts` (and `EChartsCore`) expose the upstream helper namespaces with typed members where the
upstream declarations provide enough information:

- `format` — `formatTpl`, `addCommas`, `encodeHTML`, `truncateText`, ...
- `time` — `parse`, `format`, `roundTime`, `getTimezoneOffset`, ...
- `number` — `parsePercent`, `nice`, `linearMap`, `getPrecision`, ...
- `helper` — model and data helpers for extension authors.
- `graphic`, `color`, `matrix`, `vector`, `util`, `zrUtil`, `zrender` and `dataTool`.

`registerTheme`, `registerMap`, `registerLocale`, `registerAction`, `registerCoordinateSystem`,
`registerCustomSeries`, `registerLayout`, `registerVisual` and the other registration functions
have the same overloads as upstream.

## Build and test

```bash
./gradlew :ktjs-echarts:jsBrowserTest
```

After adding or upgrading an npm dependency, refresh the shared lock file:

```bash
./gradlew kotlinUpgradePackageLock
```
