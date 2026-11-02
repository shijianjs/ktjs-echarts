package demo.json.examples.line

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val linePolarJsonCategory = JsonCategory("json/examples/line/LinePolarJson.kt")

val linePolarJsonExample: JsonExample =
    linePolarJsonCategory.example("line-polar") {
            title { text = "Two Value-Axes in Polar" }
            legend { entries("line") }
            polar { }
            tooltip {
                trigger = TooltipTrigger.Axis
                axisPointer = pointerOf("cross")
            }
            // The `startAngle: 0` of the original has no field on `AxisOption`.
            angleAxis { type = AxisType.Value }
            radiusAxis { }
            series {
                line {
                    coordinateSystem = "polar"
                    name = "line"
                    data = polarSpiral()
                }
            }
        }
