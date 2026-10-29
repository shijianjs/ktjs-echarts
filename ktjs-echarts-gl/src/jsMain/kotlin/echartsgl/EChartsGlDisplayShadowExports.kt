@file:JsModule("echarts-gl/lib/util/shader/displayShadow.glsl.js")

package echartsgl

import kotlin.js.JsName

/**
 * Source of the `ecgl.displayShadow` shader chunks.
 *
 * Registering it is the wrapper's job because upstream only does it from the mapbox3D and maptalks3D
 * views, which the wrapper does not install; `Geo3DBuilder` nevertheless creates the matching
 * material for the ground plane of every `geo3D` page and logs a missing-shader error without it.
 */
@JsName("default")
internal external val displayShadowShaderSource: String
