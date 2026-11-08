package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.examples.shared.labelPie
import demo.json.examples.shared.pieLabelSlices
import demo.json.*

private val docExamplePieLabelBleedMarginJsonCategory = JsonCategory("json/examples/doc/DocExamplePieLabelBleedMarginJson.kt")

val docExamplePieLabelBleedMarginJsonExample: JsonExample =
    docExamplePieLabelBleedMarginJsonCategory.example("doc-example/pie-label-bleedMargin") {
            title { text = "Pie label margin" }
            title {
                subtext = "bleedMargin: 50"
                left = str("25%")
                top = str("75%")
                textAlign = "center"
            }
            title {
                subtext = "bleedMargin: 10"
                left = str("75%")
                top = str("75%")
                textAlign = "center"
            }
            val slices = pieLabelSlices(longNames = true)
            series {
                labelPie(slices, num(0), str("50%"), bleedMargin = 50.0)
                labelPie(slices, str("50%"), num(0), bleedMargin = 10.0)
            }
        }
