package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.ECharts
import echarts.Orients
import echarts.TooltipTriggers
import echarts.VisualMapTypes
import echarts.options.CalendarDayLabelOption
import echarts.options.CalendarMonthLabelOption
import echarts.options.CalendarOption
import echarts.options.CalendarRange
import echarts.options.CalendarYearLabelOption
import echarts.options.DataZoomValue
import echarts.options.DoubleOrDoubleArray
import echarts.options.EChartsOption
import echarts.options.EffectScatterDataItem
import echarts.options.EffectScatterSeriesOption
import echarts.options.GraphDataItem
import echarts.options.HeatmapDataItem
import echarts.options.HeatmapSeriesOption
import echarts.options.ItemStyleOption
import echarts.options.LabelFormatter
import echarts.options.LabelOption
import echarts.options.LabelPosition
import echarts.options.LegendOption
import echarts.options.LineStyleOption
import echarts.options.PieSeriesOption
import echarts.options.RippleEffectOption
import echarts.options.ScatterDataItem
import echarts.options.ScatterSeriesOption
import echarts.options.SeriesOption
import echarts.options.SplitLineOption
import echarts.options.SplitLineStyleOption
import echarts.options.StringOrDouble
import echarts.options.StringOrDoubleOrArray
import echarts.options.StringOrStringArray
import echarts.options.SymbolSize
import echarts.options.TitleOption
import echarts.options.TooltipFormatter
import echarts.options.TooltipFormatterCallback
import echarts.options.TooltipOption
import echarts.options.TooltipPosition
import echarts.options.VisualMapOption
import echarts.options.VisualOptionUnit
import echarts.options.ZRColorOrArray
import echarts.options.dataItems
import echarts.options.effectScatterSeries
import echarts.options.graphSeries
import echarts.options.heatmapSeries
import echarts.options.one
import echarts.options.pieSeries
import echarts.options.scatterSeries
import js.objects.unsafeJso
import kotlin.js.Date
import kotlin.js.js
import zrender.ZRColor
import zrender.ZRLineType

/*
 * Calendar examples ported from `public/examples/ts/calendar-*.ts` of apache/echarts-examples.
 *
 * The day data of the official examples comes from a `getVirtualData` helper that walks the days of
 * a range and fills each one with `Math.random()`; see [dailyValues] for the reproducible
 * replacement.
 *
 * `calendar-lunar` is not ported: its 366-row lunar calendar table is data rather than an example
 * of the API.
 */

private val calendar = Category(Groups.CALENDAR, "examples/Calendar.kt")

/**
 * `getVirtualData` of the official examples: one `[date, value]` point per day of the range.
 *
 * The value comes from a fixed linear congruential sequence instead of `Math.random()`, so a page
 * looks the same on every visit and the end-to-end assertions stay stable.
 */
private fun dailyValues(from: String, to: String, max: Double, seed: Long): List<Pair<String, Double>> {
    var state = seed
    fun next(): Double {
        state = (state * 6364136223846793005L + 1442695040888963407L) and Long.MAX_VALUE
        return (state % 100000L).toDouble() / 100000.0
    }

    val dayTime = 3600.0 * 24 * 1000
    val end = ECharts.time.parse(to).getTime()
    val days = mutableListOf<Pair<String, Double>>()
    var time = ECharts.time.parse(from).getTime()
    while (time < end) {
        val date = ECharts.time.format(Date(time), "{yyyy}-{MM}-{dd}", false)
        days += date to kotlin.math.floor(next() * max)
        time += dayTime
    }
    return days
}

/** The days of [year] as the `[date, value]` points of a calendar heatmap. */
private fun yearValues(year: String, max: Double, seed: Long): Array<HeatmapDataItem?> =
    dataItems(
        *dailyValues("$year-01-01", "${year.toInt() + 1}-01-01", max, seed)
            .map { (date, value) -> HeatmapDataItem(row(date, value)) }
            .toTypedArray(),
    )

/** One calendar of the examples; [block] sets the placement and the labels. */
private fun calendarOption(range: String, block: CalendarOption.() -> Unit = {}): CalendarOption =
    unsafeJso {
        this.range = CalendarRange(DataZoomValue(range))
        block()
    }

