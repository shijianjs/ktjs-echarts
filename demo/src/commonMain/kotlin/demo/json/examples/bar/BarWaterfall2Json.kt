package demo.json.examples.bar

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val barWaterfall2JsonCategory = JsonCategory("json/examples/bar/BarWaterfall2Json.kt")

val barWaterfall2JsonExample: JsonExample =
    barWaterfall2JsonCategory.example("bar-waterfall2") {
            title { text = "Accumulated Waterfall Chart" }
            shadowAxisTooltip()
            legend { entries("Expenses", "Income") }
            grid {
                left = str("3%")
                right = str("4%")
                bottom = str("3%")
                containLabel = true
            }
            xAxis {
                type = AxisType.Category
                categories(*(1..11).map { "Nov $it" }.toTypedArray())
            }
            yAxis { type = AxisType.Value }
            series {
                bar {
                    name = "Placeholder"
                    stack = "Total"
                    silent = true
                    itemStyle = invisibleStyle()
                    emphasis { itemStyle = invisibleStyle() }
                    values(0.0, 900.0, 1245.0, 1530.0, 1376.0, 1376.0, 1511.0, 1689.0, 1856.0, 1495.0, 1292.0)
                }
                bar {
                    name = "Income"
                    stack = "Total"
                    label {
                        show = true
                        position = "top"
                    }
                    data = waterfallRow(900.0, 345.0, 393.0, null, null, 135.0, 178.0, 286.0, null, null, null)
                }
                bar {
                    name = "Expenses"
                    stack = "Total"
                    label {
                        show = true
                        position = "bottom"
                    }
                    data = waterfallRow(null, null, null, 108.0, 154.0, null, null, null, 119.0, 361.0, 203.0)
                }
            }
        }
