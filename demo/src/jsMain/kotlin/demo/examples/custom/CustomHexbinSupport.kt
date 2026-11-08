package demo.examples.custom

import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import kotlin.math.PI
import kotlin.math.round
import kotlin.math.sin
import demo.examples.chord.column
import demo.examples.lines.coordinate
import demo.examples.matrix.dimension
import demo.examples.matrix.names
import demo.examples.scatter.point
import demo.examples.shared.every
import demo.examples.shared.row

/** Shape of the shot table: column names plus one row per shot. */
internal external interface ShotData {
    val schema: Array<String>
    val data: Array<Array<JsAny>>
}

/** One polygon of the court outline. */
internal external interface CourtShape {
    val type: String
    val points: Array<Array<Double>>
}

/** Shape of the court table: the border map plus the line segments. */
internal external interface CourtData {
    val borderGeoJSON: JsAny
    val geometry: Array<CourtShape>
}

/** One hexagonal bin: its centre and the shots it holds. */
internal class HexBin(val x: Double, val y: Double) {
    val points = mutableListOf<DoubleArray>()
}

/** Result of the binning: the largest bin and every bin. */
internal class HexBinResult(val maxBinLen: Int, val bins: List<HexBin>)

/**
 * Port of the `hexBinStatistics` helper of the example (itself based on d3-hexbin).
 *
 * Each point is `[locX, locY, made]`; the returned bins carry their centre in the same coordinate
 * space as the input.
 */
internal fun hexBinStatistics(points: List<DoubleArray>, r: Double): HexBinResult {
    val dx = r * 2 * sin(PI / 3)
    val dy = r * 1.5
    val binsById = mutableMapOf<String, HexBin>()
    val bins = mutableListOf<HexBin>()
    for (point in points) {
        val rawX = point[0]
        val rawY = point[1]
        if (rawX.isNaN() || rawY.isNaN()) continue
        var py = rawY / dy
        var pj = round(py).toInt()
        var px = rawX / dx - (pj and 1) / 2.0
        var pi = round(px).toInt()
        val py1 = py - pj
        if (kotlin.math.abs(py1) * 3 > 1) {
            val px1 = px - pi
            val pi2 = pi + if (px < pi) -1 else 1
            val pj2 = pj + if (py < pj) -1 else 1
            val px2 = px - pi2
            val py2 = py - pj2
            if (px1 * px1 + py1 * py1 > px2 * px2 + py2 * py2) {
                pi = (pi2 + if (pj and 1 != 0) 1 else -1) / 2
                pj = pj2
            }
        }
        val id = "$pi-$pj"
        val existing = binsById[id]
        if (existing != null) {
            existing.points += point
        } else {
            val bin = HexBin((pi + (pj and 1) / 2.0) * dx, pj * dy)
            bin.points += point
            binsById[id] = bin
            bins += bin
        }
    }
    return HexBinResult(bins.size, bins)
}

/** Reads one dimension of the current data item as a number. */
internal fun CustomSeriesRenderItemAPI.numberValue(index: Int): Double =
    value(DimensionLoose(index.toDouble())).unsafeCast<Double>()
