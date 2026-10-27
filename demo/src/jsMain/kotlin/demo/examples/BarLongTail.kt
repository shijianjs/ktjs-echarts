package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.math.round
import zrender.ZRColor
import zrender.ZRShapeProps
import zrender.ZRStyleProps

/*
 * The long tail of the bar family: the marker pages and the stacked bars whose shape is computed
 * out of the data table instead of written down.
 *
 * The normalization pages keep the upstream table and do the same division at build time, so the
 * percent labels and the connecting polygons of `bar-stack-normalization-and-variation` stay in
 * agreement with the bars. That page needs the size of the chart to place its polygons, which is why
 * it is an `interactive` example: the original reads `myChart.getWidth()` the same way.
 *
 * `bar-stack-borderRadius` keeps the truthiness test of the original: a value of `0` counts as an
 * empty slot, so the series that closes a stack — and therefore gets the rounded top — can be an
 * earlier one than a strict "is not `-`" test would pick.
 *
 * Dropped on purpose: the root `calculable: true` of `bar1`, a pre-5 option field that ECharts 6 no
 * longer reads and therefore no longer declares.
 */

private val barLongTail = Category(Groups.BAR, "examples/BarLongTail.kt")

/** The months of `bar1`. */
private val RAINFALL_MONTHS =
    arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

/** The monthly rainfall of `bar1`. */
private val RAINFALL_VALUES =
    doubleArrayOf(2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3)

/** The monthly evaporation of `bar1`, whose extremes are written out instead of computed. */
private val EVAPORATION_VALUES =
    doubleArrayOf(2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3)

/** The advertising channels stacked by the two normalization pages, in legend order. */
private val AD_CHANNELS = arrayOf("Direct", "Mail Ad", "Affiliate Ad", "Video Ad", "Search Engine")

/** The days of the two normalization pages. */
private val AD_DAYS = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

/** `[channel][day]` counts; the upstream comment warns that the table holds no negative value. */
private val AD_COUNTS =
    arrayOf(
        doubleArrayOf(100.0, 302.0, 301.0, 334.0, 390.0, 330.0, 320.0),
        doubleArrayOf(320.0, 132.0, 101.0, 134.0, 90.0, 230.0, 210.0),
        doubleArrayOf(220.0, 182.0, 191.0, 234.0, 290.0, 330.0, 310.0),
        doubleArrayOf(150.0, 212.0, 201.0, 154.0, 190.0, 330.0, 410.0),
        doubleArrayOf(820.0, 832.0, 901.0, 934.0, 1290.0, 1330.0, 1320.0),
    )

/** The total of every day, the denominator of the normalization. */
private val AD_TOTALS = Array(AD_DAYS.size) { day -> AD_COUNTS.sumOf { it[day] } }

/** The colors that tint the polygons between the normalized bars of the variation page. */
private val AD_POLYGON_COLORS = arrayOf("#5070dd", "#b6d634", "#505372", "#ff994d", "#0ca8df")

/** The pixel margins of the grid of `bar-stack-normalization-and-variation`. */
private const val VARIATION_GRID_LEFT = 100.0

/** See [VARIATION_GRID_LEFT]. */
private const val VARIATION_GRID_RIGHT = 100.0

/** See [VARIATION_GRID_LEFT]. */
private const val VARIATION_GRID_TOP = 50.0

/** See [VARIATION_GRID_LEFT]. */
private const val VARIATION_GRID_BOTTOM = 90.0

/** Fraction of a category the stacked bars of the normalization pages occupy. */
private const val NORMALIZED_BAR_RATIO = 0.6

/** Corner radius of the bar that closes a stack in `bar-stack-borderRadius`. */
private const val STACK_CORNER_RADIUS = 20.0

/** The days of `bar-waterfall2`, numbered like the original. */
private val WATERFALL_DAYS = (1..11).map { "Nov $it" }.toTypedArray()