/** `cellSize: [20, 'auto']`. */
private fun cellSize(width: StringOrDouble, height: StringOrDouble): StringOrDoubleOrArray =
    StringOrDoubleOrArray(arrayOf(width, height))

/** `dayLabel: { firstDay: 1, nameMap: 'cn' }`. */
private fun chineseDayLabel(margin: Double? = null): CalendarDayLabelOption =
    unsafeJso {
        firstDay = 1.0
        nameMap = StringOrStringArray("cn")
        if (margin != null) this.margin = StringOrDouble(margin)
    }

/** `monthLabel: { nameMap: 'cn', margin: <margin> }`. */
private fun chineseMonthLabel(margin: Double): CalendarMonthLabelOption =
    unsafeJso {
        nameMap = StringOrStringArray("cn")
        this.margin = margin
    }

/** The heatmap series of the calendar examples, all of them on the calendar coordinate system. */
private fun calendarHeatmap(
    data: Array<HeatmapDataItem?>,
    calendarIndex: Double? = null,
): HeatmapSeriesOption =
    heatmapSeries {
        coordinateSystem = "calendar"
        typedData = data
        if (calendarIndex != null) this.calendarIndex = calendarIndex
    }

/** The discrete legend of `calendar-heatmap` and `calendar-graph`. */
private fun piecewiseVisualMap(block: VisualMapOption.() -> Unit): Array<VisualMapOption> =
    one(
        unsafeJso {
            type = VisualMapTypes.piecewise
            orient = Orients.horizontal
            block()
        },
    )

