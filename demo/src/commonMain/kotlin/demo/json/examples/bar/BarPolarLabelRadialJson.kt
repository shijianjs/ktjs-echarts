package demo.json.examples.bar

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val barPolarLabelRadialJsonCategory = JsonCategory("json/examples/bar/BarPolarLabelRadialJson.kt")

val barPolarLabelRadialJsonExample: JsonExample =
    // --------------------------------------------------------------- polar
        barPolarLabelRadialJsonCategory.example("bar-polar-label-radial") {
            title { text = "Radial Polar Bar Label Position (middle)" }
            polar { radius = NumberOrStringOrNumbers.of(num(30), str("80%")) }
            radiusAxis { max = num(4) }
            angleAxis {
                type = AxisType.Category
                categories("a", "b", "c", "d")
            }
            tooltip { }
            series {
                bar {
                    coordinateSystem = "polar"
                    label {
                        show = true
                        position = "middle"
                        formatter = "{b}: {c}"
                    }
                    values(2.0, 1.2, 2.4, 3.6)
                }
            }
            animation = false
        }
