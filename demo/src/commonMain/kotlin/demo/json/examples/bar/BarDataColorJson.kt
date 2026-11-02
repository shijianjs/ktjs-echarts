package demo.json.examples.bar

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val barDataColorJsonCategory = JsonCategory("json/examples/bar/BarDataColorJson.kt")

val barDataColorJsonExample: JsonExample =
    barDataColorJsonCategory.example("bar-data-color") {
            xAxis {
                type = AxisType.Category
                categories(*WEEK)
            }
            yAxis { type = AxisType.Value }
            series {
                bar {
                    data =
                        listOf(
                            valueOf(120),
                            dataItem {
                                value = valueOf(200)
                                itemStyle { color = "#505372".asColor() }
                            },
                            valueOf(150),
                            valueOf(80),
                            valueOf(70),
                            valueOf(110),
                            valueOf(130),
                        )
                }
            }
        }
