package demo.json.examples.pie

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import demo.json.*

private val pieSimpleJsonCategory = JsonCategory("json/examples/pie/PieSimpleJson.kt")

val pieSimpleJsonExample: JsonExample =
    pieSimpleJsonCategory.example("pie-simple") {
            title {
                text = "Referer of a Website"
                subtext = "Fake Data"
                left = str("center")
            }
            tooltip { trigger = TooltipTrigger.Item }
            legend {
                orient = Orient.Vertical
                left = str("left")
            }
            series {
                pie {
                    name = "Access From"
                    radius = NumberOrStringOrNumbers.of("50%")
                    data = ACCESS_FROM
                    emphasis {
                        itemStyle {
                            shadowBlur = 10.0
                            shadowOffsetX = 0.0
                            shadowColor = "rgba(0, 0, 0, 0.5)".asColor()
                        }
                    }
                }
            }
        }
