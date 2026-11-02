package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.nodes
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import demo.json.examples.shared.sunburstNode
import demo.json.*

private val docExampleSunburstColorJsonCategory = JsonCategory("json/examples/doc/DocExampleSunburstColorJson.kt")

val docExampleSunburstColorJsonExample: JsonExample =
    docExampleSunburstColorJsonCategory.example("doc-example/sunburst-color") {
            series {
                sunburst {
                    data =
                        nodes(
                            sunburstNode(
                                "A",
                                10,
                                null,
                                sunburstNode("Aa", 3, color = "red"),
                                sunburstNode("Ab", 5),
                            ),
                            sunburstNode(
                                "B",
                                null,
                                "red",
                                sunburstNode("Ba", 4),
                                sunburstNode("Bb", 2),
                            ),
                            sunburstNode("C", 3),
                        )
                    itemStyle { color = "#aaa".asColor() }
                    levels =
                        listOf(
                            buildJsonObject { },
                            buildJsonObject {
                                put(
                                    "itemStyle",
                                    buildJsonObject { put("color", "blue") },
                                )
                            },
                        )
                }
            }
        }
