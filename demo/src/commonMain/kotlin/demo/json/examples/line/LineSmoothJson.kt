package demo.json.examples.line

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val lineSmoothJsonCategory = JsonCategory("json/examples/line/LineSmoothJson.kt")

val lineSmoothJsonExample: JsonExample =
    lineSmoothJsonCategory.example("line-smooth") {
            xAxis {
                type = AxisType.Category
                categories(*WEEK)
            }
            yAxis { type = AxisType.Value }
            series {
                line {
                    smooth = BooleanOrNumber.of(true)
                    values(820, 932, 901, 934, 1290, 1330, 1320)
                }
            }
        }
