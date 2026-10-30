package demo.json.examples

import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.nodeItem
import demo.json.nodes
import io.github.shijianjs.kmpechartsjson.*

/*
 * Tree and treemap examples carried over from `tree-*.ts` / `treemap-simple.ts` of
 * apache/echarts-examples.
 *
 * The pages keep the official ids, so the Kotlin/JS port and the JSON port of the same chart show
 * up under one route.
 *
 * Data: `tree-basic`, `tree-orient-bottom-top`, `tree-orient-right-left` and `tree-vertical` load
 * the 250-node `flare.json` upstream. The model examples use a trimmed flare subtree with the same
 * top-level branches (`data` / `display` / `flex` / `query` / `scale`) and the upstream values, so
 * the page stays readable without a multi-hundred-line literal. `tree-legend` and `tree-polyline`
 * inline their (already smaller) tables upstream; those are copied as-is.
 *
 * Modelled differences:
 * - `SeriesOption.orient` is the two-value `Orient` enum, so the upstream `'BT'` /
 *   `'RL'` directions are approximated with `Vertical` / `Horizontal`; the reverse direction
 *   (`bottom-to-top`, `right-to-left`) cannot be expressed and is omitted on those two pages.
 * - `leaves` is a `LabelOption` in the model while ECharts expects `leaves: { label: ... }`, so the
 *   leaf-only label overrides are omitted (they would be dropped by ECharts anyway).
 * - `edgeForkPosition` of `tree-polyline` is not modelled and is omitted; the numeric `id: 0` is
 *   dropped because `SeriesOption.id` is a string and no other component references it.
 */

private val tree = JsonCategory("json/examples/TreeJson.kt")

/** One leaf of the flare hierarchy. */
private fun flareLeaf(name: String, value: Number): DataItem = nodeItem(name, value)

/**
 * The flare hierarchy used by the four orientation pages.
 *
 * Mirrors the branches of the upstream `flare.json` table with the same names and values; the
 * `query` / `methods` branch keeps a representative slice of the 49 method leaves.
 */
private fun flareTree(collapseAlternate: Boolean = false): DataItem =
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
        nodeItem(
            "query",
            null,
            flareLeaf("AggregateExpression", 1616),
            flareLeaf("And", 1027),
            flareLeaf("Arithmetic", 3891),
            flareLeaf("Count", 781),
            nodeItem(
                "methods",
                null,
                flareLeaf("add", 593),
                flareLeaf("and", 330),
                flareLeaf("average", 287),
                flareLeaf("count", 277),
            ),
            flareLeaf("Query", 13896),
            flareLeaf("Sum", 791),
        ),
        nodeItem(
            "scale",
            null,
            flareLeaf("IScaleMap", 2105),
            flareLeaf("LinearScale", 1316),
            flareLeaf("LogScale", 3151),
            flareLeaf("OrdinalScale", 3770),
            flareLeaf("TimeScale", 5833),
        ),
    ).apply {
        // `tree-basic` / `tree-orient-right-left` collapse every other top-level branch on load.
        if (collapseAlternate) {
            children?.forEachIndexed { index, child ->
                if (index % 2 == 0) child.collapsed = true
            }
        }
    }

/** Branch labels of the left-to-right pages. */
private fun SeriesOption.orientedLabels(labelPosition: String, labelAlign: String) {
    label {
        position = labelPosition
        verticalAlign = "middle"
        align = labelAlign
    }
    emphasis { focus = "descendant" }
    expandAndCollapse = true
}

