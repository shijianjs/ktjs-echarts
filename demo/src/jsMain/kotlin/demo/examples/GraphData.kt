package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import zrender.ZRColor

/*
 * Graph pages that read their node list from a data file: the four renders of
 * `les-miserables.json` (plain, circular, force and overlap-hiding labels), plus the two dependency
 * graphs of NPM packages and of the WebKit modules.
 *
 * The tables are `demo/src/jsMain/resources/data/les-miserables.json`, `npmdepgraph.min10.json` and
 * `webkit-dep.json` (see the `SOURCE.md` of that folder). `graph` and `graph-force` keep the
 * `Math.random`-free data as is; only the node sizes and labels of the upstream `forEach` callback
 * are folded into the data items here.
 *
 * The `thumbnail` component of the two dependency pages is not ported (the site still has no
 * thumbnail); the miniature map only repeats the visible viewport.
 */

private val graphData = Category(Groups.GRAPH, "examples/GraphData.kt")

/** Shape of `les-miserables.json`: character nodes with a fixed position, their links and categories. */
private external interface LesMiserablesNode {
    val id: String
    val name: String
    val symbolSize: Double
    val x: Double
    val y: Double
    val value: Double
    val category: Double
}

/** One link of `les-miserables.json`, referencing nodes by id. */
private external interface LesMiserablesLink {
    val source: String
    val target: String
}

/** One node category of `les-miserables.json`; only the name is read. */
private external interface LesMiserablesCategory {
    val name: String
}

/** The whole `les-miserables.json` table. */
private external interface LesMiserablesData {
    val nodes: Array<LesMiserablesNode>
    val links: Array<LesMiserablesLink>
    val categories: Array<LesMiserablesCategory>
}

/**
 * The nodes of `les-miserables.json` as graph data items.
 *
 * [size] overrides the `symbolSize` of every node (`graph-force` draws all of them at 5), and
 * [labelAbove] shows the label only for the nodes whose own size is above it, the `node.label.show`
 * loop of the upstream callback.
 */
private fun lesNodes(
    table: LesMiserablesData,
    size: Double? = null,
    labelAbove: Double? = null,
): Array<GraphDataItem?> =
    Array(table.nodes.size) { index ->
        val node = table.nodes[index]
        GraphDataItem(
            unsafeJso<GraphNodeItemOption> {
                id = node.id
                name = node.name
                x = node.x
                y = node.y
                value = OptionDataValueOrArray(node.value)
                category = StringOrDouble(node.category)
                symbolSize = SymbolSize(size ?: node.symbolSize)
                if (labelAbove != null) {
                    label = unsafeJso<LabelOption> { show = node.symbolSize > labelAbove }
                }
            },
        )
    }

/** The links of `les-miserables.json`; the ends are node ids. */
private fun lesLinks(table: LesMiserablesData): Array<GraphEdgeItemOption> =
    Array(table.links.size) { index ->
        val link = table.links[index]
        unsafeJso<GraphEdgeItemOption> {
            source = StringOrDouble(link.source)
            target = StringOrDouble(link.target)
        }
    }

/** The categories of `les-miserables.json`, the legend of the four pages. */
private fun lesCategories(table: LesMiserablesData): Array<GraphCategoryItemOption> =
    Array(table.categories.size) { index ->
        unsafeJso<GraphCategoryItemOption> { name = table.categories[index].name }
    }

/** Legend names of the four `les-miserables` pages. */
private fun lesLegend(table: LesMiserablesData): Array<LegendOption> =
    one(unsafeJso<LegendOption> { data = legendNames(*Array(table.categories.size) { table.categories[it].name }) })

/** The shared series of the `les-miserables` pages; [block] sets the layout of the page. */
private fun lesSeries(
    table: LesMiserablesData,
    size: Double? = null,
    labelAbove: Double? = null,
    block: GraphSeriesOption.() -> Unit,
): GraphSeriesOption =
    graphSeries {
        name = "Les Miserables"
        typedData = lesNodes(table, size, labelAbove)
        links = lesLinks(table)
        categories = lesCategories(table)
        roam = BooleanOrString(true)
        label =
            unsafeJso<LabelOption> {
                position = LabelPosition("right")
                formatter = LabelFormatter("{b}")
            }
        lineStyle =
            unsafeJso<LineStyleOption> {
                color = ZRColor("source")
                curveness = 0.3
            }
        block()
    }

/** Shape of `npmdepgraph.min10.json`: placed package nodes plus edges that reference them by id. */
private external interface NpmGraphNode {
    val x: Double
    val y: Double
    val id: String
    val label: String
    val size: Double
    val color: String
}

