package demo.examples.calendar

import demo.CatalogEntry
import demo.Groups
import echarts.TooltipTriggers
import echarts.options.EChartsOption
import echarts.options.LabelOption
import echarts.options.LegendOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.TitleOption
import echarts.options.TooltipOption
import echarts.options.one
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.js
import zrender.ZRColor
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.shared.legendNames
import demo.examples.themeriver.days

private val calendarEffectscatterCategory = Category(Groups.CALENDAR, "examples/calendar/CalendarEffectscatter.kt")

val calendarEffectscatterEntry: CatalogEntry =
    calendarEffectscatterCategory.chart("calendar-effectscatter", "热力特效散点图", "Calendar EffectScatter") {
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
        }
