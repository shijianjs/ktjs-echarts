package demo.json.examples

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.math.floor
import kotlin.math.round

/*
 * Calendar examples carried over from `calendar-*.ts` of apache/echarts-examples, matching the
 * Kotlin/JS port in `demo/src/jsMain/kotlin/demo/examples/Calendar.kt`.
 *
 * The pages keep the official ids, so the Kotlin/JS port and the JSON port of the same chart share
 * one route. `calendar-lunar` stays Kotlin-only: its 365-row table is an external JSON file and both
 * scatter layers exist only to run `label.formatter` callbacks.
 *
 * The day values come from [dailyValues], which copies the sequence constants of the Kotlin/JS port,
 * so both modes of a page draw the same data instead of two independent `Math.random()` runs.
 *
 * Compacted on purpose, because the model cannot express them:
 *
 * - `CalendarLabelOption` carries only `show` / `margin` / `position` plus the text style. The
 *   `nameMap` (`'cn'` and the explicit `["Sun", ...]` lists) and `firstDay: 1` of `calendar-graph`,
 *   `calendar-charts` and `calendar-pie` are therefore dropped and those pages fall back to the labels
 *   the runtime locale produces (day and month names, and which weekday a week starts on);
 * - the same field is missing for `calendar-effectscatter`'s `yearLabel.formatter: "{start}  1st"`,
 *   so both half-year blocks show the default year label;
 * - `label.formatter` callbacks have no JSON form: `calendar-pie`'s scatter layer keeps `label.show`
 *   at its "inside" position but prints the raw `[date, value]` pair instead of the day number;
 * - the `symbolSize` callbacks are replaced by fixed sizes — 20 for the plain scatter layers, 25 for
 *   the ripple layers — so `calendar-charts` and `calendar-effectscatter` draw dots of one size
 *   instead of sizing each dot from its value;
 * - `calendar-vertical`'s `tooltip.formatter` (which only re-prints the date and value already in the
 *   payload) is dropped, leaving the default tooltip.
 *
 * `calendar-effectscatter`'s `Top 12` series takes a sorted copy of the shared day list instead of
 * sorting it in place, as upstream does; the points are independent, so the picture is unchanged.
 */

private val calendarJson = JsonCategory("json/examples/CalendarJson.kt")

/**
 * `getVirtualData` of the official examples: one `[date, value]` point per day of the range.
 *
 * The value comes from the fixed linear congruential sequence of the Kotlin/JS port, so a page looks
 * the same on every visit and both code sources produce the same chart.
 */
