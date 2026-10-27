package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.EmphasisFocuses
import echarts.Orients
import echarts.TooltipTriggerOns
import echarts.TooltipTriggers
import echarts.TreeLayouts
import echarts.TreeOrients
import echarts.options.EChartsOption
import echarts.options.LabelOption
import echarts.options.LabelPosition
import echarts.options.LegendDataItemOption
import echarts.options.LegendOption
import echarts.options.LineStyleOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.SymbolShape
import echarts.options.SymbolSize
import echarts.options.TooltipOption
import echarts.options.TreeSeriesLeavesOption
import echarts.options.TreeSeriesNodeItemOption
import echarts.options.one
import echarts.options.treeSeries
import js.objects.unsafeJso
import web.cssom.TextAlign
import web.cssom.VerticalAlign
import zrender.ZRColor

/*
 * Tree examples ported from `public/examples/ts/tree-*.ts` of apache/echarts-examples.
 *
 * Every official tree example loads the flare hierarchy from the example server; the tree itself is
 * inlined in `TreeData.kt` (see its KDoc), so the whole family is portable.
 */

private val tree = Category(Groups.TREE, "examples/Tree.kt")

/** Collapses every second child of [root], the preparation `tree-basic` and `tree-orient-*` do. */
private fun collapseEverySecondChild(root: TreeSeriesNodeItemOption) {
    root.children?.forEachIndexed { index, child ->
        if (index % 2 == 0) child.collapsed = true
    }
}

/** Label of the non-leaf nodes: to the left of the node, right aligned. */
private fun branchLabel(): LabelOption =
    unsafeJso {
        position = LabelPosition("left")
        verticalAlign = VerticalAlign.middle
        align = TextAlign.right
    }

/** Label of the leaves: to the right of the node, left aligned. */
private fun leafLabel(): LabelOption =
    unsafeJso {
        position = LabelPosition("right")
        verticalAlign = VerticalAlign.middle
        align = TextAlign.left
    }

/** Leaves of a left-to-right tree: the mirror image of [branchLabel]. */
private fun leaves(): TreeSeriesLeavesOption = unsafeJso { label = leafLabel() }

