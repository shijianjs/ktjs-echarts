package demo.examples.tree

import demo.CatalogEntry
import demo.Groups
import echarts.EmphasisFocuses
import echarts.TooltipTriggerOns
import echarts.TooltipTriggers
import echarts.TreeLayouts
import echarts.options.EChartsOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.SymbolShape
import echarts.options.SymbolSize
import echarts.options.TooltipOption
import echarts.options.one
import echarts.options.treeSeries
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
                            animationDurationUpdate = echarts.options.AnimationDuration(750.0)
                            emphasis = unsafeJso { focus = EmphasisFocuses.descendant }
                        },
                    )
            }
        }
