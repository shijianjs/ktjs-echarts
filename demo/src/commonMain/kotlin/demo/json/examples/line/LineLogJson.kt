package demo.json.examples.line

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val lineLogJsonCategory = JsonCategory("json/examples/line/LineLogJson.kt")

val lineLogJsonExample: JsonExample =
    lineLogJsonCategory.example("line-log") {
            title {
                text = "Log Axis"
                left = str("center")
            }
            tooltip {
                trigger = TooltipTrigger.Item
                formatter = "{a} <br/>{b} : {c}"
            }
            legend { left = str("left") }
            grid {
                left = str("3%")
                right = str("4%")
                bottom = str("3%")
                containLabel = true
            }
            xAxis {
                type = AxisType.Category
                name = "x"
                splitLine { show = false }
                categories("A", "B", "C", "D", "E", "F", "G", "H", "I")
            }
            // The `minorSplitLine` of the original has no field on `AxisOption`.
            yAxis {
                type = AxisType.Log
                name = "y"
            }
            series {
                line {
                    name = "Log2"
                    values(1, 3, 9, 27, 81, 247, 741, 2223, 6669)
                }
                line {
                    name = "Log3"
                    values(1, 2, 4, 8, 16, 32, 64, 128, 256)
                }
                line {
                    name = "Log1/2"
                    values(0.5, 0.25, 0.125, 0.0625, 0.03125, 0.015625, 0.0078125, 0.00390625, 0.001953125)
                }
            }
        }
