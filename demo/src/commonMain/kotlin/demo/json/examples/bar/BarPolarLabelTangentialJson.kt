package demo.json.examples.bar

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val barPolarLabelTangentialJsonCategory = JsonCategory("json/examples/bar/BarPolarLabelTangentialJson.kt")

val barPolarLabelTangentialJsonExample: JsonExample =
    barPolarLabelTangentialJsonCategory.example("bar-polar-label-tangential") {
            title { text = "Tangential Polar Bar Label Position (middle)" }
            polar { radius = NumberOrStringOrNumbers.of(num(30), str("80%")) }
            angleAxis { max = num(4) }
            radiusAxis {
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
        }