/** The invisible base of the waterfall; the visible segment always starts on top of it. */
private val WATERFALL_PLACEHOLDERS =
    doubleArrayOf(0.0, 900.0, 1245.0, 1530.0, 1376.0, 1376.0, 1511.0, 1689.0, 1856.0, 1495.0, 1292.0)

/** Raw values of `bar-stack-borderRadius`, where `"-"` is the hole the original leaves in a stack. */
private val RADIUS_ROWS: Array<Array<Any?>> =
    arrayOf(
        arrayOf(120, 200, 150, 80, 70, 110, 130),
        arrayOf(10, 46, 64, "-", 0, "-", 0),
        arrayOf(30, "-", 0, 20, 10, "-", 0),
        arrayOf(30, "-", 0, 20, 10, "-", 0),
        arrayOf(10, 20, 150, 0, "-", 50, 10),
    )

/** The stack group every series of `bar-stack-borderRadius` belongs to. */
private val RADIUS_STACKS = arrayOf("a", "a", "a", "b", "b")

/** The names of the series of `bar-stack-borderRadius`. */
private val RADIUS_NAMES = arrayOf("a", "b", "c", "d", "e")

/**
 * Whether one raw value of [RADIUS_ROWS] contributes to its stack.
 *
 * Only numbers count, which reproduces the `data && data !== '-'` test of the original: `"-"` is
 * filtered explicitly and the value `0` falls to the same branch because it is falsy in JavaScript.
 */
private fun radiusFilled(value: Any?): Boolean = value is Number && value.toDouble() != 0.0

/** For every stack of [RADIUS_STACKS], the index of the last series that contributes at every day. */
private val RADIUS_STACK_ENDS: Map<String, IntArray> =
    run {
        val ends = LinkedHashMap<String, IntArray>()
        for (day in AD_DAYS.indices) {
            for (index in RADIUS_NAMES.indices) {
                val stack = RADIUS_STACKS[index]
                val end = ends.getOrPut(stack) { IntArray(AD_DAYS.size) { -1 } }
                if (radiusFilled(RADIUS_ROWS[index][day])) {
                    end[day] = index
                }
            }
        }
        ends
    }

/** The percent label of a normalized stack: `Math.round(value * 1000) / 10` followed by a percent sign. */
private fun normalizedLabels(): LabelOption =
    unsafeJso<LabelOption> {
        show = true
        // valueText keeps the JavaScript formatting, which prints 30 where Kotlin would print 30.0.
        formatter = LabelFormatter({ params -> "${valueText(round(numeric(params.value) * 1000) / 10)}%" })
    }

/** One normalized stacked bar of the two `bar-stack-normalization*` pages. */
private fun normalizedSeries(channel: Int): BarSeriesOption =
    barSeries {
        name = AD_CHANNELS[channel]
        stack = "total"
        barWidth = StringOrDouble("60%")
        label = normalizedLabels()
        typedData =
            numbers(
                *DoubleArray(AD_DAYS.size) { day ->
                    val total = AD_TOTALS[day]
                    if (total <= 0) 0.0 else AD_COUNTS[channel][day] / total
                },
            )
    }

/** The five stacked bars shared by the two normalization pages. */
private fun normalizedSeriesList(): Array<SeriesOption> =
    Array<SeriesOption>(AD_CHANNELS.size) { index -> normalizedSeries(index) }

/** The average mark line both series of `bar1` draw. */
private fun averageMarkLine(): MarkLineOption =
    unsafeJso<MarkLineOption> {
        data =
            arrayOf<MarkLineDataItem?>(
                MarkLineDataItem(
                    unsafeJso<MarkLine1DDataItemOption> {
                        name = "Avg"
                        type = MarkerStatisticTypes.average
                    },
                ),
            )
    }

/** The axes of the two normalization pages. */
private fun normalizationAxes(): Pair<XAxisOption, YAxisOption> =
    unsafeJso<XAxisOption> {
        type = AxisTypes.category
        data = strings(*AD_DAYS)
    } to unsafeJso<YAxisOption> { type = AxisTypes.value }

