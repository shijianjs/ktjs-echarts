package demo.json.examples.bar

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val barBackgroundJsonCategory = JsonCategory("json/examples/bar/BarBackgroundJson.kt")

val barBackgroundJsonExample: JsonExample =
    barBackgroundJsonCategory.example("bar-background") {
            xAxis {
                type = AxisType.Category
                categories(*WEEK)
            }
            yAxis { type = AxisType.Value }
            series {
                bar {
                    showBackground = true
                    backgroundStyle = fillStyle("rgba(180, 180, 180, 0.2)")
                    values(120.0, 200.0, 150.0, 80.0, 70.0, 110.0, 130.0)
                }
            }
        }
