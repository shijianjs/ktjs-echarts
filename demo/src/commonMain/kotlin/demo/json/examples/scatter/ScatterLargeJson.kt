package demo.json.examples.scatter

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val scatterLargeJsonCategory = JsonCategory("json/examples/scatter/ScatterLargeJson.kt")

val scatterLargeJsonExample: JsonExample =
    scatterLargeJsonCategory.example("scatter-large") {
            val random = ScatterRandom(20260428)
            val cloudA = largeCloud(random, 0.0)
            val cloudB = largeCloud(random, 10.0)
            title { text = "${LARGE_SCATTER_POINTS * 2} Points" }
            tooltip {}
            toolbox {
                left = str("center")
                feature = ToolboxFeatureOption().apply { dataZoom = ToolboxFeature() }
            }
            legend {
                orient = Orient.Vertical
                right = num(10)
            }
            xAxis {}
            yAxis {}
            dataZoom { type = DataZoomType.Inside }
            dataZoom { type = DataZoomType.Slider }
            animation = false
            series {
                scatter {
                    name = "A"
                    data = cloudA
                    symbolSize = NumberOrStringOrNumbers.of(3)
                    itemStyle = ItemStyleOption().apply { opacity = 0.4 }
                    large = true
                }
                scatter {
                    name = "B"
                    data = cloudB
                    symbolSize = NumberOrStringOrNumbers.of(3)
                    itemStyle = ItemStyleOption().apply { opacity = 0.4 }
                    large = true
                }
            }
        }
