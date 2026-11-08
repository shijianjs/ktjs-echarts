package demo.examples.calendar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.CalendarYearLabelOption
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.ItemStyleOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.TitleOption
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.one
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.js
import demo.examples.Category
import demo.examples.gauge.center

private val calendarHeatmapCategory = Category(Groups.CALENDAR, "examples/calendar/CalendarHeatmap.kt")

val calendarHeatmapEntry: CatalogEntry =
    calendarHeatmapCategory.chart("calendar-heatmap", "日历热力图", "Calendar Heatmap") {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<io.github.shijianjs.ktjs.echarts.options.TitleOption> {
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
        }
