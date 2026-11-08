package demo.json.examples.line

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val lineInCartesianCoordinateSystemJsonCategory = JsonCategory("json/examples/line/LineInCartesianCoordinateSystemJson.kt")

val lineInCartesianCoordinateSystemJsonExample: JsonExample =
    lineInCartesianCoordinateSystemJsonCategory.example("line-in-cartesian-coordinate-system") {
            xAxis { }
            yAxis { }
            series {
                line {
                    data = listOf(point(10, 40), point(50, 100), point(40, 20))
                }
            }
        }
