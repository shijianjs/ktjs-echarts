package demo.json.examples.dataset

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.*
import demo.json.*

private val datasetSeriesLayoutByJsonCategory = JsonCategory("json/examples/dataset/DatasetSeriesLayoutByJson.kt")

val datasetSeriesLayoutByJsonExample: JsonExample =
    datasetSeriesLayoutByJsonCategory.example("dataset-series-layout-by") {
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
                    ),
                    namedRow("Matcha Latte", 41.1, 30.4, 65.1, 53.3),
                    namedRow("Milk Tea", 86.5, 92.1, 85.7, 83.1),
                    namedRow("Cheese Cocoa", 24.1, 67.2, 79.5, 86.4),
                )
            }
            xAxis {
                type = AxisType.Category
                gridIndex = 0
            }
            xAxis {
                type = AxisType.Category
                gridIndex = 1
            }
            yAxis { gridIndex = 0 }
            yAxis { gridIndex = 1 }
            grid { bottom = str("55%") }
            grid { top = str("55%") }
            series {
                // The first three series are laid out by row, in the first grid.
                bar { seriesLayoutBy = "row" }
                bar { seriesLayoutBy = "row" }
                bar { seriesLayoutBy = "row" }
                // The others are laid out by column, in the second grid.
                bar {
                    xAxisIndex = 1
                    yAxisIndex = 1
                }
                bar {
                    xAxisIndex = 1
                    yAxisIndex = 1
                }
                bar {
                    xAxisIndex = 1
                    yAxisIndex = 1
                }
                bar {
                    xAxisIndex = 1
                    yAxisIndex = 1
                }
            }
        }
