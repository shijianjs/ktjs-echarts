package demo.json.examples.sunburst

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import demo.json.*

private val sunburstSimpleJsonCategory = JsonCategory("json/examples/sunburst/SunburstSimpleJson.kt")

val sunburstSimpleJsonExample: JsonExample =
    sunburstSimpleJsonCategory.example("sunburst-simple") {
            series {
                sunburst {
                    data = familyTree
                    radius = NumberOrStringOrNumbers.of(num(0), str("90%"))
                }
            }
        }
