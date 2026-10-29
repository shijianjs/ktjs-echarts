package echartsgl

import kotlin.js.JsAny

/*
 * Loading of the `echarts-gl` core module.
 *
 * The module registers the post update hook that inserts the WebGL layer into the zrender painter
 * and mounts `echarts.graphicGL`. The chart and component installers `import '../../echarts-gl'`
 * for the same reason, but upstream's `sideEffects` globs only match the files directly under
 * `lib/chart` and `lib/component`, not the nested installer files, so a bundler with `sideEffects`
 * optimization (webpack's default) drops that import and the chart renders nothing.
 */

private var loadedCore: JsAny? = null

/**
 * Returns [installer] after reading the core module, so that a bundler keeps its side effects.
 */
internal fun <T : JsAny> withEChartsGl(installer: T): T {
    if (loadedCore == null) loadedCore = eChartsGlCoreExport
    return installer
}

private var shadowShaderRegistered = false

/**
 * Returns [installer] after registering the shadow shader chunks of `echarts-gl`.
 *
 * `Geo3DBuilder` creates a `groundPlane` material for every shading model on construction, including
 * `"shadow"`, which maps to the `ecgl.displayShadow` chunks. Upstream registers those chunks only
 * from the mapbox3D and maptalks3D views, so a bundle without them logs four missing-shader errors
 * per `geo3D` page even though nothing uses that material.
 */
internal fun <T : JsAny> withGeo3DShadowShader(installer: T): T {
    if (!shadowShaderRegistered) {
        graphicGlNamespace.shader.`import`(displayShadowShaderSource)
        shadowShaderRegistered = true
    }
    return installer
}
