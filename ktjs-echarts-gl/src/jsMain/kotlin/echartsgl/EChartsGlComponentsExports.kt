@file:JsModule("echarts-gl/components.js")

package echartsgl

import echarts.EChartsExtensionInstaller
import kotlin.js.JsName

/*
 * Component installers exported by the `echarts-gl/components` entry.
 *
 * The declarations are internal because the public values in `EChartsGlComponents.kt` have to load
 * the core module first; see `EChartsGlCore.kt` for why.
 */

@JsName("Grid3DComponent")
internal external val grid3DComponentInstaller: EChartsExtensionInstaller

@JsName("Geo3DComponent")
internal external val geo3DComponentInstaller: EChartsExtensionInstaller

@JsName("GlobeComponent")
internal external val globeComponentInstaller: EChartsExtensionInstaller

@JsName("Mapbox3DComponent")
internal external val mapbox3DComponentInstaller: EChartsExtensionInstaller

@JsName("Maptalks3DComponent")
internal external val maptalks3DComponentInstaller: EChartsExtensionInstaller
