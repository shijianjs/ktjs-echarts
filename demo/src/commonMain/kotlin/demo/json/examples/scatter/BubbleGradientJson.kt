package demo.json.examples.scatter

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val bubbleGradientJsonCategory = JsonCategory("json/examples/scatter/BubbleGradientJson.kt")

val bubbleGradientJsonExample: JsonExample =
    bubbleGradientJsonCategory.example("bubble-gradient") {
            backgroundColor =
                EChartsColor.RadialGradient(
                    x = 0.3,
                    y = 0.3,
                    r = 0.8,
                    colorStops =
                        listOf(
                            GradientColorStop(0.0, "#f7f8fa"),
                            GradientColorStop(1.0, "#cdd0d5"),
                        ),
                )
            title {
                text = "Life Expectancy and GDP by Country"
                left = str("5%")
                top = str("3%")
            }
            legend {
                right = str("10%")
                top = str("3%")
                entries("1990", "2015")
            }
            grid {
                left = str("8%")
                top = str("10%")
            }
            xAxis { splitLine = dashedSplitLine() }
            yAxis {
                splitLine = dashedSplitLine()
                scale = true
            }
            series {
                scatter {
                    name = "1990"
                    data = expectancyItems(EXPECTANCY_1990)
                    emphasis = focusSeries()
                    itemStyle =
                        ItemStyleOption().apply {
                            shadowBlur = 10.0
                            shadowColor = "rgba(120, 36, 50, 0.5)".asColor()
                            shadowOffsetY = 5.0
                            color =
                                EChartsColor.RadialGradient(
                                    x = 0.4,
                                    y = 0.3,
                                    r = 1.0,
                                    colorStops =
                                        listOf(
                                            GradientColorStop(0.0, "rgb(251, 118, 123)"),
                                            GradientColorStop(1.0, "rgb(204, 46, 72)"),
                                        ),
                                )
                        }
                }
                scatter {
                    name = "2015"
                    data = expectancyItems(EXPECTANCY_2015)
                    emphasis = focusSeries()
                    itemStyle =
                        ItemStyleOption().apply {
                            shadowBlur = 10.0
                            shadowColor = "rgba(25, 100, 150, 0.5)".asColor()
                            shadowOffsetY = 5.0
                            color =
                                EChartsColor.RadialGradient(
                                    x = 0.4,
                                    y = 0.3,
                                    r = 1.0,
                                    colorStops =
                                        listOf(
                                            GradientColorStop(0.0, "rgb(129, 227, 238)"),
                                            GradientColorStop(1.0, "rgb(25, 183, 207)"),
                                        ),
                                )
                        }
                }
            }
        }
