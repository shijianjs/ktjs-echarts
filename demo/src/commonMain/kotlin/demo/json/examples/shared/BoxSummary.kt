package demo.json.examples.shared

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*

/** One five-number summary plus the outliers of a measurement group. */
internal class BoxSummary(
    val low: Double,
    val q1: Double,
    val median: Double,
    val q3: Double,
    val high: Double,
    val outliers: List<Double>,
)
