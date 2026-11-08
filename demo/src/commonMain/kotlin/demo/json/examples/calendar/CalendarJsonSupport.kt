package demo.json.examples.calendar

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlin.math.floor
import kotlin.math.round
import demo.json.examples.shared.stroke
import demo.json.namedValues
import demo.json.nodes
import demo.json.objectValue
import demo.json.points
import demo.json.timedValue

/**
 * `getVirtualData` of the official examples: one `[date, value]` point per day of the range.
 *
 * The value comes from the fixed linear congruential sequence of the Kotlin/JS port, so a page looks
 * the same on every visit and both code sources produce the same chart.
 */
internal fun dailyValues(from: String, to: String, max: Double, seed: Long): List<Pair<String, Double>> {
    var state = seed
    fun next(): Double {
        state = (state * 6364136223846793005L + 1442695040888963407L) and Long.MAX_VALUE
        return (state % 100000L).toDouble() / 100000.0
    }

    val fromParts = from.split('-').map { it.toInt() }
    val toParts = to.split('-').map { it.toInt() }
    var year = fromParts[0]
    var month = fromParts[1]
    var day = fromParts[2]
    val end = dayNumber(toParts[0], toParts[1], toParts[2])
    val days = mutableListOf<Pair<String, Double>>()
    while (dayNumber(year, month, day) < end) {
        days += dateLabel(year, month, day) to floor(next() * max)
        day++
        if (day > daysInMonth(year, month)) {
            day = 1
            month++
            if (month > 12) {
                month = 1
                year++
            }
        }
    }
    return days
}

/** A comparable `yyyyMMdd` number of a day. */
internal fun dayNumber(year: Int, month: Int, day: Int): Int = year * 10000 + month * 100 + day

/** The `yyyy-MM-dd` label of a day. */
internal fun dateLabel(year: Int, month: Int, day: Int): String =
    "${year.toString().padStart(4, '0')}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"

/** Days per month of [year], the leap rule of the Gregorian calendar. */
internal fun daysInMonth(year: Int, month: Int): Int =
    when (month) {
        2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
        4, 6, 9, 11 -> 30
        else -> 31
    }

/** The days of [year] as the `[date, value]` points of a calendar heatmap. */
internal fun yearValues(year: String, max: Double, seed: Long): List<DataValue> =
    dailyValues("$year-01-01", "${year.toInt() + 1}-01-01", max, seed)
        .map { (date, value) -> timedValue(date, value) }

/** One calendar of the examples whose range is a year or a month, for example `"2017"` / `"2017-02"`. */
internal fun EChartsOption.calendarIn(range: String, block: CalendarOption.() -> Unit = {}) {
    calendar {
        this.range = JsonPrimitive(range)
        block()
    }
}

/** One calendar with an explicit `[from, to]` range. */
internal fun EChartsOption.calendarBetween(from: String, to: String, block: CalendarOption.() -> Unit = {}) {
    calendar {
        this.range =
            buildJsonArray {
                add(JsonPrimitive(from))
                add(JsonPrimitive(to))
            }
        block()
    }
}

/** `cellSize: [width, height]`. */
internal fun cellSize(width: NumberOrString, height: NumberOrString): NumberOrStringOrNumbers =
    NumberOrStringOrNumbers.of(width, height)

/** `cellSize: <size>`: the square cells of `calendar-graph` and `calendar-charts`. */
internal fun cellSize(size: Number): NumberOrStringOrNumbers = NumberOrStringOrNumbers.of(size)

/** The heatmap series of the calendar examples, all of them on the calendar coordinate system. */
internal fun SeriesBuilder.calendarHeatmap(points: List<DataValue>, calendarIndex: Int? = null) {
    heatmap {
        coordinateSystem = "calendar"
        data = points
        if (calendarIndex != null) this.calendarIndex = calendarIndex
    }
}

/** The discrete legend of `calendar-heatmap` and `calendar-graph`. */
internal fun EChartsOption.piecewiseVisualMap(block: VisualMapOption.() -> Unit) {
    visualMap {
        type = VisualMapType.Piecewise
        orient = Orient.Horizontal
        block()
    }
}

