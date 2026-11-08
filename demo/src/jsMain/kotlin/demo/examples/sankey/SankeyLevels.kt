package demo.examples.sankey

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.EmphasisFocuses
import io.github.shijianjs.ktjs.echarts.options.EmphasisOption
import io.github.shijianjs.ktjs.echarts.options.ItemStyleOption
import io.github.shijianjs.ktjs.echarts.options.LineStyleOption
import io.github.shijianjs.ktjs.echarts.options.SankeyEdgeColor
import io.github.shijianjs.ktjs.echarts.options.SankeyEdgeStyleOption
import io.github.shijianjs.ktjs.echarts.options.SankeyLevelOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.sankeySeries
import js.objects.unsafeJso
import io.github.shijianjs.ktjs.zrender.ZRColor
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
