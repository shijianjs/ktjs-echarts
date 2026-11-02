package demo.examples.matrix

import demo.CatalogEntry
import demo.Groups
import echarts.options.EChartsOption
import echarts.options.LabelOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.heatmapSeries
import js.objects.unsafeJso
import demo.examples.Category

private val matrixCorrelationHeatmapCategory = Category(Groups.MATRIX, "examples/matrix/MatrixCorrelationHeatmap.kt")

val matrixCorrelationHeatmapEntry: CatalogEntry =
    matrixCorrelationHeatmapCategory.chart("matrix-correlation-heatmap", "相关矩阵（热力图）", "Correlation Matrix (Heatmap)") {
            val random = sequence(20260602L)
            val points =
                (1..8).flatMap { x ->
                    (1..8).filter { y -> x >= y }.map { y ->
                        Triple("X$x", "Y$y", if (x == y) 1.0 else random() * 2 - 1)
                    }
                }
            unsafeJso<EChartsOption> {
                this.matrix =
                    matrixOption {
                        x = dimension { data = names(*(1..8).map { "X$it" }.toTypedArray()) }
                        y = dimension { data = names(*(1..8).map { "Y$it" }.toTypedArray()) }
                        top = StringOrDouble(80.0)
                    }
                visualMap = valueVisualMap(-1.0, 1.0)
                series =
                    arrayOf<SeriesOption>(
                        heatmapSeries {
                            coordinateSystem = "matrix"
                            typedData = heatmapPoints(points)
                            label = unsafeJso<LabelOption> { show = true; formatter = twoDecimals() }
                        },
                    )
            }
        }
