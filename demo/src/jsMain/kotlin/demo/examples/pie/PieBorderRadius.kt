package demo.examples.pie

import demo.CatalogEntry
import demo.Groups
import echarts.options.EChartsOption
import echarts.options.ItemStyleOption
import echarts.options.LabelLineOption
import echarts.options.LabelOption
import echarts.options.LabelPosition
import echarts.options.SeriesOption
import echarts.options.StringOrDoubleOrArray
import echarts.options.pieSeries
import js.objects.unsafeJso
import zrender.ZRColor
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.sunburst.radius

private val pieBorderRadiusCategory = Category(Groups.PIE, "examples/pie/PieBorderRadius.kt")

val pieBorderRadiusEntry: CatalogEntry =
    pieBorderRadiusCategory.chart("pie-borderRadius", "圆角环形图", "Doughnut Chart with Rounded Corner") {
            unsafeJso<EChartsOption> {
                tooltip = tooltipItem
                legend = centerLegend
                series =
                    arrayOf<SeriesOption>(
                        pieSeries {
                            name = "Access From"
                            radius = doughnutRadius
                            avoidLabelOverlap = false
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    borderRadius = StringOrDoubleOrArray(10.0)
                                    borderColor = ZRColor("#fff")
                                    borderWidth = 2.0
                                }
                            label = unsafeJso<LabelOption> { show = false; position = LabelPosition("center") }
                            emphasis = centerLabelEmphasis()
                            labelLine = unsafeJso<LabelLineOption> { show = false }
                            typedData = accessFrom()
                        },
                    )
            }
        }
