package demo.examples.gauge

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.js
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.shared.axisLabel

private val gaugeSpeedCategory = Category(Groups.GAUGE, "examples/gauge/GaugeSpeed.kt")

val gaugeSpeedEntry: CatalogEntry =
    gaugeSpeedCategory.chart("gauge-speed", "速度仪表盘", "Speed Gauge") {
            unsafeJso<EChartsOption> {
                series =
                    arrayOf<SeriesOption>(
                        gaugeSeries {
                            startAngle = 180.0
                            endAngle = 0.0
                            min = 0.0
                            max = 240.0
                            splitNumber = 12.0
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    color = ZRColor("#58D9F9")
                                    shadowColor = ZRColor("rgba(0,138,255,0.45)")
                                    shadowBlur = 10.0
                                    shadowOffsetX = 2.0
                                    shadowOffsetY = 2.0
                                }
                            progress = unsafeJso<GaugeProgressOption> { show = true; roundCap = true; width = 18.0 }
                            pointer =
                                unsafeJso<GaugePointerOption> {
                                    icon = WIDE_NEEDLE_PATH
                                    length = StringOrDouble("75%")
                                    width = 16.0
                                    offsetCenter = gaugeCenter("5%")
                                }
                            axisLine =
                                unsafeJso<GaugeAxisLineOption> {
                                    roundCap = true
                                    lineStyle = unsafeJso<GaugeAxisLineStyleOption> { width = 18.0 }
                                }
                            axisTick =
                                unsafeJso<GaugeAxisTickOption> {
                                    splitNumber = 2.0
                                    lineStyle = unsafeJso<LineStyleOption> { width = 2.0; color = ZRColor("#999") }
                                }
                            splitLine =
                                unsafeJso<GaugeSplitLineOption> {
                                    length = 12.0
                                    lineStyle = unsafeJso<LineStyleOption> { width = 3.0; color = ZRColor("#999") }
                                }
                            axisLabel =
                                unsafeJso<GaugeAxisLabelOption> {
                                    distance = 30.0
                                    color = "#999"
                                    fontSize = StringOrDouble(20.0)
                                }
                            title = unsafeJso<GaugeTitleOption> { show = false }
                            detail =
                                unsafeJso<GaugeDetailOption> {
                                    backgroundColor = ZRColor("#fff")
                                    borderColor = "#999"
                                    borderWidth = 2.0
                                    width = StringOrDouble("60%")
                                    lineHeight = 40.0
                                    height = 40.0
                                    borderRadius = DoubleOrDoubleArray(8.0)
                                    offsetCenter = gaugeCenter("35%")
                                    valueAnimation = true
                                    formatter = LabelFormatter(speedReadout)
                                    rich = speedRich
                                }
                            typedData = arrayOf(dialItem(100.0))
                        },
                    )
            }
        }
