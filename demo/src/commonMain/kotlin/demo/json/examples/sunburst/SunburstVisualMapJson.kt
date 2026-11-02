package demo.json.examples.sunburst

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import demo.json.*

private val sunburstVisualMapJsonCategory = JsonCategory("json/examples/sunburst/SunburstVisualMapJson.kt")

val sunburstVisualMapJsonExample: JsonExample =
    sunburstVisualMapJsonCategory.example("sunburst-visualMap") {
            visualMap {
                type = VisualMapType.Continuous
                min = 0.0
                max = 10.0
                inRange {
                    color = colors("#2F93C8", "#AEC48F", "#FFDB5C", "#F98862")
                }
            }
            series {
                sunburst {
                    data = visualMapTree
                    radius = NumberOrStringOrNumbers.of(num(0), str("90%"))
                }
            }
        }
