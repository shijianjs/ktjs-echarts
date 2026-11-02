package demo.examples.calendar

import demo.CatalogEntry
import demo.Groups
import echarts.Orients
import echarts.options.EChartsOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.TooltipOption
import echarts.options.TooltipPosition
import echarts.options.VisualMapOption
import echarts.options.one
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.js
import demo.examples.Category
import demo.examples.gauge.center

private val calendarHorizontalCategory = Category(Groups.CALENDAR, "examples/calendar/CalendarHorizontal.kt")

val calendarHorizontalEntry: CatalogEntry =
    calendarHorizontalCategory.chart("calendar-horizontal", "横向日历图", "Calendar Heatmap Horizontal") {
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
        }
