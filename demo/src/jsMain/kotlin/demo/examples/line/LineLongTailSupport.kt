package demo.examples.line

import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.Date
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import kotlin.math.PI
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
import demo.examples.funnel.labels
import demo.examples.gauge.center
import demo.examples.matrix.branch
import demo.examples.matrix.cell
import demo.examples.matrix.names
import demo.examples.scatter.point
import demo.examples.shared.Lcg
import demo.examples.shared.every
import demo.examples.shared.textOf
import demo.examples.shared.valueText

/** Columns of `line-gradient`: the date and the value of one day. */
internal val LINE_GRADIENT_DATA =
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
internal class StackBand(
    val name: String,
    val from: String,
    val to: String,
    val values: DoubleArray,
    val showLabel: Boolean = false,
)

/** The five bands of `area-stack-gradient`, in the upstream order. */
internal val STACK_BANDS =
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
internal class Easing(val name: String, val valueAt: (Double) -> Double)

/** Bounce easing: the falling branch, which the other two are computed from. */
internal fun bounceOut(k: Double): Double =
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
internal fun bounceIn(k: Double): Double = 1 - bounceOut(1 - k)

/** The elastic branch with `a` forced to `1`, which is the one zrender always takes. */
internal fun elasticWave(k: Double, exponent: Double): Double {
    val period = 0.4
    val amplitude = 1.0
    val shift = period / 4
    return amplitude * 2.0.pow(exponent * k) * sin((k - shift) * (2 * PI) / period)
}

/** The 31 easing functions sampled by `line-easing`, in the upstream order. */
internal val EASINGS =
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
internal const val TIME_AXIS_POINT_COUNT = 20000

/** One day in milliseconds. */
internal const val ONE_DAY = 24 * 3600 * 1000.0

/**
 * The random walk of `area-time-axis`: one step per day from 1988-10-03, rounded to whole values.
 *
 * The first point keeps the unrounded `Math.random() * 300` of the original.
 */
internal fun timeAxisPoints(): Array<DoubleArray> {
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
internal fun shadedBetween(greaterThan: Double, lessThan: Double): VisualPiece =
    unsafeJso<VisualPiece> {
        gt = greaterThan
        lt = lessThan
        color = StringOrStringArray("rgba(0, 0, 180, 0.4)")
    }

/** A vertical mark line at one category index; it hides the edges and the label of `area-pieces`. */
internal fun categoryBoundary(index: Double): MarkLineDataItem =
    MarkLineDataItem(unsafeJso<MarkLine1DDataItemOption> { xAxis = StringOrDouble(index) })

/** The twelve months of one year, the category labels of `multiple-x-axis`. */
internal fun monthsOf(year: String): Array<String> = Array(12) { index -> "$year-${index + 1}" }

/**
 * One category axis of `multiple-x-axis`.
 *
 * Its own line takes the color of the series drawn on the *other* axis, as upstream does, and its
 * pointer labels itself with the month plus the value under the pointer.
 */
internal fun precipitationAxis(months: Array<String>, lineColor: ZRColor): XAxisOption =
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
internal fun precipitationLabel(): LabelFormatter =
    LabelFormatter(
        { params ->
            val pointer = params.unsafeCast<PointerLabelParams>()
            val value = pointer.seriesData?.firstOrNull()?.data
            "Precipitation  " + textOf(pointer.value) + if (value == null) "" else "：" + valueText(value)
        },
    )

/** Params of an axis-pointer label formatter, narrowed to the fields `multiple-x-axis` reads. */
internal external interface PointerLabelParams {
    val value: JsAny?
    val seriesData: Array<PointerSeriesData>?
}

/** One series item under the axis pointer; `data` is the raw value of the item. */
internal external interface PointerSeriesData {
    val data: JsAny?
}

/** A band of `area-stack-gradient`: an invisible line and the gradient fill under it. */
internal fun stackBand(band: StackBand): SeriesOption =
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
internal class DynamicDataWindow(seed: Int) {
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
internal fun dynamicDataTooltip(): TooltipFormatter =
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
internal class EasingCell(
    val grid: GridOption,
    val xAxis: XAxisOption,
    val yAxis: YAxisOption,
    val series: SeriesOption,
    val title: TitleOption,
)

/** Builds one [EasingCell] and places it in the `columns` by `columns` mesh of `line-easing`. */
internal fun easingCell(easing: Easing, index: Int, columns: Int, rows: Int): EasingCell {
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
internal const val SAMPLE_COUNT = 30

/** The foods the bump chart ranks. */
internal val BUMP_NAMES =
    arrayOf("Orange", "Tomato", "Apple", "Sakana", "Banana", "Iwashi", "Snappy Fish", "Lemon", "Pasta")

/** The years of the bump chart, which are also its categories. */
internal val BUMP_YEARS = arrayOf("2001", "2002", "2003", "2004", "2005", "2006")

/**
 * The ranks of one year, shuffled out of the natural order.
 *
 * This is the Fisher-Yates loop of the original, including the order in which it decrements the
 * index; [Lcg] replaces `Math.random()` so a visit always draws the same chart.
 */
internal fun bumpYearOrder(random: Lcg): MutableList<Int> {
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
internal fun bumpRankings(): Array<DoubleArray> {
    val random = Lcg(20060101)
    val orders = Array(BUMP_YEARS.size) { bumpYearOrder(random) }
    return Array(BUMP_NAMES.size) { name -> DoubleArray(BUMP_YEARS.size) { year -> orders[year][name].toDouble() } }
}

/** One ranking line of the bump chart, with the food name written at its end. */
internal fun bumpSeries(
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
internal fun graphicPanel(
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
internal fun graphicCaption(
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
internal fun graphicBanner(): GraphicElement =
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
internal fun graphicExplanation(): GraphicElement =
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
