package demo.examples.matrix

import demo.CatalogEntry
import demo.Groups
import echarts.options.DoubleOrDoubleArray
import echarts.options.EChartsOption
import echarts.options.ItemStyleOption
import echarts.options.LabelOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.StringOrStringArray
import echarts.options.VisualOptionUnit
import echarts.options.scatterSeries
import js.objects.unsafeJso
import demo.examples.Category

private val matrixCorrelationScatterCategory = Category(Groups.MATRIX, "examples/matrix/MatrixCorrelationScatter.kt")

val matrixCorrelationScatterEntry: CatalogEntry =
    matrixCorrelationScatterCategory.chart("matrix-correlation-scatter", "相关矩阵（散点图）", "Correlation Matrix (Scatter)") {
            val random = sequence(20260603L)
            val points =
                (1..10).flatMap { x ->
                    (1..6).map { y -> Triple("X$x", "Y$y", random() * 2 - 1) }
                }
            unsafeJso<EChartsOption> {
                this.matrix =
                    matrixOption {
                        x = dimension { data = names(*(1..10).map { "X$it" }.toTypedArray()) }
                        y = dimension { data = names(*(1..6).map { "Y$it" }.toTypedArray()) }
                        top = StringOrDouble(80.0)
                    }
                visualMap =
                    valueVisualMap(-1.0, 1.0) {
                        inRange =
                            unsafeJso<VisualOptionUnit> {
                                color =
                                    StringOrStringArray(
                                        arrayOf(
                                            "#313695", "#4575b4", "#74add1", "#abd9e9", "#e0f3f8",
                                            "#ffffbf", "#fee090", "#fdae61", "#f46d43", "#d73027", "#a50026",
                                        ),
                                    )
                                symbolSize = DoubleOrDoubleArray(arrayOf(15.0, 40.0))
                            }
                    }
                series =
                    arrayOf<SeriesOption>(
                        scatterSeries {
                            coordinateSystem = "matrix"
                            typedData = scatterPoints(points)
                            itemStyle = unsafeJso<ItemStyleOption> { opacity = 1.0 }
                            label = unsafeJso<LabelOption> { show = true; formatter = twoDecimals() }
                        },
                    )
            }
        }
