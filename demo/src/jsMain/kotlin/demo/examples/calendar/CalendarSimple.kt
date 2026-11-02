package demo.examples.calendar

import demo.CatalogEntry
import demo.Groups
import echarts.options.EChartsOption
import echarts.options.SeriesOption
import echarts.options.VisualMapOption
import echarts.options.one
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.js
import demo.examples.Category

private val calendarSimpleCategory = Category(Groups.CALENDAR, "examples/calendar/CalendarSimple.kt")

val calendarSimpleEntry: CatalogEntry =
    calendarSimpleCategory.chart("calendar-simple", "基础日历图", "Simple Calendar") {
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
        }
