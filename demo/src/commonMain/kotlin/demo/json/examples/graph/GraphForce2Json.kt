package demo.json.examples.graph

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.forceLinks
import demo.json.examples.shared.forceNodes

private val graphForce2JsonCategory = JsonCategory("json/examples/graph/GraphForce2Json.kt")

val graphForce2JsonExample: JsonExample =
    graphForce2JsonCategory.example("graph-force2") {
            series {
                for (index in 0 until 16) {
                    val count = index + 2
                    graph {
                        layout = "force"
                        animation = false
                        data = forceNodes(count)
                        left = str("${(index % 4) * 25}%")
                        top = str("${(index / 4) * 25}%")
                        width = str("25%")
                        height = str("25%")
                        force =
                            ForceOption().apply {
                                repulsion = NumberOrStringOrNumbers.of(60)
                                edgeLength = NumberOrStringOrNumbers.of(2)
                            }
                        links = forceLinks(count)
                    }
                }
            }
        }
