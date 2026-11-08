package demo.json.examples.tree

import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.nodes
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.examples.shared.flareTree
import demo.json.*

private val treeRadialJsonCategory = JsonCategory("json/examples/tree/TreeRadialJson.kt")

val treeRadialJsonExample: JsonExample =
    treeRadialJsonCategory.example("tree-radial") {
            tooltip {
                trigger = TooltipTrigger.Item
                triggerOn = "mousemove"
            }
            series {
                tree {
                    data = nodes(flareTree())
                    top = str("18%")
                    bottom = str("14%")
                    layout = "radial"
                    symbol = "emptyCircle"
                    symbolSize = NumberOrStringOrNumbers.of(7)
                    initialTreeDepth = 3
                    animationDurationUpdate = 750.0
                    emphasis { focus = "descendant" }
                }
            }
        }
