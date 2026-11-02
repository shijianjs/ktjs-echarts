package demo.json.examples.radar

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.indicator
import demo.json.examples.shared.radarItem

private val radarJsonCategory = JsonCategory("json/examples/radar/RadarJson.kt")

// ---------------------------------------------------------------------------------------------
// Shared builders
// ---------------------------------------------------------------------------------------------

val radarJsonExample: JsonExample =
    // ------------------------------------------------------------------------------- radars
        radarJsonCategory.example("radar") {
            title { text = "Basic Radar Chart" }
            legend { entries("Allocated Budget", "Actual Spending") }
            radar {
                indicator =
                    listOf(
                        indicator("Sales", 6500.0),
                        indicator("Administration", 16000.0),
                        indicator("Information Technology", 30000.0),
                        indicator("Customer Support", 38000.0),
                        indicator("Development", 52000.0),
                        indicator("Marketing", 25000.0),
                    )
            }
            series {
                radar {
                    name = "Budget vs spending"
                    data =
                        listOf(
                            radarItem(
                                "Allocated Budget",
                                4200.0,
                                3000.0,
                                20000.0,
                                35000.0,
                                50000.0,
                                18000.0,
                            ),
                            radarItem(
                                "Actual Spending",
                                5000.0,
                                14000.0,
                                28000.0,
                                26000.0,
                                42000.0,
                                21000.0,
                            ),
                        )
                }
            }
        }
