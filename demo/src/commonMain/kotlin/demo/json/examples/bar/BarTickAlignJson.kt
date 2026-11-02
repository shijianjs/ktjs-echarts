package demo.json.examples.bar

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val barTickAlignJsonCategory = JsonCategory("json/examples/bar/BarTickAlignJson.kt")

val barTickAlignJsonExample: JsonExample =
    barTickAlignJsonCategory.example("bar-tick-align") {
            shadowAxisTooltip()
            grid {
                left = str("3%")
                right = str("4%")
                bottom = str("3%")
                containLabel = true
            }
            xAxis {
                type = AxisType.Category
                categories(*WEEK)
                axisTick { alignWithLabel = true }
            }
            yAxis { type = AxisType.Value }
            series {
                bar {
                    name = "Direct"
                    barWidth = str("60%")
                    values(10.0, 52.0, 200.0, 334.0, 390.0, 330.0, 220.0)
                }
            }
        }
