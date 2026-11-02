package demo.json.examples.scatter

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val scatterPunchCardJsonCategory = JsonCategory("json/examples/scatter/ScatterPunchCardJson.kt")

val scatterPunchCardJsonExample: JsonExample =
    scatterPunchCardJsonCategory.example("scatter-punchCard") {
            title { text = "Punch Card of Github" }
            legend {
                entries("Punch Card")
                left = str("right")
            }
            grid {
                left = num(2)
                bottom = num(10)
                right = num(10)
                containLabel = true
            }
            xAxis {
                type = AxisType.Category
                categories(*PUNCH_HOURS)
                boundaryGap = BoundaryGap.of(false)
                splitLine { show = true }
                axisLine { show = false }
            }
            yAxis {
                type = AxisType.Category
                categories(*PUNCH_DAYS)
                axisLine { show = false }
            }
            series {
                scatter {
                    name = "Punch Card"
                    data = punchCardItems(polar = false)
                }
            }
        }
