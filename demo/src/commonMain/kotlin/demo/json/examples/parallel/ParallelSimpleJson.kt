package demo.json.examples.parallel

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.*

private val parallelSimpleJsonCategory = JsonCategory("json/examples/parallel/ParallelSimpleJson.kt")

val parallelSimpleJsonExample: JsonExample =
    parallelSimpleJsonCategory.example("parallel-simple") {
            parallelAxis { name = "Price" }
            parallelAxis { name = "Net Weight" }
            parallelAxis { name = "Amount" }
            parallelAxis {
                name = "Score"
                type = AxisType.Category
                categories("Excellent", "Good", "OK", "Bad")
            }
            series {
                parallel {
                    lineStyle { width = 4.0 }
                    data =
                        listOf(
                            arrayValue(valueOf(12.99), valueOf(100), valueOf(82), textValue("Good")),
                            arrayValue(valueOf(9.99), valueOf(80), valueOf(77), textValue("OK")),
                            arrayValue(valueOf(20), valueOf(120), valueOf(60), textValue("Excellent")),
                        )
                }
            }
        }
