package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import js.objects.Record
import kotlin.js.JsAny
import kotlin.js.js
import kotlin.js.unsafeCast
import zrender.gradientColorStop
import zrender.linearGradient
import zrender.ZRColor

/*
 * The two intraday pages of ECharts 6, ported from `public/examples/ts/intraday-breaks-1.ts` and
 * `intraday-breaks-2.ts`.
 *
 * Both draw a minute-by-minute price line over an axis with a break for every night the market is
 * closed. The first one spans four days and gives every break a 1% gap; the second one covers a
 * single day and writes the break on the axis label instead of moving the label away, which is what
 * `breakLabelLayout.moveOverlap: false` is for.
 *
 * Changed on purpose:
 *
 * - `Math.random()` is replaced by a seeded [Lcg], so the walk is the same on every visit;
 * - the times are built with `Date.UTC`, which is what the upstream `useUTC: true` axis means; the
 *   original's `time.setUTCHours()` after `roundTime` is the same thing but harder to follow;
 * - the labels of the first page keep the upstream rich text block (`{weak|...}`) with its `rich`
 *   definition, because a missing `rich` entry would print the tag itself.
 */

private val intradayBreaks = Category(Groups.LINE, "examples/IntradayBreaks.kt")

/** The two generated-data pages live in this file too, but they belong to other families. */
private val generatedArea = Category(Groups.LINE, "examples/IntradayBreaks.kt")

/** `candlestick-large` belongs to the candlestick family; the source file is shared with the two above. */
private val generatedCandlestick = Category(Groups.CANDLESTICK, "examples/IntradayBreaks.kt")

/** One minute in milliseconds. */
private const val MINUTE = 60_000.0

/** One day in milliseconds. */
private const val ONE_DAY = 24 * 60 * MINUTE

/** One break per closed night: the value the first page gives the `gap`. */
private const val NIGHT_GAP = "1%"

/** Smallest window the data zoom of both pages allows, one hour. */
private const val ZOOM_MIN_SPAN = 3600 * 1000.0

/** Open time of the market, in hours and minutes UTC. */
private const val MARKET_OPEN_HOUR = 9

/** See [MARKET_OPEN_HOUR]. */
private const val MARKET_OPEN_MINUTE = 30

/** Close time of the market, in hours UTC. */
private const val MARKET_CLOSE_HOUR = 16

/** Value the random walk starts on, the upstream `val = 1669`. */
private const val WALK_START = 1669.0

/** First day of the four-day page, as `[year, month, day]`; the month is zero based like `Date`. */
private val MULTI_DAY_START = intArrayOf(2024, 3, 9)

/** Last day of the four-day page, as `[year, month, day]`. */
private val MULTI_DAY_END = intArrayOf(2024, 3, 12)

/** The single day of the second page, as `[year, month, day]`. */
private val SINGLE_DAY = intArrayOf(2024, 3, 9)

/** Lunch break of the second page: start and end, in hours UTC. */
private const val LUNCH_START_HOUR = 11

/** See [LUNCH_START_HOUR]. */
private const val LUNCH_START_MINUTE = 30

/** See [LUNCH_START_HOUR]. */
private const val LUNCH_END_HOUR = 13

/** Result of [walkMultiDay]: the minute series and the breaks between the days. */
private class IntradayData(val points: Array<LineDataItem?>, val breaks: Array<AxisBreakOption>)

/** `Date.UTC(year, month, day, hour, minute)` of one wall-clock time. */
private fun utc(year: Int, month: Int, day: Int, hour: Int = 0, minute: Int = 0): Double =
    js("(y, mo, d, h, mi) => Date.UTC(y, mo, d, h, mi)")(year, month, day, hour, minute) as Double

/** `Date.UTC(...)` of [MONTH_START]-style triples; see [utc]. */
private fun utcOf(date: IntArray, hour: Int = 0, minute: Int = 0): Double =
    utc(date[0], date[1], date[2], hour, minute)

