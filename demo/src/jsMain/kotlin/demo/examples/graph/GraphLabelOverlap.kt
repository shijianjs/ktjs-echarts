package demo.examples.graph

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import demo.examples.Category

private val graphLabelOverlapCategory = Category(Groups.GRAPH, "examples/graph/GraphLabelOverlap.kt")

val graphLabelOverlapEntry: CatalogEntry =
    graphLabelOverlapCategory.interactive(
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
        }