val calendarEntries: List<CatalogEntry> =
    listOf(
        calendar.chart("calendar-simple", "基础日历图", "Simple Calendar") {
            unsafeJso<EChartsOption> {
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            show = false
                            min = 0.0
                            max = 10000.0
                        },
                    )
                calendar = one(calendarOption("2017"))
                series = arrayOf<SeriesOption>(calendarHeatmap(yearValues("2017", 10000.0, 20170401L)))
            }
        },
        calendar.chart("calendar-heatmap", "日历热力图", "Calendar Heatmap") {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<echarts.options.TitleOption> {
                            top = StringOrDouble(30.0)
                            left = StringOrDouble("center")
                            text = "Daily Step Count"
                        },
                    )
                tooltip = one(unsafeJso<TooltipOption> {})
                visualMap =
                    piecewiseVisualMap {
                        min = 0.0
                        max = 10000.0
                        left = StringOrDouble("center")
                        top = StringOrDouble(65.0)
                    }
                calendar =
                    one(
                        calendarOption("2016") {
                            top = StringOrDouble(120.0)
                            left = StringOrDouble(30.0)
                            right = StringOrDouble(30.0)
                            cellSize = cellSize(StringOrDouble("auto"), StringOrDouble(13.0))
                            itemStyle = unsafeJso<ItemStyleOption> { borderWidth = 0.5 }
                            yearLabel = unsafeJso<CalendarYearLabelOption> { show = false }
                        },
                    )
                series = arrayOf<SeriesOption>(calendarHeatmap(yearValues("2016", 10000.0, 20160101L)))
            }
        },
        calendar.chart("calendar-vertical", "纵向日历图", "Calendar Heatmap Vertical") {
            unsafeJso<EChartsOption> {
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            position = TooltipPosition("top")
                            // The original formats `p.data[0]`, which already is a `yyyy-MM-dd`
                            // string, with the same template, so the formatting is the identity.
                            formatter =
                                TooltipFormatter(
                                    js("(p) => p.data[0] + ': ' + p.data[1]") as TooltipFormatterCallback,
                                )
                        },
                    )
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            min = 0.0
                            max = 1000.0
                            calculable = true
                            orient = Orients.vertical
                            left = StringOrDouble("670")
                            top = StringOrDouble("center")
                        },
                    )
                calendar =
                    arrayOf(
                        calendarOption("2015") { orient = Orients.vertical },
                        calendarOption("2016") {
                            left = StringOrDouble(300.0)
                            orient = Orients.vertical
                        },
                        calendarOption("2017") {
                            left = StringOrDouble(520.0)
                            cellSize = cellSize(StringOrDouble(20.0), StringOrDouble("auto"))
                            bottom = StringOrDouble(10.0)
                            orient = Orients.vertical
                            dayLabel = unsafeJso { margin = StringOrDouble(5.0) }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        calendarHeatmap(yearValues("2015", 1000.0, 20150101L), 0.0),
                        calendarHeatmap(yearValues("2016", 1000.0, 20160101L), 1.0),
                        calendarHeatmap(yearValues("2017", 1000.0, 20170101L), 2.0),
                    )
            }
        },
        calendar.chart("calendar-horizontal", "横向日历图", "Calendar Heatmap Horizontal") {
            unsafeJso<EChartsOption> {
                tooltip = one(unsafeJso<TooltipOption> { position = TooltipPosition("top") })
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            min = 0.0
                            max = 1000.0
                            calculable = true
                            orient = Orients.horizontal
                            left = StringOrDouble("center")
                            top = StringOrDouble("top")
                        },
                    )
                calendar =
                    arrayOf(
                        calendarOption("2017") { cellSize = cellSize(StringOrDouble("auto"), StringOrDouble(20.0)) },
                        calendarOption("2016") {
                            top = StringOrDouble(260.0)
                            cellSize = cellSize(StringOrDouble("auto"), StringOrDouble(20.0))
                        },
                        calendarOption("2015") {
                            top = StringOrDouble(450.0)
                            cellSize = cellSize(StringOrDouble("auto"), StringOrDouble(20.0))
                            right = StringOrDouble(5.0)
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        calendarHeatmap(yearValues("2017", 1000.0, 20170101L), 0.0),
                        calendarHeatmap(yearValues("2016", 1000.0, 20160201L), 1.0),
                        calendarHeatmap(yearValues("2015", 1000.0, 20150301L), 2.0),
                    )
            }
        },
        calendar.chart("calendar-graph", "日历关系图", "Calendar Graph") {
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
            unsafeJso<EChartsOption> {
                tooltip = one(unsafeJso<TooltipOption> {})
                calendar =
                    one(
                        calendarOption("2017-02") {
                            range = CalendarRange(arrayOf(DataZoomValue("2017-02"), DataZoomValue("2017-03-31")))
                            top = StringOrDouble("middle")
                            left = StringOrDouble("center")
                            orient = Orients.vertical
                            cellSize = StringOrDoubleOrArray(40.0)
                            yearLabel = unsafeJso<CalendarYearLabelOption> { margin = 50.0; fontSize = StringOrDouble(30.0) }
                            dayLabel = chineseDayLabel()
                            monthLabel =
                                unsafeJso {
                                    nameMap = StringOrStringArray("cn")
                                    margin = 15.0
                                    fontSize = StringOrDouble(20.0)
                                    color = "#999"
                                }
                        },
                    )
                visualMap =
                    piecewiseVisualMap {
                        min = 0.0
                        max = 1000.0
                        left = StringOrDouble("center")
                        bottom = StringOrDouble(20.0)
                        seriesIndex = echarts.options.IndexOrIndexArray(1.0)
                        inRange =
                            unsafeJso<VisualOptionUnit> {
                                color = StringOrStringArray(arrayOf("#5291FF", "#C7DBFF"))
                            }
                    }
                series =
                    arrayOf<SeriesOption>(
                        graphSeries {
                            coordinateSystem = "calendar"
                            edgeSymbol = StringOrStringArray(arrayOf("none", "arrow"))
                            links = calendarGraphLinks(days.size)
                            symbolSize = SymbolSize(15.0)
                            calendarIndex = 0.0
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    color = ZRColor("yellow")
                                    shadowBlur = 9.0
                                    shadowOffsetX = 1.5
                                    shadowOffsetY = 3.0
                                    shadowColor = ZRColor("#555")
                                }
                            lineStyle =
                                unsafeJso<LineStyleOption> {
                                    color = ZRColor("#D10E00")
                                    width = 1.0
                                    opacity = 1.0
                                }
                            typedData = calendarGraphNodes(days)
                            z = 20.0
                        },
                        calendarHeatmap(yearValues("2017", 1000.0, 20170401L)),
                    )
            }
        },
        calendar.chart("calendar-pie", "日历饼图", "Calendar Pie") {
            // The labels are the day numbers of February 2017, the pies sit on the same days.
            val days = dailyValues("2017-02-01", "2017-03-01", 10000.0, 20170201L)
            unsafeJso<EChartsOption> {
                tooltip = one(unsafeJso<TooltipOption> {})
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            data = legendNames("Work", "Entertainment", "Sleep")
                            bottom = StringOrDouble(20.0)
                        },
                    )
                calendar =
                    one(
                        calendarOption("2017-02") {
                            top = StringOrDouble("middle")
                            left = StringOrDouble("center")
                            orient = Orients.vertical
                            cellSize =
                                cellSize(StringOrDouble(80.0), StringOrDouble(80.0))
                            yearLabel = unsafeJso<CalendarYearLabelOption> { show = false; fontSize = StringOrDouble(30.0) }
                            dayLabel =
                                unsafeJso {
                                    margin = StringOrDouble(20.0)
                                    firstDay = 1.0
                                    nameMap =
                                        StringOrStringArray(
                                            arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
                                        )
                                }
                            monthLabel = unsafeJso<CalendarMonthLabelOption> { show = false }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        // The day number of every cell, drawn as a label at its top left corner.
                        scatterSeries {
                            id = "label"
                            coordinateSystem = "calendar"
                            symbolSize = SymbolSize(0.0)
                            label =
                                unsafeJso<LabelOption> {
                                    show = true
                                    position = LabelPosition("inside")
                                    offset = arrayOf(-30.0, -30.0)
                                    fontSize = StringOrDouble(14.0)
                                    formatter =
                                        LabelFormatter { params ->
                                            ECharts.time.format(
                                                ECharts.time.parse(textAt(params.value, 0)),
                                                "{dd}",
                                                false,
                                            )
                                        }
                                }
                            typedData =
                                dataItems(
                                    *days.map { (date, value) -> ScatterDataItem(row(date, value)) }.toTypedArray(),
                                )
                        },
                        *days.mapIndexed { index, (date, _) -> dayPie(index, date) }.toTypedArray(),
                    )
            }
        },
        calendar.chart("calendar-charts", "日历图", "Calendar Charts") {
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
            unsafeJso<EChartsOption> {
                tooltip = one(unsafeJso<TooltipOption> { position = TooltipPosition("top") })
                visualMap =
                    arrayOf(
                        unsafeJso<VisualMapOption> {
                            min = 0.0
                            max = 1000.0
                            calculable = true
                            seriesIndex = echarts.options.IndexOrIndexArray(arrayOf(2.0, 3.0, 4.0))
                            orient = Orients.horizontal
                            left = StringOrDouble("55%")
                            bottom = StringOrDouble(20.0)
                        },
                        unsafeJso<VisualMapOption> {
                            min = 0.0
                            max = 1000.0
                            inRange =
                                unsafeJso<VisualOptionUnit> {
                                    color = StringOrStringArray(arrayOf("grey"))
                                    opacity = DoubleOrDoubleArray(arrayOf(0.0, 0.3))
                                }
                            controller =
                                unsafeJso {
                                    inRange =
                                        unsafeJso<VisualOptionUnit> {
                                            opacity = DoubleOrDoubleArray(arrayOf(0.3, 0.6))
                                        }
                                    outOfRange = unsafeJso<VisualOptionUnit> { color = StringOrStringArray("#ccc") }
                                }
                            seriesIndex = echarts.options.IndexOrIndexArray(1.0)
                            orient = Orients.horizontal
                            left = StringOrDouble("10%")
                            bottom = StringOrDouble(20.0)
                        },
                    )
                calendar =
                    arrayOf(
                        calendarOption("2017-02") {
                            orient = Orients.vertical
                            yearLabel = unsafeJso<CalendarYearLabelOption> { margin = 40.0 }
                            monthLabel = chineseMonthLabel(20.0)
                            dayLabel = chineseDayLabel()
                            cellSize = StringOrDoubleOrArray(40.0)
                        },
                        calendarOption("2017-01") {
                            orient = Orients.vertical
                            yearLabel = unsafeJso<CalendarYearLabelOption> { margin = 40.0 }
                            monthLabel = unsafeJso { margin = 20.0 }
                            cellSize = StringOrDoubleOrArray(40.0)
                            left = StringOrDouble(460.0)
                        },
                        calendarOption("2017-03") {
                            orient = Orients.vertical
                            yearLabel = unsafeJso<CalendarYearLabelOption> { margin = 40.0 }
                            monthLabel = unsafeJso { margin = 20.0 }
                            cellSize = StringOrDoubleOrArray(40.0)
                            top = StringOrDouble(350.0)
                        },
                        calendarOption("2017-04") {
                            orient = Orients.vertical
                            yearLabel = unsafeJso<CalendarYearLabelOption> { margin = 40.0 }
                            dayLabel =
                                unsafeJso {
                                    firstDay = 1.0
                                    nameMap =
                                        StringOrStringArray(
                                            arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"),
                                        )
                                }
                            monthLabel = chineseMonthLabel(20.0)
                            cellSize = StringOrDoubleOrArray(40.0)
                            top = StringOrDouble(350.0)
                            left = StringOrDouble(460.0)
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        graphSeries {
                            coordinateSystem = "calendar"
                            edgeSymbol = StringOrStringArray(arrayOf("none", "arrow"))
                            links = calendarGraphLinks(graphDays.size)
                            symbolSize = SymbolSize(10.0)
                            calendarIndex = 0.0
                            typedData = calendarGraphNodes(graphDays)
                        },
                        calendarHeatmap(yearValues("2017", 1000.0, 20170115L)),
                        effectScatterSeries {
                            coordinateSystem = "calendar"
                            calendarIndex = 1.0
                            symbolSize = SymbolSize { value, _ -> DoubleOrDoubleArray(numberAt(value, 1) / 40.0) }
                            typedData =
                                dataItems(
                                    *dailyValues("2017-01-01", "2018-01-01", 1000.0, 20170501L)
                                        .map { (date, value) -> EffectScatterDataItem(row(date, value)) }
                                        .toTypedArray(),
                                )
                        },
                        scatterSeries {
                            coordinateSystem = "calendar"
                            calendarIndex = 2.0
                            symbolSize = SymbolSize { value, _ -> DoubleOrDoubleArray(numberAt(value, 1) / 60.0) }
                            typedData =
                                dataItems(
                                    *dailyValues("2017-03-01", "2018-01-01", 1000.0, 20170601L)
                                        .map { (date, value) -> ScatterDataItem(row(date, value)) }
                                        .toTypedArray(),
                                )
                        },
                        calendarHeatmap(yearValues("2017", 1000.0, 20170701L), 3.0),
                    )
            }
        },
        calendar.chart("calendar-effectscatter", "热力特效散点图", "Calendar EffectScatter") {
            // The original draws the values with `Math.random()`; the seeded helper keeps the page
            // reproducible. Its `Top 12` series sorts the shared array in place, which only changes
            // the draw order of independent points.
            val days = dailyValues("2016-01-01", "2017-01-01", 10000.0, 20160101L)
            val topDays = days.sortedByDescending { it.second }.take(12)
            unsafeJso<EChartsOption> {
                backgroundColor = ZRColor("#404a59")
                title =
                    one(
                        unsafeJso<TitleOption> {
                            top = StringOrDouble(30.0)
                            text = "Daily Step Count in 2016"
                            subtext = "Fake Data"
                            left = StringOrDouble("center")
                            textStyle = unsafeJso<LabelOption> { color = "#fff" }
                        },
                    )
                tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.item })
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            top = StringOrDouble("30")
                            left = StringOrDouble("100")
                            data = legendNames("Steps", "Top 12")
                            textStyle = unsafeJso<LabelOption> { color = "#fff" }
                        },
                    )
                calendar =
                    arrayOf(
                        effectScatterCalendar("2016-01-01", "2016-06-30", 120.0, "1st"),
                        effectScatterCalendar("2016-07-01", "2016-12-31", 340.0, "2nd"),
                    )
                series =
                    arrayOf<SeriesOption>(
                        calendarSteps(days, null),
                        calendarSteps(days, 1.0),
                        calendarTopSteps(topDays, 1.0),
                        calendarTopSteps(topDays, null),
                    )
            }
        },
    )

