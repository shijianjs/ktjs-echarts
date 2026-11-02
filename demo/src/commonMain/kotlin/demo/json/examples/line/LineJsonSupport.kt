package demo.json.examples.line

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.examples.dataset.row
import demo.json.examples.doc.hidden
import demo.json.examples.shared.fill
import demo.json.examples.shared.stroke
import demo.json.points

/** Days of the week, the category axis of most pages. */
internal val WEEK = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

/** The counterclockwise sectors of `line-polar`, one point every 3.6 degrees. */
internal const val POLAR_SPIRAL_STEPS = 100

/** The sectors of `line-polar2`, one point every degree. */
internal const val POLAR_ROSE_STEPS = 360

/** Points plotted by `area-simple` and `area-time-axis`; the originals draw 20000. */
internal const val AREA_POINT_COUNT = 2000

/** One day in milliseconds, the step of the time axis of `area-time-axis`. */
internal const val ONE_DAY = 86_400_000.0

/** First timestamp of `area-time-axis`: 1988-10-03 00:00 UTC. */
internal const val TIME_AXIS_START = 591_926_400_000.0

/** First day of `area-simple`, as in the original. */
internal const val AREA_SIMPLE_YEAR = 1968

/** See [AREA_SIMPLE_YEAR]. */
internal const val AREA_SIMPLE_MONTH = 10

/** See [AREA_SIMPLE_YEAR]. */
internal const val AREA_SIMPLE_DAY = 3

/** Samples of one easing curve of `line-easing`; every curve has `SAMPLE_COUNT + 1` points. */
internal const val SAMPLE_COUNT = 30

/** The foods `bump-chart` ranks. */
internal val BUMP_NAMES =
    arrayOf("Orange", "Tomato", "Apple", "Sakana", "Banana", "Iwashi", "Snappy Fish", "Lemon", "Pasta")

/** The years of `bump-chart`, which are also its category axis. */
internal val BUMP_YEARS = arrayOf("2001", "2002", "2003", "2004", "2005", "2006")

