package demo.examples.scatter

import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import demo.examples.chord.column
import demo.examples.shared.Lcg
import demo.examples.shared.every
import demo.examples.themeriver.days

/** `scatter-large` plots one million points; the count is the original `5e5` per series. */
internal const val LARGE_SCATTER_POINTS = 500_000

/**
 * The `[x, y]` table of `scatter-large`.
 *
 * The original writes into a `Float32Array` and adds a random jitter to the sine curve; the
 * generate-then-read order is the same here, so the values match the upstream shape while staying
 * reproducible.
 */
internal fun largeScatterData(random: Lcg, offset: Double): Array<ScatterDataItem?> {
    val x = DoubleArray(LARGE_SCATTER_POINTS)
    val y = DoubleArray(LARGE_SCATTER_POINTS)
    for (index in 0 until LARGE_SCATTER_POINTS) {
        val xValue = random.next() * 10
        x[index] = xValue
        y[index] = kotlin.math.sin(xValue) - xValue * (if (LARGE_SCATTER_POINTS % 2 == 0) -0.1 else 0.1) * random.next() + offset / 10
    }
    val points = Array(LARGE_SCATTER_POINTS) { index ->
        OptionDataItem(Array(2) { column -> OptionDataValue(if (column == 0) x[index] else y[index]) })
    }
    return points.unsafeCast<Array<ScatterDataItem?>>()
}

/**
 * Shape of `masterPainterColorChoice.json`: the painters as column arrays plus their colours.
 *
 * `x` holds the years as **strings** (the plotly export writes `"1007"`), which the original
 * converts with the unary `+` operator; a Kotlin `Double` cast would reject them.
 */
internal external interface PainterColorData {
    val x: Array<JsAny>
    val y: Array<JsAny>
    val marker: PainterColorMarker
}

/** The per-point symbol size and fill colour of [PainterColorData]. */
internal external interface PainterColorMarker {
    val size: Array<Double>
    val sizeref: Double
    val color: Array<String>
}

/**
 * `scatter-large` of the original draws the two series in one chart: `A` on the plain sine and `B`
 * lifted by `10 / 10` of the offset, both in large mode.
 */
internal fun largeScatterSeries(name: String, data: Array<ScatterDataItem?>): ScatterSeriesOption =
    scatterSeries {
        this.name = name
        typedData = data
        dimensions = arrayOf(DimensionDefinitionLoose("x"), DimensionDefinitionLoose("y"))
        symbolSize = SymbolSize(3.0)
        itemStyle = unsafeJso<ItemStyleOption> { opacity = 0.4 }
        large = true
    }

/** The hours of `scatter-single-axis`. */
internal val SINGLE_AXIS_HOURS =
    arrayOf("12a", "1a", "2a", "3a", "4a", "5a", "6a", "7a", "8a", "9a", "10a", "11a",
        "12p", "1p", "2p", "3p", "4p", "5p", "6p", "7p", "8p", "9p", "10p", "11p")

/** The days of `scatter-single-axis`, top to bottom. */
internal val SINGLE_AXIS_DAYS =
    arrayOf("Saturday", "Friday", "Thursday", "Wednesday", "Tuesday", "Monday", "Sunday")

/**
 * The punch card of `scatter-single-axis`: for every day, the commits per hour. `rows[i][h]` is the
 * value the original pushes as `[hour, commits]` into the series of day `i`.
 */
internal val SINGLE_AXIS_VALUES: Array<DoubleArray> =
    arrayOf(
        doubleArrayOf(5.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0, 4.0, 1.0, 1.0, 3.0, 4.0, 6.0, 4.0, 4.0, 3.0, 3.0, 2.0, 5.0),
        doubleArrayOf(7.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 5.0, 2.0, 2.0, 6.0, 9.0, 11.0, 6.0, 7.0, 8.0, 12.0, 5.0, 5.0, 7.0, 2.0),
        doubleArrayOf(1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 3.0, 2.0, 1.0, 9.0, 8.0, 10.0, 6.0, 5.0, 5.0, 5.0, 7.0, 4.0, 2.0, 4.0),
        doubleArrayOf(7.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 5.0, 4.0, 7.0, 14.0, 13.0, 12.0, 9.0, 5.0, 5.0, 10.0, 6.0, 4.0, 4.0, 1.0),
        doubleArrayOf(1.0, 3.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 2.0, 4.0, 4.0, 2.0, 4.0, 4.0, 14.0, 12.0, 1.0, 8.0, 5.0, 3.0, 7.0, 3.0, 0.0),
        doubleArrayOf(2.0, 1.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 4.0, 1.0, 5.0, 10.0, 5.0, 7.0, 11.0, 6.0, 0.0, 5.0, 3.0, 4.0, 2.0, 0.0),
        doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 2.0, 1.0, 3.0, 4.0, 0.0, 0.0, 0.0, 0.0, 1.0, 2.0, 2.0, 6.0),
    )

/** The `[hour, commits]` points of one day of `scatter-single-axis`. */
internal fun singleAxisPoints(day: Int): Array<ScatterDataItem?> =
    points(
        *Array(SINGLE_AXIS_HOURS.size) { hour ->
            doubleArrayOf(hour.toDouble(), SINGLE_AXIS_VALUES[day][hour])
        },
    )

/**
 * The `[year, hue]` points of `scatter-painter-choice`.
 *
 * The year column of the upstream table is a string, so both columns go through `Number(...)` the
 * way the unary `+` of the original does.
 */
internal fun painterPoints(painters: PainterColorData): Array<ScatterDataItem?> {
    val rows =
        Array(painters.x.size) { index ->
            doubleArrayOf(toNumber(painters.x[index]), toNumber(painters.y[index]))
        }
    return points(*rows)
}

/** `Number(value)` of a JSON column entry. */
internal fun toNumber(value: JsAny?): Double = js("(value) => Number(value)")(value) as Double

/**
 * The per-point colour callback of `scatter-painter-choice`.
 *
 * ECharts calls `itemStyle.color` with the params of the data item, so the colour table is bound to
 * the callback once instead of being looked up per call.
 */
internal fun painterColors(table: Array<String>): (CallbackDataParams) -> String =
    js("(table) => function (params) { return table[params.dataIndex]; }")(table)
        as (CallbackDataParams) -> String

/** `12345` as `"12,345"`, the formatting `echarts.format.addCommas` applies to the title. */
internal fun formatThousands(value: Int): String = value.toString().reversed().chunked(3).joinToString(",").reversed()
