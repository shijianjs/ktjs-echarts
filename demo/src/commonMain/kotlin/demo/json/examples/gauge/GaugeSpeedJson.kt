package demo.json.examples.gauge

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import kotlinx.serialization.json.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.WIDE_NEEDLE_PATH
import demo.json.examples.shared.fill
import demo.json.examples.shared.gaugeAxisLabel
import demo.json.examples.shared.gaugeAxisLine
import demo.json.examples.shared.gaugeAxisTick
import demo.json.examples.shared.gaugePointer
import demo.json.examples.shared.gaugeProgress
import demo.json.examples.shared.gaugeSplitLine
import demo.json.examples.shared.labelOf
import demo.json.examples.shared.speedRich
import demo.json.examples.shared.stroke

private val gaugeSpeedJsonCategory = JsonCategory("json/examples/gauge/GaugeSpeedJson.kt")

// ---------------------------------------------------------------------------------------------
// Shared builders
// ---------------------------------------------------------------------------------------------

val gaugeSpeedJsonExample: JsonExample =
    gaugeSpeedJsonCategory.example("gauge-speed") {
            series {
                gauge {
                    startAngle = 180.0
                    endAngle = 0.0
                    min = 0.0
                    max = 240.0
                    splitNumber = 12
                    itemStyle =
                        fill(
                            color = "#58D9F9",
                            shadowColor = "rgba(0,138,255,0.45)",
                            shadowBlur = 10.0,
                            shadowOffsetX = 2.0,
                            shadowOffsetY = 2.0,
                        )
                    progress = gaugeProgress(show = true, width = 18.0, roundCap = true)
                    pointer =
                        gaugePointer(
                            icon = WIDE_NEEDLE_PATH,
                            width = 16.0,
                            length = "75%",
                            offsetCenter = "0%" to "5%",
                        )
                    axisLine = gaugeAxisLine(width = 18.0)
                    axisTick = gaugeAxisTick(lineStyle = stroke(color = "#999", width = 2.0))
                    splitLine = gaugeSplitLine(lineStyle = stroke(color = "#999", width = 3.0))
                    axisLabel = gaugeAxisLabel(fontSize = 20.0, color = "#999")
                    title = labelOf(show = false)
                    detail =
                        labelOf(
                            width = str("60%"),
                            height = num(40),
                            lineHeight = 40.0,
                            borderRadius = 8,
                            backgroundColor = "#fff",
                            borderColor = "#999",
                            borderWidth = 2.0,
                            valueAnimation = true,
                            formatter = "{value|{value}}{unit|km/h}",
                            rich = speedRich,
                        )
                    values(100.0)
                }
            }
        }
