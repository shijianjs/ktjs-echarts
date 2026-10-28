package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import js.objects.unsafeJso as jso
import zrender.ZRColor

/*
 * `scatter-matrix` of the official gallery: a scatter-matrix heat map of the AQI table, six
 * dimensions of which six pairwise cells are drawn, next to a parallel coordinate view of the same
 * rows.
 *
 * Everything is generated from [AQI_ROWS] the way the original walks its table: the upper triangle
 * of the 6x6 grid, one scatter series per cell, and the piecewise visual map that colours every point
 * by its city. The upstream `brush.brushLink` and the piecewise `visualMap` are the reason the page
 * is worth porting at all — they are the two features no other scatter page here exercises.
 */

private val scatterMatrix = Category(Groups.SCATTER, "examples/ScatterMatrix.kt")

/** Number of numeric dimensions of [AQI_ROWS]; the seventh column is the city. */
private const val MATRIX_DIMS = 6

/** Cell gap of the matrix grid, in percent of the chart. */
private const val MATRIX_GAP = 2.0

/** Left margin of the matrix grid, in percent. */
private const val MATRIX_BASE_LEFT = 5.0

/** Top margin of the matrix grid, in percent. */
private const val MATRIX_BASE_TOP = 10.0

/** Width of one cell, in percent of the chart. */
private val MATRIX_GRID_WIDTH = "${(100.0 - MATRIX_BASE_LEFT - MATRIX_GAP) / MATRIX_DIMS - MATRIX_GAP}%"

/** Height of one cell, in percent of the chart. */
private val MATRIX_GRID_HEIGHT = "${(100.0 - MATRIX_BASE_TOP - MATRIX_GAP) / MATRIX_DIMS - MATRIX_GAP}%"

/** Left offset of the cell of [column], in percent. */
private fun cellLeft(column: Int): String =
    "${MATRIX_BASE_LEFT + column * ((100.0 - MATRIX_BASE_LEFT - MATRIX_GAP) / MATRIX_DIMS)}%"

/** Top offset of the cell of [row], in percent. */
private fun cellTop(row: Int): String =
    "${MATRIX_BASE_TOP + row * ((100.0 - MATRIX_BASE_TOP - MATRIX_GAP) / MATRIX_DIMS)}%"

/** Symbol size of the points in every cell. */
private const val MATRIX_SYMBOL_SIZE = 4.0

/** One row of the AQI table: the city it belongs to, its six numbers and its grade. */
private class AqiRow(val city: String, val grades: DoubleArray, val grade: String)

/** The names of the six numeric dimensions, in the order of [AQI_ROWS]. */
private val MATRIX_DIMS_AXES = arrayOf("AQI", "PM 2.5", "PM 10", "CO", "NO₂", "SO₂")

/** The three cities, in the order of the piecewise visual map categories. */
private val MATRIX_CITIES = arrayOf("北京", "上海", "广州")

/** The grades of the parallel coordinate axis of the page. */
private val MATRIX_GRADES = arrayOf("优", "良", "轻度", "中度", "重度", "严重")

/**
 * The AQI table of the original: `[AQI, PM2.5, PM10, CO, NO2, SO2]` per day, with the grade and
 * the city it was measured in.
 */
