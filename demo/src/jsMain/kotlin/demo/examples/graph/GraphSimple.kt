package demo.examples.graph

import demo.CatalogEntry
import demo.Groups
import echarts.AnimationEasings
import echarts.GraphLayouts
import echarts.options.DoubleOrDoubleArray
import echarts.options.EChartsOption
import echarts.options.LabelOption
import echarts.options.LineStyleOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.StringOrStringArray
import echarts.options.SymbolSize
import echarts.options.TitleOption
import echarts.options.TooltipOption
import echarts.options.graphSeries
import echarts.options.one
import js.objects.unsafeJso
import demo.examples.Category

private val graphSimpleCategory = Category(Groups.GRAPH, "examples/graph/GraphSimple.kt")

val graphSimpleEntry: CatalogEntry =
    graphSimpleCategory.chart("graph-simple", "Graph 简单示例", "Simple Graph") {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Basic Graph" })
                tooltip = one(unsafeJso<TooltipOption> {})
                animationDurationUpdate = echarts.options.AnimationDuration(1500.0)
                animationEasingUpdate = AnimationEasings.quinticInOut
                series =
                    arrayOf<SeriesOption>(
                        graphSeries {
                            layout = GraphLayouts.none
                            symbolSize = SymbolSize(50.0)
                            roam = echarts.options.BooleanOrString(true)
                            label = unsafeJso<LabelOption> { show = true }
                            edgeSymbol = StringOrStringArray(arrayOf("circle", "arrow"))
                            edgeSymbolSize = DoubleOrDoubleArray(arrayOf(4.0, 10.0))
                            edgeLabel = unsafeJso<LabelOption> { fontSize = echarts.options.StringOrDouble(20.0) }
                            typedData =
                                arrayOf(
                                    node("Node 1", 300.0, 300.0),
                                    node("Node 2", 800.0, 300.0),
                                    node("Node 3", 550.0, 100.0),
                                    node("Node 4", 550.0, 500.0),
                                )
                            links =
                                arrayOf(
                                    link("0", "1") {
                                        symbolSize = DoubleOrDoubleArray(arrayOf(5.0, 20.0))
                                        label = unsafeJso<LabelOption> { show = true }
                                        lineStyle = unsafeJso<LineStyleOption> { width = 5.0; curveness = 0.2 }
                                    },
                                    link("Node 2", "Node 1") {
                                        label = unsafeJso<LabelOption> { show = true }
                                        lineStyle = unsafeJso<LineStyleOption> { curveness = 0.2 }
                                    },
                                    link("Node 1", "Node 3"),
                                    link("Node 2", "Node 3"),
                                    link("Node 2", "Node 4"),
                                    link("Node 1", "Node 4"),
                                )
                            lineStyle = unsafeJso<LineStyleOption> { opacity = 0.9; width = 2.0; curveness = 0.0 }
                        },
                    )
            }
        }
