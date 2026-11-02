package demo.examples.matrix

import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.math.PI
import kotlin.math.cos
import zrender.ZRColor
import zrender.ZRFontWeight
import demo.examples.calendar.cellSize
import demo.examples.chord.column
import demo.examples.custom.coord
import demo.examples.geo.region
import demo.examples.shared.axisLabel
import demo.examples.shared.row

/** One cell of the matrix header of `matrix-sparkline`. */
internal fun headerCell(value: String, size: Double? = null): MatrixDimensionCellLooseOption {
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
internal fun sparklineData(dayCount: Int, xIndex: Int, yIndex: Int): Array<LineDataItem?> {
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

/** One row of `matrix-mini-bar-geo`: the region name and its two values. */
internal data class MiniBarRow(val region: String, val values: DoubleArray)

/** One year of `matrix-mini-bar-geo`; the year is the series name of the mini bars. */
internal data class MiniBarSource(val year: String, val rows: List<MiniBarRow>)

internal val MINI_BAR_HEADERS = arrayOf("Region and Time", "Data A", "Data B", "Location")

internal const val MINI_BAR_REGION_COL = 0

internal const val MINI_BAR_GEO_COL = 3

internal val MINI_BAR_COLORS =
    arrayOf("#ffd10a", "#0ca8df", "#b6d634", "#3fbe95", "#5070dd", "#ff994d", "#505372", "#fb628b", "#785db0")

internal val MINI_BAR_SOURCES =
    listOf(
        MiniBarSource(
            "2021",
            listOf(
                MiniBarRow("Valais", doubleArrayOf(1212.0, 2321.0)),
                MiniBarRow("Ticino", doubleArrayOf(7181.0, 2114.0)),
                MiniBarRow("Graubünden", doubleArrayOf(2763.0, 4212.0)),
                MiniBarRow("Uri", doubleArrayOf(6122.0, 2942.0)),
                MiniBarRow("Lucerne", doubleArrayOf(4221.0, 3411.0)),
                MiniBarRow("Neuchâtel", doubleArrayOf(7221.0, 5121.0)),
                MiniBarRow("Jura", doubleArrayOf(5121.0, 4121.0)),
                MiniBarRow("Vaud", doubleArrayOf(6121.0, 3121.0)),
                MiniBarRow("Thurgau", doubleArrayOf(7121.0, 2121.0)),
                MiniBarRow("Schwyz", doubleArrayOf(8121.0, 1121.0)),
            ),
        ),
        MiniBarSource(
            "2020",
            listOf(
                MiniBarRow("Valais", doubleArrayOf(1010.0, 2221.0)),
                MiniBarRow("Ticino", doubleArrayOf(7040.0, 1810.0)),
                MiniBarRow("Graubünden", doubleArrayOf(2313.0, 4011.0)),
                MiniBarRow("Uri", doubleArrayOf(6011.0, 2749.0)),
                MiniBarRow("Lucerne", doubleArrayOf(3329.0, 3015.0)),
                MiniBarRow("Neuchâtel", doubleArrayOf(7116.0, 4822.0)),
                MiniBarRow("Jura", doubleArrayOf(4968.0, 3820.0)),
                MiniBarRow("Vaud", doubleArrayOf(6027.0, 2928.0)),
                MiniBarRow("Thurgau", doubleArrayOf(7011.0, 1725.0)),
                MiniBarRow("Schwyz", doubleArrayOf(7311.0, 825.0)),
            ),
        ),
    )

/** A geo component of `matrix-mini-bar-geo`: one Swiss canton, highlighted in its matrix cell. */
internal fun miniGeo(column: Int, row: Int, region: String): GeoOption =
    unsafeJso {
        id = "mini-geo-$row"
        map = "target_map"
        animation = false
        // The map is laid out for the latitude of Switzerland.
        aspectScale = cos(47 * PI / 180)
        coordinateSystem = "matrix"
        coord =
            CoordinateSystemDataCoord(
                arrayOf(OptionDataValue(column.toDouble()), OptionDataValue(row.toDouble())),
            )
        roam = BooleanOrString(false)
        selectedMode = SelectionMode(false)
        tooltip = unsafeJso<TooltipOption> { show = false }
        regions =
            arrayOf(
                unsafeJso<RegionOption> {
                    name = region
                    selected = true
                    select =
                        unsafeJso<RegionStateOption> {
                            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#0a41e6") }
                        }
                },
            )
        select = unsafeJso<SelectOption> { label = unsafeJso<LabelOption> { show = false } }
    }

/** The option of `matrix-mini-bar-geo`: two text columns, two bar columns and a map column. */
internal fun miniBarGeoOption(): EChartsOption {
    val rowCount = MINI_BAR_SOURCES[0].rows.size
    val bodyCells = mutableListOf<MatrixBodyCornerCellOption>()
    val grids = mutableListOf<GridOption>()
    val xAxes = mutableListOf<XAxisOption>()
    val yAxes = mutableListOf<YAxisOption>()
    val geos = mutableListOf<GeoOption>()
    val series = mutableListOf<SeriesOption>()

    for (column in MINI_BAR_HEADERS.indices) {
        val columnMax =
            when (column) {
                MINI_BAR_REGION_COL, MINI_BAR_GEO_COL -> null
                else -> MINI_BAR_SOURCES.flatMap { source -> source.rows.map { it.values[column - 1] } }.max()
            }
        for (row in 0 until rowCount) {
            when (column) {
                MINI_BAR_REGION_COL -> {
                    val region = MINI_BAR_SOURCES[0].rows[row].region
                    bodyCells +=
                        unsafeJso<MatrixBodyCornerCellOption> {
                            value = region
                            coord =
                                arrayOf(
                                    MatrixCoordRangeOption(OptionDataValue(column.toDouble())),
                                    MatrixCoordRangeOption(OptionDataValue(row.toDouble())),
                                )
                        }
                }
                MINI_BAR_GEO_COL -> geos += miniGeo(column, row, MINI_BAR_SOURCES[0].rows[row].region)
                else -> {
                    val id = "mini-bar-$column-$row"
                    grids +=
                        unsafeJso<GridOption> {
                            this.id = id
                            coordinateSystem = "matrix"
                            coord =
                                CoordinateSystemDataCoord(
                                    arrayOf(OptionDataValue(column.toDouble()), OptionDataValue(row.toDouble())),
                                )
                            top = StringOrDouble("15%")
                            bottom = StringOrDouble("15%")
                        }
                    xAxes +=
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.value
                            this.id = id
                            gridId = id
                            min = AxisMinMax(0.0)
                            if (columnMax != null) max = AxisMinMax(columnMax)
                            scale = false
                            axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
                            axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                            axisLabel = unsafeJso<AxisLabelOption> { show = false }
                        }
                    yAxes +=
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.category
                            this.id = id
                            gridId = id
                            boundaryGap = BooleanOrStringArray(false)
                            inverse = true
                            axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
                            axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                            axisLabel = unsafeJso<AxisLabelOption> { show = false }
                        }
                    MINI_BAR_SOURCES.forEachIndexed { index, source ->
                        series +=
                            barSeries {
                                name = source.year
                                xAxisId = id
                                yAxisId = id
                                label =
                                    unsafeJso<LabelOption> {
                                        show = true
                                        position = LabelPosition("insideLeft")
                                    }
                                barMinHeight = 2.0
                                barGap = StringOrDouble("40%")
                                barWidth = StringOrDouble("40%")
                                itemStyle =
                                    unsafeJso<ItemStyleOption> {
                                        color = ZRColor(MINI_BAR_COLORS[index % MINI_BAR_COLORS.size])
                                    }
                                encode =
                                    unsafeJso<OptionEncode> {
                                        label = OptionEncodeValue(DimensionLoose(0.0))
                                    }
                                // Both years share the empty Y value, so the two bars stay side by side.
                                typedData =
                                    dataItems(BarDataItem(row(source.rows[row].values[column - 1], "")))
                            }
                    }
                }
            }
        }
    }

    return unsafeJso {
        matrix =
            matrixOption {
                x =
                    dimension {
                        levelSize = StringOrDouble(40.0)
                        data =
                            Array(MINI_BAR_HEADERS.size) { index ->
                                val header = MINI_BAR_HEADERS[index]
                                val size =
                                    when (index) {
                                        MINI_BAR_GEO_COL -> StringOrDouble("15%")
                                        MINI_BAR_REGION_COL -> StringOrDouble(120.0)
                                        else -> null
                                    }
                                MatrixDimensionCellLooseOption(
                                    unsafeJso<MatrixDimensionCellOption> {
                                        value = header
                                        if (size != null) this.size = size
                                    },
                                )
                            }
                        itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#f0f8ff") }
                        label = unsafeJso<LabelOption> { fontWeight = ZRFontWeight(700.0) }
                    }
                y =
                    dimension {
                        data = names(*Array(rowCount) { "_" })
                        show = false
                    }
                body = unsafeJso<MatrixBodyOption> { data = bodyCells.toTypedArray() }
                top = StringOrDouble(25.0)
            }
        legend = one(unsafeJso<LegendOption> {})
        tooltip = one(unsafeJso<TooltipOption> {})
        this.grid = grids.toTypedArray()
        this.xAxis = xAxes.toTypedArray()
        this.yAxis = yAxes.toTypedArray()
        this.geo = geos.toTypedArray()
        this.series = series.toTypedArray()
    }
}
