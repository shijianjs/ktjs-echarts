package demo.examples.calendar

import io.github.shijianjs.ktjs.echarts.ECharts
import io.github.shijianjs.ktjs.echarts.Orients
import io.github.shijianjs.ktjs.echarts.VisualMapTypes
import io.github.shijianjs.ktjs.echarts.options.CalendarDayLabelOption
import io.github.shijianjs.ktjs.echarts.options.CalendarMonthLabelOption
import io.github.shijianjs.ktjs.echarts.options.CalendarOption
import io.github.shijianjs.ktjs.echarts.options.CalendarRange
import io.github.shijianjs.ktjs.echarts.options.CalendarYearLabelOption
import io.github.shijianjs.ktjs.echarts.options.CallbackDataParams
import io.github.shijianjs.ktjs.echarts.options.DataZoomValue
import io.github.shijianjs.ktjs.echarts.options.DoubleOrDoubleArray
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.EffectScatterDataItem
import io.github.shijianjs.ktjs.echarts.options.EffectScatterSeriesOption
import io.github.shijianjs.ktjs.echarts.options.GraphDataItem
import io.github.shijianjs.ktjs.echarts.options.HeatmapDataItem
import io.github.shijianjs.ktjs.echarts.options.HeatmapSeriesOption
import io.github.shijianjs.ktjs.echarts.options.ItemStyleOption
import io.github.shijianjs.ktjs.echarts.options.LabelFormatter
import io.github.shijianjs.ktjs.echarts.options.LabelOption
import io.github.shijianjs.ktjs.echarts.options.LabelPosition
import io.github.shijianjs.ktjs.echarts.options.OptionDataValue
import io.github.shijianjs.ktjs.echarts.options.PieSeriesOption
import io.github.shijianjs.ktjs.echarts.options.RippleEffectOption
import io.github.shijianjs.ktjs.echarts.options.ScatterDataItem
import io.github.shijianjs.ktjs.echarts.options.ScatterSeriesOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.SplitLineOption
import io.github.shijianjs.ktjs.echarts.options.SplitLineStyleOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.StringOrDoubleOrArray
import io.github.shijianjs.ktjs.echarts.options.StringOrStringArray
import io.github.shijianjs.ktjs.echarts.options.SymbolSize
import io.github.shijianjs.ktjs.echarts.options.TooltipContent
import io.github.shijianjs.ktjs.echarts.options.TooltipFormatter
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.VisualMapOption
import io.github.shijianjs.ktjs.echarts.options.VisualOptionUnit
import io.github.shijianjs.ktjs.echarts.options.ZRColorOrArray
import io.github.shijianjs.ktjs.echarts.options.dataItems
import io.github.shijianjs.ktjs.echarts.options.effectScatterSeries
import io.github.shijianjs.ktjs.echarts.options.heatmapSeries
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.pieSeries
import io.github.shijianjs.ktjs.echarts.options.scatterSeries
import js.objects.unsafeJso
import kotlin.js.Date
import kotlin.js.JsAny
import kotlin.js.js
import io.github.shijianjs.ktjs.zrender.ZRColor
import io.github.shijianjs.ktjs.zrender.ZRFontWeight
import io.github.shijianjs.ktjs.zrender.ZRLineType
import demo.examples.chord.column
import demo.examples.funnel.labels
import demo.examples.gauge.center
import demo.examples.lines.coordinate
import demo.examples.scatter.point
import demo.examples.shared.Lcg
import demo.examples.shared.every
import demo.examples.shared.fixed
import demo.examples.shared.numberAt
import demo.examples.shared.pieItem
import demo.examples.shared.row
import demo.examples.shared.textAt
import demo.examples.sunburst.radius
import demo.examples.themeriver.days

/**
 * `getVirtualData` of the official examples: one `[date, value]` point per day of the range.
 *
 * The value comes from a fixed linear congruential sequence instead of `Math.random()`, so a page
 * looks the same on every visit and the end-to-end assertions stay stable.
 */
