package demo.json.examples.bar

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val mixLineBarJsonCategory = JsonCategory("json/examples/bar/MixLineBarJson.kt")

val mixLineBarJsonExample: JsonExample =
    mixLineBarJsonCategory.example("mix-line-bar") {
            tooltip {
                trigger = TooltipTrigger.Axis
                axisPointer = pointerOf("cross")
            }
            showToolbox(magicTypes = listOf(SeriesType.Line, SeriesType.Bar))
            legend { entries("Evaporation", "Precipitation", "Temperature") }
            xAxis {
                type = AxisType.Category
                categories(*WEEK)
                axisPointer = pointerOf("shadow")
            }
            yAxis {
                type = AxisType.Value
                name = "Precipitation"
                min = num(0)
                max = num(250)
                interval = 50.0
                axisLabel { formatter = "{value} ml" }
            }
            yAxis {
                type = AxisType.Value
                name = "Temperature"
                min = num(0)
                max = num(25)
                interval = 5.0
                axisLabel { formatter = "{value} °C" }
            }
            series {
                bar {
                    name = "Evaporation"
                    values(2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3)
                }
                bar {
                    name = "Precipitation"
                    values(2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3)
                }
                line {
                    name = "Temperature"
                    yAxisIndex = 1
                    values(2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2)
                }
            }
        }
