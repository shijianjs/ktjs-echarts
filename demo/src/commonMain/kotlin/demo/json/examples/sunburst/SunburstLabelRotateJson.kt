package demo.json.examples.sunburst

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.JsonPrimitive
import demo.json.*

private val sunburstLabelRotateJsonCategory = JsonCategory("json/examples/sunburst/SunburstLabelRotateJson.kt")

val sunburstLabelRotateJsonExample: JsonExample =
    sunburstLabelRotateJsonCategory.example("sunburst-label-rotate") {
            series {
                sunburst {
                    radius = NumberOrStringOrNumbers.of(str("15%"), str("80%"))
                    emphasis { focus = "ancestor" }
                    data = rotateTree
                    label {
                        color = "#000".asColor()
                        textBorderColor = "#fff".asColor()
                        textBorderWidth = 2.0
                    }
                    levels = listOf(
                        level(),
                        level(
                            itemStyle = levelItemStyle(color = "#CD4949"),
                            label = levelLabel(rotate = JsonPrimitive("radial")),
                        ),
                        level(
                            itemStyle = levelItemStyle(color = "#F47251"),
                            label = levelLabel(rotate = JsonPrimitive("tangential")),
                        ),
                        level(
                            itemStyle = levelItemStyle(color = "#FFC75F"),
                            label = levelLabel(rotate = JsonPrimitive(0)),
                        ),
                    )
                }
            }
        }
