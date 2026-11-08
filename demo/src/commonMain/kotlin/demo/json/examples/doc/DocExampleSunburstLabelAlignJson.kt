package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.nodes
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.examples.shared.alignedSunburstNode
import demo.json.*

private val docExampleSunburstLabelAlignJsonCategory = JsonCategory("json/examples/doc/DocExampleSunburstLabelAlignJson.kt")

val docExampleSunburstLabelAlignJsonExample: JsonExample =
    docExampleSunburstLabelAlignJsonCategory.example("doc-example/sunburst-label-align") {
            series {
                sunburst {
                    radius = NumberOrStringOrNumbers.of(str("20%"), str("90%"))
                    emphasis { focus = "none" }
                    data =
                        nodes(
                            *Array(8) {
                                alignedSunburstNode(
                                    name = "left",
                                    align = "left",
                                    children =
                                        listOf(
                                            alignedSunburstNode(name = "right", align = "right", value = 1),
                                        ),
                                )
                            },
                        )
                    label { fontSize = 14.0 }
                }
            }
        }
