package echarts

import echarts.options.StringOrDouble
import kotlin.js.Date
import kotlin.js.JsAny

/**
 * The `echarts.format` namespace: string and number formatting helpers.
 */
external interface FormatNamespace {
    /** Adds thousands separators to a number or numeric string. */
    fun addCommas(x: StringOrDouble): String

    /** Converts `a-b` to `aB`; when [upperCaseFirst] is `true`, to `AB`. */
    fun toCamelCase(str: String, upperCaseFirst: Boolean? = definedExternally): String

    /** Escapes HTML special characters. */
    fun encodeHTML(source: String): String

    /**
     * Fills the placeholders of [tpl] with the values of [paramsList].
     *
     * [paramsList] is a parameter object (or an array of them) whose `$vars` property lists the
     * keys to substitute, e.g. `{ "$vars": ["a", "b", "c"], a: "series", b: "Jan", c: 12 }`
     * replaces `{a}`, `{b}` and `{c}`.
     */
    fun formatTpl(tpl: String, paramsList: JsAny, encode: Boolean? = definedExternally): String

    /** Returns the HTML markup of a tooltip marker for [color]. */
    fun getTooltipMarker(color: JsAny, extraCssText: String? = definedExternally): String

    /** Formats a date with an ECharts time template such as `"{yyyy}-{MM}-{dd}"`. */
    fun formatTime(tpl: String, value: JsAny, isUTC: Boolean? = definedExternally): String

    /** Upper-cases the first character. */
    fun capitalFirst(str: String): String

    /**
     * Truncates [text] so that it fits into [containerWidth] pixels with the given [font].
     *
     * @param ellipsis text appended after the truncation, `"..."` by default.
     */
    fun truncateText(
        text: String,
        containerWidth: Double,
        font: String,
        ellipsis: String? = definedExternally,
        options: JsAny? = definedExternally,
    ): String

    /** Measures a text block and returns its bounding rectangle. */
    fun getTextRect(
        text: String,
        font: String? = definedExternally,
        align: String? = definedExternally,
        verticalAlign: String? = definedExternally,
        padding: JsAny? = definedExternally,
        rich: JsAny? = definedExternally,
        truncate: Boolean? = definedExternally,
        lineHeight: Double? = definedExternally,
    ): JsAny

    /** Normalizes a CSS shorthand array. */
    fun normalizeCssArray(value: JsAny): Array<Double>
}

/**
 * The `echarts.time` namespace: date parsing and formatting helpers.
 */
external interface TimeNamespace {
    /**
     * Parses a date value (timestamp, ISO string, `Date`, ...). Returns an invalid date when the
     * input cannot be parsed.
     */
    fun parse(value: JsAny): Date

    /**
     * Formats a date with an ECharts time template such as `"{yyyy}-{MM}-{dd} {HH}:{mm}:{ss}"`.
     *
     * @param isUTC when `true`, the date is formatted in UTC.
     */
    fun format(time: JsAny, template: String, isUTC: Boolean): String

    /**
     * Rounds a date down to the given time unit (`"year"`, `"month"`, `"day"`, `"hour"`, ...).
     */
    fun roundTime(date: Date, timeUnit: String, isUTC: Boolean): Date
}

/**
 * The `echarts.number` namespace: numeric helpers.
 */
external interface NumberNamespace {
    /** Largest safe integer value. */
    val MAX_SAFE_INTEGER: Double

    /** Maps [value] linearly from [domain] to [range]. */
    fun linearMap(value: Double, domain: Array<Double>, range: Array<Double>, clamp: Boolean? = definedExternally): Double

    /** Rounds [x] to [precision] decimals. */
    fun round(x: StringOrDouble, precision: Double? = definedExternally): Double

    /** Sorts an array of numbers in place and returns it. */
    fun asc(arr: Array<Double>): Array<Double>

    /** Returns the number of decimals of a value. */
    fun getPrecision(value: StringOrDouble): Double

    /** Returns the number of decimals of a value, using a slower but safer algorithm. */
    fun getPrecisionSafe(value: StringOrDouble): Double

    /** Returns the precision that is still visible at the given pixel extent. */
    fun getPixelPrecision(dataExtent: Array<Double>, pixelExtent: Array<Double>): Double

    /** Returns the percentage of `valueList[idx]` rounded so that all percentages sum to 100. */
    fun getPercentWithPrecision(valueList: Array<Double>, idx: Double, precision: Double): Double

    /** Parses a percentage or pixel position. */
    fun parsePercent(option: JsAny, percentBase: Double, percentOffset: Double? = definedExternally): Double

    /** Normalizes a radian value to `0..2π`. */
    fun remRadian(radian: Double): Double

    /** Whether a radian value is close to zero. */
    fun isRadianAroundZero(radian: Double): Boolean

    /** Parses a date value; see [TimeNamespace.parse]. */
    fun parseDate(value: JsAny): Date

    /** Returns the order of magnitude of a number, e.g. `100` for `0.1`. */
    fun quantity(value: Double): Double

    /** Returns the exponent of the order of magnitude, e.g. `3` for `9876`. */
    fun quantityExponent(value: Double): Double

    /** Rounds a value to a "nice" number. */
    fun nice(value: Double, mode: JsAny? = definedExternally): Double

    /** Returns the value at the given quantile of a sorted array. */
    fun quantile(ascArr: Array<Double>, p: Double): Double

    /** Orders and splits overlapping intervals. */
    fun reformIntervals(list: Array<JsAny>): Array<JsAny>

    /** Whether a value can be interpreted as a number. */
    fun isNumeric(value: JsAny): Boolean

    /** Converts a numeric value to a number, returning `NaN` for non-numeric input. */
    fun numericToNumber(value: JsAny): Double
}