val treeEntries: List<CatalogEntry> =
    listOf(
        tree.chart("tree-basic", "从左到右树状图", "From Left to Right Tree") {
            val flare = flareTree()
            collapseEverySecondChild(flare)
            unsafeJso<EChartsOption> {
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.item
                            triggerOn = TooltipTriggerOns.mouseMove
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        treeSeries {
                            typedData = arrayOf(flare)
                            top = StringOrDouble("1%")
                            left = StringOrDouble("7%")
                            bottom = StringOrDouble("1%")
                            right = StringOrDouble("20%")
                            symbolSize = SymbolSize(7.0)
                            label = branchLabel()
                            leaves = leaves()
                            emphasis = unsafeJso { focus = EmphasisFocuses.descendant }
                            expandAndCollapse = true
                            animationDuration = echarts.options.AnimationDuration(550.0)
                            animationDurationUpdate = echarts.options.AnimationDuration(750.0)
                        },
                    )
            }
        },
        tree.chart("tree-legend", "多棵树", "Multiple Trees") {
            unsafeJso<EChartsOption> {
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.item
                            triggerOn = TooltipTriggerOns.mouseMove
                        },
                    )
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            top = StringOrDouble("2%")
                            left = StringOrDouble("3%")
                            orient = Orients.vertical
                            borderColor = ZRColor("#c23531")
                            data =
                                arrayOf(
                                    unsafeJso<LegendDataItemOption> { name = "tree1"; icon = "rectangle" },
                                    unsafeJso<LegendDataItemOption> { name = "tree2"; icon = "rectangle" },
                                )
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        treeSeries {
                            name = "tree1"
                            typedData = arrayOf(treeLegendData1())
                            top = StringOrDouble("5%")
                            left = StringOrDouble("7%")
                            bottom = StringOrDouble("2%")
                            right = StringOrDouble("60%")
                            symbolSize = SymbolSize(7.0)
                            label = branchLabel()
                            leaves = leaves()
                            emphasis = unsafeJso { focus = EmphasisFocuses.descendant }
                            expandAndCollapse = true
                            animationDuration = echarts.options.AnimationDuration(550.0)
                            animationDurationUpdate = echarts.options.AnimationDuration(750.0)
                        },
                        treeSeries {
                            name = "tree2"
                            typedData = arrayOf(treeLegendData2())
                            top = StringOrDouble("20%")
                            left = StringOrDouble("60%")
                            bottom = StringOrDouble("22%")
                            right = StringOrDouble("18%")
                            symbolSize = SymbolSize(7.0)
                            label = branchLabel()
                            leaves = leaves()
                            emphasis = unsafeJso { focus = EmphasisFocuses.descendant }
                            expandAndCollapse = true
                            animationDuration = echarts.options.AnimationDuration(550.0)
                            animationDurationUpdate = echarts.options.AnimationDuration(750.0)
                        },
                    )
            }
        },
        tree.chart("tree-polyline", "折线树图", "Tree with Polyline Edge") {
            unsafeJso<EChartsOption> {
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.item
                            triggerOn = TooltipTriggerOns.mouseMove
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        treeSeries {
                            id = "0"
                            name = "tree1"
                            typedData = arrayOf(flareTree())
                            top = StringOrDouble("10%")
                            left = StringOrDouble("8%")
                            bottom = StringOrDouble("22%")
                            right = StringOrDouble("20%")
                            symbolSize = SymbolSize(7.0)
                            edgeShape = echarts.TreeEdgeShapes.polyline
                            edgeForkPosition = StringOrDouble("63%")
                            initialTreeDepth = 3.0
                            lineStyle = unsafeJso<LineStyleOption> { width = 2.0 }
                            label =
                                unsafeJso<LabelOption> {
                                    backgroundColor = ZRColor("#fff")
                                    position = LabelPosition("left")
                                    verticalAlign = VerticalAlign.middle
                                    align = TextAlign.right
                                }
                            leaves = leaves()
                            emphasis = unsafeJso { focus = EmphasisFocuses.descendant }
                            expandAndCollapse = true
                            animationDuration = echarts.options.AnimationDuration(550.0)
                            animationDurationUpdate = echarts.options.AnimationDuration(750.0)
                        },
                    )
            }
        },
        tree.chart("tree-radial", "径向树状图", "Radial Tree") {
            unsafeJso<EChartsOption> {
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.item
                            triggerOn = TooltipTriggerOns.mouseMove
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        treeSeries {
                            typedData = arrayOf(flareTree())
                            top = StringOrDouble("18%")
                            bottom = StringOrDouble("14%")
                            layout = TreeLayouts.radial
                            symbol = SymbolShape("emptyCircle")
                            symbolSize = SymbolSize(7.0)
                            initialTreeDepth = 3.0
                            animationDurationUpdate = echarts.options.AnimationDuration(750.0)
                            emphasis = unsafeJso { focus = EmphasisFocuses.descendant }
                        },
                    )
            }
        },
        tree.chart("tree-vertical", "从上到下树状图", "From Top to Bottom Tree") {
            unsafeJso<EChartsOption> {
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.item
                            triggerOn = TooltipTriggerOns.mouseMove
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        treeSeries {
                            typedData = arrayOf(flareTree())
                            left = StringOrDouble("2%")
                            right = StringOrDouble("2%")
                            top = StringOrDouble("8%")
                            bottom = StringOrDouble("20%")
                            symbol = SymbolShape("emptyCircle")
                            orient = TreeOrients.vertical
                            expandAndCollapse = true
                            label =
                                unsafeJso<LabelOption> {
                                    position = LabelPosition("top")
                                    rotate = -90.0
                                    verticalAlign = VerticalAlign.middle
                                    align = TextAlign.right
                                    fontSize = StringOrDouble(9.0)
                                }
                            leaves =
                                unsafeJso {
                                    label =
                                        unsafeJso<LabelOption> {
                                            position = LabelPosition("bottom")
                                            rotate = -90.0
                                            verticalAlign = VerticalAlign.middle
                                            align = TextAlign.left
                                        }
                                }
                            animationDurationUpdate = echarts.options.AnimationDuration(750.0)
                        },
                    )
            }
        },
        tree.chart("tree-orient-bottom-top", "从下到上树状图", "From Bottom to Top Tree") {
            unsafeJso<EChartsOption> {
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.item
                            triggerOn = TooltipTriggerOns.mouseMove
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        treeSeries {
                            typedData = arrayOf(flareTree())
                            left = StringOrDouble("2%")
                            right = StringOrDouble("2%")
                            top = StringOrDouble("20%")
                            bottom = StringOrDouble("8%")
                            symbol = SymbolShape("emptyCircle")
                            orient = TreeOrients.bottomToTop
                            expandAndCollapse = true
                            label =
                                unsafeJso<LabelOption> {
                                    position = LabelPosition("bottom")
                                    rotate = 90.0
                                    verticalAlign = VerticalAlign.middle
                                    align = TextAlign.right
                                }
                            leaves =
                                unsafeJso {
                                    label =
                                        unsafeJso<LabelOption> {
                                            position = LabelPosition("top")
                                            rotate = 90.0
                                            verticalAlign = VerticalAlign.middle
                                            align = TextAlign.left
                                        }
                                }
                            emphasis = unsafeJso { focus = EmphasisFocuses.descendant }
                            animationDurationUpdate = echarts.options.AnimationDuration(750.0)
                        },
                    )
            }
        },
        tree.chart("tree-orient-right-left", "从右到左树状图", "From Right to Left Tree") {
            val flare = flareTree()
            collapseEverySecondChild(flare)
            unsafeJso<EChartsOption> {
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.item
                            triggerOn = TooltipTriggerOns.mouseMove
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        treeSeries {
                            typedData = arrayOf(flare)
                            top = StringOrDouble("1%")
                            left = StringOrDouble("15%")
                            bottom = StringOrDouble("1%")
                            right = StringOrDouble("7%")
                            symbolSize = SymbolSize(7.0)
                            orient = TreeOrients.rightToLeft
                            label = leafLabel()
                            leaves =
                                unsafeJso {
                                    label =
                                        unsafeJso<LabelOption> {
                                            position = LabelPosition("left")
                                            verticalAlign = VerticalAlign.middle
                                            align = TextAlign.right
                                        }
                                }
                            emphasis = unsafeJso { focus = EmphasisFocuses.descendant }
                            expandAndCollapse = true
                            animationDuration = echarts.options.AnimationDuration(550.0)
                            animationDurationUpdate = echarts.options.AnimationDuration(750.0)
                        },
                    )
            }
        },
    )
