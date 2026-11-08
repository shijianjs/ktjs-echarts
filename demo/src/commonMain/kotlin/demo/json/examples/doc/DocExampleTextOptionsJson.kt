package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.examples.shared.hiddenTextAxes
import demo.json.*

private val docExampleTextOptionsJsonCategory = JsonCategory("json/examples/doc/DocExampleTextOptionsJson.kt")

val docExampleTextOptionsJsonExample: JsonExample =
    docExampleTextOptionsJsonCategory.example("doc-example/text-options") {
            series {
                scatter {
                    symbolSize = NumberOrStringOrNumbers.of(1)
                    data =
                        listOf(
                            dataItem {
                                value = point(0, 0)
                                label {
                                    show = true
                                    formatter =
                                        "Plain text\n" +
                                            "{textBorder|textBorderColor + textBorderWidth}\n" +
                                            "{textShadow|textShadowColor + textShadowBlur + textShadowOffsetX + textShadowOffsetY}\n" +
                                            "{bg|backgroundColor + borderRadius + padding}\n" +
                                            "{border|borderColor + borderWidth + borderRadius + padding}\n" +
                                            "{shadow|shadowColor + shadowBlur + shadowOffsetX + shadowOffsetY}"
                                    backgroundColor = "#eee".asColor()
                                    borderColor = "#333".asColor()
                                    borderWidth = 2.0
                                    borderRadius = NumberOrStringOrNumbers.of(5)
                                    padding = NumberOrStringOrNumbers.of(10)
                                    color = "#000".asColor()
                                    fontSize = 14.0
                                    shadowBlur = 3.0
                                    shadowColor = "#888".asColor()
                                    shadowOffsetX = 0.0
                                    shadowOffsetY = 3.0
                                    lineHeight = 30.0
                                    rich =
                                        mapOf(
                                            "textBorder" to
                                                textStyleOf {
                                                    fontSize = 20.0
                                                    textBorderColor = "#000".asColor()
                                                    textBorderWidth = 3.0
                                                    color = "#fff".asColor()
                                                },
                                            "textShadow" to
                                                textStyleOf {
                                                    fontSize = 16.0
                                                    textShadowBlur = 5.0
                                                    textShadowColor = "#000".asColor()
                                                    textShadowOffsetX = 3.0
                                                    textShadowOffsetY = 3.0
                                                    color = "#fff".asColor()
                                                },
                                            "bg" to
                                                textStyleOf {
                                                    backgroundColor = "#339911".asColor()
                                                    color = "#fff".asColor()
                                                    borderRadius = NumberOrStringOrNumbers.of(15)
                                                    padding = NumberOrStringOrNumbers.of(5)
                                                },
                                            "border" to
                                                textStyleOf {
                                                    color = "#000".asColor()
                                                    borderColor = "#449911".asColor()
                                                    borderWidth = 1.0
                                                    borderRadius = NumberOrStringOrNumbers.of(3)
                                                    padding = NumberOrStringOrNumbers.of(5)
                                                },
                                            "shadow" to
                                                textStyleOf {
                                                    backgroundColor = "#992233".asColor()
                                                    padding = NumberOrStringOrNumbers.of(5)
                                                    color = "#fff".asColor()
                                                    shadowBlur = 5.0
                                                    shadowColor = "#336699".asColor()
                                                    shadowOffsetX = 6.0
                                                    shadowOffsetY = 6.0
                                                },
                                        )
                                }
                            },
                        )
                }
            }
            hiddenTextAxes()
        }
