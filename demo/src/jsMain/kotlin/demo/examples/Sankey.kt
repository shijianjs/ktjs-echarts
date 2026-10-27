package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.Orients
import echarts.TooltipTriggers
import echarts.TooltipTriggerOns
import echarts.options.EChartsOption
import echarts.EmphasisFocuses
import echarts.options.EmphasisOption
import echarts.options.LabelOption
import echarts.options.LabelPosition
import echarts.options.LineStyleOption
import echarts.options.OptionDataValue
import echarts.options.SankeyEdgeItemOption
import echarts.options.OptionId
import echarts.options.SankeyNodeItemOption
import echarts.options.SankeySeriesOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.TooltipOption
import echarts.options.dataItems
import echarts.options.one
import echarts.options.sankeySeries
import js.objects.unsafeJso

/*
 * Sankey examples ported from `public/examples/ts/sankey-*.ts` of apache/echarts-examples.
 *
 * The remaining four originals (`sankey-energy`, `sankey-itemstyle`, `sankey-levels`,
 * `sankey-nodeAlign-*`) load `energy.json`, `product.json` or a large generated table from the
 * example server; they are not ported because this page ships neither the files nor a loader.
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
    )
