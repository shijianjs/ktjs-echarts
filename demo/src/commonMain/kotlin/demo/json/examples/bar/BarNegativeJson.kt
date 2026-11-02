package demo.json.examples.bar

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val barNegativeJsonCategory = JsonCategory("json/examples/bar/BarNegativeJson.kt")

val barNegativeJsonExample: JsonExample =
    barNegativeJsonCategory.example("bar-negative") {
            shadowAxisTooltip()
            legend { entries("Profit", "Expenses", "Income") }
            xAxis { type = AxisType.Value }
            yAxis {
                type = AxisType.Category
                axisTick { show = false }
                categories(*WEEK)
            }
            series {
                bar {
                    name = "Profit"
                    label {
                        show = true
                        position = "inside"
                    }
                    emphasis = focusSeries()
                    values(200.0, 170.0, 240.0, 244.0, 200.0, 220.0, 210.0)
                }
                bar {
                    name = "Income"
                    stack = "Total"
                    label { show = true }
                    emphasis = focusSeries()
                    values(320.0, 302.0, 341.0, 374.0, 390.0, 450.0, 420.0)
                }
                bar {
                    name = "Expenses"
                    stack = "Total"
                    label {
                        show = true
                        position = "left"
                    }
                    emphasis = focusSeries()
                    values(-120.0, -132.0, -101.0, -134.0, -190.0, -230.0, -210.0)
                }
            }
        }