/** The legend of the two normalization pages, which cannot toggle the channels. */
private fun normalizationLegend(): LegendOption = unsafeJso { selectedMode = SelectionMode(false) }

/**
 * The polygons that connect two neighbouring normalized bars.
 *
 * Every band is drawn as one closed path per channel, from the top of the previous day's stack down
 * to the top of this day's; because the bars are only 60% wide, the band starts at the right edge of
 * one bar and ends at the left edge of the next.
 */
private fun variationPolygons(width: Double, height: Double): Array<GraphicElement> {
    val gridWidth = width - VARIATION_GRID_LEFT - VARIATION_GRID_RIGHT
    val gridHeight = height - VARIATION_GRID_TOP - VARIATION_GRID_BOTTOM
    val categoryWidth = gridWidth / AD_DAYS.size
    val barPadding = (categoryWidth - categoryWidth * NORMALIZED_BAR_RATIO) / 2
    val shapes = mutableListOf<GraphicElement>()
    for (day in 1 until AD_DAYS.size) {
        val leftX = VARIATION_GRID_LEFT + categoryWidth * day - barPadding
        val rightX = leftX + barPadding * 2
        var leftY = VARIATION_GRID_TOP + gridHeight
        var rightY = leftY
        for (channel in AD_CHANNELS.indices) {
            val leftHeight = AD_COUNTS[channel][day - 1] / AD_TOTALS[day - 1] * gridHeight
            val rightHeight = AD_COUNTS[channel][day] / AD_TOTALS[day] * gridHeight
            shapes +=
                unsafeJso<GraphicElement> {
                    type = "polygon"
                    shape =
                        unsafeJso<ZRShapeProps> {
                            points =
                                arrayOf(
                                    arrayOf(leftX, leftY),
                                    arrayOf(leftX, leftY - leftHeight),
                                    arrayOf(rightX, rightY - rightHeight),
                                    arrayOf(rightX, rightY),
                                    arrayOf(leftX, leftY),
                                )
                        }
                    style =
                        unsafeJso<ZRStyleProps> {
                            fill = ZRColor(AD_POLYGON_COLORS[channel])
                            opacity = 0.25
                        }
                }
            leftY -= leftHeight
            rightY -= rightHeight
        }
    }
    return shapes.toTypedArray()
}

/** A bar value of the waterfall pages, where `"-"` marks the month without income or expense. */
private fun waterfallValues(vararg values: Any?): Array<BarDataItem?> =
    Array(values.size) { index ->
        when (val value = values[index]) {
            is Number -> BarDataItem(OptionDataValue(value.toDouble()))
            is String -> BarDataItem(OptionDataValue(value))
            else -> BarDataItem(OptionDataValue("-"))
        }
    }

/** The transparent style that hides the placeholder series of `bar-waterfall2`. */
private fun invisibleStyle(): ItemStyleOption =
    unsafeJso<ItemStyleOption> {
        borderColor = ZRColor("transparent")
        color = ZRColor("transparent")
    }

/**
 * The tooltip of `bar-waterfall2`.
 *
 * With `trigger: 'axis'` every stacked segment arrives, including the transparent placeholder, so the
 * original picks the income entry and falls back to the expense entry of the same day.
 */
private fun waterfallTooltip(): TooltipFormatter =
    TooltipFormatter(
        { params ->
            val segments = params.unsafeCast<Array<CallbackDataParams>>()
            val income = segments.getOrNull(1)
            val shown = if (income != null && textOf(income.value) != "-") income else segments.getOrNull(2)
            TooltipContent(
                shown?.let { "${it.name}<br/>${it.seriesName.orEmpty()} : ${textOf(it.value)}" } ?: "",
            )
        },
    )

