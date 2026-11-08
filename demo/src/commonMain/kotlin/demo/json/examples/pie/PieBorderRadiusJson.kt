package demo.json.examples.pie

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.*

private val pieBorderRadiusJsonCategory = JsonCategory("json/examples/pie/PieBorderRadiusJson.kt")

val pieBorderRadiusJsonExample: JsonExample =
    pieBorderRadiusJsonCategory.example("pie-borderRadius") {
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
                    itemStyle {
                        borderRadius = NumberOrStringOrNumbers.of(10)
                        borderColor = "#fff".asColor()
                        borderWidth = 2.0
                    }
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
