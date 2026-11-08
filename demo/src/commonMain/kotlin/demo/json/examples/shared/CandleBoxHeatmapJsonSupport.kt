package demo.json.examples.shared

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.examples.dataset.row
import demo.json.points
import demo.json.rowsOf

/** Days since 1970-01-01 of a proleptic Gregorian date, the usual `days_from_civil` algorithm. */
internal fun epochDay(year: Int, month: Int, day: Int): Int {
    val shiftedYear = if (month <= 2) year - 1 else year
    val era = (if (shiftedYear >= 0) shiftedYear else shiftedYear - 399) / 400
    val yearOfEra = shiftedYear - era * 400
    val shiftedMonth = if (month > 2) month - 3 else month + 9
    val dayOfYear = (153 * shiftedMonth + 2) / 5 + day - 1
    val dayOfEra = yearOfEra * 365 + yearOfEra / 4 - yearOfEra / 100 + dayOfYear
    return era * 146097 + dayOfEra - 719468
}

/** Inverse of [epochDay]; the result is `[year, month, day]`. */
internal fun civilDay(epoch: Int): IntArray {
    val shifted = epoch + 719468
    val era = (if (shifted >= 0) shifted else shifted - 146096) / 146097
    val dayOfEra = shifted - era * 146097
    val yearOfEra = (dayOfEra - dayOfEra / 1460 + dayOfEra / 36524 - dayOfEra / 146096) / 365
    val dayOfYear = dayOfEra - (365 * yearOfEra + yearOfEra / 4 - yearOfEra / 100)
    val shiftedMonth = (5 * dayOfYear + 2) / 153
    val day = dayOfYear - (153 * shiftedMonth + 2) / 5 + 1
    val month = if (shiftedMonth < 10) shiftedMonth + 3 else shiftedMonth - 9
    val year = yearOfEra + era * 400 + if (month <= 2) 1 else 0
    return intArrayOf(year, month, day)
}

/** `yyyy-MM-dd` of an epoch day. */
internal fun isoDate(epoch: Int): String {
    val parts = civilDay(epoch)
    return "${parts[0]}-${twoDigits(parts[1])}-${twoDigits(parts[2])}"
}

/** Two-digit padding of a date part. */
internal fun twoDigits(value: Int): String = value.toString().padStart(2, '0')

/** Whether an epoch day is a weekday; 1970-01-01 was a Thursday. */
internal fun isWeekday(epoch: Int): Boolean = ((epoch + 3) % 7 + 7) % 7 < 5

/** Deterministic stand-in for the `Math.random()` of the regenerated candlestick pages. */
internal class CandleRandom(seed: Long) {
    private var state = seed

    /** Next value in `0..1`. */
    fun next(): Double {
        state = state * 6364136223846793005L + 1442695040888963407L
        return (state ushr 11).toDouble() / 9007199254740992.0
    }
}

/** Rounds a price to the two decimals it is quoted with. */
internal fun round2(value: Double): Double = kotlin.math.round(value * 100.0) / 100.0

/** Three decimals, the precision of the generated heatmap field. */
internal fun round3(value: Double): Double = kotlin.math.round(value * 1000.0) / 1000.0

/** One trading day: its date, its four prices and its volume. */
internal class CandleRow(
    val date: String,
    val open: Double,
    val close: Double,
    val lowest: Double,
    val highest: Double,
    val volume: Double,
)

/**
 * A [count]-day random walk over the weekdays that follow [startEpoch].
 *
 * The pages that upstream fills from a transcribed table are regenerated: every day opens at the
 * previous close, drifts by [drift] and closes at a random body of [volatility] size, so the moving
 * averages, the extremes and the zoom ranges of the original keep their meaning.
 */
internal fun candleWalk(
    startEpoch: Int,
    count: Int,
    startPrice: Double,
    drift: Double,
    volatility: Double,
    seed: Long,
): List<CandleRow> {
    val random = CandleRandom(seed)
    val rows = mutableListOf<CandleRow>()
    var price = startPrice
    var epoch = startEpoch
    while (rows.size < count) {
        if (isWeekday(epoch)) {
            val open = price
            val close = kotlin.math.max(1.0, open + drift + (random.next() - 0.5) * volatility)
            val highest = kotlin.math.max(open, close) + random.next() * volatility * 0.5
            val lowest =
                kotlin.math.max(1.0, kotlin.math.min(open, close) - random.next() * volatility * 0.5)
            rows +=
                CandleRow(
                    date = isoDate(epoch),
                    open = round2(open),
                    close = round2(close),
                    lowest = round2(lowest),
                    highest = round2(highest),
                    volume = kotlin.math.round(4000.0 + random.next() * 9000.0),
                )
            price = close
        }
        epoch++
    }
    return rows
}

