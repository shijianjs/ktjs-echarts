package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import demo.json.examples.shared.labelPie
import demo.json.examples.shared.pieLabelSlices
import demo.json.*

private val docExamplePieLabelMarginJsonCategory = JsonCategory("json/examples/doc/DocExamplePieLabelMarginJson.kt")

val docExamplePieLabelMarginJsonExample: JsonExample =
    docExamplePieLabelMarginJsonCategory.example("doc-example/pie-label-margin") {
            title { text = "Pie label margin" }
            title {
                subtext = "margin: \"25%\""
                left = str("25%")
                top = str("75%")
                textAlign = "center"
            }
            title {
                subtext = "margin: 10"
                left = str("75%")
                top = str("75%")
                textAlign = "center"
            }
            val slices = pieLabelSlices(longNames = false)
            series {
                labelPie(slices, num(0), str("50%"), align = "edge", margin = str("25%"))
                labelPie(slices, str("50%"), num(0), align = "edge", margin = num(10))
            }
        }
