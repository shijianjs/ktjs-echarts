package demo.json.examples.tree

import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.nodes
import io.github.shijianjs.kmpechartsjson.*
import demo.json.examples.shared.flareTree
import demo.json.*

private val treeBasicJsonCategory = JsonCategory("json/examples/tree/TreeBasicJson.kt")

val treeBasicJsonExample: JsonExample =
    treeBasicJsonCategory.example("tree-basic") {
            tooltip {
                trigger = TooltipTrigger.Item
                triggerOn = "mousemove"
            }
            series {
                tree {
                    data = nodes(flareTree(collapseAlternate = true))
                    top = str("1%")
                    left = str("7%")
                    bottom = str("1%")
                    right = str("20%")
                    symbolSize = NumberOrStringOrNumbers.of(7)
                    label {
                        position = "left"
                        verticalAlign = "middle"
                        align = "right"
                        fontSize = 9.0
                    }
                    emphasis { focus = "descendant" }
                    expandAndCollapse = true
                    animationDuration = 550.0
                    animationDurationUpdate = 750.0
                }
            }
        }
