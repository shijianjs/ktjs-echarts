package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.JsonPrimitive
import demo.json.examples.shared.decalOf
import demo.json.examples.shared.jsonIntArrays
import demo.json.examples.shared.jsonInts
import demo.json.*

private val docExampleAriaDecalNewspaperJsonCategory = JsonCategory("json/examples/doc/DocExampleAriaDecalNewspaperJson.kt")

val docExampleAriaDecalNewspaperJsonExample: JsonExample =
    docExampleAriaDecalNewspaperJsonCategory.example("doc-example/aria-decal-newspaper") {
            series {
                bar {
                    name = "Apples"
                    values(108, 26, 39, 24)
                    itemStyle {
                        borderColor = "black".asColor()
                        borderWidth = 2.0
                    }
                }
                bar {
                    name = "Oranges"
                    values(23, 40, 60, 70)
                    itemStyle {
                        borderColor = "black".asColor()
                        borderWidth = 2.0
                    }
                }
                bar {
                    name = "Bananas"
                    values(129, 40, 40, 50)
                    itemStyle {
                        borderColor = "black".asColor()
                        borderWidth = 2.0
                    }
                }
                bar {
                    name = "Pears"
                    values(40, 60, 50, 89)
                    itemStyle {
                        borderColor = "black".asColor()
                        borderWidth = 2.0
                    }
                }
            }
            xAxis {
                categories("Q1", "Q2", "Q3", "Q4")
                axisLine { lineStyle = LineStyleOption().apply { color = "black".asColor() } }
                axisTick { lineStyle = LineStyleOption().apply { color = "black".asColor() } }
                axisLabel { color = "black".asColor() }
            }
            yAxis {
                axisLine { lineStyle = LineStyleOption().apply { color = "black".asColor() } }
                axisTick { lineStyle = LineStyleOption().apply { color = "black".asColor() } }
                axisLabel { color = "black".asColor() }
                splitLine {
                    lineStyle =
                        LineStyleOption().apply {
                            color = "black".asColor()
                            type = "dashed"
                        }
                }
            }
            aria {
                decal =
                    AriaDecalOption().apply {
                        show = true
                        decals =
                            listOf(
                                decalOf(
                                    dashArrayX = JsonPrimitive(0),
                                    dashArrayY = JsonPrimitive(0),
                                    color = "black",
                                ),
                                decalOf(
                                    dashArrayX = JsonPrimitive(0),
                                    dashArrayY = JsonPrimitive(0),
                                    color = "black",
                                ),
                                decalOf(
                                    dashArrayX = jsonInts(1, 0),
                                    dashArrayY = jsonInts(2, 5),
                                    symbolSize = 1.0,
                                    rotation = kotlin.math.PI / 6,
                                    color = "black",
                                ),
                                decalOf(
                                    symbol = "circle",
                                    dashArrayX = jsonIntArrays(listOf(8, 8), listOf(0, 8, 8, 0)),
                                    dashArrayY = jsonInts(6, 0),
                                    symbolSize = 0.8,
                                    color = "black",
                                ),
                            )
                    }
            }
            color = colors("white", "black", "white", "white")
        }
