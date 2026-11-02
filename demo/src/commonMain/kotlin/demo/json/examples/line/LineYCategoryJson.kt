package demo.json.examples.line

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val lineYCategoryJsonCategory = JsonCategory("json/examples/line/LineYCategoryJson.kt")

val lineYCategoryJsonExample: JsonExample =
    lineYCategoryJsonCategory.example("line-y-category") {
            legend { entries("Altitude (km) vs. temperature (°C)") }
            tooltip {
                trigger = TooltipTrigger.Axis
                formatter = "Temperature : <br/>{b}km : {c}°C"
            }
            grid {
                left = str("3%")
                right = str("4%")
                bottom = str("3%")
                containLabel = true
            }
            xAxis {
                type = AxisType.Value
                axisLabel { formatter = "{value} °C" }
            }
            yAxis {
                type = AxisType.Category
                boundaryGap = BoundaryGap.of(false)
                axisLine { onZero = false }
                axisLabel { formatter = "{value} km" }
                categories("0", "10", "20", "30", "40", "50", "60", "70", "80")
            }
            series {
                line {
                    name = "Altitude (km) vs. temperature (°C)"
                    symbol = "circle"
                    symbolSize = NumberOrStringOrNumbers.of(10)
                    smooth = BooleanOrNumber.of(true)
                    lineStyle {
                        width = 3.0
                        shadowColor = "rgba(0,0,0,0.3)".asColor()
                        shadowBlur = 10.0
                        shadowOffsetY = 8.0
                    }
                    values(15, -50, -56.5, -46.5, -22.1, -2.5, -27.7, -55.7, -76.5)
                }
            }
        }