private val AQI_ROWS: Array<AqiRow> =
    arrayOf(
        AqiRow("北京", doubleArrayOf(55.0, 9.0, 56.0, 0.46, 18.0, 6.0), "良"),
        AqiRow("北京", doubleArrayOf(25.0, 11.0, 21.0, 0.65, 34.0, 9.0), "优"),
        AqiRow("北京", doubleArrayOf(56.0, 7.0, 63.0, 0.3, 14.0, 5.0), "良"),
        AqiRow("北京", doubleArrayOf(33.0, 7.0, 29.0, 0.33, 16.0, 6.0), "优"),
        AqiRow("北京", doubleArrayOf(42.0, 24.0, 44.0, 0.76, 40.0, 16.0), "优"),
        AqiRow("北京", doubleArrayOf(82.0, 58.0, 90.0, 1.77, 68.0, 33.0), "良"),
        AqiRow("北京", doubleArrayOf(74.0, 49.0, 77.0, 1.46, 48.0, 27.0), "良"),
        AqiRow("北京", doubleArrayOf(78.0, 55.0, 80.0, 1.29, 59.0, 29.0), "良"),
        AqiRow("北京", doubleArrayOf(267.0, 216.0, 280.0, 4.8, 108.0, 64.0), "重度"),
        AqiRow("北京", doubleArrayOf(185.0, 127.0, 216.0, 2.52, 61.0, 27.0), "中度"),
        AqiRow("北京", doubleArrayOf(39.0, 19.0, 38.0, 0.57, 31.0, 15.0), "优"),
        AqiRow("北京", doubleArrayOf(41.0, 11.0, 40.0, 0.43, 21.0, 7.0), "优"),
        AqiRow("北京", doubleArrayOf(64.0, 38.0, 74.0, 1.04, 46.0, 22.0), "良"),
        AqiRow("北京", doubleArrayOf(108.0, 79.0, 120.0, 1.7, 75.0, 41.0), "轻度"),
        AqiRow("北京", doubleArrayOf(108.0, 63.0, 116.0, 1.48, 44.0, 26.0), "轻度"),
        AqiRow("北京", doubleArrayOf(33.0, 6.0, 29.0, 0.34, 13.0, 5.0), "优"),
        AqiRow("北京", doubleArrayOf(94.0, 66.0, 110.0, 1.54, 62.0, 31.0), "良"),
        AqiRow("北京", doubleArrayOf(186.0, 142.0, 192.0, 3.88, 93.0, 79.0), "中度"),
        AqiRow("北京", doubleArrayOf(57.0, 31.0, 54.0, 0.96, 32.0, 14.0), "良"),
        AqiRow("北京", doubleArrayOf(22.0, 8.0, 17.0, 0.48, 23.0, 10.0), "优"),
        AqiRow("北京", doubleArrayOf(39.0, 15.0, 36.0, 0.61, 29.0, 13.0), "优"),
        AqiRow("北京", doubleArrayOf(94.0, 69.0, 114.0, 2.08, 73.0, 39.0), "良"),
        AqiRow("北京", doubleArrayOf(99.0, 73.0, 110.0, 2.43, 76.0, 48.0), "良"),
        AqiRow("北京", doubleArrayOf(31.0, 12.0, 30.0, 0.5, 32.0, 16.0), "优"),
        AqiRow("北京", doubleArrayOf(42.0, 27.0, 43.0, 1.0, 53.0, 22.0), "优"),
        AqiRow("北京", doubleArrayOf(154.0, 117.0, 157.0, 3.05, 92.0, 58.0), "中度"),
        AqiRow("北京", doubleArrayOf(234.0, 185.0, 230.0, 4.09, 123.0, 69.0), "重度"),
        AqiRow("北京", doubleArrayOf(160.0, 120.0, 186.0, 2.77, 91.0, 50.0), "中度"),
        AqiRow("北京", doubleArrayOf(134.0, 96.0, 165.0, 2.76, 83.0, 41.0), "轻度"),
        AqiRow("北京", doubleArrayOf(52.0, 24.0, 60.0, 1.03, 50.0, 21.0), "良"),
        AqiRow("北京", doubleArrayOf(46.0, 5.0, 49.0, 0.28, 10.0, 6.0), "优"),
        AqiRow("广州", doubleArrayOf(26.0, 37.0, 27.0, 1.163, 27.0, 13.0), "优"),
        AqiRow("广州", doubleArrayOf(85.0, 62.0, 71.0, 1.195, 60.0, 8.0), "良"),
        AqiRow("广州", doubleArrayOf(78.0, 38.0, 74.0, 1.363, 37.0, 7.0), "良"),
        AqiRow("广州", doubleArrayOf(21.0, 21.0, 36.0, 0.634, 40.0, 9.0), "优"),
        AqiRow("广州", doubleArrayOf(41.0, 42.0, 46.0, 0.915, 81.0, 13.0), "优"),
        AqiRow("广州", doubleArrayOf(56.0, 52.0, 69.0, 1.067, 92.0, 16.0), "良"),
        AqiRow("广州", doubleArrayOf(64.0, 30.0, 28.0, 0.924, 51.0, 2.0), "良"),
        AqiRow("广州", doubleArrayOf(55.0, 48.0, 74.0, 1.236, 75.0, 26.0), "良"),
        AqiRow("广州", doubleArrayOf(76.0, 85.0, 113.0, 1.237, 114.0, 27.0), "良"),
        AqiRow("广州", doubleArrayOf(91.0, 81.0, 104.0, 1.041, 56.0, 40.0), "良"),
        AqiRow("广州", doubleArrayOf(84.0, 39.0, 60.0, 0.964, 25.0, 11.0), "良"),
        AqiRow("广州", doubleArrayOf(64.0, 51.0, 101.0, 0.862, 58.0, 23.0), "良"),
        AqiRow("广州", doubleArrayOf(70.0, 69.0, 120.0, 1.198, 65.0, 36.0), "良"),
        AqiRow("广州", doubleArrayOf(77.0, 105.0, 178.0, 2.549, 64.0, 16.0), "良"),
        AqiRow("广州", doubleArrayOf(109.0, 68.0, 87.0, 0.996, 74.0, 29.0), "轻度"),
        AqiRow("广州", doubleArrayOf(73.0, 68.0, 97.0, 0.905, 51.0, 34.0), "良"),
        AqiRow("广州", doubleArrayOf(54.0, 27.0, 47.0, 0.592, 53.0, 12.0), "良"),
        AqiRow("广州", doubleArrayOf(51.0, 61.0, 97.0, 0.811, 65.0, 19.0), "良"),
        AqiRow("广州", doubleArrayOf(91.0, 71.0, 121.0, 1.374, 43.0, 18.0), "良"),
        AqiRow("广州", doubleArrayOf(73.0, 102.0, 182.0, 2.787, 44.0, 19.0), "良"),
        AqiRow("广州", doubleArrayOf(73.0, 50.0, 76.0, 0.717, 31.0, 20.0), "良"),
        AqiRow("广州", doubleArrayOf(84.0, 94.0, 140.0, 2.238, 68.0, 18.0), "良"),
        AqiRow("广州", doubleArrayOf(93.0, 77.0, 104.0, 1.165, 53.0, 7.0), "良"),
        AqiRow("广州", doubleArrayOf(99.0, 130.0, 227.0, 3.97, 55.0, 15.0), "良"),
        AqiRow("广州", doubleArrayOf(146.0, 84.0, 139.0, 1.094, 40.0, 17.0), "轻度"),
        AqiRow("广州", doubleArrayOf(113.0, 108.0, 137.0, 1.481, 48.0, 15.0), "轻度"),
        AqiRow("广州", doubleArrayOf(81.0, 48.0, 62.0, 1.619, 26.0, 3.0), "良"),
        AqiRow("广州", doubleArrayOf(56.0, 48.0, 68.0, 1.336, 37.0, 9.0), "良"),
        AqiRow("广州", doubleArrayOf(82.0, 92.0, 174.0, 3.29, 0.0, 13.0), "良"),
        AqiRow("广州", doubleArrayOf(106.0, 116.0, 188.0, 3.628, 101.0, 16.0), "轻度"),
        AqiRow("广州", doubleArrayOf(118.0, 50.0, 0.0, 1.383, 76.0, 11.0), "轻度"),
        AqiRow("上海", doubleArrayOf(91.0, 45.0, 125.0, 0.82, 34.0, 23.0), "良"),
        AqiRow("上海", doubleArrayOf(65.0, 27.0, 78.0, 0.86, 45.0, 29.0), "良"),
        AqiRow("上海", doubleArrayOf(83.0, 60.0, 84.0, 1.09, 73.0, 27.0), "良"),
        AqiRow("上海", doubleArrayOf(109.0, 81.0, 121.0, 1.28, 68.0, 51.0), "轻度"),
        AqiRow("上海", doubleArrayOf(106.0, 77.0, 114.0, 1.07, 55.0, 51.0), "轻度"),
        AqiRow("上海", doubleArrayOf(109.0, 81.0, 121.0, 1.28, 68.0, 51.0), "轻度"),
        AqiRow("上海", doubleArrayOf(106.0, 77.0, 114.0, 1.07, 55.0, 51.0), "轻度"),
        AqiRow("上海", doubleArrayOf(89.0, 65.0, 78.0, 0.86, 51.0, 26.0), "良"),
        AqiRow("上海", doubleArrayOf(53.0, 33.0, 47.0, 0.64, 50.0, 17.0), "良"),
        AqiRow("上海", doubleArrayOf(80.0, 55.0, 80.0, 1.01, 75.0, 24.0), "良"),
        AqiRow("上海", doubleArrayOf(117.0, 81.0, 124.0, 1.03, 45.0, 24.0), "轻度"),
        AqiRow("上海", doubleArrayOf(99.0, 71.0, 142.0, 1.1, 62.0, 42.0), "良"),
        AqiRow("上海", doubleArrayOf(95.0, 69.0, 130.0, 1.28, 74.0, 50.0), "良"),
        AqiRow("上海", doubleArrayOf(116.0, 87.0, 131.0, 1.47, 84.0, 40.0), "轻度"),
        AqiRow("上海", doubleArrayOf(108.0, 80.0, 121.0, 1.3, 85.0, 37.0), "轻度"),
        AqiRow("上海", doubleArrayOf(134.0, 83.0, 167.0, 1.16, 57.0, 43.0), "轻度"),
        AqiRow("上海", doubleArrayOf(79.0, 43.0, 107.0, 1.05, 59.0, 37.0), "良"),
        AqiRow("上海", doubleArrayOf(71.0, 46.0, 89.0, 0.86, 64.0, 25.0), "良"),
        AqiRow("上海", doubleArrayOf(97.0, 71.0, 113.0, 1.17, 88.0, 31.0), "良"),
        AqiRow("上海", doubleArrayOf(84.0, 57.0, 91.0, 0.85, 55.0, 31.0), "良"),
        AqiRow("上海", doubleArrayOf(87.0, 63.0, 101.0, 0.9, 56.0, 41.0), "良"),
        AqiRow("上海", doubleArrayOf(104.0, 77.0, 119.0, 1.09, 73.0, 48.0), "轻度"),
        AqiRow("上海", doubleArrayOf(87.0, 62.0, 100.0, 1.0, 72.0, 28.0), "良"),
        AqiRow("上海", doubleArrayOf(168.0, 128.0, 172.0, 1.49, 97.0, 56.0), "中度"),
        AqiRow("上海", doubleArrayOf(65.0, 45.0, 51.0, 0.74, 39.0, 17.0), "良"),
        AqiRow("上海", doubleArrayOf(39.0, 24.0, 38.0, 0.61, 47.0, 17.0), "优"),
        AqiRow("上海", doubleArrayOf(39.0, 24.0, 39.0, 0.59, 50.0, 19.0), "优"),
        AqiRow("上海", doubleArrayOf(93.0, 68.0, 96.0, 1.05, 79.0, 29.0), "良"),
        AqiRow("上海", doubleArrayOf(188.0, 143.0, 197.0, 1.66, 99.0, 51.0), "中度"),
        AqiRow("上海", doubleArrayOf(174.0, 131.0, 174.0, 1.55, 108.0, 50.0), "中度"),
        AqiRow("上海", doubleArrayOf(187.0, 143.0, 201.0, 1.39, 89.0, 53.0), "中度"),
    )

