package demo.examples.shared

import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*

/** One row of the `candlestick-large` dataset. */
internal class CandleRow(
    val label: String,
    val open: Double,
    val highest: Double,
    val lowest: Double,
    val close: Double,
    val volume: Double,
    val sign: Double,
)