/**
 * The four-day random walk of `intraday-breaks-1`.
 *
 * Every step is `(random - 0.5 * sin(value / 1000)) * 20 / 100`, rounded to whole cents; the first
 * step of a day is ten times larger, which is the upstream `valBreak` flag. The last point of a day
 * is `NaN` so the line breaks, and the night between the close and the next open becomes an axis
 * break.
 */
private fun walkMultiDay(random: Lcg): IntradayData {
    val points = mutableListOf<LineDataItem?>()
    val breaks = mutableListOf<AxisBreakOption>()
    val lastDay = utcOf(MULTI_DAY_END, 23, 59)
    var day = utcOf(MULTI_DAY_START)
    var afterBreak = false
    var value = WALK_START
    while (day <= lastDay) {
        val open = day + (MARKET_OPEN_HOUR * 60 + MARKET_OPEN_MINUTE) * MINUTE
        val close = day + MARKET_CLOSE_HOUR * 60 * MINUTE
        var time = open
        while (time <= close) {
            val scale = if (afterBreak) 10.0 else 1.0
            val delta =
                kotlin.math.floor((random.next() - 0.5 * kotlin.math.sin(value / 1000)) * 20 * scale) / 100.0
            value = kotlin.math.round((value + delta) * 100) / 100.0
            points += LineDataItem(doubleRow(time, value))
            afterBreak = false
            time += MINUTE
        }
        // The night: a NaN point closes the segment and an axis break carries the gap.
        points += LineDataItem(row(close + MINUTE, Double.NaN))
        val nextOpen = day + ONE_DAY + (MARKET_OPEN_HOUR * 60 + MARKET_OPEN_MINUTE) * MINUTE
        breaks +=
            unsafeJso<AxisBreakOption> {
                start = OptionDataValue(close)
                end = OptionDataValue(nextOpen)
                gap = StringOrDouble(NIGHT_GAP)
            }
        afterBreak = true
        day += ONE_DAY
    }
    return IntradayData(points.toTypedArray(), breaks.toTypedArray())
}

/** The single-day walk of `intraday-breaks-2`, skipping the lunch break. */
private fun walkSingleDay(random: Lcg): IntradayData {
    val points = mutableListOf<LineDataItem?>()
    val dayBreakStart = utcOf(SINGLE_DAY, LUNCH_START_HOUR, LUNCH_START_MINUTE)
    val dayBreakEnd = utcOf(SINGLE_DAY, LUNCH_END_HOUR)
    val end = utcOf(SINGLE_DAY, 15)
    var time = utcOf(SINGLE_DAY, MARKET_OPEN_HOUR, MARKET_OPEN_MINUTE)
    var value = WALK_START
    while (time <= end) {
        if (time <= dayBreakStart || time >= dayBreakEnd) {
            val delta =
                kotlin.math.floor((random.next() - 0.5 * kotlin.math.sin(value / 1000)) * 20 * 100) / 100.0
            value = kotlin.math.round((value + delta) * 100) / 100.0
            points += LineDataItem(doubleRow(time, value))
        }
        time += MINUTE
    }
    return IntradayData(
        points.toTypedArray(),
        arrayOf(
            unsafeJso<AxisBreakOption> {
                start = OptionDataValue(dayBreakStart)
                this.end = OptionDataValue(dayBreakEnd)
                gap = StringOrDouble(0.0)
            },
        ),
    )
}

/** The area style of both break bands: invisible, only the gap is drawn. */
private fun invisibleBreakArea(): AxisBreakAreaOption =
    unsafeJso<AxisBreakAreaOption> {
        expandOnClick = false
        zigzagAmplitude = 0.0
        zigzagZ = 200.0
        itemStyle = unsafeJso<ItemStyleOption> { borderColor = ZRColor("none"); opacity = 0.0 }
    }

/** The `rich` block of the axis label of `intraday-breaks-1`: one grey `weak` tag. */
private fun weakRichText(): Record<String, TextStyleOption> =
    unsafeJso<Record<String, TextStyleOption>> {
        @Suppress("UNUSED_EXPRESSION")
        this
    }.also { block -> js("(block) => { block['weak'] = { color: '#999' }; }")(block) }

