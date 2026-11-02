package demo.json.examples.pie

import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.namedValues
import io.github.shijianjs.kmpechartsjson.*
import demo.json.*

private val pieNestJsonCategory = JsonCategory("json/examples/pie/PieNestJson.kt")

val pieNestJsonExample: JsonExample =
    pieNestJsonCategory.example("pie-nest") {
            tooltip {
                trigger = TooltipTrigger.Item
                formatter = "{a} <br/>{b} : {c} ({d}%)"
            }
            legend {
                entries(
                    "Direct",
                    "Marketing",
                    "Search Engine",
                    "Email",
                    "Union Ads",
                    "Video Ads",
                    "Baidu",
                    "Google",
                    "Bing",
                    "Others",
                )
            }
            series {
                pie {
                    name = "Access From"
                    selectedMode = io.github.shijianjs.kmpechartsjson.BooleanOrString.of("single")
                    radius = NumberOrStringOrNumbers.of(num(0), str("30%"))
                    data =
                        namedValues(
                            "Search Engine" to 1548,
                            "Direct" to 775,
                            "Marketing" to 679,
                        )
                    label {
                        position = "inner"
                        fontSize = 14.0
                    }
                    labelLine { show = false }
                }
                pie {
                    name = "Access From"
                    radius = NumberOrStringOrNumbers.of(str("45%"), str("60%"))
                    data =
                        namedValues(
                            "Baidu" to 1048,
                            "Direct" to 335,
                            "Email" to 310,
                            "Google" to 251,
                            "Union Ads" to 234,
                            "Bing" to 147,
                            "Video Ads" to 135,
                            "Others" to 102,
                        )
                    labelLine { length = 30.0 }
                    label {
                        formatter = "{a|{a}}{abg|}\n{hr|}\n  {b|{b}：}{c}  {per|{d}%}  "
                        backgroundColor = "#F6F8FC".asColor()
                        borderColor = "#8C8D8E".asColor()
                        borderWidth = 1.0
                        borderRadius = NumberOrStringOrNumbers.of(4)
                        rich =
                            mapOf(
                                "a" to
                                    textStyleOf {
                                        color = "#6E7079".asColor()
                                        lineHeight = 22.0
                                        align = "center"
                                    },
                                "hr" to
                                    textStyleOf {
                                        borderColor = "#8C8D8E".asColor()
                                        width = str("100%")
                                        borderWidth = 1.0
                                        height = num(0)
                                    },
                                "b" to
                                    textStyleOf {
                                        color = "#4C5058".asColor()
                                        fontSize = 14.0
                                        fontWeight = str("bold")
                                        lineHeight = 33.0
                                    },
                                "per" to
                                    textStyleOf {
                                        color = "#fff".asColor()
                                        backgroundColor = "#4C5058".asColor()
                                        padding = NumberOrStringOrNumbers.of(num(3), num(4))
                                        borderRadius = NumberOrStringOrNumbers.of(4)
                                    },
                            )
                    }
                }
            }
        }
