package demo.examples.matrix

import echarts.*
import echarts.options.*
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.js
import kotlin.js.unsafeCast
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.round
import web.cssom.TextAlign
import web.cssom.VerticalAlign
import zrender.ZRColor
import zrender.ZRFontWeights
import zrender.ZRLineType
import zrender.ZRShapeProps
import zrender.ZRStyleProps
import demo.examples.chord.column
import demo.examples.custom.coord
import demo.examples.funnel.labels
import demo.examples.lines.coordinate
import demo.examples.radar.indicator
import demo.examples.scatter.point
import demo.examples.shared.Lcg
import demo.examples.shared.doubleRow
import demo.examples.shared.every
import demo.examples.shared.recordOf
import demo.examples.shared.row

/** Close of the previous day, the value every price is compared with. */
internal const val LAST_CLOSE = 50.0

/** One minute in milliseconds. */
internal const val MINUTE_MS = 60_000.0

/** Distance between the matrix border and the canvas, the upstream `matrixMargin`. */
internal const val MATRIX_MARGIN = 10.0

/** Color of an unchanged price. */
internal const val COLOR_GRAY = "#888"

/** Color of a price above [LAST_CLOSE]. */
internal const val COLOR_RED = "#eb5454"

/** Color of a price below [LAST_CLOSE]. */
internal const val COLOR_GREEN = "#47b262"

/** Fill of the area above the last price in the depth chart. */
internal const val COLOR_RED_OPACITY = "rgba(235, 84, 84, 0.2)"

/** Fill of the area below the last price in the depth chart. */
internal const val COLOR_GREEN_OPACITY = "rgba(71, 178, 98, 0.2)"

/** Levels of the order book, the upstream `orderCount`. */
internal const val ORDER_COUNT = 10

/** Levels per side of the depth chart, the upstream `depthCount`. */
internal const val DEPTH_COUNT = 20

/** Short EMA period of MACD. */
internal const val SHORT_PERIOD = 12

/** Long EMA period of MACD. */
internal const val LONG_PERIOD = 26

/** Signal EMA period of MACD. */
internal const val SIGNAL_PERIOD = 9

/** The trading day of the example, as `[year, month, day]` with a zero-based month. */
internal val STOCK_DAY = intArrayOf(2025, 9, 16)

/** 09:30 of [STOCK_DAY], the first minute of the data. */
internal val STOCK_OPEN = stockTime(9, 30)

/** 11:30 of [STOCK_DAY], where the lunch break starts. */
internal val STOCK_BREAK_START = stockTime(11, 30)

/** 13:00 of [STOCK_DAY], where the lunch break ends. */
internal val STOCK_BREAK_END = stockTime(13, 0)

/** 15:00 of [STOCK_DAY], where the data stops. */
internal val STOCK_CLOSE = stockTime(15, 0)

/** `Date.UTC(year, month, day, hour, minute)` of one wall-clock time. */
internal fun stockTime(hour: Int, minute: Int): Double =
    js("(y, mo, d, h, mi) => Date.UTC(y, mo, d, h, mi)")(STOCK_DAY[0], STOCK_DAY[1], STOCK_DAY[2], hour, minute) as Double

/** Rounds to whole cents. */
internal fun cents(value: Double): Double = round(value * 100) / 100

/** `Math.round(value * 100) / 100` written with two decimals, the upstream `priceFormatter`. */
internal fun priceFormatter(value: Double): String {
    val hundredths = round(value * 100)
    val whole = floor(hundredths / 100).toInt()
    val fraction = abs(hundredths - whole * 100).toInt()
    return "$whole.${fraction.toString().padStart(2, '0')}"
}

/** Color of [price] compared with the previous close, the upstream `getPriceColor`. */
internal fun priceColor(price: Double): String =
    when {
        price == LAST_CLOSE -> COLOR_GRAY
        price > LAST_CLOSE -> COLOR_RED
        else -> COLOR_GREEN
    }

