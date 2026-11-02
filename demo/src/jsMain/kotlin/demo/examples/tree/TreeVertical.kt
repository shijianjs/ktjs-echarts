package demo.examples.tree

import demo.CatalogEntry
import demo.Groups
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

private val treeVerticalCategory = Category(Groups.TREE, "examples/tree/TreeVertical.kt")

val treeVerticalEntry: CatalogEntry =
    treeVerticalCategory.chart("tree-vertical", "从上到下树状图", "From Top to Bottom Tree") {
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
        }
