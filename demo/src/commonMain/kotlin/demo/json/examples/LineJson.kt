package demo.json.examples

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*

/*
 * Line and area examples carried over from `line-*.ts` and `area-*.ts` of
 * apache/echarts-examples.
 *
 * The pages keep the official ids, so the Kotlin/JS port and the JSON port of the same chart share
 * one route.
 *
 * Skipped ids, and why:
 *
 * - `line-function` and `line-graphic`: both stay Kotlin-only, as before. The first draws a curve
 *   produced by a closure and drives it with a `dataZoom` pair that only makes sense together with
 *   the generated points, the second is built from `graphic` elements rather than from a chart
 *   option; neither survives a JSON round trip.
 *
 * Compacted on purpose, because the model has no field for them:
 *
 * - `line-log` drops `yAxis.minorSplitLine` and `line-polar` / `line-polar2` drop the
 *   `startAngle: 0` of their angle axis, which `AxisOption` does not model;
 * - the tooltip `position` callbacks of `area-simple`, `area-time-axis` and `line-tooltip-touch`,
 *   the axis-pointer `label.formatter` of `multiple-x-axis` / `line-tooltip-touch`, and the pointer
 *   `value` / `handle` of the latter are callbacks (or a render shape) with no JSON form;
 * - the `dataZoom` toolbox feature loses its `yAxisIndex: 'none'` argument: the model exposes the
 *   feature without its options;
 * - `line-markline` drops the `distance: [20, 8]` array, which `LabelOption` does not carry, and
 *   labels only the start of each diagonal line, as the original does; the two-corner `markArea` of
 *   `line-sections` is written as a pair of `coord` corners;
 * - `area-simple` and `area-time-axis` plot 2000 points instead of the 20000 of the original, and
 *   every `Math.random()` sequence is replaced by a seeded generator so a page always draws the
 *   same picture.
 */

private val line = JsonCategory("json/examples/LineJson.kt")

/** Days of the week, the category axis of most pages. */
private val WEEK = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

/** The counterclockwise sectors of `line-polar`, one point every 3.6 degrees. */
private const val POLAR_SPIRAL_STEPS = 100

/** The sectors of `line-polar2`, one point every degree. */
private const val POLAR_ROSE_STEPS = 360

/** Points plotted by `area-simple` and `area-time-axis`; the originals draw 20000. */
private const val AREA_POINT_COUNT = 2000

/** One day in milliseconds, the step of the time axis of `area-time-axis`. */
private const val ONE_DAY = 86_400_000.0

/** First timestamp of `area-time-axis`: 1988-10-03 00:00 UTC. */
private const val TIME_AXIS_START = 591_926_400_000.0

/** First day of `area-simple`, as in the original. */
private const val AREA_SIMPLE_YEAR = 1968

/** See [AREA_SIMPLE_YEAR]. */
private const val AREA_SIMPLE_MONTH = 10

/** See [AREA_SIMPLE_YEAR]. */
private const val AREA_SIMPLE_DAY = 3

/** Samples of one easing curve of `line-easing`; every curve has `SAMPLE_COUNT + 1` points. */
private const val SAMPLE_COUNT = 30

/** The foods `bump-chart` ranks. */
private val BUMP_NAMES =
    arrayOf("Orange", "Tomato", "Apple", "Sakana", "Banana", "Iwashi", "Snappy Fish", "Lemon", "Pasta")

/** The years of `bump-chart`, which are also its category axis. */
private val BUMP_YEARS = arrayOf("2001", "2002", "2003", "2004", "2005", "2006")

/** The dates of `line-gradient`, the category axis and the data of both panels. */
private val LINE_GRADIENT_DATA =
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
private val MARK_LINE_POSITIONS =
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
private fun focusSeries(): EmphasisOption = EmphasisOption().apply { focus = "series" }

/** An axis pointer of [pointerType]; the model has no builder for it. */
private fun pointerOf(pointerType: String): AxisPointerOption =
    AxisPointerOption().apply { type = pointerType }

/** The `saveAsImage` toolbox most pages carry. */
private fun EChartsOption.saveAsImageToolbox() {
    toolbox { feature = ToolboxFeatureOption().apply { saveAsImage = ToolboxSaveAsImageFeature() } }
}

/** A line style with only a stroke color. */
private fun strokeOf(strokeColor: String): LineStyleOption =
    LineStyleOption().apply { color = strokeColor.asColor() }

/** The transparent-to-solid fill of one `area-stack-gradient` band. */
private fun verticalGradient(from: String, to: String): EChartsColor =
    EChartsColor.LinearGradient(
        x = 0.0,
        y = 0.0,
        x2 = 0.0,
        y2 = 1.0,
        colorStops = listOf(GradientColorStop(0.0, from), GradientColorStop(1.0, to)),
    )

/** A `[label, value]` row, the shape of the dated line pages. */
private fun datedValue(date: String, dateValue: Number): DataValue =
    arrayValue(textValue(date), valueOf(dateValue))

/** Days of [month] of [year], the leap rule of the Gregorian calendar. */
private fun daysInMonth(year: Int, month: Int): Int =
    when (month) {
        2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
        4, 6, 9, 11 -> 30
        else -> 31
    }

