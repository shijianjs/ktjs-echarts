package demo.examples.graph

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import demo.examples.Category

private val graphCategory = Category(Groups.GRAPH, "examples/graph/Graph.kt")

val graphEntry: CatalogEntry =
    graphCategory.interactive(
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
        }