/**
 * The minute-by-minute random walk of the trading day, its volume, the volume weighted average and
 * the numbers the rest of the page derives from it.
 */
internal class StockWalk(
    val prices: List<Pair<Double, Double>>,
    val volumes: List<Pair<Double, Double>>,
    val averages: List<Pair<Double, Double>>,
    val maxAbs: Double,
    val volumeTotal: Double,
    val lastPrice: Double,
)

/**
 * The walk of the example: a step of at most ten cents per minute, with a 70% chance of keeping the
 * direction, and a jump from 11:30 to 13:00 that the axis break later folds away.
 */
internal fun walkStock(random: Lcg): StockWalk {
    val prices = ArrayList<Pair<Double, Double>>()
    val volumes = ArrayList<Pair<Double, Double>>()
    val averages = ArrayList<Pair<Double, Double>>()
    var sumPrice = 0.0
    var sumVolume = 0.0
    var maxAbs = 0.0
    var time = STOCK_OPEN
    var price = 0.0
    var direction = 1.0
    while (time < STOCK_CLOSE) {
        val volume = random.next() * 1000 + 500
        volumes += time to volume
        sumVolume += volume
        if (time == STOCK_OPEN) {
            direction = if (random.next() < 0.5) 1.0 else -1.0
            price = LAST_CLOSE * (1 + (random.next() - 0.5) * 0.02)
        } else {
            direction = if (random.next() < 0.8) direction else -direction
            price = cents(price + direction * (random.next() * 0.1))
        }
        prices += time to price
        sumPrice += price * volume
        averages += time to sumPrice / sumVolume
        maxAbs = max(maxAbs, abs(price - LAST_CLOSE))
        time = if (time == STOCK_BREAK_START) STOCK_BREAK_END else time + MINUTE_MS
    }
    return StockWalk(prices, volumes, averages, maxAbs, sumVolume, prices.last().second)
}

/** Exponential moving average of [values]; empty when there are fewer points than [period]. */
internal fun ema(values: List<Pair<Double, Double>>, period: Int): List<Pair<Double, Double>> {
    if (values.size < period) return emptyList()
    val k = 2.0 / (period + 1)
    val out = ArrayList<Pair<Double, Double>>(values.size - period + 1)
    var sum = 0.0
    for (index in 0 until period) sum += values[index].second
    out += values[period - 1].first to sum / period
    for (index in period until values.size) {
        val previous = out.last().second
        out += values[index].first to (values[index].second * k + previous * (1 - k))
    }
    return out
}

/** The three MACD outputs, all on the timestamps where both the DIF and the signal line exist. */
internal class Macd(
    val bars: List<Pair<Double, Double>>,
    val dif: List<Pair<Double, Double>>,
    val dea: List<Pair<Double, Double>>,
)

/** MACD (12, 26, 9): DIF from the two EMAs, DEA as the EMA of DIF, histogram as their difference. */
internal fun macd(walk: StockWalk): Macd {
    val short = ema(walk.prices, SHORT_PERIOD).associate { it.first to it.second }
    val long = ema(walk.prices, LONG_PERIOD).associate { it.first to it.second }
    val dif = ArrayList<Pair<Double, Double>>()
    for (index in LONG_PERIOD - 1 until walk.prices.size) {
        val time = walk.prices[index].first
        val shortValue = short[time] ?: continue
        val longValue = long[time] ?: continue
        dif += time to (shortValue - longValue)
    }
    val dea = ema(dif, SIGNAL_PERIOD).associate { it.first to it.second }
    val difOut = ArrayList<Pair<Double, Double>>()
    val deaOut = ArrayList<Pair<Double, Double>>()
    val bars = ArrayList<Pair<Double, Double>>()
    for ((time, value) in dif) {
        val signal = dea[time] ?: continue
        difOut += time to value
        deaOut += time to signal
        bars += time to (value - signal)
    }
    return Macd(bars, difOut, deaOut)
}

