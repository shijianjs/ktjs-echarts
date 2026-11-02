package demo.examples.tree

import demo.CatalogEntry
import demo.Groups
import echarts.EmphasisFocuses
import echarts.TooltipTriggerOns
import echarts.TooltipTriggers
import echarts.TreeOrients
import echarts.options.EChartsOption
import echarts.options.LabelOption
import echarts.options.LabelPosition
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.SymbolSize
import echarts.options.TooltipOption
import echarts.options.one
import echarts.options.treeSeries
import js.objects.unsafeJso
import web.cssom.TextAlign
import web.cssom.VerticalAlign
import demo.examples.Category

private val treeOrientRightLeftCategory = Category(Groups.TREE, "examples/tree/TreeOrientRightLeft.kt")

val treeOrientRightLeftEntry: CatalogEntry =
    treeOrientRightLeftCategory.chart("tree-orient-right-left", "从右到左树状图", "From Right to Left Tree") {
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
        }
