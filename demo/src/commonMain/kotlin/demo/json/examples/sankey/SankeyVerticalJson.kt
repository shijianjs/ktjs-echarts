package demo.json.examples.sankey

import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.linkItem
import io.github.shijianjs.kmpechartsjson.*
import demo.json.examples.shared.VERTICAL_NODES
import demo.json.examples.shared.sankeyNode
import demo.json.*

private val sankeyVerticalJsonCategory = JsonCategory("json/examples/sankey/SankeyVerticalJson.kt")

val sankeyVerticalJsonExample: JsonExample =
    sankeyVerticalJsonCategory.example("sankey-vertical") {
            tooltip {
                trigger = TooltipTrigger.Item
                triggerOn = "mousemove"
            }
            animation = false
            series {
                sankey {
                    bottom = str("10%")
                    emphasis { focus = "adjacency" }
                    data = VERTICAL_NODES.map(::sankeyNode)
                    links =
                        listOf(
                            linkItem("a", "a1", 5.0),
                            linkItem("e", "b", 3.0),
                            linkItem("a", "b1", 3.0),
                            linkItem("b1", "a1", 1.0),
                            linkItem("b1", "c", 2.0),
                            linkItem("b", "c", 1.0),
                        )
                    orient = Orient.Vertical
                    label { position = "top" }
                    lineStyle { color = "source".asColor() }
                }
            }
        }
