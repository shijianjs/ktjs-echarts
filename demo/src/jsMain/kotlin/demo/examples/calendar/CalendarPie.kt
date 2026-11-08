package demo.examples.calendar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.ECharts
import io.github.shijianjs.ktjs.echarts.Orients
import io.github.shijianjs.ktjs.echarts.options.CalendarMonthLabelOption
import io.github.shijianjs.ktjs.echarts.options.CalendarYearLabelOption
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.LabelFormatter
import io.github.shijianjs.ktjs.echarts.options.LabelOption
import io.github.shijianjs.ktjs.echarts.options.LabelPosition
import io.github.shijianjs.ktjs.echarts.options.LegendOption
import io.github.shijianjs.ktjs.echarts.options.ScatterDataItem
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.StringOrStringArray
import io.github.shijianjs.ktjs.echarts.options.SymbolSize
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.dataItems
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.scatterSeries
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
