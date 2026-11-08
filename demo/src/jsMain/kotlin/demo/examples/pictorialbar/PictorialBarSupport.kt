package demo.examples.pictorialbar

import io.github.shijianjs.ktjs.echarts.options.AxisLineOption
import io.github.shijianjs.ktjs.echarts.options.LineStyleOption
import io.github.shijianjs.ktjs.echarts.options.OptionDataItem
import io.github.shijianjs.ktjs.echarts.options.strings
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.strings
import js.objects.unsafeJso
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.shared.fixed

/** The 20 dates of `pictorialBar-dotted`, starting from a fixed day. */
internal val dottedDays: List<String> =
    (0 until 20).map { index -> "2026/10/${(27 + index).coerceAtMost(31).toString().padStart(2, '0')}" }

/** The bar values; the original uses `Math.random()`, a fixed sequence keeps the page stable. */
internal val dottedBarValues: DoubleArray =
    DoubleArray(20) { index -> ((index * 37 + 11) % 200).toDouble() }

/** The line values: the same sequence plus a smaller one. */
internal val dottedLineValues: DoubleArray =
    DoubleArray(20) { index -> dottedBarValues[index] + ((index * 53 + 7) % 200).toDouble() }

internal val dottedLabels: Array<OptionDataItem?> = strings(*dottedDays.toTypedArray())

internal val dottedAxisLine = unsafeJso<AxisLineOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("#ccc") } }