/** [count] consecutive day labels starting at the given date, formatted by [format]. */
private fun consecutiveDays(
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
private fun monthsOf(year: String): List<DataValue> = (1..12).map { textValue("$year-$it") }

/**
 * A fixed-seed linear congruential generator, the same one the Kotlin pages use.
 *
 * It replaces the `Math.random()` sequences of `area-simple`, `area-time-axis`, `bump-chart` and
 * `line-tooltip-touch`, so a page draws the same picture on every visit and the two modes of the
 * switch plot identical curves.
 */
private class LineRandom(seed: Int) {
    private var state = seed

    /** Next value in `0..1`. */
    fun next(): Double {
        state = state * 1664525 + 1013904223
        return (state ushr 1).toDouble() / 2147483648.0
    }
}

/** The random walk of `area-simple`: `round((random - 0.5) * 20 + previous)`. */
private fun areaSimpleValues(): List<DataValue> {
    val random = LineRandom(19681003)
    var previous = random.next() * 300
    return List(AREA_POINT_COUNT) { index ->
        if (index > 0) previous = kotlin.math.round((random.next() - 0.5) * 20 + previous)
        valueOf(previous)
    }
}

/** The random walk of `area-time-axis`, one step per day from [TIME_AXIS_START]. */
private fun timeAxisPoints(): List<DataValue> {
    val random = LineRandom(19881003)
    var previous = random.next() * 300
    return List(AREA_POINT_COUNT) { index ->
        if (index > 0) previous = kotlin.math.round((random.next() - 0.5) * 20 + previous)
        point(TIME_AXIS_START + index * ONE_DAY, previous)
    }
}

/** The `[r, theta]` spiral of `line-polar`: `r = 5 * (1 + sin(theta))`. */
private fun polarSpiral(): List<DataValue> =
    (0..POLAR_SPIRAL_STEPS).map { index ->
        val theta = index / 100.0 * 360
        val radius = 5 * (1 + kotlin.math.sin(theta / 180 * kotlin.math.PI))
        point(radius, theta)
    }

/** The `[r, i]` rose of `line-polar2`: `r = sin(2t) * cos(2t)`. */
private fun polarRose(): List<DataValue> =
    (0..POLAR_ROSE_STEPS).map { index ->
        val t = index / 180.0 * kotlin.math.PI
        point(kotlin.math.sin(2 * t) * kotlin.math.cos(2 * t), index)
    }

/** One nine-day walk of `line-tooltip-touch`; the category labels start at 2016-10-4. */
private fun touchWalk(seed: Int, reset: Double): List<DataValue> {
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
private fun bumpRankings(): Array<DoubleArray> {
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
private class EasingCell(
    /** Left edge of the cell, in percent. */
    val leftPercent: Double,
    /** Top edge of the cell, in percent. */
    val topPercent: Double,
    /** Width and height of the cell, in percent. */
    val sizePercent: Double,
)

/** Lays the easing panels out in a square mesh, as the original does. */
private fun easingCells(count: Int): List<EasingCell> {
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
private fun percentText(value: Double): String =
    "${kotlin.math.round(value * 100) / 100.0}%"

/** One easing function of `line-easing`. */
private class Easing(val name: String, val at: (Double) -> Double)

/** `2 ^ exponent`, the base of the exponential easings. */
private fun twoPow(exponent: Double): Double = kotlin.math.exp(exponent * kotlin.math.ln(2.0))

/** The elastic branch of the elastic easings; `a` is forced to `1`, as zrender always does. */
private fun elasticWave(k: Double, exponent: Double): Double {
    val period = 0.4
    val shift = period / 4
    return twoPow(exponent * k) * kotlin.math.sin((k - shift) * (2 * kotlin.math.PI) / period)
}

/** Bounce easing, falling branch. */
private fun bounceOutAt(k: Double): Double =
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
private fun bounceInAt(k: Double): Double = 1 - bounceOutAt(1 - k)

/** The 31 easing functions `line-easing` samples, in the upstream order. */
private val EASINGS =
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
private fun hourBand(bandName: String, from: String, to: String): MarkAreaDataItem =
    MarkAreaDataItem.of(
        MarkAreaData().apply {
            name = bandName
            coord = listOf(str(from), num(0))
        },
        MarkAreaData().apply { coord = listOf(str(to), num(1000)) },
    )

/** One piece of the hidden visual map of `line-sections`; `null` leaves that bound open. */
private fun hourPiece(greaterThan: Double?, atMost: Double?, fillColor: String): VisualMapPiece =
    VisualMapPiece().apply {
        if (greaterThan != null) gt = greaterThan
        if (atMost != null) lte = atMost
        color = fillColor.asColor()
    }

/** One shaded band of `area-pieces`: the categories strictly between both indices. */
private fun shadedPiece(greaterThan: Double, lessThan: Double): VisualMapPiece =
    VisualMapPiece().apply {
        gt = greaterThan
        lt = lessThan
        color = "rgba(0, 0, 180, 0.4)".asColor()
    }

/** A vertical mark line on the category axis of `area-pieces`. */
private fun categoryBoundary(index: Int): MarkLineDataItem =
    MarkLineDataItem.of(MarkLineData().apply { xAxis = num(index) })

/** The `{type: 'average', name: 'Avg'}` mark line of `line-marker`. */
private fun averageMarkLine(): MarkLineDataItem =
    MarkLineDataItem.of(
        MarkLineData().apply {
            name = "Avg"
            type = "average"
        },
    )

/** One horizontal mark line of `line-markline`, labelled with its position name. */
private fun horizontalMarkLine(positionName: String, axisValue: Double): MarkLineDataItem =
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
private fun diagonalMarkLine(positionName: String, labelText: String): MarkLineDataItem =
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

public val lineJsonExamples: List<JsonExample> =
    listOf(
        // ------------------------------------------------------------- basic lines
        line.example("line-simple") {
            xAxis {
                type = AxisType.Category
                categories(*WEEK)
            }
            yAxis { type = AxisType.Value }
            series {
                line { values(150, 230, 224, 218, 135, 147, 260) }
            }
        },
        line.example("line-smooth") {
            xAxis {
                type = AxisType.Category
                categories(*WEEK)
            }
            yAxis { type = AxisType.Value }
            series {
                line {
                    smooth = BooleanOrNumber.of(true)
                    values(820, 932, 901, 934, 1290, 1330, 1320)
                }
            }
        },
        line.example("line-stack") {
            title { text = "Stacked Line" }
            tooltip { trigger = TooltipTrigger.Axis }
            legend { entries("Email", "Union Ads", "Video Ads", "Direct", "Search Engine") }
            grid {
                left = str("3%")
                right = str("4%")
                bottom = str("3%")
                containLabel = true
            }
            saveAsImageToolbox()
            xAxis {
                type = AxisType.Category
                boundaryGap = BoundaryGap.of(false)
                categories(*WEEK)
            }
            yAxis { type = AxisType.Value }
            series {
                line {
                    name = "Email"
                    stack = "Total"
                    emphasis = focusSeries()
                    values(120, 132, 101, 134, 90, 230, 210)
                }
                line {
                    name = "Union Ads"
                    stack = "Total"
                    emphasis = focusSeries()
                    values(220, 182, 191, 234, 290, 330, 310)
                }
                line {
                    name = "Video Ads"
                    stack = "Total"
                    emphasis = focusSeries()
                    values(150, 232, 201, 154, 190, 330, 410)
                }
                line {
                    name = "Direct"
                    stack = "Total"
                    emphasis = focusSeries()
                    values(320, 332, 301, 334, 390, 330, 320)
                }
                line {
                    name = "Search Engine"
                    stack = "Total"
                    emphasis = focusSeries()
                    values(820, 932, 901, 934, 1290, 1330, 1320)
                }
            }
        },
        line.example("line-step") {
            title { text = "Step Line" }
            tooltip { trigger = TooltipTrigger.Axis }
            legend { entries("Step Start", "Step Middle", "Step End") }
            grid {
                left = str("3%")
                right = str("4%")
                bottom = str("3%")
                containLabel = true
            }
            saveAsImageToolbox()
            xAxis {
                type = AxisType.Category
                categories(*WEEK)
            }
            yAxis { type = AxisType.Value }
            series {
                line {
                    name = "Step Start"
                    step = BooleanOrString.of("start")
                    values(120, 132, 101, 134, 90, 230, 210)
                }
                line {
                    name = "Step Middle"
                    step = BooleanOrString.of("middle")
                    values(220, 282, 201, 234, 290, 430, 410)
                }
                line {
                    name = "Step End"
                    step = BooleanOrString.of("end")
                    values(450, 432, 401, 454, 590, 530, 510)
                }
            }
        },
        line.example("line-style") {
            xAxis {
                type = AxisType.Category
                categories(*WEEK)
            }
            yAxis { type = AxisType.Value }
            series {
                line {
                    symbol = "triangle"
                    symbolSize = NumberOrStringOrNumbers.of(20)
                    lineStyle {
                        color = "#5470C6".asColor()
                        width = 4.0
                        type = "dashed"
                    }
                    itemStyle {
                        borderWidth = 3.0
                        borderColor = "#EE6666".asColor()
                        color = "yellow".asColor()
                    }
                    values(120, 200, 150, 80, 70, 110, 130)
                }
            }
        },
        line.example("line-y-category") {
            legend { entries("Altitude (km) vs. temperature (°C)") }
            tooltip {
                trigger = TooltipTrigger.Axis
                formatter = "Temperature : <br/>{b}km : {c}°C"
            }
            grid {
                left = str("3%")
                right = str("4%")
                bottom = str("3%")
                containLabel = true
            }
            xAxis {
                type = AxisType.Value
                axisLabel { formatter = "{value} °C" }
            }
            yAxis {
                type = AxisType.Category
                boundaryGap = BoundaryGap.of(false)
                axisLine { onZero = false }
                axisLabel { formatter = "{value} km" }
                categories("0", "10", "20", "30", "40", "50", "60", "70", "80")
            }
            series {
                line {
                    name = "Altitude (km) vs. temperature (°C)"
                    symbol = "circle"
                    symbolSize = NumberOrStringOrNumbers.of(10)
                    smooth = BooleanOrNumber.of(true)
                    lineStyle {
                        width = 3.0
                        shadowColor = "rgba(0,0,0,0.3)".asColor()
                        shadowBlur = 10.0
                        shadowOffsetY = 8.0
                    }
                    values(15, -50, -56.5, -46.5, -22.1, -2.5, -27.7, -55.7, -76.5)
                }
            }
        },
        line.example("line-in-cartesian-coordinate-system") {
            xAxis { }
            yAxis { }
            series {
                line {
                    data = listOf(point(10, 40), point(50, 100), point(40, 20))
                }
            }
        },
        line.example("line-log") {
            title {
                text = "Log Axis"
                left = str("center")
            }
            tooltip {
                trigger = TooltipTrigger.Item
                formatter = "{a} <br/>{b} : {c}"
            }
            legend { left = str("left") }
            grid {
                left = str("3%")
                right = str("4%")
                bottom = str("3%")
                containLabel = true
            }
            xAxis {
                type = AxisType.Category
                name = "x"
                splitLine { show = false }
                categories("A", "B", "C", "D", "E", "F", "G", "H", "I")
            }
            // The `minorSplitLine` of the original has no field on `AxisOption`.
            yAxis {
                type = AxisType.Log
                name = "y"
            }
            series {
                line {
                    name = "Log2"
                    values(1, 3, 9, 27, 81, 247, 741, 2223, 6669)
                }
                line {
                    name = "Log3"
                    values(1, 2, 4, 8, 16, 32, 64, 128, 256)
                }
                line {
                    name = "Log1/2"
                    values(0.5, 0.25, 0.125, 0.0625, 0.03125, 0.015625, 0.0078125, 0.00390625, 0.001953125)
                }
            }
        },
        // ------------------------------------------------------------------ areas
        line.example("area-basic") {
            xAxis {
                type = AxisType.Category
                boundaryGap = BoundaryGap.of(false)
                categories(*WEEK)
            }
            yAxis { type = AxisType.Value }
            series {
                line {
                    areaStyle { }
                    values(820, 932, 901, 934, 1290, 1330, 1320)
                }
            }
        },
        line.example("area-stack") {
            title { text = "Stacked Area Chart" }
            tooltip {
                trigger = TooltipTrigger.Axis
                axisPointer =
                    pointerOf("cross").apply {
                        label = AxisPointerLabelOption().apply { backgroundColor = "#6a7985".asColor() }
                    }
            }
            legend { entries("Email", "Union Ads", "Video Ads", "Direct", "Search Engine") }
            saveAsImageToolbox()
            xAxis {
                type = AxisType.Category
                boundaryGap = BoundaryGap.of(false)
                categories(*WEEK)
            }
            yAxis { type = AxisType.Value }
            series {
                line {
                    name = "Email"
                    stack = "Total"
                    areaStyle { }
                    emphasis = focusSeries()
                    values(120, 132, 101, 134, 90, 230, 210)
                }
                line {
                    name = "Union Ads"
                    stack = "Total"
                    areaStyle { }
                    emphasis = focusSeries()
                    values(220, 182, 191, 234, 290, 330, 310)
                }
                line {
                    name = "Video Ads"
                    stack = "Total"
                    areaStyle { }
                    emphasis = focusSeries()
                    values(150, 232, 201, 154, 190, 330, 410)
                }
                line {
                    name = "Direct"
                    stack = "Total"
                    areaStyle { }
                    emphasis = focusSeries()
                    values(320, 332, 301, 334, 390, 330, 320)
                }
                line {
                    name = "Search Engine"
                    stack = "Total"
                    areaStyle { }
                    emphasis = focusSeries()
                    label {
                        show = true
                        position = "top"
                    }
                    values(820, 932, 901, 934, 1290, 1330, 1320)
                }
            }
        },
        line.example("area-pieces") {
            xAxis {
                type = AxisType.Category
                boundaryGap = BoundaryGap.of(false)
                categories(
                    "2019-10-10", "2019-10-11", "2019-10-12", "2019-10-13", "2019-10-14",
                    "2019-10-15", "2019-10-16", "2019-10-17", "2019-10-18",
                )
            }
            yAxis {
                type = AxisType.Value
                boundaryGap = BoundaryGap.of(num(0), str("30%"))
            }
            // The visual map shades the second and the sixth category band; the mark lines draw the
            // borders of those bands.
            visualMap {
                type = VisualMapType.Piecewise
                show = false
                dimension = num(0)
                seriesIndex = listOf(0)
                pieces = listOf(shadedPiece(1.0, 3.0), shadedPiece(5.0, 7.0))
            }
            series {
                line {
                    smooth = BooleanOrNumber.of(0.6)
                    symbol = "none"
                    lineStyle {
                        color = "#5470C6".asColor()
                        width = 5.0
                    }
                    areaStyle { }
                    markLine {
                        symbol = listOf("none", "none")
                        label = LabelOption().apply { show = false }
                        data =
                            listOf(
                                categoryBoundary(1),
                                categoryBoundary(3),
                                categoryBoundary(5),
                                categoryBoundary(7),
                            )
                    }
                    data =
                        listOf(
                            datedValue("2019-10-10", 200),
                            datedValue("2019-10-11", 560),
                            datedValue("2019-10-12", 750),
                            datedValue("2019-10-13", 580),
                            datedValue("2019-10-14", 250),
                            datedValue("2019-10-15", 300),
                            datedValue("2019-10-16", 450),
                            datedValue("2019-10-17", 300),
                            datedValue("2019-10-18", 100),
                        )
                }
            }
        },
        line.example("area-stack-gradient") {
            color = colors("#80FFA5", "#00DDFF", "#37A2FF", "#FF0087", "#FFBF00")
            title { text = "Gradient Stacked Area Chart" }
            tooltip {
                trigger = TooltipTrigger.Axis
                axisPointer =
                    pointerOf("cross").apply {
                        label = AxisPointerLabelOption().apply { backgroundColor = "#6a7985".asColor() }
                    }
            }
            legend { entries("Line 1", "Line 2", "Line 3", "Line 4", "Line 5") }
            saveAsImageToolbox()
            xAxis {
                type = AxisType.Category
                boundaryGap = BoundaryGap.of(false)
                categories(*WEEK)
            }
            yAxis { type = AxisType.Value }
            series {
                line {
                    name = "Line 1"
                    stack = "Total"
                    smooth = BooleanOrNumber.of(true)
                    lineStyle { width = 0.0 }
                    showSymbol = false
                    areaStyle {
                        opacity = 0.8
                        color = verticalGradient("rgb(128, 255, 165)", "rgb(1, 191, 236)")
                    }
                    emphasis = focusSeries()
                    values(140, 232, 101, 264, 90, 340, 250)
                }
                line {
                    name = "Line 2"
                    stack = "Total"
                    smooth = BooleanOrNumber.of(true)
                    lineStyle { width = 0.0 }
                    showSymbol = false
                    areaStyle {
                        opacity = 0.8
                        color = verticalGradient("rgb(0, 221, 255)", "rgb(77, 119, 255)")
                    }
                    emphasis = focusSeries()
                    values(120, 282, 111, 234, 220, 340, 310)
                }
                line {
                    name = "Line 3"
                    stack = "Total"
                    smooth = BooleanOrNumber.of(true)
                    lineStyle { width = 0.0 }
                    showSymbol = false
                    areaStyle {
                        opacity = 0.8
                        color = verticalGradient("rgb(55, 162, 255)", "rgb(116, 21, 219)")
                    }
                    emphasis = focusSeries()
                    values(320, 132, 201, 334, 190, 130, 220)
                }
                line {
                    name = "Line 4"
                    stack = "Total"
                    smooth = BooleanOrNumber.of(true)
                    lineStyle { width = 0.0 }
                    showSymbol = false
                    areaStyle {
                        opacity = 0.8
                        color = verticalGradient("rgb(255, 0, 135)", "rgb(135, 0, 157)")
                    }
                    emphasis = focusSeries()
                    values(220, 402, 231, 134, 190, 230, 120)
                }
                line {
                    name = "Line 5"
                    stack = "Total"
                    smooth = BooleanOrNumber.of(true)
                    lineStyle { width = 0.0 }
                    showSymbol = false
                    areaStyle {
                        opacity = 0.8
                        color = verticalGradient("rgb(255, 191, 0)", "rgb(224, 62, 76)")
                    }
                    emphasis = focusSeries()
                    label {
                        show = true
                        position = "top"
                    }
                    values(220, 302, 181, 234, 210, 290, 150)
                }
            }
        },
        line.example("area-simple") {
            title {
                left = str("center")
                text = "Large Area Chart"
            }
            // The original pins the tooltip at 10% of the height through a `position` callback.
            tooltip { trigger = TooltipTrigger.Axis }
            toolbox {
                feature =
                    ToolboxFeatureOption().apply {
                        dataZoom = ToolboxFeature()
                        restore = ToolboxFeature()
                        saveAsImage = ToolboxSaveAsImageFeature()
                    }
            }
            xAxis {
                type = AxisType.Category
                boundaryGap = BoundaryGap.of(false)
                data =
                    consecutiveDays(AREA_SIMPLE_YEAR, AREA_SIMPLE_MONTH, AREA_SIMPLE_DAY, AREA_POINT_COUNT) {
                            year,
                            month,
                            day,
                        ->
                        "$year/$month/$day"
                    }
            }
            yAxis {
                type = AxisType.Value
                boundaryGap = BoundaryGap.of(num(0), str("100%"))
            }
            dataZoom {
                type = DataZoomType.Inside
                start = 0.0
                end = 10.0
            }
            dataZoom {
                start = 0.0
                end = 10.0
            }
            series {
                line {
                    name = "Fake Data"
                    symbol = "none"
                    sampling = "lttb"
                    itemStyle { color = "rgb(255, 70, 131)".asColor() }
                    areaStyle {
                        color = verticalGradient("rgb(255, 158, 68)", "rgb(255, 70, 131)")
                    }
                    data = areaSimpleValues()
                }
            }
        },
        line.example("area-time-axis") {
            // The title of the original is kept verbatim, including the misspelling.
            title {
                left = str("center")
                text = "Large Ara Chart"
            }
            tooltip { trigger = TooltipTrigger.Axis }
            toolbox {
                feature =
                    ToolboxFeatureOption().apply {
                        dataZoom = ToolboxFeature()
                        restore = ToolboxFeature()
                        saveAsImage = ToolboxSaveAsImageFeature()
                    }
            }
            xAxis {
                type = AxisType.Time
                boundaryGap = BoundaryGap.of(false)
            }
            yAxis {
                type = AxisType.Value
                boundaryGap = BoundaryGap.of(num(0), str("100%"))
            }
            dataZoom {
                type = DataZoomType.Inside
                start = 0.0
                end = 20.0
            }
            dataZoom {
                type = DataZoomType.Slider
                start = 0.0
                end = 20.0
            }
            series {
                line {
                    name = "Fake Data"
                    smooth = BooleanOrNumber.of(true)
                    symbol = "none"
                    areaStyle { }
                    data = timeAxisPoints()
                }
            }
        },
        // ------------------------------------------------------------ long tail
        line.example("bump-chart") {
            val rankings = bumpRankings()
            title { text = "Bump Chart (Ranking)" }
            tooltip { trigger = TooltipTrigger.Item }
            grid {
                left = num(30)
                right = num(110)
                bottom = num(30)
                containLabel = true
            }
            saveAsImageToolbox()
            xAxis {
                type = AxisType.Category
                boundaryGap = BoundaryGap.of(false)
                splitLine { show = true }
                axisLabel {
                    margin = 30.0
                    fontSize = 16.0
                }
                categories(*BUMP_YEARS)
            }
            yAxis {
                type = AxisType.Value
                inverse = true
                interval = 1.0
                min = num(1)
                max = num(BUMP_NAMES.size)
                axisLabel {
                    margin = 30.0
                    fontSize = 16.0
                    formatter = "#{value}"
                }
            }
            series {
                BUMP_NAMES.forEachIndexed { index, food ->
                    line {
                        name = food
                        symbolSize = NumberOrStringOrNumbers.of(20)
                        smooth = BooleanOrNumber.of(true)
                        emphasis = focusSeries()
                        endLabel {
                            show = true
                            formatter = "{a}"
                            distance = 20.0
                        }
                        lineStyle { width = 4.0 }
                        data = rankings[index].map { rank -> valueOf(rank) }
                    }
                }
            }
        },
        line.example("line-easing") {
            val cells = easingCells(EASINGS.size)
            EASINGS.forEachIndexed { index, easing ->
                val cell = cells[index]
                title {
                    text = easing.name
                    textAlign = "center"
                    left = str(percentText(cell.leftPercent + cell.sizePercent / 2))
                    top = str(percentText(cell.topPercent))
                    textStyle {
                        fontSize = 12.0
                        fontWeight = str("normal")
                    }
                }
                grid {
                    show = true
                    borderWidth = 0.0
                    shadowColor = "rgba(0, 0, 0, 0.3)".asColor()
                    shadowBlur = 2.0
                    left = str(percentText(cell.leftPercent))
                    top = str(percentText(cell.topPercent))
                    width = str(percentText(cell.sizePercent))
                    height = str(percentText(cell.sizePercent))
                }
                xAxis {
                    type = AxisType.Value
                    show = false
                    min = num(0)
                    max = num(1)
                    gridIndex = index
                }
                yAxis {
                    type = AxisType.Value
                    show = false
                    min = num(-0.4)
                    max = num(1.4)
                    gridIndex = index
                }
            }
            series {
                EASINGS.forEachIndexed { index, easing ->
                    line {
                        name = easing.name
                        xAxisIndex = index
                        yAxisIndex = index
                        showSymbol = false
                        animationEasing = easing.name
                        animationDuration = 1000.0
                        data =
                            List(SAMPLE_COUNT + 1) { step ->
                                val k = step.toDouble() / SAMPLE_COUNT
                                point(k, easing.at(k))
                            }
                    }
                }
            }
            title {
                text = "Different Easing Functions"
                top = str("bottom")
                left = str("center")
            }
        },
        line.example("line-gradient") {
            val dates = LINE_GRADIENT_DATA.map { it.first }
            val plotValues = LINE_GRADIENT_DATA.map { it.second }
            // The first visual map colors by the value, the second by the position on the axis.
            visualMap {
                show = false
                type = VisualMapType.Continuous
                seriesIndex = listOf(0)
                min = 0.0
                max = 400.0
            }
            visualMap {
                show = false
                type = VisualMapType.Continuous
                seriesIndex = listOf(1)
                dimension = num(0)
                min = 0.0
                max = (dates.size - 1).toDouble()
            }
            title {
                left = str("center")
                text = "Gradient along the y axis"
            }
            title {
                top = str("55%")
                left = str("center")
                text = "Gradient along the x axis"
            }
            tooltip { trigger = TooltipTrigger.Axis }
            xAxis {
                type = AxisType.Category
                data = dates.map { textValue(it) }
            }
            xAxis {
                type = AxisType.Category
                gridIndex = 1
                data = dates.map { textValue(it) }
            }
            yAxis { type = AxisType.Value }
            yAxis {
                type = AxisType.Value
                gridIndex = 1
            }
            grid { bottom = str("60%") }
            grid { top = str("60%") }
            series {
                line {
                    showSymbol = false
                    data = plotValues.map { valueOf(it) }
                }
                line {
                    showSymbol = false
                    xAxisIndex = 1
                    yAxisIndex = 1
                    data = plotValues.map { valueOf(it) }
                }
            }
        },
        line.example("multiple-x-axis") {
            color = colors("#5470C6", "#EE6666")
            // Upstream labels the pointer with the month and the value under it, through a callback.
            tooltip {
                trigger = TooltipTrigger.None
                axisPointer = pointerOf("cross")
            }
            legend { }
            grid {
                top = num(70)
                bottom = num(50)
            }
            xAxis {
                type = AxisType.Category
                axisTick { alignWithLabel = true }
                axisLine {
                    onZero = false
                    lineStyle = strokeOf("#EE6666")
                }
                data = monthsOf("2016")
            }
            xAxis {
                type = AxisType.Category
                axisTick { alignWithLabel = true }
                axisLine {
                    onZero = false
                    lineStyle = strokeOf("#5470C6")
                }
                data = monthsOf("2015")
            }
            yAxis { type = AxisType.Value }
            series {
                // The 2015 series is drawn on the second axis, the one labelled with 2015.
                line {
                    name = "Precipitation(2015)"
                    xAxisIndex = 1
                    smooth = BooleanOrNumber.of(true)
                    emphasis = focusSeries()
                    values(2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3)
                }
                line {
                    name = "Precipitation(2016)"
                    smooth = BooleanOrNumber.of(true)
                    emphasis = focusSeries()
                    values(3.9, 5.9, 11.1, 18.7, 48.3, 69.2, 231.6, 46.6, 55.4, 18.4, 10.3, 0.7)
                }
            }
        },
        line.example("line-marker") {
            title { text = "Temperature Change in the Coming Week" }
            tooltip { trigger = TooltipTrigger.Axis }
            legend { }
            toolbox {
                feature =
                    ToolboxFeatureOption().apply {
                        dataZoom = ToolboxFeature()
                        dataView = ToolboxDataViewFeature().apply { readOnly = false }
                        magicType = ToolboxMagicTypeFeature().apply { type = listOf(SeriesType.Line, SeriesType.Bar) }
                        restore = ToolboxFeature()
                        saveAsImage = ToolboxSaveAsImageFeature()
                    }
            }
            xAxis {
                type = AxisType.Category
                boundaryGap = BoundaryGap.of(false)
                categories(*WEEK)
            }
            yAxis {
                type = AxisType.Value
                axisLabel { formatter = "{value} °C" }
            }
            series {
                line {
                    name = "Highest"
                    values(10, 11, 13, 11, 12, 12, 9)
                    markPoint {
                        data =
                            listOf(
                                MarkPointData().apply {
                                    type = "max"
                                    name = "Max"
                                },
                                MarkPointData().apply {
                                    type = "min"
                                    name = "Min"
                                },
                            )
                    }
                    markLine { data = listOf(averageMarkLine()) }
                }
                line {
                    name = "Lowest"
                    values(1, -2, 2, 5, 3, 2, 0)
                    markPoint {
                        data =
                            listOf(
                                MarkPointData().apply {
                                    name = "周最低"
                                    value = valueOf(-2)
                                    xAxis = num(1)
                                    yAxis = num(-1.5)
                                },
                            )
                    }
                    markLine {
                        data =
                            listOf(
                                averageMarkLine(),
                                MarkLineDataItem.of(
                                    MarkLineData().apply {
                                        symbol = "none"
                                        x = str("90%")
                                        yAxis = str("max")
                                    },
                                    MarkLineData().apply {
                                        symbol = "circle"
                                        type = "max"
                                        name = "最高点"
                                        label =
                                            LabelOption().apply {
                                                position = "start"
                                                formatter = "Max"
                                            }
                                    },
                                ),
                            )
                    }
                }
            }
        },
        line.example("line-markline") {
            animation = false
            textStyle = TextStyleOption().apply { fontSize = 14.0 }
            grid {
                top = num(30)
                left = num(60)
                right = num(60)
                bottom = num(40)
            }
            xAxis {
                type = AxisType.Category
                boundaryGap = BoundaryGap.of(true)
                splitArea { show = true }
                categories("A", "B", "C", "D", "E")
            }
            yAxis {
                type = AxisType.Value
                max = num(2)
            }
            series {
                line {
                    name = "line"
                    stack = "all"
                    symbolSize = NumberOrStringOrNumbers.of(6)
                    data = listOf(valueOf(0.3), valueOf(1.4), valueOf(1.2), valueOf(1.0), valueOf(0.6))
                    markLine {
                        // `distance: [20, 8]` is an array on one label, which `LabelOption` does not
                        // carry; the labels keep their positions only.
                        data =
                            MARK_LINE_POSITIONS.flatMapIndexed { index, positionName ->
                                val axisValue = 1.8 - 0.2 * kotlin.math.floor(index / 3.0)
                                // `insideMiddle` and `middle` land on the same place, so the diagonal
                                // shares one label.
                                val labelText =
                                    if (positionName == "insideMiddle") "insideMiddle / middle" else positionName
                                if (positionName == "middle") {
                                    listOf(horizontalMarkLine(positionName, axisValue))
                                } else {
                                    listOf(
                                        horizontalMarkLine(positionName, axisValue),
                                        diagonalMarkLine(positionName, labelText),
                                    )
                                }
                            }
                    }
                }
            }
        },
        line.example("line-polar") {
            title { text = "Two Value-Axes in Polar" }
            legend { entries("line") }
            polar { }
            tooltip {
                trigger = TooltipTrigger.Axis
                axisPointer = pointerOf("cross")
            }
            // The `startAngle: 0` of the original has no field on `AxisOption`.
            angleAxis { type = AxisType.Value }
            radiusAxis { }
            series {
                line {
                    coordinateSystem = "polar"
                    name = "line"
                    data = polarSpiral()
                }
            }
        },
        line.example("line-polar2") {
            animationDuration = 2000.0
            title { text = "Two Value-Axes in Polar" }
            legend { entries("line") }
            polar { center = listOf(str("50%"), str("54%")) }
            tooltip {
                trigger = TooltipTrigger.Axis
                axisPointer = pointerOf("cross")
            }
            angleAxis { type = AxisType.Value }
            radiusAxis { min = num(0) }
            series {
                line {
                    coordinateSystem = "polar"
                    name = "line"
                    showSymbol = false
                    data = polarRose()
                }
            }
        },
        line.example("line-sections") {
            title {
                text = "Distribution of Electricity"
                subtext = "Fake Data"
            }
            tooltip {
                trigger = TooltipTrigger.Axis
                axisPointer = pointerOf("cross")
            }
            saveAsImageToolbox()
            xAxis {
                type = AxisType.Category
                boundaryGap = BoundaryGap.of(false)
                categories(
                    "00:00", "01:15", "02:30", "03:45", "05:00", "06:15", "07:30", "08:45",
                    "10:00", "11:15", "12:30", "13:45", "15:00", "16:15", "17:30", "18:45",
                    "20:00", "21:15", "22:30", "23:45",
                )
            }
            yAxis {
                type = AxisType.Value
                axisLabel { formatter = "{value} W" }
                axisPointer = AxisPointerOption().apply { snap = true }
            }
            visualMap {
                show = false
                dimension = num(0)
                pieces =
                    listOf(
                        hourPiece(null, 6.0, "green"),
                        hourPiece(6.0, 8.0, "red"),
                        hourPiece(8.0, 14.0, "green"),
                        hourPiece(14.0, 17.0, "red"),
                        hourPiece(17.0, null, "green"),
                    )
            }
            series {
                line {
                    name = "Electricity"
                    smooth = BooleanOrNumber.of(true)
                    values(
                        300, 280, 250, 260, 270, 300, 550, 500, 400, 390,
                        380, 390, 400, 500, 600, 750, 800, 700, 600, 400,
                    )
                    markArea {
                        itemStyle =
                            ItemStyleOption().apply { color = "rgba(255, 173, 177, 0.4)".asColor() }
                        data =
                            listOf(
                                hourBand("Morning Peak", "07:30", "10:00"),
                                hourBand("Evening Peak", "17:30", "21:15"),
                            )
                    }
                }
            }
        },
        line.example("line-tooltip-touch") {
            title {
                left = str("center")
                text = "Tootip and dataZoom on Mobile Device"
            }
            legend {
                top = str("bottom")
                entries("Intention")
            }
            // The original pins the tooltip to a fixed height through a `position` callback.
            tooltip { triggerOn = "none" }
            toolbox {
                left = str("center")
                itemSize = 25.0
                top = num(55)
                feature =
                    ToolboxFeatureOption().apply {
                        dataZoom = ToolboxFeature()
                        restore = ToolboxFeature()
                    }
            }
            xAxis {
                type = AxisType.Time
                splitLine { show = false }
                axisPointer =
                    AxisPointerOption().apply {
                        snap = true
                        lineStyle =
                            LineStyleOption().apply {
                                color = "#7581BD".asColor()
                                width = 2.0
                            }
                        label =
                            AxisPointerLabelOption().apply {
                                show = true
                                backgroundColor = "#7581BD".asColor()
                            }
                    }
            }
            yAxis {
                type = AxisType.Value
                z = 10.0
                axisTick { inside = true }
                splitLine { show = false }
                axisLabel {
                    inside = true
                    formatter = "{value}\n"
                }
            }
            grid {
                top = num(110)
                left = num(15)
                right = num(15)
                height = num(160)
            }
            dataZoom {
                type = DataZoomType.Inside
                throttle = 50.0
            }
            series {
                line {
                    name = "Fake Data"
                    smooth = BooleanOrNumber.of(true)
                    symbol = "circle"
                    symbolSize = NumberOrStringOrNumbers.of(5)
                    sampling = "average"
                    stack = "a"
                    itemStyle { color = "#0770FF".asColor() }
                    areaStyle {
                        color = verticalGradient("rgba(58,77,233,0.8)", "rgba(58,77,233,0.3)")
                    }
                    data = touchWalk(11, 300.0)
                }
                line {
                    name = "Fake Data"
                    smooth = BooleanOrNumber.of(true)
                    symbol = "circle"
                    symbolSize = NumberOrStringOrNumbers.of(5)
                    sampling = "average"
                    stack = "a"
                    itemStyle { color = "#F2597F".asColor() }
                    areaStyle {
                        color = verticalGradient("rgba(213,72,120,0.8)", "rgba(213,72,120,0.3)")
                    }
                    data = touchWalk(29, 50.0)
                }
            }
        },
    )
