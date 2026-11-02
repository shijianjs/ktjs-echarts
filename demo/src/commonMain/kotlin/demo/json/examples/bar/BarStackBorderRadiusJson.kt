package demo.json.examples.bar

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val barStackBorderRadiusJsonCategory = JsonCategory("json/examples/bar/BarStackBorderRadiusJson.kt")

val barStackBorderRadiusJsonExample: JsonExample =
    // --------------------------------------------------------- stacked tails
        barStackBorderRadiusJsonCategory.example("bar-stack-borderRadius") {
            xAxis {
                type = AxisType.Category
                categories(*WEEK)
            }
            yAxis { type = AxisType.Value }
            series {
                roundedStack(0)
                roundedStack(1)
                roundedStack(2)
                roundedStack(3)
                roundedStack(4)
            }
        }