/** The `[open, close, lowest, highest]` rows of a candlestick series. */
internal fun candleRows(rows: List<CandleRow>): List<DataValue> =
    rowsOf(*rows.map { listOf<Number>(it.open, it.close, it.lowest, it.highest) }.toTypedArray())

/** The category labels of a candle table. */
internal fun candleCategories(rows: List<CandleRow>): Array<String> =
    rows.map { it.date }.toTypedArray()

/** Upstream `calculateMA`: the mean of the last [window] closes, `"-"` until the window is full. */
internal fun movingAverage(rows: List<CandleRow>, window: Int): List<DataValue> =
    rows.mapIndexed { index, _ ->
        if (index < window) {
            textValue("-")
        } else {
            val sum = (index - window + 1..index).sumOf { rows[it].close }
            valueOf(round2(sum / window))
        }
    }

/** Window lengths of the moving-average lines of the Shanghai pages. */
internal val MA_WINDOWS = intArrayOf(5, 10, 20, 30)

/** The four moving-average lines of the Shanghai pages. */
internal fun SeriesBuilder.movingAverageLines(
    rows: List<CandleRow>,
    lineOpacity: Double?,
    lineWidth: Double?,
    symbols: Boolean?,
) {
    MA_WINDOWS.forEach { window ->
        line {
            name = "MA$window"
            smooth = BooleanOrNumber.of(true)
            if (symbols != null) showSymbol = symbols
            lineStyle =
                LineStyleOption().apply {
                    if (lineOpacity != null) opacity = lineOpacity
                    if (lineWidth != null) width = lineWidth
                }
            data = movingAverage(rows, window)
        }
    }
}

/** The MA5 / MA10 / MA20 lines of `candlestick-touch`, smoothed and rounded to two decimals. */
internal fun SeriesBuilder.touchMovingAverages(rows: List<CandleRow>) {
    listOf(5, 10, 20).forEach { window ->
        line {
            name = "MA$window"
            showSymbol = false
            smooth = BooleanOrNumber.of(true)
            lineStyle = LineStyleOption().apply { width = 1.0 }
            data = movingAverage(rows, window)
        }
    }
}

// ---------------------------------------------------------------------------------------------
// candlestick-touch data
// ---------------------------------------------------------------------------------------------

/** The trading calendar of `candlestick-touch`, the upstream `dates`. */
internal val TOUCH_DATES =
    arrayOf(
        "2016-03-29", "2016-03-30", "2016-03-31", "2016-04-01",
        "2016-04-04", "2016-04-05", "2016-04-06", "2016-04-07",
        "2016-04-08", "2016-04-11", "2016-04-12", "2016-04-13",
        "2016-04-14", "2016-04-15", "2016-04-18", "2016-04-19",
        "2016-04-20", "2016-04-21", "2016-04-22", "2016-04-25",
        "2016-04-26", "2016-04-27", "2016-04-28", "2016-04-29",
        "2016-05-02", "2016-05-03", "2016-05-04", "2016-05-05",
        "2016-05-06", "2016-05-09", "2016-05-10", "2016-05-11",
        "2016-05-12", "2016-05-13", "2016-05-16", "2016-05-17",
        "2016-05-18", "2016-05-19", "2016-05-20", "2016-05-23",
        "2016-05-24", "2016-05-25", "2016-05-26", "2016-05-27",
        "2016-05-31", "2016-06-01", "2016-06-02", "2016-06-03",
        "2016-06-06", "2016-06-07", "2016-06-08", "2016-06-09",
        "2016-06-10", "2016-06-13", "2016-06-14", "2016-06-15",
        "2016-06-16", "2016-06-17", "2016-06-20", "2016-06-21",
        "2016-06-22",
    )

