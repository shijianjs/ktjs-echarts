package demo.json.examples.radar

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.examples.shared.indicator
import demo.json.examples.shared.indicators
import demo.json.examples.shared.radarItemRadarScatterExtra
import demo.json.*

private val radarMultipleJsonCategory = JsonCategory("json/examples/radar/RadarMultipleJson.kt")

val radarMultipleJsonExample: JsonExample =
    radarMultipleJsonCategory.example("radar-multiple") {
            title { text = "Multiple Radar" }
            tooltip { trigger = TooltipTrigger.Axis }
            legend {
                left = str("center")
                entries("A Software", "A Phone", "Another Phone", "Precipitation", "Evaporation")
            }
            radar {
                indicator =
                    indicators(
                        "Brand" to 100,
                        "Content" to 100,
                        "Usability" to 100,
                        "Function" to 100,
                    )
                center = strings("25%", "40%")
                radius = NumberOrStringOrNumbers.of(80)
            }
            radar {
                indicator =
                    indicators(
                        "Look" to 100,
                        "Photo" to 100,
                        "System" to 100,
                        "Performance" to 100,
                        "Screen" to 100,
                    )
                radius = NumberOrStringOrNumbers.of(80)
                center = strings("50%", "60%")
            }
            radar {
                indicator = indicators(*(1..12).map { "${it}月" to 100 }.toTypedArray())
                center = strings("75%", "40%")
                radius = NumberOrStringOrNumbers.of(80)
            }
            series {
                radar {
                    tooltip = TooltipOption().apply { trigger = TooltipTrigger.Item }
                    areaStyle = AreaStyleOption()
                    items(radarItemRadarScatterExtra("A Software", 60, 73, 85, 40))
                }
                radar {
                    radarIndex = 1
                    areaStyle = AreaStyleOption()
                    items(
                        radarItemRadarScatterExtra("A Phone", 85, 90, 90, 95, 95),
                        radarItemRadarScatterExtra("Another Phone", 95, 80, 95, 90, 93),
                    )
                }
                radar {
                    radarIndex = 2
                    areaStyle = AreaStyleOption()
                    items(
                        radarItemRadarScatterExtra("Precipitation", 2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 75.6, 82.2, 48.7, 18.8, 6.0, 2.3),
                        radarItemRadarScatterExtra("Evaporation", 2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 35.6, 62.2, 32.6, 20.0, 6.4, 3.3),
                    )
                }
            }
        }
