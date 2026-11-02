package demo.json.examples.chord

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.chordNodes
import demo.json.linkItem

private val chordMinAngleJsonCategory = JsonCategory("json/examples/chord/ChordMinAngleJson.kt")

val chordMinAngleJsonExample: JsonExample =
    chordMinAngleJsonCategory.example("chord-minAngle") {
            tooltip {}
            legend {}
            series {
                chord {
                    label { show = true }
                    minAngle = 30.0
                    data = chordNodes("A", "B", "C", "D", "E", "F")
                    links =
                        listOf(
                            linkItem("A", "B", 40),
                            linkItem("B", "C", 20),
                            linkItem("E", "A", 5),
                        )
                }
            }
        }
