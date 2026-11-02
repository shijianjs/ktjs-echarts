package demo.examples.custom

import echarts.*
import echarts.options.*
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import zrender.ZRColor
import zrender.ZRStyleProps
import demo.examples.chord.column
import demo.examples.funnel.labels
import demo.examples.heatmap.punchCard
import demo.examples.line.bounceOut
import demo.examples.shared.fixed
import demo.examples.shared.percent
import demo.examples.shared.row
import demo.examples.sunburst.radius

/** Shape of `winds.json`: a grid of `[vx, vy]` vectors. */
internal external interface WindData {
    val nx: Double
    val ny: Double
    val data: Array<Array<Double>>
}

/** Fisher-Yates shuffle with a fixed seed, so the page is reproducible. */
internal fun shuffleFixed(values: Array<DoubleArray>, seed: Int) {
    val random = kotlin.random.Random(seed)
    for (index in values.indices.reversed()) {
        val swap = random.nextInt(index + 1)
        val temporary = values[index]
        values[index] = values[swap]
        values[swap] = temporary
    }
}

/** The location pin path used by the SVG custom-effect example. */
internal const val PIN_PATH =
    "M16 0c-5.523 0-10 4.477-10 10 0 10 10 22 10 22s10-12 10-22c0-5.523-4.477-10-10-10zM16 16c-3.314 0-6-2.686-6-6s2.686-6 6-6 6 2.686 6 6-2.686 6-6 6z"

/** Ripple animation of a location marker; `index` staggers the start across the five rings. */
internal fun rippleAnimation(index: Int): ElementKeyframeAnimationOption {
    val delay = (-index.toDouble() / 4) * 4000
    return unsafeJso {
        duration = 4000.0
        loop = true
        this.delay = delay
        keyframes = arrayOf(
            unsafeJso<AnimationKeyframe> {
                percent = 0.0
                scaleX = 0.0
                scaleY = 0.0
                style = unsafeJso<ZRStyleProps> { opacity = 1.0 }
            },
            unsafeJso<AnimationKeyframe> {
                percent = 1.0
                scaleX = 1.0
                scaleY = 0.4
                style = unsafeJso<ZRStyleProps> { opacity = 0.0 }
            },
        )
    }
}

/** Bouncing animation of the pin itself. */
internal fun jumpAnimation(): ElementKeyframeAnimationOption {
    return unsafeJso {
        duration = 1000.0
        loop = true
        this.delay = kotlin.random.Random.nextDouble() * 1000
        keyframes = arrayOf(
            unsafeJso<AnimationKeyframe> { y = -10.0; percent = 0.5; easing = "cubicOut".unsafeCast<AnimationEasing>() },
            unsafeJso<AnimationKeyframe> { y = 0.0; percent = 1.0; easing = "bounceOut".unsafeCast<AnimationEasing>() },
        )
    }
}

/** Hours of the day, the angle axis of `custom-polar-heatmap` and `scatter-polar-punchCard`. */
internal val punchCardHours =
    arrayOf("12a", "1a", "2a", "3a", "4a", "5a", "6a", "7a", "8a", "9a", "10a", "11a", "12p", "1p", "2p", "3p", "4p", "5p", "6p", "7p", "8p", "9p", "10p", "11p")

/** Days of the week, the radius axis of `custom-polar-heatmap` and `scatter-polar-punchCard`. */
internal val punchCardDays = arrayOf("Saturday", "Friday", "Thursday", "Wednesday", "Tuesday", "Monday", "Sunday")

/**
 * `rawData` of the original punch card, one row per day and one column per hour.
 *
 * The examples expand it into `[dayIndex, hourIndex, value]` triples.
 */
internal val punchCardValues =
    arrayOf(
        intArrayOf(5, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 4, 1, 1, 3, 4, 6, 4, 4, 3, 3, 2, 5),
        intArrayOf(7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 2, 2, 6, 9, 11, 6, 7, 8, 12, 5, 5, 7, 2),
        intArrayOf(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 3, 2, 1, 9, 8, 10, 6, 5, 5, 5, 7, 4, 2, 4),
        intArrayOf(7, 3, 0, 0, 0, 0, 0, 0, 1, 0, 5, 4, 7, 14, 13, 12, 9, 5, 5, 10, 6, 4, 4, 1),
        intArrayOf(1, 3, 0, 0, 0, 1, 0, 0, 0, 2, 4, 4, 2, 4, 4, 14, 12, 1, 8, 5, 3, 7, 3, 0),
        intArrayOf(2, 1, 0, 3, 0, 0, 0, 0, 2, 0, 4, 1, 5, 10, 5, 7, 11, 6, 0, 5, 3, 4, 2, 0),
        intArrayOf(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 2, 1, 3, 4, 0, 0, 0, 0, 1, 2, 2, 6),
    )

/** The `[day, hour, value]` triples of `custom-polar-heatmap`. */
internal fun punchCardData(): Array<OptionDataItem?> =
    dataItems(
        *punchCardValues
            .flatMapIndexed { day, row ->
                row.mapIndexed { hour, value ->
                    OptionDataItem(
                        arrayOf<OptionDataValue?>(
                            OptionDataValue(day.toDouble()),
                            OptionDataValue(hour.toDouble()),
                            OptionDataValue(value.toDouble()),
                        ),
                    )
                }
            }
            .toTypedArray(),
    )

/** Text style of the spiral labels; [ZRStyleProps] does not declare `rich` yet. */
internal external interface SpiralTextStyle : ZRStyleProps {
    var rich: Record<String, ZRStyleProps>?
}

