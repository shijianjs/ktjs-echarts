package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.*

private val docExampleTextBlockFragmentJsonCategory = JsonCategory("json/examples/doc/DocExampleTextBlockFragmentJson.kt")

val docExampleTextBlockFragmentJsonExample: JsonExample =
    docExampleTextBlockFragmentJsonCategory.example("doc-example/text-block-fragment") {
            xAxis {
                hidden()
                min = num(-1)
                max = num(1)
            }
            yAxis {
                hidden()
                min = num(-1)
                max = num(1)
            }
            series {
                scatter {
                    symbolSize = NumberOrStringOrNumbers.of(1)
                    data = listOf(point(0, 0))
                    label {
                        show = true
                        formatter =
                            "The whole box is a {term|Text Block}, with\n" +
                                "red border and grey background.\n" +
                                "{fragment1|A Text Fragment} {fragment2|Another Text Fragment}\n" +
                                "Text fragments can be customized."
                        backgroundColor = "#eee".asColor()
                        borderColor = "rgb(199,86,83)".asColor()
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
                                "term" to
                                    textStyleOf {
                                        fontSize = 18.0
                                        color = "rgb(199,86,83)".asColor()
                                    },
                                "fragment1" to
                                    textStyleOf {
                                        backgroundColor = "#000".asColor()
                                        color = "yellow".asColor()
                                        padding = NumberOrStringOrNumbers.of(5)
                                    },
                                "fragment2" to
                                    textStyleOf {
                                        backgroundColor = "#339911".asColor()
                                        color = "#fff".asColor()
                                        borderRadius = NumberOrStringOrNumbers.of(15)
                                        padding = NumberOrStringOrNumbers.of(5)
                                    },
                            )
                    }
                }
            }
        }
