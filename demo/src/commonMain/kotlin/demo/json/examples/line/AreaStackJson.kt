package demo.json.examples.line

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val areaStackJsonCategory = JsonCategory("json/examples/line/AreaStackJson.kt")

val areaStackJsonExample: JsonExample =
    areaStackJsonCategory.example("area-stack") {
            title { text = "Stacked Area Chart" }
            tooltip {
                trigger = TooltipTrigger.Axis
                axisPointer =
                    pointerOf("cross").apply {
                        label = AxisPointerLabelOption().apply { backgroundColor = "#6a7985".asColor() }
                    }
            }
            legend { entries("Email", "Union Ads", "Video Ads", "Direct", "Search Engine") }
            saveAsImageToolbox()
            xAxis {
                type = AxisType.Category
                boundaryGap = BoundaryGap.of(false)
                categories(*WEEK)
            }
            yAxis { type = AxisType.Value }
            series {
                line {
                    name = "Email"
                    stack = "Total"
                    areaStyle { }
                    emphasis = focusSeries()
                    values(120, 132, 101, 134, 90, 230, 210)
                }
                line {
                    name = "Union Ads"
                    stack = "Total"
                    areaStyle { }
                    emphasis = focusSeries()
                    values(220, 182, 191, 234, 290, 330, 310)
                }
                line {
                    name = "Video Ads"
                    stack = "Total"
                    areaStyle { }
                    emphasis = focusSeries()
                    values(150, 232, 201, 154, 190, 330, 410)
                }
                line {
                    name = "Direct"
                    stack = "Total"
                    areaStyle { }
                    emphasis = focusSeries()
                    values(320, 332, 301, 334, 390, 330, 320)
                }
                line {
                    name = "Search Engine"
                    stack = "Total"
                    areaStyle { }
                    emphasis = focusSeries()
                    label {
                        show = true
                        position = "top"
                    }
                    values(820, 932, 901, 934, 1290, 1330, 1320)
                }
            }
        }
