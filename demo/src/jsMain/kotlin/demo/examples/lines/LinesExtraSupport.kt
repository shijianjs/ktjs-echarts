package demo.examples.lines

import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import demo.examples.shared.row

/**
 * Shape of `flights.json`: airports as `[name, city, country, lng, lat]` rows, airlines as
 * `[name, country]` rows and routes as `[airline, from, to]` indices into the first two. The GL
 * pages read the smaller `data-gl` copy of the same table, whose `airlines` has 11 entries.
 */
internal external interface FlightsData {
    val airlines: Array<Array<JsAny>>
    val airports: Array<Array<JsAny>>
    val routes: Array<Array<Double>>
}

/** Reads one number of an airport row. */
internal fun Array<JsAny>.coordinate(index: Int): Double = this[index].unsafeCast<Double>()
