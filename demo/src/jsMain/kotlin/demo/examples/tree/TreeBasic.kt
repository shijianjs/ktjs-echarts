package demo.examples.tree

import demo.CatalogEntry
import demo.Groups
import echarts.EmphasisFocuses
import echarts.TooltipTriggerOns
import echarts.TooltipTriggers
import echarts.options.EChartsOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.SymbolSize
import echarts.options.TooltipOption
import echarts.options.one
import echarts.options.treeSeries
import js.objects.unsafeJso
import demo.examples.Category

private val treeBasicCategory = Category(Groups.TREE, "examples/tree/TreeBasic.kt")

val treeBasicEntry: CatalogEntry =
    treeBasicCategory.chart("tree-basic", "从左到右树状图", "From Left to Right Tree") {
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
        }
