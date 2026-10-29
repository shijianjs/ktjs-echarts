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
 * `sankey-itemstyle` carries its own table: 99 nodes with a fixed color each and 109 links, both
 * written out as Kotlin arrays because upstream inlines them in the option.
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

/**
 * Nodes of `sankey-itemstyle`: the name of the node and the color used for its fill and border.
 *
 * Upstream writes one `data` object per node; the table keeps the pairs because the item style is
 * the only per-node setting of this example.
 */
private val sankeyItemStyleNodes =
    arrayOf(
            "Werne" to "#f18bbf",
            "Duesseldorf" to "#0078D7",
            "Cambridge" to "#3891A7",
            "Colma" to "#0037DA",
            "W. York" to "#C0BEAF",
            "Frankfurt am Main" to "#EA005E",
            "Metz" to "#D13438",
            "Orleans" to "#567C73",
            "Saint-Denis" to "#9ed566",
            "Hof" to "#2BCC7F",
            "Cliffside" to "#809B48",
            "Leeds" to "#9B2D1F",
            "Victoria" to "#604878",
            "Erlangen" to "#A5644E",
            "Saint Germain en Laye" to "#2D3F3A",
            "Roissy en Brie" to "#761721",
            "Wokingham" to "#B1BADD",
            "Runcorn" to "#B0CCB0",
            "Newton" to "#8164A3",
            "Morangis" to "#8E562E",
            "Metchosin" to "#C1504D",
            "Kirkby" to "#CCAF0A",
            "London" to "#956251",
            "Offenbach" to "#C17529",
            "Warrington" to "#CEC597",
            "Vancouver" to "#9F2936",
            "SuperiorCard" to "rgba(128,155,72,255)",
            "Lille" to "#ac7430",
            "Hamburg" to "#00BCF2",
            "Langley" to "#CD7B38",
            "Les Ulis" to "#424242",
            "Saarbrücken" to "#f63185",
            "N. Vancouver" to "#9CBC59",
            "Chalk Riber" to "#4F4BD9",
            "Esher-Molesey" to "#3EC562",
            "Chatou" to "#F06F2E",
            "Hannover" to "#C3986D",
            "Roncq" to "#4D291C",
            "Ingolstadt" to "#009c7a",
            "Drancy" to "#986F0B",
            "Langford" to "#3C8EA4",
            "Lebanon" to "#4F82BE",
            "Maidenhead" to "#D38017",
            "Stoke-on-Trent" to "#A8CDD7",
            "Peterborough" to "#7A072D",
            "Suresnes" to "#859599",
            "Versailles" to "#84AA33",
            "Neunkirchen" to "#ff8b67",
            "Vista" to "rgba(106,82,134,255)",
            "Westminster" to "#1B587C",
            "Kiel" to "#A19574",
            "Newcastle upon Tyne" to "#918485",
            "Oxon" to "#FFA98C",
            "West Sussex" to "#B0E3C0",
            "Oak Bay" to "#4BADC7",
            "Milton Keynes" to "#BA144C",
            "Eilenburg" to "#F0A22E",
            "ColonialVoice" to "rgba(64,105,157,255)",
            "Liverpool" to "#A28E6A",
            "Calgary" to "#9F413E",
            "CAD" to "#40699D",
            "Paris La Defense" to "#989391",
            "Villeneuve-d'Ascq" to "#886CE4",
            "Gloucestershire" to "#964305",
            "Gateshead" to "#485FB5",
            "Salzgitter" to "#87a0c7",
            "Woolston" to "#FFE2C5",
            "Frankfurt" to "#40699D",
            "Münster" to "#7e7eb2",
            "York" to "#587C7D",
            "High Wycombe" to "#F07F09",
            "Stuttgart" to "#E3008C",
            "Sooke" to "#4E8542",
            "Essen" to "#B58B80",
            "München" to "#4dc0a6",
            "Haney" to "#6A5286",
            "Port Hammond" to "#F89746",
            "Saint Ouen" to "#744DA9",
            "Watford" to "#E8B7B7",
            "GBP" to "#C32D2E",
            "Paderborn" to "#F0C42E",
            "Dunkerque" to "#881798",
            "Colomiers" to "#efa835",
            "Oxford" to "#D8B25C",
            "Bury" to "#FEB80A",
            "Royal Oak" to "#009DD9",
            "Shawnee" to "#F07F09",
            "Lancaster" to "#D34817",
            "DEM" to "#4E342E",
            "Grevenbroich" to "#FFA836",
            "Distinguish" to "rgba(159,65,62,255)",
            "Cheltenham" to "#FF6551",
            "Reading" to "#72A376",
            "Pantin" to "#69797E",
            "Kassel" to "#e65e20",
            "Orly" to "#6E6A68",
            "FRF" to "#5ba33b",
            "Cergy" to "#B4009E",
            "Paris" to "#666666",
    )

