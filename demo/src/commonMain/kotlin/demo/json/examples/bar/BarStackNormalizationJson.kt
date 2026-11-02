package demo.json.examples.bar

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val barStackNormalizationJsonCategory = JsonCategory("json/examples/bar/BarStackNormalizationJson.kt")

val barStackNormalizationJsonExample: JsonExample =
    barStackNormalizationJsonCategory.example("bar-stack-normalization") {
            legend { selectedMode = BooleanOrString.of(false) }
            xAxis {
                type = AxisType.Category
                categories(*WEEK)
            }
            yAxis { type = AxisType.Value }
            series {
                normalizedChannel(0)
                normalizedChannel(1)
                normalizedChannel(2)
                normalizedChannel(3)
                normalizedChannel(4)
            }
        }
