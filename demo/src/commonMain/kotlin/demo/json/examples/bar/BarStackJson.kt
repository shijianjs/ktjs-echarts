package demo.json.examples.bar

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val barStackJsonCategory = JsonCategory("json/examples/bar/BarStackJson.kt")

val barStackJsonExample: JsonExample =
    barStackJsonCategory.example("bar-stack") {
            shadowAxisTooltip()
            legend { }
            xAxis {
                type = AxisType.Category
                categories(*WEEK)
            }
            yAxis { type = AxisType.Value }
            val searchEngine = doubleArrayOf(862.0, 1018.0, 964.0, 1026.0, 1679.0, 1600.0, 1570.0)
            series {
                bar {
                    name = "Direct"
                    emphasis = focusSeries()
                    values(320.0, 332.0, 301.0, 334.0, 390.0, 330.0, 320.0)
                }
                bar {
                    name = "Email"
                    stack = "Ad"
                    emphasis = focusSeries()
                    values(120.0, 132.0, 101.0, 134.0, 90.0, 230.0, 210.0)
                }
                bar {
                    name = "Union Ads"
                    stack = "Ad"
                    emphasis = focusSeries()
                    values(220.0, 182.0, 191.0, 234.0, 290.0, 330.0, 310.0)
                }
                bar {
                    name = "Video Ads"
                    stack = "Ad"
                    emphasis = focusSeries()
                    values(150.0, 232.0, 201.0, 154.0, 190.0, 330.0, 410.0)
                }
                bar {
                    name = "Search Engine"
                    emphasis = focusSeries()
                    values(*searchEngine.toTypedArray())
                    markLine {
                        lineStyle = LineStyleOption().apply { type = "dashed" }
                        data =
                            listOf(
                                MarkLineDataItem.of(
                                    MarkLineData().apply { type = "min" },
                                    MarkLineData().apply { type = "max" },
                                ),
                            )
                    }
                }
                bar {
                    name = "Baidu"
                    barWidth = num(5)
                    stack = "Search Engine"
                    emphasis = focusSeries()
                    values(620.0, 732.0, 701.0, 734.0, 1090.0, 1130.0, 1120.0)
                }
                bar {
                    name = "Google"
                    stack = "Search Engine"
                    emphasis = focusSeries()
                    values(120.0, 132.0, 101.0, 134.0, 290.0, 230.0, 220.0)
                }
                bar {
                    name = "Bing"
                    stack = "Search Engine"
                    emphasis = focusSeries()
                    values(60.0, 72.0, 71.0, 74.0, 190.0, 130.0, 110.0)
                }
                bar {
                    name = "Others"
                    stack = "Search Engine"
                    emphasis = focusSeries()
                    values(62.0, 82.0, 91.0, 84.0, 109.0, 110.0, 120.0)
                }
            }
        }
