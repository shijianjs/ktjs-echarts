package demo.json.examples.dataset

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.*
import demo.json.*

private val datasetSimple1JsonCategory = JsonCategory("json/examples/dataset/DatasetSimple1Json.kt")

val datasetSimple1JsonExample: JsonExample =
    datasetSimple1JsonCategory.example("dataset-simple1") {
            legend { }
            tooltip { }
            dataset {
                dimensions = dimensionsOf("product", "2015", "2016", "2017")
                source =
                    DatasetSource.ObjectRows(
                        listOf(
                            objectRow("Matcha Latte", 43.3, 85.8, 93.7),
                            objectRow("Milk Tea", 83.1, 73.4, 55.1),
                            objectRow("Cheese Cocoa", 86.4, 65.2, 82.5),
                            objectRow("Walnut Brownie", 72.4, 53.9, 39.1),
                        ),
                    )
            }
            xAxis { type = AxisType.Category }
            yAxis { }
            series {
                bar { }
                bar { }
                bar { }
            }
        }
