package demo.examples.gauge

import demo.CatalogEntry
import demo.Groups
import echarts.options.EChartsOption
import echarts.options.GaugeDetailOption
import echarts.options.LabelFormatter
import echarts.options.SeriesOption
import echarts.options.gaugeSeries
import js.objects.unsafeJso
import kotlin.js.js
import demo.examples.Category

private val gaugeCategory = Category(Groups.GAUGE, "examples/gauge/Gauge.kt")

val gaugeEntry: CatalogEntry =
    gaugeCategory.chart("gauge", "基础仪表盘", "Gauge Basic chart") {
            unsafeJso<EChartsOption> {
                tooltip = gaugeTooltip
                series =
                    arrayOf<SeriesOption>(
                        gaugeSeries {
                            name = "Pressure"
                            detail = unsafeJso<GaugeDetailOption> { formatter = LabelFormatter("{value}") }
                            typedData = arrayOf(gaugeData("SCORE", 50.0))
                        },
                    )
            }
        }
