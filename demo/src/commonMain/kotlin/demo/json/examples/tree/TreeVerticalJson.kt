package demo.json.examples.tree

import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.nodes
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.examples.shared.flareTree
import demo.json.*

private val treeVerticalJsonCategory = JsonCategory("json/examples/tree/TreeVerticalJson.kt")

val treeVerticalJsonExample: JsonExample =
    treeVerticalJsonCategory.example("tree-vertical") {
            tooltip {
                trigger = TooltipTrigger.Item
                triggerOn = "mousemove"
            }
            series {
                tree {
                    data = nodes(flareTree())
                    left = str("2%")
                    right = str("2%")
                    top = str("8%")
                    bottom = str("20%")
                    symbol = "emptyCircle"
                    orient = Orient.Vertical
                    expandAndCollapse = true
                    label {
                        position = "top"
                        rotate = -90.0
                        verticalAlign = "middle"
                        align = "right"
                        fontSize = 9.0
                    }
                    emphasis { focus = "descendant" }
                    animationDurationUpdate = 750.0
                }
            }
        }
