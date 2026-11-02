package demo.examples.gauge

import demo.CatalogEntry
import demo.Groups
import echarts.options.EChartsOption
import echarts.options.GaugeDetailOption
import echarts.options.GaugeProgressOption
import echarts.options.LabelFormatter
import echarts.options.SeriesOption
import echarts.options.gaugeSeries
import js.objects.unsafeJso
import kotlin.js.js
import demo.examples.Category

private val gaugeSimpleCategory = Category(Groups.GAUGE, "examples/gauge/GaugeSimple.kt")

val gaugeSimpleEntry: CatalogEntry =
    gaugeSimpleCategory.chart("gauge-simple", "带标签数字动画的基础仪表盘", "Simple Gauge") {
            unsafeJso<EChartsOption> {
                tooltip = gaugeTooltip
                series =
                    arrayOf<SeriesOption>(
                        gaugeSeries {
                            name = "Pressure"
                            progress = unsafeJso<GaugeProgressOption> { show = true }
                            detail =
                                unsafeJso<GaugeDetailOption> {
                                    valueAnimation = true
                                    formatter = LabelFormatter("{value}")
                                }
                            typedData = arrayOf(gaugeData("SCORE", 50.0))
                        },
                    )
            }
        }
