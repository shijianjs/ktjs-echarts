package demo.examples.graph

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.shared.Lcg
import demo.examples.shared.every
import demo.examples.shared.fixed

private val graphForceDynamicCategory = Category(Groups.GRAPH, "examples/graph/GraphForceDynamic.kt")

val graphForceDynamicEntry: CatalogEntry =
    graphForceDynamicCategory.interactive(
            "graph-force-dynamic",
            "动态增加图节点",
            "Graph Dynamic",
            summaryZh = "每 200ms 往力导向布局里加一个节点和一条边，节点从画布中心的那颗定住不动。",
            summaryEn = "A force layout that gains a node and an edge every 200 ms, growing out of one fixed node at the center.",
        ) { chart, holder ->
            ForceField(chart, Lcg(20160613), holder).start()
        }
