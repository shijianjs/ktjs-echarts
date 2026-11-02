package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import demo.json.*

private val docExamplePictorialBarSymbolBoundingDataArrayJsonCategory = JsonCategory("json/examples/doc/DocExamplePictorialBarSymbolBoundingDataArrayJson.kt")

val docExamplePictorialBarSymbolBoundingDataArrayJsonExample: JsonExample =
    docExamplePictorialBarSymbolBoundingDataArrayJsonCategory.example("doc-example/pictorialBar-symbolBoundingDataArray") {
            val categories = listOf("DTA", "Fin", "Tool", "VTG", "CAAT", "UYL", "TC")
            val values = listOf(-12, -60, 34, 23, -21, 0, -25).map { valueOf(it) }
            tooltip {
                trigger = TooltipTrigger.Axis
                axisPointer = AxisPointerOption().apply { type = "shadow" }
            }
            grid { left = num(120) }
            xAxis {
                splitLine { show = false }
                axisLine { show = false }
                axisTick { show = false }
                axisLabel { show = false }
            }
            yAxis {
                categories(*categories.toTypedArray())
                axisTick { show = false }
                axisLine { lineStyle = LineStyleOption().apply { color = "#ccc".asColor() } }
                axisLabel {
                    margin = 40.0
                    color = "#999".asColor()
                    fontSize = 16.0
                }
                splitLine { show = true }
                position = "top"
            }
            series {
                starSeries("bg", values) {
                    tooltip = TooltipOption().apply { trigger = TooltipTrigger.None }
                    itemStyle { color = "#ddd".asColor() }
                    silent = true
                    symbolRepeat = BooleanOrNumberOrString.of("fixed")
                    symbolClip = false
                    symbolBoundingData = NumberOrNumbers.of(40)
                    z = -1.0
                }
                starSeries("bg", values) {
                    tooltip = TooltipOption().apply { trigger = TooltipTrigger.None }
                    itemStyle { color = "#ddd".asColor() }
                    silent = true
                    symbolRepeat = BooleanOrNumberOrString.of("fixed")
                    symbolClip = false
                    symbolBoundingData = NumberOrNumbers.of(-60)
                    z = -1.0
                }
                starSeries("data", values) {
                    symbolRepeat = BooleanOrNumberOrString.of(true)
                    symbolClip = true
                    symbolBoundingData = NumberOrNumbers.of(-60, 40)
                }
            }
        }
