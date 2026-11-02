package demo.examples.pie

import demo.CatalogEntry
import demo.Groups
import echarts.RoseTypes
import echarts.options.EChartsOption
import echarts.options.ItemStyleOption
import echarts.options.LegendOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.StringOrDoubleOrArray
import echarts.options.one
import echarts.options.pieSeries
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