/** The only x axis of the second page, as the index list both data zooms target. */
private fun allAxisIndices(): IndexOrIndexArray = IndexOrIndexArray(arrayOf(0.0))

/**
 * The axis label of `intraday-breaks-1`: on a break it also writes the day, through the `weak` rich
 * tag defined by the page.
 *
 * The axis formatter receives a third argument (the break info) that the wrapper's
 * [LabelFormatterCallback] does not model, so the extra argument is read in JavaScript and the
 * formatting itself goes through [timeFormat].
 */
private fun axisFormatterWithDay(value: JsAny, extra: JsAny?): String {
    val timestamp = numeric(value)
    val onBreak = js("(extra) => !!(extra && extra.break)")(extra) as Boolean
    return if (onBreak) {
        timeFormat(timestamp, "{HH}:{mm}\n{weak|{dd}d}")
    } else {
        timeFormat(timestamp, "{HH}:{mm}")
    }
}

/**
 * The axis label of `intraday-breaks-2`: a single label spanning the lunch break, and nothing on the
 * far side of it.
 */
private fun axisFormatterWithBreak(value: JsAny, extra: JsAny?): String {
    val timestamp = numeric(value)
    val info = js("(extra) => (extra && extra.break) || null")(extra)
    if (info == null || js("(i) => i === null")(info) as Boolean) {
        return timeFormat(timestamp, "{HH}:{mm}")
    }
    val type = js("(i) => i.type")(info) as String
    if (type != "start") return ""
    val start = js("(i) => i.start")(info)
    val end = js("(i) => i.end")(info)
    return timeFormat(numeric(start), "{HH}:{mm}") + "/" + timeFormat(numeric(end), "{HH}:{mm}")
}

/**
 * Wraps a three-argument axis formatter as the one-argument [LabelFormatterCallback].
 *
 * ECharts passes `(value, index, extra)`; the wrapper keeps the single-argument shape, so the
 * callback is widened in JavaScript and the Kotlin function receives the value and the extra info.
 */
private fun axisLabel(formatter: (value: JsAny, extra: JsAny?) -> String): LabelFormatterCallback =
    js("(fn) => (value, index, extra) => fn(value, extra)")(formatter).unsafeCast<LabelFormatterCallback>()

/** `ECharts.time.format(value, template, true)`: the pages format their labels in UTC. */
private fun timeFormat(value: Double, template: String): String =
    ECharts.time.format(value.unsafeCast<JsAny>(), template, true)

