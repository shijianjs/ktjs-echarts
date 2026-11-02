package demo.examples.calendar

import demo.CatalogEntry
import demo.Groups
import echarts.ECharts
import echarts.Orients
import echarts.options.CalendarMonthLabelOption
import echarts.options.CalendarYearLabelOption
import echarts.options.EChartsOption
import echarts.options.LabelFormatter
import echarts.options.LabelOption
import echarts.options.LabelPosition
import echarts.options.LegendOption
import echarts.options.ScatterDataItem
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.StringOrStringArray
import echarts.options.SymbolSize
import echarts.options.TooltipOption
import echarts.options.dataItems
import echarts.options.one
import echarts.options.scatterSeries
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.js
import demo.examples.Category
import demo.examples.funnel.labels
import demo.examples.gauge.center
import demo.examples.matrix.cell
import demo.examples.shared.every
import demo.examples.shared.legendNames
import demo.examples.shared.row
import demo.examples.shared.textAt
import demo.examples.themeriver.days

private val calendarPieCategory = Category(Groups.CALENDAR, "examples/calendar/CalendarPie.kt")

val calendarPieEntry: CatalogEntry =
    calendarPieCategory.chart("calendar-pie", "日历饼图", "Calendar Pie") {
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
        }
