package zrender

import kotlin.js.unsafeCast

/**
 * Renderer used by a zrender or ECharts instance: `"canvas"` or `"svg"`.
 */
sealed external interface RendererType

/** Wraps a renderer name. Prefer the constants in [RendererTypes]. */
inline fun RendererType(value: String): RendererType = value.unsafeCast<RendererType>()

/**
 * The documented values of [RendererType].
 */
object RendererTypes {
    /** The canvas renderer. */
    inline val canvas: RendererType get() = RendererType("canvas")

    /** The SVG renderer. */
    inline val svg: RendererType get() = RendererType("svg")
}

/**
 * How a text block that does not fit its box is handled.
 */
sealed external interface TextOverflow

/** Wraps an overflow name. Prefer the constants in [TextOverflows]. */
inline fun TextOverflow(value: String): TextOverflow = value.unsafeCast<TextOverflow>()

/**
 * The documented values of [TextOverflow].
 */
object TextOverflows {
    /** Cut the text at the box edge. */
    inline val truncate: TextOverflow get() = TextOverflow("truncate")

    /** Break the text at word boundaries. */
    inline val `break`: TextOverflow get() = TextOverflow("break")

    /** Break the text between any two characters. */
    inline val breakAll: TextOverflow get() = TextOverflow("breakAll")

    /** Let the text overflow the box. */
    inline val none: TextOverflow get() = TextOverflow("none")
}
