package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.AxisTypes
import io.github.shijianjs.ktjs.echarts.TooltipTriggers
import io.github.shijianjs.ktjs.echarts.options.AxisLabelOption
import io.github.shijianjs.ktjs.echarts.options.BooleanOrDouble
import io.github.shijianjs.ktjs.echarts.options.BooleanOrStringArray
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.GridOption
import io.github.shijianjs.ktjs.echarts.options.LegendOption
import io.github.shijianjs.ktjs.echarts.options.LineStyleOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.SymbolShape
import io.github.shijianjs.ktjs.echarts.options.SymbolSize
import io.github.shijianjs.ktjs.echarts.options.TooltipFormatter
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.XAxisOption
import io.github.shijianjs.ktjs.echarts.options.YAxisOption
import io.github.shijianjs.ktjs.echarts.options.lineSeries
import io.github.shijianjs.ktjs.echarts.options.numbers
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.strings
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
                            axisLabel = unsafeJso<AxisLabelOption> { formatter = io.github.shijianjs.ktjs.echarts.options.LabelFormatter("{value} °C") }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.category
                            axisLine = unsafeJso { onZero = io.github.shijianjs.ktjs.echarts.options.BooleanOrString(false) }
                            axisLabel = unsafeJso<AxisLabelOption> { formatter = io.github.shijianjs.ktjs.echarts.options.LabelFormatter("{value} km") }
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
                                    shadowColor = io.github.shijianjs.ktjs.zrender.ZRColor("rgba(0,0,0,0.3)")
                                    shadowBlur = 10.0
                                    shadowOffsetY = 8.0
                                }
                            typedData = numbers(15.0, -50.0, -56.5, -46.5, -22.1, -2.5, -27.7, -55.7, -76.5)
                        },
                    )
            }
        }
