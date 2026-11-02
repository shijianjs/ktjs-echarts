package demo.examples.pie

import demo.CatalogEntry
import demo.Groups
import echarts.RoseTypes
import echarts.options.EChartsOption
import echarts.options.EmphasisOption
import echarts.options.ItemStyleOption
import echarts.options.LabelOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.StringOrDoubleOrArray
import echarts.options.TitleOption
import echarts.options.one
import echarts.options.pieSeries
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.sunburst.radius

private val pieRoseTypeCategory = Category(Groups.PIE, "examples/pie/PieRoseType.kt")

val pieRoseTypeEntry: CatalogEntry =
    pieRoseTypeCategory.chart("pie-roseType", "南丁格尔玫瑰图", "Nightingale Chart") {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Nightingale Chart"
                            subtext = "Fake Data"
                            left = StringOrDouble("center")
                        },
                    )
                tooltip = one(tooltipWithPercent())
                legend = legendOf("rose1", "rose2", "rose3", "rose4", "rose5", "rose6", "rose7", "rose8")
                series =
                    arrayOf<SeriesOption>(
                        pieSeries {
                            name = "Radius Mode"
                            radius = StringOrDoubleOrArray(arrayOf(StringOrDouble(20.0), StringOrDouble(140.0)))
                            center = arrayOf(StringOrDouble("25%"), StringOrDouble("50%"))
                            roseType = RoseTypes.radius
                            itemStyle = unsafeJso<ItemStyleOption> { borderRadius = StringOrDoubleOrArray(5.0) }
                            label = unsafeJso<LabelOption> { show = false }
                            emphasis = unsafeJso<EmphasisOption> { label = unsafeJso<LabelOption> { show = true } }
                            typedData = roseData(40.0, 33.0, 28.0, 22.0, 20.0, 15.0, 12.0, 10.0)
                        },
                        pieSeries {
                            name = "Area Mode"
                            radius = StringOrDoubleOrArray(arrayOf(StringOrDouble(20.0), StringOrDouble(140.0)))
                            center = arrayOf(StringOrDouble("75%"), StringOrDouble("50%"))
                            roseType = RoseTypes.area
                            itemStyle = unsafeJso<ItemStyleOption> { borderRadius = StringOrDoubleOrArray(5.0) }
                            typedData = roseData(30.0, 28.0, 26.0, 24.0, 22.0, 20.0, 18.0, 16.0)
                        },
                    )
            }
        }