public val treeJsonExamples: List<JsonExample> =
    listOf(
        tree.example("tree-basic") {
            tooltip {
                trigger = TooltipTrigger.Item
                triggerOn = "mousemove"
            }
            series {
                tree {
                    data = nodes(flareTree(collapseAlternate = true))
                    top = str("1%")
                    left = str("7%")
                    bottom = str("1%")
                    right = str("20%")
                    symbolSize = NumberOrStringOrNumbers.of(7)
                    label {
                        position = "left"
                        verticalAlign = "middle"
                        align = "right"
                        fontSize = 9.0
                    }
                    emphasis { focus = "descendant" }
                    expandAndCollapse = true
                    animationDuration = 550.0
                    animationDurationUpdate = 750.0
                }
            }
        },
        tree.example("tree-legend") {
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
        },
        tree.example("tree-orient-bottom-top") {
            tooltip {
                trigger = TooltipTrigger.Item
                triggerOn = "mousemove"
            }
            series {
                tree {
                    data = nodes(flareTree())
                    left = str("2%")
                    right = str("2%")
                    top = str("20%")
                    bottom = str("8%")
                    symbol = "emptyCircle"
                    // Upstream `orient: 'BT'`; the model enum only distinguishes the two axes.
                    orient = Orient.Vertical
                    expandAndCollapse = true
                    label {
                        position = "bottom"
                        rotate = 90.0
                        verticalAlign = "middle"
                        align = "right"
                    }
                    emphasis { focus = "descendant" }
                    animationDurationUpdate = 750.0
                }
            }
        },
        tree.example("tree-orient-right-left") {
            tooltip {
                trigger = TooltipTrigger.Item
                triggerOn = "mousemove"
            }
            series {
                tree {
                    data = nodes(flareTree(collapseAlternate = true))
                    top = str("1%")
                    left = str("15%")
                    bottom = str("1%")
                    right = str("7%")
                    symbolSize = NumberOrStringOrNumbers.of(7)
                    // Upstream `orient: 'RL'`; the model enum only distinguishes the two axes.
                    orient = Orient.Horizontal
                    label {
                        position = "right"
                        verticalAlign = "middle"
                        align = "left"
                    }
                    emphasis { focus = "descendant" }
                    expandAndCollapse = true
                    animationDuration = 550.0
                    animationDurationUpdate = 750.0
                }
            }
        },
        tree.example("tree-polyline") {
            tooltip {
                trigger = TooltipTrigger.Item
                triggerOn = "mousemove"
            }
            series {
                tree {
                    name = "tree1"
                    data = nodes(flareTree())
                    top = str("10%")
                    left = str("8%")
                    bottom = str("22%")
                    right = str("20%")
                    symbolSize = NumberOrStringOrNumbers.of(7)
                    edgeShape = "polyline"
                    initialTreeDepth = 3
                    lineStyle { width = 2.0 }
                    label {
                        backgroundColor = "#fff".asColor()
                        position = "left"
                        verticalAlign = "middle"
                        align = "right"
                    }
                    emphasis { focus = "descendant" }
                    expandAndCollapse = true
                    animationDuration = 550.0
                    animationDurationUpdate = 750.0
                }
            }
        },
        tree.example("tree-radial") {
            tooltip {
                trigger = TooltipTrigger.Item
                triggerOn = "mousemove"
            }
            series {
                tree {
                    data = nodes(flareTree())
                    top = str("18%")
                    bottom = str("14%")
                    layout = "radial"
                    symbol = "emptyCircle"
                    symbolSize = NumberOrStringOrNumbers.of(7)
                    initialTreeDepth = 3
                    animationDurationUpdate = 750.0
                    emphasis { focus = "descendant" }
                }
            }
        },
        tree.example("tree-vertical") {
            tooltip {
                trigger = TooltipTrigger.Item
                triggerOn = "mousemove"
            }
            series {
                tree {
                    data = nodes(flareTree())
                    left = str("2%")
                    right = str("2%")
                    top = str("8%")
                    bottom = str("20%")
                    symbol = "emptyCircle"
                    orient = Orient.Vertical
                    expandAndCollapse = true
                    label {
                        position = "top"
                        rotate = -90.0
                        verticalAlign = "middle"
                        align = "right"
                        fontSize = 9.0
                    }
                    emphasis { focus = "descendant" }
                    animationDurationUpdate = 750.0
                }
            }
        },
        tree.example("treemap-simple") {
            series {
                treemap {
                    data = nodes(
                        nodeItem(
                            "nodeA",
                            10,
                            nodeItem("nodeAa", 4),
                            nodeItem("nodeAb", 6),
                        ),
                        nodeItem(
                            "nodeB",
                            20,
                            nodeItem(
                                "nodeBa",
                                20,
                                nodeItem("nodeBa1", 20),
                            ),
                        ),
                    )
                }
            }
        },
    )
