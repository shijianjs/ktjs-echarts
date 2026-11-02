package demo.json.examples.bar

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val barWaterfallJsonCategory = JsonCategory("json/examples/bar/BarWaterfallJson.kt")

val barWaterfallJsonExample: JsonExample =
    // ------------------------------------------------------------ waterfalls
        barWaterfallJsonCategory.example("bar-waterfall") {
            title {
                text = "Waterfall Chart"
                subtext = "Living Expenses in Shenzhen"
            }
            shadowAxisTooltip()
            grid {
                left = str("3%")
                right = str("4%")
                bottom = str("3%")
                containLabel = true
            }
            xAxis {
                type = AxisType.Category
                splitLine { show = false }
                categories("Total", "Rent", "Utilities", "Transportation", "Meals", "Other")
            }
            yAxis { type = AxisType.Value }
            series {
                bar {
                    name = "Placeholder"
                    stack = "Total"
                    itemStyle = invisibleStyle()
                    emphasis { itemStyle = invisibleStyle() }
                    values(0.0, 1700.0, 1400.0, 1200.0, 300.0, 0.0)
                }
                bar {
                    name = "Life Cost"
                    stack = "Total"
                    label {
                        show = true
                        position = "inside"
                    }
                    values(2900.0, 1200.0, 300.0, 200.0, 900.0, 300.0)
                }
            }
        }
