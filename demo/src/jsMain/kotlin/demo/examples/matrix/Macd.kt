package demo.examples.matrix

import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*

/** The three MACD outputs, all on the timestamps where both the DIF and the signal line exist. */
internal class Macd(
    val bars: List<Pair<Double, Double>>,
    val dif: List<Pair<Double, Double>>,
    val dea: List<Pair<Double, Double>>,
)
