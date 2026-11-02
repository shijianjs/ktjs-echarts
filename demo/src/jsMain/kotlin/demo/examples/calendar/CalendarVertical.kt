package demo.examples.calendar

import demo.CatalogEntry
import demo.Groups
import echarts.Orients
import echarts.options.EChartsOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.TooltipFormatter
import echarts.options.TooltipFormatterCallback
import echarts.options.TooltipOption
import echarts.options.TooltipPosition
import echarts.options.VisualMapOption
import echarts.options.one
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.js
import demo.examples.Category
import demo.examples.gauge.center

private val calendarVerticalCategory = Category(Groups.CALENDAR, "examples/calendar/CalendarVertical.kt")

val calendarVerticalEntry: CatalogEntry =
    calendarVerticalCategory.chart("calendar-vertical", "纵向日历图", "Calendar Heatmap Vertical") {
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
        }
