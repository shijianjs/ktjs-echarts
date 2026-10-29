package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.AnimationEasings
import echarts.GraphLayouts
import echarts.options.DoubleOrDoubleArray
import echarts.options.EChartsOption
import echarts.options.GraphDataItem
import echarts.options.GraphEdgeItemOption
import echarts.options.GraphNodeItemOption
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

/*
 * Graph examples ported from `public/examples/ts/graph*.ts` of apache/echarts-examples.
 *
 * The two pages here need no data file. The four renders of `les-miserables.json` and the two
 * dependency graphs live in `GraphData.kt`, `graph-life-expectancy`, `graph-grid` and
 * `graph-force-dynamic` in `GraphExtra.kt`.
 */

private val graph = Category(Groups.GRAPH, "examples/Graph.kt")

/** One node with an explicit position. */
private fun node(name: String, x: Double, y: Double): GraphDataItem =
    GraphDataItem(
        unsafeJso<GraphNodeItemOption> {
            this.name = name
            this.x = x
            this.y = y
        },
    )

/** A node of the force layout, identified by id. */
private fun idNode(id: Int): GraphDataItem =
    GraphDataItem(unsafeJso<GraphNodeItemOption> { this.id = "$id" })

/** One link between two nodes. */
private fun link(source: String, target: String, block: GraphEdgeItemOption.() -> Unit = {}): GraphEdgeItemOption =
    unsafeJso {
        this.source = StringOrDouble(source)
        this.target = StringOrDouble(target)
        block()
    }

val graphEntries: List<CatalogEntry> =
    listOf(
        graph.chart("graph-simple", "Graph 简单示例", "Simple Graph") {
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
        },
        graph.chart("graph-force2", "力引导布局", "Force Layout") {
            unsafeJso<EChartsOption> {
                series = Array<SeriesOption>(16) { index ->
                    val count = index + 2
                    graphSeries {
                        layout = GraphLayouts.force
                        animation = false
                        typedData = Array(count) { position -> idNode(position) }
                        left = StringOrDouble("${index % 4 * 25}%")
                        top = StringOrDouble("${index / 4 * 25}%")
                        width = StringOrDouble("25%")
                        height = StringOrDouble("25%")
                        force =
                            unsafeJso {
                                repulsion = DoubleOrDoubleArray(60.0)
                                edgeLength = DoubleOrDoubleArray(2.0)
                            }
                        edges =
                            Array(count) { position ->
                                link("$position", "${(position + 1) % count}")
                            }
                    }
                }
            }
        },
    )
