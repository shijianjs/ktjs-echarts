package demo.examples.pie

import demo.CatalogEntry
import demo.Groups
import echarts.Orients
import echarts.options.EChartsOption
import echarts.options.EmphasisOption
import echarts.options.ItemStyleOption
import echarts.options.LegendOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.StringOrDoubleOrArray
import echarts.options.TitleOption
import echarts.options.one
import echarts.options.pieSeries
import js.objects.unsafeJso
import zrender.ZRColor
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.sunburst.radius

private val pieSimpleCategory = Category(Groups.PIE, "examples/pie/PieSimple.kt")

val pieSimpleEntry: CatalogEntry =
    pieSimpleCategory.chart("pie-simple", "某站点用户访问来源", "Referer of a Website") {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Referer of a Website"
                            subtext = "Fake Data"
                            left = StringOrDouble("center")
                        },
                    )
                tooltip = tooltipItem
                legend = one(unsafeJso<LegendOption> { orient = Orients.vertical; left = StringOrDouble("left") })
                series =
                    arrayOf<SeriesOption>(
                        pieSeries {
                            name = "Access From"
                            radius = StringOrDoubleOrArray("50%")
                            typedData = accessFrom()
                            emphasis =
                                unsafeJso<EmphasisOption> {
                                    itemStyle =
                                        unsafeJso<ItemStyleOption> {
                                            shadowBlur = 10.0
                                            shadowOffsetX = 0.0
                                            shadowColor = ZRColor("rgba(0, 0, 0, 0.5)")
                                        }
                                }
                        },
                    )
            }
        }
