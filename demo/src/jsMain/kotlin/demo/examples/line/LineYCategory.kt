package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import echarts.AxisTypes
import echarts.TooltipTriggers
import echarts.options.AxisLabelOption
import echarts.options.BooleanOrDouble
import echarts.options.BooleanOrStringArray
import echarts.options.EChartsOption
import echarts.options.GridOption
import echarts.options.LegendOption
import echarts.options.LineStyleOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.SymbolShape
import echarts.options.SymbolSize
import echarts.options.TooltipFormatter
import echarts.options.TooltipOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.lineSeries
import echarts.options.numbers
import echarts.options.one
import echarts.options.strings
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.shared.axisLabel
import demo.examples.shared.legendNames

private val lineYCategoryCategory = Category(Groups.LINE, "examples/line/LineYCategory.kt")

val lineYCategoryEntry: CatalogEntry =
    lineYCategoryCategory.chart("line-y-category", "垂直折线图（Y轴为类目轴）", "Line Y Category") {
            unsafeJso<EChartsOption> {
                legend = one(unsafeJso<LegendOption> { data = legendNames("Altitude (km) vs. temperature (°C)") })
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            formatter = TooltipFormatter("Temperature : <br/>{b}km : {c}°C")
                        },
                    )
                grid = one(unsafeJso<GridOption> { left = StringOrDouble("3%"); right = StringOrDouble("4%"); bottom = StringOrDouble("3%"); containLabel = true })
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.value
                            axisLabel = unsafeJso<AxisLabelOption> { formatter = echarts.options.LabelFormatter("{value} °C") }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.category
                            axisLine = unsafeJso { onZero = echarts.options.BooleanOrString(false) }
                            axisLabel = unsafeJso<AxisLabelOption> { formatter = echarts.options.LabelFormatter("{value} km") }
                            boundaryGap = BooleanOrStringArray(false)
                            data = strings("0", "10", "20", "30", "40", "50", "60", "70", "80")
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        lineSeries {
                            name = "Altitude (km) vs. temperature (°C)"
                            symbolSize = SymbolSize(10.0)
                            symbol = SymbolShape("circle")
                            smooth = BooleanOrDouble(true)
                            lineStyle =
                                unsafeJso<LineStyleOption> {
                                    width = 3.0
                                    shadowColor = zrender.ZRColor("rgba(0,0,0,0.3)")
                                    shadowBlur = 10.0
                                    shadowOffsetY = 8.0
                                }
                            typedData = numbers(15.0, -50.0, -56.5, -46.5, -22.1, -2.5, -27.7, -55.7, -76.5)
                        },
                    )
            }
        }
