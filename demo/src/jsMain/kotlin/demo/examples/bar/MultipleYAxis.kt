package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import echarts.AxisPointerTypes
import echarts.AxisTypes
import echarts.TooltipTriggers
import echarts.options.AxisPointerOption
import echarts.options.BooleanOrString
import echarts.options.EChartsOption
import echarts.options.GridOption
import echarts.options.LegendOption
import echarts.options.LineStyleOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
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

private val multipleYAxisCategory = Category(Groups.BAR, "examples/bar/MultipleYAxis.kt")

val multipleYAxisEntry: CatalogEntry =
    multipleYAxisCategory.chart("multiple-y-axis", "多 Y 轴示例", "Multiple Y Axes") {
            val colours = arrayOf("#5070dd", "#b6d634", "#505372")
            unsafeJso<EChartsOption> {
                color = Array(colours.size) { ZRColor(colours[it]) }
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.cross }
                        },
                    )
                grid = one(unsafeJso<GridOption> { right = StringOrDouble("20%") })
                toolbox = toolbox(dataView = true, restore = true, saveAsImage = true)
                legend = one(unsafeJso<LegendOption> { data = legendNames("Evaporation", "Precipitation", "Temperature") })
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            axisTick = unsafeJso { alignWithLabel = true }
                            data =
                                strings(
                                    "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
                                )
                        },
                    )
                yAxis =
                    arrayOf(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            name = "Evaporation"
                            position = echarts.AxisPositions.right
                            alignTicks = true
                            axisLine = unsafeJso { show = echarts.options.BooleanOrString(true); lineStyle = unsafeJso<LineStyleOption> { color = ZRColor(colours[0]) } }
                            axisLabel = unsafeJso { formatter = echarts.options.LabelFormatter("{value} ml") }
                        },
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            name = "Precipitation"
                            position = echarts.AxisPositions.right
                            alignTicks = true
                            offset = 80.0
                            axisLine = unsafeJso { show = echarts.options.BooleanOrString(true); lineStyle = unsafeJso<LineStyleOption> { color = ZRColor(colours[1]) } }
                            axisLabel = unsafeJso { formatter = echarts.options.LabelFormatter("{value} ml") }
                        },
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            name = "温度"
                            position = echarts.AxisPositions.left
                            alignTicks = true
                            axisLine = unsafeJso { show = echarts.options.BooleanOrString(true); lineStyle = unsafeJso<LineStyleOption> { color = ZRColor(colours[2]) } }
                            axisLabel = unsafeJso { formatter = echarts.options.LabelFormatter("{value} °C") }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        barSeries { name = "Evaporation"; typedData = numbers(2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3) },
                        barSeries { name = "Precipitation"; yAxisIndex = 1.0; typedData = numbers(2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3) },
                        lineSeries { name = "Temperature"; yAxisIndex = 2.0; typedData = numbers(2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2) },
                    )
            }
        }
