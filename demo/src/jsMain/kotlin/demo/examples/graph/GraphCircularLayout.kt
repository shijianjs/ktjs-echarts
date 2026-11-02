package demo.examples.graph

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import demo.examples.Category
import demo.examples.funnel.labels

private val graphCircularLayoutCategory = Category(Groups.GRAPH, "examples/graph/GraphCircularLayout.kt")

val graphCircularLayoutEntry: CatalogEntry =
    graphCircularLayoutCategory.interactive(
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
        }
