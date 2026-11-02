package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.points
import io.github.shijianjs.kmpechartsjson.*
import demo.json.examples.shared.hiddenTextAxes
import demo.json.*

private val docExampleTextFregmentAlignJsonCategory = JsonCategory("json/examples/doc/DocExampleTextFregmentAlignJson.kt")

val docExampleTextFregmentAlignJsonExample: JsonExample =
    docExampleTextFregmentAlignJsonCategory.example("doc-example/text-fregment-align") {
            series {
                scatter {
                    data = points(0 to 0)
                    symbolSize = NumberOrStringOrNumbers.of(1)
                    label {
                        show = true
                        formatter =
                            "{tc|height is specified as 30}{box|}\n" +
                                "{lh|Biggest lineHeight} {vt|verticalAlign \"top\"} {vb|verticalAlign \"bottom\"} " +
                                "{vm|verticalAlign \"middle\"}\n" +
                                "{l1|align \"left\"}{l2|align \"left\"}{c1|align \"center\"}{c2|align \"center\"}" +
                                "{r2|align \"right\"}{r1|align \"right\"}\n" +
                                "{tc|height is specified as 30}{box|}"
                        backgroundColor = "#ddd".asColor()
                        borderColor = "#555".asColor()
                        borderWidth = 1.0
                        borderRadius = NumberOrStringOrNumbers.of(5)
                        padding = NumberOrStringOrNumbers.of(num(0), num(20), num(0), num(20))
                        color = "#000".asColor()
                        fontSize = 14.0
                        rich =
                            mapOf(
                                "box" to
                                    textStyleOf {
                                        backgroundColor = "#000".asColor()
                                        height = num(30)
                                        padding = NumberOrStringOrNumbers.of(num(0), num(20), num(0), num(20))
                                        align = "right"
                                        width = str("100%")
                                        color = "#eee".asColor()
                                    },
                                "tc" to
                                    textStyleOf {
                                        align = "center"
                                        color = "#eee".asColor()
                                    },
                                "lh" to
                                    textStyleOf {
                                        fontSize = 30.0
                                        color = "rgb(199,86,83)".asColor()
                                        borderWidth = 1.0
                                        borderColor = "rgb(199,86,83)".asColor()
                                    },
                                "vt" to
                                    textStyleOf {
                                        verticalAlign = "top"
                                        backgroundColor = "#991122".asColor()
                                        color = "#fff".asColor()
                                        padding = NumberOrStringOrNumbers.of(num(1), num(2), num(1), num(2))
                                    },
                                "vb" to
                                    textStyleOf {
                                        verticalAlign = "bottom"
                                        backgroundColor = "#991122".asColor()
                                        color = "#fff".asColor()
                                        padding = NumberOrStringOrNumbers.of(num(1), num(2), num(1), num(2))
                                    },
                                "vm" to
                                    textStyleOf {
                                        verticalAlign = "middle"
                                        backgroundColor = "#991122".asColor()
                                        color = "#fff".asColor()
                                        padding = NumberOrStringOrNumbers.of(num(1), num(2), num(1), num(2))
                                    },
                                "l1" to
                                    textStyleOf {
                                        backgroundColor = "#7066FF".asColor()
                                        color = "#fff".asColor()
                                        padding = NumberOrStringOrNumbers.of(5)
                                    },
                                "l2" to
                                    textStyleOf {
                                        backgroundColor = "#339911".asColor()
                                        color = "#fff".asColor()
                                        borderRadius = NumberOrStringOrNumbers.of(15)
                                        padding = NumberOrStringOrNumbers.of(5)
                                    },
                                "r1" to
                                    textStyleOf {
                                        backgroundColor = "#7066FF".asColor()
                                        align = "right"
                                        color = "#fff".asColor()
                                        padding = NumberOrStringOrNumbers.of(5)
                                    },
                                "r2" to
                                    textStyleOf {
                                        backgroundColor = "#339911".asColor()
                                        align = "right"
                                        color = "#fff".asColor()
                                        borderRadius = NumberOrStringOrNumbers.of(15)
                                        padding = NumberOrStringOrNumbers.of(5)
                                    },
                                "c1" to
                                    textStyleOf {
                                        backgroundColor = "#7066FF".asColor()
                                        align = "center"
                                        color = "#fff".asColor()
                                        padding = NumberOrStringOrNumbers.of(5)
                                        width = num(100)
                                    },
                                "c2" to
                                    textStyleOf {
                                        backgroundColor = "#339911".asColor()
                                        align = "center"
                                        color = "#fff".asColor()
                                        borderRadius = NumberOrStringOrNumbers.of(15)
                                        padding = NumberOrStringOrNumbers.of(5)
                                        width = num(100)
                                    },
                            )
                    }
                }
            }
            hiddenTextAxes()
        }
