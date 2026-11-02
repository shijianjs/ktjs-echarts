package demo.json.examples.themeriver

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import demo.json.examples.shared.lastfmBand
import demo.json.*

private val themeRiverLastfmJsonCategory = JsonCategory("json/examples/themeriver/ThemeRiverLastfmJson.kt")

val themeRiverLastfmJsonExample: JsonExample =
    themeRiverLastfmJsonCategory.example("themeRiver-lastfm") {
            singleAxis { max = str("dataMax") }
            series {
                themeRiver {
                    label { show = false }
                    data =
                        lastfmBand(
                            "Andrew Bird",
                            listOf(0, 49, 67, 16, 0, 19, 19, 0, 0, 1, 10, 5),
                        ) +
                            lastfmBand(
                                "Laura Veirs",
                                listOf(0, 6, 3, 34, 0, 16, 1, 0, 0, 1, 6, 0),
                            ) +
                            lastfmBand(
                                "Brian Eno",
                                listOf(0, 8, 13, 15, 0, 12, 23, 0, 0, 1, 0, 1),
                            ) +
                            lastfmBand(
                                "Christopher Willits",
                                listOf(0, 9, 28, 0, 91, 6, 1, 0, 0, 0, 7, 18),
                            )
                }
            }
        }
