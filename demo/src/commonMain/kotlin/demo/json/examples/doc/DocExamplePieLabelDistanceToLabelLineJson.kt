package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.examples.shared.labelPie
import demo.json.examples.shared.pieLabelSlices
import demo.json.*

private val docExamplePieLabelDistanceToLabelLineJsonCategory = JsonCategory("json/examples/doc/DocExamplePieLabelDistanceToLabelLineJson.kt")

val docExamplePieLabelDistanceToLabelLineJsonExample: JsonExample =
    docExamplePieLabelDistanceToLabelLineJsonCategory.example("doc-example/pie-label-distanceToLabelLine") {
            title { text = "Pie label margin" }
            title {
                subtext = "distanceToLabelLine: 5"
                left = str("25%")
                top = str("75%")
                textAlign = "center"
            }
            title {
                subtext = "distanceToLabelLine: 20"
                left = str("75%")
                top = str("75%")
                textAlign = "center"
            }
            val slices = pieLabelSlices(longNames = true)
            series {
                labelPie(slices, num(0), str("50%"))
                labelPie(slices, str("50%"), num(0))
            }
        }