/** One `[open, close, lowest, highest]` row per day of `candlestick-touch`. */
internal val TOUCH_CANDLES =
    arrayOf(
        doubleArrayOf(17512.58, 17633.11, 17434.27, 17642.81),
        doubleArrayOf(17652.36, 17716.66, 17652.36, 17790.11),
        doubleArrayOf(17716.05, 17685.09, 17669.72, 17755.7),
        doubleArrayOf(17661.74, 17792.75, 17568.02, 17811.48),
        doubleArrayOf(17799.39, 17737.0, 17710.67, 17806.38),
        doubleArrayOf(17718.03, 17603.32, 17579.56, 17718.03),
        doubleArrayOf(17605.45, 17716.05, 17542.54, 17723.55),
        doubleArrayOf(17687.28, 17541.96, 17484.23, 17687.28),
        doubleArrayOf(17555.39, 17576.96, 17528.16, 17694.51),
        doubleArrayOf(17586.48, 17556.41, 17555.9, 17731.63),
        doubleArrayOf(17571.34, 17721.25, 17553.57, 17744.43),
        doubleArrayOf(17741.66, 17908.28, 17741.66, 17918.35),
        doubleArrayOf(17912.25, 17926.43, 17885.44, 17962.14),
        doubleArrayOf(17925.95, 17897.46, 17867.41, 17937.65),
        doubleArrayOf(17890.2, 18004.16, 17848.22, 18009.53),
        doubleArrayOf(18012.1, 18053.6, 17984.43, 18103.46),
        doubleArrayOf(18059.49, 18096.27, 18031.21, 18167.63),
        doubleArrayOf(18092.84, 17982.52, 17963.89, 18107.29),
        doubleArrayOf(17985.05, 18003.75, 17909.89, 18026.85),
        doubleArrayOf(17990.94, 17977.24, 17855.55, 17990.94),
        doubleArrayOf(17987.38, 17990.32, 17934.17, 18043.77),
        doubleArrayOf(17996.14, 18041.55, 17920.26, 18084.66),
        doubleArrayOf(18023.88, 17830.76, 17796.55, 18035.73),
        doubleArrayOf(17813.09, 17773.64, 17651.98, 17814.83),
        doubleArrayOf(17783.78, 17891.16, 17773.71, 17912.35),
        doubleArrayOf(17870.75, 17750.91, 17670.88, 17870.75),
        doubleArrayOf(17735.02, 17651.26, 17609.01, 17738.06),
        doubleArrayOf(17664.48, 17660.71, 17615.82, 17736.11),
        doubleArrayOf(17650.3, 17740.63, 17580.38, 17744.54),
        doubleArrayOf(17743.85, 17705.91, 17668.38, 17783.16),
        doubleArrayOf(17726.66, 17928.35, 17726.66, 17934.61),
        doubleArrayOf(17919.03, 17711.12, 17711.05, 17919.03),
        doubleArrayOf(17711.12, 17720.5, 17625.38, 17798.19),
        doubleArrayOf(17711.12, 17535.32, 17512.48, 17734.74),
        doubleArrayOf(17531.76, 17710.71, 17531.76, 17755.8),
        doubleArrayOf(17701.46, 17529.98, 17469.92, 17701.46),
        doubleArrayOf(17501.28, 17526.62, 17418.21, 17636.22),
        doubleArrayOf(17514.16, 17435.4, 17331.07, 17514.16),
        doubleArrayOf(17437.32, 17500.94, 17437.32, 17571.75),
        doubleArrayOf(17507.04, 17492.93, 17480.05, 17550.7),
        doubleArrayOf(17525.19, 17706.05, 17525.19, 17742.59),
        doubleArrayOf(17735.09, 17851.51, 17735.09, 17891.71),
        doubleArrayOf(17859.52, 17828.29, 17803.82, 17888.66),
        doubleArrayOf(17826.85, 17873.22, 17824.73, 17873.22),
        doubleArrayOf(17891.5, 17787.2, 17724.03, 17899.24),
        doubleArrayOf(17754.55, 17789.67, 17664.79, 17809.18),
        doubleArrayOf(17789.05, 17838.56, 17703.55, 17838.56),
        doubleArrayOf(17799.8, 17807.06, 17689.68, 17833.17),
        doubleArrayOf(17825.69, 17920.33, 17822.81, 17949.68),
        doubleArrayOf(17936.22, 17938.28, 17936.22, 18003.23),
        doubleArrayOf(17931.91, 18005.05, 17931.91, 18016.0),
        doubleArrayOf(17969.98, 17985.19, 17915.88, 18005.22),
        doubleArrayOf(17938.82, 17865.34, 17812.34, 17938.82),
        doubleArrayOf(17830.5, 17732.48, 17731.35, 17893.28),
        doubleArrayOf(17710.77, 17674.82, 17595.79, 17733.92),
        doubleArrayOf(17703.65, 17640.17, 17629.01, 17762.96),
        doubleArrayOf(17602.23, 17733.1, 17471.29, 17754.91),
        doubleArrayOf(17733.44, 17675.16, 17602.78, 17733.44),
        doubleArrayOf(17736.87, 17804.87, 17736.87, 17946.36),
        doubleArrayOf(17827.33, 17829.73, 17799.8, 17877.84),
        doubleArrayOf(17832.67, 17780.83, 17770.36, 17920.16),
    )

