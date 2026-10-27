package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.Date
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import kotlin.math.PI
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sin
import kotlin.math.sqrt
import web.cssom.FontWeight
import zrender.ZRColor
import zrender.ZRFontWeight
import zrender.ZRShapeProps
import zrender.ZRStyleProps
import zrender.TextOverflows
import zrender.gradientColorStop
import zrender.linearGradient

/*
 * The line/area pages that generate their data or assemble their layout at run time.
 *
 * `area-time-axis` plots twenty thousand points, `line-easing` builds one grid per easing function of
 * zrender, `dynamic-data2` shifts its window once a second, `bump-chart` shuffles a ranking every
 * visit; the rest are option combinations of the family: a piecewise visual map painted over an area,
 * two x axes carrying two years, two continuous visual maps on two grids, a stacked area filled by a
 * gradient, and the `graphic` overlay page of the original gallery.
 *
 * Changed on purpose:
 *
 * - `Math.random()` of `area-time-axis`, `dynamic-data2` and `bump-chart` is replaced by [Lcg]: the
 *   curves and the ranking still look random, but a page renders the same one every time;
 * - `area-time-axis` keeps the misspelled title `Large Ara Chart` of the original;
 * - `line-easing` inlines the elastic branch that zrender always takes (`a < 1` forces `a = 1`), and
 *   computes the percentage geometry instead of re-reading it with `parseFloat` as upstream does;
 * - `multiple-x-axis` reads the raw value of the first series under the pointer, which upstream calls
 *   `params.seriesData[0].data`; that field has no public type upstream, so it is narrowed here.
 */

private val lineLongTail = Category(Groups.LINE, "examples/LineLongTail.kt")

/** Columns of `line-gradient`: the date and the value of one day. */
private val LINE_GRADIENT_DATA =
    arrayOf(
        "2000-06-05" to 116.0, "2000-06-06" to 129.0, "2000-06-07" to 135.0, "2000-06-08" to 86.0,
        "2000-06-09" to 73.0, "2000-06-10" to 85.0, "2000-06-11" to 73.0, "2000-06-12" to 68.0,
        "2000-06-13" to 92.0, "2000-06-14" to 130.0, "2000-06-15" to 245.0, "2000-06-16" to 139.0,
        "2000-06-17" to 115.0, "2000-06-18" to 111.0, "2000-06-19" to 309.0, "2000-06-20" to 206.0,
        "2000-06-21" to 137.0, "2000-06-22" to 128.0, "2000-06-23" to 85.0, "2000-06-24" to 94.0,
        "2000-06-25" to 71.0, "2000-06-26" to 106.0, "2000-06-27" to 84.0, "2000-06-28" to 93.0,
        "2000-06-29" to 85.0, "2000-06-30" to 73.0, "2000-07-01" to 83.0, "2000-07-02" to 125.0,
        "2000-07-03" to 107.0, "2000-07-04" to 82.0, "2000-07-05" to 44.0, "2000-07-06" to 72.0,
        "2000-07-07" to 106.0, "2000-07-08" to 107.0, "2000-07-09" to 66.0, "2000-07-10" to 91.0,
        "2000-07-11" to 92.0, "2000-07-12" to 113.0, "2000-07-13" to 107.0, "2000-07-14" to 131.0,
        "2000-07-15" to 111.0, "2000-07-16" to 64.0, "2000-07-17" to 69.0, "2000-07-18" to 88.0,
        "2000-07-19" to 77.0, "2000-07-20" to 83.0, "2000-07-21" to 111.0, "2000-07-22" to 57.0,
        "2000-07-23" to 55.0, "2000-07-24" to 60.0,
    )

/** One band of `area-stack-gradient`: a name, the colors of the gradient and the seven daily values. */
private class StackBand(
    val name: String,
    val from: String,
    val to: String,
    val values: DoubleArray,
    val showLabel: Boolean = false,
)

/** The five bands of `area-stack-gradient`, in the upstream order. */
private val STACK_BANDS =
    arrayOf(
        StackBand("Line 1", "rgb(128, 255, 165)", "rgb(1, 191, 236)",
            doubleArrayOf(140.0, 232.0, 101.0, 264.0, 90.0, 340.0, 250.0)),
        StackBand("Line 2", "rgb(0, 221, 255)", "rgb(77, 119, 255)",
            doubleArrayOf(120.0, 282.0, 111.0, 234.0, 220.0, 340.0, 310.0)),
        StackBand("Line 3", "rgb(55, 162, 255)", "rgb(116, 21, 219)",
            doubleArrayOf(320.0, 132.0, 201.0, 334.0, 190.0, 130.0, 220.0)),
        StackBand("Line 4", "rgb(255, 0, 135)", "rgb(135, 0, 157)",
            doubleArrayOf(220.0, 402.0, 231.0, 134.0, 190.0, 230.0, 120.0)),
        StackBand("Line 5", "rgb(255, 191, 0)", "rgb(224, 62, 76)",
            doubleArrayOf(220.0, 302.0, 181.0, 234.0, 210.0, 290.0, 150.0), showLabel = true),
    )

