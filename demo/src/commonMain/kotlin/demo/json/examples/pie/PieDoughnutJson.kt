package demo.json.examples.pie

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.*

private val pieDoughnutJsonCategory = JsonCategory("json/examples/pie/PieDoughnutJson.kt")

val pieDoughnutJsonExample: JsonExample =
    pieDoughnutJsonCategory.example("pie-doughnut") {
            tooltip { trigger = TooltipTrigger.Item }
            legend {
                top = str("5%")
                left = str("center")
            }
            series {
                pie {
                    name = "Access From"
                    radius = doughnutRadius
                    avoidLabelOverlap = false
                    label {
                        show = false
                        position = "center"
                    }
                    emphasis(centerEmphasis())
                    labelLine { show = false }
                    data = ACCESS_FROM
                }
            }
        }
