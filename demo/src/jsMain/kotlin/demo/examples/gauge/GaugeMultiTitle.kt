package demo.examples.gauge

import demo.CatalogEntry
import demo.Groups
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.js
import kotlin.math.round
import demo.examples.Category
import demo.examples.shared.Lcg

private val gaugeMultiTitleCategory = Category(Groups.GAUGE, "examples/gauge/GaugeMultiTitle.kt")

val gaugeMultiTitleEntry: CatalogEntry =
    gaugeMultiTitleCategory.interactive("gauge-multi-title", "多标题仪表盘", "Multi Title Gauge") { chart, holder ->
            val values = doubleArrayOf(20.0, 40.0, 60.0)
            chart.setOption(multiTitleOption(values))
            val random = Lcg(17)
            holder.setInterval({
                if (chart.isDisposed()) return@setInterval
                for (index in values.indices) {
                    values[index] = round(random.next() * 100 * 100) / 100
                }
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        series = arrayOf<SeriesOption>(gaugeSeries { typedData = multiTitleItems(values) })
                    },
                )
            }, 2000)
        }
