package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import kotlin.js.unsafeCast
import demo.examples.Category

private val graphglLargeInternetCategory = Category(Groups.GL, "examples/gl/GraphglLargeInternet.kt")

val graphglLargeInternetEntry: CatalogEntry =
    graphglLargeInternetCategory.interactive(
            "graphgl-large-internet",
            "GraphGL - 大规模互联网图谱",
            "GraphGL - Large Internet",
            "两万三千个节点、四万八千条边的互联网拓扑，按第四列的 31 个类别着色，一次布局只跑一步。",
            "An internet topology of twenty-three thousand nodes and forty-eight thousand edges, " +
                "coloured by its thirty-one categories with a single layout step per frame.",
        ) { chart ->
            loadJson("data/gl/internet.graph.json").then { json ->
                chart.setOption(largeInternetOption(json.unsafeCast<InternetGraph>()))
            }
        }
