package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.matrix.dimension
import demo.examples.shared.row

private val areaPiecesCategory = Category(Groups.LINE, "examples/line/AreaPieces.kt")

val areaPiecesEntry: CatalogEntry =
    areaPiecesCategory.chart("area-pieces", "折线图区域高亮", "Area Pieces") {
            unsafeJso<EChartsOption> {
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            boundaryGap = BooleanOrStringArray(false)
                            data =
                                strings(
                                    "2019-10-10", "2019-10-11", "2019-10-12", "2019-10-13", "2019-10-14",
                                    "2019-10-15", "2019-10-16", "2019-10-17", "2019-10-18",
                                )
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            boundaryGap = BooleanOrStringArray(arrayOf(StringOrDouble(0.0), StringOrDouble("30%")))
                        },
                    )
                // The visual map shades the area between the second and third and between the sixth
                // and seventh category; the mark lines draw the borders of those bands.
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            type = VisualMapTypes.piecewise
                            show = false
                            dimension = 0.0
                            seriesIndex = IndexOrIndexArray(0.0)
                            pieces = arrayOf(shadedBetween(1.0, 3.0), shadedBetween(5.0, 7.0))
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        lineSeries {
                            smooth = BooleanOrDouble(0.6)
                            symbol = SymbolShape("none")
                            lineStyle =
                                unsafeJso<LineStyleOption> {
                                    color = ZRColor("#5470C6")
                                    width = 5.0
                                }
                            markLine =
                                unsafeJso<MarkLineOption> {
                                    symbol = StringOrStringArray(arrayOf("none", "none"))
                                    label = unsafeJso<MarkLineLabelOption> { show = false }
                                    data = arrayOf(
                                        categoryBoundary(1.0),
                                        categoryBoundary(3.0),
                                        categoryBoundary(5.0),
                                        categoryBoundary(7.0),
                                    )
                                }
                            areaStyle = unsafeJso<AreaStyleOption> {}
                            typedData =
                                arrayOf(
                                    LineDataItem(row("2019-10-10", 200.0)),
                                    LineDataItem(row("2019-10-11", 560.0)),
                                    LineDataItem(row("2019-10-12", 750.0)),
                                    LineDataItem(row("2019-10-13", 580.0)),
                                    LineDataItem(row("2019-10-14", 250.0)),
                                    LineDataItem(row("2019-10-15", 300.0)),
                                    LineDataItem(row("2019-10-16", 450.0)),
                                    LineDataItem(row("2019-10-17", 300.0)),
                                    LineDataItem(row("2019-10-18", 100.0)),
                                )
                        },
                    )
            }
        }