/**
 * The `echarts.helper` namespace: helpers used by extension authors.
 */
external interface HelperNamespace {
    /** Reads layout information from an option fragment. */
    fun getLayoutRect(positionInfo: JsAny, containerInfo: JsAny, margin: Double? = definedExternally): JsAny

    /** Returns the ECharts metadata attached to an element. */
    fun getECData(data: JsAny): JsAny

    /** Creates a data list from series data. */
    fun createList(data: JsAny): JsAny

    /** Data stack helpers. */
    val dataStack: JsAny

    /** Creates a scale from the given extent. */
    val createScale: JsAny

    /** Mixes the common axis model methods into a model class. */
    fun mixinAxisModelCommonMethods(model: JsAny)

    /** Creates a zrender text style from an option fragment. */
    fun createTextStyle(textStyle: JsAny, opts: JsAny? = definedExternally): JsAny

    /** Creates data dimensions from a source and dimension definitions. */
    fun createDimensions(source: JsAny, dimsDef: JsAny): JsAny

    /** Creates a symbol path. */
    fun createSymbol(symbolType: String, opts: JsAny): JsAny

    /** Enables the hover emphasis effect on an element. */
    fun enableHoverEmphasis(element: JsAny, focus: String? = definedExternally)
}

/**
 * The `echarts.graphic` namespace: shape and transform helpers for extension authors.
 */
external interface GraphicNamespace {
    /** Creates a shape class from a shape definition object. */
    fun extendShape(shape: JsAny): JsAny

    /** Creates a path class from path data. */
    fun extendPath(pathData: String): JsAny

    /** Creates a path element from path data. */
    fun makePath(pathData: String, opts: JsAny): JsAny

    /** Creates an image element. */
    fun makeImage(imageUrl: String, rect: JsAny): JsAny

    /** Recomputes the path of an element after its shape changed. */
    fun resizePath(el: JsAny, opts: JsAny, onlyShape: Boolean? = definedExternally)

    /** Creates an icon element from an icon name or path data. */
    fun createIcon(icon: String, opts: JsAny): JsAny

    /** Updates the animatable properties of an element. */
    fun updateProps(el: JsAny, props: JsAny, animatableModel: JsAny? = definedExternally, dataIndex: Double? = definedExternally)

    /** Initializes the animatable properties of an element. */
    fun initProps(el: JsAny, props: JsAny, animatableModel: JsAny? = definedExternally, dataIndex: Double? = definedExternally)

    /** Returns the transform between two elements. */
    fun getTransform(targetEl: JsAny, ancestorEl: JsAny): JsAny

    /** Clips a list of points to a rectangle. */
    fun clipPointsByRect(points: Array<DoubleArray>, rect: JsAny): Array<DoubleArray>

    /** Clips a rectangle to another rectangle. */
    fun clipRectByRect(targetRect: JsAny, rect: JsAny): JsAny

    /** Registers a custom shape class. */
    fun registerShape(shapeType: String, shapeClass: JsAny)

    /** Returns a registered shape class. */
    fun getShapeClass(shapeType: String): JsAny
}

/**
 * The `echarts.color` namespace: color parsing and interpolation helpers.
 */
external interface ColorNamespace {
    /** Parses a color string into `[r, g, b, a]`. */
    fun parse(colorStr: String, rgbaArr: Array<Double>? = definedExternally): Array<Double>

    /** Parses an integer from a CSS value. */
    fun parseCssInt(value: StringOrDouble): Double

    /** Parses a float from a CSS value. */
    fun parseCssFloat(value: StringOrDouble): Double

    /** Lightens or darkens a color; `level` is in `-1..1`. */
    fun lift(color: String, level: Double): String

    /** Converts a color to `#rrggbb`. */
    fun toHex(color: String): String

    /** Linearly interpolates a normalized value inside a list of RGB colors. */
    fun fastLerp(normalizedValue: Double, colors: Array<Array<Double>>, out: Array<Double>? = definedExternally): Array<Double>

    /** Linearly interpolates a normalized value inside a list of color strings. */
    fun mapToColor(normalizedValue: Double, colors: Array<String>): String

    /** Modifies the HSL components of a color. */
    fun modifyHSL(color: String, h: JsAny? = definedExternally, s: JsAny? = definedExternally, l: JsAny? = definedExternally): String

    /** Modifies the alpha channel of a color. */
    fun modifyAlpha(color: String, alpha: Double? = definedExternally): String

    /** Converts an `[r, g, b, a]` array to a color string. */
    fun stringify(arrColor: Array<Double>, type: String): String

    /** Returns the luminosity of a color. */
    fun lum(color: String, backgroundLum: Double): Double

    /** Returns a random color. */
    fun random(): String
}
