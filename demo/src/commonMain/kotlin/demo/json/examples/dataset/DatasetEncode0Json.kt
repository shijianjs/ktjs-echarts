package demo.json.examples.dataset

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.ktjs.echarts.json.*
import kotlinx.serialization.json.*
import demo.json.*

private val datasetEncode0JsonCategory = JsonCategory("json/examples/dataset/DatasetEncode0Json.kt")

val datasetEncode0JsonExample: JsonExample =
    datasetEncode0JsonCategory.example("dataset-encode0") {
            dataset {
                rows(
                    row(textValue("score"), textValue("amount"), textValue("product")),
                    row(valueOf(89.3), valueOf(58212), textValue("Matcha Latte")),
                    row(valueOf(57.1), valueOf(78254), textValue("Milk Tea")),
                    row(valueOf(74.4), valueOf(41032), textValue("Cheese Cocoa")),
                    row(valueOf(50.1), valueOf(12755), textValue("Cheese Brownie")),
                    row(valueOf(89.7), valueOf(20145), textValue("Matcha Cocoa")),
                    row(valueOf(68.1), valueOf(79146), textValue("Tea")),
                    row(valueOf(19.6), valueOf(91852), textValue("Orange Juice")),
                    row(valueOf(10.6), valueOf(101852), textValue("Lemon Juice")),
                    row(valueOf(32.7), valueOf(20112), textValue("Walnut Brownie")),
                )
            }
            grid { containLabel = true }
            xAxis { name = "amount" }
            yAxis { type = AxisType.Category }
            visualMap {
                orient = Orient.Horizontal
                left = str("center")
                min = 10.0
                max = 100.0
                text = listOf("High Score", "Low Score")
                // The score column drives the color.
                dimension = num(0)
                inRange { color = colors("#65B581", "#FFCE34", "#FD665F") }
            }
            series {
                bar { encode = encodeOf("x" to "amount", "y" to "product") }
            }
        }
