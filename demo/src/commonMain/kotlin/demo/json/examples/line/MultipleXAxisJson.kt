package demo.json.examples.line

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val multipleXAxisJsonCategory = JsonCategory("json/examples/line/MultipleXAxisJson.kt")

val multipleXAxisJsonExample: JsonExample =
    multipleXAxisJsonCategory.example("multiple-x-axis") {
            color = colors("#5470C6", "#EE6666")
            // Upstream labels the pointer with the month and the value under it, through a callback.
            tooltip {
                trigger = TooltipTrigger.None
                axisPointer = pointerOf("cross")
            }
            legend { }
            grid {
                top = num(70)
                bottom = num(50)
            }
            xAxis {
                type = AxisType.Category
                axisTick { alignWithLabel = true }
                axisLine {
                    onZero = false
                    lineStyle = strokeOf("#EE6666")
                }
                data = monthsOf("2016")
            }
            xAxis {
                type = AxisType.Category
                axisTick { alignWithLabel = true }
                axisLine {
                    onZero = false
                    lineStyle = strokeOf("#5470C6")
                }
                data = monthsOf("2015")
            }
            yAxis { type = AxisType.Value }
            series {
                // The 2015 series is drawn on the second axis, the one labelled with 2015.
                line {
                    name = "Precipitation(2015)"
                    xAxisIndex = 1
                    smooth = BooleanOrNumber.of(true)
                    emphasis = focusSeries()
                    values(2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3)
                }
                line {
                    name = "Precipitation(2016)"
                    smooth = BooleanOrNumber.of(true)
                    emphasis = focusSeries()
                    values(3.9, 5.9, 11.1, 18.7, 48.3, 69.2, 231.6, 46.6, 55.4, 18.4, 10.3, 0.7)
                }
            }
        }
