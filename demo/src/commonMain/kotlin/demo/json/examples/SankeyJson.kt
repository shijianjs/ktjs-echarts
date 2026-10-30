package demo.json.examples

import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.linkItem
import demo.json.timedValue
import io.github.shijianjs.kmpechartsjson.*

/*
 * Sankey and theme river examples carried over from `sankey-*.ts` and `themeRiver-*.ts` of
 * apache/echarts-examples.
 *
 * Sampling and omissions (the inline data of the upstream files is far too large for a JSON page):
 * - `sankey-itemstyle` keeps 16 nodes and the first 12 links of the upstream table, with each node's
 *   original fill/border color; the series-level defaults are unchanged. Upstream values keep their
 *   original precision.
 * - `themeRiver-basic` keeps the first 12 of the 21 days, all six bands. `DD` repeats `DQ` except the
 *   eleventh day (the upstream table puts `4` there).
 * - `themeRiver-lastfm` keeps four genres (Andrew Bird, Laura Veirs, Brian Eno, Christopher Willits)
 *   and their first 12 months, indexed `0..11` like upstream.
 * - `lineStyle.curveness` (sankey-vertical, sankey-itemstyle) and the empty `axisTick: {}` /
 *   `axisLabel: {}` objects of `themeRiver-basic` have no model field or no effect, so they are
 *   omitted.
 */

private val flow = JsonCategory("json/examples/SankeyJson.kt")

/** A plain named sankey node. */
private fun sankeyNode(name: String): DataValue = dataItem { this.name = name }

/** A sankey node with the same fill and border color, the shape of `sankey-itemstyle`. */
private fun coloredNode(name: String, hex: String): DataValue =
    dataItem {
        this.name = name
        itemStyle {
            color = hex.asColor()
            borderColor = hex.asColor()
        }
    }

/** Six nodes and six links of the upstream basic example. */
private val SIMPLE_NODES = listOf("a", "b", "a1", "a2", "b1", "c")

/** Nodes of the vertical example. */
private val VERTICAL_NODES = listOf("a", "b", "a1", "b1", "c", "e")

/** Nodes of the sampled `sankey-itemstyle` table: `name to color`. */
private val ITEM_STYLE_NODES =
    listOf(
        "FRF" to "#5ba33b",
        "Colomiers" to "#efa835",
        "SuperiorCard" to "rgba(128,155,72,255)",
        "DEM" to "#4E342E",
        "München" to "#4dc0a6",
        "GBP" to "#C32D2E",
        "Reading" to "#72A376",
        "CAD" to "#40699D",
        "Shawnee" to "#F07F09",
        "Kirkby" to "#CCAF0A",
        "Roncq" to "#4D291C",
        "Peterborough" to "#7A072D",
        "Frankfurt am Main" to "#EA005E",
        "Oxford" to "#D8B25C",
        "Vista" to "rgba(106,82,134,255)",
        "Langley" to "#CD7B38",
    )

/** Twelve links of the sampled `sankey-itemstyle` table, in upstream order. */
private val ITEM_STYLE_LINKS =
    listOf(
        linkItem("FRF", "Colomiers", 357.8399963378906),
        linkItem("SuperiorCard", "FRF", 894.5999908447266),
        linkItem("DEM", "München", 178.9199981689453),
        linkItem("GBP", "Reading", 188.52999836206436),
        linkItem("CAD", "Shawnee", 2346.919983509928),
        linkItem("GBP", "Kirkby", 753.4000000059605),
        linkItem("FRF", "Roncq", 178.9199981689453),
        linkItem("GBP", "Peterborough", 999.159998036921),
        linkItem("DEM", "Frankfurt am Main", 536.7599945068359),
        linkItem("GBP", "Oxford", 1831.3799968883395),
        linkItem("Vista", "FRF", 1789.1999816894531),
        linkItem("CAD", "Langley", 1274.8199949413538),
    )

/** First 12 days of the basic theme river. */
private val RIVER_DAYS =
    listOf(
        "2015/11/08",
        "2015/11/09",
        "2015/11/10",
        "2015/11/11",
        "2015/11/12",
        "2015/11/13",
        "2015/11/14",
        "2015/11/15",
        "2015/11/16",
        "2015/11/17",
        "2015/11/18",
        "2015/11/19",
    )

/** Builds one `[day, value, band]` row per day. */
private fun riverBand(label: String, values: List<Number>): List<DataValue> =
    RIVER_DAYS.indices.map { index -> timedValue(RIVER_DAYS[index], values[index], label) }

/** Builds one `[month, value, genre]` row per month, the shape of the Lastfm example. */
private fun lastfmBand(label: String, values: List<Number>): List<DataValue> =
    values.mapIndexed { month, value ->
        DataValue.Array(listOf(valueOf(month), valueOf(value), textValue(label)))
    }

