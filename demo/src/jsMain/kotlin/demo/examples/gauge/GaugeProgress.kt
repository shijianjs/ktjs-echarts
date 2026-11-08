package demo.examples.gauge

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.GaugeAnchorOption
import io.github.shijianjs.ktjs.echarts.options.GaugeAxisLabelOption
import io.github.shijianjs.ktjs.echarts.options.GaugeAxisLineOption
import io.github.shijianjs.ktjs.echarts.options.GaugeAxisLineStyleOption
import io.github.shijianjs.ktjs.echarts.options.GaugeAxisTickOption
import io.github.shijianjs.ktjs.echarts.options.GaugeDetailOption
import io.github.shijianjs.ktjs.echarts.options.GaugeProgressOption
import io.github.shijianjs.ktjs.echarts.options.GaugeSplitLineOption
import io.github.shijianjs.ktjs.echarts.options.GaugeTitleOption
import io.github.shijianjs.ktjs.echarts.options.ItemStyleOption
import io.github.shijianjs.ktjs.echarts.options.LineStyleOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.gaugeSeries
import js.objects.unsafeJso
import kotlin.js.js
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.shared.axisLabel

private val gaugeProgressCategory = Category(Groups.GAUGE, "examples/gauge/GaugeProgress.kt")

val gaugeProgressEntry: CatalogEntry =
    gaugeProgressCategory.chart("gauge-progress", "进度仪表盘", "Progress Gauge") {
            unsafeJso<EChartsOption> {
                series =
                    arrayOf<SeriesOption>(
                        gaugeSeries {
                            progress = unsafeJso<GaugeProgressOption> { show = true; width = 18.0 }
                            axisLine = unsafeJso<GaugeAxisLineOption> { lineStyle = unsafeJso<GaugeAxisLineStyleOption> { width = 18.0 } }
                            axisTick = unsafeJso<GaugeAxisTickOption> { show = false }
                            splitLine =
                                unsafeJso<GaugeSplitLineOption> {
                                    length = 15.0
                                    lineStyle = unsafeJso<LineStyleOption> { width = 2.0; color = ZRColor("#999") }
                                }
                            axisLabel =
                                unsafeJso<GaugeAxisLabelOption> {
                                    distance = 25.0
                                    color = "#999"
                                    fontSize = StringOrDouble(20.0)
                                }
                            anchor =
                                unsafeJso<GaugeAnchorOption> {
                                    show = true
                                    showAbove = true
                                    size = 25.0
                                    itemStyle = unsafeJso<ItemStyleOption> { borderWidth = 10.0 }
                                }
                            title = unsafeJso<GaugeTitleOption> { show = false }
                            detail =
                                unsafeJso<GaugeDetailOption> {
                                    valueAnimation = true
                                    fontSize = StringOrDouble(80.0)
                                    offsetCenter = center("0", "70%")
                                }
                            typedData = arrayOf(gaugeData(null, 70.0))
                        },
                    )
            }
        }