/** One row of the order book: a price, its amount, and whether it is a bid. */
internal class OrderRow(val price: Double, val amount: Double, val isBid: Boolean)

/**
 * The ten levels around the last price, one cent apart, skipping the price itself.
 *
 * The original walks a floating point `orderPrice`; the index form is the same sequence but does not
 * depend on how many additions were skipped.
 */
internal fun orderBook(walk: StockWalk, random: Lcg): List<OrderRow> {
    val rows = ArrayList<OrderRow>(ORDER_COUNT)
    val start = walk.lastPrice - (0.01 * ORDER_COUNT) / 2
    for (index in 0 until ORDER_COUNT) {
        val price = start + 0.01 * (index + 1)
        if (price == walk.lastPrice) continue
        rows += OrderRow(price, round(random.next() * 200) + 10, price < walk.lastPrice)
    }
    return rows
}

/** The `rich` block of the order book labels. */
internal fun orderBookRich(): Record<String, TextStyleOption> =
    recordOf(
        "red" to unsafeJso<TextStyleOption> { color = COLOR_RED },
        "green" to unsafeJso<TextStyleOption> { color = COLOR_GREEN },
        "amount" to unsafeJso<TextStyleOption> { color = "#666" },
        "name" to
            unsafeJso<TextStyleOption> {
                fontWeight = ZRFontWeights.bold
                color = "#444"
            },
    ).unsafeCast<Record<String, TextStyleOption>>()

/** The label of one order book level: the side, the price and the amount in parentheses. */
internal fun orderBookLabel(row: OrderRow): LabelOption =
    unsafeJso {
        formatter =
            LabelFormatter(
                "{name|${if (row.isBid) "Bid" else "Ask"}} " +
                    "{${if (row.isBid) "green" else "red"}|${priceFormatter(row.price)}} " +
                    "{amount|(${row.amount.toInt()})}",
            )
        rich = orderBookRich()
    }

/** `[time, value]` rows of a line series. */
internal fun lineRows(points: List<Pair<Double, Double>>): Array<LineDataItem?> =
    Array(points.size) { LineDataItem(doubleRow(points[it].first, points[it].second)) }

/** `{ value: [time, value], itemStyle.color }` rows of a bar series. */
internal fun coloredBar(time: Double, value: Double, color: String): BarDataItem =
    BarDataItem(
        unsafeJso<BarDataItemOption> {
            typedValue =
                OptionDataValueOrArray(arrayOf(OptionDataValue(time), OptionDataValue(value)))
            itemStyle = unsafeJso<ItemStyleOption> { this.color = ZRColor(color) }
        },
    )

/** One cell of a merged matrix body range. */
internal fun mergedCell(x0: Double, x1: Double, y0: Double, y1: Double): MatrixBodyCornerCellOption =
    unsafeJso {
        coord =
            arrayOf<MatrixCoordRangeOption?>(
                MatrixCoordRangeOption(arrayOf(OptionDataValue(x0), OptionDataValue(x1))),
                MatrixCoordRangeOption(arrayOf(OptionDataValue(y0), OptionDataValue(y1))),
            )
        mergeCells = true
    }

/** One title pinned to a matrix cell, with the small captions the page uses. */
internal fun matrixTitle(text: String, subtext: String, x: Double, y: Double): TitleOption =
    unsafeJso {
        this.text = text
        this.subtext = subtext
        left = StringOrDouble(2.0)
        top = StringOrDouble(2.0)
        padding = DoubleOrDoubleArray(0.0)
        textStyle =
            unsafeJso<TextStyleOption> {
                fontSize = StringOrDouble(12.0)
                fontWeight = ZRFontWeights.bold
                color = "#444"
            }
        subtextStyle =
            unsafeJso<TextStyleOption> {
                fontSize = StringOrDouble(10.0)
                color = "#666"
            }
        itemGap = 0.0
        coordinateSystem = "matrix"
        coord = CoordinateSystemDataCoord(arrayOf(OptionDataValue(x), OptionDataValue(y)))
    }

