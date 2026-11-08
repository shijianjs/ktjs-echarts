package demo.examples.shared

import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import js.objects.Record
import kotlin.js.JsAny
import kotlin.js.js
import kotlin.js.unsafeCast
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.candlestick.candle
import demo.examples.chord.column
import demo.examples.funnel.labels
import demo.examples.matrix.cents
import demo.examples.scatter.point
import demo.examples.themeriver.days

/** One minute in milliseconds. */
internal const val MINUTE = 60_000.0

/** One day in milliseconds. */
internal const val ONE_DAY = 24 * 60 * MINUTE

/** One break per closed night: the value the first page gives the `gap`. */
internal const val NIGHT_GAP = "1%"

/** Smallest window the data zoom of both pages allows, one hour. */
internal const val ZOOM_MIN_SPAN = 3600 * 1000.0

/** Open time of the market, in hours and minutes UTC. */
internal const val MARKET_OPEN_HOUR = 9

/** See [MARKET_OPEN_HOUR]. */
internal const val MARKET_OPEN_MINUTE = 30

/** Close time of the market, in hours UTC. */
internal const val MARKET_CLOSE_HOUR = 16

/** Value the random walk starts on, the upstream `val = 1669`. */
internal const val WALK_START = 1669.0

/** First day of the four-day page, as `[year, month, day]`; the month is zero based like `Date`. */
internal val MULTI_DAY_START = intArrayOf(2024, 3, 9)

/** Last day of the four-day page, as `[year, month, day]`. */
internal val MULTI_DAY_END = intArrayOf(2024, 3, 12)

/** The single day of the second page, as `[year, month, day]`. */
internal val SINGLE_DAY = intArrayOf(2024, 3, 9)

/** Lunch break of the second page: start and end, in hours UTC. */
internal const val LUNCH_START_HOUR = 11

/** See [LUNCH_START_HOUR]. */
internal const val LUNCH_START_MINUTE = 30

/** See [LUNCH_START_HOUR]. */
internal const val LUNCH_END_HOUR = 13

/** Result of [walkMultiDay]: the minute series and the breaks between the days. */
internal class IntradayData(val points: Array<LineDataItem?>, val breaks: Array<AxisBreakOption>)

/** `Date.UTC(year, month, day, hour, minute)` of one wall-clock time. */
internal fun utc(year: Int, month: Int, day: Int, hour: Int = 0, minute: Int = 0): Double =
    js("(y, mo, d, h, mi) => Date.UTC(y, mo, d, h, mi)")(year, month, day, hour, minute) as Double

/** `Date.UTC(...)` of [MONTH_START]-style triples; see [utc]. */
internal fun utcOf(date: IntArray, hour: Int = 0, minute: Int = 0): Double =
    utc(date[0], date[1], date[2], hour, minute)

/**
 * The four-day random walk of `intraday-breaks-1`.
 *
 * Every step is `(random - 0.5 * sin(value / 1000)) * 20 / 100`, rounded to whole cents; the first
 * step of a day is ten times larger, which is the upstream `valBreak` flag. The last point of a day
 * is `NaN` so the line breaks, and the night between the close and the next open becomes an axis
 * break.
 */
internal fun walkMultiDay(random: Lcg): IntradayData {
    val points = mutableListOf<LineDataItem?>()
    val breaks = mutableListOf<AxisBreakOption>()
    val lastDay = utcOf(MULTI_DAY_END, 23, 59)
    var day = utcOf(MULTI_DAY_START)
    var afterBreak = false
    var value = WALK_START
    while (day <= lastDay) {
        val open = day + (MARKET_OPEN_HOUR * 60 + MARKET_OPEN_MINUTE) * MINUTE
        val close = day + MARKET_CLOSE_HOUR * 60 * MINUTE
        var time = open
        while (time <= close) {
            val scale = if (afterBreak) 10.0 else 1.0
            val delta =
                kotlin.math.floor((random.next() - 0.5 * kotlin.math.sin(value / 1000)) * 20 * scale) / 100.0
            value = kotlin.math.round((value + delta) * 100) / 100.0
            points += LineDataItem(doubleRow(time, value))
            afterBreak = false
            time += MINUTE
        }
        // The night: a NaN point closes the segment and an axis break carries the gap.
        points += LineDataItem(row(close + MINUTE, Double.NaN))
        val nextOpen = day + ONE_DAY + (MARKET_OPEN_HOUR * 60 + MARKET_OPEN_MINUTE) * MINUTE
        breaks +=
            unsafeJso<AxisBreakOption> {
                start = OptionDataValue(close)
                end = OptionDataValue(nextOpen)
                gap = StringOrDouble(NIGHT_GAP)
            }
        afterBreak = true
        day += ONE_DAY
    }
    return IntradayData(points.toTypedArray(), breaks.toTypedArray())
}

