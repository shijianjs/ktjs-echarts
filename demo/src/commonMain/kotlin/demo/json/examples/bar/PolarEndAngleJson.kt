package demo.json.examples.bar

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import demo.json.*

private val polarEndAngleJsonCategory = JsonCategory("json/examples/bar/PolarEndAngleJson.kt")

val polarEndAngleJsonExample: JsonExample =
    polarEndAngleJsonCategory.example("polar-endAngle") {
            tooltip { }
            angleAxis {
                type = AxisType.Category
                polarIndex = 0
                startAngle = 90.0
                endAngle = 0.0
                categories("S1", "S2", "S3")
            }
            angleAxis {
                type = AxisType.Category
                polarIndex = 1
                startAngle = -90.0
                endAngle = -180.0
                categories("T1", "T2", "T3")
            }
            radiusAxis { polarIndex = 0 }
            radiusAxis { polarIndex = 1 }
            polar { }
            polar { }
            series {
                bar {
                    polarIndex = 0
                    values(1, 2, 3)
                    coordinateSystem = "polar"
                }
                bar {
                    polarIndex = 1
                    values(1, 2, 3)
                    coordinateSystem = "polar"
                }
            }
        }
