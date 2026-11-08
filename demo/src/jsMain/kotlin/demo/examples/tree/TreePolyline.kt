package demo.examples.tree

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.EmphasisFocuses
import io.github.shijianjs.ktjs.echarts.TooltipTriggerOns
import io.github.shijianjs.ktjs.echarts.TooltipTriggers
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.LabelOption
import io.github.shijianjs.ktjs.echarts.options.LabelPosition
import io.github.shijianjs.ktjs.echarts.options.LineStyleOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.SymbolSize
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.treeSeries
import js.objects.unsafeJso
import web.cssom.TextAlign
import web.cssom.VerticalAlign
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category

private val treePolylineCategory = Category(Groups.TREE, "examples/tree/TreePolyline.kt")

val treePolylineEntry: CatalogEntry =
    treePolylineCategory.chart("tree-polyline", "折线树图", "Tree with Polyline Edge") {
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
                            id = "0"
                            name = "tree1"
                            typedData = arrayOf(flareTree())
                            top = StringOrDouble("10%")
                            left = StringOrDouble("8%")
                            bottom = StringOrDouble("22%")
                            right = StringOrDouble("20%")
                            symbolSize = SymbolSize(7.0)
                            edgeShape = io.github.shijianjs.ktjs.echarts.TreeEdgeShapes.polyline
                            edgeForkPosition = StringOrDouble("63%")
                            initialTreeDepth = 3.0
                            lineStyle = unsafeJso<LineStyleOption> { width = 2.0 }
                            label =
                                unsafeJso<LabelOption> {
                                    backgroundColor = ZRColor("#fff")
                                    position = LabelPosition("left")
                                    verticalAlign = VerticalAlign.middle
                                    align = TextAlign.right
                                }
                            leaves = leaves()
                            emphasis = unsafeJso { focus = EmphasisFocuses.descendant }
                            expandAndCollapse = true
                            animationDuration = io.github.shijianjs.ktjs.echarts.options.AnimationDuration(550.0)
                            animationDurationUpdate = io.github.shijianjs.ktjs.echarts.options.AnimationDuration(750.0)
                        },
                    )
            }
        }
