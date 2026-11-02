package demo.json.examples.scatter

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val scatterLabelAlignTopJsonCategory = JsonCategory("json/examples/scatter/ScatterLabelAlignTopJson.kt")

val scatterLabelAlignTopJsonExample: JsonExample =
    scatterLabelAlignTopJsonCategory.example("scatter-label-align-top") {
            xAxis {}
            yAxis { scale = true }
            series {
                scatter {
                    name = "1990"
                    data = countryItems(COUNTRY_ROWS_1990)
                    emphasis = focusSelf()
                    labelLine =
                        LabelLineOption().apply {
                            show = true
                            length2 = num(5)
                            lineStyle = LineStyleOption().apply { color = "#bbb".asColor() }
                        }
                    label =
                        LabelOption().apply {
                            show = true
                            formatter = "{b}"
                            position = "top"
                            offset = listOf(num(0), num(20))
                        }
                }
            }
        }
