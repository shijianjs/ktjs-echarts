package demo.examples.sankey

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.EmphasisFocuses
import io.github.shijianjs.ktjs.echarts.options.EmphasisOption
import io.github.shijianjs.ktjs.echarts.options.SankeyEdgeColor
import io.github.shijianjs.ktjs.echarts.options.SankeyEdgeStyleOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.sankeySeries
import js.objects.unsafeJso
import demo.examples.Category

private val sankeyEnergyCategory = Category(Groups.SANKEY, "examples/sankey/SankeyEnergy.kt")

val sankeyEnergyEntry: CatalogEntry =
    sankeyEnergyCategory.interactive("sankey-energy", "桑基图渐变色边", "Gradient Edge") { chart ->
            loadSankeyTable("data/energy.json") { table ->
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
                                    lineStyle =
                                        unsafeJso<SankeyEdgeStyleOption> {
                                            typedColor = SankeyEdgeColor("gradient")
                                            curveness = 0.5
                                        }
                                },
                            )
                    },
                )
            }
        }
