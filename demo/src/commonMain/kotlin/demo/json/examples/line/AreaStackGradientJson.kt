package demo.json.examples.line

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val areaStackGradientJsonCategory = JsonCategory("json/examples/line/AreaStackGradientJson.kt")

val areaStackGradientJsonExample: JsonExample =
    areaStackGradientJsonCategory.example("area-stack-gradient") {
            color = colors("#80FFA5", "#00DDFF", "#37A2FF", "#FF0087", "#FFBF00")
            title { text = "Gradient Stacked Area Chart" }
            tooltip {
                trigger = TooltipTrigger.Axis
                axisPointer =
                    pointerOf("cross").apply {
                        label = AxisPointerLabelOption().apply { backgroundColor = "#6a7985".asColor() }
                    }
            }
            legend { entries("Line 1", "Line 2", "Line 3", "Line 4", "Line 5") }
            saveAsImageToolbox()
            xAxis {
                type = AxisType.Category
                boundaryGap = BoundaryGap.of(false)
                categories(*WEEK)
            }
            yAxis { type = AxisType.Value }
            series {
                line {
                    name = "Line 1"
                    stack = "Total"
                    smooth = BooleanOrNumber.of(true)
                    lineStyle { width = 0.0 }
                    showSymbol = false
                    areaStyle {
                        opacity = 0.8
                        color = verticalGradient("rgb(128, 255, 165)", "rgb(1, 191, 236)")
                    }
                    emphasis = focusSeries()
                    values(140, 232, 101, 264, 90, 340, 250)
                }
                line {
                    name = "Line 2"
                    stack = "Total"
                    smooth = BooleanOrNumber.of(true)
                    lineStyle { width = 0.0 }
                    showSymbol = false
                    areaStyle {
                        opacity = 0.8
                        color = verticalGradient("rgb(0, 221, 255)", "rgb(77, 119, 255)")
                    }
                    emphasis = focusSeries()
                    values(120, 282, 111, 234, 220, 340, 310)
                }
                line {
                    name = "Line 3"
                    stack = "Total"
                    smooth = BooleanOrNumber.of(true)
                    lineStyle { width = 0.0 }
                    showSymbol = false
                    areaStyle {
                        opacity = 0.8
                        color = verticalGradient("rgb(55, 162, 255)", "rgb(116, 21, 219)")
                    }
                    emphasis = focusSeries()
                    values(320, 132, 201, 334, 190, 130, 220)
                }
                line {
                    name = "Line 4"
                    stack = "Total"
                    smooth = BooleanOrNumber.of(true)
                    lineStyle { width = 0.0 }
                    showSymbol = false
                    areaStyle {
                        opacity = 0.8
                        color = verticalGradient("rgb(255, 0, 135)", "rgb(135, 0, 157)")
                    }
                    emphasis = focusSeries()
                    values(220, 402, 231, 134, 190, 230, 120)
                }
                line {
                    name = "Line 5"
                    stack = "Total"
                    smooth = BooleanOrNumber.of(true)
                    lineStyle { width = 0.0 }
                    showSymbol = false
                    areaStyle {
                        opacity = 0.8
                        color = verticalGradient("rgb(255, 191, 0)", "rgb(224, 62, 76)")
                    }
                    emphasis = focusSeries()
                    label {
                        show = true
                        position = "top"
                    }
                    values(220, 302, 181, 234, 210, 290, 150)
                }
            }
        }