/** The single-day walk of `intraday-breaks-2`, skipping the lunch break. */
internal fun walkSingleDay(random: Lcg): IntradayData {
    val points = mutableListOf<LineDataItem?>()
    val dayBreakStart = utcOf(SINGLE_DAY, LUNCH_START_HOUR, LUNCH_START_MINUTE)
    val dayBreakEnd = utcOf(SINGLE_DAY, LUNCH_END_HOUR)
    val end = utcOf(SINGLE_DAY, 15)
    var time = utcOf(SINGLE_DAY, MARKET_OPEN_HOUR, MARKET_OPEN_MINUTE)
    var value = WALK_START
    while (time <= end) {
        if (time <= dayBreakStart || time >= dayBreakEnd) {
            val delta =
                kotlin.math.floor((random.next() - 0.5 * kotlin.math.sin(value / 1000)) * 20 * 100) / 100.0
            value = kotlin.math.round((value + delta) * 100) / 100.0
            points += LineDataItem(doubleRow(time, value))
        }
        time += MINUTE
    }
    return IntradayData(
        points.toTypedArray(),
        arrayOf(
            unsafeJso<AxisBreakOption> {
                start = OptionDataValue(dayBreakStart)
                this.end = OptionDataValue(dayBreakEnd)
                gap = StringOrDouble(0.0)
            },
        ),
    )
}

/** The area style of both break bands: invisible, only the gap is drawn. */
internal fun invisibleBreakArea(): AxisBreakAreaOption =
    unsafeJso<AxisBreakAreaOption> {
        expandOnClick = false
        zigzagAmplitude = 0.0
        zigzagZ = 200.0
        itemStyle = unsafeJso<ItemStyleOption> { borderColor = ZRColor("none"); opacity = 0.0 }
    }

/** The `rich` block of the axis label of `intraday-breaks-1`: one grey `weak` tag. */
internal fun weakRichText(): Record<String, TextStyleOption> =
    unsafeJso<Record<String, TextStyleOption>> {
        @Suppress("UNUSED_EXPRESSION")
        this
    }.also { block -> js("(block) => { block['weak'] = { color: '#999' }; }")(block) }

/** The only x axis of the second page, as the index list both data zooms target. */
internal fun allAxisIndices(): IndexOrIndexArray = IndexOrIndexArray(arrayOf(0.0))

/**
 * The axis label of `intraday-breaks-1`: on a break it also writes the day, through the `weak` rich
 * tag defined by the page.
 *
 * The axis formatter receives a third argument (the break info) that the wrapper's
 * [LabelFormatterCallback] does not model, so the extra argument is read in JavaScript and the
 * formatting itself goes through [timeFormat].
 */
internal fun axisFormatterWithDay(value: JsAny, extra: JsAny?): String {
    val timestamp = numeric(value)
    val onBreak = js("(extra) => !!(extra && extra.break)")(extra) as Boolean
    return if (onBreak) {
        timeFormat(timestamp, "{HH}:{mm}\n{weak|{dd}d}")
    } else {
        timeFormat(timestamp, "{HH}:{mm}")
    }
}

/**
 * The axis label of `intraday-breaks-2`: a single label spanning the lunch break, and nothing on the
 * far side of it.
 */
internal fun axisFormatterWithBreak(value: JsAny, extra: JsAny?): String {
    val timestamp = numeric(value)
    val info = js("(extra) => (extra && extra.break) || null")(extra)
    if (info == null || js("(i) => i === null")(info) as Boolean) {
        return timeFormat(timestamp, "{HH}:{mm}")
    }
    val type = js("(i) => i.type")(info) as String
    if (type != "start") return ""
    val start = js("(i) => i.start")(info)
    val end = js("(i) => i.end")(info)
    return timeFormat(numeric(start), "{HH}:{mm}") + "/" + timeFormat(numeric(end), "{HH}:{mm}")
}

/**
 * Wraps a three-argument axis formatter as the one-argument [LabelFormatterCallback].
 *
 * ECharts passes `(value, index, extra)`; the wrapper keeps the single-argument shape, so the
 * callback is widened in JavaScript and the Kotlin function receives the value and the extra info.
 */
internal fun axisLabel(formatter: (value: JsAny, extra: JsAny?) -> String): LabelFormatterCallback =
    js("(fn) => (value, index, extra) => fn(value, extra)")(formatter).unsafeCast<LabelFormatterCallback>()

/** `ECharts.time.format(value, template, true)`: the pages format their labels in UTC. */
internal fun timeFormat(value: Double, template: String): String =
    ECharts.time.format(value.unsafeCast<JsAny>(), template, true)

/** Points of `area-simple`, the upstream `20000`. */
internal const val AREA_SIMPLE_POINTS = 20000

/** First day of `area-simple`: 1968-10-03 in the original. */
internal const val AREA_SIMPLE_START = "1968-10-03"

/** Candles of `candlestick-large`, the upstream `2e5`. */
internal const val LARGE_CANDLE_COUNT = 200_000

/** First timestamp of `candlestick-large`, one candle per minute from 2011-01-01. */
internal const val LARGE_CANDLE_START = 1_293_840_000_000.0

