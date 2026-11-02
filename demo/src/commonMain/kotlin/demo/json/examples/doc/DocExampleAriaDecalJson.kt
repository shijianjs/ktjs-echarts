package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.namedValue
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.JsonPrimitive
import demo.json.examples.shared.DECAL_COLOR
import demo.json.examples.shared.DECAL_COUNT
import demo.json.examples.shared.decalOf
import demo.json.examples.shared.jsonIntArrays
import demo.json.examples.shared.jsonInts
import demo.json.*

private val docExampleAriaDecalJsonCategory = JsonCategory("json/examples/doc/DocExampleAriaDecalJson.kt")

val docExampleAriaDecalJsonExample: JsonExample =
    docExampleAriaDecalJsonCategory.example("doc-example/aria-decal") {
            xAxis {
                type = AxisType.Value
                max = num(1)
                show = false
            }
            yAxis {
                type = AxisType.Value
                show = false
            }
            aria {
                enabled = true
                decal =
                    AriaDecalOption().apply {
                        show = true
                        decals =
                            listOf(
                                decalOf(
                                    color = DECAL_COLOR,
                                    dashArrayX = jsonInts(1, 0),
                                    dashArrayY = jsonInts(2, 5),
                                    symbolSize = 1.0,
                                    rotation = kotlin.math.PI / 6,
                                ),
                                decalOf(
                                    color = DECAL_COLOR,
                                    symbol = "circle",
                                    dashArrayX = jsonIntArrays(listOf(8, 8), listOf(0, 8, 8, 0)),
                                    dashArrayY = jsonInts(6, 0),
                                    symbolSize = 0.8,
                                ),
                                decalOf(
                                    color = DECAL_COLOR,
                                    dashArrayX = jsonInts(1, 0),
                                    dashArrayY = jsonInts(4, 3),
                                    rotation = -kotlin.math.PI / 4,
                                ),
                                decalOf(
                                    color = DECAL_COLOR,
                                    dashArrayX = jsonIntArrays(listOf(6, 6), listOf(0, 6, 6, 0)),
                                    dashArrayY = jsonInts(6, 0),
                                ),
                                decalOf(
                                    color = DECAL_COLOR,
                                    dashArrayX = jsonIntArrays(listOf(1, 0), listOf(1, 6)),
                                    dashArrayY = jsonInts(1, 0, 6, 0),
                                    rotation = kotlin.math.PI / 4,
                                ),
                                decalOf(
                                    color = DECAL_COLOR,
                                    symbol = "triangle",
                                    dashArrayX = jsonIntArrays(listOf(9, 9), listOf(0, 9, 9, 0)),
                                    dashArrayY = jsonInts(7, 2),
                                    symbolSize = 0.75,
                                ),
                                decalOf(
                                    color = DECAL_COLOR,
                                    dashArrayX = jsonInts(1, 0),
                                    dashArrayY = JsonPrimitive(10),
                                    rotation = 0.0,
                                ),
                                decalOf(
                                    color = DECAL_COLOR,
                                    dashArrayX = jsonInts(10, 10),
                                    dashArrayY = jsonInts(1, 0),
                                    rotation = 0.0,
                                ),
                                decalOf(
                                    color = DECAL_COLOR,
                                    dashArrayX = jsonInts(10, 5, 20, 5),
                                    dashArrayY = JsonPrimitive(10),
                                    rotation = 0.0,
                                ),
                                decalOf(
                                    color = DECAL_COLOR,
                                    dashArrayX = jsonIntArrays(listOf(10, 5, 20, 5), listOf(20, 5)),
                                    dashArrayY = jsonInts(10, 5),
                                    rotation = 0.0,
                                ),
                                decalOf(
                                    symbol = "rect",
                                    dashArrayX = jsonIntArrays(listOf(8, 8), listOf(0, 8, 8, 0)),
                                    dashArrayY = jsonInts(8, 0),
                                    symbolSize = 0.8,
                                    rotation = kotlin.math.PI / 4,
                                    color = "black",
                                ),
                            )
                    }
            }
            series {
                pie {
                    radius = NumberOrStringOrNumbers.of(str("25%"), str("80%"))
                    label { show = false }
                    data = List(DECAL_COUNT) { index -> namedValue("decal ${index + 1}", 1) }
                }
            }
            color = colors("#ddd")
        }
