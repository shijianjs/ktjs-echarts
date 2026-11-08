package demo.examples.matrix

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.MatrixDimensionCellLooseOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.StringOrStringArray
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.TooltipValueFormatter
import io.github.shijianjs.ktjs.echarts.options.VisualOptionUnit
import io.github.shijianjs.ktjs.echarts.options.heatmapSeries
import io.github.shijianjs.ktjs.echarts.options.one
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.scatter.point
import demo.examples.shared.every
import demo.examples.shared.numeric
import demo.examples.tree.leaves

private val matrixCovarianceCategory = Category(Groups.MATRIX, "examples/matrix/MatrixCovariance.kt")

val matrixCovarianceEntry: CatalogEntry =
    matrixCovarianceCategory.chart("matrix-covariance", "协方差矩阵", "Covariance Matrix", height = "620px") {
            // The matrix header is two levels deep: five groups of five columns / rows, and the
            // 25x25 data below is addressed by the leaf cell names ("1" .. "25"). A flat header of
            // five cells leaves the heatmap without a cell for every point, which renders empty.
            fun grouped(prefix: String): Array<MatrixDimensionCellLooseOption> =
                Array(5) { group ->
                    branch(
                        "$prefix${group + 1}",
                        *Array(5) { index -> cell((group * 5 + index + 1).toString()) },
                    )
                }
            // The 25x25 grid is symmetric: the upper triangle is generated and mirrored below it.
            val random = sequence(20260604L)
            val values = HashMap<String, Double>()
            for (i in 1..25) {
                for (j in i..25) {
                    var base = if (i == j) 100.0 else 20.0
                    val iGroup = kotlin.math.ceil(i / 5.0)
                    val jGroup = kotlin.math.ceil(j / 5.0)
                    base += (3 - kotlin.math.abs(iGroup - jGroup)) * 35
                    if (i % 5 == j % 5) base += 20
                    if (random() > 0.9) base += random() * 40
                    values["${i}_${j}"] = (random() * 0.5 + 0.5) * base
                }
            }
            val points =
                (1..25).flatMap { i ->
                    (1..25).map { j ->
                        val value = if (i > j) values["${j}_${i}"]!! else values["${i}_${j}"]!!
                        Triple("$i", "$j", value)
                    }
                }
            unsafeJso<EChartsOption> {
                this.matrix =
                    matrixOption {
                        x =
                            dimension {
                                data = grouped("X")
                                show = false
                            }
                        y =
                            dimension {
                                data = grouped("Y")
                                show = false
                            }
                        width = StringOrDouble(500.0)
                        height = StringOrDouble(500.0)
                        left = StringOrDouble("center")
                    }
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            show = true
                            valueFormatter =
                                TooltipValueFormatter { value, _ -> kotlin.math.round(numeric(value)).toInt().toString() }
                        },
                    )
                visualMap =
                    valueVisualMap(15.0, 120.0) {
                        inRange =
                            unsafeJso<VisualOptionUnit> {
                                color =
                                    StringOrStringArray(
                                        arrayOf(
                                            "#313695", "#4575b4", "#74add1", "#abd9e9", "#e0f3f8",
                                            "#ffffbf", "#fee090", "#fdae61", "#f46d43", "#d73027", "#a50026",
                                        ),
                                    )
                            }
                    }
                series =
                    arrayOf<SeriesOption>(
                        heatmapSeries {
                            coordinateSystem = "matrix"
                            typedData = heatmapPoints(points)
                        },
                    )
            }
        }
