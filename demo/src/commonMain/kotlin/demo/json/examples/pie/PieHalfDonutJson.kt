package demo.json.examples.pie

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import demo.json.*

private val pieHalfDonutJsonCategory = JsonCategory("json/examples/pie/PieHalfDonutJson.kt")

val pieHalfDonutJsonExample: JsonExample =
    pieHalfDonutJsonCategory.example("pie-half-donut") {
            tooltip { trigger = TooltipTrigger.Item }
            legend {
                top = str("5%")
                left = str("center")
            }
            series {
                pie {
                    name = "Access From"
                    radius = doughnutRadius
                    center = listOf(str("50%"), str("70%"))
                    startAngle = 180.0
                    endAngle = 360.0
                    data = ACCESS_FROM
                }
            }
        }