/** One edge of `npmdepgraph.min10.json`. */
private external interface NpmGraphEdge {
    val sourceID: String
    val targetID: String
}

/** The whole `npmdepgraph.min10.json` table. */
private external interface NpmGraph {
    val nodes: Array<NpmGraphNode>
    val edges: Array<NpmGraphEdge>
}

/** The package nodes of `npmdepgraph.min10.json`, each one with its upstream colour. */
private fun npmNodes(table: NpmGraph): Array<GraphDataItem?> =
    Array(table.nodes.size) { index ->
        val node = table.nodes[index]
        GraphDataItem(
            unsafeJso<GraphNodeItemOption> {
                x = node.x
                y = node.y
                id = node.id
                name = node.label
                symbolSize = SymbolSize(node.size)
                itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor(node.color) }
            },
        )
    }

/** The edges of `npmdepgraph.min10.json`, the two ends are package ids. */
private fun npmEdges(table: NpmGraph): Array<GraphEdgeItemOption> =
    Array(table.edges.size) { index ->
        val edge = table.edges[index]
        unsafeJso<GraphEdgeItemOption> {
            source = StringOrDouble(edge.sourceID)
            target = StringOrDouble(edge.targetID)
        }
    }

/** Shape of `webkit-dep.json`: nodes without an id and links that reference the node index. */
private external interface WebkitNode {
    val name: String
    val value: Double
    val category: Double
}

/** One link of `webkit-dep.json`; the ends are node indexes. */
private external interface WebkitLink {
    val source: Double
    val target: Double
}

/** The whole `webkit-dep.json` table. */
private external interface WebkitDepData {
    val nodes: Array<WebkitNode>
    val links: Array<WebkitLink>
}

/** The five categories of `webkit-dep.json`, in the order the upstream legend lists them. */
private val WEBKIT_CATEGORIES = arrayOf("HTMLElement", "WebGL", "SVG", "CSS", "Other")

/** The nodes of `webkit-dep.json`; upstream assigns the index as the id, which the links rely on. */
private fun webkitNodes(table: WebkitDepData): Array<GraphDataItem?> =
    Array(table.nodes.size) { index ->
        val node = table.nodes[index]
        GraphDataItem(
            unsafeJso<GraphNodeItemOption> {
                id = "$index"
                name = node.name
                value = OptionDataValueOrArray(node.value)
                category = StringOrDouble(node.category)
            },
        )
    }

/** The links of `webkit-dep.json`; the numeric ends are node indexes. */
private fun webkitEdges(table: WebkitDepData): Array<GraphEdgeItemOption> =
    Array(table.links.size) { index ->
        val link = table.links[index]
        unsafeJso<GraphEdgeItemOption> {
            source = StringOrDouble(link.source)
            target = StringOrDouble(link.target)
        }
    }