public val sankeyJsonExamples: List<JsonExample> =
    listOf(
        flow.example("sankey-simple") {
            series {
                sankey {
                    layout = "none"
                    emphasis { focus = "adjacency" }
                    data = SIMPLE_NODES.map(::sankeyNode)
                    links =
                        listOf(
                            linkItem("a", "a1", 5.0),
                            linkItem("a", "a2", 3.0),
                            linkItem("b", "b1", 8.0),
                            linkItem("a", "b1", 3.0),
                            linkItem("b1", "a1", 1.0),
                            linkItem("b1", "c", 2.0),
                        )
                }
            }
        },
        flow.example("sankey-vertical") {
            tooltip {
                trigger = TooltipTrigger.Item
                triggerOn = "mousemove"
            }
            animation = false
            series {
                sankey {
                    bottom = str("10%")
                    emphasis { focus = "adjacency" }
                    data = VERTICAL_NODES.map(::sankeyNode)
                    links =
                        listOf(
                            linkItem("a", "a1", 5.0),
                            linkItem("e", "b", 3.0),
                            linkItem("a", "b1", 3.0),
                            linkItem("b1", "a1", 1.0),
                            linkItem("b1", "c", 2.0),
                            linkItem("b", "c", 1.0),
                        )
                    orient = Orient.Vertical
                    label { position = "top" }
                    lineStyle { color = "source".asColor() }
                }
            }
        },
        flow.example("sankey-itemstyle") {
            backgroundColor = "#fff".asColor()
            title {
                subtext = "Data From lisachristina1234 on GitHub"
                left = str("center")
            }
            tooltip { trigger = TooltipTrigger.Item }
            series {
                sankey {
                    left = num(50)
                    top = num(20)
                    right = num(150)
                    bottom = num(25)
                    data = ITEM_STYLE_NODES.map { (name, hex) -> coloredNode(name, hex) }
                    links = ITEM_STYLE_LINKS
                    lineStyle { color = "source".asColor() }
                    itemStyle {
                        color = "#1f77b4".asColor()
                        borderColor = "#1f77b4".asColor()
                    }
                    label {
                        color = "rgba(0,0,0,0.7)".asColor()
                        fontFamily = "Arial"
                        fontSize = 10.0
                    }
                }
            }
        },
        flow.example("themeRiver-basic") {
            tooltip {
                trigger = TooltipTrigger.Axis
                axisPointer =
                    AxisPointerOption().apply {
                        type = "line"
                        lineStyle =
                            LineStyleOption().apply {
                                color = "rgba(0,0,0,0.2)".asColor()
                                width = 1.0
                                type = "solid"
                            }
                    }
            }
            legend {
                entries("DQ", "TY", "SS", "QG", "SY", "DD")
                top = num(15)
            }
            singleAxis {
                top = num(50)
                bottom = num(50)
                type = AxisType.Time
                axisPointer =
                    AxisPointerOption().apply {
                        animation = true
                        label = AxisPointerLabelOption().apply { show = true }
                    }
                splitLine =
                    SplitLineOption().apply {
                        show = true
                        lineStyle =
                            LineStyleOption().apply {
                                type = "dashed"
                                opacity = 0.2
                            }
                    }
            }
            series {
                themeRiver {
                    singleAxisIndex = 0
                    emphasis {
                        itemStyle {
                            shadowBlur = 20.0
                            shadowColor = "rgba(0, 0, 0, 0.8)".asColor()
                        }
                    }
                    data =
                        riverBand("DQ", listOf(10, 15, 35, 38, 22, 16, 7, 2, 17, 33, 40, 32)) +
                            riverBand("TY", listOf(35, 36, 37, 22, 24, 26, 34, 21, 18, 45, 32, 35)) +
                            riverBand("SS", listOf(21, 25, 27, 23, 24, 21, 35, 39, 40, 36, 33, 43)) +
                            riverBand("QG", listOf(10, 15, 35, 38, 22, 16, 7, 2, 17, 33, 40, 32)) +
                            riverBand("SY", listOf(10, 15, 35, 38, 22, 16, 7, 2, 17, 33, 40, 32)) +
                            riverBand("DD", listOf(10, 15, 35, 38, 22, 16, 7, 2, 17, 33, 4, 32))
                }
            }
        },
        flow.example("themeRiver-lastfm") {
            singleAxis { max = str("dataMax") }
            series {
                themeRiver {
                    label { show = false }
                    data =
                        lastfmBand(
                            "Andrew Bird",
                            listOf(0, 49, 67, 16, 0, 19, 19, 0, 0, 1, 10, 5),
                        ) +
                            lastfmBand(
                                "Laura Veirs",
                                listOf(0, 6, 3, 34, 0, 16, 1, 0, 0, 1, 6, 0),
                            ) +
                            lastfmBand(
                                "Brian Eno",
                                listOf(0, 8, 13, 15, 0, 12, 23, 0, 0, 1, 0, 1),
                            ) +
                            lastfmBand(
                                "Christopher Willits",
                                listOf(0, 9, 28, 0, 91, 6, 1, 0, 0, 0, 7, 18),
                            )
                }
            }
        },
    )
