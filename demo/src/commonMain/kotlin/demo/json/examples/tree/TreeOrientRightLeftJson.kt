package demo.json.examples.tree

import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.nodes
import io.github.shijianjs.kmpechartsjson.*
import demo.json.examples.shared.flareTree
import demo.json.*

private val treeOrientRightLeftJsonCategory = JsonCategory("json/examples/tree/TreeOrientRightLeftJson.kt")

val treeOrientRightLeftJsonExample: JsonExample =
    treeOrientRightLeftJsonCategory.example("tree-orient-right-left") {
            tooltip {
                trigger = TooltipTrigger.Item
                triggerOn = "mousemove"
            }
            series {
                tree {
                    data = nodes(flareTree(collapseAlternate = true))
                    top = str("1%")
                    left = str("15%")
                    bottom = str("1%")
                    right = str("7%")
                    symbolSize = NumberOrStringOrNumbers.of(7)
                    // Upstream `orient: 'RL'`; the model enum only distinguishes the two axes.
                    orient = Orient.Horizontal
                    label {
                        position = "right"
                        verticalAlign = "middle"
                        align = "left"
                    }
                    emphasis { focus = "descendant" }
                    expandAndCollapse = true
                    animationDuration = 550.0
                    animationDurationUpdate = 750.0
                }
            }
        }
