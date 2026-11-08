package demo.json.examples.heatmap

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.FIELD_COLUMNS
import demo.json.examples.shared.FIELD_ROWS
import demo.json.examples.shared.fieldCategories
import demo.json.examples.shared.fieldCells
import demo.json.examples.shared.fieldColors

private val heatmapLargeJsonCategory = JsonCategory("json/examples/heatmap/HeatmapLargeJson.kt")

// ---------------------------------------------------------------------------------------------
// Dates and generated prices
// ---------------------------------------------------------------------------------------------

val heatmapLargeJsonExample: JsonExample =
    heatmapLargeJsonCategory.example("heatmap-large") {
            tooltip { }
            xAxis {
                type = AxisType.Category
                data = fieldCategories(FIELD_COLUMNS)
            }
            yAxis {
                type = AxisType.Category
                data = fieldCategories(FIELD_ROWS)
            }
            visualMap {
                min = 0.0
                max = 1.0
                calculable = true
                realtime = false
                inRange { color = fieldColors() }
            }
            series {
                heatmap {
                    name = "Gaussian"
                    coordinateSystem = "cartesian2d"
                    data = fieldCells()
                    progressive = BooleanOrNumber.of(1000)
                    animation = false
                    emphasis {
                        itemStyle =
                            ItemStyleOption().apply {
                                borderColor = "#333".asColor()
                                borderWidth = 1.0
                            }
                    }
                }
            }
        }
