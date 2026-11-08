package demo.json.examples.radar

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import kotlinx.serialization.json.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.BEIJING_AQI
import demo.json.examples.shared.GUANGZHOU_AQI
import demo.json.examples.shared.SHANGHAI_AQI
import demo.json.examples.shared.aqiRows
import demo.json.examples.shared.aqiSeries
import demo.json.examples.shared.indicator
import demo.json.examples.shared.labelOf
import demo.json.examples.shared.stroke

private val radarAqiJsonCategory = JsonCategory("json/examples/radar/RadarAqiJson.kt")

// ---------------------------------------------------------------------------------------------
// Shared builders
// ---------------------------------------------------------------------------------------------

val radarAqiJsonExample: JsonExample =
    radarAqiJsonCategory.example("radar-aqi") {
            backgroundColor = "#161627".asColor()
            title {
                text = "AQI - Radar"
                left = str("center")
                textStyle { color = "#eee".asColor() }
            }
            legend {
                bottom = num(5)
                entries("Beijing", "Shanghai", "Guangzhou")
                itemGap = 20.0
                textStyle {
                    color = "#fff".asColor()
                    fontSize = 14.0
                }
                selectedMode = BooleanOrString.of("single")
            }
            radar {
                indicator =
                    listOf(
                        indicator("AQI", 300.0),
                        indicator("PM2.5", 250.0),
                        indicator("PM10", 300.0),
                        indicator("CO", 5.0),
                        indicator("NO2", 200.0),
                        indicator("SO2", 100.0),
                    )
                shape = "circle"
                splitNumber = 5
                axisName = labelOf(color = "rgb(238, 197, 102)")
                splitLine = SplitLineOption().apply { lineStyle = stroke(color = "rgba(238, 197, 102, 1)") }
                splitArea = SplitAreaOption().apply { show = false }
                axisLine = AxisLineOption().apply { lineStyle = stroke(color = "rgba(238, 197, 102, 0.5)") }
            }
            series {
                aqiSeries("Beijing", "#F9713C", 0.1, aqiRows(BEIJING_AQI))
                aqiSeries("Shanghai", "#B3E4A1", 0.05, aqiRows(SHANGHAI_AQI))
                aqiSeries("Guangzhou", "rgb(238, 197, 102)", 0.05, aqiRows(GUANGZHOU_AQI))
            }
        }
