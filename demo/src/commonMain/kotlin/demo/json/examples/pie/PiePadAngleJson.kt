package demo.json.examples.pie

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.*

private val piePadAngleJsonCategory = JsonCategory("json/examples/pie/PiePadAngleJson.kt")

val piePadAngleJsonExample: JsonExample =
    piePadAngleJsonCategory.example("pie-padAngle") {
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
                    padAngle = 5.0
                    itemStyle { borderRadius = NumberOrStringOrNumbers.of(10) }
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
