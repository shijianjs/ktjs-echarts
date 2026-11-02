package demo.json.examples.bar

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val barPolarStackJsonCategory = JsonCategory("json/examples/bar/BarPolarStackJson.kt")

val barPolarStackJsonExample: JsonExample =
    barPolarStackJsonCategory.example("bar-polar-stack") {
            angleAxis { }
            radiusAxis {
                type = AxisType.Category
                z = 10.0
                categories("Mon", "Tue", "Wed", "Thu")
            }
            polar { }
            series {
                polarStacked("A", 1.0, 2.0, 3.0, 4.0)
                polarStacked("B", 2.0, 4.0, 6.0, 8.0)
                polarStacked("C", 1.0, 2.0, 3.0, 4.0)
            }
            legend {
                show = true
                entries("A", "B", "C")
            }
        }
