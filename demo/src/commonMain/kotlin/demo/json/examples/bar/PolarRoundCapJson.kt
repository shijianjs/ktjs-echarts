package demo.json.examples.bar

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val polarRoundCapJsonCategory = JsonCategory("json/examples/bar/PolarRoundCapJson.kt")

val polarRoundCapJsonExample: JsonExample =
    polarRoundCapJsonCategory.example("polar-roundCap") {
            angleAxis {
                max = num(2)
                splitLine { show = false }
            }
            radiusAxis {
                type = AxisType.Category
                z = 10.0
                categories("v", "w", "x", "y", "z")
            }
            polar { }
            series {
                bar {
                    name = "Without Round Cap"
                    coordinateSystem = "polar"
                    values(4.0, 3.0, 2.0, 1.0, 0.0)
                    itemStyle {
                        borderColor = "red".asColor()
                        opacity = 0.8
                        borderWidth = 1.0
                    }
                }
                bar {
                    name = "With Round Cap"
                    coordinateSystem = "polar"
                    roundCap = true
                    values(4.0, 3.0, 2.0, 1.0, 0.0)
                    itemStyle {
                        borderColor = "green".asColor()
                        opacity = 0.8
                        borderWidth = 1.0
                    }
                }
            }
            legend {
                show = true
                entries("Without Round Cap", "With Round Cap")
            }
        }
