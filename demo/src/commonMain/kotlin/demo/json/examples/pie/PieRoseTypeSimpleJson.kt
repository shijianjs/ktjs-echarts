package demo.json.examples.pie

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.*

private val pieRoseTypeSimpleJsonCategory = JsonCategory("json/examples/pie/PieRoseTypeSimpleJson.kt")

val pieRoseTypeSimpleJsonExample: JsonExample =
    pieRoseTypeSimpleJsonCategory.example("pie-roseType-simple") {
            legend { top = str("bottom") }
            series {
                pie {
                    name = "Nightingale Chart"
                    radius = NumberOrStringOrNumbers.of(num(50), num(250))
                    center = listOf(str("50%"), str("50%"))
                    roseType = io.github.shijianjs.ktjs.echarts.json.BooleanOrString.of("area")
                    itemStyle { borderRadius = NumberOrStringOrNumbers.of(8) }
                    data = roseData(40, 38, 32, 30, 28, 26, 22, 18)
                }
            }
        }
