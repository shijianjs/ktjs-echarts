package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.AxisPointerTypes
import io.github.shijianjs.ktjs.echarts.AxisTypes
import io.github.shijianjs.ktjs.echarts.TooltipTriggers
import io.github.shijianjs.ktjs.echarts.options.AxisPointerOption
import io.github.shijianjs.ktjs.echarts.options.BooleanOrString
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.GridOption
import io.github.shijianjs.ktjs.echarts.options.LegendOption
import io.github.shijianjs.ktjs.echarts.options.LineStyleOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.XAxisOption
import io.github.shijianjs.ktjs.echarts.options.YAxisOption
import io.github.shijianjs.ktjs.echarts.options.barSeries
import io.github.shijianjs.ktjs.echarts.options.lineSeries
import io.github.shijianjs.ktjs.echarts.options.numbers
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.strings
import js.objects.unsafeJso
import kotlin.js.js
import io.github.shijianjs.ktjs.zrender.ZRColor
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
                            position = io.github.shijianjs.ktjs.echarts.AxisPositions.right
                            alignTicks = true
                            axisLine = unsafeJso { show = io.github.shijianjs.ktjs.echarts.options.BooleanOrString(true); lineStyle = unsafeJso<LineStyleOption> { color = ZRColor(colours[0]) } }
                            axisLabel = unsafeJso { formatter = io.github.shijianjs.ktjs.echarts.options.LabelFormatter("{value} ml") }
                        },
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            name = "Precipitation"
                            position = io.github.shijianjs.ktjs.echarts.AxisPositions.right
                            alignTicks = true
                            offset = 80.0
                            axisLine = unsafeJso { show = io.github.shijianjs.ktjs.echarts.options.BooleanOrString(true); lineStyle = unsafeJso<LineStyleOption> { color = ZRColor(colours[1]) } }
                            axisLabel = unsafeJso { formatter = io.github.shijianjs.ktjs.echarts.options.LabelFormatter("{value} ml") }
                        },
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            name = "温度"
                            position = io.github.shijianjs.ktjs.echarts.AxisPositions.left
                            alignTicks = true
                            axisLine = unsafeJso { show = io.github.shijianjs.ktjs.echarts.options.BooleanOrString(true); lineStyle = unsafeJso<LineStyleOption> { color = ZRColor(colours[2]) } }
                            axisLabel = unsafeJso { formatter = io.github.shijianjs.ktjs.echarts.options.LabelFormatter("{value} °C") }
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
