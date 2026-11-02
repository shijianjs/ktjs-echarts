package demo.json.examples.line

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val lineSimpleJsonCategory = JsonCategory("json/examples/line/LineSimpleJson.kt")

val lineSimpleJsonExample: JsonExample =
    // ------------------------------------------------------------- basic lines
        lineSimpleJsonCategory.example("line-simple") {
            xAxis {
                type = AxisType.Category
                categories(*WEEK)
            }
            yAxis { type = AxisType.Value }
            series {
                line { values(150, 230, 224, 218, 135, 147, 260) }
            }
        }
