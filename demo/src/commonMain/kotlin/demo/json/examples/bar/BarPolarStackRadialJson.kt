package demo.json.examples.bar

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val barPolarStackRadialJsonCategory = JsonCategory("json/examples/bar/BarPolarStackRadialJson.kt")

val barPolarStackRadialJsonExample: JsonExample =
    barPolarStackRadialJsonCategory.example("bar-polar-stack-radial") {
            angleAxis {
                type = AxisType.Category
                categories(*WEEK)
            }
            radiusAxis { }
            polar { }
            series {
                polarStacked("A", 1.0, 2.0, 3.0, 4.0, 3.0, 5.0, 1.0)
                polarStacked("B", 2.0, 4.0, 6.0, 1.0, 3.0, 2.0, 1.0)
                polarStacked("C", 1.0, 2.0, 3.0, 4.0, 1.0, 2.0, 5.0)
            }
            legend {
                show = true
                entries("A", "B", "C")
            }
        }
