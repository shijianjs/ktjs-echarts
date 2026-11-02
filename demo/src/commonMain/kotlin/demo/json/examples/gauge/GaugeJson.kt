package demo.json.examples.gauge

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.gaugeTooltip
import demo.json.examples.shared.labelOf
import demo.json.namedValues

private val gaugeJsonCategory = JsonCategory("json/examples/gauge/GaugeJson.kt")

// ---------------------------------------------------------------------------------------------
// Shared builders
// ---------------------------------------------------------------------------------------------

val gaugeJsonExample: JsonExample =
    // ------------------------------------------------------------------------------ gauges
        gaugeJsonCategory.example("gauge") {
            gaugeTooltip()
            series {
                gauge {
                    name = "Pressure"
                    detail = labelOf(formatter = "{value}")
                    data = namedValues("SCORE" to 50)
                }
            }
        }
