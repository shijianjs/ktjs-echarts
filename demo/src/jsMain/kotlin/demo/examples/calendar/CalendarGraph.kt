package demo.examples.calendar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.Orients
import io.github.shijianjs.ktjs.echarts.options.CalendarRange
import io.github.shijianjs.ktjs.echarts.options.CalendarYearLabelOption
import io.github.shijianjs.ktjs.echarts.options.DataZoomValue
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.ItemStyleOption
import io.github.shijianjs.ktjs.echarts.options.LineStyleOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.StringOrDoubleOrArray
import io.github.shijianjs.ktjs.echarts.options.StringOrStringArray
import io.github.shijianjs.ktjs.echarts.options.SymbolSize
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.VisualOptionUnit
import io.github.shijianjs.ktjs.echarts.options.graphSeries
import io.github.shijianjs.ktjs.echarts.options.one
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.js
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.themeriver.days

private val calendarGraphCategory = Category(Groups.CALENDAR, "examples/calendar/CalendarGraph.kt")

val calendarGraphEntry: CatalogEntry =
    calendarGraphCategory.chart("calendar-graph", "日历关系图", "Calendar Graph") {
            // The seven days of the graph, and the chain that links them.
            val days =
                listOf(
                    "2017-02-01" to 260.0,
                    "2017-02-04" to 200.0,
                    "2017-02-09" to 279.0,
                    "2017-02-13" to 847.0,
                    "2017-02-18" to 241.0,
                    "2017-02-23" to 411.0,
                    "2017-03-14" to 985.0,
                )
            unsafeJso<EChartsOption> {
                tooltip = one(unsafeJso<TooltipOption> {})
                calendar =
                    one(
                        calendarOption("2017-02") {
                            range = CalendarRange(arrayOf(DataZoomValue("2017-02"), DataZoomValue("2017-03-31")))
                            top = StringOrDouble("middle")
                            left = StringOrDouble("center")
                            orient = Orients.vertical
                            cellSize = StringOrDoubleOrArray(40.0)
                            yearLabel = unsafeJso<CalendarYearLabelOption> { margin = 50.0; fontSize = StringOrDouble(30.0) }
                            dayLabel = chineseDayLabel()
                            monthLabel =
                                unsafeJso {
                                    nameMap = StringOrStringArray("cn")
                                    margin = 15.0
                                    fontSize = StringOrDouble(20.0)
                                    color = "#999"
                                }
                        },
                    )
                visualMap =
                    piecewiseVisualMap {
                        min = 0.0
                        max = 1000.0
                        left = StringOrDouble("center")
                        bottom = StringOrDouble(20.0)
                        seriesIndex = io.github.shijianjs.ktjs.echarts.options.IndexOrIndexArray(1.0)
                        inRange =
                            unsafeJso<VisualOptionUnit> {
                                color = StringOrStringArray(arrayOf("#5291FF", "#C7DBFF"))
                            }
                    }
                series =
                    arrayOf<SeriesOption>(
                        graphSeries {
                            coordinateSystem = "calendar"
                            edgeSymbol = StringOrStringArray(arrayOf("none", "arrow"))
                            links = calendarGraphLinks(days.size)
                            symbolSize = SymbolSize(15.0)
                            calendarIndex = 0.0
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    color = ZRColor("yellow")
                                    shadowBlur = 9.0
                                    shadowOffsetX = 1.5
                                    shadowOffsetY = 3.0
                                    shadowColor = ZRColor("#555")
                                }
                            lineStyle =
                                unsafeJso<LineStyleOption> {
                                    color = ZRColor("#D10E00")
                                    width = 1.0
                                    opacity = 1.0
                                }
                            typedData = calendarGraphNodes(days)
                            z = 20.0
                        },
                        calendarHeatmap(yearValues("2017", 1000.0, 20170401L)),
                    )
            }
        }