/** One series of `bar-stack-borderRadius`, with the corner radius of every single bar. */
private fun radiusSeries(index: Int): BarSeriesOption =
    barSeries {
        name = RADIUS_NAMES[index]
        stack = RADIUS_STACKS[index]
        val ends = RADIUS_STACK_ENDS.getValue(RADIUS_STACKS[index])
        typedData =
            Array(RADIUS_ROWS[index].size) { day ->
                val radius = if (ends[day] == index) STACK_CORNER_RADIUS else 0.0
                BarDataItem(
                    unsafeJso<BarDataItemOption> {
                        value = radiusValue(RADIUS_ROWS[index][day])
                        itemStyle =
                            unsafeJso<ItemStyleOption> {
                                borderRadius =
                                    StringOrDoubleOrArray(
                                        arrayOf(
                                            StringOrDouble(radius),
                                            StringOrDouble(radius),
                                            StringOrDouble(0.0),
                                            StringOrDouble(0.0),
                                        ),
                                    )
                            }
                    },
                )
            }
    }

/** A raw [RADIUS_ROWS] value as the `value` of a data item; `"-"` stays a hole in the stack. */
private fun radiusValue(raw: Any?): OptionDataValue =
    when (raw) {
        is Number -> OptionDataValue(raw.toDouble())
        else -> OptionDataValue("-")
    }

