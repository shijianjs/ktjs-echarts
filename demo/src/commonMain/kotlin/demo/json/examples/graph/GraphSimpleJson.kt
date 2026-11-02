package demo.json.examples.graph

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.SIMPLE_LINKS
import demo.json.examples.shared.SIMPLE_NODES

private val graphSimpleJsonCategory = JsonCategory("json/examples/graph/GraphSimpleJson.kt")

val graphSimpleJsonExample: JsonExample =
    graphSimpleJsonCategory.example("graph-simple") {
            title { text = "Basic Graph" }
            tooltip {}
            animationDurationUpdate = 1500.0
            animationEasingUpdate = "quinticInOut"
            series {
                graph {
                    layout = "none"
                    symbolSize = NumberOrStringOrNumbers.of(50)
                    roam = BooleanOrString.of(true)
                    label { show = true }
                    edgeSymbol = listOf("circle", "arrow")
                    edgeSymbolSize = NumberOrStringOrNumbers.numbers(4, 10)
                    edgeLabel = LabelOption().apply { fontSize = 20.0 }
                    data = SIMPLE_NODES
                    links = SIMPLE_LINKS
                    lineStyle = LineStyleOption().apply {
                        opacity = 0.9
                        width = 2.0
                    }
                }
            }
        }