internal fun dailyValues(from: String, to: String, max: Double, seed: Long): List<Pair<String, Double>> {
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
internal fun yearValues(year: String, max: Double, seed: Long): Array<HeatmapDataItem?> =
    dataItems(
        *dailyValues("$year-01-01", "${year.toInt() + 1}-01-01", max, seed)
            .map { (date, value) -> HeatmapDataItem(row(date, value)) }
            .toTypedArray(),
    )

/** One calendar of the examples; [block] sets the placement and the labels. */
internal fun calendarOption(range: String, block: CalendarOption.() -> Unit = {}): CalendarOption =
    unsafeJso {
        this.range = CalendarRange(DataZoomValue(range))
        block()
    }

/** `cellSize: [20, 'auto']`. */
internal fun cellSize(width: StringOrDouble, height: StringOrDouble): StringOrDoubleOrArray =
    StringOrDoubleOrArray(arrayOf(width, height))

/** `dayLabel: { firstDay: 1, nameMap: 'cn' }`. */
internal fun chineseDayLabel(margin: Double? = null): CalendarDayLabelOption =
    unsafeJso {
        firstDay = 1.0
        nameMap = StringOrStringArray("cn")
        if (margin != null) this.margin = StringOrDouble(margin)
    }

/** `monthLabel: { nameMap: 'cn', margin: <margin> }`. */
internal fun chineseMonthLabel(margin: Double): CalendarMonthLabelOption =
    unsafeJso {
        nameMap = StringOrStringArray("cn")
        this.margin = margin
    }

/** The heatmap series of the calendar examples, all of them on the calendar coordinate system. */
internal fun calendarHeatmap(
    data: Array<HeatmapDataItem?>,
    calendarIndex: Double? = null,
): HeatmapSeriesOption =
    heatmapSeries {
        coordinateSystem = "calendar"
        typedData = data
        if (calendarIndex != null) this.calendarIndex = calendarIndex
    }

/** The discrete legend of `calendar-heatmap` and `calendar-graph`. */
internal fun piecewiseVisualMap(block: VisualMapOption.() -> Unit): Array<VisualMapOption> =
    one(
        unsafeJso {
            type = VisualMapTypes.piecewise
            orient = Orients.horizontal
            block()
        },
    )

/** One half-year calendar of `calendar-effectscatter`, [ordinal] labels the year. */
internal fun effectScatterCalendar(from: String, to: String, top: Double, ordinal: String): CalendarOption =
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
internal fun calendarSteps(days: List<Pair<String, Double>>, calendarIndex: Double?): ScatterSeriesOption =
    scatterSeries {
        name = "Steps"
        coordinateSystem = "calendar"
        if (calendarIndex != null) this.calendarIndex = calendarIndex
        symbolSize = SymbolSize { value, _ -> DoubleOrDoubleArray(numberAt(value, 1) / 500.0) }
        itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#ddb926") }
        typedData = dataItems(*days.map { (date, value) -> ScatterDataItem(row(date, value)) }.toTypedArray())
    }

/** The ripple series of the twelve largest days of `calendar-effectscatter`. */
internal fun calendarTopSteps(days: List<Pair<String, Double>>, calendarIndex: Double?): EffectScatterSeriesOption =
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
internal fun calendarGraphLinks(count: Int): Array<io.github.shijianjs.ktjs.echarts.options.GraphEdgeItemOption> =
    Array(count - 1) { index ->
        unsafeJso {
            source = io.github.shijianjs.ktjs.echarts.options.StringOrDouble(index.toDouble())
            target = io.github.shijianjs.ktjs.echarts.options.StringOrDouble((index + 1).toDouble())
        }
    }

/** The `[date, value]` points of the graph series of the calendar examples. */
internal fun calendarGraphNodes(days: List<Pair<String, Double>>): Array<GraphDataItem?> =
    dataItems(*days.map { (date, value) -> GraphDataItem(row(date, value)) }.toTypedArray())

/** `value[3] || ''` of a lunar row: the solar term, or an empty string when the day has none. */
internal fun solarTerm(value: JsAny?): String = js("(value) => value[3] || ''")(value) as String

/**
 * The option of `calendar-lunar`.
 *
 * The 365-row lunar table is data rather than an example of the API, so it lives in
 * `data/calendar-lunar.json` (the `dateList` literal of the original; the solar term is an optional
 * third column). Both scatter series draw no symbol at all: they put the day of the month, the
 * lunar day, the lunar month and the solar term on the cells of March 2017, while the heatmap
 * behind them carries the rainfall column.
 */
internal fun lunarOption(table: Array<Array<String?>>): EChartsOption {
    val rain = Lcg(20170301)
    fun lunarRow(day: Array<String?>): Array<OptionDataValue?> {
        val date = day[0] ?: ""
        val name = day[1] ?: ""
        val term = day.getOrNull(2)
        return if (term == null) row(date, 1.0, name) else row(date, 1.0, name, term)
    }
    val lunarData = dataItems(*table.map { ScatterDataItem(lunarRow(it)) }.toTypedArray())
    val heatmapData =
        dataItems(
            *table.map { HeatmapDataItem(row(it[0] ?: "", rain.next() * 300)) }.toTypedArray(),
        )
    return unsafeJso {
        tooltip =
            one(
                unsafeJso<TooltipOption> {
                    formatter =
                        TooltipFormatter { params ->
                            val item = params.unsafeCast<CallbackDataParams>()
                            TooltipContent("降雨量: " + fixed(numberAt(item.value, 1), 2))
                        }
                },
            )
        visualMap =
            one(
                unsafeJso<VisualMapOption> {
                    show = false
                    min = 0.0
                    max = 300.0
                    calculable = true
                    seriesIndex = io.github.shijianjs.ktjs.echarts.options.IndexOrIndexArray(2.0)
                    orient = Orients.horizontal
                    left = StringOrDouble("center")
                    bottom = StringOrDouble(20.0)
                    inRange =
                        unsafeJso<VisualOptionUnit> {
                            color = StringOrStringArray(arrayOf("#e0ffff", "#006edd"))
                            opacity = DoubleOrDoubleArray(0.3)
                        }
                    controller = unsafeJso { inRange = unsafeJso<VisualOptionUnit> { opacity = DoubleOrDoubleArray(0.5) } }
                },
            )
        calendar =
            one(
                calendarOption("2017-03") {
                    left = StringOrDouble("center")
                    top = StringOrDouble("middle")
                    cellSize = cellSize(StringOrDouble(70.0), StringOrDouble(70.0))
                    yearLabel = unsafeJso<CalendarYearLabelOption> { show = false }
                    orient = Orients.vertical
                    dayLabel = chineseDayLabel()
                    monthLabel = unsafeJso<CalendarMonthLabelOption> { show = false }
                },
            )
        series =
            arrayOf<SeriesOption>(
                scatterSeries {
                    coordinateSystem = "calendar"
                    symbolSize = SymbolSize(0.0)
                    label =
                        unsafeJso<LabelOption> {
                            show = true
                            formatter =
                                LabelFormatter { params ->
                                    ECharts.number.parseDate(textAt(params.value, 0)).getDate().toString() +
                                        "\n\n" + textAt(params.value, 2) + "\n\n"
                                }
                            color = "#000"
                        }
                    typedData = lunarData
                    silent = true
                },
                scatterSeries {
                    coordinateSystem = "calendar"
                    symbolSize = SymbolSize(0.0)
                    label =
                        unsafeJso<LabelOption> {
                            show = true
                            formatter = LabelFormatter { params -> "\n\n\n" + solarTerm(params.value) }
                            fontSize = StringOrDouble(14.0)
                            fontWeight = ZRFontWeight(700.0)
                            color = "#a00"
                        }
                    typedData = lunarData
                    silent = true
                },
                heatmapSeries {
                    name = "降雨量"
                    coordinateSystem = "calendar"
                    typedData = heatmapData
                },
            )
    }
}

/** One day pie of `calendar-pie`: the three categories of that day. */
internal fun dayPie(index: Int, date: String): PieSeriesOption {
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
