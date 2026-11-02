package demo.json.examples.heatmap

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.FIELD_COLUMNS
import demo.json.examples.shared.FIELD_ROWS
import demo.json.examples.shared.fieldCategories
import demo.json.examples.shared.fieldCells
import demo.json.examples.shared.fieldColors

private val heatmapLargePiecewiseJsonCategory = JsonCategory("json/examples/heatmap/HeatmapLargePiecewiseJson.kt")

// ---------------------------------------------------------------------------------------------
// Dates and generated prices
// ---------------------------------------------------------------------------------------------

val heatmapLargePiecewiseJsonExample: JsonExample =
    heatmapLargePiecewiseJsonCategory.example("heatmap-large-piecewise") {
            tooltip { }
            grid {
                right = num(140)
                left = num(40)
            }
            xAxis {
                type = AxisType.Category
                data = fieldCategories(FIELD_COLUMNS)
            }
            yAxis {
                type = AxisType.Category
                data = fieldCategories(FIELD_ROWS)
            }
            visualMap {
                type = VisualMapType.Piecewise
                min = 0.0
                max = 1.0
                left = str("right")
                top = str("center")
                calculable = true
                realtime = false
                splitNumber = 8
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