/** The `[date, value]` points of the graph series of the calendar examples. */
internal fun graphNodes(days: List<Pair<String, Double>>): List<DataValue> =
    days.map { (date, value) -> timedValue(date, value) }

/**
 * The chain that links the `[date, value]` points of `calendar-graph` / `calendar-charts`.
 *
 * The endpoints are the numeric data indices of the nodes, as upstream writes them; a node of this
 * graph has neither an id nor a name.
 */
internal fun graphLinks(count: Int): List<DataValue> =
    List(count - 1) { index ->
        objectValue(
            "source" to JsonPrimitive(index),
            "target" to JsonPrimitive(index + 1),
        )
    }

/** Encodes a visual map range into the free-form `controller` object. */
internal fun visualRangeJson(range: VisualMapRange): JsonElement =
    EChartsJson.format.encodeToJsonElement(VisualMapRange.serializer(), range)

/** One half-year calendar of `calendar-effectscatter`; [offsetTop] places the second block. */
internal fun EChartsOption.halfYearCalendar(from: String, to: String, offsetTop: Double) {
    calendarBetween(from, to) {
        top = num(offsetTop)
        left = str("center")
        splitLine =
            CalendarSplitLineOption().apply {
                show = true
                lineStyle =
                    LineStyleOption().apply {
                        color = "#000".asColor()
                        width = 4.0
                        type = "solid"
                    }
            }
        yearLabel = CalendarLabelOption().apply { color = "#fff".asColor() }
        monthLabel = CalendarLabelOption().apply { color = "#aaa".asColor() }
        dayLabel = CalendarLabelOption().apply { color = "#aaa".asColor() }
        itemStyle =
            ItemStyleOption().apply {
                color = "#323c48".asColor()
                borderWidth = 1.0
                borderColor = "#111".asColor()
            }
    }
}

/** The plain scatter series of `calendar-effectscatter`. */
internal fun SeriesBuilder.calendarSteps(days: List<Pair<String, Double>>, calendarIndex: Int?) {
    scatter {
        name = "Steps"
        coordinateSystem = "calendar"
        if (calendarIndex != null) this.calendarIndex = calendarIndex
        symbolSize = NumberOrStringOrNumbers.of(20)
        itemStyle = ItemStyleOption().apply { color = "#ddb926".asColor() }
        data = days.map { (date, value) -> timedValue(date, value) }
    }
}

/** The ripple series of the twelve largest days of `calendar-effectscatter`. */
internal fun SeriesBuilder.calendarTopSteps(days: List<Pair<String, Double>>, calendarIndex: Int?) {
    effectScatter {
        name = "Top 12"
        coordinateSystem = "calendar"
        if (calendarIndex != null) this.calendarIndex = calendarIndex
        symbolSize = NumberOrStringOrNumbers.of(25)
        showEffectOn = "render"
        rippleEffect = RippleEffectOption().apply { brushType = "stroke" }
        itemStyle =
            ItemStyleOption().apply {
                color = "#f4e925".asColor()
                shadowBlur = 10.0
                shadowColor = "#333".asColor()
            }
        zlevel = 1.0
        data = days.map { (date, value) -> timedValue(date, value) }
    }
}

/** One day pie of `calendar-pie`: the three categories of that day. */
internal fun dayPie(index: Int, date: String): SeriesOption {
    var state = 20170201L + index
    fun next(): Double {
        state = (state * 6364136223846793005L + 1442695040888963407L) and Long.MAX_VALUE
        return (state % 100000L).toDouble() / 100000.0
    }
    return SeriesOption(SeriesType.Pie).apply {
        id = "pie-$index"
        center = listOf(str(date))
        radius = NumberOrStringOrNumbers.of(30)
        coordinateSystem = "calendar"
        label =
            LabelOption().apply {
                formatter = "{c}"
                position = "inside"
            }
        data =
            namedValues(
                "Work" to round(next() * 24),
                "Entertainment" to round(next() * 24),
                "Sleep" to round(next() * 24),
            )
    }
}
