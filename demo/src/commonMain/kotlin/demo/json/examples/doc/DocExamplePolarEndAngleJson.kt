package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import demo.json.*

private val docExamplePolarEndAngleJsonCategory = JsonCategory("json/examples/doc/DocExamplePolarEndAngleJson.kt")

val docExamplePolarEndAngleJsonExample: JsonExample =
    docExamplePolarEndAngleJsonCategory.example("doc-example/polar-end-angle") {
            tooltip { }
            angleAxis {
                type = AxisType.Category
                endAngle = -180.0
                categories("S1", "S2", "S3")
            }
            radiusAxis { }
            polar { }
            series {
                bar {
                    polarIndex = 0
                    values(1, 2, 3)
                    coordinateSystem = "polar"
                }
            }
        }
