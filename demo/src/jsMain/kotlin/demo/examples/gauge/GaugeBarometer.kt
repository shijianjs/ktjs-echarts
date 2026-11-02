package demo.examples.gauge

import demo.CatalogEntry
import demo.Groups
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.js
import kotlin.math.round
import demo.examples.Category
import demo.examples.shared.Lcg

private val gaugeBarometerCategory = Category(Groups.GAUGE, "examples/gauge/GaugeBarometer.kt")

val gaugeBarometerEntry: CatalogEntry =
    gaugeBarometerCategory.interactive("gauge-barometer", "气压表", "Gauge Barometer") { chart, holder ->
            chart.setOption(barometerOption(58.46))
            val random = Lcg(31)
            holder.setInterval({
                if (chart.isDisposed()) return@setInterval
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        series =
                            arrayOf<SeriesOption>(
                                gaugeSeries {
                                    typedData = arrayOf(dialItem(round(random.next() * 100 * 100) / 100, "PLP"))
                                },
                            )
                    },
                )
            }, 2000)
        }
