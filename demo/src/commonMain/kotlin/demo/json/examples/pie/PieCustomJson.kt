package demo.json.examples.pie

import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.namedValues
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.*

private val pieCustomJsonCategory = JsonCategory("json/examples/pie/PieCustomJson.kt")

val pieCustomJsonExample: JsonExample =
    pieCustomJsonCategory.example("pie-custom") {
            backgroundColor = "#2c343c".asColor()
            title {
                text = "Customized Pie"
                left = str("center")
                top = num(20)
                textStyle { color = "#ccc".asColor() }
            }
            tooltip { trigger = TooltipTrigger.Item }
            visualMap {
                show = false
                min = 80.0
                max = 600.0
                inRange { colorLightness = listOf(0.0, 1.0) }
            }
            series {
                pie {
                    name = "Access From"
                    radius = NumberOrStringOrNumbers.of("55%")
                    center = listOf(str("50%"), str("50%"))
                    data =
                        namedValues(
                            "Video Ads" to 235,
                            "Union Ads" to 274,
                            "Email" to 310,
                            "Direct" to 335,
                            "Search Engine" to 400,
                        )
                    roseType = io.github.shijianjs.ktjs.echarts.json.BooleanOrString.of("radius")
                    label { color = "rgba(255, 255, 255, 0.3)".asColor() }
                    labelLine {
                        lineStyle { color = "rgba(255, 255, 255, 0.3)".asColor() }
                        smooth = NumberOrStringOrNumbers.of(0.2)
                        length = 10.0
                        length2 = num(20)
                    }
                    itemStyle {
                        color = "#c23531".asColor()
                        shadowBlur = 200.0
                        shadowColor = "rgba(0, 0, 0, 0.5)".asColor()
                    }
                    animationEasing = "elasticOut"
                    animationDelay = 100.0
                }
            }
        }
