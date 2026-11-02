package demo.examples.bar

import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.math.round
import zrender.ZRColor
import zrender.ZRShapeProps
import zrender.ZRStyleProps
import demo.examples.gl.channel
import demo.examples.matrix.branch
import demo.examples.matrix.names
import demo.examples.scatter.extremes
import demo.examples.shared.every
import demo.examples.shared.numeric
import demo.examples.shared.percent
import demo.examples.shared.textOf
import demo.examples.shared.valueText
import demo.examples.sunburst.radius
import demo.examples.themeriver.days
import demo.examples.tree.leaves

/** The months of `bar1`. */
internal val RAINFALL_MONTHS =
    arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

/** The monthly rainfall of `bar1`. */
internal val RAINFALL_VALUES =
    doubleArrayOf(2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3)

/** The monthly evaporation of `bar1`, whose extremes are written out instead of computed. */
internal val EVAPORATION_VALUES =
    doubleArrayOf(2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3)

/** The advertising channels stacked by the two normalization pages, in legend order. */
internal val AD_CHANNELS = arrayOf("Direct", "Mail Ad", "Affiliate Ad", "Video Ad", "Search Engine")

/** The days of the two normalization pages. */
internal val AD_DAYS = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

/** `[channel][day]` counts; the upstream comment warns that the table holds no negative value. */
internal val AD_COUNTS =
    arrayOf(
        doubleArrayOf(100.0, 302.0, 301.0, 334.0, 390.0, 330.0, 320.0),
        doubleArrayOf(320.0, 132.0, 101.0, 134.0, 90.0, 230.0, 210.0),
        doubleArrayOf(220.0, 182.0, 191.0, 234.0, 290.0, 330.0, 310.0),
        doubleArrayOf(150.0, 212.0, 201.0, 154.0, 190.0, 330.0, 410.0),
        doubleArrayOf(820.0, 832.0, 901.0, 934.0, 1290.0, 1330.0, 1320.0),
    )

/** The total of every day, the denominator of the normalization. */
internal val AD_TOTALS = Array(AD_DAYS.size) { day -> AD_COUNTS.sumOf { it[day] } }

/** The colors that tint the polygons between the normalized bars of the variation page. */
internal val AD_POLYGON_COLORS = arrayOf("#5070dd", "#b6d634", "#505372", "#ff994d", "#0ca8df")

/** The pixel margins of the grid of `bar-stack-normalization-and-variation`. */
internal const val VARIATION_GRID_LEFT = 100.0

/** See [VARIATION_GRID_LEFT]. */
internal const val VARIATION_GRID_RIGHT = 100.0

/** See [VARIATION_GRID_LEFT]. */
internal const val VARIATION_GRID_TOP = 50.0

/** See [VARIATION_GRID_LEFT]. */
internal const val VARIATION_GRID_BOTTOM = 90.0

/** Fraction of a category the stacked bars of the normalization pages occupy. */
internal const val NORMALIZED_BAR_RATIO = 0.6

/** Corner radius of the bar that closes a stack in `bar-stack-borderRadius`. */
internal const val STACK_CORNER_RADIUS = 20.0

/** The days of `bar-waterfall2`, numbered like the original. */
internal val WATERFALL_DAYS = (1..11).map { "Nov $it" }.toTypedArray()

/** The invisible base of the waterfall; the visible segment always starts on top of it. */
internal val WATERFALL_PLACEHOLDERS =
    doubleArrayOf(0.0, 900.0, 1245.0, 1530.0, 1376.0, 1376.0, 1511.0, 1689.0, 1856.0, 1495.0, 1292.0)

/** Raw values of `bar-stack-borderRadius`, where `"-"` is the hole the original leaves in a stack. */
internal val RADIUS_ROWS: Array<Array<Any?>> =
    arrayOf(
        arrayOf(120, 200, 150, 80, 70, 110, 130),
        arrayOf(10, 46, 64, "-", 0, "-", 0),
        arrayOf(30, "-", 0, 20, 10, "-", 0),
        arrayOf(30, "-", 0, 20, 10, "-", 0),
        arrayOf(10, 20, 150, 0, "-", 50, 10),
    )

/** The stack group every series of `bar-stack-borderRadius` belongs to. */
internal val RADIUS_STACKS = arrayOf("a", "a", "a", "b", "b")

/** The names of the series of `bar-stack-borderRadius`. */
internal val RADIUS_NAMES = arrayOf("a", "b", "c", "d", "e")

/**
 * Whether one raw value of [RADIUS_ROWS] contributes to its stack.
 *
 * Only numbers count, which reproduces the `data && data !== '-'` test of the original: `"-"` is
 * filtered explicitly and the value `0` falls to the same branch because it is falsy in JavaScript.
 */
internal fun radiusFilled(value: Any?): Boolean = value is Number && value.toDouble() != 0.0

/** For every stack of [RADIUS_STACKS], the index of the last series that contributes at every day. */
internal val RADIUS_STACK_ENDS: Map<String, IntArray> =
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
internal fun normalizedLabels(): LabelOption =
    unsafeJso<LabelOption> {
        show = true
        // valueText keeps the JavaScript formatting, which prints 30 where Kotlin would print 30.0.
        formatter = LabelFormatter({ params -> "${valueText(round(numeric(params.value) * 1000) / 10)}%" })
    }

/** One normalized stacked bar of the two `bar-stack-normalization*` pages. */
internal fun normalizedSeries(channel: Int): BarSeriesOption =
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
internal fun normalizedSeriesList(): Array<SeriesOption> =
    Array<SeriesOption>(AD_CHANNELS.size) { index -> normalizedSeries(index) }

/** The average mark line both series of `bar1` draw. */
internal fun averageMarkLine(): MarkLineOption =
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
internal fun normalizationAxes(): Pair<XAxisOption, YAxisOption> =
    unsafeJso<XAxisOption> {
        type = AxisTypes.category
        data = strings(*AD_DAYS)
    } to unsafeJso<YAxisOption> { type = AxisTypes.value }

/** The legend of the two normalization pages, which cannot toggle the channels. */
internal fun normalizationLegend(): LegendOption = unsafeJso { selectedMode = SelectionMode(false) }

/**
 * The polygons that connect two neighbouring normalized bars.
 *
 * Every band is drawn as one closed path per channel, from the top of the previous day's stack down
 * to the top of this day's; because the bars are only 60% wide, the band starts at the right edge of
 * one bar and ends at the left edge of the next.
 */
internal fun variationPolygons(width: Double, height: Double): Array<GraphicElement> {
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
internal fun waterfallValues(vararg values: Any?): Array<BarDataItem?> =
    Array(values.size) { index ->
        when (val value = values[index]) {
            is Number -> BarDataItem(OptionDataValue(value.toDouble()))
            is String -> BarDataItem(OptionDataValue(value))
            else -> BarDataItem(OptionDataValue("-"))
        }
    }

/** The transparent style that hides the placeholder series of `bar-waterfall2`. */
internal fun invisibleStyle(): ItemStyleOption =
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
internal fun waterfallTooltip(): TooltipFormatter =
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
internal fun radiusSeries(index: Int): BarSeriesOption =
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
internal fun radiusValue(raw: Any?): OptionDataValue =
    when (raw) {
        is Number -> OptionDataValue(raw.toDouble())
        else -> OptionDataValue("-")
    }