/** Volume of each day of `candlestick-touch`, the lower grid of the page. */
internal val TOUCH_VOLUMES =
    arrayOf(
        8616.0, 7933.0, 1026.0, 10489.0, 8523.0, 11523.0,
        9941.0, 9012.0, 7999.0, 1071.0, 8102.0, 9171.0,
        8451.0, 11816.0, 8939.0, 8982.0, 10021.0, 10272.0,
        13412.0, 8377.0, 9257.0, 10909.0, 10092.0, 13667.0,
        801.0, 9706.0, 9502.0, 8153.0, 8002.0, 8559.0,
        7579.0, 8739.0, 8856.0, 8664.0, 8844.0, 10326.0,
        7912.0, 9553.0, 11199.0, 8779.0, 8648.0, 7918.0,
        6894.0, 7319.0, 14739.0, 7853.0, 7556.0, 8227.0,
        7187.0, 7875.0, 7126.0, 6969.0, 9054.0, 10169.0,
        9374.0, 9413.0, 9195.0, 24868.0, 9938.0, 8513.0,
        8944.0,
    )

/** The rows of `candlestick-touch`, zipped once from the three upstream columns. */
internal val TOUCH_ROWS: List<CandleRow> =
    TOUCH_CANDLES.mapIndexed { index, day ->
        CandleRow(TOUCH_DATES[index], day[0], day[1], day[2], day[3], TOUCH_VOLUMES[index])
    }

/** The eleven-colour palette of `candlestick-touch`. */
internal val TOUCH_COLORS =
    arrayOf(
        "#c23531", "#2f4554", "#61a0a8", "#d48265", "#91c7ae", "#749f83", "#ca8622", "#bda29a",
        "#6e7074", "#546570", "#c4ccd3",
    )

/** Slider handle of `candlestick-touch`, copied verbatim from upstream. */
internal const val TOUCH_HANDLE_ICON =
    "path://M10.7,11.9H9.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4h1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z"

/** Handle icon of the slider of `candlestick-sh-2015`, copied verbatim from upstream. */
internal const val HANDLE_2015_ICON =
    "path://M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z"

// ---------------------------------------------------------------------------------------------
// Shanghai and large-scale candle tables
// ---------------------------------------------------------------------------------------------

/** The first half of 2013, the span of `candlestick-sh`. */
internal val SHANGHAI_2013: List<CandleRow> by lazy {
    candleWalk(epochDay(2013, 1, 24), 90, 2240.0, 0.7, 30.0, 20130124L)
}

/** One year of weekdays of 2015, the span of `candlestick-sh-2015`. */
internal val SHANGHAI_2015: List<CandleRow> by lazy {
    candleWalk(epochDay(2015, 1, 5), 244, 3250.0, 1.6, 42.0, 20150105L)
}

/** Candles of `candlestick-large`; the original plots two hundred thousand minutes. */
internal const val LARGE_CANDLE_COUNT = 300

/** The candles of `candlestick-large`. */
internal val LARGE_CANDLES: List<CandleRow> by lazy {
    candleWalk(epochDay(2011, 1, 3), LARGE_CANDLE_COUNT, 3200.0, 0.4, 18.0, 20110101L)
}

// ---------------------------------------------------------------------------------------------
// Box plot summaries
// ---------------------------------------------------------------------------------------------

/** One five-number summary plus the outliers of a measurement group. */
internal class BoxSummary(
    val low: Double,
    val q1: Double,
    val median: Double,
    val q3: Double,
    val high: Double,
    val outliers: List<Double>,
)

/** The `quantile` of d3.js, the one ECharts uses in its boxplot transform. */
internal fun quantile(sorted: DoubleArray, p: Double): Double {
    val h = (sorted.size - 1) * p + 1.0
    val whole = kotlin.math.floor(h).toInt()
    val base = sorted[whole - 1]
    val fraction = h - whole
    return if (fraction != 0.0) base + fraction * (sorted[whole] - base) else base
}

