package demo.json.examples.dataset

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.*
import demo.json.*

private val datasetDefaultJsonCategory = JsonCategory("json/examples/dataset/DatasetDefaultJson.kt")

val datasetDefaultJsonExample: JsonExample =
    datasetDefaultJsonCategory.example("dataset-default") {
            legend { }
            tooltip { }
            dataset {
                rows(
                    row(
                        textValue("product"),
                        textValue("2012"),
                        textValue("2013"),
                        textValue("2014"),
                        textValue("2015"),
                        textValue("2016"),
                        textValue("2017"),
                    ),
                    namedRow("Milk Tea", 86.5, 92.1, 85.7, 83.1, 73.4, 55.1),
                    namedRow("Matcha Latte", 41.1, 30.4, 65.1, 53.3, 83.8, 98.7),
                    namedRow("Cheese Cocoa", 24.1, 67.2, 79.5, 86.4, 65.2, 82.5),
                    namedRow("Walnut Brownie", 55.2, 67.1, 69.2, 72.4, 53.9, 39.1),
                )
            }
            series {
                // No encode: the first column after `product` is used.
                yearPie(null, "25%", "30%")
                yearPie("2013", "75%", "30%")
                yearPie("2014", "25%", "75%")
                yearPie("2015", "75%", "75%")
            }
        }
