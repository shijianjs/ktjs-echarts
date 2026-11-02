package demo.json.examples.gauge

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.gaugeProgress
import demo.json.examples.shared.gaugeTooltip
import demo.json.examples.shared.labelOf
import demo.json.namedValues

private val gaugeSimpleJsonCategory = JsonCategory("json/examples/gauge/GaugeSimpleJson.kt")

// ---------------------------------------------------------------------------------------------
// Shared builders
// ---------------------------------------------------------------------------------------------

val gaugeSimpleJsonExample: JsonExample =
    gaugeSimpleJsonCategory.example("gauge-simple") {
            gaugeTooltip()
            series {
                gauge {
                    name = "Pressure"
                    progress = gaugeProgress(show = true)
                    detail = labelOf(formatter = "{value}", valueAnimation = true)
                    data = namedValues("SCORE" to 50)
                }
            }
        }
