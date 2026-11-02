package demo.json.examples.bar

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val barYCategoryStackJsonCategory = JsonCategory("json/examples/bar/BarYCategoryStackJson.kt")

val barYCategoryStackJsonExample: JsonExample =
    barYCategoryStackJsonCategory.example("bar-y-category-stack") {
            shadowAxisTooltip()
            legend { }
            xAxis { type = AxisType.Value }
            yAxis {
                type = AxisType.Category
                categories(*WEEK)
            }
            series {
                stackedBar("Direct", 320.0, 302.0, 301.0, 334.0, 390.0, 330.0, 320.0)
                stackedBar("Mail Ad", 120.0, 132.0, 101.0, 134.0, 90.0, 230.0, 210.0)
                stackedBar("Affiliate Ad", 220.0, 182.0, 191.0, 234.0, 290.0, 330.0, 310.0)
                stackedBar("Video Ad", 150.0, 212.0, 201.0, 154.0, 190.0, 330.0, 410.0)
                stackedBar("Search Engine", 820.0, 832.0, 901.0, 934.0, 1290.0, 1330.0, 1320.0)
            }
        }
