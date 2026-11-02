package demo.json.examples.bar

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val barNegative2JsonCategory = JsonCategory("json/examples/bar/BarNegative2Json.kt")

val barNegative2JsonExample: JsonExample =
    barNegative2JsonCategory.example("bar-negative2") {
            title { text = "Bar Chart with Negative Value" }
            shadowAxisTooltip()
            grid {
                top = num(80)
                bottom = num(30)
            }
            xAxis {
                type = AxisType.Value
                position = "top"
                splitLine { lineStyle = LineStyleOption().apply { type = "dashed" } }
            }
            yAxis {
                type = AxisType.Category
                axisLine { show = false }
                axisLabel { show = false }
                axisTick { show = false }
                splitLine { show = false }
                categories("ten", "nine", "eight", "seven", "six", "five", "four", "three", "two", "one")
            }
            val costs = doubleArrayOf(-0.07, -0.09, 0.2, 0.44, -0.23, 0.08, -0.17, 0.47, -0.36, 0.18)
            series {
                bar {
                    name = "Cost"
                    stack = "Total"
                    label {
                        show = true
                        formatter = "{b}"
                    }
                    data =
                        costs.map { cost ->
                            dataItem {
                                value = valueOf(cost)
                                if (cost < 0) label { position = "right" }
                            }
                        }
                }
            }
        }
