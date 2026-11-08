package demo.json.examples.treemap

import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.nodeItem
import demo.json.nodes
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.*

private val treemapSimpleJsonCategory = JsonCategory("json/examples/treemap/TreemapSimpleJson.kt")

val treemapSimpleJsonExample: JsonExample =
    treemapSimpleJsonCategory.example("treemap-simple") {
            series {
                treemap {
                    data = nodes(
                        nodeItem(
                            "nodeA",
                            10,
                            nodeItem("nodeAa", 4),
                            nodeItem("nodeAb", 6),
                        ),
                        nodeItem(
                            "nodeB",
                            20,
                            nodeItem(
                                "nodeBa",
                                20,
                                nodeItem("nodeBa1", 20),
                            ),
                        ),
                    )
                }
            }
        }
