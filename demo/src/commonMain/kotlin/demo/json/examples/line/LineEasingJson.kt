package demo.json.examples.line

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val lineEasingJsonCategory = JsonCategory("json/examples/line/LineEasingJson.kt")

val lineEasingJsonExample: JsonExample =
    lineEasingJsonCategory.example("line-easing") {
            val cells = easingCells(EASINGS.size)
            EASINGS.forEachIndexed { index, easing ->
                val cell = cells[index]
                title {
                    text = easing.name
                    textAlign = "center"
                    left = str(percentText(cell.leftPercent + cell.sizePercent / 2))
                    top = str(percentText(cell.topPercent))
                    textStyle {
                        fontSize = 12.0
                        fontWeight = str("normal")
                    }
                }
                grid {
                    show = true
                    borderWidth = 0.0
                    shadowColor = "rgba(0, 0, 0, 0.3)".asColor()
                    shadowBlur = 2.0
                    left = str(percentText(cell.leftPercent))
                    top = str(percentText(cell.topPercent))
                    width = str(percentText(cell.sizePercent))
                    height = str(percentText(cell.sizePercent))
                }
                xAxis {
                    type = AxisType.Value
                    show = false
                    min = num(0)
                    max = num(1)
                    gridIndex = index
                }
                yAxis {
                    type = AxisType.Value
                    show = false
                    min = num(-0.4)
                    max = num(1.4)
                    gridIndex = index
                }
            }
            series {
                EASINGS.forEachIndexed { index, easing ->
                    line {
                        name = easing.name
                        xAxisIndex = index
                        yAxisIndex = index
                        showSymbol = false
                        animationEasing = easing.name
                        animationDuration = 1000.0
                        data =
                            List(SAMPLE_COUNT + 1) { step ->
                                val k = step.toDouble() / SAMPLE_COUNT
                                point(k, easing.at(k))
                            }
                    }
                }
            }
            title {
                text = "Different Easing Functions"
                top = str("bottom")
                left = str("center")
            }
        }
