package echarts.options

import kotlin.js.JsAny
import kotlin.js.unsafeCast

/**
 * Wraps a single value in a JavaScript array.
 *
 * ECharts accepts both a single component object and an array of components in most places; the
 * wrapper always types those fields as arrays so that multiple components stay possible.
 */
fun <T : JsAny> one(value: T): Array<T> = arrayOf(value)

/**
 * Wraps numeric series data in the array shape expected by the `data` fields.
 *
 * The element type is inferred from the target property, so the same helper fills the read-only
 * [SeriesOption.data] view, the `data` fields of components and the per-series `typedData` fields:
 *
 * ```kotlin
 * lineSeries { typedData = numbers(1.0, 2.0) }
 * ```
 *
 * The Kotlin vararg produces a primitive `DoubleArray` (a JavaScript typed array), which ECharts
 * rejects as series data, so this copies the values into a real JavaScript array.
 */
@Suppress("UNCHECKED_CAST")
fun <T : JsAny> numbers(vararg values: Double): Array<T?> {
    val data: Array<OptionDataItem?> =
        Array(values.size) { index -> OptionDataItem(OptionDataValue(values[index])) }
    return data as Array<T?>
}

/**
 * Wraps string values, such as category names, in the array shape expected by the `data` fields.
 *
 * The element type is inferred from the target property; see [numbers].
 */
@Suppress("UNCHECKED_CAST")
fun <T : JsAny> strings(vararg values: String): Array<T?> {
    val data: Array<OptionDataItem?> =
        Array(values.size) { index -> OptionDataItem(OptionDataValue(values[index])) }
    return data as Array<T?>
}

/**
 * Wraps `[x, y]` pairs (or similar tuples) as series data.
 *
 * The element type is inferred from the target property; see [numbers]. Each pair is copied into a
 * real JavaScript array for the same reason as [numbers].
 */
@Suppress("UNCHECKED_CAST")
fun <T : JsAny> points(vararg pairs: DoubleArray): Array<T?> {
    val data: Array<OptionDataItem?> = Array(pairs.size) { index ->
        OptionDataItem(Array(pairs[index].size) { position -> OptionDataValue(pairs[index][position]) })
    }
    return data as Array<T?>
}

/**
 * Wraps already built data item objects as series data.
 *
 * The element type is inferred from the target property; see [numbers].
 */
@Suppress("UNCHECKED_CAST")
fun <T : JsAny> items(vararg values: OptionDataItemObject): Array<T?> {
    val data: Array<OptionDataItem?> = Array(values.size) { index -> OptionDataItem(values[index]) }
    return data as Array<T?>
}

/**
 * Wraps already built data items of any type, for example the branches of a `*DataItem` union.
 *
 * Unlike [items], the element type follows the argument, so this is the helper for the unions
 * (`LineDataItem(…)`, `BarDataItem(…)`, …) and for the item interfaces that do not extend
 * [OptionDataItemObject]:
 *
 * ```kotlin
 * lineSeries { typedData = dataItems(LineDataItem(OptionDataValue(1.0))) }
 * ```
 */
@Suppress("UNCHECKED_CAST")
fun <T : JsAny> dataItems(vararg values: T): Array<T?> = values as Array<T?>

/**
 * `string | number`, used by positions and sizes that accept either a percentage-like string or an
 * absolute pixel value.
 */
external interface StringOrDouble

/** Wraps a percentage/alias string such as `"40%"` or `"center"`. */
fun StringOrDouble(value: String): StringOrDouble = value.unsafeCast<StringOrDouble>()

/** Wraps an absolute number. */
fun StringOrDouble(value: Double): StringOrDouble = value.unsafeCast<StringOrDouble>()

/**
 * `boolean | string`, used by options such as `selectedMode` or `triggerOn`.
 */
external interface BooleanOrString

/** Wraps a boolean. */
fun BooleanOrString(value: Boolean): BooleanOrString = value.unsafeCast<BooleanOrString>()

/** Wraps a string. */
fun BooleanOrString(value: String): BooleanOrString = value.unsafeCast<BooleanOrString>()

/**
 * `boolean | number`, used by options such as `smooth`, `progressive` or `showAllSymbol`.
 */
external interface BooleanOrDouble

/** Wraps a boolean. */
fun BooleanOrDouble(value: Boolean): BooleanOrDouble = value.unsafeCast<BooleanOrDouble>()

/** Wraps a number. */
fun BooleanOrDouble(value: Double): BooleanOrDouble = value.unsafeCast<BooleanOrDouble>()

/**
 * `number | number[]`, used by options such as `padding`, `borderRadius` or `symbolSize`.
 */
external interface DoubleOrDoubleArray

/** Wraps a single number. */
fun DoubleOrDoubleArray(value: Double): DoubleOrDoubleArray = value.unsafeCast<DoubleOrDoubleArray>()

/** Wraps an array of numbers. */
fun DoubleOrDoubleArray(values: Array<Double>): DoubleOrDoubleArray = values.unsafeCast<DoubleOrDoubleArray>()

/**
 * `string | string[]`, used by options such as `color` overrides on a single item.
 */
external interface StringOrStringArray

/** Wraps a single string. */
fun StringOrStringArray(value: String): StringOrStringArray = value.unsafeCast<StringOrStringArray>()

/** Wraps an array of strings. */
fun StringOrStringArray(values: Array<String>): StringOrStringArray = values.unsafeCast<StringOrStringArray>()
