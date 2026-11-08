package demo.json.examples.gauge

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
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
import demo.json.namedValues

private val gaugeGradeJsonCategory = JsonCategory("json/examples/gauge/GaugeGradeJson.kt")

// ---------------------------------------------------------------------------------------------
// Shared builders
// ---------------------------------------------------------------------------------------------

val gaugeGradeJsonExample: JsonExample =
    gaugeGradeJsonCategory.example("gauge-grade") {
            series {
                gauge {
                    startAngle = 180.0
                    endAngle = 0.0
                    center = listOf(str("50%"), str("75%"))
                    radius = NumberOrStringOrNumbers.of("90%")
                    min = 0.0
                    max = 1.0
                    splitNumber = 8
                    axisLine = gaugeAxisLine(width = 6.0)
                    pointer =
                        gaugePointer(
                            icon = "path://M12.8,0.7l12,40.1H0.7L12.8,0.7z",
                            width = 20.0,
                            length = "12%",
                            offsetCenter = "0" to "-60%",
                            itemStyle = pointerStyle(color = "auto"),
                        )
                    axisTick = gaugeAxisTick(length = 12.0, lineStyle = stroke(color = "auto", width = 2.0))
                    splitLine = gaugeSplitLine(lineStyle = stroke(color = "auto", width = 5.0))
                    axisLabel =
                        gaugeAxisLabel(fontSize = 20.0, color = "#464646")
                    title = labelOf(fontSize = 20.0)
                    detail =
                        labelOf(fontSize = 30.0, valueAnimation = true, color = "inherit")
                    data = namedValues("Grade Rating" to 0.7)
                }
            }
        }
