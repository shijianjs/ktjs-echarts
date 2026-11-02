package demo.json.examples.graph

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.GRID_DAYS
import demo.json.examples.shared.GRID_VALUES
import demo.json.examples.shared.graphLink

private val graphGridJsonCategory = JsonCategory("json/examples/graph/GraphGridJson.kt")

val graphGridJsonExample: JsonExample =
    graphGridJsonCategory.example("graph-grid") {
            title { text = "Graph on Cartesian" }
            tooltip {}
            xAxis {
                type = AxisType.Category
                boundaryGap = BoundaryGap.of(false)
                categories(*GRID_DAYS)
            }
            yAxis { type = AxisType.Value }
            series {
                graph {
                    layout = "none"
                    coordinateSystem = "cartesian2d"
                    symbolSize = NumberOrStringOrNumbers.of(40)
                    label { show = true }
                    edgeSymbol = listOf("circle", "arrow")
                    edgeSymbolSize = NumberOrStringOrNumbers.numbers(4, 10)
                    data = GRID_VALUES.map { value -> valueOf(value) }
                    links = List(GRID_VALUES.size - 1) { index -> graphLink("$index", "${index + 1}") }
                    lineStyle = LineStyleOption().apply { color = "#2f4554".asColor() }
                }
            }
        }
