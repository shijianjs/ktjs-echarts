package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import echarts.AxisPointerTypes
import echarts.AxisTypes
import echarts.TooltipTriggers
import echarts.options.AxisPointerOption
import echarts.options.EChartsOption
import echarts.options.LegendOption
import echarts.options.LineStyleOption
import echarts.options.SeriesOption
import echarts.options.TooltipOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.barSeries
import echarts.options.lineSeries
import echarts.options.numbers
import echarts.options.one
import echarts.options.strings
import js.objects.unsafeJso
import kotlin.js.js
import zrender.ZRColor
import demo.examples.Category
import demo.examples.shared.axisLabel
import demo.examples.shared.legendNames
import demo.examples.shared.toolbox

private val mixLineBarCategory = Category(Groups.BAR, "examples/bar/MixLineBar.kt")

val mixLineBarEntry: CatalogEntry =
    mixLineBarCategory.chart("mix-line-bar", "折柱混合", "Mixed Line and Bar") {
            unsafeJso<EChartsOption> {
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer =
                                unsafeJso<AxisPointerOption> {
                                    type = AxisPointerTypes.cross
                                    crossStyle = unsafeJso<LineStyleOption> { color = ZRColor("#999") }
                                }
                        },
                    )
                toolbox = toolbox(dataView = true, magicType = true, restore = true, saveAsImage = true)
                legend = one(unsafeJso<LegendOption> { data = legendNames("Evaporation", "Precipitation", "Temperature") })
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            data = strings(*week)
                            axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.shadow }
                        },
                    )
                yAxis =
                    arrayOf(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            name = "Precipitation"
                            min = echarts.options.AxisMinMax(0.0)
                            max = echarts.options.AxisMinMax(250.0)
                            interval = echarts.options.AxisInterval(50.0)
                            axisLabel = unsafeJso { formatter = echarts.options.LabelFormatter("{value} ml") }
                        },
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            name = "Temperature"
                            min = echarts.options.AxisMinMax(0.0)
                            max = echarts.options.AxisMinMax(25.0)
                            interval = echarts.options.AxisInterval(5.0)
                            axisLabel = unsafeJso { formatter = echarts.options.LabelFormatter("{value} °C") }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        barSeries { name = "Evaporation"; typedData = numbers(2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3) },
                        barSeries { name = "Precipitation"; typedData = numbers(2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3) },
                        lineSeries {
                            name = "Temperature"
                            yAxisIndex = 1.0
                            typedData = numbers(2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2)
                        },
                    )
            }
        }
