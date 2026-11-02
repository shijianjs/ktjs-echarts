package demo.json.examples.scatter

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.stroke
import demo.json.points

private val scatterEffectJsonCategory = JsonCategory("json/examples/scatter/ScatterEffectJson.kt")

val scatterEffectJsonExample: JsonExample =
    scatterEffectJsonCategory.example("scatter-effect") {
            xAxis { scale = true }
            yAxis { scale = true }
            series {
                effectScatter {
                    symbolSize = NumberOrStringOrNumbers.of(20)
                    data = points(172.7 to 105.2, 153.4 to 42.0)
                    rippleEffect =
                        RippleEffectOption().apply {
                            scale = 4.0
                            brushType = "stroke"
                        }
                }
                scatter {
                    data =
                        points(
                            161.2 to 51.6, 167.5 to 59.0, 159.5 to 49.2, 157.0 to 63.0,
                            155.8 to 53.6, 170.0 to 59.0, 159.1 to 47.6, 166.0 to 69.8,
                            176.2 to 66.8, 160.2 to 75.2, 172.5 to 55.2, 170.9 to 54.2,
                            172.9 to 62.5, 153.4 to 42.0, 160.0 to 50.0, 147.2 to 49.8,
                            168.2 to 49.2, 175.0 to 73.2, 157.0 to 47.8, 167.6 to 68.8,
                            159.5 to 50.6, 175.0 to 82.5, 166.8 to 57.2, 176.5 to 87.8,
                            170.2 to 72.8, 174.0 to 54.5, 173.0 to 59.8, 179.9 to 67.3,
                            170.5 to 67.8, 160.0 to 47.0, 154.4 to 46.2, 162.0 to 55.0,
                            176.5 to 83.0, 160.0 to 54.4, 152.0 to 45.8, 162.1 to 53.6,
                            170.0 to 73.2, 160.2 to 52.1, 161.3 to 67.9, 166.4 to 56.6,
                            172.7 to 105.2,
                        )
                }
            }
        }
