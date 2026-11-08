package demo.json.examples.chord

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.chordNodes
import demo.json.linkItem

private val chordSimpleJsonCategory = JsonCategory("json/examples/chord/ChordSimpleJson.kt")

val chordSimpleJsonExample: JsonExample =
    chordSimpleJsonCategory.example("chord-simple") {
            tooltip {}
            legend {}
            series {
                chord {
                    clockwise = false
                    label { show = true }
                    lineStyle = LineStyleOption().apply { color = "target".asColor() }
                    data = chordNodes("A", "B", "C", "D")
                    links =
                        listOf(
                            linkItem("A", "B", 40),
                            linkItem("A", "C", 20),
                            linkItem("B", "D", 20),
                        )
                }
            }
        }