/** Content of the `dataset` of step [index]: `[radius value, radian value]` pairs. */
internal fun spiralDataset(index: Int): DatasetOption =
    unsafeJso {
        source =
            datasetArrayRows(
                Array(datasourceList[index].size) { row ->
                    Array(datasourceList[index][row].size) { column -> OptionDataValue(datasourceList[index][row][column]) }
                },
            )
    }

internal external interface SpiralColor {
    var fill: ZRColor
    var text: ZRColor
}

internal val spiralColors =
    arrayOf(
        unsafeJso<SpiralColor> { fill = ZRColor("#5470c6"); text = ZRColor("#2747a5") },
        unsafeJso<SpiralColor> { fill = ZRColor("#91cc75"); text = ZRColor("#447f27") },
        unsafeJso<SpiralColor> { fill = ZRColor("#fac858"); text = ZRColor("#a0761c") },
    )

internal val radianLabels =
    arrayOf(
        "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo", "Libra", "Scorpius",
        "Sagittarius", "Capricornus", "Aquarius", "Pisces",
    )

internal val valOnRoundRadian = radianLabels.size

internal val radianStep = kotlin.math.PI / 45

internal val barWidthValue = 0.4

internal val valOnRadiusStep = 4.0

internal val startRadian = kotlin.math.PI / 2

internal const val spiralAnimationDuration = 5000.0

internal const val spiralAnimationDurationUpdate = 7000.0

internal val datasourceList: Array<Array<Array<Double>>> =
    arrayOf(
        arrayOf(arrayOf(1.0, 3.0), arrayOf(2.0, 6.0), arrayOf(3.0, 9.0)),
        arrayOf(arrayOf(1.0, 12.0), arrayOf(2.0, 16.0), arrayOf(3.0, 14.0)),
        arrayOf(arrayOf(1.0, 17.0), arrayOf(2.0, 22.0), arrayOf(3.0, 19.0)),
        arrayOf(arrayOf(1.0, 19.0), arrayOf(2.0, 33.0), arrayOf(3.0, 24.0)),
        arrayOf(arrayOf(1.0, 24.0), arrayOf(2.0, 42.0), arrayOf(3.0, 29.0)),
        arrayOf(arrayOf(1.0, 27.0), arrayOf(2.0, 47.0), arrayOf(3.0, 41.0)),
        arrayOf(arrayOf(1.0, 36.0), arrayOf(2.0, 52.0), arrayOf(3.0, 52.0)),
        arrayOf(arrayOf(1.0, 46.0), arrayOf(2.0, 59.0), arrayOf(3.0, 63.0)),
        arrayOf(arrayOf(1.0, 60.0), arrayOf(2.0, 63.0), arrayOf(3.0, 69.0)),
    )

internal fun getSpiralValueOnRadius(valOnStartRadius: Double, valOnEndAngle: Double): Double =
    valOnStartRadius + valOnRadiusStep * (valOnEndAngle / valOnRoundRadian)

internal fun getSpiralRadius(startRadius: Double, endRadian: Double, radiusStep: Double): Double =
    startRadius + radiusStep * ((startRadian - endRadian) / (kotlin.math.PI * 2))

internal fun getRadiusStepByWidth(widthRadius: Double): Double = (widthRadius / barWidthValue) * valOnRadiusStep

internal fun convertToPolarPoint(radius: Double, radian: Double, cx: Double, cy: Double): Array<Double> =
    arrayOf(kotlin.math.cos(radian) * radius + cx, -kotlin.math.sin(radian) * radius + cy)

internal fun makeShapePoints(
    cx: Double,
    cy: Double,
    widthRadius: Double,
    startRadius: Double,
    endRadian: Double,
): Array<Array<Double>> {
    val points = mutableListOf<Array<Double>>()
    val radiusStep = getRadiusStepByWidth(widthRadius)
    var iRadian = startRadian
    val end1 = endRadian - radianStep
    while (iRadian > end1) {
        if (iRadian < endRadian) iRadian = endRadian
        val iRadius = getSpiralRadius(startRadius - widthRadius, iRadian, radiusStep)
        points.add(convertToPolarPoint(iRadius, iRadian, cx, cy))
        iRadian -= radianStep
    }
    iRadian = endRadian
    val end2 = startRadian + radianStep
    while (iRadian < end2) {
        if (iRadian > startRadian) iRadian = startRadian
        val iRadius = getSpiralRadius(startRadius + widthRadius, iRadian, radiusStep)
        points.add(convertToPolarPoint(iRadius, iRadian, cx, cy))
        iRadian += radianStep
    }
    return points.toTypedArray()
}

internal fun makeLabelPosition(
    cx: Double,
    cy: Double,
    widthRadius: Double,
    startRadius: Double,
    endRadian: Double,
): Array<Double> {
    val radiusStep = getRadiusStepByWidth(widthRadius)
    val iRadius = getSpiralRadius(startRadius, endRadian, radiusStep)
    return convertToPolarPoint(iRadius, endRadian - 10 / iRadius, cx, cy)
}

internal fun getMaxRadius(idx: Int): Double {
    var radius = 0.0
    for (item in datasourceList[idx]) {
        radius = kotlin.math.max(radius, getSpiralValueOnRadius(item[0], item[1]))
    }
    return kotlin.math.ceil(radius * 1.2)
}

internal fun makeText(endRadian: Double): String {
    val radian = startRadian - endRadian
    val pi2 = kotlin.math.PI * 2
    val round = kotlin.math.floor(radian / pi2)
    val p = ((radian / pi2) % 1) * 100
    val percent = (kotlin.math.round(p * 10) / 10.0).toString() + "%"
    return "Round {round|" + round.toInt() + "}\n{percent|" + percent + "}"
}
