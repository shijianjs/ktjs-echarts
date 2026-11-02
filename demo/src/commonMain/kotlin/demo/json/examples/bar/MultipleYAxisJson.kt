package demo.json.examples.bar

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val multipleYAxisJsonCategory = JsonCategory("json/examples/bar/MultipleYAxisJson.kt")

val multipleYAxisJsonExample: JsonExample =
    // ---------------------------------------------------------- multiple axes
        multipleYAxisJsonCategory.example("multiple-y-axis") {
            val axisColors = arrayOf("#5070dd", "#b6d634", "#505372")
            color = colors(*axisColors)
            tooltip {
                trigger = TooltipTrigger.Axis
                axisPointer = pointerOf("cross")
            }
            grid { right = str("20%") }
            showToolbox()
            legend { entries("Evaporation", "Precipitation", "Temperature") }
            xAxis {
                type = AxisType.Category
                axisTick { alignWithLabel = true }
                categories(*MONTHS)
            }
            // Upstream also sets `alignTicks: true`, which `AxisOption` does not model.
            yAxis {
                type = AxisType.Value
                name = "Evaporation"
                position = "right"
                axisLine {
                    show = true
                    lineStyle = lineColor(axisColors[0])
                }
                axisLabel { formatter = "{value} ml" }
            }
            yAxis {
                type = AxisType.Value
                name = "Precipitation"
                position = "right"
                offset = 80.0
                axisLine {
                    show = true
                    lineStyle = lineColor(axisColors[1])
                }
                axisLabel { formatter = "{value} ml" }
            }
            yAxis {
                type = AxisType.Value
                name = "温度"
                position = "left"
                axisLine {
                    show = true
                    lineStyle = lineColor(axisColors[2])
                }
                axisLabel { formatter = "{value} °C" }
            }
            series {
                bar {
                    name = "Evaporation"
                    values(2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3)
                }
                bar {
                    name = "Precipitation"
                    yAxisIndex = 1
                    values(2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3)
                }
                line {
                    name = "Temperature"
                    yAxisIndex = 2
                    values(2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2)
                }
            }
        }