/**
 * A zrender easing function: maps the elapsed fraction `k` of an animation to its value.
 *
 * The arithmetic is the one of the upstream example, including the `k *= 2` / `k -= 2` steps, which
 * are spelled out as local values because Kotlin has no assignment inside an expression.
 */
private class Easing(val name: String, val valueAt: (Double) -> Double)

/** Bounce easing: the falling branch, which the other two are computed from. */
private fun bounceOut(k: Double): Double =
    when {
        k < 1 / 2.75 -> 7.5625 * k * k
        k < 2 / 2.75 -> {
            val shifted = k - 1.5 / 2.75
            7.5625 * shifted * shifted + 0.75
        }
        k < 2.5 / 2.75 -> {
            val shifted = k - 2.25 / 2.75
            7.5625 * shifted * shifted + 0.9375
        }
        else -> {
            val shifted = k - 2.625 / 2.75
            7.5625 * shifted * shifted + 0.984375
        }
    }

/** Bounce easing: the rising branch, the mirror image of [bounceOut]. */
private fun bounceIn(k: Double): Double = 1 - bounceOut(1 - k)

/** The elastic branch with `a` forced to `1`, which is the one zrender always takes. */
private fun elasticWave(k: Double, exponent: Double): Double {
    val period = 0.4
    val amplitude = 1.0
    val shift = period / 4
    return amplitude * 2.0.pow(exponent * k) * sin((k - shift) * (2 * PI) / period)
}

/** The 31 easing functions sampled by `line-easing`, in the upstream order. */
private val EASINGS =
    arrayOf(
        Easing("linear") { k -> k },
        Easing("quadraticIn") { k -> k * k },
        Easing("quadraticOut") { k -> k * (2 - k) },
        Easing("quadraticInOut") { k0 ->
            val doubled = k0 * 2
            if (doubled < 1) 0.5 * doubled * doubled
            else {
                val shifted = doubled - 1
                -0.5 * (shifted * (shifted - 2) - 1)
            }
        },
        Easing("cubicIn") { k -> k * k * k },
        Easing("cubicOut") { k0 ->
            val k = k0 - 1
            k * k * k + 1
        },
        Easing("cubicInOut") { k0 ->
            val doubled = k0 * 2
            if (doubled < 1) 0.5 * doubled * doubled * doubled
            else {
                val shifted = doubled - 2
                0.5 * (shifted * shifted * shifted + 2)
            }
        },
        Easing("quarticIn") { k -> k * k * k * k },
        Easing("quarticOut") { k0 ->
            val k = k0 - 1
            1 - k * k * k * k
        },
        Easing("quarticInOut") { k0 ->
            val doubled = k0 * 2
            if (doubled < 1) 0.5 * doubled * doubled * doubled * doubled
            else {
                val shifted = doubled - 2
                -0.5 * (shifted * shifted * shifted * shifted - 2)
            }
        },
        Easing("quinticIn") { k -> k * k * k * k * k },
        Easing("quinticOut") { k0 ->
            val k = k0 - 1
            k * k * k * k * k + 1
        },
        Easing("quinticInOut") { k0 ->
            val doubled = k0 * 2
            if (doubled < 1) 0.5 * doubled * doubled * doubled * doubled * doubled
            else {
                val shifted = doubled - 2
                0.5 * (shifted * shifted * shifted * shifted * shifted + 2)
            }
        },
        Easing("sinusoidalIn") { k -> 1 - cos((k * PI) / 2) },
        Easing("sinusoidalOut") { k -> sin((k * PI) / 2) },
        Easing("sinusoidalInOut") { k -> 0.5 * (1 - cos(PI * k)) },
        Easing("exponentialIn") { k -> if (k == 0.0) 0.0 else 1024.0.pow(k - 1) },
        Easing("exponentialOut") { k -> if (k == 1.0) 1.0 else 1 - 2.0.pow(-10 * k) },
        Easing("exponentialInOut") { k0 ->
            when {
                k0 == 0.0 -> 0.0
                k0 == 1.0 -> 1.0
                else -> {
                    val doubled = k0 * 2
                    if (doubled < 1) 0.5 * 1024.0.pow(doubled - 1) else 0.5 * (-2.0.pow(-10 * (doubled - 1)) + 2)
                }
            }
        },
        Easing("circularIn") { k -> 1 - sqrt(1 - k * k) },
        Easing("circularOut") { k0 ->
            val k = k0 - 1
            sqrt(1 - k * k)
        },
        Easing("circularInOut") { k0 ->
            val doubled = k0 * 2
            if (doubled < 1) -0.5 * (sqrt(1 - doubled * doubled) - 1)
            else {
                val shifted = doubled - 2
                0.5 * (sqrt(1 - shifted * shifted) + 1)
            }
        },
        Easing("elasticIn") { k0 ->
            when {
                k0 == 0.0 -> 0.0
                k0 == 1.0 -> 1.0
                else -> -elasticWave(k0 - 1, 10.0)
            }
        },
        Easing("elasticOut") { k0 ->
            when {
                k0 == 0.0 -> 0.0
                k0 == 1.0 -> 1.0
                else -> elasticWave(k0, -10.0) + 1
            }
        },
        Easing("elasticInOut") { k0 ->
            when {
                k0 == 0.0 -> 0.0
                k0 == 1.0 -> 1.0
                else -> {
                    val doubled = k0 * 2
                    if (doubled < 1) -0.5 * elasticWave(doubled - 1, 10.0)
                    else 0.5 * elasticWave(doubled - 1, -10.0) + 1
                }
            }
        },
        Easing("backIn") { k ->
            val overshoot = 1.70158
            k * k * ((overshoot + 1) * k - overshoot)
        },
        Easing("backOut") { k0 ->
            val overshoot = 1.70158
            val k = k0 - 1
            k * k * ((overshoot + 1) * k + overshoot) + 1
        },
        Easing("backInOut") { k0 ->
            val overshoot = 1.70158 * 1.525
            val doubled = k0 * 2
            if (doubled < 1) 0.5 * (doubled * doubled * ((overshoot + 1) * doubled - overshoot))
            else {
                val shifted = doubled - 2
                0.5 * (shifted * shifted * ((overshoot + 1) * shifted + overshoot) + 2)
            }
        },
        Easing("bounceIn", ::bounceIn),
        Easing("bounceOut", ::bounceOut),
        Easing("bounceInOut") { k ->
            if (k < 0.5) bounceIn(k * 2) * 0.5 else bounceOut(k * 2 - 1) * 0.5 + 0.5
        },
    )

