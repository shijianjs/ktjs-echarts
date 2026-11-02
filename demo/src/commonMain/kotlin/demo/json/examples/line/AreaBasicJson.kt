package demo.json.examples.line

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val areaBasicJsonCategory = JsonCategory("json/examples/line/AreaBasicJson.kt")

val areaBasicJsonExample: JsonExample =
    // ------------------------------------------------------------------ areas
        areaBasicJsonCategory.example("area-basic") {
            xAxis {
                type = AxisType.Category
                boundaryGap = BoundaryGap.of(false)
                categories(*WEEK)
            }
            yAxis { type = AxisType.Value }
            series {
                line {
                    areaStyle { }
                    values(820, 932, 901, 934, 1290, 1330, 1320)
                }
            }
        }