/** Links of `sankey-itemstyle`: source, target and value. */
private val sankeyItemStyleLinks =
    arrayOf(
            Triple("FRF", "Colomiers", 357.8399963378906),
            Triple("SuperiorCard", "FRF", 894.5999908447266),
            Triple("DEM", "München", 178.9199981689453),
            Triple("GBP", "Reading", 188.52999836206436),
            Triple("CAD", "Shawnee", 2346.919983509928),
            Triple("GBP", "Kirkby", 753.4000000059605),
            Triple("FRF", "Roncq", 178.9199981689453),
            Triple("GBP", "Peterborough", 999.159998036921),
            Triple("DEM", "Frankfurt am Main", 536.7599945068359),
            Triple("GBP", "Oxford", 1831.3799968883395),
            Triple("Vista", "FRF", 1789.1999816894531),
            Triple("CAD", "Langley", 1274.8199949413538),
            Triple("DEM", "Offenbach", 357.8399963378906),
            Triple("FRF", "Villeneuve-d'Ascq", 178.9199981689453),
            Triple("FRF", "Dunkerque", 357.8399963378906),
            Triple("DEM", "Eilenburg", 178.9199981689453),
            Triple("FRF", "Paris", 1073.5199890136719),
            Triple("GBP", "Maidenhead", 549.8400026857853),
            Triple("CAD", "Sooke", 1764.499989286065),
            Triple("CAD", "Vancouver", 1528.580000281334),
            Triple("DEM", "Hamburg", 357.8399963378906),
            Triple("GBP", "London", 8619.309983983636),
            Triple("CAD", "Oak Bay", 1565.109990529716),
            Triple("Distinguish", "FRF", 2683.7999725341797),
            Triple("DEM", "Neunkirchen", 178.9199981689453),
            Triple("FRF", "Cergy", 178.9199981689453),
            Triple("DEM", "Hof", 357.8399963378906),
            Triple("FRF", "Paris La Defense", 178.9199981689453),
            Triple("CAD", "Westminster", 1149.7999994903803),
            Triple("DEM", "Ingolstadt", 536.7599945068359),
            Triple("GBP", "Saint Ouen", 0.5899999737739563),
            Triple("FRF", "Lille", 357.8399963378906),
            Triple("GBP", "Leeds", 1356.6899970173836),
            Triple("FRF", "Morangis", 357.8399963378906),
            Triple("GBP", "Orly", 0.5899999737739563),
            Triple("SuperiorCard", "DEM", 1431.3599853515625),
            Triple("Vista", "CAD", 5369.929964579642),
            Triple("GBP", "Paris", 0.6399999856948853),
            Triple("GBP", "Liverpool", 857.1999968588352),
            Triple("GBP", "Stoke-on-Trent", 1131.7099939212203),
            Triple("Distinguish", "DEM", 2504.8799743652344),
            Triple("CAD", "Langford", 2343.4599857851863),
            Triple("DEM", "Kassel", 536.7599945068359),
            Triple("GBP", "High Wycombe", 216.83999809622765),
            Triple("CAD", "Port Hammond", 1711.1399984136224),
            Triple("DEM", "Duesseldorf", 178.9199981689453),
            Triple("GBP", "Gloucestershire", 422.28999888151884),
            Triple("Distinguish", "GBP", 10384.949975416064),
            Triple("FRF", "Roissy en Brie", 178.9199981689453),
            Triple("GBP", "West Sussex", 592.1700052022934),
            Triple("CAD", "Cliffside", 2906.2699892893434),
            Triple("GBP", "Newcastle upon Tyne", 1448.2899911925197),
            Triple("GBP", "Runcorn", 1120.4800013303757),
            Triple("GBP", "W. York", 612.1199932694435),
            Triple("DEM", "Kiel", 178.9199981689453),
            Triple("GBP", "Woolston", 833.3199937939644),
            Triple("Distinguish", "CAD", 6950.059956334531),
            Triple("DEM", "Frankfurt", 715.6799926757812),
            Triple("CAD", "Colma", 0.2199999988079071),
            Triple("DEM", "Essen", 178.9199981689453),
            Triple("FRF", "Chatou", 178.9199981689453),
            Triple("GBP", "Cheltenham", 573.0499979257584),
            Triple("SuperiorCard", "GBP", 8228.39999615401),
            Triple("CAD", "Haney", 2310.4499812424183),
            Triple("FRF", "Saint Ouen", 178.9199981689453),
            Triple("CAD", "Chalk Riber", 0.9200000166893005),
            Triple("DEM", "Salzgitter", 178.9199981689453),
            Triple("ColonialVoice", "FRF", 1610.2799835205078),
            Triple("DEM", "Stuttgart", 357.8399963378906),
            Triple("FRF", "Saint-Denis", 178.9199981689453),
            Triple("CAD", "Royal Oak", 2128.459992274642),
            Triple("FRF", "Les Ulis", 715.6799926757812),
            Triple("FRF", "Drancy", 178.9199981689453),
            Triple("GBP", "Esher-Molesey", 911.4700058028102),
            Triple("SuperiorCard", "CAD", 7388.099954992533),
            Triple("GBP", "Bury", 903.9400005489588),
            Triple("GBP", "Watford", 1326.5300009772182),
            Triple("CAD", "Victoria", 827.3899968340993),
            Triple("DEM", "Saarbrücken", 178.9199981689453),
            Triple("GBP", "Lancaster", 685.6899967193604),
            Triple("FRF", "Pantin", 178.9199981689453),
            Triple("CAD", "Newton", 1781.909985654056),
            Triple("GBP", "Oxon", 493.6499986946583),
            Triple("CAD", "Calgary", 361.3899962902069),
            Triple("DEM", "Münster", 715.6799926757812),
            Triple("DEM", "Grevenbroich", 536.7599945068359),
            Triple("DEM", "Paderborn", 357.8399963378906),
            Triple("GBP", "York", 3172.9999914616346),
            Triple("CAD", "Metchosin", 1750.7899813987315),
            Triple("FRF", "Suresnes", 357.8399963378906),
            Triple("FRF", "Versailles", 894.5999908447266),
            Triple("DEM", "Erlangen", 536.7599945068359),
            Triple("CAD", "Lebanon", 0.8700000047683716),
            Triple("GBP", "Wokingham", 812.6600027084351),
            Triple("GBP", "Cambridge", 500.36999743431807),
            Triple("ColonialVoice", "GBP", 8040.7799860313535),
            Triple("FRF", "Saint Germain en Laye", 178.9199981689453),
            Triple("FRF", "Metz", 178.9199981689453),
            Triple("FRF", "Orleans", 357.8399963378906),
            Triple("GBP", "Milton Keynes", 1648.2200061008334),
            Triple("GBP", "Warrington", 2162.8300000429153),
            Triple("CAD", "N. Vancouver", 1862.4599857926369),
            Triple("DEM", "Hannover", 178.9199981689453),
            Triple("Vista", "GBP", 9497.539981640875),
            Triple("DEM", "Werne", 178.9199981689453),
            Triple("ColonialVoice", "DEM", 1789.1999816894531),
            Triple("ColonialVoice", "CAD", 7907.36997512728),
            Triple("GBP", "Gateshead", 1425.7099913656712),
            Triple("Vista", "DEM", 1968.1199798583984),
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
        sankey.chart("sankey-itemstyle", "桑基图节点自定义样式", "Specify ItemStyle for Each Node in Sankey") {
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
        },
    )