/** One candle of `candlestick-large`, as `[open, highest, lowest, close, volume, sign]`. */
internal const val LARGE_CANDLE_DAY_RANGE = 12.0

/** Colors of the rising candles of `candlestick-large`. */
internal const val UP_COLOR = "#ec0000"

/** Border color of the rising candles of `candlestick-large`. */
internal const val UP_BORDER_COLOR = "#8A0000"

/** Color of the falling candles of `candlestick-large`. */
internal const val DOWN_COLOR = "#00da3c"

/** Border color of the falling candles of `candlestick-large`. */
internal const val DOWN_BORDER_COLOR = "#008F28"

/** The dates of `area-simple`, one per day from 1968-10-03, as `yyyy/M/d` strings. */
internal fun areaSimpleDates(): Array<String> {
    val start = js("(iso) => new Date(iso).getTime()")(AREA_SIMPLE_START) as Double
    return Array(AREA_SIMPLE_POINTS) { index ->
        val date = kotlin.js.Date(start + index * ONE_DAY)
        "${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()}"
    }
}

/** The random walk of `area-simple`: `round((random - 0.5) * 20 + previous)`. */
internal fun areaSimpleValues(): DoubleArray {
    val random = Lcg(19681003)
    val values = DoubleArray(AREA_SIMPLE_POINTS)
    values[0] = random.next() * 300
    for (index in 1 until AREA_SIMPLE_POINTS) {
        values[index] = kotlin.math.round((random.next() - 0.5) * 20 + values[index - 1])
    }
    return values
}

/** One row of the `candlestick-large` dataset. */
internal class CandleRow(
    val label: String,
    val open: Double,
    val highest: Double,
    val lowest: Double,
    val close: Double,
    val volume: Double,
    val sign: Double,
)

/**
 * The OHLC table of `candlestick-large`.
 *
 * The original draws four box values per candle, sorts them and picks the open and the close out of
 * the sorted list before writing `[open, highest, lowest, close]`; the sign column is `1` when the
 * candle closes at or above its open (the previous close decides a tie) and `-1` otherwise, which is
 * what the hidden visual map of the page reads.
 */
internal fun largeCandles(): Array<CandleRow> {
    val random = Lcg(20110101)
    val rows = arrayOfNulls<CandleRow>(LARGE_CANDLE_COUNT)
    var base = random.next() * 12000
    var previousClose = 0.0
    val label =
        js(
            "(value) => { const d = new Date(value);" +
                " const p = (n) => String(n).padStart(2, '0');" +
                " return d.getFullYear() + '-' + p(d.getMonth() + 1) + '-' + p(d.getDate()) + '\\n' +" +
                " p(d.getHours()) + ':' + p(d.getMinutes()) + ':' + p(d.getSeconds()); }",
        )
    for (index in 0 until LARGE_CANDLE_COUNT) {
        base += random.next() * 20 - 10
        val box = DoubleArray(4) { (random.next() - 0.5) * LARGE_CANDLE_DAY_RANGE + base }
        box.sort()
        val openIndex = kotlin.math.round(random.next() * 3).toInt()
        var closeIndex = kotlin.math.round(random.next() * 2).toInt()
        if (closeIndex == openIndex) closeIndex++
        val open = box[openIndex]
        val close = box[closeIndex]
        val sign =
            when {
                open > close -> -1.0
                open < close -> 1.0
                else -> if (index > 0 && previousClose <= close) 1.0 else if (index > 0) -1.0 else 1.0
            }
        previousClose = close
        rows[index] =
            CandleRow(
                label(LARGE_CANDLE_START + index * 60_000) as String,
                kotlin.math.round(open * 100) / 100,
                kotlin.math.round(box[3] * 100) / 100,
                kotlin.math.round(box[0] * 100) / 100,
                kotlin.math.round(close * 100) / 100,
                kotlin.math.round(box[3] * (1000 + random.next() * 500)),
                sign,
            )
    }
    @Suppress("UNCHECKED_CAST")
    return rows as Array<CandleRow>
}

/** `echarts.format.addCommas` of [value], the title of `candlestick-large`. */
internal fun thousands(value: Int): String =
    ECharts.format.addCommas(StringOrDouble(value.toDouble()))

/** The candles of `candlestick-large`, generated once when the page mounts. */
internal val LARGE_CANDLES: Array<CandleRow> by lazy { largeCandles() }

/** The rows of [largeCandles] as the `dataset.source` the page needs. */
internal fun largeCandleSource(): DatasetSource =
    datasetArrayRows(
        Array(LARGE_CANDLE_COUNT) { index ->
            val row = LARGE_CANDLES[index]
            arrayOf(
                OptionDataValue(row.label),
                OptionDataValue(row.open),
                OptionDataValue(row.highest),
                OptionDataValue(row.lowest),
                OptionDataValue(row.close),
                OptionDataValue(row.volume),
                OptionDataValue(row.sign),
            )
        },
    )