private fun dailyValues(from: String, to: String, max: Double, seed: Long): List<Pair<String, Double>> {
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
private fun dayNumber(year: Int, month: Int, day: Int): Int = year * 10000 + month * 100 + day

/** The `yyyy-MM-dd` label of a day. */
private fun dateLabel(year: Int, month: Int, day: Int): String =
    "${year.toString().padStart(4, '0')}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"

/** Days per month of [year], the leap rule of the Gregorian calendar. */
private fun daysInMonth(year: Int, month: Int): Int =
    when (month) {
        2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
        4, 6, 9, 11 -> 30
        else -> 31
    }

/** The days of [year] as the `[date, value]` points of a calendar heatmap. */
private fun yearValues(year: String, max: Double, seed: Long): List<DataValue> =
    dailyValues("$year-01-01", "${year.toInt() + 1}-01-01", max, seed)
        .map { (date, value) -> timedValue(date, value) }

/** One calendar of the examples whose range is a year or a month, for example `"2017"` / `"2017-02"`. */
private fun EChartsOption.calendarIn(range: String, block: CalendarOption.() -> Unit = {}) {
    calendar {
        this.range = JsonPrimitive(range)
        block()
    }
}

/** One calendar with an explicit `[from, to]` range. */
private fun EChartsOption.calendarBetween(from: String, to: String, block: CalendarOption.() -> Unit = {}) {
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
private fun cellSize(width: NumberOrString, height: NumberOrString): NumberOrStringOrNumbers =
    NumberOrStringOrNumbers.of(width, height)

/** `cellSize: <size>`: the square cells of `calendar-graph` and `calendar-charts`. */
private fun cellSize(size: Number): NumberOrStringOrNumbers = NumberOrStringOrNumbers.of(size)

/** The heatmap series of the calendar examples, all of them on the calendar coordinate system. */
private fun SeriesBuilder.calendarHeatmap(points: List<DataValue>, calendarIndex: Int? = null) {
    heatmap {
        coordinateSystem = "calendar"
        data = points
        if (calendarIndex != null) this.calendarIndex = calendarIndex
    }
}

/** The discrete legend of `calendar-heatmap` and `calendar-graph`. */
private fun EChartsOption.piecewiseVisualMap(block: VisualMapOption.() -> Unit) {
    visualMap {
        type = VisualMapType.Piecewise
        orient = Orient.Horizontal
        block()
    }
}

/** The `[date, value]` points of the graph series of the calendar examples. */
private fun graphNodes(days: List<Pair<String, Double>>): List<DataValue> =
    days.map { (date, value) -> timedValue(date, value) }

/**
 * The chain that links the `[date, value]` points of `calendar-graph` / `calendar-charts`.
 *
 * The endpoints are the numeric data indices of the nodes, as upstream writes them; a node of this
 * graph has neither an id nor a name.
 */
private fun graphLinks(count: Int): List<DataValue> =
    List(count - 1) { index ->
        objectValue(
            "source" to JsonPrimitive(index),
            "target" to JsonPrimitive(index + 1),
        )
    }

/** Encodes a visual map range into the free-form `controller` object. */
private fun visualRangeJson(range: VisualMapRange): JsonElement =
    EChartsJson.format.encodeToJsonElement(VisualMapRange.serializer(), range)

/** One half-year calendar of `calendar-effectscatter`; [offsetTop] places the second block. */
private fun EChartsOption.halfYearCalendar(from: String, to: String, offsetTop: Double) {
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
private fun SeriesBuilder.calendarSteps(days: List<Pair<String, Double>>, calendarIndex: Int?) {
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
private fun SeriesBuilder.calendarTopSteps(days: List<Pair<String, Double>>, calendarIndex: Int?) {
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
private fun dayPie(index: Int, date: String): SeriesOption {
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

public val calendarJsonExamples: List<JsonExample> =
    listOf(
        calendarJson.example("calendar-simple") {
            visualMap {
                show = false
                min = 0.0
                max = 10000.0
            }
            calendarIn("2017")
            series {
                calendarHeatmap(yearValues("2017", 10000.0, 20170401L))
            }
        },
        calendarJson.example("calendar-heatmap") {
            title {
                top = num(30)
                left = str("center")
                text = "Daily Step Count"
            }
            tooltip { }
            piecewiseVisualMap {
                min = 0.0
                max = 10000.0
                left = str("center")
                top = num(65)
            }
            calendarIn("2016") {
                top = num(120)
                left = num(30)
                right = num(30)
                cellSize = cellSize(str("auto"), num(13))
                itemStyle = ItemStyleOption().apply { borderWidth = 0.5 }
                yearLabel = CalendarLabelOption().apply { show = false }
            }
            series {
                calendarHeatmap(yearValues("2016", 10000.0, 20160101L))
            }
        },
        calendarJson.example("calendar-vertical") {
            tooltip { position = JsonPrimitive("top") }
            visualMap {
                min = 0.0
                max = 1000.0
                calculable = true
                orient = Orient.Vertical
                left = str("670")
                top = str("center")
            }
            calendarIn("2015") { orient = Orient.Vertical }
            calendarIn("2016") {
                left = num(300)
                orient = Orient.Vertical
            }
            calendarIn("2017") {
                left = num(520)
                cellSize = cellSize(num(20), str("auto"))
                bottom = num(10)
                orient = Orient.Vertical
                dayLabel { margin = 5.0 }
            }
            series {
                calendarHeatmap(yearValues("2015", 1000.0, 20150101L), 0)
                calendarHeatmap(yearValues("2016", 1000.0, 20160101L), 1)
                calendarHeatmap(yearValues("2017", 1000.0, 20170101L), 2)
            }
        },
        calendarJson.example("calendar-horizontal") {
            tooltip { position = JsonPrimitive("top") }
            visualMap {
                min = 0.0
                max = 1000.0
                calculable = true
                orient = Orient.Horizontal
                left = str("center")
                top = str("top")
            }
            calendarIn("2017") {
                cellSize = cellSize(str("auto"), num(20))
            }
            calendarIn("2016") {
                top = num(260)
                cellSize = cellSize(str("auto"), num(20))
            }
            calendarIn("2015") {
                top = num(450)
                cellSize = cellSize(str("auto"), num(20))
                right = num(5)
            }
            series {
                calendarHeatmap(yearValues("2017", 1000.0, 20170101L), 0)
                calendarHeatmap(yearValues("2016", 1000.0, 20160201L), 1)
                calendarHeatmap(yearValues("2015", 1000.0, 20150301L), 2)
            }
        },
        calendarJson.example("calendar-graph") {
            // The seven days of the graph, and the chain that links them.
            val days =
                listOf(
                    "2017-02-01" to 260.0,
                    "2017-02-04" to 200.0,
                    "2017-02-09" to 279.0,
                    "2017-02-13" to 847.0,
                    "2017-02-18" to 241.0,
                    "2017-02-23" to 411.0,
                    "2017-03-14" to 985.0,
                )
            tooltip { }
            calendarBetween("2017-02", "2017-03-31") {
                top = str("middle")
                left = str("center")
                orient = Orient.Vertical
                cellSize = cellSize(40)
                yearLabel =
                    CalendarLabelOption().apply {
                        margin = 50.0
                        fontSize = 30.0
                    }
                // `dayLabel: { firstDay: 1, nameMap: 'cn' }` has no model field; see the file header.
                monthLabel =
                    CalendarLabelOption().apply {
                        margin = 15.0
                        fontSize = 20.0
                        color = "#999".asColor()
                    }
            }
            piecewiseVisualMap {
                min = 0.0
                max = 1000.0
                left = str("center")
                bottom = num(20)
                seriesIndex = listOf(1)
                inRange { color = listOf("#5291FF".asColor(), "#C7DBFF".asColor()) }
            }
            series {
                graph {
                    coordinateSystem = "calendar"
                    edgeSymbol = listOf("none", "arrow")
                    links = graphLinks(days.size)
                    symbolSize = NumberOrStringOrNumbers.of(15)
                    calendarIndex = 0
                    itemStyle =
                        ItemStyleOption().apply {
                            color = "yellow".asColor()
                            shadowBlur = 9.0
                            shadowOffsetX = 1.5
                            shadowOffsetY = 3.0
                            shadowColor = "#555".asColor()
                        }
                    lineStyle =
                        LineStyleOption().apply {
                            color = "#D10E00".asColor()
                            width = 1.0
                            opacity = 1.0
                        }
                    data = graphNodes(days)
                    z = 20.0
                }
                calendarHeatmap(yearValues("2017", 1000.0, 20170401L))
            }
        },
        calendarJson.example("calendar-pie") {
            // The pies sit on the days of February 2017, one per day.
            val days = dailyValues("2017-02-01", "2017-03-01", 10000.0, 20170201L)
            tooltip { }
            legend {
                entries("Work", "Entertainment", "Sleep")
                bottom = num(20)
            }
            calendarIn("2017-02") {
                top = str("middle")
                left = str("center")
                orient = Orient.Vertical
                cellSize = cellSize(num(80), num(80))
                yearLabel =
                    CalendarLabelOption().apply {
                        show = false
                        fontSize = 30.0
                    }
                // The `firstDay` / `nameMap` day labels of the original have no model field.
                dayLabel = CalendarLabelOption().apply { margin = 20.0 }
                monthLabel = CalendarLabelOption().apply { show = false }
            }
            series {
                // The layer that carried the day numbers as a `label.formatter` callback.
                scatter {
                    id = "label"
                    coordinateSystem = "calendar"
                    symbolSize = NumberOrStringOrNumbers.of(0)
                    label {
                        show = true
                        position = "inside"
                        offset = listOf(num(-30), num(-30))
                        fontSize = 14.0
                    }
                    data = days.map { (date, value) -> timedValue(date, value) }
                }
                days.forEachIndexed { index, (date, _) -> add(dayPie(index, date)) }
            }
        },
        calendarJson.example("calendar-charts") {
            val graphDays =
                listOf(
                    "2017-02-01" to 260.0,
                    "2017-02-04" to 200.0,
                    "2017-02-09" to 279.0,
                    "2017-02-13" to 847.0,
                    "2017-02-18" to 241.0,
                    "2017-02-23" to 411.0,
                    "2017-02-27" to 985.0,
                )
            tooltip { position = JsonPrimitive("top") }
            visualMap {
                min = 0.0
                max = 1000.0
                calculable = true
                seriesIndex = listOf(2, 3, 4)
                orient = Orient.Horizontal
                left = str("55%")
                bottom = num(20)
            }
            visualMap {
                min = 0.0
                max = 1000.0
                inRange {
                    color = listOf("grey".asColor())
                    opacity = listOf(0.0, 0.3)
                }
                controller =
                    buildJsonObject {
                        put("inRange", visualRangeJson(VisualMapRange().apply { opacity = listOf(0.3, 0.6) }))
                        put("outOfRange", visualRangeJson(VisualMapRange().apply { color = listOf("#ccc".asColor()) }))
                    }
                seriesIndex = listOf(1)
                orient = Orient.Horizontal
                left = str("10%")
                bottom = num(20)
            }
            calendarIn("2017-02") {
                orient = Orient.Vertical
                yearLabel = CalendarLabelOption().apply { margin = 40.0 }
                monthLabel = CalendarLabelOption().apply { margin = 20.0 }
                // The `dayLabel: { firstDay: 1, nameMap: 'cn' }` of the original has no model field.
                cellSize = cellSize(40)
            }
            calendarIn("2017-01") {
                orient = Orient.Vertical
                yearLabel = CalendarLabelOption().apply { margin = 40.0 }
                monthLabel = CalendarLabelOption().apply { margin = 20.0 }
                cellSize = cellSize(40)
                left = num(460)
            }
            calendarIn("2017-03") {
                orient = Orient.Vertical
                yearLabel = CalendarLabelOption().apply { margin = 40.0 }
                monthLabel = CalendarLabelOption().apply { margin = 20.0 }
                cellSize = cellSize(40)
                top = num(350)
            }
            calendarIn("2017-04") {
                orient = Orient.Vertical
                yearLabel = CalendarLabelOption().apply { margin = 40.0 }
                // The English `nameMap` day labels of the original have no model field.
                monthLabel = CalendarLabelOption().apply { margin = 20.0 }
                cellSize = cellSize(40)
                top = num(350)
                left = num(460)
            }
            series {
                graph {
                    coordinateSystem = "calendar"
                    edgeSymbol = listOf("none", "arrow")
                    links = graphLinks(graphDays.size)
                    symbolSize = NumberOrStringOrNumbers.of(10)
                    calendarIndex = 0
                    data = graphNodes(graphDays)
                }
                calendarHeatmap(yearValues("2017", 1000.0, 20170115L))
                effectScatter {
                    coordinateSystem = "calendar"
                    calendarIndex = 1
                    symbolSize = NumberOrStringOrNumbers.of(25)
                    data =
                        dailyValues("2017-01-01", "2018-01-01", 1000.0, 20170501L)
                            .map { (date, value) -> timedValue(date, value) }
                }
                scatter {
                    coordinateSystem = "calendar"
                    calendarIndex = 2
                    symbolSize = NumberOrStringOrNumbers.of(20)
                    data =
                        dailyValues("2017-03-01", "2018-01-01", 1000.0, 20170601L)
                            .map { (date, value) -> timedValue(date, value) }
                }
                calendarHeatmap(yearValues("2017", 1000.0, 20170701L), 3)
            }
        },
        calendarJson.example("calendar-effectscatter") {
            val days = dailyValues("2016-01-01", "2017-01-01", 10000.0, 20160101L)
            val topDays = days.sortedByDescending { it.second }.take(12)
            backgroundColor = "#404a59".asColor()
            title {
                top = num(30)
                text = "Daily Step Count in 2016"
                subtext = "Fake Data"
                left = str("center")
                textStyle { color = "#fff".asColor() }
            }
            tooltip { trigger = TooltipTrigger.Item }
            legend {
                top = str("30")
                left = str("100")
                entries("Steps", "Top 12")
                textStyle { color = "#fff".asColor() }
            }
            halfYearCalendar("2016-01-01", "2016-06-30", 120.0)
            halfYearCalendar("2016-07-01", "2016-12-31", 340.0)
            series {
                calendarSteps(days, null)
                calendarSteps(days, 1)
                calendarTopSteps(topDays, 1)
                calendarTopSteps(topDays, null)
            }
        },
    )
