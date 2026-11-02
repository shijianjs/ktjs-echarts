package demo.json.examples.line

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val linePolar2JsonCategory = JsonCategory("json/examples/line/LinePolar2Json.kt")

val linePolar2JsonExample: JsonExample =
    linePolar2JsonCategory.example("line-polar2") {
            animationDuration = 2000.0
            title { text = "Two Value-Axes in Polar" }
            legend { entries("line") }
            polar { center = listOf(str("50%"), str("54%")) }
            tooltip {
                trigger = TooltipTrigger.Axis
                axisPointer = pointerOf("cross")
            }
            angleAxis { type = AxisType.Value }
            radiusAxis { min = num(0) }
            series {
                line {
                    coordinateSystem = "polar"
                    name = "line"
                    showSymbol = false
                    data = polarRose()
                }
            }
        }
