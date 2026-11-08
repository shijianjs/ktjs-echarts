package demo.examples.calendar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.Orients
import io.github.shijianjs.ktjs.echarts.options.CalendarYearLabelOption
import io.github.shijianjs.ktjs.echarts.options.DoubleOrDoubleArray
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.EffectScatterDataItem
import io.github.shijianjs.ktjs.echarts.options.ScatterDataItem
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.StringOrDoubleOrArray
import io.github.shijianjs.ktjs.echarts.options.StringOrStringArray
import io.github.shijianjs.ktjs.echarts.options.SymbolSize
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.TooltipPosition
import io.github.shijianjs.ktjs.echarts.options.VisualMapOption
import io.github.shijianjs.ktjs.echarts.options.VisualOptionUnit
import io.github.shijianjs.ktjs.echarts.options.dataItems
import io.github.shijianjs.ktjs.echarts.options.effectScatterSeries
import io.github.shijianjs.ktjs.echarts.options.graphSeries
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.scatterSeries
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.js
import demo.examples.Category
import demo.examples.shared.numberAt
import demo.examples.shared.row

private val calendarChartsCategory = Category(Groups.CALENDAR, "examples/calendar/CalendarCharts.kt")

val calendarChartsEntry: CatalogEntry =
    calendarChartsCategory.chart("calendar-charts", "日历图", "Calendar Charts") {
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
                            seriesIndex = io.github.shijianjs.ktjs.echarts.options.IndexOrIndexArray(arrayOf(2.0, 3.0, 4.0))
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
                            seriesIndex = io.github.shijianjs.ktjs.echarts.options.IndexOrIndexArray(1.0)
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
        }
