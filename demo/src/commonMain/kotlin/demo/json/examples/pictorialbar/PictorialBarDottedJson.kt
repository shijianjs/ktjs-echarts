package demo.json.examples.pictorialbar

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import demo.json.examples.shared.DOTTED_BAR
import demo.json.examples.shared.DOTTED_DAYS
import demo.json.examples.shared.DOTTED_LINE
import demo.json.*

private val pictorialBarDottedJsonCategory = JsonCategory("json/examples/pictorialbar/PictorialBarDottedJson.kt")

val pictorialBarDottedJsonExample: JsonExample =
    pictorialBarDottedJsonCategory.example("pictorialBar-dotted") {
            backgroundColor = "#0f375f".asColor()
            tooltip {
                trigger = TooltipTrigger.Axis
                axisPointer = AxisPointerOption().apply { type = "shadow" }
            }
            legend {
                entries("line", "bar")
                textStyle { color = "#ccc".asColor() }
            }
            xAxis {
                categories(*DOTTED_DAYS)
                axisLine {
                    lineStyle = LineStyleOption().apply { color = "#ccc".asColor() }
                }
            }
            yAxis {
                splitLine { show = false }
                axisLine {
                    lineStyle = LineStyleOption().apply { color = "#ccc".asColor() }
                }
            }
            series {
                line {
                    name = "line"
                    smooth = BooleanOrNumber.of(true)
                    showAllSymbol = BooleanOrString.of(true)
                    symbol = "emptyCircle"
                    symbolSize = NumberOrStringOrNumbers.of(15)
                    data = DOTTED_LINE.map { value -> valueOf(value) }
                }
                bar {
                    name = "bar"
                    barWidth = num(10)
                    itemStyle {
                        borderRadius = NumberOrStringOrNumbers.of(5)
                        color = "#14c8d4".asColor()
                    }
                    data = DOTTED_BAR.map { value -> valueOf(value) }
                }
                bar {
                    name = "line"
                    barGap = "-100%"
                    barWidth = num(10)
                    itemStyle { color = "rgba(20, 200, 212, 0.2)".asColor() }
                    z = -12.0
                    data = DOTTED_LINE.map { value -> valueOf(value) }
                }
                pictorialBar {
                    name = "dotted"
                    symbol = "rect"
                    itemStyle { color = "#0f375f".asColor() }
                    symbolRepeat = BooleanOrNumberOrString.of(true)
                    symbolSize = NumberOrStringOrNumbers.numbers(12, 4)
                    symbolMargin = num(1)
                    z = -10.0
                    data = DOTTED_LINE.map { value -> valueOf(value) }
                }
            }
        }