/** Points of `area-time-axis`; the original draws 20000 of them. */
private const val TIME_AXIS_POINT_COUNT = 20000

/** One day in milliseconds. */
private const val ONE_DAY = 24 * 3600 * 1000.0

/**
 * The random walk of `area-time-axis`: one step per day from 1988-10-03, rounded to whole values.
 *
 * The first point keeps the unrounded `Math.random() * 300` of the original.
 */
private fun timeAxisPoints(): Array<DoubleArray> {
    val random = Lcg(19881003)
    var time = Date(1988, 9, 3).getTime()
    var previous = random.next() * 300
    val points = ArrayList<DoubleArray>(TIME_AXIS_POINT_COUNT)
    points += doubleArrayOf(time, previous)
    repeat(TIME_AXIS_POINT_COUNT - 1) {
        time += ONE_DAY
        previous = round((random.next() - 0.5) * 20 + previous)
        points += doubleArrayOf(time, previous)
    }
    return points.toTypedArray()
}

/** A visual-map band strictly between two category indices, the shading of `area-pieces`. */
private fun shadedBetween(greaterThan: Double, lessThan: Double): VisualPiece =
    unsafeJso<VisualPiece> {
        gt = greaterThan
        lt = lessThan
        color = StringOrStringArray("rgba(0, 0, 180, 0.4)")
    }

/** A vertical mark line at one category index; it hides the edges and the label of `area-pieces`. */
private fun categoryBoundary(index: Double): MarkLineDataItem =
    MarkLineDataItem(unsafeJso<MarkLine1DDataItemOption> { xAxis = StringOrDouble(index) })

/** The twelve months of one year, the category labels of `multiple-x-axis`. */
private fun monthsOf(year: String): Array<String> = Array(12) { index -> "$year-${index + 1}" }

/**
 * One category axis of `multiple-x-axis`.
 *
 * Its own line takes the color of the series drawn on the *other* axis, as upstream does, and its
 * pointer labels itself with the month plus the value under the pointer.
 */
private fun precipitationAxis(months: Array<String>, lineColor: ZRColor): XAxisOption =
    unsafeJso {
        type = AxisTypes.category
        axisTick = unsafeJso<AxisTickOption> { alignWithLabel = true }
        axisLine =
            unsafeJso<AxisLineOption> {
                onZero = BooleanOrString(false)
                lineStyle = unsafeJso<LineStyleOption> { color = lineColor }
            }
        axisPointer =
            unsafeJso<AxisPointerOption> {
                label = unsafeJso<AxisPointerLabelOption> { formatter = precipitationLabel() }
            }
        data = strings(*months)
    }

/**
 * Label of the pointer of `multiple-x-axis`: the month and, when a series is under it, its value.
 *
 * Upstream reads `params.seriesData[0].data`, the raw value of the first item of the batch.
 */
private fun precipitationLabel(): LabelFormatter =
    LabelFormatter(
        { params ->
            val pointer = params.unsafeCast<PointerLabelParams>()
            val value = pointer.seriesData?.firstOrNull()?.data
            "Precipitation  " + textOf(pointer.value) + if (value == null) "" else "：" + valueText(value)
        },
    )

/** Params of an axis-pointer label formatter, narrowed to the fields `multiple-x-axis` reads. */
private external interface PointerLabelParams {
    val value: JsAny?
    val seriesData: Array<PointerSeriesData>?
}

/** One series item under the axis pointer; `data` is the raw value of the item. */
private external interface PointerSeriesData {
    val data: JsAny?
}

