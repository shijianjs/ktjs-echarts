package demo.examples.tree

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.EmphasisFocuses
import io.github.shijianjs.ktjs.echarts.Orients
import io.github.shijianjs.ktjs.echarts.TooltipTriggerOns
import io.github.shijianjs.ktjs.echarts.TooltipTriggers
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.LegendDataItemOption
import io.github.shijianjs.ktjs.echarts.options.LegendOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.SymbolSize
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.treeSeries
import js.objects.unsafeJso
import io.github.shijianjs.ktjs.zrender.ZRColor
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
                            animationDuration = io.github.shijianjs.ktjs.echarts.options.AnimationDuration(550.0)
                            animationDurationUpdate = io.github.shijianjs.ktjs.echarts.options.AnimationDuration(750.0)
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
                            animationDuration = io.github.shijianjs.ktjs.echarts.options.AnimationDuration(550.0)
                            animationDurationUpdate = io.github.shijianjs.ktjs.echarts.options.AnimationDuration(750.0)
                        },
                    )
            }
        }
