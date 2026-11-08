package demo.json.examples.sunburst

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.ktjs.echarts.json.*
import kotlinx.serialization.json.JsonPrimitive
import demo.json.*

private val sunburstDrinkJsonCategory = JsonCategory("json/examples/sunburst/SunburstDrinkJson.kt")

val sunburstDrinkJsonExample: JsonExample =
    sunburstDrinkJsonCategory.example("sunburst-drink") {
            title {
                text = "WORLD COFFEE RESEARCH SENSORY LEXICON"
                subtext = "Source: https://worldcoffeeresearch.org/work/sensory-lexicon/"
                sublink = "https://worldcoffeeresearch.org/work/sensory-lexicon/"
                textStyle {
                    fontSize = 14.0
                    align = "center"
                }
                subtextStyle { align = "center" }
            }
            series {
                sunburst {
                    data = drinkTree
                    radius = NumberOrStringOrNumbers.of(num(0), str("95%"))
                    emphasis { focus = "ancestor" }
                    levels = listOf(
                        level(),
                        level(
                            r0 = str("15%"),
                            r = str("35%"),
                            itemStyle = levelItemStyle(borderWidth = 2),
                            label = levelLabel(rotate = JsonPrimitive("tangential")),
                        ),
                        level(
                            r0 = str("35%"),
                            r = str("70%"),
                            label = levelLabel(align = "right"),
                        ),
                        level(
                            r0 = str("70%"),
                            r = str("72%"),
                            itemStyle = levelItemStyle(borderWidth = 3),
                            label = levelLabel(position = "outside", padding = 3, silent = false),
                        ),
                    )
                }
            }
        }
