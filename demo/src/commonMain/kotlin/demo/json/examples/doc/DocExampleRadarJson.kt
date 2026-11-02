package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import demo.json.examples.shared.indicator
import demo.json.*

private val docExampleRadarJsonCategory = JsonCategory("json/examples/doc/DocExampleRadarJson.kt")

val docExampleRadarJsonExample: JsonExample =
    docExampleRadarJsonCategory.example("doc-example/radar") {
            radar {
                indicator =
                    listOf(
                        RadarIndicator().apply { name = "指标一" },
                        RadarIndicator().apply { name = "指标二" },
                        RadarIndicator().apply { name = "指标三" },
                        RadarIndicator().apply { name = "指标四" },
                        RadarIndicator().apply { name = "指标五" },
                    )
                center = listOf(str("50%"), str("50%"))
                radius = NumberOrStringOrNumbers.of("80%")
                startAngle = 90.0
                splitNumber = 4
                shape = "circle"
                axisName =
                    LabelOption().apply {
                        formatter = "【{value}】"
                        color = "#72ACD1".asColor()
                    }
                axisTick =
                    AxisTickOption().apply {
                        show = true
                        lineStyle =
                            LineStyleOption().apply {
                                color = "rgba(255, 255, 255, 0.8)".asColor()
                            }
                    }
                axisLabel =
                    AxisLabelOption().apply {
                        show = true
                        color = "white".asColor()
                    }
                axisLine =
                    AxisLineOption().apply {
                        lineStyle =
                            LineStyleOption().apply {
                                color = "rgba(255, 255, 255, 0.4)".asColor()
                            }
                    }
                splitLine =
                    SplitLineOption().apply {
                        lineStyle =
                            LineStyleOption().apply {
                                color = "rgba(255, 255, 255, 0.4)".asColor()
                            }
                    }
            }
        }