val graphDataEntries: List<CatalogEntry> =
    listOf(
        graphData.interactive(
            "graph",
            "悲惨世界人物关系图",
            "Les Miserables",
            summaryZh = "《悲惨世界》的人物关系图，节点位置由数据给定；标签只显示在较大的节点上。",
            summaryEn = "The characters of Les Miserables at the positions of the table; only the larger nodes carry a label.",
        ) { chart ->
            loadJson("data/les-miserables.json").then { json ->
                val table = json.unsafeCast<LesMiserablesData>()
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        title =
                            one(
                                unsafeJso<TitleOption> {
                                    text = "Les Miserables"
                                    subtext = "Default layout"
                                    top = StringOrDouble("bottom")
                                    left = StringOrDouble("right")
                                },
                            )
                        tooltip = one(unsafeJso<TooltipOption> {})
                        legend = lesLegend(table)
                        animationDuration = AnimationDuration(1500.0)
                        animationEasingUpdate = AnimationEasings.quinticInOut
                        series =
                            one<SeriesOption>(
                                lesSeries(table, labelAbove = 30.0) {
                                    layout = GraphLayouts.none
                                    legendHoverLink = false
                                    emphasis =
                                        unsafeJso<EmphasisOption> {
                                            focus = EmphasisFocuses.adjacency
                                            lineStyle = unsafeJso<LineStyleOption> { width = 10.0 }
                                        }
                                },
                            )
                    },
                )
                null
            }
        },
        graphData.interactive(
            "graph-circular-layout",
            "悲惨世界人物关系图(环形布局)",
            "Les Miserables",
            summaryZh = "同一份人物关系数据换成环形布局，标签沿圆周旋转。",
            summaryEn = "The same character table on a circular layout, with the labels rotated along the ring.",
        ) { chart ->
            loadJson("data/les-miserables.json").then { json ->
                val table = json.unsafeCast<LesMiserablesData>()
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        title =
                            one(
                                unsafeJso<TitleOption> {
                                    text = "Les Miserables"
                                    subtext = "Circular layout"
                                    top = StringOrDouble("bottom")
                                    left = StringOrDouble("right")
                                },
                            )
                        tooltip = one(unsafeJso<TooltipOption> {})
                        legend = lesLegend(table)
                        animationDurationUpdate = AnimationDuration(1500.0)
                        animationEasingUpdate = AnimationEasings.quinticInOut
                        series =
                            one<SeriesOption>(
                                lesSeries(table, labelAbove = 30.0) {
                                    layout = GraphLayouts.circular
                                    circular = unsafeJso<GraphCircularOption> { rotateLabel = true }
                                },
                            )
                    },
                )
                null
            }
        },
        graphData.interactive(
            "graph-force",
            "力引导布局",
            "Force Layout",
            summaryZh = "人物关系数据交给力引导布局，所有节点统一画成小圆点，斥力 100。",
            summaryEn = "The character table on a force layout, every node drawn as a small dot with a repulsion of 100.",
        ) { chart ->
            loadJson("data/les-miserables.json").then { json ->
                val table = json.unsafeCast<LesMiserablesData>()
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        title =
                            one(
                                unsafeJso<TitleOption> {
                                    text = "Les Miserables"
                                    subtext = "Default layout"
                                    top = StringOrDouble("bottom")
                                    left = StringOrDouble("right")
                                },
                            )
                        tooltip = one(unsafeJso<TooltipOption> {})
                        legend = lesLegend(table)
                        series =
                            one<SeriesOption>(
                                lesSeries(table, size = 5.0) {
                                    layout = GraphLayouts.force
                                    label = unsafeJso<LabelOption> { position = LabelPosition("right") }
                                    force = unsafeJso<GraphForceOption> { repulsion = DoubleOrDoubleArray(100.0) }
                                },
                            )
                    },
                )
                null
            }
        },
        graphData.interactive(
            "graph-label-overlap",
            "关系图自动隐藏重叠标签",
            "Hide Overlapped Label",
            summaryZh = "所有节点都带标签，`labelLayout.hideOverlap` 把压在一起的那些隐藏掉。",
            summaryEn = "Every node carries a label; `labelLayout.hideOverlap` drops the ones that would collide.",
        ) { chart ->
            loadJson("data/les-miserables.json").then { json ->
                val table = json.unsafeCast<LesMiserablesData>()
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        tooltip = one(unsafeJso<TooltipOption> {})
                        legend = lesLegend(table)
                        series =
                            one<SeriesOption>(
                                lesSeries(table) {
                                    layout = GraphLayouts.none
                                    label =
                                        unsafeJso<LabelOption> {
                                            show = true
                                            position = LabelPosition("right")
                                            formatter = LabelFormatter("{b}")
                                        }
                                    labelLayout =
                                        LabelLayout(unsafeJso<LabelLayoutOption> { hideOverlap = true })
                                    scaleLimit =
                                        unsafeJso<ScaleLimitOption> {
                                            min = 0.4
                                            max = 2.0
                                        }
                                },
                            )
                    },
                )
                null
            }
        },
        graphData.interactive(
            "graph-npm",
            "NPM 依赖关系图",
            "NPM Dependencies",
            summaryZh = "七百多个 npm 包按预计算坐标铺开，边按来源包着色，悬停时高亮相邻节点。",
            summaryEn = "Seven hundred npm packages at precomputed positions; hovering a package highlights its neighbours.",
        ) { chart ->
            loadJson("data/npmdepgraph.min10.json").then { json ->
                val table = json.unsafeCast<NpmGraph>()
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        title = one(unsafeJso<TitleOption> { text = "NPM Dependencies" })
                        animationDurationUpdate = AnimationDuration(1500.0)
                        animationEasingUpdate = AnimationEasings.quinticInOut
                        series =
                            one<SeriesOption>(
                                graphSeries {
                                    layout = GraphLayouts.none
                                    typedData = npmNodes(table)
                                    edges = npmEdges(table)
                                    emphasis =
                                        unsafeJso<EmphasisOption> {
                                            focus = EmphasisFocuses.adjacency
                                            label =
                                                unsafeJso<LabelOption> {
                                                    position = LabelPosition("right")
                                                    show = true
                                                }
                                        }
                                    roam = BooleanOrString(true)
                                    roamTrigger = RoamTriggers.global
                                    lineStyle =
                                        unsafeJso<LineStyleOption> {
                                            width = 0.5
                                            curveness = 0.3
                                            opacity = 0.7
                                        }
                                },
                            )
                    },
                    true,
                )
                null
            }
        },
        graphData.interactive(
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
        },
    )
