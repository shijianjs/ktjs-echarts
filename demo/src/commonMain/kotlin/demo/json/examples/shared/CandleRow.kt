package demo.json.examples.shared

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*

/** One trading day: its date, its four prices and its volume. */
internal class CandleRow(
    val date: String,
    val open: Double,
    val close: Double,
    val lowest: Double,
    val highest: Double,
    val volume: Double,
)
