package demo.examples.pie

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.RoseTypes
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.ItemStyleOption
import io.github.shijianjs.ktjs.echarts.options.LegendOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.StringOrDoubleOrArray
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.pieSeries
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.sunburst.radius

private val pieRoseTypeSimpleCategory = Category(Groups.PIE, "examples/pie/PieRoseTypeSimple.kt")

val pieRoseTypeSimpleEntry: CatalogEntry =
    pieRoseTypeSimpleCategory.chart("pie-roseType-simple", "基础南丁格尔玫瑰图", "Nightingale Chart") {
            unsafeJso<EChartsOption> {
                legend = one(unsafeJso<LegendOption> { top = StringOrDouble("bottom") })
                series =
                    arrayOf<SeriesOption>(
                        pieSeries {
                            name = "Nightingale Chart"
                            radius = StringOrDoubleOrArray(arrayOf(StringOrDouble(50.0), StringOrDouble(250.0)))
                            center = arrayOf(StringOrDouble("50%"), StringOrDouble("50%"))
                            roseType = RoseTypes.area
                            itemStyle = unsafeJso<ItemStyleOption> { borderRadius = StringOrDoubleOrArray(8.0) }
                            typedData = roseData(40.0, 38.0, 32.0, 30.0, 28.0, 26.0, 22.0, 18.0)
                        },
                    )
            }
        }
