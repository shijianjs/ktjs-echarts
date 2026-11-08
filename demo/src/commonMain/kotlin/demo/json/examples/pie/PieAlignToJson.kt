package demo.json.examples.pie

import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.namedValues
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.*

private val pieAlignToJsonCategory = JsonCategory("json/examples/pie/PieAlignToJson.kt")

val pieAlignToJsonExample: JsonExample =
    pieAlignToJsonCategory.example("pie-alignTo") {
            val alignData =
                namedValues(
                    "Apples" to 70,
                    "Strawberries" to 68,
                    "Bananas" to 48,
                    "Oranges" to 40,
                    "Pears" to 32,
                    "Pineapples" to 27,
                    "Grapes" to 18,
                )
            title {
                text = "Pie label alignTo"
                left = str("center")
            }
            title {
                subtext = "alignTo: \"none\" (default)"
                left = str("16.67%")
                top = str("75%")
                textAlign = "center"
            }
            title {
                subtext = "alignTo: \"labelLine\""
                left = str("50%")
                top = str("75%")
                textAlign = "center"
            }
            title {
                subtext = "alignTo: \"edge\""
                left = str("83.33%")
                top = str("75%")
                textAlign = "center"
            }
            series {
                pie {
                    radius = NumberOrStringOrNumbers.of("25%")
                    center = listOf(str("50%"), str("50%"))
                    data = alignData
                    label {
                        position = "outer"
                        alignTo = "none"
                        bleedMargin = 5.0
                    }
                    left = num(0)
                    right = str("66.6667%")
                    top = num(0)
                    bottom = num(0)
                }
                pie {
                    radius = NumberOrStringOrNumbers.of("25%")
                    center = listOf(str("50%"), str("50%"))
                    data = alignData
                    label {
                        position = "outer"
                        alignTo = "labelLine"
                        bleedMargin = 5.0
                    }
                    left = str("33.3333%")
                    right = str("33.3333%")
                    top = num(0)
                    bottom = num(0)
                }
                pie {
                    radius = NumberOrStringOrNumbers.of("25%")
                    center = listOf(str("50%"), str("50%"))
                    data = alignData
                    label {
                        position = "outer"
                        alignTo = "edge"
                        margin = num(20)
                    }
                    left = str("66.6667%")
                    right = num(0)
                    top = num(0)
                    bottom = num(0)
                }
            }
        }
