package demo.json.examples.pictorialbar

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.examples.shared.VELOCITY_GLYPHS
import demo.json.examples.shared.VELOCITY_HILL
import demo.json.examples.shared.VELOCITY_NAMES
import demo.json.examples.shared.VELOCITY_VALUES
import demo.json.*

private val pictorialBarVelocityJsonCategory = JsonCategory("json/examples/pictorialbar/PictorialBarVelocityJson.kt")

val pictorialBarVelocityJsonExample: JsonExample =
    pictorialBarVelocityJsonCategory.example("pictorialBar-velocity") {
            tooltip {
                trigger = TooltipTrigger.Axis
                axisPointer = AxisPointerOption().apply { type = "none" }
            }
            xAxis {
                categories(*VELOCITY_NAMES)
                axisTick { show = false }
                axisLine { show = false }
                axisLabel { color = "#e54035".asColor() }
            }
            yAxis {
                splitLine { show = false }
                axisTick { show = false }
                axisLine { show = false }
                axisLabel { show = false }
            }
            color = colors("#e54035")
            series {
                pictorialBar {
                    name = "hill"
                    barCategoryGap = "-130%"
                    symbol = VELOCITY_HILL
                    itemStyle { opacity = 0.5 }
                    emphasis { itemStyle { opacity = 1.0 } }
                    data = VELOCITY_VALUES.map { value -> valueOf(value) }
                    z = 10.0
                }
                pictorialBar {
                    name = "glyph"
                    barGap = "-100%"
                    symbolPosition = "end"
                    symbolSize = NumberOrStringOrNumbers.of(50)
                    symbolOffset = listOf(num(0), str("-120%"))
                    data = VELOCITY_GLYPHS
                }
            }
        }
