package demo.json.examples.tree

import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.nodes
import io.github.shijianjs.kmpechartsjson.*
import demo.json.examples.shared.flareTree
import demo.json.*

private val treeOrientBottomTopJsonCategory = JsonCategory("json/examples/tree/TreeOrientBottomTopJson.kt")

val treeOrientBottomTopJsonExample: JsonExample =
    treeOrientBottomTopJsonCategory.example("tree-orient-bottom-top") {
            tooltip {
                trigger = TooltipTrigger.Item
                triggerOn = "mousemove"
            }
            series {
                tree {
                    data = nodes(flareTree())
                    left = str("2%")
                    right = str("2%")
                    top = str("20%")
                    bottom = str("8%")
                    symbol = "emptyCircle"
                    // Upstream `orient: 'BT'`; the model enum only distinguishes the two axes.
                    orient = Orient.Vertical
                    expandAndCollapse = true
                    label {
                        position = "bottom"
                        rotate = 90.0
                        verticalAlign = "middle"
                        align = "right"
                    }
                    emphasis { focus = "descendant" }
                    animationDurationUpdate = 750.0
                }
            }
        }
