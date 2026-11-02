package demo.json.examples.tree

import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.nodes
import io.github.shijianjs.kmpechartsjson.*
import demo.json.examples.shared.flareTree
import demo.json.*

private val treePolylineJsonCategory = JsonCategory("json/examples/tree/TreePolylineJson.kt")

val treePolylineJsonExample: JsonExample =
    treePolylineJsonCategory.example("tree-polyline") {
            tooltip {
                trigger = TooltipTrigger.Item
                triggerOn = "mousemove"
            }
            series {
                tree {
                    name = "tree1"
                    data = nodes(flareTree())
                    top = str("10%")
                    left = str("8%")
                    bottom = str("22%")
                    right = str("20%")
                    symbolSize = NumberOrStringOrNumbers.of(7)
                    edgeShape = "polyline"
                    initialTreeDepth = 3
                    lineStyle { width = 2.0 }
                    label {
                        backgroundColor = "#fff".asColor()
                        position = "left"
                        verticalAlign = "middle"
                        align = "right"
                    }
                    emphasis { focus = "descendant" }
                    expandAndCollapse = true
                    animationDuration = 550.0
                    animationDurationUpdate = 750.0
                }
            }
        }
