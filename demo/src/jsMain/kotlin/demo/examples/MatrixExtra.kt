package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import zrender.ZRStyleProps

/*
 * Matrix examples whose layout is built at run time: `matrix-sparkline` puts one grid (and one line
 * series) on every cell of the matrix and links them through ids, so a single `dataZoom` can drive
 * every grid at once.
 *
 * Dropped on purpose: `matrix-mini-bar-geo` needs a GeoJSON map, `matrix-mbti`, `matrix-periodic-table`
 * and `matrix-stock` carry several kilobytes of tables that are ported in a later batch.
 */

private val matrixExtra = Category(Groups.MATRIX, "examples/MatrixExtra.kt")

/** One cell of the matrix header of `matrix-sparkline`. */
private fun headerCell(value: String, size: Double? = null): MatrixDimensionCellLooseOption {
    val label = value
    val cellSize = size
    return MatrixDimensionCellLooseOption(
        unsafeJso<MatrixDimensionCellOption> {
            this.value = label
            if (cellSize != null) this.size = StringOrDouble(cellSize)
        },
    )
}

/**
 * A year of `[date, value]` pairs for one sparkline cell.
 *
 * The original walks the values with `Math.random()`; a seeded sequence keeps the page stable.
 */
private fun sparklineData(dayCount: Int, xIndex: Int, yIndex: Int): Array<LineDataItem?> {
    val random = kotlin.random.Random(20260604 + xIndex * 31 + yIndex * 7)
    val dayStart = 1746403200000.0 + xIndex * 86400000.0
    val sevenDays = 604800000.0
    var lastValue = (random.nextDouble() * 300).toInt()
    var turnCount: Int? = null
    var sign = -1
    val data =
        Array<LineDataItem?>(dayCount) { index ->
            if (turnCount == null || index >= turnCount!!) {
                turnCount = index + kotlin.math.round((dayCount / 4.0) * ((random.nextDouble() - 0.5) * 0.1)).toInt()
                sign = -sign
            }
            val deltaMagnitude = 50.0
            val delta =
                (random.nextDouble() * deltaMagnitude - deltaMagnitude / 2 + (sign * deltaMagnitude) / 3).toInt()
            lastValue = kotlin.math.max(0, lastValue + delta)
            LineDataItem(
                arrayOf<OptionDataValue?>(
                    OptionDataValue(ECharts.time.format(dayStart + index * sevenDays, "{yyyy}-{MM}-{dd}", false)),
                    OptionDataValue(lastValue.toDouble()),
                ),
            )
        }
    return data
}

val matrixExtraEntries: List<CatalogEntry> =
    listOf(
        matrixExtra.chart("matrix-sparkline", "矩阵中的微型折线图", "Mini Line Charts (Sparkline) in Matrix") {
            val days = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri")
            val rowValues =
                arrayOf(
                    "8:00\n~\n10:00",
                    "10:00\n~\n12:00",
                    "12:00\n~\n14:00",
                    "14:00\n~\n16:00",
                    "16:00\n~\n18:00",
                    "18:00\n~\n20:00",
                )
            val rows = Array(rowValues.size) { headerCell(rowValues[it], if (it == 2) 55.0 else null) }
            // The row of the paused time window turns into a single merged cell below.
            val breakRow = 2
            val grids = mutableListOf<GridOption>()
            val xAxes = mutableListOf<XAxisOption>()
            val yAxes = mutableListOf<YAxisOption>()
            val series = mutableListOf<SeriesOption>()
            rows.forEachIndexed { yIndex, row ->
                if (yIndex == breakRow) return@forEachIndexed
                days.forEachIndexed { xIndex, day ->
                    val id = "$xIndex|$yIndex"
                    grids += unsafeJso<GridOption> {
                        this.id = id
                        coordinateSystem = "matrix"
                        coord =
                            CoordinateSystemDataCoord(
                                arrayOf(OptionDataValue(day), OptionDataValue(rowValues[yIndex])),
                            )
                        top = StringOrDouble(10.0)
                        bottom = StringOrDouble(10.0)
                        left = StringOrDouble("center")
                        width = StringOrDouble("90%")
                        containLabel = true
                    }
                    xAxes += unsafeJso<XAxisOption> {
                        type = AxisTypes.category
                        this.id = id
                        gridId = id
                        scale = true
                        axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                        axisLabel = unsafeJso<AxisLabelOption> { show = false }
                        axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
                        splitLine = unsafeJso<SplitLineOption> { show = false }
                    }
                    yAxes += unsafeJso<YAxisOption> {
                        this.id = id
                        gridId = id
                        interval = AxisInterval(9007199254740991.0)
                        scale = true
                        axisLabel =
                            unsafeJso<AxisLabelOption> {
                                showMaxLabel = true
                                fontSize = StringOrDouble(9.0)
                            }
                        axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
                        axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                    }
                    series +=
                        lineSeries {
                            xAxisId = id
                            yAxisId = id
                            symbol = SymbolShape("none")
                            lineStyle = unsafeJso<LineStyleOption> { width = 1.0 }
                            typedData = sparklineData(365, xIndex, yIndex)
                        }
                }
            }
            unsafeJso<EChartsOption> {
                this.matrix =
                    matrixOption {
                        x =
                            dimension {
                                data = names(*days)
                                levelSize = StringOrDouble(40.0)
                                label = unsafeJso<LabelOption> { fontSize = StringOrDouble(16.0); color = "#555" }
                            }
                        y =
                            dimension {
                                data = rows
                                levelSize = StringOrDouble(70.0)
                                label = unsafeJso<LabelOption> { fontSize = StringOrDouble(14.0); color = "#777" }
                            }
                        corner =
                            unsafeJso<MatrixCornerOption> {
                                data =
                                    arrayOf(
                                        unsafeJso<MatrixBodyCornerCellOption> {
                                            coord =
                                                arrayOf(
                                                    MatrixCoordRangeOption(OptionDataValue(-1.0)),
                                                    MatrixCoordRangeOption(OptionDataValue(-1.0)),
                                                )
                                            value = "Time"
                                        },
                                    )
                                label = unsafeJso<LabelOption> { fontSize = StringOrDouble(16.0); color = "#777" }
                            }
                        body =
                            unsafeJso<MatrixBodyOption> {
                                data =
                                    arrayOf(
                                        unsafeJso<MatrixBodyCornerCellOption> {
                                            coord =
                                                arrayOf(
                                                    MatrixCoordRangeOption(arrayOf(null, OptionDataValue(breakRow.toDouble()))),
                                                )
                                            coordClamp = true
                                            mergeCells = true
                                            value = "Break"
                                            label =
                                                unsafeJso<LabelOption> {
                                                    color = "#999"
                                                    fontSize = StringOrDouble(16.0)
                                                }
                                        },
                                    )
                            }
                        top = StringOrDouble(30.0)
                        bottom = StringOrDouble(80.0)
                        width = StringOrDouble("90%")
                        left = StringOrDouble("center")
                    }
                tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.axis })
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.slider
                            xAxisIndex = IndexOrIndexArray("all")
                            left = StringOrDouble("10%")
                            right = StringOrDouble("10%")
                            bottom = StringOrDouble(30.0)
                            height = StringOrDouble(30.0)
                            throttle = 120.0
                        },
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.inside
                            xAxisIndex = IndexOrIndexArray("all")
                            throttle = 120.0
                        },
                    )
                grid = grids.toTypedArray()
                this.xAxis = xAxes.toTypedArray()
                this.yAxis = yAxes.toTypedArray()
                this.series = series.toTypedArray()
            }
        },
    )
