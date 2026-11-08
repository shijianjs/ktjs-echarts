package demo.examples.gauge

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.GaugeDetailOption
import io.github.shijianjs.ktjs.echarts.options.GaugeProgressOption
import io.github.shijianjs.ktjs.echarts.options.LabelFormatter
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.gaugeSeries
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
