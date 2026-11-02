package demo.examples.matrix

import demo.CatalogEntry
import demo.Groups
import echarts.options.EChartsOption
import echarts.options.EmphasisOption
import echarts.options.LabelOption
import echarts.options.LegendOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.StringOrDoubleOrArray
import echarts.options.TooltipOption
import echarts.options.dataItems
import echarts.options.one
import echarts.options.pieSeries
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.chord.column
import demo.examples.gauge.center
import demo.examples.shared.every
import demo.examples.shared.pieItem
import demo.examples.sunburst.radius

private val matrixPieCategory = Category(Groups.MATRIX, "examples/matrix/MatrixPie.kt")

val matrixPieEntry: CatalogEntry =
    matrixPieCategory.chart("matrix-pie", "矩阵布局下的饼图", "Pie Charts in Matrix") {
            // 9 grades (two schools) by 6 classes; every cell holds the male/female split of a class.
            val random = sequence(20260601L)
            val pies =
                (0 until 9).flatMap { column ->
                    (0 until 6).map { rowIndex ->
                        pieSeries {
                            coordinateSystem = "matrix"
                            center =
                                arrayOf(
                                    StringOrDouble("Grade ${column + 1}"),
                                    StringOrDouble("Class ${rowIndex + 1}"),
                                )
                            radius = StringOrDoubleOrArray(18.0)
                            typedData =
                                dataItems(
                                    pieItem("Male", kotlin.math.round(random() * 10) + 10),
                                    pieItem("Female", kotlin.math.round(random() * 10) + 10),
                                )
                            label = unsafeJso<LabelOption> { show = false }
                            emphasis = unsafeJso<EmphasisOption> { label = unsafeJso { show = false } }
                        }
                    }
                }
            unsafeJso<EChartsOption> {
                legend = one(unsafeJso<LegendOption> { show = true; bottom = StringOrDouble(40.0) })
                this.matrix =
                    matrixOption {
                        x =
                            dimension {
                                data =
                                    arrayOf(
                                        branch("Primary School", *(1..5).map { cell("Grade $it") }.toTypedArray()),
                                        branch("High School", *(6..9).map { cell("Grade $it") }.toTypedArray()),
                                    )
                            }
                        y = dimension { data = names(*(1..6).map { "Class $it" }.toTypedArray()) }
                        top = StringOrDouble(80.0)
                        bottom = StringOrDouble(80.0)
                    }
                series = pies.toTypedArray<SeriesOption>()
                tooltip = one(unsafeJso<TooltipOption> { show = true })
            }
        }
