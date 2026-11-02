package demo.json.examples.tree

import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.nodeItem
import demo.json.nodes
import io.github.shijianjs.kmpechartsjson.*
import demo.json.examples.shared.flareLeaf
import demo.json.examples.shared.orientedLabels
import demo.json.*

private val treeLegendJsonCategory = JsonCategory("json/examples/tree/TreeLegendJson.kt")

val treeLegendJsonExample: JsonExample =
    treeLegendJsonCategory.example("tree-legend") {
            tooltip {
                trigger = TooltipTrigger.Item
                triggerOn = "mousemove"
            }
            legend {
                top = str("2%")
                left = str("3%")
                orient = Orient.Vertical
                data = listOf(
                    LegendItem.Entry("tree1", icon = "rectangle"),
                    LegendItem.Entry("tree2", icon = "rectangle"),
                )
                borderColor = "#c23531".asColor()
            }
            series {
                tree {
                    name = "tree1"
                    data = nodes(
                        nodeItem(
                            "flare",
                            null,
                            nodeItem(
                                "data",
                                null,
                                nodeItem(
                                    "converters",
                                    null,
                                    flareLeaf("Converters", 721),
                                    flareLeaf("DelimitedTextConverter", 4294),
                                ),
                                nodeItem("DataUtil", 3322),
                            ),
                            nodeItem(
                                "display",
                                null,
                                flareLeaf("DirtySprite", 8833),
                                flareLeaf("LineSprite", 1732),
                                flareLeaf("RectSprite", 3623),
                            ),
                            nodeItem("flex", null, flareLeaf("FlareVis", 4116)),
                        ),
                    )
                    top = str("5%")
                    left = str("7%")
                    bottom = str("2%")
                    right = str("60%")
                    symbolSize = NumberOrStringOrNumbers.of(7)
                    orientedLabels(labelPosition = "left", labelAlign = "right")
                    animationDuration = 550.0
                    animationDurationUpdate = 750.0
                }
                tree {
                    name = "tree2"
                    data = nodes(
                        nodeItem(
                            "display",
                            null,
                            flareLeaf("DirtySprite", 8833),
                        ),
                    )
                    top = str("20%")
                    left = str("60%")
                    bottom = str("22%")
                    right = str("18%")
                    symbolSize = NumberOrStringOrNumbers.of(7)
                    orientedLabels(labelPosition = "left", labelAlign = "right")
                    animationDuration = 550.0
                    animationDurationUpdate = 750.0
                }
            }
        }
