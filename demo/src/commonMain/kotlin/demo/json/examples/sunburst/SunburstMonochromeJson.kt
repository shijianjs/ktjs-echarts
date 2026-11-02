package demo.json.examples.sunburst

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import demo.json.*

private val sunburstMonochromeJsonCategory = JsonCategory("json/examples/sunburst/SunburstMonochromeJson.kt")

val sunburstMonochromeJsonExample: JsonExample =
    sunburstMonochromeJsonCategory.example("sunburst-monochrome") {
            series {
                sunburst {
                    radius = NumberOrStringOrNumbers.of(str("15%"), str("80%"))
                    emphasis { focus = "ancestor" }
                    data = monochromeTree
                    levels = emptyList()
                    itemStyle {
                        color = "#ddd".asColor()
                        borderWidth = 2.0
                    }
                }
            }
        }
