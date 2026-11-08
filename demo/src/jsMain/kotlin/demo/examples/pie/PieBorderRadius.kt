package demo.examples.pie

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.ItemStyleOption
import io.github.shijianjs.ktjs.echarts.options.LabelLineOption
import io.github.shijianjs.ktjs.echarts.options.LabelOption
import io.github.shijianjs.ktjs.echarts.options.LabelPosition
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDoubleOrArray
import io.github.shijianjs.ktjs.echarts.options.pieSeries
import js.objects.unsafeJso
import io.github.shijianjs.ktjs.zrender.ZRColor
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
