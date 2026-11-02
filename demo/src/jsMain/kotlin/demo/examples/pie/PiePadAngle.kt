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
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.sunburst.radius

private val piePadAngleCategory = Category(Groups.PIE, "examples/pie/PiePadAngle.kt")

val piePadAngleEntry: CatalogEntry =
    piePadAngleCategory.chart("pie-padAngle", "饼图扇区间隙", "Pie with padAngle") {
            unsafeJso<EChartsOption> {
                tooltip = tooltipItem
                legend = centerLegend
                series =
                    arrayOf<SeriesOption>(
                        pieSeries {
                            name = "Access From"
                            radius = doughnutRadius
                            avoidLabelOverlap = false
                            padAngle = 5.0
                            itemStyle = unsafeJso<ItemStyleOption> { borderRadius = StringOrDoubleOrArray(10.0) }
                            label = unsafeJso<LabelOption> { show = false; position = LabelPosition("center") }
                            emphasis = centerLabelEmphasis()
                            labelLine = unsafeJso<LabelLineOption> { show = false }
                            typedData = accessFrom()
                        },
                    )
            }
        }
