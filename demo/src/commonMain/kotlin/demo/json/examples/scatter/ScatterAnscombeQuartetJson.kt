package demo.json.examples.scatter

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val scatterAnscombeQuartetJsonCategory = JsonCategory("json/examples/scatter/ScatterAnscombeQuartetJson.kt")

val scatterAnscombeQuartetJsonExample: JsonExample =
    scatterAnscombeQuartetJsonCategory.example("scatter-anscombe-quartet") {
            title {
                text = "Anscombe's quartet"
                left = str("center")
                top = num(0)
            }
            grid {
                left = str("7%")
                top = str("7%")
                width = str("38%")
                height = str("38%")
            }
            grid {
                right = str("7%")
                top = str("7%")
                width = str("38%")
                height = str("38%")
            }
            grid {
                left = str("7%")
                bottom = str("7%")
                width = str("38%")
                height = str("38%")
            }
            grid {
                right = str("7%")
                bottom = str("7%")
                width = str("38%")
                height = str("38%")
            }
            tooltip { formatter = "Group {a}: ({c})" }
            repeat(4) { index ->
                xAxis {
                    gridIndex = index
                    min = num(0)
                    max = num(20)
                }
                yAxis {
                    gridIndex = index
                    min = num(0)
                    max = num(15)
                }
            }
            series {
                repeat(4) { index ->
                    scatter {
                        name = ANSCOMBE_NAMES[index]
                        xAxisIndex = index
                        yAxisIndex = index
                        data = ANSCOMBE_GROUPS[index].map { point(it[0], it[1]) }
                        markLine {
                            symbol = listOf("none", "none")
                            lineStyle = LineStyleOption().apply { type = "solid" }
                            label =
                                LabelOption().apply {
                                    formatter = "y = 0.5 * x + 3"
                                    align = "right"
                                }
                            data =
                                listOf(
                                    MarkLineDataItem.of(
                                        MarkLineData().apply { coord = listOf(num(0), num(3)) },
                                        MarkLineData().apply { coord = listOf(num(20), num(13)) },
                                    ),
                                )
                        }
                    }
                }
            }
        }
