package demo.json.examples.gauge

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.gaugeAxisLabel
import demo.json.examples.shared.gaugeAxisLine
import demo.json.examples.shared.gaugeAxisTick
import demo.json.examples.shared.gaugePointer
import demo.json.examples.shared.gaugeSplitLine
import demo.json.examples.shared.labelOf
import demo.json.examples.shared.pointerStyle
import demo.json.examples.shared.stroke

private val gaugeStageJsonCategory = JsonCategory("json/examples/gauge/GaugeStageJson.kt")

// ---------------------------------------------------------------------------------------------
// Shared builders
// ---------------------------------------------------------------------------------------------

val gaugeStageJsonExample: JsonExample =
    gaugeStageJsonCategory.example("gauge-stage") {
            series {
                gauge {
                    axisLine = gaugeAxisLine(width = 30.0)
                    pointer = gaugePointer(itemStyle = pointerStyle(color = "auto"))
                    axisTick = gaugeAxisTick(length = 8.0, lineStyle = stroke(color = "#fff", width = 2.0))
                    splitLine = gaugeSplitLine(lineStyle = stroke(color = "#fff", width = 4.0))
                    axisLabel = gaugeAxisLabel(fontSize = 20.0, color = "inherit")
                    detail =
                        labelOf(
                            formatter = "{value} km/h",
                            color = "inherit",
                            valueAnimation = true,
                        )
                    values(70.0)
                }
            }
        }
