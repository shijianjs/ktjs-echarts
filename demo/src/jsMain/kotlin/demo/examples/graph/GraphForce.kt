package demo.examples.graph

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import demo.examples.Category
import demo.examples.shared.every

private val graphForceCategory = Category(Groups.GRAPH, "examples/graph/GraphForce.kt")

val graphForceEntry: CatalogEntry =
    graphForceCategory.interactive(
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
        }