/**
 * One horizontal or vertical separator of the matrix, drawn through the `graphic` component.
 *
 * The horizontal lines stop at the fourth column because the right-hand column belongs to the order
 * book and the depth chart; only the middle one is dashed and darker, like the original.
 */
internal fun separator(x1: Double, y1: Double, x2: Double, y2: Double, dashed: Boolean, stroke: String): GraphicElement =
    unsafeJso {
        type = "line"
        shape =
            unsafeJso<ZRShapeProps> {
                this.x1 = x1
                this.y1 = y1
                this.x2 = x2
                this.y2 = y2
            }
        style =
            unsafeJso<ZRStyleProps> {
                this.stroke = ZRColor(stroke)
                lineWidth = 1.0
                if (dashed) lineDash = ZRLineType("dashed")
            }
    }

/** The axis break that folds the lunch break away; every time axis of the page carries it. */
internal fun middayBreak(): Array<AxisBreakOption> =
    one(
        unsafeJso {
            start = OptionDataValue(STOCK_BREAK_START)
            end = OptionDataValue(STOCK_BREAK_END)
            gap = StringOrDouble(0.0)
        },
    )

/** One mark point of the price axis: a placed label with no symbol. */
internal fun priceMarkPoint(
    text: String,
    x: StringOrDouble,
    y: StringOrDouble,
    align: TextAlign,
    verticalAlign: VerticalAlign,
    color: String,
    statistic: MarkerStatisticType? = null,
): MarkPointDataItemOption =
    unsafeJso {
        name = text
        relativeTo = MarkerRelativeTos.coordinate
        this.x = x
        this.y = y
        if (statistic != null) type = statistic
        label =
            unsafeJso<LabelOption> {
                this.align = align
                this.verticalAlign = verticalAlign
                formatter = LabelFormatter(text)
                this.color = color
            }
    }

/** The six labels of the price cell: the high / low and the previous close on the left, the swing on the right. */
internal fun priceMarkPointOption(maxAbs: Double): MarkPointOption =
    unsafeJso {
        symbol = SymbolShape("circle")
        symbolSize = SymbolSize(0.0)
        data =
            arrayOf(
                priceMarkPoint(
                    priceFormatter(LAST_CLOSE + maxAbs),
                    StringOrDouble(0.0),
                    StringOrDouble(0.0),
                    TextAlign.left,
                    VerticalAlign.top,
                    priceColor(LAST_CLOSE + maxAbs),
                    MarkerStatisticTypes.max,
                ),
                priceMarkPoint(
                    priceFormatter(LAST_CLOSE),
                    StringOrDouble(0.0),
                    StringOrDouble("50%"),
                    TextAlign.left,
                    VerticalAlign.middle,
                    priceColor(LAST_CLOSE),
                ),
                priceMarkPoint(
                    priceFormatter(LAST_CLOSE - maxAbs),
                    StringOrDouble(0.0),
                    StringOrDouble("100%"),
                    TextAlign.left,
                    VerticalAlign.bottom,
                    priceColor(LAST_CLOSE - maxAbs),
                    MarkerStatisticTypes.min,
                ),
                priceMarkPoint(
                    "${priceFormatter(maxAbs / LAST_CLOSE * 100)}%",
                    StringOrDouble("100%"),
                    StringOrDouble(0.0),
                    TextAlign.right,
                    VerticalAlign.top,
                    COLOR_RED,
                ),
                priceMarkPoint(
                    "0%",
                    StringOrDouble("100%"),
                    StringOrDouble("50%"),
                    TextAlign.right,
                    VerticalAlign.middle,
                    COLOR_GRAY,
                ),
                priceMarkPoint(
                    "-${priceFormatter(maxAbs / LAST_CLOSE * 100)}%",
                    StringOrDouble("100%"),
                    StringOrDouble("100%"),
                    TextAlign.right,
                    VerticalAlign.bottom,
                    COLOR_GREEN,
                ),
            )
    }

