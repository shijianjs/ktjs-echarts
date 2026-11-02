package demo.json.examples.themeriver

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import demo.json.examples.shared.riverBand
import demo.json.*

private val themeRiverBasicJsonCategory = JsonCategory("json/examples/themeriver/ThemeRiverBasicJson.kt")

val themeRiverBasicJsonExample: JsonExample =
    themeRiverBasicJsonCategory.example("themeRiver-basic") {
            tooltip {
                trigger = TooltipTrigger.Axis
                axisPointer =
                    AxisPointerOption().apply {
                        type = "line"
                        lineStyle =
                            LineStyleOption().apply {
                                color = "rgba(0,0,0,0.2)".asColor()
                                width = 1.0
                                type = "solid"
                            }
                    }
            }
            legend {
                entries("DQ", "TY", "SS", "QG", "SY", "DD")
                top = num(15)
            }
            singleAxis {
                top = num(50)
                bottom = num(50)
                type = AxisType.Time
                axisPointer =
                    AxisPointerOption().apply {
                        animation = true
                        label = AxisPointerLabelOption().apply { show = true }
                    }
                splitLine =
                    SplitLineOption().apply {
                        show = true
                        lineStyle =
                            LineStyleOption().apply {
                                type = "dashed"
                                opacity = 0.2
                            }
                    }
            }
            series {
                themeRiver {
                    singleAxisIndex = 0
                    emphasis {
                        itemStyle {
                            shadowBlur = 20.0
                            shadowColor = "rgba(0, 0, 0, 0.8)".asColor()
                        }
                    }
                    data =
                        riverBand("DQ", listOf(10, 15, 35, 38, 22, 16, 7, 2, 17, 33, 40, 32)) +
                            riverBand("TY", listOf(35, 36, 37, 22, 24, 26, 34, 21, 18, 45, 32, 35)) +
                            riverBand("SS", listOf(21, 25, 27, 23, 24, 21, 35, 39, 40, 36, 33, 43)) +
                            riverBand("QG", listOf(10, 15, 35, 38, 22, 16, 7, 2, 17, 33, 40, 32)) +
                            riverBand("SY", listOf(10, 15, 35, 38, 22, 16, 7, 2, 17, 33, 40, 32)) +
                            riverBand("DD", listOf(10, 15, 35, 38, 22, 16, 7, 2, 17, 33, 4, 32))
                }
            }
        }