val intradayBreaksEntries: List<CatalogEntry> =
    listOf(
        intradayBreaks.chart(
            "intraday-breaks-1",
            "断轴上的日内走势图",
            "Intraday Chart with Breaks",
            "四个交易日、逐分钟的价格折线，每晚收盘到次日开盘之间是一段被折叠的断轴，" +
                "断口上标着日期；价格在夜间断开，线不会连成一条。",
            "Four trading days of minute data with a folded break between the close and the next " +
                "open; the break carries the date and the line does not connect across the night.",
        ) {
            val data = walkMultiDay(Lcg(20240409))
            unsafeJso<EChartsOption> {
                useUTC = true
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Intraday Chart with Breaks (Multiple Days)"
                            left = StringOrDouble("center")
                        },
                    )
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            show = true
                            trigger = TooltipTriggers.axis
                        },
                    )
                grid =
                    one(
                        unsafeJso<GridOption> {
                            outerBounds =
                                unsafeJso<BoxLayoutOption> {
                                    top = StringOrDouble("20%")
                                    bottom = StringOrDouble("30%")
                                }
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.time
                            interval = AxisInterval(1000.0 * 60 * 30)
                            axisLabel =
                                unsafeJso<AxisLabelOption> {
                                    showMinLabel = true
                                    showMaxLabel = true
                                    formatter = LabelFormatter(axisLabel(::axisFormatterWithDay))
                                    rich = weakRichText()
                                }
                            breaks = data.breaks
                            breakArea = invisibleBreakArea()
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            min = AxisMinMax("dataMin")
                        },
                    )
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.inside
                            minValueSpan = ZOOM_MIN_SPAN
                        },
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.slider
                            top = StringOrDouble("73%")
                            minValueSpan = ZOOM_MIN_SPAN
                        },
                    )
                series =
                    one(
                        lineSeries {
                            symbolSize = SymbolSize(0.0)
                            areaStyle = unsafeJso<AreaStyleOption> {}
                            typedData = data.points
                        },
                    )
            }
        },
        intradayBreaks.chart(
            "intraday-breaks-2",
            "断轴上的日内走势图 (II)",
            "Intraday Chart with Breaks (II)",
            "单日逐分钟走势，午休时段折成一段零宽度的断轴；`breakLabelLayout.moveOverlap: false` " +
                "关掉标签自动避让，轴标签自己把「开盘/收盘」写成一个跨断口的标签。",
            "One trading day of minute data with the lunch break folded to zero width; " +
                "`breakLabelLayout.moveOverlap: false` turns off the automatic label avoidance so the " +
                "axis label writes the break itself.",
        ) {
            val data = walkSingleDay(Lcg(20240409))
            unsafeJso<EChartsOption> {
                useUTC = true
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Intraday Chart with Breaks (Single Day)"
                            left = StringOrDouble("center")
                        },
                    )
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            show = true
                            trigger = TooltipTriggers.axis
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.time
                            interval = AxisInterval(1000.0 * 60 * 30)
                            axisLabel =
                                unsafeJso<AxisLabelOption> {
                                    showMinLabel = true
                                    showMaxLabel = true
                                    formatter = LabelFormatter(axisLabel(::axisFormatterWithBreak))
                                }
                            breakLabelLayout =
                                unsafeJso<AxisBreakLabelLayoutOption> { moveOverlap = BooleanOrString(false) }
                            breaks = data.breaks
                            breakArea =
                                unsafeJso<AxisBreakAreaOption> {
                                    expandOnClick = false
                                    zigzagAmplitude = 0.0
                                    zigzagZ = 200.0
                                }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            min = AxisMinMax("dataMin")
                        },
                    )
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.inside
                            xAxisIndex = allAxisIndices()
                        },
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.slider
                            xAxisIndex = allAxisIndices()
                        },
                    )
                series =
                    one(
                        lineSeries {
                            symbolSize = SymbolSize(0.0)
                            typedData = data.points
                        },
                    )
            }
        },
    )

/*
 * The two generated-data pages that belong to the same "many points" family as the intraday ones:
 * `area-simple` (twenty thousand days of a random walk under a gradient area) and
 * `candlestick-large` (two hundred thousand candles with a volume sub-chart).
 *
 * Both replace `Math.random()` with a seeded [Lcg]; `candlestick-large` also builds its rows as a
 * `dataset` like the original, with the sign column that drives the hidden visual map.
 */

/** Points of `area-simple`, the upstream `20000`. */
private const val AREA_SIMPLE_POINTS = 20000

/** First day of `area-simple`: 1968-10-03 in the original. */
private const val AREA_SIMPLE_START = "1968-10-03"

/** Candles of `candlestick-large`, the upstream `2e5`. */
private const val LARGE_CANDLE_COUNT = 200_000

/** First timestamp of `candlestick-large`, one candle per minute from 2011-01-01. */
private const val LARGE_CANDLE_START = 1_293_840_000_000.0

/** One candle of `candlestick-large`, as `[open, highest, lowest, close, volume, sign]`. */
private const val LARGE_CANDLE_DAY_RANGE = 12.0

/** Colors of the rising candles of `candlestick-large`. */
private const val UP_COLOR = "#ec0000"

/** Border color of the rising candles of `candlestick-large`. */
private const val UP_BORDER_COLOR = "#8A0000"

/** Color of the falling candles of `candlestick-large`. */
private const val DOWN_COLOR = "#00da3c"

/** Border color of the falling candles of `candlestick-large`. */
private const val DOWN_BORDER_COLOR = "#008F28"

