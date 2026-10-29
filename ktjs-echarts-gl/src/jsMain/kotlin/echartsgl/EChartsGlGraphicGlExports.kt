@file:JsModule("echarts-gl/lib/util/graphicGL.js")

package echartsgl

import kotlin.js.JsName

/*
 * The `graphicGL` namespace of `echarts-gl`.
 *
 * The wrapper reads it only to register shader chunks that upstream registers from modules the
 * wrapper does not install; see `EChartsGlShadowShader.kt`.
 */

/** The `graphicGL` namespace, the default export of `echarts-gl`'s `util/graphicGL`. */
internal external interface GraphicGlNamespace {
    /** Registry of the shader chunks, a claygl `Shader` with its static members. */
    @JsName("Shader")
    val shader: GraphicGlShaderRegistry
}

/** Static side of a claygl `Shader`: the chunk registry. */
internal external interface GraphicGlShaderRegistry {
    /** Registers every `@export` chunk of [source]; repeat calls overwrite the same names. */
    fun `import`(source: String)

    /** Returns the registered chunk called [name]; an unregistered name comes back empty. */
    fun source(name: String): String
}

/** The namespace object itself. */
@JsName("default")
internal external val graphicGlNamespace: GraphicGlNamespace
