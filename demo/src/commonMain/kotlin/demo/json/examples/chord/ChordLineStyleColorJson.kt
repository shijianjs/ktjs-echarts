package demo.json.examples.chord

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.chordColumn
import demo.json.examples.shared.chordColumnTitle

private val chordLineStyleColorJsonCategory = JsonCategory("json/examples/chord/ChordLineStyleColorJson.kt")

val chordLineStyleColorJsonExample: JsonExample =
    chordLineStyleColorJsonCategory.example("chord-lineStyle-color") {
            tooltip {}
            legend {}
            title {
                text = "lineStyle.color"
                textStyle { fontSize = 24.0 }
            }
            chordColumnTitle(0, "source")
            chordColumnTitle(1, "target")
            chordColumnTitle(2, "gradient")
            series {
                chord { chordColumn(0, "source") }
                chord { chordColumn(1, "target") }
                chord { chordColumn(2, "gradient") }
            }
        }
