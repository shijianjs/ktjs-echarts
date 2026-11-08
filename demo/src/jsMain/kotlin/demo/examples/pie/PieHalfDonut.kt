package demo.examples.pie

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.pieSeries
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.sunburst.radius

private val pieHalfDonutCategory = Category(Groups.PIE, "examples/pie/PieHalfDonut.kt")

val pieHalfDonutEntry: CatalogEntry =
    pieHalfDonutCategory.chart("pie-half-donut", "半环形图", "Half Doughnut Chart") {
            unsafeJso<EChartsOption> {
                tooltip = tooltipItem
                legend = centerLegend
                series =
                    arrayOf<SeriesOption>(
                        pieSeries {
                            name = "Access From"
                            radius = doughnutRadius
                            center = arrayOf(StringOrDouble("50%"), StringOrDouble("70%"))
                            startAngle = 180.0
                            endAngle = 360.0
                            typedData = accessFrom()
                        },
                    )
            }
        }
