package demo.examples.pie

import demo.CatalogEntry
import demo.Groups
import echarts.options.EChartsOption
import echarts.options.LabelLineOption
import echarts.options.LabelOption
import echarts.options.LabelPosition
import echarts.options.SeriesOption
import echarts.options.pieSeries
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.sunburst.radius

private val pieDoughnutCategory = Category(Groups.PIE, "examples/pie/PieDoughnut.kt")

val pieDoughnutEntry: CatalogEntry =
    pieDoughnutCategory.chart("pie-doughnut", "环形图", "Doughnut Chart") {
            unsafeJso<EChartsOption> {
                tooltip = tooltipItem
                legend = centerLegend
                series =
                    arrayOf<SeriesOption>(
                        pieSeries {
                            name = "Access From"
                            radius = doughnutRadius
                            avoidLabelOverlap = false
                            label = unsafeJso<LabelOption> { show = false; position = LabelPosition("center") }
                            emphasis = centerLabelEmphasis()
                            labelLine = unsafeJso<LabelLineOption> { show = false }
                            typedData = accessFrom()
                        },
                    )
            }
        }