/** `visualMap.categories`: the three cities as the array the visual map walks. */
private val AQI_CATEGORIES: kotlin.js.JsAny =
    Array(MATRIX_CITIES.size) { OptionDataValue(MATRIX_CITIES[it]) }.unsafeCast<kotlin.js.JsAny>()

/** `[offset, offset + 1, ...]`, the index lists of the brush and the visual map. */
private fun allIndices(count: Int, offset: Int = 0): Array<Double> = Array(count) { (it + offset).toDouble() }

/** The name of the city of [row], the third value of a cell tuple. */
private fun cityOf(row: AqiRow): String = row.city

/**
 * One cell of the matrix: every row projected onto the dimensions [dimX] and [dimY].
 *
 * Upstream parks the city on index 7 of the two-element tuple so that the piecewise visual map can
 * colour the points by category ([MATRIX_DIMS] is the dimension of that value); the wrapper types a
 * scatter item as a tuple of values, so the city name goes into the same slot through [row].
 */
private fun cellData(dimX: Int, dimY: Int): Array<ScatterDataItem?> =
    Array(AQI_ROWS.size) { index ->
        val row = AQI_ROWS[index]
        ScatterDataItem(row(row.grades[dimX], row.grades[dimY], cityOf(row)))
    }

/** The generated grids, axes and series of the upper triangle of the matrix. */
private class MatrixCells(val grids: Array<GridOption>, val xAxes: Array<XAxisOption>, val yAxes: Array<YAxisOption>, val series: Array<SeriesOption>)