/** The dates of `area-simple`, one per day from 1968-10-03, as `yyyy/M/d` strings. */
private fun areaSimpleDates(): Array<String> {
    val start = js("(iso) => new Date(iso).getTime()")(AREA_SIMPLE_START) as Double
    return Array(AREA_SIMPLE_POINTS) { index ->
        val date = kotlin.js.Date(start + index * ONE_DAY)
        "${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()}"
    }
}

/** The random walk of `area-simple`: `round((random - 0.5) * 20 + previous)`. */
private fun areaSimpleValues(): DoubleArray {
    val random = Lcg(19681003)
    val values = DoubleArray(AREA_SIMPLE_POINTS)
    values[0] = random.next() * 300
    for (index in 1 until AREA_SIMPLE_POINTS) {
        values[index] = kotlin.math.round((random.next() - 0.5) * 20 + values[index - 1])
    }
    return values
}

/** One row of the `candlestick-large` dataset. */
private class CandleRow(
    val label: String,
    val open: Double,
    val highest: Double,
    val lowest: Double,
    val close: Double,
    val volume: Double,
    val sign: Double,
)

/**
 * The OHLC table of `candlestick-large`.
 *
 * The original draws four box values per candle, sorts them and picks the open and the close out of
 * the sorted list before writing `[open, highest, lowest, close]`; the sign column is `1` when the
 * candle closes at or above its open (the previous close decides a tie) and `-1` otherwise, which is
 * what the hidden visual map of the page reads.
 */
private fun largeCandles(): Array<CandleRow> {
    val random = Lcg(20110101)
    val rows = arrayOfNulls<CandleRow>(LARGE_CANDLE_COUNT)
    var base = random.next() * 12000
    var previousClose = 0.0
    val label =
        js(
            "(value) => { const d = new Date(value);" +
                " const p = (n) => String(n).padStart(2, '0');" +
                " return d.getFullYear() + '-' + p(d.getMonth() + 1) + '-' + p(d.getDate()) + '\\n' +" +
                " p(d.getHours()) + ':' + p(d.getMinutes()) + ':' + p(d.getSeconds()); }",
        )
    for (index in 0 until LARGE_CANDLE_COUNT) {
        base += random.next() * 20 - 10
        val box = DoubleArray(4) { (random.next() - 0.5) * LARGE_CANDLE_DAY_RANGE + base }
        box.sort()
        val openIndex = kotlin.math.round(random.next() * 3).toInt()
        var closeIndex = kotlin.math.round(random.next() * 2).toInt()
        if (closeIndex == openIndex) closeIndex++
        val open = box[openIndex]
        val close = box[closeIndex]
        val sign =
            when {
                open > close -> -1.0
                open < close -> 1.0
                else -> if (index > 0 && previousClose <= close) 1.0 else if (index > 0) -1.0 else 1.0
            }
        previousClose = close
        rows[index] =
            CandleRow(
                label(LARGE_CANDLE_START + index * 60_000) as String,
                kotlin.math.round(open * 100) / 100,
                kotlin.math.round(box[3] * 100) / 100,
                kotlin.math.round(box[0] * 100) / 100,
                kotlin.math.round(close * 100) / 100,
                kotlin.math.round(box[3] * (1000 + random.next() * 500)),
                sign,
            )
    }
    @Suppress("UNCHECKED_CAST")
    return rows as Array<CandleRow>
}

/** `echarts.format.addCommas` of [value], the title of `candlestick-large`. */
private fun thousands(value: Int): String =
    ECharts.format.addCommas(StringOrDouble(value.toDouble()))

/** The candles of `candlestick-large`, generated once when the page mounts. */
private val LARGE_CANDLES: Array<CandleRow> by lazy { largeCandles() }

/** The rows of [largeCandles] as the `dataset.source` the page needs. */
private fun largeCandleSource(): DatasetSource =
    datasetArrayRows(
        Array(LARGE_CANDLE_COUNT) { index ->
            val row = LARGE_CANDLES[index]
            arrayOf(
                OptionDataValue(row.label),
                OptionDataValue(row.open),
                OptionDataValue(row.highest),
                OptionDataValue(row.lowest),
                OptionDataValue(row.close),
                OptionDataValue(row.volume),
                OptionDataValue(row.sign),
            )
        },
    )

