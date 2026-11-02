package demo.json.examples.line

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val lineGradientJsonCategory = JsonCategory("json/examples/line/LineGradientJson.kt")

val lineGradientJsonExample: JsonExample =
    lineGradientJsonCategory.example("line-gradient") {
            val dates = LINE_GRADIENT_DATA.map { it.first }
            val plotValues = LINE_GRADIENT_DATA.map { it.second }
            // The first visual map colors by the value, the second by the position on the axis.
            visualMap {
                show = false
                type = VisualMapType.Continuous
                seriesIndex = listOf(0)
                min = 0.0
                max = 400.0
            }
            visualMap {
                show = false
                type = VisualMapType.Continuous
                seriesIndex = listOf(1)
                dimension = num(0)
                min = 0.0
                max = (dates.size - 1).toDouble()
            }
            title {
                left = str("center")
                text = "Gradient along the y axis"
            }
            title {
                top = str("55%")
                left = str("center")
                text = "Gradient along the x axis"
            }
            tooltip { trigger = TooltipTrigger.Axis }
            xAxis {
                type = AxisType.Category
                data = dates.map { textValue(it) }
            }
            xAxis {
                type = AxisType.Category
                gridIndex = 1
                data = dates.map { textValue(it) }
            }
            yAxis { type = AxisType.Value }
            yAxis {
                type = AxisType.Value
                gridIndex = 1
            }
            grid { bottom = str("60%") }
            grid { top = str("60%") }
            series {
                line {
                    showSymbol = false
                    data = plotValues.map { valueOf(it) }
                }
                line {
                    showSymbol = false
                    xAxisIndex = 1
                    yAxisIndex = 1
                    data = plotValues.map { valueOf(it) }
                }
            }
        }
