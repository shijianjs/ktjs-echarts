package demo.examples.tree

import demo.CatalogEntry
import demo.Groups
import echarts.EmphasisFocuses
import echarts.TooltipTriggerOns
import echarts.TooltipTriggers
import echarts.options.EChartsOption
import echarts.options.LabelOption
import echarts.options.LabelPosition
import echarts.options.LineStyleOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.SymbolSize
import echarts.options.TooltipOption
import echarts.options.one
import echarts.options.treeSeries
import js.objects.unsafeJso
import web.cssom.TextAlign
import web.cssom.VerticalAlign
import zrender.ZRColor
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
                            edgeShape = echarts.TreeEdgeShapes.polyline
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
                            animationDuration = echarts.options.AnimationDuration(550.0)
                            animationDurationUpdate = echarts.options.AnimationDuration(750.0)
                        },
                    )
            }
        }