/** The dates of `line-gradient`, the category axis and the data of both panels. */
internal val LINE_GRADIENT_DATA =
    listOf(
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

/** The twelve label positions `line-markline` demonstrates, in the upstream order. */
internal val MARK_LINE_POSITIONS =
    arrayOf(
        "start",
        "middle",
        "end",
        "insideStart",
        "insideStartTop",
        "insideStartBottom",
        "insideMiddle",
        "insideMiddleTop",
        "insideMiddleBottom",
        "insideEnd",
        "insideEndTop",
        "insideEndBottom",
    )

/** `focus: 'series'` of the hovered series. */
internal fun focusSeries(): EmphasisOption = EmphasisOption().apply { focus = "series" }

/** An axis pointer of [pointerType]; the model has no builder for it. */
internal fun pointerOf(pointerType: String): AxisPointerOption =
    AxisPointerOption().apply { type = pointerType }

/** The `saveAsImage` toolbox most pages carry. */
internal fun EChartsOption.saveAsImageToolbox() {
    toolbox { feature = ToolboxFeatureOption().apply { saveAsImage = ToolboxSaveAsImageFeature() } }
}

/** A line style with only a stroke color. */
internal fun strokeOf(strokeColor: String): LineStyleOption =
    LineStyleOption().apply { color = strokeColor.asColor() }

/** The transparent-to-solid fill of one `area-stack-gradient` band. */
internal fun verticalGradient(from: String, to: String): EChartsColor =
    EChartsColor.LinearGradient(
        x = 0.0,
        y = 0.0,
        x2 = 0.0,
        y2 = 1.0,
        colorStops = listOf(GradientColorStop(0.0, from), GradientColorStop(1.0, to)),
    )

/** A `[label, value]` row, the shape of the dated line pages. */
internal fun datedValue(date: String, dateValue: Number): DataValue =
    arrayValue(textValue(date), valueOf(dateValue))

/** Days of [month] of [year], the leap rule of the Gregorian calendar. */
internal fun daysInMonth(year: Int, month: Int): Int =
    when (month) {
        2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
        4, 6, 9, 11 -> 30
        else -> 31
    }

/** [count] consecutive day labels starting at the given date, formatted by [format]. */
internal fun consecutiveDays(
    startYear: Int,
    startMonth: Int,
    startDay: Int,
    count: Int,
    format: (year: Int, month: Int, day: Int) -> String,
): List<DataValue> {
    var year = startYear
    var month = startMonth
    var day = startDay
    return List(count) {
        val label = textValue(format(year, month, day))
        day++
        if (day > daysInMonth(year, month)) {
            day = 1
            month++
            if (month > 12) {
                month = 1
                year++
            }
        }
        label
    }
}

/** The months of one year, the category axis of `multiple-x-axis`. */
internal fun monthsOf(year: String): List<DataValue> = (1..12).map { textValue("$year-$it") }

/**
 * A fixed-seed linear congruential generator, the same one the Kotlin pages use.
 *
 * It replaces the `Math.random()` sequences of `area-simple`, `area-time-axis`, `bump-chart` and
 * `line-tooltip-touch`, so a page draws the same picture on every visit and the two modes of the
 * switch plot identical curves.
 */
internal class LineRandom(seed: Int) {
    private var state = seed

    /** Next value in `0..1`. */
    fun next(): Double {
        state = state * 1664525 + 1013904223
        return (state ushr 1).toDouble() / 2147483648.0
    }
}

/** The random walk of `area-simple`: `round((random - 0.5) * 20 + previous)`. */
internal fun areaSimpleValues(): List<DataValue> {
    val random = LineRandom(19681003)
    var previous = random.next() * 300
    return List(AREA_POINT_COUNT) { index ->
        if (index > 0) previous = kotlin.math.round((random.next() - 0.5) * 20 + previous)
        valueOf(previous)
    }
}

/** The random walk of `area-time-axis`, one step per day from [TIME_AXIS_START]. */
internal fun timeAxisPoints(): List<DataValue> {
    val random = LineRandom(19881003)
    var previous = random.next() * 300
    return List(AREA_POINT_COUNT) { index ->
        if (index > 0) previous = kotlin.math.round((random.next() - 0.5) * 20 + previous)
        point(TIME_AXIS_START + index * ONE_DAY, previous)
    }
}

/** The `[r, theta]` spiral of `line-polar`: `r = 5 * (1 + sin(theta))`. */
internal fun polarSpiral(): List<DataValue> =
    (0..POLAR_SPIRAL_STEPS).map { index ->
        val theta = index / 100.0 * 360
        val radius = 5 * (1 + kotlin.math.sin(theta / 180 * kotlin.math.PI))
        point(radius, theta)
    }

/** The `[r, i]` rose of `line-polar2`: `r = sin(2t) * cos(2t)`. */
internal fun polarRose(): List<DataValue> =
    (0..POLAR_ROSE_STEPS).map { index ->
        val t = index / 180.0 * kotlin.math.PI
        point(kotlin.math.sin(2 * t) * kotlin.math.cos(2 * t), index)
    }

/** One nine-day walk of `line-tooltip-touch`; the category labels start at 2016-10-4. */
internal fun touchWalk(seed: Int, reset: Double): List<DataValue> {
    val random = LineRandom(seed)
    var value = random.next() * reset
    return List(9) { index ->
        value = kotlin.math.round((random.next() - 0.5) * 20 + value)
        if (value <= 0) value = random.next() * reset
        // Upstream joins the date parts without padding, so the labels stay as they are.
        datedValue("2016-10-${index + 4}", value)
    }
}

/** The ranks of `bump-chart`: a Fisher-Yates shuffle per year, out of a fixed seed. */
internal fun bumpRankings(): Array<DoubleArray> {
    val random = LineRandom(20060101)
    val orders =
        Array(BUMP_YEARS.size) {
            val order = MutableList(BUMP_NAMES.size) { index -> index + 1 }
            var current = order.size
            while (current > 0) {
                val randomIndex = kotlin.math.floor(random.next() * current).toInt()
                current--
                val held = order[current]
                order[current] = order[randomIndex]
                order[randomIndex] = held
            }
            order
        }
    return Array(BUMP_NAMES.size) { name ->
        DoubleArray(BUMP_YEARS.size) { year -> orders[year][name].toDouble() }
    }
}

/** `centered text that sits above one cell of the `line-easing` mesh. */
internal class EasingCell(
    /** Left edge of the cell, in percent. */
    val leftPercent: Double,
    /** Top edge of the cell, in percent. */
    val topPercent: Double,
    /** Width and height of the cell, in percent. */
    val sizePercent: Double,
)

/** Lays the easing panels out in a square mesh, as the original does. */
internal fun easingCells(count: Int): List<EasingCell> {
    val columns = kotlin.math.ceil(kotlin.math.sqrt(count.toDouble())).toInt()
    val rows = kotlin.math.ceil(count / columns.toDouble()).toInt()
    val size = 100.0 / columns - 1
    return List(count) { index ->
        EasingCell(
            leftPercent = (index % columns).toDouble() / columns * 100 + 0.5,
            topPercent = kotlin.math.floor(index / columns.toDouble()) / rows * 100 + 0.5,
            sizePercent = size,
        )
    }
}

/** Percent text rounded to two decimals, so the option stays readable. */
internal fun percentText(value: Double): String =
    "${kotlin.math.round(value * 100) / 100.0}%"

/** One easing function of `line-easing`. */
internal class Easing(val name: String, val at: (Double) -> Double)

/** `2 ^ exponent`, the base of the exponential easings. */
internal fun twoPow(exponent: Double): Double = kotlin.math.exp(exponent * kotlin.math.ln(2.0))

/** The elastic branch of the elastic easings; `a` is forced to `1`, as zrender always does. */
internal fun elasticWave(k: Double, exponent: Double): Double {
    val period = 0.4
    val shift = period / 4
    return twoPow(exponent * k) * kotlin.math.sin((k - shift) * (2 * kotlin.math.PI) / period)
}

/** Bounce easing, falling branch. */
internal fun bounceOutAt(k: Double): Double =
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

/** Bounce easing, rising branch. */
internal fun bounceInAt(k: Double): Double = 1 - bounceOutAt(1 - k)

/** The 31 easing functions `line-easing` samples, in the upstream order. */
internal val EASINGS =
    arrayOf(
        Easing("linear") { k -> k },
        Easing("quadraticIn") { k -> k * k },
        Easing("quadraticOut") { k -> k * (2 - k) },
        Easing("quadraticInOut") { k0 ->
            val doubled = k0 * 2
            if (doubled < 1) {
                0.5 * doubled * doubled
            } else {
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
            if (doubled < 1) {
                0.5 * doubled * doubled * doubled
            } else {
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
            if (doubled < 1) {
                0.5 * doubled * doubled * doubled * doubled
            } else {
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
            if (doubled < 1) {
                0.5 * doubled * doubled * doubled * doubled * doubled
            } else {
                val shifted = doubled - 2
                0.5 * (shifted * shifted * shifted * shifted * shifted + 2)
            }
        },
        Easing("sinusoidalIn") { k -> 1 - kotlin.math.cos((k * kotlin.math.PI) / 2) },
        Easing("sinusoidalOut") { k -> kotlin.math.sin((k * kotlin.math.PI) / 2) },
        Easing("sinusoidalInOut") { k -> 0.5 * (1 - kotlin.math.cos(kotlin.math.PI * k)) },
        Easing("exponentialIn") { k -> if (k == 0.0) 0.0 else twoPow(10 * (k - 1)) },
        Easing("exponentialOut") { k -> if (k == 1.0) 1.0 else 1 - twoPow(-10 * k) },
        Easing("exponentialInOut") { k0 ->
            when {
                k0 == 0.0 -> 0.0
                k0 == 1.0 -> 1.0
                else -> {
                    val doubled = k0 * 2
                    if (doubled < 1) {
                        0.5 * twoPow(10 * (doubled - 1))
                    } else {
                        0.5 * (-twoPow(-10 * (doubled - 1)) + 2)
                    }
                }
            }
        },
        Easing("circularIn") { k -> 1 - kotlin.math.sqrt(1 - k * k) },
        Easing("circularOut") { k0 ->
            val k = k0 - 1
            kotlin.math.sqrt(1 - k * k)
        },
        Easing("circularInOut") { k0 ->
            val doubled = k0 * 2
            if (doubled < 1) {
                -0.5 * (kotlin.math.sqrt(1 - doubled * doubled) - 1)
            } else {
                val shifted = doubled - 2
                0.5 * (kotlin.math.sqrt(1 - shifted * shifted) + 1)
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
                    if (doubled < 1) {
                        -0.5 * elasticWave(doubled - 1, 10.0)
                    } else {
                        0.5 * elasticWave(doubled - 1, -10.0) + 1
                    }
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
            if (doubled < 1) {
                0.5 * (doubled * doubled * ((overshoot + 1) * doubled - overshoot))
            } else {
                val shifted = doubled - 2
                0.5 * (shifted * shifted * ((overshoot + 1) * shifted + overshoot) + 2)
            }
        },
        Easing("bounceIn") { k -> bounceInAt(k) },
        Easing("bounceOut") { k -> bounceOutAt(k) },
        Easing("bounceInOut") { k ->
            if (k < 0.5) bounceInAt(k * 2) * 0.5 else bounceOutAt(k * 2 - 1) * 0.5 + 0.5
        },
    )

/** One shaded hour band of `line-sections`, written as the two `coord` corners of the area. */
internal fun hourBand(bandName: String, from: String, to: String): MarkAreaDataItem =
    MarkAreaDataItem.of(
        MarkAreaData().apply {
            name = bandName
            coord = listOf(str(from), num(0))
        },
        MarkAreaData().apply { coord = listOf(str(to), num(1000)) },
    )

/** One piece of the hidden visual map of `line-sections`; `null` leaves that bound open. */
internal fun hourPiece(greaterThan: Double?, atMost: Double?, fillColor: String): VisualMapPiece =
    VisualMapPiece().apply {
        if (greaterThan != null) gt = greaterThan
        if (atMost != null) lte = atMost
        color = fillColor.asColor()
    }

/** One shaded band of `area-pieces`: the categories strictly between both indices. */
internal fun shadedPiece(greaterThan: Double, lessThan: Double): VisualMapPiece =
    VisualMapPiece().apply {
        gt = greaterThan
        lt = lessThan
        color = "rgba(0, 0, 180, 0.4)".asColor()
    }

/** A vertical mark line on the category axis of `area-pieces`. */
internal fun categoryBoundary(index: Int): MarkLineDataItem =
    MarkLineDataItem.of(MarkLineData().apply { xAxis = num(index) })

/** The `{type: 'average', name: 'Avg'}` mark line of `line-marker`. */
internal fun averageMarkLine(): MarkLineDataItem =
    MarkLineDataItem.of(
        MarkLineData().apply {
            name = "Avg"
            type = "average"
        },
    )

/** One horizontal mark line of `line-markline`, labelled with its position name. */
internal fun horizontalMarkLine(positionName: String, axisValue: Double): MarkLineDataItem =
    MarkLineDataItem.of(
        MarkLineData().apply {
            name = positionName
            yAxis = num(axisValue)
            label =
                LabelOption().apply {
                    formatter = "{b}"
                    position = positionName
                }
        },
    )

/** One diagonal mark line of `line-markline`, drawn from `[0, 0.3]` to `[3, 1]`. */
internal fun diagonalMarkLine(positionName: String, labelText: String): MarkLineDataItem =
    MarkLineDataItem.of(
        MarkLineData().apply {
            name = "start: $positionName"
            coord = listOf(num(0), num(0.3))
            label =
                LabelOption().apply {
                    formatter = labelText
                    position = positionName
                }
        },
        MarkLineData().apply {
            name = "end: $positionName"
            coord = listOf(num(3), num(1))
        },
    )
