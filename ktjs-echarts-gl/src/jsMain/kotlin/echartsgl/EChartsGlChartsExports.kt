@file:JsModule("echarts-gl/charts.js")

package echartsgl

import echarts.EChartsExtensionInstaller
import kotlin.js.JsName

/*
 * Chart installers exported by the `echarts-gl/charts` entry.
 *
 * The declarations are internal because the public values in `EChartsGlCharts.kt` have to load the
 * core module first; see `EChartsGlCore.kt` for why.
 */

@JsName("Bar3DChart")
internal external val bar3DChartInstaller: EChartsExtensionInstaller

@JsName("Line3DChart")
internal external val line3DChartInstaller: EChartsExtensionInstaller

@JsName("Scatter3DChart")
internal external val scatter3DChartInstaller: EChartsExtensionInstaller

@JsName("Lines3DChart")
internal external val lines3DChartInstaller: EChartsExtensionInstaller

@JsName("Polygons3DChart")
internal external val polygons3DChartInstaller: EChartsExtensionInstaller

@JsName("SurfaceChart")
internal external val surfaceChartInstaller: EChartsExtensionInstaller

@JsName("Map3DChart")
internal external val map3DChartInstaller: EChartsExtensionInstaller

@JsName("ScatterGLChart")
internal external val scatterGLChartInstaller: EChartsExtensionInstaller

@JsName("GraphGLChart")
internal external val graphGLChartInstaller: EChartsExtensionInstaller

@JsName("FlowGLChart")
internal external val flowGLChartInstaller: EChartsExtensionInstaller

@JsName("LinesGLChart")
internal external val linesGLChartInstaller: EChartsExtensionInstaller
