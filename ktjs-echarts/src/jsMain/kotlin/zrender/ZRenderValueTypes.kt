package zrender

import js.objects.unsafeJso
import kotlin.js.unsafeCast
import web.cssom.FontWeight
import web.html.HTMLCanvasElement
import web.html.HTMLImageElement
import web.html.HTMLVideoElement
import web.svg.SVGElement

/**
 * A rectangle with `x`, `y`, `width` and `height`.
 */
external interface RectLike {
    /** X coordinate. */
    var x: Double

    /** Y coordinate. */
    var y: Double

    /** Width. */
    var width: Double

    /** Height. */
    var height: Double
}

/**
 * A color as accepted by zrender and ECharts: a CSS color string, a linear or radial gradient, or
 * a pattern.
 *
 * The upstream type is `ZRColor = ColorString | LinearGradientObject | RadialGradientObject |
 * PatternObject`. Create a value with one of the `ZRColor(...)` functions:
 *
 * ```kotlin
 * itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#5470c6") }
 * ```
 */
external interface ZRColor

/** Wraps a CSS color string such as `"#5470c6"` or `"rgba(0, 0, 0, 0.5)"`. */
fun ZRColor(value: String): ZRColor = value.unsafeCast<ZRColor>()

/** Wraps a linear gradient created with [linearGradient]. */
fun ZRColor(value: LinearGradientObject): ZRColor = value.unsafeCast<ZRColor>()

/** Wraps a radial gradient created with [radialGradient]. */
fun ZRColor(value: RadialGradientObject): ZRColor = value.unsafeCast<ZRColor>()

/** Wraps an image or SVG pattern. */
fun ZRColor(value: PatternObject): ZRColor = value.unsafeCast<ZRColor>()

/**
 * A pattern fill: either an image pattern or an SVG pattern.
 */
external interface PatternObject

/** Wraps an image pattern. */
fun PatternObject(value: ImagePatternObject): PatternObject = value.unsafeCast<PatternObject>()

/** Wraps an SVG pattern. */
fun PatternObject(value: SVGPatternObject): PatternObject = value.unsafeCast<PatternObject>()

/**
 * A pattern backed by an image, canvas or video.
 *
 * Create one with `unsafeJso`:
 *
 * ```kotlin
 * val pattern = unsafeJso<ImagePatternObject> {
 *     image = ImageLike(imageElement)
 *     repeat = "repeat"
 * }
 * ```
 */
external interface ImagePatternObject {
    /** Image, canvas or video used as the pattern. */
    var image: ImageLike

    /** How the image repeats: `"repeat"`, `"repeat-x"`, `"repeat-y"` or `"no-repeat"`. */
    var repeat: String?

    /** Width of one tile; defaults to the image width. */
    var imageWidth: Double?

    /** Height of one tile; defaults to the image height. */
    var imageHeight: Double?
}

/**
 * A pattern backed by an SVG element.
 */
external interface SVGPatternObject {
    /** SVG element used as the pattern. */
    var svgElement: SVGElement?

    /** Width of one tile. */
    var svgWidth: Double?

    /** Height of one tile. */
    var svgHeight: Double?
}

/**
 * An image source accepted by [ImagePatternObject.image]: an image, a canvas or a video element.
 */
external interface ImageLike

/** Wraps an image element. */
fun ImageLike(value: HTMLImageElement): ImageLike = value.unsafeCast<ImageLike>()

/** Wraps a canvas element. */
fun ImageLike(value: HTMLCanvasElement): ImageLike = value.unsafeCast<ImageLike>()

/** Wraps a video element. */
fun ImageLike(value: HTMLVideoElement): ImageLike = value.unsafeCast<ImageLike>()

/**
 * `string | ImageLike`, accepted wherever zrender takes an image source.
 */
external interface StringOrImageLike

/** Wraps an image URL. */
fun StringOrImageLike(value: String): StringOrImageLike = value.unsafeCast<StringOrImageLike>()

/** Wraps an image source. */
fun StringOrImageLike(value: ImageLike): StringOrImageLike = value.unsafeCast<StringOrImageLike>()

/**
 * Font weight: a CSS font-weight keyword or a number in `100..900`.
 *
 * Upstream `ZRFontWeight = FontWeight | number`. Use the constants for the keywords and
 * `ZRFontWeight(700.0)` for a numeric weight.
 */
external interface ZRFontWeight

/** Wraps a CSS font-weight keyword. */
fun ZRFontWeight(value: FontWeight): ZRFontWeight = value.unsafeCast<ZRFontWeight>()

/** Wraps a numeric font weight. */
fun ZRFontWeight(value: Double): ZRFontWeight = value.unsafeCast<ZRFontWeight>()

