package demo.json.examples.doc

import io.github.shijianjs.ktjs.echarts.json.*

/** The star of `pictorialBar-symbolBoundingDataArray`. */
internal const val STAR_SYMBOL =
    "path://m15.5,19c-0.082,0 -0.164,-0.02 -0.239,-0.061l-5.261,-2.869l-5.261,2.869c-0.168,0.092 -0.373,0.079 -0.529,-0.032s-0.235,-0.301 -0.203,-0.49l0.958,-5.746l-3.818,-3.818c-0.132,-0.132 -0.18,-0.328 -0.123,-0.506s0.209,-0.31 0.394,-0.341l5.749,-0.958l2.386,-4.772c0.085,-0.169 0.258,-0.276 0.447,-0.276s0.363,0.107 0.447,0.276l2.386,4.772l5.749,0.958c0.185,0.031 0.337,0.162 0.394,0.341s0.01,0.374 -0.123,0.506l-3.818,3.818l0.958,5.746c0.031,0.189 -0.048,0.379 -0.203,0.49c-0.086,0.061 -0.188,0.093 -0.29,0.093z"

/** Hides every part of an axis; the text pages draw on invisible `-1..1` axes. */
internal fun AxisOption.hidden() {
    axisLabel { show = false }
    axisLine { show = false }
    splitLine { show = false }
    axisTick { show = false }
}

/** One background or filled star series of the pictorial page. */
internal fun SeriesBuilder.starSeries(name: String, values: List<DataValue>, block: SeriesOption.() -> Unit) {
    pictorialBar {
        this.name = name
        symbol = STAR_SYMBOL
        symbolSize = NumberOrStringOrNumbers.of(str("80%"), str("80%"))
        data = values
        block()
    }
}
