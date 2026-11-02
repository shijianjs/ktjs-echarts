package demo.json.examples.scatter

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val scatterWeightJsonCategory = JsonCategory("json/examples/scatter/ScatterWeightJson.kt")

val scatterWeightJsonExample: JsonExample =
    scatterWeightJsonCategory.example("scatter-weight") {
            title {
                text = "Male and female height and weight distribution"
                subtext = "Data from: Heinz 2003"
            }
            grid {
                left = str("3%")
                right = str("7%")
                bottom = str("7%")
                containLabel = true
            }
            tooltip {
                showDelay = 0.0
                axisPointer =
                    AxisPointerOption().apply {
                        show = true
                        type = "cross"
                        lineStyle = LineStyleOption().apply {
                            type = "dashed"
                            width = 1.0
                        }
                    }
            }
            toolbox {
                feature =
                    ToolboxFeatureOption().apply {
                        dataZoom = ToolboxFeature()
                        brush = ToolboxFeature()
                    }
            }
            brush {}
            legend {
                entries("Female", "Male")
                left = str("center")
                bottom = num(10)
            }
            xAxis {
                type = AxisType.Value
                scale = true
                axisLabel { formatter = "{value} cm" }
                splitLine { show = false }
            }
            yAxis {
                type = AxisType.Value
                scale = true
                axisLabel { formatter = "{value} kg" }
                splitLine { show = false }
            }
            series {
                scatter { weightSeries("Female", FEMALE_WEIGHT, "AVG", 160.0) }
                scatter { weightSeries("Male", MALE_WEIGHT, "Average", 170.0) }
            }
        }
