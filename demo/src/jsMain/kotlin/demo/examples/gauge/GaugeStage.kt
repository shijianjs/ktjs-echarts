package demo.examples.gauge

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.shared.axisLabel

private val gaugeStageCategory = Category(Groups.GAUGE, "examples/gauge/GaugeStage.kt")

val gaugeStageEntry: CatalogEntry =
    gaugeStageCategory.chart("gauge-stage", "阶段速度仪表盘", "Stage Speed Gauge") {
            unsafeJso<EChartsOption> {
                series =
                    arrayOf<SeriesOption>(
                        gaugeSeries {
                            axisLine =
                                unsafeJso<GaugeAxisLineOption> {
                                    lineStyle =
                                        unsafeJso<GaugeAxisLineStyleOption> {
                                            width = 30.0
                                            color =
                                                arrayOf(
                                                    gaugeColorStop(0.3, "#67e0e3"),
                                                    gaugeColorStop(0.7, "#37a2da"),
                                                    gaugeColorStop(1.0, "#fd666d"),
                                                )
                                        }
                                }
                            pointer =
                                unsafeJso<GaugePointerOption> {
                                    itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("auto") }
                                }
                            axisTick =
                                unsafeJso<GaugeAxisTickOption> {
                                    distance = -30.0
                                    length = 8.0
                                    lineStyle =
                                        unsafeJso<LineStyleOption> {
                                            color = ZRColor("#fff")
                                            width = 2.0
                                        }
                                }
                            splitLine =
                                unsafeJso<GaugeSplitLineOption> {
                                    distance = -30.0
                                    length = 30.0
                                    lineStyle =
                                        unsafeJso<LineStyleOption> {
                                            color = ZRColor("#fff")
                                            width = 4.0
                                        }
                                }
                            axisLabel =
                                unsafeJso<GaugeAxisLabelOption> {
                                    color = "inherit"
                                    distance = 40.0
                                    fontSize = StringOrDouble(20.0)
                                }
                            detail =
                                unsafeJso<GaugeDetailOption> {
                                    valueAnimation = true
                                    formatter = LabelFormatter("{value} km/h")
                                    color = "inherit"
                                }
                            typedData = arrayOf(gaugeValue(70.0))
                        },
                    )
            }
        }
