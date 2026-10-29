package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.math.PI
import kotlin.math.cos
import zrender.ZRColor
import zrender.ZRFontWeight
import zrender.ZRStyleProps

/*
 * Matrix examples whose layout is built at run time: `matrix-sparkline` puts one grid (and one line
 * series) on every cell of the matrix and links them through ids, so a single `dataZoom` can drive
 * every grid at once; `matrix-mini-bar-geo` puts a grid with two bar series or a geo component on
 * every cell.
 *
 * Dropped on purpose: `matrix-mbti`, `matrix-periodic-table` and `matrix-stock` carry several
 * kilobytes of tables that are ported in a later batch.
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

/** One row of `matrix-mini-bar-geo`: the region name and its two values. */
private data class MiniBarRow(val region: String, val values: DoubleArray)

/** One year of `matrix-mini-bar-geo`; the year is the series name of the mini bars. */
private data class MiniBarSource(val year: String, val rows: List<MiniBarRow>)

private val MINI_BAR_HEADERS = arrayOf("Region and Time", "Data A", "Data B", "Location")

private const val MINI_BAR_REGION_COL = 0
private const val MINI_BAR_GEO_COL = 3

private val MINI_BAR_COLORS =
    arrayOf("#ffd10a", "#0ca8df", "#b6d634", "#3fbe95", "#5070dd", "#ff994d", "#505372", "#fb628b", "#785db0")

private val MINI_BAR_SOURCES =
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
private fun miniGeo(column: Int, row: Int, region: String): GeoOption =
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
private fun miniBarGeoOption(): EChartsOption {
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
        matrixExtra.interactive("matrix-mini-bar-geo", "矩阵坐标系下的微型条形图和地图", "Mini Bars and Geo in Matrix") { chart ->
            chart.showLoading(null)
            loadJson("data/ch.geo.json").then { json ->
                chart.hideLoading()
                ECharts.registerMap(
                    "target_map",
                    MapInput(unsafeJso<GeoJSONMapInput> { geoJSON = json.unsafeCast<GeoJSONSourceInput>() }),
                )
                chart.setOption(miniBarGeoOption())
                null
            }
        },
    )
