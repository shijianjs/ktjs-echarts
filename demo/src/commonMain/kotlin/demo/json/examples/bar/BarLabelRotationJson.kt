package demo.json.examples.bar

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val barLabelRotationJsonCategory = JsonCategory("json/examples/bar/BarLabelRotationJson.kt")

val barLabelRotationJsonExample: JsonExample =
    // ------------------------------------------------------------- long tail
        barLabelRotationJsonCategory.example("bar-label-rotation") {
            shadowAxisTooltip()
            legend { entries("Forest", "Steppe", "Desert", "Wetland") }
            xAxis {
                type = AxisType.Category
                axisTick { show = false }
                categories("2012", "2013", "2014", "2015", "2016")
            }
            yAxis { type = AxisType.Value }
            series {
                bar {
                    name = "Forest"
                    barGap = "0"
                    label = rotatedLabel()
                    emphasis = focusSeries()
                    values(320.0, 332.0, 301.0, 334.0, 390.0)
                }
                bar {
                    name = "Steppe"
                    label = rotatedLabel()
                    emphasis = focusSeries()
                    values(220.0, 182.0, 191.0, 234.0, 290.0)
                }
                bar {
                    name = "Desert"
                    label = rotatedLabel()
                    emphasis = focusSeries()
                    values(150.0, 232.0, 201.0, 154.0, 190.0)
                }
                bar {
                    name = "Wetland"
                    label = rotatedLabel()
                    emphasis = focusSeries()
                    values(98.0, 77.0, 101.0, 99.0, 40.0)
                }
            }
            color = colors("#5470c6", "#91cc75", "#fac858", "#ee6666")
        }