/** One half-year calendar of `calendar-effectscatter`, [ordinal] labels the year. */
private fun effectScatterCalendar(from: String, to: String, top: Double, ordinal: String): CalendarOption =
    unsafeJso {
        this.top = StringOrDouble(top)
        left = StringOrDouble("center")
        range = CalendarRange(arrayOf(DataZoomValue(from), DataZoomValue(to)))
        splitLine =
            unsafeJso<SplitLineOption> {
                show = true
                lineStyle =
                    unsafeJso<SplitLineStyleOption> {
                        typedColor = ZRColorOrArray(ZRColor("#000"))
                        width = 4.0
                        type = ZRLineType("solid")
                    }
            }
        yearLabel =
            unsafeJso<CalendarYearLabelOption> {
                formatter = LabelFormatter("{start}  $ordinal")
                color = "#fff"
            }
        monthLabel = unsafeJso<CalendarMonthLabelOption> { color = "#aaa" }
        dayLabel = unsafeJso<CalendarDayLabelOption> { color = "#aaa" }
        itemStyle =
            unsafeJso<ItemStyleOption> {
                color = ZRColor("#323c48")
                borderWidth = 1.0
                borderColor = ZRColor("#111")
            }
    }

/** The plain scatter series of `calendar-effectscatter`; the size is `value / 500`. */
private fun calendarSteps(days: List<Pair<String, Double>>, calendarIndex: Double?): ScatterSeriesOption =
    scatterSeries {
        name = "Steps"
        coordinateSystem = "calendar"
        if (calendarIndex != null) this.calendarIndex = calendarIndex
        symbolSize = SymbolSize { value, _ -> DoubleOrDoubleArray(numberAt(value, 1) / 500.0) }
        itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#ddb926") }
        typedData = dataItems(*days.map { (date, value) -> ScatterDataItem(row(date, value)) }.toTypedArray())
    }

