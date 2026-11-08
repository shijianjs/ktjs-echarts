package demo.json.examples.bar

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val bar1JsonCategory = JsonCategory("json/examples/bar/Bar1Json.kt")

val bar1JsonExample: JsonExample =
    bar1JsonCategory.example("bar1") {
            title {
                text = "Rainfall vs Evaporation"
                subtext = "Fake Data"
            }
            tooltip { trigger = TooltipTrigger.Axis }
            legend { entries("Rainfall", "Evaporation") }
            showToolbox(magicTypes = listOf(SeriesType.Line, SeriesType.Bar))
            xAxis {
                type = AxisType.Category
                categories(*MONTHS)
            }
            yAxis { type = AxisType.Value }
            series {
                bar {
                    name = "Rainfall"
                    values(2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3)
                    markPoint {
                        data = listOf(markPointStat("Max", "max"), markPointStat("Min", "min"))
                    }
                    markLine { data = listOf(averageMarkLine()) }
                }
                bar {
                    name = "Evaporation"
                    values(2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3)
                    markPoint {
                        data =
                            listOf(
                                markPointAt("Max", 182.2, 7, 183.0),
                                markPointAt("Min", 2.3, 11, 3.0),
                            )
                    }
                    markLine { data = listOf(averageMarkLine()) }
                }
            }
        }
