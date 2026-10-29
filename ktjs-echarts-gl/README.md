# ktjs-echarts-gl

Kotlin/JS wrapper for [`echarts-gl`](https://www.npmjs.com/package/echarts-gl) 2.1.0, the extension
pack that adds 3D and WebGL series to [`echarts`](https://www.npmjs.com/package/echarts) 6.

`echarts-gl` ships plain JavaScript without TypeScript declarations, so the declarations here are
derived from the upstream source and from the `option-gl` documentation rather than generated.

## Installation

Replace the version with the one you need (`{upstream version}-kt.{revision}`):

```kotlin
dependencies {
    implementation("io.github.shijianjs:ktjs-echarts-gl:2.1.0-kt.1")
}
```

The module depends on `ktjs-echarts` with `api` scope, so the core wrapper comes along.

## Quick start: 3D bars

Register the installers on the core `ECharts` object before the first `init`, then set a `grid3D`
option and a `bar3D` series:

```kotlin
import echarts.AxisTypes
import echarts.ECharts
import echarts.options.OptionDataValue
import echarts.options.one
import echartsgl.Bar3DChart
import echartsgl.Grid3DComponent
import echartsgl.options.Axis3DOption
import echartsgl.options.Bar3DDataItem
import echartsgl.options.EChartsGlOption
import echartsgl.options.Grid3DOption
import echartsgl.options.bar3DSeries
import js.objects.unsafeJso
import web.dom.document
import web.html.HtmlTagName

ECharts.use(arrayOf(Bar3DChart, Grid3DComponent))

val element = document.createElement(HtmlTagName.div).apply {
    style.width = "600px"
    style.height = "400px"
}
document.body.append(element)

val chart = ECharts.init(element)
chart.setOption(
    unsafeJso<EChartsGlOption> {
        grid3D = one(
            unsafeJso<Grid3DOption> {
                boxWidth = 100.0
                boxHeight = 100.0
                boxDepth = 100.0
                viewControl = unsafeJso {
                    alpha = 30.0
                    beta = 40.0
                    distance = 250.0
                }
            },
        )
        xAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
        yAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
        zAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
        series = one(
            bar3DSeries {
                typedData = arrayOf(
                    Bar3DDataItem(
                        arrayOf(
                            OptionDataValue(1.0),
                            OptionDataValue(2.0),
                            OptionDataValue(3.0),
                        ),
                    ),
                )
            },
        )
    },
)
```

The series is drawn into a WebGL layer on top of the canvas, so the container needs a size and the
browser needs WebGL; without it `echarts-gl` renders its "Sorry, your browser does not support
WebGL" placeholder instead.

## Installers

Chart installers (`echarts-gl/charts`):

| Value | `series.type` | Notes |
| --- | --- | --- |
| `Bar3DChart` | `bar3D` | Bars in `grid3D`, `geo3D` or `globe` |
| `Line3DChart` | `line3D` | Polylines in the same coordinate systems |
| `Scatter3DChart` | `scatter3D` | Points with symbols |
| `Lines3DChart` | `lines3D` | Lines between coordinates, with an optional trail effect |
| `Polygons3DChart` | `polygons3D` | Extruded polygons on `geo3D` |
| `SurfaceChart` | `surface` | Surface from an `[x, y, z]` grid |
| `Map3DChart` | `map3D` | Regions of a registered map on `geo3D` / `mapbox3D` / `maptalks3D` |
| `ScatterGLChart` | `scatterGL` | WebGL 2D scatter for very large point sets |
| `GraphGLChart` | `graphGL` | Force-directed graph laid out on the GPU |
| `FlowGLChart` | `flowGL` | Particles along a vector field |
| `LinesGLChart` | `linesGL` | WebGL 2D lines for large numbers of segments |

Component installers (`echarts-gl/components`): `Grid3DComponent` (`grid3D` with `xAxis3D` /
`yAxis3D` / `zAxis3D`), `Geo3DComponent` (`geo3D`), `GlobeComponent` (`globe`), `Mapbox3DComponent`
(`mapbox3D`) and `Maptalks3DComponent` (`maptalks3D`). The Mapbox and maptalks variants assume that
the page loads those libraries itself; `echarts-gl` only renders into their canvas.

## Options

`EChartsGlOption` extends the core `EChartsOption` with `grid3D`, `xAxis3D`, `yAxis3D` and
`zAxis3D`. The GL specific option types live in `echartsgl.options`:

- `Grid3DOption` — layout (`left` / `top` / `width` / `height`), `boxWidth` / `boxHeight` /
  `boxDepth`, `environment`, and the nested `viewControl`, `light`, `postEffect` and
  `temporalSuperSampling` options.
- `Axis3DOption` — one type for the three axes: type, range, `name*`, split lines and areas, axis
  line, ticks, labels and axis pointer.
- `ViewControlOption` — projection, camera angles and distance, auto rotation, damping and the
  mouse-button bindings.
- `LightOption` — main, ambient and ambient cubemap lights.
- `PostEffectOption` — bloom, depth of field, SSAO, SSR, FXAA, color correction and edges. The
  upstream aliases `DOF`, `SSAO`, `SSR` and `FXAA` are available as `dof`, `ssao`, `ssr` and
  `fxaa`.
- `ShadingOption` — `shading` plus the `realisticMaterial`, `lambertMaterial`, `colorMaterial` and
  `hatchingMaterial` options, implemented by every series and geographic component that shades a
  surface.
- `Bar3DSeriesOption`, built with the `bar3DSeries { }` factory: bar geometry (`bevelSize`,
  `bevelSmoothness`, `minHeight`, `stack`, `onGridPlane`), `grid3DIndex` / `geo3DIndex` /
  `globeIndex`, and the typed views `typedData`, `typedItemStyle`, `typedLabel`, `typedEmphasis`,
  `typedCoordinateSystem` and `typedBlendMode`.
- `Bar3DDataItem` — one data entry: a `[x, y, z]` (or `[lng, lat, value]`) row, a single value or a
  `Bar3DDataItemOption` with per-item style, label and emphasis.
- The other series follow the same shape: `scatterGLSeries { }`, `linesGLSeries { }`,
  `flowGLSeries { }`, `graphGLSeries { }`, `polygons3DSeries { }` and `map3DSeries { }` create a
  `ScatterGLSeriesOption`, `LinesGLSeriesOption`, `FlowGLSeriesOption`, `GraphGLSeriesOption`,
  `Polygons3DSeriesOption` and `Map3DSeriesOption`, each with a typed `data` view.
- `Mapbox3DOption` and `Maptalks3DOption` configure the two map-service components. They assume the
  page itself loads the map library and sets its global access token, which is not an option field.

Enumerated values are typed instead of being plain strings: `CoordinateSystems2D`,
`CoordinateSystems3D`, `ShadingTypes`, `GridPlaneTypes`, `GlBlendModes`, `ProjectionTypes`,
`QualityTypes`, `AutoRotateDirections`, `MouseButtons`, `FlowGLParticleTypes`,
`GraphGLFocusNodeAdjacencyOns`, and `EChartsGlSeriesTypes` for the series types.

## Why the wrapper imports the core module

Every chart and component installer `import`s `echarts-gl/lib/echarts-gl.js` for its side effects:
that module registers the post-update hook which inserts the WebGL layer into the zrender painter
and mounts `echarts.graphicGL`. Upstream's `sideEffects` list only matches the files directly under
`lib/chart` and `lib/component`, not the nested installer files, so a bundler with `sideEffects`
optimization — webpack's default — drops that import and the chart renders nothing at all.

Reading an installer therefore loads the core module through the wrapper, which keeps it in the
bundle regardless of how thin the application's own imports are. The cost is that the core module
is not tree-shakable; it is a few kilobytes next to claygl and zrender.

## Build and test

```bash
./gradlew :ktjs-echarts-gl:jsBrowserTest
```

After adding or upgrading an npm dependency, refresh the shared lock file:

```bash
./gradlew kotlinUpgradePackageLock
```