/** A band of `area-stack-gradient`: an invisible line and the gradient fill under it. */
private fun stackBand(band: StackBand): SeriesOption =
    lineSeries {
        name = band.name
        stack = "Total"
        smooth = BooleanOrDouble(true)
        lineStyle = unsafeJso<LineStyleOption> { width = 0.0 }
        showSymbol = false
        areaStyle =
            unsafeJso<AreaStyleOption> {
                opacity = 0.8
                color =
                    linearGradient(
                        0.0,
                        0.0,
                        0.0,
                        1.0,
                        arrayOf(gradientColorStop(0.0, band.from), gradientColorStop(1.0, band.to)),
                    )
            }
        emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }
        if (band.showLabel) {
            label = unsafeJso<LabelOption> { show = true; position = LabelPosition("top") }
        }
        typedData = numbers(*band.values)
    }

/**
 * The rolling window of `dynamic-data2`.
 *
 * The original keeps an array of a thousand `{ name, value: [date, value] }` items and moves it five
 * steps every second; [advance] and [data] are the two operations of that loop.
 */
private class DynamicDataWindow(seed: Int) {
    private val random = Lcg(seed)
    private var time = Date(1997, 9, 3).getTime()
    private var walkingValue = random.next() * 1000
    private val items = MutableList(ITEM_COUNT) { nextItem() }

    /** Drops the [count] oldest points and appends [count] new ones. */
    fun advance(count: Int) {
        repeat(count) {
            items.removeAt(0)
            items.add(nextItem())
        }
    }

    /** The window as series data, rebuilt so `setOption` sees a fresh array. */
    fun data(): Array<LineDataItem?> = items.toTypedArray()

    private fun nextItem(): LineDataItem {
        time += ONE_DAY
        walkingValue += random.next() * 21 - 10
        val now = Date(time)
        val label = "${now.getFullYear()}/${now.getMonth() + 1}/${now.getDate()}"
        val rounded = round(walkingValue)
        return LineDataItem(
            unsafeJso<LineDataItemOption> {
                name = OptionId(now.toString())
                typedValue = OptionDataValueOrArray(arrayOf(OptionDataValue(label), OptionDataValue(rounded)))
            },
        )
    }

    private companion object {
        /** Number of points of the window; the original keeps a thousand. */
        const val ITEM_COUNT = 1000
    }
}

/** Tooltip of `dynamic-data2`: the date of the pointer and the value of the series. */
private fun dynamicDataTooltip(): TooltipFormatter =
    TooltipFormatter(
        { params ->
            val item = params.unsafeCast<Array<CallbackDataParams>>()[0]
            val now = Date(textOf(item.name))
            val values = item.value.unsafeCast<Array<JsAny?>>()
            TooltipContent(
                "${now.getDate()}/${now.getMonth() + 1}/${now.getFullYear()} : " + textOf(values[1]),
            )
        },
    )

/**
 * A cell of the `line-easing` grid, laid out by the percentage math of the original.
 *
 * The title sits above the middle of its cell, so it carries the two numbers the layout needs.
 */
private class EasingCell(
    val grid: GridOption,
    val xAxis: XAxisOption,
    val yAxis: YAxisOption,
    val series: SeriesOption,
    val title: TitleOption,
)

/** Builds one [EasingCell] and places it in the `columns` by `columns` mesh of `line-easing`. */
private fun easingCell(easing: Easing, index: Int, columns: Int, rows: Int): EasingCell {
    val data = Array(SAMPLE_COUNT + 1) { step -> easing.valueAt(step.toDouble() / SAMPLE_COUNT) }
    val leftPercent = (index % columns).toDouble() / columns * 100 + 0.5
    val topPercent = floor(index / columns.toDouble()) / rows * 100 + 0.5
    val sizePercent = 100.0 / columns - 1
    val grid =
        unsafeJso<GridOption> {
            show = true
            borderWidth = 0.0
            shadowColor = "rgba(0, 0, 0, 0.3)"
            shadowBlur = 2.0
            left = StringOrDouble("$leftPercent%")
            top = StringOrDouble("$topPercent%")
            width = StringOrDouble("$sizePercent%")
            height = StringOrDouble("$sizePercent%")
        }
    return EasingCell(
        grid = grid,
        xAxis =
            unsafeJso<XAxisOption> {
                type = AxisTypes.value
                show = false
                min = AxisMinMax(0.0)
                max = AxisMinMax(1.0)
                gridIndex = index.toDouble()
            },
        yAxis =
            unsafeJso<YAxisOption> {
                type = AxisTypes.value
                show = false
                min = AxisMinMax(-0.4)
                max = AxisMinMax(1.4)
                gridIndex = index.toDouble()
            },
        series =
            lineSeries {
                name = easing.name
                xAxisIndex = index.toDouble()
                yAxisIndex = index.toDouble()
                typedData = points(*Array(data.size) { step -> doubleArrayOf(step.toDouble() / SAMPLE_COUNT, data[step]) })
                showSymbol = false
                animationEasing = AnimationEasing(easing.name)
                animationDuration = AnimationDuration(1000.0)
            },
        title =
            unsafeJso<TitleOption> {
                textAlign = "center"
                text = easing.name
                left = StringOrDouble("${leftPercent + sizePercent / 2}%")
                top = StringOrDouble("$topPercent%")
                textStyle =
                    unsafeJso<LabelOption> {
                        fontSize = StringOrDouble(12.0)
                        fontWeight = ZRFontWeight(FontWeight.normal)
                    }
            },
    )
}

