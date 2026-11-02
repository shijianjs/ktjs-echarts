package demo.examples.tree

import demo.CatalogEntry
import demo.Groups
import echarts.EmphasisFocuses
import echarts.Orients
import echarts.TooltipTriggerOns
import echarts.TooltipTriggers
import echarts.options.EChartsOption
import echarts.options.LegendDataItemOption
import echarts.options.LegendOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.SymbolSize
import echarts.options.TooltipOption
import echarts.options.one
import echarts.options.treeSeries
import js.objects.unsafeJso
import zrender.ZRColor
import demo.examples.Category

private val treeLegendCategory = Category(Groups.TREE, "examples/tree/TreeLegend.kt")

val treeLegendEntry: CatalogEntry =
    treeLegendCategory.chart("tree-legend", "多棵树", "Multiple Trees") {
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
        }
