package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import zrender.ZRColor
import zrender.ZRFontWeights

/*
 * Gauge examples that the official page keeps animating with `setInterval`.
 *
 * The port shows the initial value of each gauge and drops the timer: the demo builds a page once,
 * and a timer would keep running after the chart is disposed. The option itself is identical.
 *
 * Dropped on purpose: `gauge-clock` and `gauge-speed` draw their pointers from `path://` icons of a
 * few kilobytes, `gauge-barometer` uses an icon as well.
 */

private val gaugeExtra = Category(Groups.GAUGE, "examples/GaugeExtra.kt")

/** `{ value: <value> }`, the single data item every gauge here uses. */
private fun gaugeValue(value: Double): GaugeDataItem =
    GaugeDataItem(unsafeJso<GaugeDataItemOption> { this.value = OptionDataValue(value) })

val gaugeExtraEntries: List<CatalogEntry> =
    listOf(
        gaugeExtra.chart("gauge-stage", "阶段速度仪表盘", "Stage Speed Gauge") {
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
        },
        gaugeExtra.chart("gauge-temperature", "气温仪表盘", "Temperature Gauge") {
            val temperature = 20.0
            unsafeJso<EChartsOption> {
                series =
                    arrayOf<SeriesOption>(
                        gaugeSeries {
                            center = arrayOf(StringOrDouble("50%"), StringOrDouble("60%"))
                            startAngle = 200.0
                            endAngle = -20.0
                            min = 0.0
                            max = 60.0
                            splitNumber = 12.0
                            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#FFAB91") }
                            progress =
                                unsafeJso<GaugeProgressOption> {
                                    show = true
                                    width = 30.0
                                }
                            pointer = unsafeJso<GaugePointerOption> { show = false }
                            axisLine =
                                unsafeJso<GaugeAxisLineOption> {
                                    lineStyle = unsafeJso<GaugeAxisLineStyleOption> { width = 30.0 }
                                }
                            axisTick =
                                unsafeJso<GaugeAxisTickOption> {
                                    distance = -45.0
                                    splitNumber = 5.0
                                    lineStyle =
                                        unsafeJso<LineStyleOption> {
                                            width = 2.0
                                            color = ZRColor("#999")
                                        }
                                }
                            splitLine =
                                unsafeJso<GaugeSplitLineOption> {
                                    distance = -52.0
                                    length = 14.0
                                    lineStyle =
                                        unsafeJso<LineStyleOption> {
                                            width = 3.0
                                            color = ZRColor("#999")
                                        }
                                }
                            axisLabel =
                                unsafeJso<GaugeAxisLabelOption> {
                                    distance = -20.0
                                    color = "#999"
                                    fontSize = StringOrDouble(20.0)
                                }
                            anchor = unsafeJso<GaugeAnchorOption> { show = false }
                            title = unsafeJso<GaugeTitleOption> { show = false }
                            detail =
                                unsafeJso<GaugeDetailOption> {
                                    valueAnimation = true
                                    width = StringOrDouble("60%")
                                    lineHeight = 40.0
                                    borderRadius = DoubleOrDoubleArray(8.0)
                                    offsetCenter = arrayOf(StringOrDouble(0.0), StringOrDouble("-15%"))
                                    fontSize = StringOrDouble(60.0)
                                    fontWeight = ZRFontWeights.bolder
                                    formatter = LabelFormatter("{value} °C")
                                    color = "inherit"
                                }
                            typedData = arrayOf(gaugeValue(temperature))
                        },
                        gaugeSeries {
                            center = arrayOf(StringOrDouble("50%"), StringOrDouble("60%"))
                            startAngle = 200.0
                            endAngle = -20.0
                            min = 0.0
                            max = 60.0
                            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#FD7347") }
                            progress =
                                unsafeJso<GaugeProgressOption> {
                                    show = true
                                    width = 8.0
                                }
                            pointer = unsafeJso<GaugePointerOption> { show = false }
                            axisLine = unsafeJso<GaugeAxisLineOption> { show = false }
                            axisTick = unsafeJso<GaugeAxisTickOption> { show = false }
                            splitLine = unsafeJso<GaugeSplitLineOption> { show = false }
                            axisLabel = unsafeJso<GaugeAxisLabelOption> { show = false }
                            detail = unsafeJso<GaugeDetailOption> { show = false }
                            typedData = arrayOf(gaugeValue(temperature))
                        },
                    )
            }
        },
    )
