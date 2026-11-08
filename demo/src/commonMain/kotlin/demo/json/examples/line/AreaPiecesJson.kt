package demo.json.examples.line

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val areaPiecesJsonCategory = JsonCategory("json/examples/line/AreaPiecesJson.kt")

val areaPiecesJsonExample: JsonExample =
    areaPiecesJsonCategory.example("area-pieces") {
            xAxis {
                type = AxisType.Category
                boundaryGap = BoundaryGap.of(false)
                categories(
                    "2019-10-10", "2019-10-11", "2019-10-12", "2019-10-13", "2019-10-14",
                    "2019-10-15", "2019-10-16", "2019-10-17", "2019-10-18",
                )
            }
            yAxis {
                type = AxisType.Value
                boundaryGap = BoundaryGap.of(num(0), str("30%"))
            }
            // The visual map shades the second and the sixth category band; the mark lines draw the
            // borders of those bands.
            visualMap {
                type = VisualMapType.Piecewise
                show = false
                dimension = num(0)
                seriesIndex = listOf(0)
                pieces = listOf(shadedPiece(1.0, 3.0), shadedPiece(5.0, 7.0))
            }
            series {
                line {
                    smooth = BooleanOrNumber.of(0.6)
                    symbol = "none"
                    lineStyle {
                        color = "#5470C6".asColor()
                        width = 5.0
                    }
                    areaStyle { }
                    markLine {
                        symbol = listOf("none", "none")
                        label = LabelOption().apply { show = false }
                        data =
                            listOf(
                                categoryBoundary(1),
                                categoryBoundary(3),
                                categoryBoundary(5),
                                categoryBoundary(7),
                            )
                    }
                    data =
                        listOf(
                            datedValue("2019-10-10", 200),
                            datedValue("2019-10-11", 560),
                            datedValue("2019-10-12", 750),
                            datedValue("2019-10-13", 580),
                            datedValue("2019-10-14", 250),
                            datedValue("2019-10-15", 300),
                            datedValue("2019-10-16", 450),
                            datedValue("2019-10-17", 300),
                            datedValue("2019-10-18", 100),
                        )
                }
            }
        }
