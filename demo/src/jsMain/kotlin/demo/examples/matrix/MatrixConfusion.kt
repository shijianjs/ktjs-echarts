package demo.examples.matrix

import demo.CatalogEntry
import demo.Groups
import echarts.options.CoordinateSystemDataCoord
import echarts.options.CustomPathOption
import echarts.options.CustomSeriesOption
import echarts.options.DimensionLoose
import echarts.options.DoubleOrDoubleArray
import echarts.options.EChartsOption
import echarts.options.GraphicComponentLooseOption
import echarts.options.GraphicComponentOption
import echarts.options.LabelFormatter
import echarts.options.LabelOption
import echarts.options.OptionDataItem
import echarts.options.TextStyleOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import web.cssom.TextAlign
import zrender.ZRColor
import zrender.ZRShapeProps
import zrender.ZRStyleProps
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.shared.numberAt
import demo.examples.shared.recordOf
import demo.examples.shared.row
import demo.examples.shared.textAt
import demo.examples.shared.textOf

private val matrixConfusionCategory = Category(Groups.MATRIX, "examples/matrix/MatrixConfusion.kt")

val matrixConfusionEntry: CatalogEntry =
    matrixConfusionCategory.chart("matrix-confusion", "混淆矩阵", "Confusion Matrix") {
            val points =
                listOf(
                    Triple("Positive", "Positive", 10.0),
                    Triple("Positive", "Negative", 2.0),
                    Triple("Negative", "Positive", 3.0),
                    Triple("Negative", "Negative", 5.0),
                )
            val custom =
                unsafeJso<CustomSeriesOption> {
                    // Without the type ECharts drops the series and the cells stay empty.
                    type = echarts.SeriesTypes.custom
                    coordinateSystem = "matrix"
                    clip = false
                    typedData =
                        Array<OptionDataItem?>(points.size) { index ->
                            val (x, y, value) = points[index]
                            OptionDataItem(row(x, y, value))
                        }
                    label =
                        unsafeJso<LabelOption> {
                            show = true
                            formatter =
                                LabelFormatter { params ->
                                    val x = textAt(params.value, 0)
                                    val y = textAt(params.value, 1)
                                    val prefix = if (x == y) "True " else "False "
                                    "{name|$prefix$y}\n{value|${numberAt(params.value, 2).toInt()}}"
                                }
                            rich =
                                recordOf(
                                    "name" to
                                        unsafeJso<TextStyleOption> {
                                            color = "#fff"
                                            backgroundColor = ZRColor("#999")
                                            textBorderColor = "#333"
                                            padding = DoubleOrDoubleArray(5.0)
                                            fontSize = StringOrDouble(18.0)
                                        },
                                    "value" to
                                        unsafeJso<TextStyleOption> {
                                            color = "#444"
                                            textBorderWidth = 0.0
                                            padding = DoubleOrDoubleArray(5.0)
                                            fontSize = StringOrDouble(16.0)
                                            align = TextAlign.center
                                        },
                                ).unsafeCast<Record<String, TextStyleOption>>()
                        }
                    renderItem = { _, api ->
                        val x = api.value(DimensionLoose(0.0))
                        val y = api.value(DimensionLoose(1.0))
                        val rect = api.layout(CoordinateSystemDataCoord(arrayOf(x, y))).rect
                        unsafeJso<CustomPathOption> {
                            type = "rect"
                            shape =
                                unsafeJso<ZRShapeProps> {
                                    this.x = rect?.x
                                    this.y = rect?.y
                                    width = rect?.width
                                    height = rect?.height
                                }
                            style =
                                api.style(
                                    unsafeJso<ZRStyleProps> {
                                        fill = ZRColor(if (textOf(x) == textOf(y)) "#8f8" else "#f88")
                                    },
                                )
                        }
                    }
                }
            unsafeJso<EChartsOption> {
                this.matrix =
                    matrixOption {
                        x =
                            dimension {
                                data = names("Positive", "Negative")
                                label = unsafeJso<LabelOption> { fontSize = StringOrDouble(16.0); color = "#555" }
                            }
                        y =
                            dimension {
                                data = names("Positive", "Negative")
                                label = unsafeJso<LabelOption> { fontSize = StringOrDouble(16.0); color = "#555" }
                            }
                        top = StringOrDouble(80.0)
                        width = StringOrDouble(600.0)
                        left = StringOrDouble("center")
                    }
                series = arrayOf<SeriesOption>(custom)
                graphic =
                    arrayOf(
                        GraphicComponentLooseOption(
                            unsafeJso<GraphicComponentOption> {
                                elements =
                                    arrayOf(
                                        textElement("True Class", StringOrDouble("70%"), StringOrDouble(20.0), 0.0),
                                        textElement(
                                            "Predicted Class",
                                            StringOrDouble("10%"),
                                            StringOrDouble("50%"),
                                            90.0,
                                        ),
                                    )
                            },
                        ),
                    )
            }
        }