/** Builds the upper triangle of the matrix the way the original does, skipping the lower half. */
private fun matrixCells(): MatrixCells {
    val grids = mutableListOf<GridOption>()
    val xAxes = mutableListOf<XAxisOption>()
    val yAxes = mutableListOf<YAxisOption>()
    val series = mutableListOf<SeriesOption>()
    var index = 0
    for (i in 0 until MATRIX_DIMS) {
        for (j in 0 until MATRIX_DIMS) {
            if (MATRIX_DIMS - i + j >= MATRIX_DIMS) continue
            grids +=
                unsafeJso<GridOption> {
                    left = StringOrDouble(cellLeft(i))
                    top = StringOrDouble(cellTop(j))
                    width = StringOrDouble(MATRIX_GRID_WIDTH)
                    height = StringOrDouble(MATRIX_GRID_HEIGHT)
                }
            xAxes +=
                unsafeJso<XAxisOption> {
                    splitNumber = 3.0
                    position = AxisPositions.top
                    axisLine =
                        unsafeJso<AxisLineOption> {
                            show = BooleanOrString(j == 0)
                            onZero = BooleanOrString(false)
                        }
                    axisTick =
                        unsafeJso<AxisTickOption> {
                            show = BooleanOrString(j == 0)
                            inside = true
                        }
                    axisLabel = unsafeJso<AxisLabelOption> { show = j == 0 }
                    type = AxisTypes.value
                    gridIndex = index.toDouble()
                    scale = true
                }
            yAxes +=
                unsafeJso<YAxisOption> {
                    splitNumber = 3.0
                    position = AxisPositions.right
                    axisLine =
                        unsafeJso<AxisLineOption> {
                            show = BooleanOrString(i == MATRIX_DIMS - 1)
                            onZero = BooleanOrString(false)
                        }
                    axisTick =
                        unsafeJso<AxisTickOption> {
                            show = BooleanOrString(i == MATRIX_DIMS - 1)
                            inside = true
                        }
                    axisLabel = unsafeJso<AxisLabelOption> { show = i == MATRIX_DIMS - 1 }
                    type = AxisTypes.value
                    gridIndex = index.toDouble()
                    scale = true
                }
            series +=
                scatterSeries {
                    symbolSize = SymbolSize(MATRIX_SYMBOL_SIZE)
                    xAxisIndex = index.toDouble()
                    yAxisIndex = index.toDouble()
                    typedData = cellData(i, j)
                }
            index++
        }
    }
    return MatrixCells(grids.toTypedArray(), xAxes.toTypedArray(), yAxes.toTypedArray(), series.toTypedArray())
}

