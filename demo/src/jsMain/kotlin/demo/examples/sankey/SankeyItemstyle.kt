package demo.examples.sankey

import demo.CatalogEntry
import demo.Groups
import echarts.options.EChartsOption
import echarts.options.ItemStyleOption
import echarts.options.LabelOption
import echarts.options.OptionId
import echarts.options.SankeyEdgeColor
import echarts.options.SankeyEdgeStyleOption
import echarts.options.SankeyNodeItemOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.TitleOption
import echarts.options.one
import echarts.options.sankeySeries
import js.objects.unsafeJso
import zrender.ZRColor
import demo.examples.Category
import demo.examples.gauge.center

private val sankeyItemstyleCategory = Category(Groups.SANKEY, "examples/sankey/SankeyItemstyle.kt")

val sankeyItemstyleEntry: CatalogEntry =
    sankeyItemstyleCategory.chart("sankey-itemstyle", "桑基图节点自定义样式", "Specify ItemStyle for Each Node in Sankey") {
            unsafeJso<EChartsOption> {
                backgroundColor = ZRColor("#fff")
                title =
                    one(
                        unsafeJso<TitleOption> {
                            subtext = "Data From lisachristina1234 on GitHub"
                            left = StringOrDouble("center")
                        },
                    )
                tooltip = sankeyTooltip
                series =
                    arrayOf<SeriesOption>(
                        sankeySeries {
                            left = StringOrDouble(50.0)
                            top = StringOrDouble(20.0)
                            right = StringOrDouble(150.0)
                            bottom = StringOrDouble(25.0)
                            typedData =
                                arrayOf(
                                    *Array<SankeyNodeItemOption?>(sankeyItemStyleNodes.size) { index ->
                                        val (name, color) = sankeyItemStyleNodes[index]
                                        unsafeJso<SankeyNodeItemOption> {
                                            this.name = OptionId(name)
                                            itemStyle =
                                                unsafeJso<ItemStyleOption> {
                                                    this.color = ZRColor(color)
                                                    borderColor = ZRColor(color)
                                                }
                                        }
                                    },
                                )
                            links =
                                Array(sankeyItemStyleLinks.size) { index ->
                                    val (source, target, value) = sankeyItemStyleLinks[index]
                                    link(source, target, value)
                                }
                            lineStyle =
                                unsafeJso<SankeyEdgeStyleOption> {
                                    typedColor = SankeyEdgeColor("source")
                                    curveness = 0.5
                                }
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    color = ZRColor("#1f77b4")
                                    borderColor = ZRColor("#1f77b4")
                                }
                            label =
                                unsafeJso<LabelOption> {
                                    color = "rgba(0,0,0,0.7)"
                                    fontFamily = "Arial"
                                    fontSize = StringOrDouble(10.0)
                                }
                        },
                    )
            }
        }
