package demo.json.examples.gauge

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import kotlinx.serialization.json.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.fill
import demo.json.examples.shared.gaugeAnchor
import demo.json.examples.shared.gaugeAxisLabel
import demo.json.examples.shared.gaugeAxisLine
import demo.json.examples.shared.gaugeAxisTick
import demo.json.examples.shared.gaugePointer
import demo.json.examples.shared.gaugeProgress
import demo.json.examples.shared.gaugeSplitLine
import demo.json.examples.shared.labelOf
import demo.json.examples.shared.stroke

private val gaugeTemperatureJsonCategory = JsonCategory("json/examples/gauge/GaugeTemperatureJson.kt")

// ---------------------------------------------------------------------------------------------
// Shared builders
// ---------------------------------------------------------------------------------------------

val gaugeTemperatureJsonExample: JsonExample =
    gaugeTemperatureJsonCategory.example("gauge-temperature") {
            series {
                gauge {
                    center = listOf(str("50%"), str("60%"))
                    startAngle = 200.0
                    endAngle = -20.0
                    min = 0.0
                    max = 60.0
                    splitNumber = 12
                    itemStyle = fill(color = "#FFAB91")
                    progress = gaugeProgress(show = true, width = 30.0)
                    pointer = gaugePointer(show = false)
                    axisLine = gaugeAxisLine(width = 30.0)
                    axisTick = gaugeAxisTick(lineStyle = stroke(color = "#999", width = 2.0))
                    splitLine = gaugeSplitLine(lineStyle = stroke(color = "#999", width = 3.0))
                    axisLabel = gaugeAxisLabel(fontSize = 20.0, color = "#999")
                    anchor = gaugeAnchor(show = false)
                    title = labelOf(show = false)
                    detail =
                        labelOf(
                            width = str("60%"),
                            lineHeight = 40.0,
                            borderRadius = 8,
                            fontSize = 60.0,
                            fontWeight = str("bolder"),
                            formatter = "{value} °C",
                            color = "inherit",
                            valueAnimation = true,
                        )
                    values(20.0)
                }
                gauge {
                    center = listOf(str("50%"), str("60%"))
                    startAngle = 200.0
                    endAngle = -20.0
                    min = 0.0
                    max = 60.0
                    itemStyle = fill(color = "#FD7347")
                    progress = gaugeProgress(show = true, width = 8.0)
                    pointer = gaugePointer(show = false)
                    axisLine = gaugeAxisLine(show = false)
                    axisTick = gaugeAxisTick(show = false)
                    splitLine = gaugeSplitLine(show = false)
                    axisLabel = gaugeAxisLabel(show = false)
                    detail = labelOf(show = false)
                    values(20.0)
                }
            }
        }
