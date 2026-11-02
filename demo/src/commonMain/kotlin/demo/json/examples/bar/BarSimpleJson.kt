package demo.json.examples.bar

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val barSimpleJsonCategory = JsonCategory("json/examples/bar/BarSimpleJson.kt")

val barSimpleJsonExample: JsonExample =
    // ---------------------------------------------------------------- basic bars
        barSimpleJsonCategory.example("bar-simple") {
            xAxis {
                type = AxisType.Category
                categories(*WEEK)
            }
            yAxis { type = AxisType.Value }
            series {
                bar { values(120.0, 200.0, 150.0, 80.0, 70.0, 110.0, 130.0) }
            }
        }
