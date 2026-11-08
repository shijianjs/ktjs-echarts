package demo.examples.sankey

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.NodeAligns
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.EmphasisFocuses
import io.github.shijianjs.ktjs.echarts.options.EmphasisOption
import io.github.shijianjs.ktjs.echarts.options.SankeyEdgeColor
import io.github.shijianjs.ktjs.echarts.options.SankeyEdgeStyleOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.sankeySeries
import js.objects.unsafeJso
import demo.examples.Category

private val sankeyNodeAlignLeftCategory = Category(Groups.SANKEY, "examples/sankey/SankeyNodeAlignLeft.kt")

val sankeyNodeAlignLeftEntry: CatalogEntry =
    sankeyNodeAlignLeftCategory.interactive("sankey-nodeAlign-left", "桑基图左对齐布局", "Node Align Left in Sankey") { chart ->
            loadSankeyTable("data/energy.json") { table ->
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        title = sankeyTitle("Node Align Left")
                        tooltip = sankeyTooltip
                        series =
                            arrayOf<SeriesOption>(
                                sankeySeries {
                                    emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.adjacency }
                                    nodeAlign = NodeAligns.left
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
