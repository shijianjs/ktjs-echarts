package echarts.options

import kotlin.js.JsAny
import kotlin.js.unsafeCast

/**
 * Creates an empty JavaScript object typed as [T] and applies [block] to it.
 *
 * ECharts options are plain JavaScript objects, so the wrapper models them as external interfaces
 * with mutable properties. Use this builder to create them with named properties:
 *
 * ```kotlin
 * val option = echartsOption<EChartsOption> {
 *     xAxis = one(echartsOption<XAxisOption> { type = "category" })
 *     series = one(lineSeries { data = numbers(1.0, 2.0, 3.0) })
 * }
 * ```
 *
 * The returned object is a real JavaScript object literal, not a Kotlin class instance.
 */
inline fun <T : JsAny> echartsOption(block: T.() -> Unit): T {
    val result = js("({})").unsafeCast<T>()
    result.block()
    return result
}

/**
 * Wraps a single value in a JavaScript array.
 *
 * ECharts accepts both a single component object and an array of components in most places; the
 * wrapper always types those fields as arrays so that multiple components stay possible.
 */
fun <T : JsAny> one(value: T): Array<T> = arrayOf(value)

/**
 * Wraps numeric series data in the `Array<JsAny>` shape used by the typed options.
 *
 * The Kotlin vararg produces a primitive `DoubleArray` (a JavaScript typed array), which ECharts
 * rejects as series data, so this copies the values into a real JavaScript array.
 */
fun numbers(vararg values: Double): Array<JsAny> =
    Array(values.size) { index -> values[index].unsafeCast<JsAny>() }

/**
 * Wraps `[x, y]` pairs (or similar tuples) as series data.
 *
 * Each pair is copied into a real JavaScript array for the same reason as [numbers].
 */
fun points(vararg pairs: DoubleArray): Array<JsAny> =
    Array(pairs.size) { index ->
        Array(pairs[index].size) { position -> pairs[index][position].unsafeCast<JsAny>() }
            .unsafeCast<JsAny>()
    }

/**
 * Wraps already built data item objects as series data.
 */
fun items(vararg values: JsAny): Array<JsAny> = values.unsafeCast<Array<JsAny>>()

/**
 * `string | number`, used by positions and sizes that accept either a percentage-like string or an
 * absolute pixel value.
 */
external interface StringOrDouble : JsAny

/** Wraps a percentage/alias string such as `"40%"` or `"center"`. */
fun StringOrDouble(value: String): StringOrDouble = value.unsafeCast<StringOrDouble>()

/** Wraps an absolute number. */
fun StringOrDouble(value: Double): StringOrDouble = value.unsafeCast<StringOrDouble>()

/**
 * `boolean | string`, used by options such as `selectedMode` or `triggerOn`.
 */
external interface BooleanOrString : JsAny

/** Wraps a boolean. */
fun BooleanOrString(value: Boolean): BooleanOrString = value.unsafeCast<BooleanOrString>()

/** Wraps a string. */
fun BooleanOrString(value: String): BooleanOrString = value.unsafeCast<BooleanOrString>()

/**
 * `boolean | number`, used by options such as `smooth`, `progressive` or `showAllSymbol`.
 */
external interface BooleanOrDouble : JsAny

/** Wraps a boolean. */
fun BooleanOrDouble(value: Boolean): BooleanOrDouble = value.unsafeCast<BooleanOrDouble>()

/** Wraps a number. */
fun BooleanOrDouble(value: Double): BooleanOrDouble = value.unsafeCast<BooleanOrDouble>()

/**
 * `number | number[]`, used by options such as `padding`, `borderRadius` or `symbolSize`.
 */
external interface DoubleOrDoubleArray : JsAny

/** Wraps a single number. */
fun DoubleOrDoubleArray(value: Double): DoubleOrDoubleArray = value.unsafeCast<DoubleOrDoubleArray>()

/** Wraps an array of numbers. */
fun DoubleOrDoubleArray(values: Array<Double>): DoubleOrDoubleArray = values.unsafeCast<DoubleOrDoubleArray>()

/**
 * `string | string[]`, used by options such as `color` overrides on a single item.
 */
external interface StringOrStringArray : JsAny

/** Wraps a single string. */
fun StringOrStringArray(value: String): StringOrStringArray = value.unsafeCast<StringOrStringArray>()

/** Wraps an array of strings. */
fun StringOrStringArray(values: Array<String>): StringOrStringArray = values.unsafeCast<StringOrStringArray>()

/**
 * A gradient color stop, used by [linearGradient] and [radialGradient].
 */
external interface GradientColorStop : JsAny {
    /** Position in the range `0..1`. */
    var offset: Double?

    /** CSS color at this stop. */
    var color: String?
}

/**
 * Creates a gradient color stop for [linearGradient] / [radialGradient].
 */
fun gradientColorStop(offset: Double, color: String): GradientColorStop =
    echartsOption { this.offset = offset; this.color = color }

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
): JsAny = echartsOption<LinearGradientObject> {
    type = "linear"
    x = fromX
    y = fromY
    x2 = toX
    y2 = toY
    this.colorStops = colorStops
    this.global = global
}

/**
 * A linear gradient object as accepted by every color-valued option.
 */
external interface LinearGradientObject : JsAny {
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
): JsAny = echartsOption<RadialGradientObject> {
    type = "radial"
    x = centerX
    y = centerY
    r = radius
    this.colorStops = colorStops
    this.global = global
}

/**
 * A radial gradient object as accepted by every color-valued option.
 */
external interface RadialGradientObject : JsAny {
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
