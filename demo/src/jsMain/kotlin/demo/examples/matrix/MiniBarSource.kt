package demo.examples.matrix

import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*

/** One year of `matrix-mini-bar-geo`; the year is the series name of the mini bars. */
internal data class MiniBarSource(val year: String, val rows: List<MiniBarRow>)
