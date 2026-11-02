package demo.examples.sankey

import demo.CatalogEntry
import demo.Groups
import echarts.options.EChartsOption
import echarts.EmphasisFocuses
import echarts.options.EmphasisOption
import echarts.options.ItemStyleOption
import echarts.options.LineStyleOption
import echarts.options.SankeyEdgeColor
import echarts.options.SankeyEdgeStyleOption
import echarts.options.SankeyLevelOption
import echarts.options.SeriesOption
import echarts.options.sankeySeries
import js.objects.unsafeJso
import zrender.ZRColor
import demo.examples.Category

private val sankeyLevelsCategory = Category(Groups.SANKEY, "examples/sankey/SankeyLevels.kt")

val sankeyLevelsEntry: CatalogEntry =
    sankeyLevelsCategory.interactive("sankey-levels", "桑基图层级自定义样式", "Sankey with Levels Setting") { chart ->
            loadSankeyTable("data/product.json") { table ->
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        title = sankeyTitle("Sankey Diagram")
                        tooltip = sankeyTooltip
                        series =
                            arrayOf<SeriesOption>(
                                sankeySeries {
                                    typedData = table.nodes
                                    links = table.links
                                    emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.adjacency }
                                    levels =
                                        Array(SANKEY_LEVEL_COLORS.size) { depth ->
                                            unsafeJso<SankeyLevelOption> {
                                                this.depth = depth.toDouble()
                                                itemStyle =
                                                    unsafeJso<ItemStyleOption> {
                                                        color = ZRColor(SANKEY_LEVEL_COLORS[depth])
                                                    }
                                                lineStyle =
                                                    unsafeJso<SankeyEdgeStyleOption> {
                                                        typedColor = SankeyEdgeColor("source")
                                                        opacity = 0.6
                                                    }
                                            }
                                        }
                                    lineStyle = unsafeJso<LineStyleOption> { curveness = 0.5 }
                                },
                            )
                    },
                )
            }
        }
