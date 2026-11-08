package demo.json.examples.line

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val lineStackJsonCategory = JsonCategory("json/examples/line/LineStackJson.kt")

val lineStackJsonExample: JsonExample =
    lineStackJsonCategory.example("line-stack") {
            title { text = "Stacked Line" }
            tooltip { trigger = TooltipTrigger.Axis }
            legend { entries("Email", "Union Ads", "Video Ads", "Direct", "Search Engine") }
            grid {
                left = str("3%")
                right = str("4%")
                bottom = str("3%")
                containLabel = true
            }
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
                    emphasis = focusSeries()
                    values(120, 132, 101, 134, 90, 230, 210)
                }
                line {
                    name = "Union Ads"
                    stack = "Total"
                    emphasis = focusSeries()
                    values(220, 182, 191, 234, 290, 330, 310)
                }
                line {
                    name = "Video Ads"
                    stack = "Total"
                    emphasis = focusSeries()
                    values(150, 232, 201, 154, 190, 330, 410)
                }
                line {
                    name = "Direct"
                    stack = "Total"
                    emphasis = focusSeries()
                    values(320, 332, 301, 334, 390, 330, 320)
                }
                line {
                    name = "Search Engine"
                    stack = "Total"
                    emphasis = focusSeries()
                    values(820, 932, 901, 934, 1290, 1330, 1320)
                }
            }
        }
