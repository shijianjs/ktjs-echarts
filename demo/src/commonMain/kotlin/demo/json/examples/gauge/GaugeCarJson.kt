package demo.json.examples.gauge

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.carFuelDial
import demo.json.examples.shared.carGearDial
import demo.json.examples.shared.carPositionDial
import demo.json.examples.shared.carRevDial
import demo.json.examples.shared.carSpeedDial
import demo.json.examples.shared.carSpeedReadout
import demo.json.examples.shared.carTripHeader
import demo.json.examples.shared.gaugeTooltip

private val gaugeCarJsonCategory = JsonCategory("json/examples/gauge/GaugeCarJson.kt")

// ---------------------------------------------------------------------------------------------
// Shared builders
// ---------------------------------------------------------------------------------------------

val gaugeCarJsonExample: JsonExample =
    gaugeCarJsonCategory.example("gauge-car") {
            backgroundColor = "#000".asColor()
            gaugeTooltip()
            toolbox {
                feature =
                    ToolboxFeatureOption().apply {
                        restore = ToolboxFeature()
                        saveAsImage = ToolboxSaveAsImageFeature()
                    }
            }
            series {
                carSpeedDial()
                carPositionDial()
                carRevDial()
                carSpeedReadout()
                carTripHeader()
                carFuelDial()
                carGearDial()
            }
        }
