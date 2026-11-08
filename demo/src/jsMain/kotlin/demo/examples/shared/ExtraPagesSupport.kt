package demo.examples.shared

import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import kotlin.js.Date
import kotlin.js.JsAny
import kotlin.math.round
import demo.examples.scatter.point
import demo.examples.themeriver.days

/** Shape of `confidence-band.json`: one record per day, the band around `value`. */
internal external interface ConfidenceBandItem {
    /** Lower bound. */
    val l: Double

    /** Upper bound. */
    val u: Double

    /** The measured value. */
    val value: Double

    /** ISO date, used as the category of the x axis. */
    val date: String
}

/** The days a `dynamic-data` window holds. */
internal const val DYNAMIC_WINDOW = 10

/** One clock label of `dynamic-data`: `hh:mm:ss` of the current time. */
internal fun clockLabel(date: Date): String {
    fun pad(value: Int): String = value.toString().padStart(2, '0')
    return pad(date.getHours()) + ":" + pad(date.getMinutes()) + ":" + pad(date.getSeconds())
}

/** `value` rounded to one decimal, the precision of the order series upstream. */
internal fun round1(value: Double): Double = round(value * 10) / 10

/** `(value - base) * 100` with one digit after the point, as the tooltip of the band prints it. */
internal fun percent(value: Double): String {
    val tenths = round(value * 1000)
    return (tenths / 10).toString() + "%"
}
