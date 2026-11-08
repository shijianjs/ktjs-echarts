package demo.examples.graph

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import demo.examples.Category
import demo.examples.shared.legendNames

private val graphWebkitDepCategory = Category(Groups.GRAPH, "examples/graph/GraphWebkitDep.kt")

val graphWebkitDepEntry: CatalogEntry =
    graphWebkitDepCategory.interactive(
            "graph-webkit-dep",
            "WebKit 模块关系依赖图",
            "Graph Webkit Dep",
            summaryZh = "WebKit 各模块之间的依赖关系，力引导布局、节点可拖动，悬停或拖动时看标签。",
            summaryEn = "Dependencies between the WebKit modules on a force layout; the nodes are draggable and labelled by name.",
        ) { chart ->
            loadJson("data/webkit-dep.json").then { json ->
                val table = json.unsafeCast<WebkitDepData>()
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        legend = one(unsafeJso<LegendOption> { data = legendNames(*WEBKIT_CATEGORIES) })
                        series =
                            one<SeriesOption>(
                                graphSeries {
                                    layout = GraphLayouts.force
                                    animation = false
                                    roam = BooleanOrString(true)
                                    roamTrigger = RoamTriggers.global
                                    scaleLimit =
                                        unsafeJso<ScaleLimitOption> {
                                            max = 8.0
                                            min = 0.5
                                        }
                                    label =
                                        unsafeJso<LabelOption> {
                                            position = LabelPosition("right")
                                            formatter = LabelFormatter("{b}")
                                        }
                                    draggable = true
                                    typedData = webkitNodes(table)
                                    categories =
                                        Array(WEBKIT_CATEGORIES.size) { index ->
                                            unsafeJso<GraphCategoryItemOption> { name = WEBKIT_CATEGORIES[index] }
                                        }
                                    force =
                                        unsafeJso<GraphForceOption> {
                                            edgeLength = DoubleOrDoubleArray(5.0)
                                            repulsion = DoubleOrDoubleArray(20.0)
                                            gravity = 0.2
                                        }
                                    edges = webkitEdges(table)
                                },
                            )
                    },
                )
                null
            }
        }
