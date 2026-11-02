package demo.json.examples.gauge

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.gaugeAnchor
import demo.json.examples.shared.gaugeAxisLabel
import demo.json.examples.shared.gaugeAxisLine
import demo.json.examples.shared.gaugeAxisTick
import demo.json.examples.shared.gaugeProgress
import demo.json.examples.shared.gaugeSplitLine
import demo.json.examples.shared.labelOf
import demo.json.examples.shared.pointerStyle
import demo.json.examples.shared.stroke

private val gaugeProgressJsonCategory = JsonCategory("json/examples/gauge/GaugeProgressJson.kt")

// ---------------------------------------------------------------------------------------------
// Shared builders
// ---------------------------------------------------------------------------------------------

val gaugeProgressJsonExample: JsonExample =
    gaugeProgressJsonCategory.example("gauge-progress") {
            series {
                gauge {
                    progress = gaugeProgress(show = true, width = 18.0)
                    axisLine = gaugeAxisLine(width = 18.0)
                    axisTick = gaugeAxisTick(show = false)
                    splitLine = gaugeSplitLine(lineStyle = stroke(color = "#999", width = 2.0))
                    axisLabel = gaugeAxisLabel(fontSize = 20.0, color = "#999")
                    anchor =
                        gaugeAnchor(
                            show = true,
                            showAbove = true,
                            size = 25.0,
                            itemStyle = pointerStyle(borderWidth = 10.0),
                        )
                    title = labelOf(show = false)
                    detail = labelOf(fontSize = 80.0, valueAnimation = true)
                    values(70.0)
                }
            }
        }
