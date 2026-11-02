package demo.json.examples.sunburst

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.JsonPrimitive
import demo.json.*

private val sunburstBookJsonCategory = JsonCategory("json/examples/sunburst/SunburstBookJson.kt")

val sunburstBookJsonExample: JsonExample =
    sunburstBookJsonCategory.example("sunburst-book") {
            backgroundColor = BOOK_BACKGROUND.asColor()
            color = BOOK_COLORS.map { it.asColor() }
            series {
                sunburst {
                    center = listOf(str("50%"), str("48%"))
                    data = bookTree
                    label { color = BOOK_BACKGROUND.asColor() }
                    itemStyle {
                        borderColor = BOOK_BACKGROUND.asColor()
                        borderWidth = 2.0
                    }
                    levels = listOf(
                        level(),
                        level(
                            r0 = num(0),
                            r = num(40),
                            label = levelLabel(rotate = JsonPrimitive(0)),
                        ),
                        level(r0 = num(40), r = num(105)),
                        level(
                            r0 = num(115),
                            r = num(140),
                            itemStyle = levelItemStyle(
                                color = "transparent",
                                shadowBlur = 2,
                                shadowColor = BOOK_COLORS[2],
                            ),
                            label = levelLabel(
                                rotate = JsonPrimitive("tangential"),
                                fontSize = 10,
                                color = BOOK_COLORS[0],
                            ),
                        ),
                        level(
                            r0 = num(140),
                            r = num(145),
                            itemStyle = levelItemStyle(shadowBlur = 80, shadowColor = BOOK_COLORS[0]),
                            label = levelLabel(
                                position = "outside",
                                textShadowBlur = 5,
                                textShadowColor = "#333",
                            ),
                        ),
                    )
                }
            }
        }
