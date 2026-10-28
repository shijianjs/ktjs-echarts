package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.NodeAligns
import echarts.Orients
import echarts.TooltipTriggers
import echarts.TooltipTriggerOns
import echarts.options.EChartsOption
import echarts.EmphasisFocuses
import echarts.options.EmphasisOption
import echarts.options.ItemStyleOption
import echarts.options.LabelOption
import echarts.options.LabelPosition
import echarts.options.LineStyleOption
import echarts.options.OptionDataValue
import echarts.options.OptionId
import echarts.options.SankeyEdgeColor
import echarts.options.SankeyEdgeItemOption
import echarts.options.SankeyEdgeStyleOption
import echarts.options.SankeyLevelOption
import echarts.options.SankeyNodeItemOption
import echarts.options.SankeySeriesOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.TitleOption
import echarts.options.TooltipOption
import echarts.options.dataItems
import echarts.options.one
import echarts.options.sankeySeries
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import zrender.ZRColor

/*
 * Sankey examples ported from `public/examples/ts/sankey-*.ts` of apache/echarts-examples.
 *
 * The data-driven ones (`sankey-energy`, `sankey-levels`, `sankey-nodeAlign-left` /
 * `-right`) load `energy.json` and `product.json` from `demo/src/jsMain/resources/data/`
 * (see its `SOURCE.md`) instead of `$.get`, and build the option once the file arrives.
 *
 * `sankey-itemstyle` stays out: its 3000-row table is generated on the example server from data
 * this repository does not carry.
 */

private val sankey = Category(Groups.SANKEY, "examples/Sankey.kt")

/** A node of a sankey series. */
private fun node(name: String): SankeyNodeItemOption = unsafeJso { this.name = OptionId(name) }

/** One link of a sankey series. */
private fun link(source: String, target: String, value: Double): SankeyEdgeItemOption =
    unsafeJso {
        this.source = StringOrDouble(source)
        this.target = StringOrDouble(target)
        this.value = OptionDataValue(value)
    }

private val sankeyTooltip =
    one(
        unsafeJso<TooltipOption> {
            trigger = TooltipTriggers.item
            triggerOn = TooltipTriggerOns.mouseMove
        },
    )


/** Shape of `energy.json`: a list of node names and a list of `{ source, target, value }`. */
private external interface SankeyEnergyData {
    val nodes: Array<SankeyEnergyNode>
    val links: Array<SankeyEnergyLink>
}

/** One node of [SankeyEnergyData]. */
private external interface SankeyEnergyNode {
    val name: String
}

/** One link of [SankeyEnergyData]. */
private external interface SankeyEnergyLink {
    val source: String
    val target: String
    val value: Double
}

/** The nodes and the links of a loaded table, in the shape the sankey series expects. */
private class SankeyTable(data: SankeyEnergyData) {
    val nodes: Array<SankeyNodeItemOption?> = Array(data.nodes.size) { node(data.nodes[it].name) }

    val links: Array<SankeyEdgeItemOption> =
        Array(data.links.size) {
            val link = data.links[it]
            link(link.source, link.target, link.value)
        }
}

/** Reads `energy.json` / `product.json` into a [SankeyTable] and hands it to [onData]. */
private fun loadSankeyTable(path: String, onData: (SankeyTable) -> Unit) {
    loadJson(path).then { json ->
            onData(SankeyTable(json.unsafeCast<SankeyEnergyData>()))
            null
        }
}

/** A one-entry title component. */
private fun sankeyTitle(text: String): Array<TitleOption> =
    one(unsafeJso<TitleOption> { this.text = text })

/** The per-level colors of `sankey-levels`, one entry per depth. */
private val SANKEY_LEVEL_COLORS = arrayOf("#fbb4ae", "#b3cde3", "#ccebc5", "#decbe4")

val sankeyEntries: List<CatalogEntry> =
    listOf(
        sankey.chart("sankey-simple", "基础桑基图", "Basic Sankey") {
            unsafeJso<EChartsOption> {
                series =
                    arrayOf<SeriesOption>(
                        sankeySeries {
                            emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.adjacency }
                            typedData =
                                arrayOf(
                                    node("a"),
                                    node("b"),
                                    node("a1"),
                                    node("a2"),
                                    node("b1"),
                                    node("c"),
                                )
                            links =
                                arrayOf(
                                    link("a", "a1", 5.0),
                                    link("a", "a2", 3.0),
                                    link("b", "b1", 8.0),
                                    link("a", "b1", 3.0),
                                    link("b1", "a1", 1.0),
                                    link("b1", "c", 2.0),
                                )
                        },
                    )
            }
        },
        sankey.chart("sankey-vertical", "垂直方向的桑基图", "Sankey Orient Vertical") {
            unsafeJso<EChartsOption> {
                tooltip = sankeyTooltip
                animation = false
                series =
                    arrayOf<SeriesOption>(
                        sankeySeries {
                            bottom = StringOrDouble("10%")
                            orient = Orients.vertical
                            label = unsafeJso<LabelOption> { position = LabelPosition("top") }
                            lineStyle = unsafeJso<LineStyleOption> { color = zrender.ZRColor("source"); curveness = 0.5 }
                            emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.adjacency }
                            typedData =
                                arrayOf(
                                    node("a"),
                                    node("b"),
                                    node("a1"),
                                    node("b1"),
                                    node("c"),
                                    node("e"),
                                )
                            links =
                                arrayOf(
                                    link("a", "a1", 5.0),
                                    link("e", "b", 3.0),
                                    link("a", "b1", 3.0),
                                    link("b1", "a1", 1.0),
                                    link("b1", "c", 2.0),
                                    link("b", "c", 1.0),
                                )
                        },
                    )
            }
        },
        sankey.interactive("sankey-energy", "桑基图渐变色边", "Gradient Edge") { chart ->
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
        },
        sankey.interactive("sankey-levels", "桑基图层级自定义样式", "Sankey with Levels Setting") { chart ->
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
        },
        sankey.interactive("sankey-nodeAlign-left", "桑基图左对齐布局", "Node Align Left in Sankey") { chart ->
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
        },
        sankey.interactive("sankey-nodeAlign-right", "桑基图右对齐布局", "Node Align Right in Sankey") { chart ->
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
        },
    )
