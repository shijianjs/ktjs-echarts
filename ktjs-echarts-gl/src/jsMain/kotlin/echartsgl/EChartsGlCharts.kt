package echartsgl

import echarts.EChartsExtensionInstaller

/*
 * Chart installers exported by the `echarts-gl/charts` entry.
 *
 * Every installer is an ECharts extension: pass it to `ECharts.use` before `ECharts.init`:
 *
 * ```kotlin
 * ECharts.use(Bar3DChart)
 * ECharts.use(Grid3DComponent)
 * ```
 *
 * Reading an installer also loads the `echarts-gl` core module, which registers the post update
 * hook that draws the WebGL layers.
 */

/**
 * Installs the 3D bar chart, `series.type = "bar3D"`.
 *
 * Draws bars in [Grid3DComponent], `geo3D` or `globe`; in `grid3D` the first three
 * values of a data item are `x`, `y`, `z`, and in the geographic coordinate systems they are
 * `lng`, `lat`, `value`.
 */
val Bar3DChart: EChartsExtensionInstaller get() = withEChartsGl(bar3DChartInstaller)

/**
 * Installs the 3D line chart, `series.type = "line3D"`.
 */
val Line3DChart: EChartsExtensionInstaller get() = withEChartsGl(line3DChartInstaller)

/**
 * Installs the 3D scatter chart, `series.type = "scatter3D"`.
 */
val Scatter3DChart: EChartsExtensionInstaller get() = withEChartsGl(scatter3DChartInstaller)

/**
 * Installs the 3D lines chart, `series.type = "lines3D"`.
 *
 * Draws lines between coordinates with an optional trail effect, for example flight routes on a
 * globe.
 */
val Lines3DChart: EChartsExtensionInstaller get() = withEChartsGl(lines3DChartInstaller)

/**
 * Installs the 3D polygons chart, `series.type = "polygons3D"`.
 *
 * Draws extruded polygons on `geo3D`, typically from GeoJSON features.
 */
val Polygons3DChart: EChartsExtensionInstaller get() = withEChartsGl(polygons3DChartInstaller)

/**
 * Installs the surface chart, `series.type = "surface"`.
 */
val SurfaceChart: EChartsExtensionInstaller get() = withEChartsGl(surfaceChartInstaller)

/**
 * Installs the 3D map chart, `series.type = "map3D"`.
 *
 * Draws regions of a registered map on `geo3D`, `mapbox3D` or `maptalks3D`.
 */
val Map3DChart: EChartsExtensionInstaller get() = withEChartsGl(map3DChartInstaller)

/**
 * Installs the WebGL scatter chart, `series.type = "scatterGL"`.
 *
 * A WebGL-accelerated 2D scatter that renders millions of points on a regular cartesian grid.
 */
val ScatterGLChart: EChartsExtensionInstaller get() = withEChartsGl(scatterGLChartInstaller)

/**
 * Installs the WebGL graph chart, `series.type = "graphGL"`.
 *
 * A force-directed graph whose layout runs on the GPU.
 */
val GraphGLChart: EChartsExtensionInstaller get() = withEChartsGl(graphGLChartInstaller)

/**
 * Installs the vector field chart, `series.type = "flowGL"`.
 *
 * Animates particles along a vector field, for example wind data.
 */
val FlowGLChart: EChartsExtensionInstaller get() = withEChartsGl(flowGLChartInstaller)

/**
 * Installs the WebGL lines chart, `series.type = "linesGL"`.
 *
 * A WebGL-accelerated 2D lines series for large numbers of segments.
 */
val LinesGLChart: EChartsExtensionInstaller get() = withEChartsGl(linesGLChartInstaller)
