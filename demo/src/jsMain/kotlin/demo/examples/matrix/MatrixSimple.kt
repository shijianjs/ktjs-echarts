package demo.examples.matrix

import demo.CatalogEntry
import demo.Groups
import echarts.VisualMapTypes
import echarts.options.EChartsOption
import echarts.options.LabelOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.VisualMapOption
import echarts.options.heatmapSeries
import echarts.options.one
import js.objects.unsafeJso
import demo.examples.Category

private val matrixSimpleCategory = Category(Groups.MATRIX, "examples/matrix/MatrixSimple.kt")

val matrixSimpleEntry: CatalogEntry =
    matrixSimpleCategory.chart("matrix-simple", "简单的矩阵图", "Simple Matrix") {
            val x =
                dimension {
                    data =
                        arrayOf(
                            branch(
                                "A",
                                cell("A1"),
                                cell("A2"),
                                cell("A3", cell("A31"), cell("A32")),
                            ),
                        )
                }
            unsafeJso<EChartsOption> {
                this.matrix =
                    matrixOption {
                        this.x = x
                        y = dimension { data = names("U", "V") }
                        top = StringOrDouble(150.0)
                        bottom = StringOrDouble(150.0)
                    }
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            type = VisualMapTypes.continuous
                            min = 0.0
                            max = 80.0
                            top = StringOrDouble("middle")
                            dimension = 2.0
                            calculable = true
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        heatmapSeries {
                            coordinateSystem = "matrix"
                            typedData =
                                heatmapPoints(
                                    listOf(
                                        Triple("A1", "U", 10.0),
                                        Triple("A1", "V", 20.0),
                                        Triple("A2", "U", 30.0),
                                        Triple("A2", "V", 40.0),
                                        Triple("A31", "U", 50.0),
                                        Triple("A3", "V", 60.0),
                                    ),
                                )
                            label = unsafeJso<LabelOption> { show = true }
                        },
                    )
            }
        }
