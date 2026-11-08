package demo.json.examples.line

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val lineStepJsonCategory = JsonCategory("json/examples/line/LineStepJson.kt")

val lineStepJsonExample: JsonExample =
    lineStepJsonCategory.example("line-step") {
            title { text = "Step Line" }
            tooltip { trigger = TooltipTrigger.Axis }
            legend { entries("Step Start", "Step Middle", "Step End") }
            grid {
                left = str("3%")
                right = str("4%")
                bottom = str("3%")
                containLabel = true
            }
            saveAsImageToolbox()
            xAxis {
                type = AxisType.Category
                categories(*WEEK)
            }
            yAxis { type = AxisType.Value }
            series {
                line {
                    name = "Step Start"
                    step = BooleanOrString.of("start")
                    values(120, 132, 101, 134, 90, 230, 210)
                }
                line {
                    name = "Step Middle"
                    step = BooleanOrString.of("middle")
                    values(220, 282, 201, 234, 290, 430, 410)
                }
                line {
                    name = "Step End"
                    step = BooleanOrString.of("end")
                    values(450, 432, 401, 454, 590, 530, 510)
                }
            }
        }