val generatedSeriesEntries: List<CatalogEntry> =
    listOf(
        generatedArea.chart(
            "area-simple",
            "大数据量面积图",
            "Large scale area chart",
            "两万个点的随机游走铺成渐变的面积图，只显示窗口里的十分之一，`sampling: 'lttb'` " +
                "负责抽稀；tooltip 固定在图表顶部 10% 处。",
            "Twenty thousand points of a random walk under a gradient area; the data zoom shows the " +
                "first tenth and `sampling: 'lttb'` thins the rest, with the tooltip pinned to 10% of " +
                "the height.",
        ) {
            val dates = areaSimpleDates()
            val values = areaSimpleValues()
            unsafeJso<EChartsOption> {
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            position =
                                TooltipPosition(
                                    fun(
                                        point: Array<Double>,
                                        _: TopLevelFormatterParams,
                                        _: JsAny?,
                                        _: zrender.RectLike?,
                                        _: TooltipSize,
                                    ): TooltipPosition =
                                        TooltipPosition(
                                            arrayOf(StringOrDouble(point[0]), StringOrDouble("10%")),
                                        ),
                                )
                        },
                    )
                title =
                    one(
                        unsafeJso<TitleOption> {
                            left = StringOrDouble("center")
                            text = "Large Area Chart"
                        },
                    )
                toolbox =
                    one(
                        unsafeJso<ToolboxOption> {
                            feature =
                                unsafeJso<ToolboxFeature> {
                                    dataZoom =
                                        unsafeJso<ToolboxDataZoomFeatureOption> {
                                            yAxisIndex = ModelFinderIndexQuery("none")
                                        }
                                    restore = unsafeJso<ToolboxFeatureOption> {}
                                    saveAsImage = unsafeJso<SaveAsImageOption> {}
                                }
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            boundaryGap = BooleanOrStringArray(false)
                            data = strings(*dates)
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            boundaryGap =
                                BooleanOrStringArray(
                                    arrayOf(StringOrDouble(0.0), StringOrDouble("100%")),
                                )
                        },
                    )
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.inside
                            start = 0.0
                            end = 10.0
                        },
                        unsafeJso<DataZoomOption> {
                            start = 0.0
                            end = 10.0
                        },
                    )
                series =
                    one(
                        lineSeries {
                            name = "Fake Data"
                            symbol = SymbolShape("none")
                            sampling = "lttb"
                            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("rgb(255, 70, 131)") }
                            areaStyle =
                                unsafeJso<AreaStyleOption> {
                                    color =
                                        linearGradient(
                                            0.0,
                                            0.0,
                                            0.0,
                                            1.0,
                                            arrayOf(
                                                gradientColorStop(0.0, "rgb(255, 158, 68)"),
                                                gradientColorStop(1.0, "rgb(255, 70, 131)"),
                                            ),
                                        )
                                }
                            typedData = numbers(*values)
                        },
                    )
            }
        },
        generatedCandlestick.chart(
            "candlestick-large",
            "大数据量K线图",
            "Large Scale Candlestick",
            "二十万根分钟级 K 线走 dataset，成交量挂在第二个网格里用 large 模式画柱；" +
                "涨跌靠额外的 sign 列与隐藏的 visualMap 上色。",
            "Two hundred thousand minute candles driven by a `dataset`, with the volume in a second " +
                "grid drawn in large mode; an extra sign column and a hidden visual map paint the up " +
                "and down candles.",
        ) {
            val source = largeCandleSource()
            unsafeJso<EChartsOption> {
                dataset = one(unsafeJso<DatasetOption> { this.source = source })
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Data Amount: ${thousands(LARGE_CANDLE_COUNT)}"
                        },
                    )
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.line }
                        },
                    )
                toolbox =
                    one(
                        unsafeJso<ToolboxOption> {
                            feature =
                                unsafeJso<ToolboxFeature> {
                                    dataZoom =
                                        unsafeJso<ToolboxDataZoomFeatureOption> {
                                            yAxisIndex = ModelFinderIndexQuery("none")
                                        }
                                }
                        },
                    )
                grid =
                    arrayOf(
                        unsafeJso<GridOption> {
                            left = StringOrDouble("10%")
                            right = StringOrDouble("10%")
                            bottom = StringOrDouble(200.0)
                        },
                        unsafeJso<GridOption> {
                            left = StringOrDouble("10%")
                            right = StringOrDouble("10%")
                            height = StringOrDouble(80.0)
                            bottom = StringOrDouble(80.0)
                        },
                    )
                xAxis =
                    arrayOf(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            boundaryGap = BooleanOrStringArray(false)
                            axisLine = unsafeJso<AxisLineOption> { onZero = BooleanOrString(false) }
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                            min = AxisMinMax("dataMin")
                            max = AxisMinMax("dataMax")
                        },
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            gridIndex = 1.0
                            boundaryGap = BooleanOrStringArray(false)
                            axisLine = unsafeJso<AxisLineOption> { onZero = BooleanOrString(false) }
                            axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                            axisLabel = unsafeJso<AxisLabelOption> { show = false }
                            min = AxisMinMax("dataMin")
                            max = AxisMinMax("dataMax")
                        },
                    )
                yAxis =
                    arrayOf(
                        unsafeJso<YAxisOption> {
                            scale = true
                            splitArea = unsafeJso<SplitAreaOption> { show = true }
                        },
                        unsafeJso<YAxisOption> {
                            scale = true
                            gridIndex = 1.0
                            splitNumber = 2.0
                            axisLabel = unsafeJso<AxisLabelOption> { show = false }
                            axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
                            axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                        },
                    )
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.inside
                            xAxisIndex = IndexOrIndexArray(arrayOf(0.0, 1.0))
                            start = 10.0
                            end = 100.0
                        },
                        unsafeJso<DataZoomOption> {
                            show = true
                            xAxisIndex = IndexOrIndexArray(arrayOf(0.0, 1.0))
                            type = DataZoomTypes.slider
                            bottom = StringOrDouble(10.0)
                            start = 10.0
                            end = 100.0
                        },
                    )
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            show = false
                            seriesIndex = IndexOrIndexArray(1.0)
                            dimension = 6.0
                            pieces =
                                arrayOf(
                                    unsafeJso<VisualPiece> {
                                        value = 1.0
                                        color = StringOrStringArray(UP_COLOR)
                                    },
                                    unsafeJso<VisualPiece> {
                                        value = -1.0
                                        color = StringOrStringArray(DOWN_COLOR)
                                    },
                                )
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        candlestickSeries {
                            itemStyle =
                                unsafeJso<CandlestickItemStyleOption> {
                                    color = ZRColor(UP_COLOR)
                                    color0 = ZRColor(DOWN_COLOR)
                                    borderColor = ZRColor(UP_BORDER_COLOR)
                                    borderColor0 = ZRColor(DOWN_BORDER_COLOR)
                                }
                            encode =
                                unsafeJso<OptionEncode> {
                                    x = OptionEncodeValue(DimensionLoose(0.0))
                                    y =
                                        OptionEncodeValue(
                                            arrayOf(
                                                DimensionLoose(1.0),
                                                DimensionLoose(4.0),
                                                DimensionLoose(3.0),
                                                DimensionLoose(2.0),
                                            ),
                                        )
                                }
                        },
                        barSeries {
                            name = "Volumn"
                            xAxisIndex = 1.0
                            yAxisIndex = 1.0
                            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#7fbe9e") }
                            large = true
                            encode =
                                unsafeJso<OptionEncode> {
                                    x = OptionEncodeValue(DimensionLoose(0.0))
                                    y = OptionEncodeValue(DimensionLoose(5.0))
                                }
                        },
                    )
            }
        },
    )
