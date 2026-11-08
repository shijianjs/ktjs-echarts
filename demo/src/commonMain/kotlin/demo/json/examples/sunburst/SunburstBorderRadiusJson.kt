package demo.json.examples.sunburst

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.*

private val sunburstBorderRadiusJsonCategory = JsonCategory("json/examples/sunburst/SunburstBorderRadiusJson.kt")

val sunburstBorderRadiusJsonExample: JsonExample =
    sunburstBorderRadiusJsonCategory.example("sunburst-borderRadius") {
            series {
                sunburst {
                    data = familyTree
                    radius = NumberOrStringOrNumbers.of(num(60), str("90%"))
                    itemStyle {
                        borderRadius = NumberOrStringOrNumbers.of(7)
                        borderWidth = 2.0
                    }
                    label { show = false }
                }
            }
        }
