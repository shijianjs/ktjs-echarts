package demo.examples.custom

import demo.CatalogEntry
import demo.Groups
import echarts.Orients
import echarts.options.CalendarDayLabelOption
import echarts.options.CalendarMonthLabelOption
import echarts.options.CalendarOption
import echarts.options.CalendarRange
import echarts.options.CalendarYearLabelOption
import echarts.options.DataZoomValue
import echarts.options.DimensionDefinition
import echarts.options.DimensionDefinitionLoose
import echarts.options.EChartsOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.StringOrDoubleOrArray
import echarts.options.StringOrStringArray
import echarts.options.TooltipOption
import echarts.options.customSeries
import echarts.options.one
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import demo.examples.Category
import demo.examples.calendar.cellSize
import demo.examples.gauge.center
import demo.examples.matrix.cell
import demo.examples.shared.fixed

private val customCalendarIconCategory = Category(Groups.CUSTOM, "examples/custom/CustomCalendarIcon.kt")

val customCalendarIconEntry: CatalogEntry =
    customCalendarIconCategory.chart(
            "custom-calendar-icon",
            "日历图自定义图标",
            "Custom Calendar Icon",
            summaryZh = "日历的每个格子按当天的“事件数”摆放 1～4 个 SVG 图标，图标位置来自固定的布局表。",
            summaryEn = "Each calendar cell places one to four SVG icons according to the event count of that day, at the offsets of a fixed layout table.",
        ) {
            unsafeJso<EChartsOption> {
                tooltip = one(unsafeJso<TooltipOption> {})
                calendar =
                    one(
                        unsafeJso<CalendarOption> {
                            left = StringOrDouble("center")
                            top = StringOrDouble("middle")
                            cellSize =
                                StringOrDoubleOrArray(
                                    arrayOf(StringOrDouble(70.0), StringOrDouble(70.0)),
                                )
                            yearLabel = unsafeJso<CalendarYearLabelOption> { show = false }
                            orient = Orients.vertical
                            dayLabel =
                                unsafeJso<CalendarDayLabelOption> {
                                    firstDay = 1.0
                                    nameMap = StringOrStringArray("cn")
                                }
                            monthLabel = unsafeJso<CalendarMonthLabelOption> { show = false }
                            range = CalendarRange(DataZoomValue("2017-03"))
                        },
                    )
                series =
                    one<SeriesOption>(
                        customSeries {
                            coordinateSystem = "calendar"
                            renderItem = { itemParams, api -> calendarIconItem(itemParams, api) }
                            dimensions =
                                arrayOf<DimensionDefinitionLoose?>(
                                    null,
                                    DimensionDefinitionLoose(unsafeJso<DimensionDefinition> { type = "ordinal" }),
                                ).unsafeCast<Array<DimensionDefinitionLoose>>()
                            typedData = marchIconData()
                        },
                    )
            }
        }
