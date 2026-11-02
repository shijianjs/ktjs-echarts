package demo.json.examples.sankey

import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.linkItem
import io.github.shijianjs.kmpechartsjson.*
import demo.json.examples.shared.SIMPLE_NODESSankey
import demo.json.examples.shared.sankeyNode
import demo.json.*

private val sankeySimpleJsonCategory = JsonCategory("json/examples/sankey/SankeySimpleJson.kt")

val sankeySimpleJsonExample: JsonExample =
    sankeySimpleJsonCategory.example("sankey-simple") {
            series {
                sankey {
                    layout = "none"
                    emphasis { focus = "adjacency" }
                    data = SIMPLE_NODESSankey.map(::sankeyNode)
                    links =
                        listOf(
                            linkItem("a", "a1", 5.0),
                            linkItem("a", "a2", 3.0),
                            linkItem("b", "b1", 8.0),
                            linkItem("a", "b1", 3.0),
                            linkItem("b1", "a1", 1.0),
                            linkItem("b1", "c", 2.0),
                        )
                }
            }
        }
