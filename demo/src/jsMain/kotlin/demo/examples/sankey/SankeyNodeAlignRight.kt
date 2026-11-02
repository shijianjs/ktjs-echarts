package demo.examples.sankey

import demo.CatalogEntry
import demo.Groups
import echarts.NodeAligns
import echarts.options.EChartsOption
import echarts.EmphasisFocuses
import echarts.options.EmphasisOption
import echarts.options.SankeyEdgeColor
import echarts.options.SankeyEdgeStyleOption
import echarts.options.SeriesOption
import echarts.options.sankeySeries
import js.objects.unsafeJso
import demo.examples.Category

private val sankeyNodeAlignRightCategory = Category(Groups.SANKEY, "examples/sankey/SankeyNodeAlignRight.kt")

val sankeyNodeAlignRightEntry: CatalogEntry =
    sankeyNodeAlignRightCategory.interactive("sankey-nodeAlign-right", "桑基图右对齐布局", "Node Align Right in Sankey") { chart ->
            loadSankeyTable("data/energy.json") { table ->
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        title = sankeyTitle("Node Align Right")
                        tooltip = sankeyTooltip
                        animation = false
                        series =
                            arrayOf<SeriesOption>(
                                sankeySeries {
                                    emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.adjacency }
                                    nodeAlign = NodeAligns.right
                                    typedData = table.nodes
                                    links = table.links
                                    lineStyle =
                                        unsafeJso<SankeyEdgeStyleOption> {
                                            typedColor = SankeyEdgeColor("source")
                                            curveness = 0.5
                                        }
                                },
                            )
                    },
                )
            }
        }
