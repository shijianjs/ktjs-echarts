package demo.examples.tree

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.EmphasisFocuses
import io.github.shijianjs.ktjs.echarts.TooltipTriggerOns
import io.github.shijianjs.ktjs.echarts.TooltipTriggers
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.SymbolSize
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.treeSeries
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
                            animationDuration = io.github.shijianjs.ktjs.echarts.options.AnimationDuration(550.0)
                            animationDurationUpdate = io.github.shijianjs.ktjs.echarts.options.AnimationDuration(750.0)
                        },
                    )
            }
        }
