package demo.json.examples.line

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val lineMarklineJsonCategory = JsonCategory("json/examples/line/LineMarklineJson.kt")

val lineMarklineJsonExample: JsonExample =
    lineMarklineJsonCategory.example("line-markline") {
            animation = false
            textStyle = TextStyleOption().apply { fontSize = 14.0 }
            grid {
                top = num(30)
                left = num(60)
                right = num(60)
                bottom = num(40)
            }
            xAxis {
                type = AxisType.Category
                boundaryGap = BoundaryGap.of(true)
                splitArea { show = true }
                categories("A", "B", "C", "D", "E")
            }
            yAxis {
                type = AxisType.Value
                max = num(2)
            }
            series {
                line {
                    name = "line"
                    stack = "all"
                    symbolSize = NumberOrStringOrNumbers.of(6)
                    data = listOf(valueOf(0.3), valueOf(1.4), valueOf(1.2), valueOf(1.0), valueOf(0.6))
                    markLine {
                        // `distance: [20, 8]` is an array on one label, which `LabelOption` does not
                        // carry; the labels keep their positions only.
                        data =
                            MARK_LINE_POSITIONS.flatMapIndexed { index, positionName ->
                                val axisValue = 1.8 - 0.2 * kotlin.math.floor(index / 3.0)
                                // `insideMiddle` and `middle` land on the same place, so the diagonal
                                // shares one label.
                                val labelText =
                                    if (positionName == "insideMiddle") "insideMiddle / middle" else positionName
                                if (positionName == "middle") {
                                    listOf(horizontalMarkLine(positionName, axisValue))
                                } else {
                                    listOf(
                                        horizontalMarkLine(positionName, axisValue),
                                        diagonalMarkLine(positionName, labelText),
                                    )
                                }
                            }
                    }
                }
            }
        }
