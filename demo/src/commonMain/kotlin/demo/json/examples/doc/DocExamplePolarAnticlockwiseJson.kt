package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.*

private val docExamplePolarAnticlockwiseJsonCategory = JsonCategory("json/examples/doc/DocExamplePolarAnticlockwiseJson.kt")

val docExamplePolarAnticlockwiseJsonExample: JsonExample =
    docExamplePolarAnticlockwiseJsonCategory.example("doc-example/polar-anticlockwise") {
            polar { }
            angleAxis {
                min = num(0)
                max = num(360)
                interval = 30.0
            }
            radiusAxis {
                min = num(0)
                max = num(10)
                interval = 2.0
            }
            series { }
        }
