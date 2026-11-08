package demo.json.examples.radar

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import kotlinx.serialization.json.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.indicator
import demo.json.examples.shared.labelOf
import demo.json.examples.shared.radarItem
import demo.json.examples.shared.stroke

private val radarCustomJsonCategory = JsonCategory("json/examples/radar/RadarCustomJson.kt")

// ---------------------------------------------------------------------------------------------
// Shared builders
// ---------------------------------------------------------------------------------------------

val radarCustomJsonExample: JsonExample =
    radarCustomJsonCategory.example("radar-custom") {
            color = colors("#67F9D8", "#FFE434", "#56A3F1", "#FF917C")
            title { text = "Customized Radar Chart" }
            legend { }
            radar {
                indicator =
                    listOf(
                        indicator("Indicator1"),
                        indicator("Indicator2"),
                        indicator("Indicator3"),
                        indicator("Indicator4"),
                        indicator("Indicator5"),
                    )
                center = listOf(str("25%"), str("50%"))
                radius = NumberOrStringOrNumbers.of(120)
                startAngle = 90.0
                splitNumber = 4
                shape = "circle"
                axisName =
                    labelOf(
                        formatter = "【{value}】",
                        color = "#428BD4",
                    )
                splitArea =
                    SplitAreaOption().apply {
                        areaStyle =
                            AreaStyleOption().apply {
                                color = "#77EADF".asColor()
                                shadowColor = "rgba(0, 0, 0, 0.2)".asColor()
                                shadowBlur = 10.0
                            }
                    }
                axisLine = AxisLineOption().apply { lineStyle = stroke(color = "rgba(211, 253, 250, 0.8)") }
                splitLine = SplitLineOption().apply { lineStyle = stroke(color = "rgba(211, 253, 250, 0.8)") }
            }
            radar {
                indicator =
                    listOf(
                        indicator("Indicator1", 150.0),
                        indicator("Indicator2", 150.0),
                        indicator("Indicator3", 150.0),
                        indicator("Indicator4", 120.0),
                        indicator("Indicator5", 108.0),
                        indicator("Indicator6", 72.0),
                    )
                center = listOf(str("75%"), str("50%"))
                radius = NumberOrStringOrNumbers.of(120)
                axisName =
                    labelOf(
                        color = "#fff",
                        backgroundColor = "#666",
                        borderRadius = 3,
                        padding = listOf<Number>(3, 5),
                    )
            }
            series {
                radar {
                    emphasis { lineStyle { width = 4.0 } }
                    data =
                        listOf(
                            radarItem("Data A", 100.0, 8.0, 0.4, -80.0, 2000.0),
                            radarItem("Data B", 60.0, 5.0, 0.3, -100.0, 1500.0),
                        )
                }
            }
        }
