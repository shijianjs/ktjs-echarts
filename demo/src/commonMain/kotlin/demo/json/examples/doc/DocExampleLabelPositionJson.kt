package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.points
import io.github.shijianjs.kmpechartsjson.*
import demo.json.*

private val docExampleLabelPositionJsonCategory = JsonCategory("json/examples/doc/DocExampleLabelPositionJson.kt")

val docExampleLabelPositionJsonExample: JsonExample =
    docExampleLabelPositionJsonCategory.example("doc-example/label-position") {
            series {
                scatter {
                    symbolSize = NumberOrStringOrNumbers.of(360)
                    symbol = "roundRect"
                    data = points(1 to 1)
                    label {
                        position = "top"
                        align = "center"
                        verticalAlign = "middle"
                        distance = 5.0
                        show = true
                        formatter = "Label Text"
                        width = num(200)
                        height = num(60)
                        backgroundColor = "rgba(0, 255, 255, 0.3)".asColor()
                        borderColor = "#555".asColor()
                        borderWidth = 2.0
                        borderRadius = NumberOrStringOrNumbers.of(5)
                        padding = NumberOrStringOrNumbers.of(10)
                        fontSize = 18.0
                        shadowBlur = 3.0
                        shadowColor = "#888".asColor()
                        shadowOffsetX = 0.0
                        shadowOffsetY = 3.0
                        textBorderColor = "#000".asColor()
                        textBorderWidth = 3.0
                        color = "#fff".asColor()
                    }
                }
            }
            xAxis { max = num(2) }
            yAxis { max = num(2) }
        }
