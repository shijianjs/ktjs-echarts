package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import echarts.AxisPointerTypes
import echarts.TooltipTriggers
import echarts.options.AxisPointerOption
import echarts.options.AxisPointerLabelOption
import echarts.options.EChartsOption
import echarts.options.LegendOption
import echarts.options.SeriesOption
import echarts.options.TitleOption
import echarts.options.TooltipOption
import echarts.options.one
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.shared.legendNames
import demo.examples.shared.toolbox

private val areaStackCategory = Category(Groups.LINE, "examples/line/AreaStack.kt")

val areaStackEntry: CatalogEntry =
    areaStackCategory.chart("area-stack", "堆叠面积图", "Stacked Area Chart") {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Stacked Area Chart" })
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer =
                                unsafeJso<AxisPointerOption> {
                                    type = AxisPointerTypes.cross
                                    label = unsafeJso<AxisPointerLabelOption> { backgroundColor = zrender.ZRColor("#6a7985") }
                                }
                        },
                    )
                legend = one(unsafeJso<LegendOption> { data = legendNames("Email", "Union Ads", "Video Ads", "Direct", "Search Engine") })
                toolbox = toolbox(saveAsImage = true)
                xAxis = categoryAxis(*week, boundaryGap = false)
                yAxis = valueAxis
                series =
                    arrayOf<SeriesOption>(
                        stackedSeries("Email", doubleArrayOf(120.0, 132.0, 101.0, 134.0, 90.0, 230.0, 210.0), area = true),
                        stackedSeries("Union Ads", doubleArrayOf(220.0, 182.0, 191.0, 234.0, 290.0, 330.0, 310.0), area = true),
                        stackedSeries("Video Ads", doubleArrayOf(150.0, 232.0, 201.0, 154.0, 190.0, 330.0, 410.0), area = true),
                        stackedSeries("Direct", doubleArrayOf(320.0, 332.0, 301.0, 334.0, 390.0, 330.0, 320.0), area = true),
                        stackedSeries("Search Engine", doubleArrayOf(820.0, 932.0, 901.0, 934.0, 1290.0, 1330.0, 1320.0), area = true, label = true),
                    )
            }
        }
