package demo.json.examples.radar

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import kotlinx.serialization.json.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.indicator
import demo.json.examples.shared.radarItem

private val radar2JsonCategory = JsonCategory("json/examples/radar/Radar2Json.kt")

// ---------------------------------------------------------------------------------------------
// Shared builders
// ---------------------------------------------------------------------------------------------

val radar2JsonExample: JsonExample =
    // `radar-multiple` is ported in `RadarScatterExtraJson.kt`, where its three radars are
        // selected through `series[].radarIndex`.
        radar2JsonCategory.example("radar2") {
            title {
                text = "Proportion of Browsers"
                subtext = "Fake Data"
                top = num(10)
                left = num(10)
            }
            tooltip { trigger = TooltipTrigger.Item }
            legend {
                type = LegendType.Scroll
                bottom = num(10)
                entries(*Array(28) { index -> "${index + 2001}" })
            }
            visualMap {
                top = str("middle")
                right = num(10)
                inRange { color = colors("red", "yellow") }
                calculable = true
            }
            radar {
                indicator =
                    listOf(
                        indicator("IE8-", 400.0),
                        indicator("IE9+", 400.0),
                        indicator("Safari", 400.0),
                        indicator("Firefox", 400.0),
                        indicator("Chrome", 400.0),
                    )
            }
            series {
                for (index in 0 until 28) {
                    val year = index + 1
                    radar {
                        symbol = "none"
                        lineStyle { width = 1.0 }
                        emphasis { areaStyle { color = "rgba(0, 250, 0, 0.3)".asColor() } }
                        data =
                            listOf(
                                radarItem(
                                    "${year + 2000}",
                                    (40 - year) * 10.0,
                                    (38 - year) * 4.0 + 60.0,
                                    year * 5.0 + 10.0,
                                    year * 9.0,
                                    year * year / 2.0,
                                ),
                            )
                    }
                }
            }
        }
