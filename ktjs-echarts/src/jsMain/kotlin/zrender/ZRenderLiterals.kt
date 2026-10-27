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

/**
 * Transform property of an element, used by `during` callbacks to read or write one value.
 */
sealed external interface TransformProp

/** Wraps a transform property name. Prefer the constants in [TransformProps]. */
inline fun TransformProp(value: String): TransformProp = value.unsafeCast<TransformProp>()

/**
 * The documented values of [TransformProp].
 */
object TransformProps {
    /** Horizontal position. */
    inline val x: TransformProp get() = TransformProp("x")

    /** Vertical position. */
    inline val y: TransformProp get() = TransformProp("y")

    /** Horizontal origin of the transform. */
    inline val originX: TransformProp get() = TransformProp("originX")

    /** Vertical origin of the transform. */
    inline val originY: TransformProp get() = TransformProp("originY")

    /** Horizontal anchor of the transform. */
    inline val anchorX: TransformProp get() = TransformProp("anchorX")

    /** Vertical anchor of the transform. */
    inline val anchorY: TransformProp get() = TransformProp("anchorY")

    /** Rotation in radians. */
    inline val rotation: TransformProp get() = TransformProp("rotation")

    /** Horizontal scale. */
    inline val scaleX: TransformProp get() = TransformProp("scaleX")

    /** Vertical scale. */
    inline val scaleY: TransformProp get() = TransformProp("scaleY")

    /** Horizontal skew. */
    inline val skewX: TransformProp get() = TransformProp("skewX")

    /** Vertical skew. */
    inline val skewY: TransformProp get() = TransformProp("skewY")
}

/**
 * Built-in position of the text attached to an element.
 */
sealed external interface BuiltinTextPosition

/** Wraps a text position name. Prefer the constants in [BuiltinTextPositions]. */
inline fun BuiltinTextPosition(value: String): BuiltinTextPosition =
    value.unsafeCast<BuiltinTextPosition>()

/**
 * The documented values of [BuiltinTextPosition].
 */
object BuiltinTextPositions {
    /** Left of the element. */
    inline val left: BuiltinTextPosition get() = BuiltinTextPosition("left")

    /** Right of the element. */
    inline val right: BuiltinTextPosition get() = BuiltinTextPosition("right")

    /** Above the element. */
    inline val top: BuiltinTextPosition get() = BuiltinTextPosition("top")

    /** Below the element. */
    inline val bottom: BuiltinTextPosition get() = BuiltinTextPosition("bottom")

    /** Centered inside the element. */
    inline val inside: BuiltinTextPosition get() = BuiltinTextPosition("inside")

    /** Inside the element, aligned left. */
    inline val insideLeft: BuiltinTextPosition get() = BuiltinTextPosition("insideLeft")

    /** Inside the element, aligned right. */
    inline val insideRight: BuiltinTextPosition get() = BuiltinTextPosition("insideRight")

    /** Inside the element, aligned top. */
    inline val insideTop: BuiltinTextPosition get() = BuiltinTextPosition("insideTop")

    /** Inside the element, aligned bottom. */
    inline val insideBottom: BuiltinTextPosition get() = BuiltinTextPosition("insideBottom")

    /** Inside the top-left corner. */
    inline val insideTopLeft: BuiltinTextPosition get() = BuiltinTextPosition("insideTopLeft")

    /** Inside the top-right corner. */
    inline val insideTopRight: BuiltinTextPosition get() = BuiltinTextPosition("insideTopRight")

    /** Inside the bottom-left corner. */
    inline val insideBottomLeft: BuiltinTextPosition get() = BuiltinTextPosition("insideBottomLeft")

    /** Inside the bottom-right corner. */
    inline val insideBottomRight: BuiltinTextPosition get() = BuiltinTextPosition("insideBottomRight")
}
