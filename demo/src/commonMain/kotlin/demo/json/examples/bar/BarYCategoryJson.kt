package demo.json.examples.bar

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val barYCategoryJsonCategory = JsonCategory("json/examples/bar/BarYCategoryJson.kt")

val barYCategoryJsonExample: JsonExample =
    barYCategoryJsonCategory.example("bar-y-category") {
            title { text = "World Population" }
            shadowAxisTooltip()
            legend { }
            xAxis {
                type = AxisType.Value
                boundaryGap = BoundaryGap.of(num(0), num(0.01))
            }
            yAxis {
                type = AxisType.Category
                categories("Brazil", "Indonesia", "USA", "India", "China", "World")
            }
            series {
                bar {
                    name = "2011"
                    values(18203.0, 23489.0, 29034.0, 104970.0, 131744.0, 630230.0)
                }
                bar {
                    name = "2012"
                    values(19325.0, 23438.0, 31000.0, 121594.0, 134141.0, 681807.0)
                }
            }
        }
