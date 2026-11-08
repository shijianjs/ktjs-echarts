package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.*

private val docExampleGettingStartedJsonCategory = JsonCategory("json/examples/doc/DocExampleGettingStartedJson.kt")

val docExampleGettingStartedJsonExample: JsonExample =
    docExampleGettingStartedJsonCategory.example("doc-example/getting-started") {
            title { text = "ECharts Getting Started Example" }
            tooltip { }
            legend { entries("sales") }
            xAxis { categories("Shirts", "Cardigans", "Chiffons", "Pants", "Heels", "Socks") }
            yAxis { }
            series {
                bar {
                    name = "sales"
                    values(5, 20, 36, 10, 10, 20)
                }
            }
        }