/** Number of samples of one easing curve of `line-easing`. */
private const val SAMPLE_COUNT = 30

/** The foods the bump chart ranks. */
private val BUMP_NAMES =
    arrayOf("Orange", "Tomato", "Apple", "Sakana", "Banana", "Iwashi", "Snappy Fish", "Lemon", "Pasta")

/** The years of the bump chart, which are also its categories. */
private val BUMP_YEARS = arrayOf("2001", "2002", "2003", "2004", "2005", "2006")

/**
 * The ranks of one year, shuffled out of the natural order.
 *
 * This is the Fisher-Yates loop of the original, including the order in which it decrements the
 * index; [Lcg] replaces `Math.random()` so a visit always draws the same chart.
 */
private fun bumpYearOrder(random: Lcg): MutableList<Int> {
    val order = (1..BUMP_NAMES.size).toMutableList()
    var currentIndex = order.size
    while (currentIndex > 0) {
        val randomIndex = floor(random.next() * currentIndex).toInt()
        currentIndex--
        val held = order[currentIndex]
        order[currentIndex] = order[randomIndex]
        order[randomIndex] = held
    }
    return order
}

/** `[name][year]` ranks of the bump chart. */
private fun bumpRankings(): Array<DoubleArray> {
    val random = Lcg(20060101)
    val orders = Array(BUMP_YEARS.size) { bumpYearOrder(random) }
    return Array(BUMP_NAMES.size) { name -> DoubleArray(BUMP_YEARS.size) { year -> orders[year][name].toDouble() } }
}

/** One ranking line of the bump chart, with the food name written at its end. */
private fun bumpSeries(
    lineName: String,
    ranks: DoubleArray,
): LineSeriesOption =
    lineSeries {
        name = lineName
        symbolSize = SymbolSize(20.0)
        smooth = BooleanOrDouble(true)
        emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }
        endLabel =
            unsafeJso<LabelOption> {
                show = true
                formatter = LabelFormatter("{a}")
                distance = 20.0
            }
        lineStyle = unsafeJso<LineStyleOption> { width = 4.0 }
        typedData = numbers(*ranks)
    }

/**
 * The centered rectangle of a `line-graphic` group.
 *
 * [verticalPosition] is `"center"` or `"middle"`, as upstream; the parameter names stay clear of the
 * receiver's own `width`, `height`, `top` and `style` members, which would shadow them.
 */
private fun graphicPanel(
    panelWidth: Double,
    panelHeight: Double,
    verticalPosition: String,
    panelStyle: ZRStyleProps,
): GraphicElement =
    unsafeJso<GraphicElement> {
        type = "rect"
        z = 100.0
        left = StringOrDouble("center")
        top = StringOrDouble(verticalPosition)
        shape = unsafeJso<ZRShapeProps> {
            width = panelWidth
            height = panelHeight
        }
        style = panelStyle
    }

/** The centered text of a `line-graphic` group; see [graphicPanel] for [verticalPosition]. */
private fun graphicCaption(
    verticalPosition: String,
    captionStyle: ZRStyleProps,
): GraphicElement =
    unsafeJso<GraphicElement> {
        type = "text"
        z = 100.0
        left = StringOrDouble("center")
        top = StringOrDouble(verticalPosition)
        style = captionStyle
    }

/** The rotated banner of `line-graphic`, drawn over the plot at forty-five degrees. */
private fun graphicBanner(): GraphicElement =
    unsafeJso<GraphicElement> {
        type = "group"
        rotation = PI / 4
        bounding = GraphicBoundings.raw
        right = StringOrDouble(110.0)
        bottom = StringOrDouble(110.0)
        z = 100.0
        children =
            arrayOf(
                graphicPanel(
                    400.0,
                    50.0,
                    "center",
                    unsafeJso<ZRStyleProps> { fill = ZRColor("rgba(0,0,0,0.3)") },
                ),
                graphicCaption(
                    "center",
                    unsafeJso<ZRStyleProps> {
                        fill = ZRColor("#fff")
                        text = "ECHARTS LINE CHART"
                        font = "bold 26px sans-serif"
                    },
                ),
            )
    }

/** The text box of `line-graphic`, which explains the axes. */
private fun graphicExplanation(): GraphicElement =
    unsafeJso<GraphicElement> {
        type = "group"
        left = StringOrDouble("10%")
        top = StringOrDouble("center")
        children =
            arrayOf(
                graphicPanel(
                    240.0,
                    90.0,
                    "middle",
                    unsafeJso<ZRStyleProps> {
                        fill = ZRColor("#fff")
                        stroke = ZRColor("#555")
                        lineWidth = 1.0
                        shadowBlur = 8.0
                        shadowOffsetX = 3.0
                        shadowOffsetY = 3.0
                        shadowColor = "rgba(0,0,0,0.2)"
                    },
                ),
                graphicCaption(
                    "middle",
                    unsafeJso<ZRStyleProps> {
                        fill = ZRColor("#333")
                        width = StringOrDouble(220.0)
                        overflow = TextOverflows.`break`
                        text =
                            "xAxis represents temperature in °C, yAxis represents altitude in km, " +
                            "An image watermark in the upper right, " +
                            "This text block can be placed in any place"
                        font = "14px Microsoft YaHei"
                    },
                ),
            )
    }

