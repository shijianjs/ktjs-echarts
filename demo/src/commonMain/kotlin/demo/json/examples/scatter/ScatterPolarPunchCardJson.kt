package demo.json.examples.scatter

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val scatterPolarPunchCardJsonCategory = JsonCategory("json/examples/scatter/ScatterPolarPunchCardJson.kt")

val scatterPolarPunchCardJsonExample: JsonExample =
    scatterPolarPunchCardJsonCategory.example("scatter-polar-punchCard") {
            title { text = "Punch Card of Github" }
            legend {
                entries("Punch Card")
                left = str("right")
            }
            polar {}
            angleAxis {
                type = AxisType.Category
                categories(*PUNCH_HOURS)
                boundaryGap = BoundaryGap.of(false)
                splitLine { show = true }
                axisLine { show = false }
            }
            radiusAxis {
                type = AxisType.Category
                categories(*PUNCH_DAYS)
                axisLine { show = false }
                axisLabel { rotate = 45.0 }
            }
            series {
                scatter {
                    name = "Punch Card"
                    coordinateSystem = "polar"
                    data = punchCardItems(polar = true)
                }
            }
        }
