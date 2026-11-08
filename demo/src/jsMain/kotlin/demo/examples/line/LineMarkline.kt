package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import kotlin.js.JsAny
import js.objects.unsafeJso
import demo.examples.Category

private val lineMarklineCategory = Category(Groups.LINE, "examples/line/LineMarkline.kt")

val lineMarklineEntry: CatalogEntry =
    lineMarklineCategory.chart("line-markline", "折线图的标记线", "Line with Marklines") {
            unsafeJso<EChartsOption> {
                animation = false
                textStyle = unsafeJso<TextStyleOption> { fontSize = StringOrDouble(14.0) }
                grid =
                    one(
                        unsafeJso<GridOption> {
                            top = StringOrDouble(30.0)
                            left = StringOrDouble(60.0)
                            right = StringOrDouble(60.0)
                            bottom = StringOrDouble(40.0)
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            data = strings("A", "B", "C", "D", "E")
                            boundaryGap = BooleanOrStringArray(true)
                            splitArea = unsafeJso<SplitAreaOption> { show = true }
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> { max = AxisMinMax(2.0) })
                series =
                    arrayOf<SeriesOption>(
                        lineSeries {
                            name = "line"
                            stack = "all"
                            symbolSize = SymbolSize(6.0)
                            typedData = points(doubleArrayOf(0.3, 1.4, 1.2, 1.0, 0.6))
                            markLine =
                                unsafeJso<MarkLineOption> {
                                    data = markLinePositions()
                                    label =
                                        unsafeJso<MarkLineLabelOption> {
                                            // `[20, 8]`: the horizontal and the vertical gap to the line.
                                            typedDistance = DoubleOrDoubleArray(arrayOf(20.0, 8.0))
                                        }
                                }
                        },
                    )
            }
        }
