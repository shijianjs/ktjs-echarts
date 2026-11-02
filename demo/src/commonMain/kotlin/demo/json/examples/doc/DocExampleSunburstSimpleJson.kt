package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.nodeItem
import demo.json.nodes
import io.github.shijianjs.kmpechartsjson.*
import demo.json.*

private val docExampleSunburstSimpleJsonCategory = JsonCategory("json/examples/doc/DocExampleSunburstSimpleJson.kt")

val docExampleSunburstSimpleJsonExample: JsonExample =
    docExampleSunburstSimpleJsonCategory.example("doc-example/sunburst-simple") {
            series {
                sunburst {
                    data =
                        nodes(
                            nodeItem("A", 10, nodeItem("Aa", 3), nodeItem("Ab", 5)),
                            nodeItem("B", null, nodeItem("Ba", 4), nodeItem("Bb", 2)),
                            nodeItem("C", 3),
                        )
                }
            }
        }
