package demo.json.examples.heatmap

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.PUNCH_DAYS
import demo.json.examples.shared.PUNCH_HOURS
import demo.json.examples.shared.punchCardCells

private val heatmapCartesianJsonCategory = JsonCategory("json/examples/heatmap/HeatmapCartesianJson.kt")

// ---------------------------------------------------------------------------------------------
// Dates and generated prices
// ---------------------------------------------------------------------------------------------

val heatmapCartesianJsonExample: JsonExample =
    // ------------------------------------------------------------------- heatmap
        heatmapCartesianJsonCategory.example("heatmap-cartesian") {
            tooltip { }
            grid {
                height = str("50%")
                top = str("10%")
            }
            xAxis {
                type = AxisType.Category
                categories(*PUNCH_HOURS)
                splitArea { show = true }
            }
            yAxis {
                type = AxisType.Category
                categories(*PUNCH_DAYS)
                splitArea { show = true }
            }
            visualMap {
                min = 0.0
                max = 10.0
                calculable = true
                orient = Orient.Horizontal
                left = str("center")
                bottom = str("15%")
            }
            series {
                heatmap {
                    name = "Punch Card"
                    coordinateSystem = "cartesian2d"
                    data = punchCardCells()
                    label { show = true }
                    emphasis {
                        itemStyle =
                            ItemStyleOption().apply {
                                shadowBlur = 10.0
                                shadowColor = "rgba(0, 0, 0, 0.5)".asColor()
                            }
                    }
                }
            }
        }
