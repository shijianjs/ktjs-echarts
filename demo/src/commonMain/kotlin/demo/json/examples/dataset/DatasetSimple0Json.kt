package demo.json.examples.dataset

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.*
import demo.json.*

private val datasetSimple0JsonCategory = JsonCategory("json/examples/dataset/DatasetSimple0Json.kt")

val datasetSimple0JsonExample: JsonExample =
    datasetSimple0JsonCategory.example("dataset-simple0") {
            legend { }
            tooltip { }
            dataset {
                rows(
                    row(textValue("product"), textValue("2015"), textValue("2016"), textValue("2017")),
                    namedRow("Matcha Latte", 43.3, 85.8, 93.7),
                    namedRow("Milk Tea", 83.1, 73.4, 55.1),
                    namedRow("Cheese Cocoa", 86.4, 65.2, 82.5),
                    namedRow("Walnut Brownie", 72.4, 53.9, 39.1),
                )
            }
            xAxis { type = AxisType.Category }
            yAxis { }
            // Each bar series is mapped to a column of `dataset.source` by default.
            series {
                bar { }
                bar { }
                bar { }
            }
        }