val barLongTailEntries: List<CatalogEntry> =
    listOf(
        barLongTail.chart("bar1", "某地区蒸发量和降水量", "Rainfall and Evaporation") {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Rainfall vs Evaporation"
                            subtext = "Fake Data"
                        },
                    )
                tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.axis })
                legend = one(unsafeJso<LegendOption> { data = legendNames("Rainfall", "Evaporation") })
                toolbox = toolbox(dataView = true, magicType = true, restore = true, saveAsImage = true)
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            data = strings(*RAINFALL_MONTHS)
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            name = "Rainfall"
                            typedData = numbers(*RAINFALL_VALUES)
                            markPoint =
                                unsafeJso<MarkPointOption> {
                                    data =
                                        arrayOf(
                                            unsafeJso<MarkPointDataItemOption> {
                                                name = "Max"
                                                type = MarkerStatisticTypes.max
                                            },
                                            unsafeJso<MarkPointDataItemOption> {
                                                name = "Min"
                                                type = MarkerStatisticTypes.min
                                            },
                                        )
                                }
                            markLine = averageMarkLine()
                        },
                        barSeries {
                            name = "Evaporation"
                            typedData = numbers(*EVAPORATION_VALUES)
                            markPoint =
                                unsafeJso<MarkPointOption> {
                                    data =
                                        arrayOf(
                                            unsafeJso<MarkPointDataItemOption> {
                                                name = "Max"
                                                value = StringOrDouble(182.2)
                                                xAxis = ScaleDataValue(7.0)
                                                yAxis = ScaleDataValue(183.0)
                                            },
                                            unsafeJso<MarkPointDataItemOption> {
                                                name = "Min"
                                                value = StringOrDouble(2.3)
                                                xAxis = ScaleDataValue(11.0)
                                                yAxis = ScaleDataValue(3.0)
                                            },
                                        )
                                }
                            markLine = averageMarkLine()
                        },
                    )
            }
        },
        barLongTail.chart(
            "bar-stack-normalization",
            "堆叠柱状图的归一化",
            "Stacked Bar Normalization",
            "五个投放渠道按天堆叠，每一根柱子的高度是当天各渠道占总量的比例，标签把该比例写成百分数。",
            "Five advertising channels stacked per day, where every bar is the share of the daily total " +
                "and the label writes that share as a percentage.",
        ) {
            val (xAxisOption, yAxisOption) = normalizationAxes()
            unsafeJso<EChartsOption> {
                legend = one(normalizationLegend())
                xAxis = one(xAxisOption)
                yAxis = one(yAxisOption)
                series = normalizedSeriesList()
            }
        },
        barLongTail.interactive(
            "bar-stack-normalization-and-variation",
            "堆叠柱状图的归一化和变化",
            "Stacked Bar Normalization and Variation",
            "归一化堆叠柱之外再加一层 graphic 多边形，把相邻两天的同渠道色带连起来，形状按图表的实际宽高算出。",
            "The normalized stacks plus a graphic layer that connects the same channel between two " +
                "neighbouring days; the polygons are laid out from the real width and height of the chart.",
        ) { chart ->
            val (xAxisOption, yAxisOption) = normalizationAxes()
            chart.setOption(
                unsafeJso<EChartsOption> {
                    legend = one(normalizationLegend())
                    grid =
                        one(
                            unsafeJso<GridOption> {
                                left = StringOrDouble(VARIATION_GRID_LEFT)
                                right = StringOrDouble(VARIATION_GRID_RIGHT)
                                top = StringOrDouble(VARIATION_GRID_TOP)
                                bottom = StringOrDouble(VARIATION_GRID_BOTTOM)
                            },
                        )
                    xAxis = one(xAxisOption)
                    yAxis = one(yAxisOption)
                    series = normalizedSeriesList()
                    graphic =
                        one(
                            GraphicComponentLooseOption(
                                unsafeJso<GraphicComponentOption> {
                                    elements = variationPolygons(chart.getWidth(), chart.getHeight())
                                },
                            ),
                        )
                },
            )
        },
        barLongTail.chart(
            "bar-stack-borderRadius",
            "带圆角的堆积柱状图",
            "Stacked Bar with borderRadius",
            "两条堆叠序列里留着 `-` 空位，只有真正封住栈顶的那根柱子带圆角，圆角写在每个数据项的 itemStyle 上。",
            "Two stack groups with `-` holes: only the bar that really closes a stack gets its corner " +
                "radius, which is written on the item style of that single data item.",
        ) {
            unsafeJso<EChartsOption> {
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            data = strings(*AD_DAYS)
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                series = Array<SeriesOption>(RADIUS_NAMES.size) { index -> radiusSeries(index) }
            }
        },
        barLongTail.chart(
            "bar-waterfall2",
            "阶梯瀑布图（柱状图模拟）",
            "Waterfall Chart",
            "一段透明的占位柱顶起收入与支出，tooltip 只报出当天可见的那一段。",
            "A transparent placeholder lifts each income and expense segment, and the tooltip names only " +
                "the visible segment of that day.",
        ) {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Accumulated Waterfall Chart" })
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.shadow }
                            formatter = waterfallTooltip()
                        },
                    )
                legend = one(unsafeJso<LegendOption> { data = legendNames("Expenses", "Income") })
                grid =
                    one(
                        unsafeJso<GridOption> {
                            left = StringOrDouble("3%")
                            right = StringOrDouble("4%")
                            bottom = StringOrDouble("3%")
                            containLabel = true
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            data = strings(*WATERFALL_DAYS)
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            name = "Placeholder"
                            stack = "Total"
                            silent = true
                            itemStyle = invisibleStyle()
                            emphasis = unsafeJso<EmphasisOption> { itemStyle = invisibleStyle() }
                            typedData = numbers(*WATERFALL_PLACEHOLDERS)
                        },
                        barSeries {
                            name = "Income"
                            stack = "Total"
                            label =
                                unsafeJso<LabelOption> {
                                    show = true
                                    position = LabelPosition("top")
                                }
                            typedData =
                                waterfallValues(
                                    900, 345, 393, "-", "-", 135, 178, 286, "-", "-", "-",
                                )
                        },
                        barSeries {
                            name = "Expenses"
                            stack = "Total"
                            label =
                                unsafeJso<LabelOption> {
                                    show = true
                                    position = LabelPosition("bottom")
                                }
                            typedData =
                                waterfallValues(
                                    "-", "-", "-", 108, 154, "-", "-", "-", 119, 361, 203,
                                )
                        },
                    )
            }
        },
    )
