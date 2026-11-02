package demo.json.examples.line

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val lineStyleJsonCategory = JsonCategory("json/examples/line/LineStyleJson.kt")

val lineStyleJsonExample: JsonExample =
    lineStyleJsonCategory.example("line-style") {
            xAxis {
                type = AxisType.Category
                categories(*WEEK)
            }
            yAxis { type = AxisType.Value }
            series {
                line {
                    symbol = "triangle"
                    symbolSize = NumberOrStringOrNumbers.of(20)
                    lineStyle {
                        color = "#5470C6".asColor()
                        width = 4.0
                        type = "dashed"
                    }
                    itemStyle {
                        borderWidth = 3.0
                        borderColor = "#EE6666".asColor()
                        color = "yellow".asColor()
                    }
                    values(120, 200, 150, 80, 70, 110, 130)
                }
            }
        }