/** The ripple series of the twelve largest days of `calendar-effectscatter`. */
private fun calendarTopSteps(days: List<Pair<String, Double>>, calendarIndex: Double?): EffectScatterSeriesOption =
    effectScatterSeries {
        name = "Top 12"
        coordinateSystem = "calendar"
        if (calendarIndex != null) this.calendarIndex = calendarIndex
        symbolSize = SymbolSize { value, _ -> DoubleOrDoubleArray(numberAt(value, 1) / 500.0) }
        showEffectOn = "render"
        rippleEffect = unsafeJso<RippleEffectOption> { brushType = "stroke" }
        itemStyle =
            unsafeJso<ItemStyleOption> {
                color = ZRColor("#f4e925")
                shadowBlur = 10.0
                shadowColor = ZRColor("#333")
            }
        zlevel = 1.0
        typedData = dataItems(*days.map { (date, value) -> EffectScatterDataItem(row(date, value)) }.toTypedArray())
    }

/** The chain that links the `[date, value]` points of `calendar-graph` / `calendar-charts`. */
private fun calendarGraphLinks(count: Int): Array<echarts.options.GraphEdgeItemOption> =
    Array(count - 1) { index ->
        unsafeJso {
            source = echarts.options.StringOrDouble(index.toDouble())
            target = echarts.options.StringOrDouble((index + 1).toDouble())
        }
    }

/** The `[date, value]` points of the graph series of the calendar examples. */
private fun calendarGraphNodes(days: List<Pair<String, Double>>): Array<GraphDataItem?> =
    dataItems(*days.map { (date, value) -> GraphDataItem(row(date, value)) }.toTypedArray())

/** One day pie of `calendar-pie`: the three categories of that day. */
private fun dayPie(index: Int, date: String): PieSeriesOption {
    var state = 20170201L + index
    fun next(): Double {
        state = (state * 6364136223846793005L + 1442695040888963407L) and Long.MAX_VALUE
        return (state % 100000L).toDouble() / 100000.0
    }
    return pieSeries {
        id = "pie-$index"
        center = arrayOf(StringOrDouble(date))
        radius = StringOrDoubleOrArray(30.0)
        coordinateSystem = "calendar"
        label = unsafeJso<LabelOption> { formatter = LabelFormatter("{c}"); position = LabelPosition("inside") }
        typedData =
            dataItems(
                pieItem("Work", kotlin.math.round(next() * 24)),
                pieItem("Entertainment", kotlin.math.round(next() * 24)),
                pieItem("Sleep", kotlin.math.round(next() * 24)),
            )
    }
}
