package demo.examples.tree

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.EmphasisFocuses
import io.github.shijianjs.ktjs.echarts.TooltipTriggerOns
import io.github.shijianjs.ktjs.echarts.TooltipTriggers
import io.github.shijianjs.ktjs.echarts.TreeOrients
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.LabelOption
import io.github.shijianjs.ktjs.echarts.options.LabelPosition
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.SymbolSize
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.treeSeries
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
                            animationDuration = io.github.shijianjs.ktjs.echarts.options.AnimationDuration(550.0)
                            animationDurationUpdate = io.github.shijianjs.ktjs.echarts.options.AnimationDuration(750.0)
                        },
                    )
            }
        }
