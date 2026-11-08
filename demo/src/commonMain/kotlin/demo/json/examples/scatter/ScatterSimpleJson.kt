package demo.json.examples.scatter

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val scatterSimpleJsonCategory = JsonCategory("json/examples/scatter/ScatterSimpleJson.kt")

val scatterSimpleJsonExample: JsonExample =
    scatterSimpleJsonCategory.example("scatter-simple") {
            xAxis {}
            yAxis {}
            series {
                scatter {
                    symbolSize = NumberOrStringOrNumbers.of(20)
                    data = anscombePoints()
                }
            }
        }
