package demo.examples.graph

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import demo.examples.Category

private val graphNpmCategory = Category(Groups.GRAPH, "examples/graph/GraphNpm.kt")

val graphNpmEntry: CatalogEntry =
    graphNpmCategory.interactive(
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
        }
