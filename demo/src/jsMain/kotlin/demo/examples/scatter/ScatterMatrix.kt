package demo.examples.scatter

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.custom.dim
import demo.examples.gauge.center
import demo.examples.matrix.cell
import demo.examples.matrix.dimension
import demo.examples.shared.every

private val scatterMatrixCategory = Category(Groups.SCATTER, "examples/scatter/ScatterMatrix.kt")

val scatterMatrixEntry: CatalogEntry =
    scatterMatrixCategory.chart(
            "scatter-matrix",
            "散点矩阵和平行坐标",
            "Scatter Matrix",
            "同一张空气质量表的两副面孔：上方是六维两两配对的散点矩阵，下方是它的平行坐标视图；" +
                "piecewise visualMap 按城市配色，brush 把两个视图联动起来。",
            "Two views of one air-quality table: a six-dimensional pairwise scatter matrix on top and " +
                "the same rows as parallel coordinates below; a piecewise visual map colours the " +
                "points by city and the brush links both views.",
        ) {
            val cells = matrixCells()
            val gridCount = cells.grids.size
            unsafeJso<EChartsOption> {
                animation = false
                brush =
                    one(
                        unsafeJso<BrushOption> {
                            // The upstream page links every cell, so a brushed band filters all of them.
                            brushLink = ModelFinderIndexQuery("all")
                            xAxisIndex = ModelFinderIndexQuery(allIndices(gridCount))
                            yAxisIndex = ModelFinderIndexQuery(allIndices(gridCount))
                            inBrush = unsafeJso<VisualOptionUnit> { opacity = DoubleOrDoubleArray(1.0) }
                        },
                    )
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            type = VisualMapTypes.piecewise
                            categories = AQI_CATEGORIES
                            dimension = MATRIX_DIMS.toDouble()
                            orient = Orients.horizontal
                            top = StringOrDouble(0.0)
                            left = StringOrDouble("center")
                            inRange =
                                unsafeJso<VisualOptionUnit> {
                                    color =
                                        StringOrStringArray(arrayOf("#51689b", "#ce5c5c", "#fbc357"))
                                }
                            outOfRange = unsafeJso<VisualOptionUnit> { color = StringOrStringArray("#ddd") }
                            seriesIndex = IndexOrIndexArray(allIndices(gridCount, 1))
                        },
                    )
                tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.item })
                parallelAxis =
                    Array(MATRIX_DIMS_AXES.size) { dim -> parallelAxisOf(dim, MATRIX_DIMS_AXES[dim]) } +
                        arrayOf(
                            parallelAxisOf(MATRIX_DIMS, "等级") {
                                type = AxisTypes.category
                                data = strings(*MATRIX_GRADES)
                            },
                        )
                parallel =
                    one(
                        unsafeJso<ParallelOption> {
                            bottom = StringOrDouble("5%")
                            left = StringOrDouble("2%")
                            height = StringOrDouble("30%")
                            width = StringOrDouble("55%")
                            parallelAxisDefault = parallelDefaults()
                        },
                    )
                xAxis = cells.xAxes
                yAxis = cells.yAxes
                grid = cells.grids
                series =
                    arrayOf<SeriesOption>(
                        parallelSeries {
                            name = "parallel"
                            smooth = BooleanOrDouble(true)
                            lineStyle = unsafeJso<LineStyleOption> { width = 1.0; opacity = 0.3 }
                            typedData = parallelRows()
                        },
                    ) + cells.series
            }
        }
