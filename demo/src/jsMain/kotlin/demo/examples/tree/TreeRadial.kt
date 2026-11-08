package demo.examples.tree

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.EmphasisFocuses
import io.github.shijianjs.ktjs.echarts.TooltipTriggerOns
import io.github.shijianjs.ktjs.echarts.TooltipTriggers
import io.github.shijianjs.ktjs.echarts.TreeLayouts
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.SymbolShape
import io.github.shijianjs.ktjs.echarts.options.SymbolSize
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.treeSeries
import js.objects.unsafeJso
import demo.examples.Category

private val treeRadialCategory = Category(Groups.TREE, "examples/tree/TreeRadial.kt")

val treeRadialEntry: CatalogEntry =
    treeRadialCategory.chart("tree-radial", "径向树状图", "Radial Tree") {
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
                            animationDurationUpdate = io.github.shijianjs.ktjs.echarts.options.AnimationDuration(750.0)
                            emphasis = unsafeJso { focus = EmphasisFocuses.descendant }
                        },
                    )
            }
        }
