package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.*

private val docExamplePolarStartAngleJsonCategory = JsonCategory("json/examples/doc/DocExamplePolarStartAngleJson.kt")

val docExamplePolarStartAngleJsonExample: JsonExample =
    docExamplePolarStartAngleJsonCategory.example("doc-example/polar-start-angle") {
            polar { }
            angleAxis {
                min = num(0)
                max = num(360)
                interval = 30.0
                startAngle = 45.0
            }
            radiusAxis {
                min = num(0)
                max = num(10)
                interval = 2.0
            }
            series { }
        }