/** The five-number summary of [values] with the 1.5 IQR whiskers of the boxplot transform. */
internal fun boxSummary(values: DoubleArray): BoxSummary {
    val sorted = values.sortedArray()
    val q1 = quantile(sorted, 0.25)
    val median = quantile(sorted, 0.5)
    val q3 = quantile(sorted, 0.75)
    val minimum = sorted.first()
    val maximum = sorted.last()
    val bound = 1.5 * (q3 - q1)
    val low = kotlin.math.max(minimum, q1 - bound)
    val high = kotlin.math.min(maximum, q3 + bound)
    return BoxSummary(low, q1, median, q3, high, sorted.filter { it < low || it > high })
}

/** The `[low, q1, median, q3, high]` rows of a box plot series. */
internal fun boxRows(summaries: List<BoxSummary>): List<DataValue> =
    rowsOf(
        *summaries.map { listOf<Number>(it.low, it.q1, it.median, it.q3, it.high) }.toTypedArray(),
    )

/** The outlier points of a box plot series; [swapped] puts the value on the x axis. */
internal fun outlierPoints(summaries: List<BoxSummary>, swapped: Boolean): List<DataValue> =
    summaries.flatMapIndexed { index, summary ->
        summary.outliers.map { value -> if (swapped) point(value, index) else point(index, value) }
    }

/** The 5 x 20 measurements of the Michelson-Morley experiment. */
internal val MICHELSON =
    arrayOf(
        doubleArrayOf(
            850.0, 740.0, 900.0, 1070.0, 930.0, 850.0, 950.0, 980.0, 980.0, 880.0,
            1000.0, 980.0, 930.0, 650.0, 760.0, 810.0, 1000.0, 1000.0, 960.0, 960.0,
        ),
        doubleArrayOf(
            960.0, 940.0, 960.0, 940.0, 880.0, 800.0, 850.0, 880.0, 900.0, 840.0,
            830.0, 790.0, 810.0, 880.0, 880.0, 830.0, 800.0, 790.0, 760.0, 800.0,
        ),
        doubleArrayOf(
            880.0, 880.0, 880.0, 860.0, 720.0, 720.0, 620.0, 860.0, 970.0, 950.0,
            880.0, 910.0, 850.0, 870.0, 840.0, 840.0, 850.0, 840.0, 840.0, 840.0,
        ),
        doubleArrayOf(
            890.0, 810.0, 810.0, 820.0, 800.0, 770.0, 760.0, 740.0, 750.0, 760.0,
            910.0, 920.0, 890.0, 860.0, 880.0, 720.0, 840.0, 850.0, 850.0, 780.0,
        ),
        doubleArrayOf(
            890.0, 840.0, 780.0, 810.0, 760.0, 810.0, 790.0, 810.0, 820.0, 850.0,
            870.0, 870.0, 810.0, 740.0, 810.0, 940.0, 950.0, 800.0, 810.0, 870.0,
        ),
    )

/** The five boxes of the Michelson-Morley pages. */
internal val MICHELSON_SUMMARIES: List<BoxSummary> by lazy { MICHELSON.map { boxSummary(it) } }

/** Groups of one table of `boxplot-multi`, one box per group. */
internal const val MULTI_GROUPS = 18

/** Measurements behind one box of `boxplot-multi`. */
internal const val MULTI_MEASUREMENTS = 100

/**
 * The three 18 x 100 tables of `boxplot-multi`.
 *
 * Upstream feeds them through the `boxplot` transform, which cannot be written as JSON, so the
 * tables are generated here exactly like the original `Lcg` generator.
 */
internal fun multiTables(): List<Array<DoubleArray>> {
    var state = 20261027L

    fun next(): Double {
        state = (state * 6364136223846793005L + 1442695040888963407L) and Long.MAX_VALUE
        return (state % 100000L).toDouble() / 100000.0 * 200.0
    }

    return (0 until 3).map { Array(MULTI_GROUPS) { DoubleArray(MULTI_MEASUREMENTS) { next() } } }
}

/** The boxes of the three tables of `boxplot-multi`. */
internal val MULTI_SUMMARIES: List<List<BoxSummary>> by lazy {
    multiTables().map { table -> table.map { boxSummary(it) } }
}

/** The two captions of the Michelson-Morley pages. */
internal fun EChartsOption.michelsonTitles(normalWeight: Boolean) {
    title {
        text = "Michelson-Morley Experiment"
        left = str("center")
    }
    title {
        text = "upper: Q3 + 1.5 * IQR \nlower: Q1 - 1.5 * IQR"
        borderColor = "#999".asColor()
        borderWidth = 1.0
        textStyle {
            if (normalWeight) fontWeight = str("normal")
            fontSize = 14.0
            lineHeight = 20.0
        }
        left = str("10%")
        top = str("90%")
    }
}