/**
 * The keyword values of [ZRFontWeight]; numeric weights are passed as `ZRFontWeight(700.0)`.
 */
object ZRFontWeights {
    /** `"normal"`. */
    inline val normal: ZRFontWeight get() = ZRFontWeight(FontWeight.normal)

    /** `"bold"`. */
    inline val bold: ZRFontWeight get() = ZRFontWeight(FontWeight.bold)

    /** `"bolder"`. */
    inline val bolder: ZRFontWeight get() = ZRFontWeight(FontWeight.bolder)

    /** `"lighter"`. */
    inline val lighter: ZRFontWeight get() = ZRFontWeight(FontWeight.lighter)
}

/**
 * Line type: `"solid"`, `"dashed"`, `"dotted"`, a number, or a `[dash, gap]` array.
 */
external interface ZRLineType

/** Wraps a line type name. Prefer the constants in [ZRLineTypes]. */
inline fun ZRLineType(value: String): ZRLineType = value.unsafeCast<ZRLineType>()

/**
 * The keyword values of [ZRLineType]; dash patterns are passed as an array of numbers.
 */
object ZRLineTypes {
    /** `"solid"`. */
    inline val solid: ZRLineType get() = ZRLineType("solid")

    /** `"dashed"`. */
    inline val dashed: ZRLineType get() = ZRLineType("dashed")

    /** `"dotted"`. */
    inline val dotted: ZRLineType get() = ZRLineType("dotted")
}

/** Wraps a dash pattern. */
fun ZRLineType(values: Array<Double>): ZRLineType = values.unsafeCast<ZRLineType>()

/** Wraps a numeric line type. */
fun ZRLineType(value: Double): ZRLineType = value.unsafeCast<ZRLineType>()

/**
 * A gradient color stop, used by [linearGradient] and [radialGradient].
 */
external interface GradientColorStop {
    /** Position in the range `0..1`. */
    var offset: Double?

    /** CSS color at this stop. */
    var color: String?
}

/**
 * A linear gradient object as accepted by every color-valued option.
 */
external interface LinearGradientObject {
    /** Always `"linear"`. */
    var type: String?

    /** Start point x. */
    var x: Double?

    /** Start point y. */
    var y: Double?

    /** End point x. */
    var x2: Double?

    /** End point y. */
    var y2: Double?

    /** Colors along the gradient. */
    var colorStops: Array<GradientColorStop>?

    /** Whether coordinates are absolute canvas coordinates. */
    var global: Boolean?
}

/**
 * A radial gradient object as accepted by every color-valued option.
 */
external interface RadialGradientObject {
    /** Always `"radial"`. */
    var type: String?

    /** Center x. */
    var x: Double?

    /** Center y. */
    var y: Double?

    /** Radius. */
    var r: Double?

    /** Colors along the gradient. */
    var colorStops: Array<GradientColorStop>?

    /** Whether coordinates are absolute canvas coordinates. */
    var global: Boolean?
}

/**
 * Creates a gradient color stop for [linearGradient] / [radialGradient].
 */
fun gradientColorStop(offset: Double, color: String): GradientColorStop =
    unsafeJso<GradientColorStop> {
        this.offset = offset
        this.color = color
    }

/**
 * Creates a linear gradient that can be assigned to any color-valued option.
 *
 * @param fromX start point x, `0..1` relative to the shape bounding box when [global] is `false`.
 * @param fromY start point y.
 * @param toX end point x.
 * @param toY end point y.
 * @param colorStops colors along the gradient.
 * @param global when `true`, coordinates are absolute canvas coordinates.
 */
fun linearGradient(
    fromX: Double,
    fromY: Double,
    toX: Double,
    toY: Double,
    colorStops: Array<GradientColorStop>,
    global: Boolean = false,
): ZRColor = ZRColor(
    unsafeJso<LinearGradientObject> {
        type = "linear"
        x = fromX
        y = fromY
        x2 = toX
        y2 = toY
        this.colorStops = colorStops
        this.global = global
    },
)

/**
 * Creates a radial gradient that can be assigned to any color-valued option.
 *
 * @param centerX center x.
 * @param centerY center y.
 * @param radius radius.
 * @param colorStops colors along the gradient.
 * @param global when `true`, coordinates are absolute canvas coordinates.
 */
fun radialGradient(
    centerX: Double,
    centerY: Double,
    radius: Double,
    colorStops: Array<GradientColorStop>,
    global: Boolean = false,
): ZRColor = ZRColor(
    unsafeJso<RadialGradientObject> {
        type = "radial"
        x = centerX
        y = centerY
        r = radius
        this.colorStops = colorStops
        this.global = global
    },
)
