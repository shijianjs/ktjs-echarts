package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.ktjs.echarts.json.*
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import demo.json.*

private val docExampleDatasetEncodeSimple0JsonCategory = JsonCategory("json/examples/doc/DocExampleDatasetEncodeSimple0Json.kt")

val docExampleDatasetEncodeSimple0JsonExample: JsonExample =
    docExampleDatasetEncodeSimple0JsonCategory.example("doc-example/dataset-encode-simple0") {
            dataset {
                source =
                    datasetRows(
                        listOf(textValue("score"), textValue("amount"), textValue("product")),
                        listOf(valueOf(89.3), valueOf(58212), textValue("Matcha Latte")),
                        listOf(valueOf(57.1), valueOf(78254), textValue("Milk Tea")),
                        listOf(valueOf(74.4), valueOf(41032), textValue("Cheese Cocoa")),
                        listOf(valueOf(50.1), valueOf(12755), textValue("Cheese Brownie")),
                        listOf(valueOf(89.7), valueOf(20145), textValue("Matcha Cocoa")),
                        listOf(valueOf(68.1), valueOf(79146), textValue("Tea")),
                        listOf(valueOf(19.6), valueOf(91852), textValue("Orange Juice")),
                        listOf(valueOf(10.6), valueOf(101852), textValue("Lemon Juice")),
                        listOf(valueOf(32.7), valueOf(20112), textValue("Walnut Brownie")),
                    )
            }
            grid { containLabel = true }
            xAxis { }
            yAxis { type = AxisType.Category }
            series {
                bar {
                    encode =
                        buildJsonObject {
                            put("x", "amount")
                            put("y", "product")
                        }
                }
            }
        }
