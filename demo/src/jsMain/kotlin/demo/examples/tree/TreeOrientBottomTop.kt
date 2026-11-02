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
import echarts.options.SymbolShape
import echarts.options.TooltipOption
import echarts.options.one
import echarts.options.treeSeries
import js.objects.unsafeJso
import web.cssom.TextAlign
import web.cssom.VerticalAlign
import demo.examples.Category

private val treeOrientBottomTopCategory = Category(Groups.TREE, "examples/tree/TreeOrientBottomTop.kt")

val treeOrientBottomTopEntry: CatalogEntry =
    treeOrientBottomTopCategory.chart("tree-orient-bottom-top", "从下到上树状图", "From Bottom to Top Tree") {
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
        }