// ---------------------------------------------------------------------------------------------
// Punch card and noise field of the heatmaps
// ---------------------------------------------------------------------------------------------

/** The 24 hours of the day, the category axis of the punch card. */
internal val PUNCH_HOURS =
    arrayOf(
        "12a", "1a", "2a", "3a", "4a", "5a", "6a", "7a", "8a", "9a", "10a", "11a",
        "12p", "1p", "2p", "3p", "4p", "5p", "6p", "7p", "8p", "9p", "10p", "11p",
    )

/** The seven weekdays, Saturday first, the other category axis of the punch card. */
internal val PUNCH_DAYS =
    arrayOf("Saturday", "Friday", "Thursday", "Wednesday", "Tuesday", "Monday", "Sunday")

/** The 168 commit counts of the punch card: `rows[day][hour]`. */
internal val PUNCH_VALUES: List<List<Int>> =
    listOf(
        listOf(
            5, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 4, 1, 1, 3, 4, 6, 4, 4, 3, 3, 2, 5,
        ),
        listOf(
            7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 2, 2, 6, 9, 11, 6, 7, 8, 12, 5, 5, 7, 2,
        ),
        listOf(
            1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 3, 2, 1, 9, 8, 10, 6, 5, 5, 5, 7, 4, 2, 4,
        ),
        listOf(
            7, 3, 0, 0, 0, 0, 0, 0, 1, 0, 5, 4, 7, 14, 13, 12, 9, 5, 5, 10, 6, 4, 4, 1,
        ),
        listOf(
            1, 3, 0, 0, 0, 1, 0, 0, 0, 2, 4, 4, 2, 4, 4, 14, 12, 1, 8, 5, 3, 7, 3, 0,
        ),
        listOf(
            2, 1, 0, 3, 0, 0, 0, 0, 2, 0, 4, 1, 5, 10, 5, 7, 11, 6, 0, 5, 3, 4, 2, 0,
        ),
        listOf(
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 2, 1, 3, 4, 0, 0, 0, 0, 1, 2, 2, 6,
        ),
    )

/** The `[hour, day, commits]` cells of the punch card; an empty hour is the `"-"` it paints. */
internal fun punchCardCells(): List<DataValue> =
    PUNCH_VALUES.flatMapIndexed { day, row ->
        row.mapIndexed { hour, commits ->
            arrayValue(
                valueOf(hour),
                valueOf(day),
                if (commits > 0) valueOf(commits) else textValue("-"),
            )
        }
    }

/** Columns of the generated heatmap field. */
internal const val FIELD_COLUMNS = 25

/** Rows of the generated heatmap field; upstream samples 201 x 101 cells. */
internal const val FIELD_ROWS = 15

/** The colour ramp of both large heatmaps. */
internal val FIELD_COLORS =
    arrayOf(
        "#313695", "#4575b4", "#74add1", "#abd9e9", "#e0f3f8", "#ffffbf",
        "#fee090", "#fdae61", "#f46d43", "#d73027", "#a50026",
    )

/** The colour ramp of both large heatmaps, as the model's colour list. */
internal fun fieldColors(): List<EChartsColor> = FIELD_COLORS.map { it.asColor() }

/** The `0..count-1` category values of one field axis. */
internal fun fieldCategories(count: Int): List<DataValue> = (0 until count).map { valueOf(it) }

/** The smooth `0..1` field both large heatmaps paint; a sine mix instead of the perlin noise. */
internal fun fieldCells(): List<DataValue> =
    (0 until FIELD_COLUMNS).flatMap { column ->
        (0 until FIELD_ROWS).map { row ->
            val value =
                0.5 +
                    0.32 * kotlin.math.sin(column / 3.4) * kotlin.math.cos(row / 2.2) +
                    0.18 * kotlin.math.sin((column + row) / 5.7)
            arrayValue(valueOf(column), valueOf(row), valueOf(round3(value)))
        }
    }

/** The tooltip shared by the box plot pages: an item tooltip with a shadow pointer. */
internal fun EChartsOption.boxplotTooltip() {
    tooltip {
        trigger = TooltipTrigger.Item
        axisPointer = AxisPointerOption().apply { type = "shadow" }
    }
}
