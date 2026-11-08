package demo.json.examples.chord

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.chordNodes
import demo.json.linkItem

private val chordStyleJsonCategory = JsonCategory("json/examples/chord/ChordStyleJson.kt")

val chordStyleJsonExample: JsonExample =
    chordStyleJsonCategory.example("chord-style") {
            tooltip {}
            legend {}
            series {
                chord {
                    padAngle = 1.0
                    center = listOf(str("50%"), str("48%"))
                    radius = NumberOrStringOrNumbers.of(str("70%"), str("80%"))
                    data = chordNodes("A", "B", "C", "D", "E", "F", "G")
                    itemStyle =
                        ItemStyleOption().apply {
                            borderRadius = NumberOrStringOrNumbers.numbers(0, 15)
                            borderWidth = 2.0
                            borderColor = "#fff".asColor()
                        }
                    lineStyle = LineStyleOption().apply {
                        opacity = 0.3
                        color = "gradient".asColor()
                    }
                    emphasis { focus = "self" }
                    label {
                        show = true
                        position = "inside"
                        color = "#fff".asColor()
                        fontWeight = str("bold")
                    }
                    links =
                        listOf(
                            linkItem("A", "B", 14),
                            linkItem("A", "C", 8),
                            linkItem("B", "C", 20),
                            linkItem("B", "E", 15),
                            linkItem("C", "B", 8),
                            linkItem("C", "E", 3),
                            linkItem("D", "A", 12),
                            linkItem("D", "B", 3),
                            linkItem("E", "A", 15),
                            linkItem("E", "C", 5),
                            linkItem("F", "C", 5),
                            linkItem("G", "A", 6),
                            linkItem("G", "B", 8),
                            linkItem("G", "D", 4),
                        )
                }
            }
        }