/** One axis of the parallel coordinate block. */
private fun parallelAxisOf(dim: Int, name: String, block: ParallelAxisOption.() -> Unit = {}): ParallelAxisOption =
    unsafeJso<ParallelAxisOption> {
        this.dim = IndexOrIndexArray(dim.toDouble())
        this.name = name
        block()
    }

/** The shared defaults of the parallel coordinate block. */
private fun parallelDefaults(): ParallelAxisOption =
    unsafeJso<ParallelAxisOption> {
        type = AxisTypes.value
        name = "AQI指数"
        nameLocation = AxisNameLocations.end
        nameGap = 20.0
        splitNumber = 3.0
        nameTextStyle = unsafeJso<LabelOption> { fontSize = StringOrDouble(14.0) }
        axisLine = unsafeJso<AxisLineOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("#555") } }
        axisTick = unsafeJso<AxisTickOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("#555") } }
        splitLine = unsafeJso<SplitLineOption> { show = false }
        axisLabel = unsafeJso<AxisLabelOption> { color = "#555" }
    }

/** The rows of the parallel block: the six numbers plus the grade as the seventh value. */
private fun parallelRows(): Array<ParallelSeriesDataItem?> =
    dataItems(
        *Array(AQI_ROWS.size) { index ->
            val numbers = AQI_ROWS[index].grades
            ParallelSeriesDataItem(
                row(numbers[0], numbers[1], numbers[2], numbers[3], numbers[4], numbers[5], AQI_ROWS[index].grade),
            )
        },
    )

val scatterMatrixEntries: List<CatalogEntry> =
    listOf(
        scatterMatrix.chart(
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
        },
    )