/** One time axis of the price, volume and MACD rows, all folded at lunch. */
internal fun hiddenTimeAxis(gridIndex: Double? = null): XAxisOption =
    unsafeJso {
        type = AxisTypes.time
        show = false
        this.gridIndex = gridIndex
        breaks = middayBreak()
    }

/**
 * The option of `matrix-stock`.
 *
 * The matrix is rebuilt for the size the chart happens to have, because the separators and the
 * matrix margins are pixel values rather than percentages.
 */
internal fun stockOption(walk: StockWalk, chartWidth: Double, chartHeight: Double): EChartsOption {
    val matrixWidth = chartWidth - MATRIX_MARGIN * 2
    val matrixHeight = chartHeight - MATRIX_MARGIN * 2
    val indicator = macd(walk)
    val orders = orderBook(walk, Lcg(20251016))
    val lastPrice = walk.lastPrice
    val depthCategories: Array<OptionDataItem?> =
        strings(*(0 until DEPTH_COUNT * 2).map { it.toString() }.toTypedArray())

    val volumeCells =        Array<BarDataItem?>(walk.volumes.size) { index ->
            val color =
                when {
                    index == 0 -> COLOR_GRAY
                    walk.prices[index].second > walk.prices[index - 1].second -> COLOR_RED
                    else -> COLOR_GREEN
                }
            coloredBar(walk.volumes[index].first, walk.volumes[index].second, color)
        }
    val macdCells =
        Array<BarDataItem?>(indicator.bars.size) { index ->
            val (time, histogram) = indicator.bars[index]
            coloredBar(time, histogram, if (histogram > 0) COLOR_RED else COLOR_GREEN)
        }
    val orderCells =
        Array<BarDataItem?>(orders.size) { index ->
            val row = orders[index]
            BarDataItem(
                unsafeJso<BarDataItemOption> {
                    value = OptionDataValue(row.amount)
                    itemStyle =
                        unsafeJso<ItemStyleOption> {
                            color = ZRColor(if (row.isBid) COLOR_GREEN_OPACITY else COLOR_RED_OPACITY)
                        }
                    label = orderBookLabel(row)
                },
            )
        }

    // The depth chart is one curve mirrored around the last price: the bids pile up to its left and
    // the asks to its right, so both series share the 40 category slots and each fills half of them.
    val depthHigh = arrayOfNulls<LineDataItem?>(DEPTH_COUNT * 2)
    val depthLow = arrayOfNulls<LineDataItem?>(DEPTH_COUNT * 2)
    val depthRandom = Lcg(20251017)
    var cumulativeHigh = 0.0
    var cumulativeLow = 0.0
    for (index in 0 until DEPTH_COUNT) {
        depthHigh[DEPTH_COUNT + index] = LineDataItem(doubleRow((DEPTH_COUNT + index).toDouble(), cumulativeHigh))
        cumulativeHigh += round(depthRandom.next() * 1000)
        depthLow[DEPTH_COUNT - index - 1] = LineDataItem(doubleRow((DEPTH_COUNT - index - 1).toDouble(), cumulativeLow))
        cumulativeLow += round(depthRandom.next() * 1000)
    }

    return unsafeJso {
        useUTC = true
        title =
            arrayOf(
                matrixTitle("Volume", "${round(walk.volumeTotal / 1000).toInt()}B", 0.0, 5.0),
                matrixTitle("MACD", "", 0.0, 4.0),
                matrixTitle("Order Book", "", 4.0, 0.0),
                matrixTitle("Depth", "", 4.0, 5.0),
            )
        xAxis =
            arrayOf(
                hiddenTimeAxis(),
                hiddenTimeAxis(1.0),
                hiddenTimeAxis(2.0),
                unsafeJso<XAxisOption> {
                    type = AxisTypes.value
                    gridIndex = 3.0
                    show = false
                    max = AxisMinMax("dataMax")
                },
                unsafeJso<XAxisOption> {
                    type = AxisTypes.category
                    gridIndex = 4.0
                    show = false
                    boundaryGap = BooleanOrStringArray(false)
                    data = depthCategories
                },
            )
        yAxis =
            arrayOf(
                unsafeJso<YAxisOption> {
                    type = AxisTypes.value
                    show = false
                    // The price axis is symmetric around the previous close so that the zero line sits
                    // in the middle of the cell.
                    min = AxisMinMax(LAST_CLOSE - walk.maxAbs)
                    max = AxisMinMax(LAST_CLOSE + walk.maxAbs)
                },
                unsafeJso<YAxisOption> {
                    type = AxisTypes.value
                    gridIndex = 1.0
                    show = false
                },
                unsafeJso<YAxisOption> {
                    type = AxisTypes.value
                    gridIndex = 2.0
                    show = false
                },
                unsafeJso<YAxisOption> {
                    type = AxisTypes.category
                    gridIndex = 3.0
                    show = false
                },
                unsafeJso<YAxisOption> {
                    type = AxisTypes.value
                    gridIndex = 4.0
                    show = false
                    max = AxisMinMax("dataMax")
                    min = AxisMinMax("dataMin")
                },
            )
        grid =
            arrayOf(
                stockGrid(0.0, 0.0, top = 0.0, bottom = 0.0, left = 0.0, right = 0.0),
                stockGrid(0.0, 5.0, top = 20.0, bottom = 0.0, left = 0.0, right = 0.0),
                stockGrid(0.0, 4.0, top = 20.0, bottom = 0.0, left = 0.0, right = 0.0),
                stockGrid(4.0, 0.0, top = 15.0, bottom = 2.0, left = 2.0, right = 2.0),
                stockGrid(4.0, 4.0, top = 15.0, bottom = 0.0, left = 0.0, right = 0.0),
            )
        series =
            arrayOf<SeriesOption>(
                lineSeries {
                    symbolSize = SymbolSize(0.0)
                    typedData = lineRows(walk.prices)
                    markPoint = priceMarkPointOption(walk.maxAbs)
                },
                lineSeries {
                    symbolSize = SymbolSize(0.0)
                    typedData = lineRows(walk.averages)
                    xAxisIndex = 0.0
                    yAxisIndex = 0.0
                },
                // The original lists the average twice; the second copy only repaints it one pixel
                // wide in yellow over the first.
                lineSeries {
                    symbolSize = SymbolSize(0.0)
                    typedData = lineRows(walk.averages)
                    xAxisIndex = 0.0
                    yAxisIndex = 0.0
                    lineStyle =
                        unsafeJso<LineStyleOption> {
                            color = ZRColor("#FFC458")
                            width = 1.0
                        }
                },
                barSeries {
                    name = "Volume"
                    xAxisIndex = 1.0
                    yAxisIndex = 1.0
                    typedData = volumeCells
                },
                barSeries {
                    name = "MACD"
                    xAxisIndex = 2.0
                    yAxisIndex = 2.0
                    typedData = macdCells
                    barWidth = StringOrDouble("70%")
                },
                lineSeries {
                    name = "DIF"
                    xAxisIndex = 2.0
                    yAxisIndex = 2.0
                    typedData = lineRows(indicator.dif)
                    lineStyle =
                        unsafeJso<LineStyleOption> {
                            color = ZRColor("#FFC458")
                            width = 1.0
                        }
                    symbol = SymbolShape("none")
                },
                lineSeries {
                    name = "DEA"
                    xAxisIndex = 2.0
                    yAxisIndex = 2.0
                    typedData = lineRows(indicator.dea)
                    lineStyle =
                        unsafeJso<LineStyleOption> {
                            color = ZRColor("#333")
                            width = 1.0
                        }
                    symbol = SymbolShape("none")
                },
                barSeries {
                    name = "Order Book"
                    xAxisIndex = 3.0
                    yAxisIndex = 3.0
                    typedData = orderCells
                    barWidth = StringOrDouble("90%")
                    label =
                        unsafeJso<LabelOption> {
                            show = true
                            position = LabelPosition("insideLeft")
                        }
                },
                lineSeries {
                    name = "Depth High"
                    xAxisIndex = 4.0
                    yAxisIndex = 4.0
                    typedData = depthHigh
                    step = BooleanOrString("end")
                    lineStyle =
                        unsafeJso<LineStyleOption> {
                            color = ZRColor(COLOR_RED)
                            width = 2.0
                        }
                    areaStyle =
                        unsafeJso<AreaStyleOption> {
                            color = ZRColor(COLOR_RED_OPACITY)
                            opacity = 1.0
                        }
                    symbol = SymbolShape("none")
                },
                lineSeries {
                    name = "Depth Low"
                    xAxisIndex = 4.0
                    yAxisIndex = 4.0
                    typedData = depthLow
                    step = BooleanOrString("end")
                    lineStyle =
                        unsafeJso<LineStyleOption> {
                            color = ZRColor(COLOR_GREEN)
                            width = 2.0
                        }
                    areaStyle =
                        unsafeJso<AreaStyleOption> {
                            color = ZRColor(COLOR_GREEN_OPACITY)
                            opacity = 1.0
                        }
                    symbol = SymbolShape("none")
                },
            )
        this.matrix =
            matrixOption {
                left = StringOrDouble(MATRIX_MARGIN)
                right = StringOrDouble(MATRIX_MARGIN)
                top = StringOrDouble(MATRIX_MARGIN)
                bottom = StringOrDouble(MATRIX_MARGIN)
                x =
                    dimension {
                        show = false
                        data = names(*(0..4).map { it.toString() }.toTypedArray())
                    }
                y =
                    dimension {
                        show = false
                        data = names(*(0..5).map { it.toString() }.toTypedArray())
                    }
                body =
                    unsafeJso<MatrixBodyOption> {
                        data =
                            arrayOf(
                                mergedCell(0.0, 3.0, 0.0, 3.0),
                                mergedCell(0.0, 3.0, 5.0, 5.0),
                                mergedCell(0.0, 3.0, 4.0, 4.0),
                                mergedCell(4.0, 4.0, 0.0, 3.0),
                                mergedCell(4.0, 4.0, 4.0, 5.0),
                            )
                    }
            }
        graphic =
            arrayOf(
                GraphicComponentLooseOption(
                    unsafeJso<GraphicComponentOption> {
                        elements =
                            Array(3) { index ->
                                separator(
                                    MATRIX_MARGIN + 1,
                                    matrixHeight / 6 * (index + 1),
                                    matrixWidth / 5 * 4 + MATRIX_MARGIN,
                                    matrixHeight / 6 * (index + 1),
                                    dashed = index == 1,
                                    stroke = if (index == 1) "#bbb" else "#eee",
                                )
                            } +
                                Array(3) { index ->
                                    separator(
                                        matrixWidth / 5 * (index + 1) + MATRIX_MARGIN,
                                        MATRIX_MARGIN + 1,
                                        matrixWidth / 5 * (index + 1) + MATRIX_MARGIN,
                                        chartHeight - MATRIX_MARGIN,
                                        dashed = false,
                                        stroke = "#eee",
                                    )
                                }
                    },
                ),
            )
    }
}

/** One grid pinned to a matrix cell. */
internal fun stockGrid(
    x: Double,
    y: Double,
    top: Double,
    bottom: Double,
    left: Double,
    right: Double,
): GridOption =
    unsafeJso {
        coordinateSystem = "matrix"
        coord = CoordinateSystemDataCoord(arrayOf(OptionDataValue(x), OptionDataValue(y)))
        this.top = StringOrDouble(top)
        this.bottom = StringOrDouble(bottom)
        this.left = StringOrDouble(left)
        this.right = StringOrDouble(right)
    }
