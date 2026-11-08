package demo.examples.tree

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.TooltipTriggerOns
import io.github.shijianjs.ktjs.echarts.TooltipTriggers
import io.github.shijianjs.ktjs.echarts.TreeOrients
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.LabelOption
import io.github.shijianjs.ktjs.echarts.options.LabelPosition
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.SymbolShape
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.treeSeries
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
                            animationDurationUpdate = io.github.shijianjs.ktjs.echarts.options.AnimationDuration(750.0)
                        },
                    )
            }
        }