val lineLongTailEntries: List<CatalogEntry> =
    listOf(
        lineLongTail.chart("area-stack-gradient", "渐变堆叠面积图", "Gradient Stacked Area Chart") {
            unsafeJso<EChartsOption> {
                color =
                    arrayOf(
                        ZRColor("#80FFA5"),
                        ZRColor("#00DDFF"),
                        ZRColor("#37A2FF"),
                        ZRColor("#FF0087"),
                        ZRColor("#FFBF00"),
                    )
                title = one(unsafeJso<TitleOption> { text = "Gradient Stacked Area Chart" })
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer =
                                unsafeJso<AxisPointerOption> {
                                    type = AxisPointerTypes.cross
                                    label = unsafeJso<AxisPointerLabelOption> { backgroundColor = ZRColor("#6a7985") }
                                }
                        },
                    )
                legend = one(unsafeJso<LegendOption> { data = legendNames(*STACK_BANDS.map { it.name }.toTypedArray()) })
                toolbox = toolbox(saveAsImage = true)
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            boundaryGap = BooleanOrStringArray(false)
                            data = strings("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                series = STACK_BANDS.map { stackBand(it) }.toTypedArray()
            }
        },
        lineLongTail.chart("area-pieces", "折线图区域高亮", "Area Pieces") {
            unsafeJso<EChartsOption> {
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            boundaryGap = BooleanOrStringArray(false)
                            data =
                                strings(
                                    "2019-10-10", "2019-10-11", "2019-10-12", "2019-10-13", "2019-10-14",
                                    "2019-10-15", "2019-10-16", "2019-10-17", "2019-10-18",
                                )
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            boundaryGap = BooleanOrStringArray(arrayOf(StringOrDouble(0.0), StringOrDouble("30%")))
                        },
                    )
                // The visual map shades the area between the second and third and between the sixth
                // and seventh category; the mark lines draw the borders of those bands.
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            type = VisualMapTypes.piecewise
                            show = false
                            dimension = 0.0
                            seriesIndex = IndexOrIndexArray(0.0)
                            pieces = arrayOf(shadedBetween(1.0, 3.0), shadedBetween(5.0, 7.0))
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        lineSeries {
                            smooth = BooleanOrDouble(0.6)
                            symbol = SymbolShape("none")
                            lineStyle =
                                unsafeJso<LineStyleOption> {
                                    color = ZRColor("#5470C6")
                                    width = 5.0
                                }
                            markLine =
                                unsafeJso<MarkLineOption> {
                                    symbol = StringOrStringArray(arrayOf("none", "none"))
                                    label = unsafeJso<MarkLineLabelOption> { show = false }
                                    data = arrayOf(
                                        categoryBoundary(1.0),
                                        categoryBoundary(3.0),
                                        categoryBoundary(5.0),
                                        categoryBoundary(7.0),
                                    )
                                }
                            areaStyle = unsafeJso<AreaStyleOption> {}
                            typedData =
                                arrayOf(
                                    LineDataItem(row("2019-10-10", 200.0)),
                                    LineDataItem(row("2019-10-11", 560.0)),
                                    LineDataItem(row("2019-10-12", 750.0)),
                                    LineDataItem(row("2019-10-13", 580.0)),
                                    LineDataItem(row("2019-10-14", 250.0)),
                                    LineDataItem(row("2019-10-15", 300.0)),
                                    LineDataItem(row("2019-10-16", 450.0)),
                                    LineDataItem(row("2019-10-17", 300.0)),
                                    LineDataItem(row("2019-10-18", 100.0)),
                                )
                        },
                    )
            }
        },
        lineLongTail.chart("area-time-axis", "时间轴折线图", "Area Chart with Time Axis") {
            unsafeJso<EChartsOption> {
                // The title of the original is kept verbatim, including the misspelling.
                title = one(unsafeJso<TitleOption> { left = StringOrDouble("center"); text = "Large Ara Chart" })
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            // The tooltip follows the pointer horizontally and sits near the top.
                            position =
                                TooltipPosition(
                                    { point, _, _, _, _ ->
                                        TooltipPosition(arrayOf<StringOrDouble>(StringOrDouble(point[0]), StringOrDouble("10%")))
                                    },
                                )
                        },
                    )
                toolbox =
                    one(
                        unsafeJso<ToolboxOption> {
                            feature =
                                unsafeJso<ToolboxFeature> {
                                    // `yAxisIndex: 'none'` keeps the zoom horizontal.
                                    dataZoom = unsafeJso<ToolboxDataZoomFeatureOption> { yAxisIndex = ModelFinderIndexQuery("none") }
                                    restore = unsafeJso<ToolboxFeatureOption> {}
                                    saveAsImage = unsafeJso<SaveAsImageOption> {}
                                }
                        },
                    )
                xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.time; boundaryGap = BooleanOrStringArray(false) })
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            boundaryGap = BooleanOrStringArray(arrayOf(StringOrDouble(0.0), StringOrDouble("100%")))
                        },
                    )
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> { type = DataZoomTypes.inside; start = 0.0; end = 20.0 },
                        unsafeJso<DataZoomOption> { type = DataZoomTypes.slider; start = 0.0; end = 20.0 },
                    )
                series =
                    arrayOf<SeriesOption>(
                        lineSeries {
                            name = "Fake Data"
                            smooth = BooleanOrDouble(true)
                            symbol = SymbolShape("none")
                            areaStyle = unsafeJso<AreaStyleOption> {}
                            typedData = points(*timeAxisPoints())
                        },
                    )
            }
        },
        lineLongTail.chart("line-gradient", "折线图的渐变", "Line Gradient") {
            val dates = LINE_GRADIENT_DATA.map { it.first }.toTypedArray()
            val values = LINE_GRADIENT_DATA.map { it.second }.toDoubleArray()
            unsafeJso<EChartsOption> {
                // The first map colors by the value, the second by the position along the axis.
                visualMap =
                    arrayOf(
                        unsafeJso<VisualMapOption> {
                            show = false
                            type = VisualMapTypes.continuous
                            seriesIndex = IndexOrIndexArray(0.0)
                            min = 0.0
                            max = 400.0
                        },
                        unsafeJso<VisualMapOption> {
                            show = false
                            type = VisualMapTypes.continuous
                            seriesIndex = IndexOrIndexArray(1.0)
                            dimension = 0.0
                            min = 0.0
                            max = (LINE_GRADIENT_DATA.size - 1).toDouble()
                        },
                    )
                title =
                    arrayOf(
                        unsafeJso<TitleOption> { left = StringOrDouble("center"); text = "Gradient along the y axis" },
                        unsafeJso<TitleOption> {
                            top = StringOrDouble("55%")
                            left = StringOrDouble("center")
                            text = "Gradient along the x axis"
                        },
                    )
                tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.axis })
                xAxis =
                    arrayOf(
                        unsafeJso<XAxisOption> { data = strings(*dates) },
                        unsafeJso<XAxisOption> { data = strings(*dates); gridIndex = 1.0 },
                    )
                yAxis =
                    arrayOf(
                        unsafeJso<YAxisOption> {},
                        unsafeJso<YAxisOption> { gridIndex = 1.0 },
                    )
                grid =
                    arrayOf(
                        unsafeJso<GridOption> { bottom = StringOrDouble("60%") },
                        unsafeJso<GridOption> { top = StringOrDouble("60%") },
                    )
                series =
                    arrayOf<SeriesOption>(
                        lineSeries {
                            showSymbol = false
                            typedData = numbers(*values)
                        },
                        lineSeries {
                            showSymbol = false
                            typedData = numbers(*values)
                            xAxisIndex = 1.0
                            yAxisIndex = 1.0
                        },
                    )
            }
        },
        lineLongTail.chart("multiple-x-axis", "多 X 轴", "Multiple X Axes") {
            val colors = arrayOf(ZRColor("#5470C6"), ZRColor("#EE6666"))
            unsafeJso<EChartsOption> {
                color = colors
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.none
                            axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.cross }
                        },
                    )
                legend = one(unsafeJso<LegendOption> {})
                grid = one(unsafeJso<GridOption> { top = StringOrDouble(70.0); bottom = StringOrDouble(50.0) })
                xAxis =
                    arrayOf(
                        precipitationAxis(monthsOf("2016"), colors[1]),
                        precipitationAxis(monthsOf("2015"), colors[0]),
                    )
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                series =
                    arrayOf<SeriesOption>(
                        // The 2015 series is drawn on the second axis, the one labelled with 2015.
                        lineSeries {
                            name = "Precipitation(2015)"
                            xAxisIndex = 1.0
                            smooth = BooleanOrDouble(true)
                            emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }
                            typedData =
                                numbers(2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3)
                        },
                        lineSeries {
                            name = "Precipitation(2016)"
                            smooth = BooleanOrDouble(true)
                            emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }
                            typedData =
                                numbers(3.9, 5.9, 11.1, 18.7, 48.3, 69.2, 231.6, 46.6, 55.4, 18.4, 10.3, 0.7)
                        },
                    )
            }
        },
        lineLongTail.interactive("dynamic-data2", "动态数据 + 时间坐标轴", "Dynamic Data + Time Axis") { chart, holder ->
            val window = DynamicDataWindow(19971003)
            chart.setOption(
                unsafeJso<EChartsOption> {
                    title = one(unsafeJso<TitleOption> { text = "Dynamic Data & Time Axis" })
                    tooltip =
                        one(
                            unsafeJso<TooltipOption> {
                                trigger = TooltipTriggers.axis
                                formatter = dynamicDataTooltip()
                                axisPointer = unsafeJso<AxisPointerOption> { animation = BooleanOrString(false) }
                            },
                        )
                    xAxis =
                        one(
                            unsafeJso<XAxisOption> {
                                type = AxisTypes.time
                                splitLine = unsafeJso<SplitLineOption> { show = false }
                            },
                        )
                    yAxis =
                        one(
                            unsafeJso<YAxisOption> {
                                type = AxisTypes.value
                                boundaryGap = BooleanOrStringArray(arrayOf(StringOrDouble(0.0), StringOrDouble("100%")))
                                splitLine = unsafeJso<SplitLineOption> { show = false }
                            },
                        )
                    series =
                        arrayOf<SeriesOption>(
                            lineSeries {
                                name = "Fake Data"
                                showSymbol = false
                                typedData = window.data()
                            },
                        )
                },
            )
            // Every second the window drops its five oldest points and gains five new ones.
            holder.setInterval(
                {
                    window.advance(5)
                    chart.setOption(
                        unsafeJso<EChartsOption> {
                            series =
                                arrayOf<SeriesOption>(
                                    lineSeries { typedData = window.data() },
                                )
                        },
                    )
                },
                1000,
            )
        },
        lineLongTail.chart("line-easing", "缓动函数可视化", "Line Easing Visualizing", height = "44rem") {
            val columns = ceil(sqrt(EASINGS.size.toDouble())).toInt()
            val rows = ceil(EASINGS.size.toDouble() / columns).toInt()
            val cells = EASINGS.mapIndexed { index, easing -> easingCell(easing, index, columns, rows) }
            unsafeJso<EChartsOption> {
                title =
                    cells.map { it.title }.toTypedArray() +
                        unsafeJso<TitleOption> {
                            text = "Different Easing Functions"
                            top = StringOrDouble("bottom")
                            left = StringOrDouble("center")
                        }
                grid = cells.map { it.grid }.toTypedArray()
                xAxis = cells.map { it.xAxis }.toTypedArray()
                yAxis = cells.map { it.yAxis }.toTypedArray()
                series = cells.map { it.series }.toTypedArray()
            }
        },
        lineLongTail.chart(
            "bump-chart",
            "凹凸图",
            "Bump Chart (Ranking)",
            "九种食物在六年里的名次曲线：名次作 y 轴并倒序，每条线末端用 endLabel 写出自己的名字。",
            "The ranking lines of nine foods over six years: the y axis is the rank, inverted, and each " +
                "line writes its own name at its end with `endLabel`.",
        ) {
            val rankings = bumpRankings()
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Bump Chart (Ranking)" })
                tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.item })
                grid =
                    one(
                        unsafeJso<GridOption> {
                            left = StringOrDouble(30.0)
                            right = StringOrDouble(110.0)
                            bottom = StringOrDouble(30.0)
                            containLabel = true
                        },
                    )
                toolbox = toolbox(saveAsImage = true)
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            splitLine = unsafeJso<SplitLineOption> { show = true }
                            axisLabel = unsafeJso<AxisLabelOption> { margin = 30.0; fontSize = StringOrDouble(16.0) }
                            boundaryGap = BooleanOrStringArray(false)
                            data = strings(*BUMP_YEARS)
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            axisLabel =
                                unsafeJso<AxisLabelOption> {
                                    margin = 30.0
                                    fontSize = StringOrDouble(16.0)
                                    formatter = LabelFormatter("#{value}")
                                }
                            inverse = true
                            interval = AxisInterval(1.0)
                            min = AxisMinMax(1.0)
                            max = AxisMinMax(BUMP_NAMES.size.toDouble())
                        },
                    )
                series =
                    Array<SeriesOption>(BUMP_NAMES.size) { index ->
                        bumpSeries(BUMP_NAMES[index], rankings[index])
                    }
            }
        },
        lineLongTail.chart(
            "line-graphic",
            "自定义图形组件",
            "Custom Graphic Component",
            "折线之外用 graphic 画两块浮层：一块 45° 旋转的标题横幅，一个带阴影和自动换行的说明框。",
            "A line chart with two graphic overlays: a banner rotated by 45 degrees and a shadowed text " +
                "box that wraps inside its own width.",
        ) {
            unsafeJso<EChartsOption> {
                legend = one(unsafeJso<LegendOption> { data = legendNames("Altitude (km) vs Temperature (°C)") })
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            formatter = TooltipFormatter("Temperature : <br/>{b}km : {c}°C")
                        },
                    )
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
                            type = AxisTypes.value
                            axisLabel = unsafeJso<AxisLabelOption> { formatter = LabelFormatter("{value} °C") }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.category
                            axisLine = unsafeJso<AxisLineOption> { onZero = BooleanOrString(false) }
                            axisLabel = unsafeJso<AxisLabelOption> { formatter = LabelFormatter("{value} km") }
                            boundaryGap = BooleanOrStringArray(true)
                            data = strings("0", "10", "20", "30", "40", "50", "60", "70", "80")
                        },
                    )
                graphic = arrayOf(GraphicComponentLooseOption(graphicBanner()), GraphicComponentLooseOption(graphicExplanation()))
                series =
                    arrayOf<SeriesOption>(
                        lineSeries {
                            name = "高度(km)与气温(°C)变化关系"
                            smooth = BooleanOrDouble(true)
                            typedData = numbers(15.0, -50.0, -56.5, -46.5, -22.1, -2.5, -27.7, -55.7, -76.5)
                        },
                    )
            }
        },
    )
