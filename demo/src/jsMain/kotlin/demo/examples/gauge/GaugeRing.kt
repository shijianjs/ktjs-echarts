package demo.examples.gauge

import demo.CatalogEntry
import demo.Groups
import echarts.options.EChartsOption
import echarts.options.GaugeAxisLabelOption
import echarts.options.GaugeAxisLineOption
import echarts.options.GaugeAxisLineStyleOption
import echarts.options.GaugeAxisTickOption
import echarts.options.GaugeDataItem
import echarts.options.GaugeDetailOption
import echarts.options.GaugePointerOption
import echarts.options.GaugeProgressOption
import echarts.options.GaugeSplitLineOption
import echarts.options.GaugeTitleOption
import echarts.options.ItemStyleOption
import echarts.options.LabelFormatter
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.gaugeSeries
import js.objects.unsafeJso
import kotlin.js.js
import zrender.ZRColor
import demo.examples.Category
import demo.examples.shared.axisLabel
import demo.examples.shared.every

private val gaugeRingCategory = Category(Groups.GAUGE, "examples/gauge/GaugeRing.kt")

val gaugeRingEntry: CatalogEntry =
    gaugeRingCategory.interactive("gauge-ring", "得分环", "Ring Gauge") { chart ->
            fun ringItem(name: String, value: Double, titleTop: String, detailTop: String): GaugeDataItem =
                gaugeData(
                    name,
                    value,
                    title = unsafeJso<GaugeTitleOption> { offsetCenter = center("0%", titleTop) },
                    detail = unsafeJso<GaugeDetailOption> { valueAnimation = true; offsetCenter = center("0%", detailTop) },
                )

            val values = doubleArrayOf(20.0, 40.0, 60.0)
            fun option(): EChartsOption =
                unsafeJso {
                    series =
                        arrayOf<SeriesOption>(
                            gaugeSeries {
                                startAngle = 90.0
                                endAngle = -270.0
                                pointer = unsafeJso<GaugePointerOption> { show = false }
                                progress =
                                    unsafeJso<GaugeProgressOption> {
                                        show = true
                                        overlap = false
                                        roundCap = true
                                        clip = false
                                        itemStyle =
                                            unsafeJso<ItemStyleOption> {
                                                borderWidth = 1.0
                                                borderColor = ZRColor("#464646")
                                            }
                                    }
                                axisLine = unsafeJso<GaugeAxisLineOption> { lineStyle = unsafeJso<GaugeAxisLineStyleOption> { width = 40.0 } }
                                splitLine = unsafeJso<GaugeSplitLineOption> { show = false; distance = 0.0; length = 10.0 }
                                axisTick = unsafeJso<GaugeAxisTickOption> { show = false }
                                axisLabel = unsafeJso<GaugeAxisLabelOption> { show = false; distance = 50.0 }
                                title = unsafeJso<GaugeTitleOption> { fontSize = StringOrDouble(14.0) }
                                detail =
                                    unsafeJso<GaugeDetailOption> {
                                        width = StringOrDouble(50.0)
                                        height = 14.0
                                        fontSize = StringOrDouble(14.0)
                                        color = "inherit"
                                        borderColor = "inherit"
                                        borderRadius = echarts.options.DoubleOrDoubleArray(20.0)
                                        borderWidth = 1.0
                                        formatter = LabelFormatter("{value}%")
                                    }
                                typedData =
                                    arrayOf(
                                        ringItem("Perfect", values[0], "-30%", "-20%"),
                                        ringItem("Good", values[1], "0%", "10%"),
                                        ringItem("Commonly", values[2], "30%", "40%"),
                                    )
                            },
                        )
                }

            chart.setOption(option())

            var seed = 7L
            fun nextValue(): Double {
                seed = (seed * 1103515245L + 12345L) and 0x7FFFFFFFL
                return (seed % 10000L).toDouble() / 100.0
            }

            every(2000) {
                if (chart.isDisposed()) return@every
                values[0] = nextValue()
                values[1] = nextValue()
                values[2] = nextValue()
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        series =
                            arrayOf<SeriesOption>(
                                gaugeSeries {
                                    pointer = unsafeJso<GaugePointerOption> { show = false }
                                    typedData =
                                        arrayOf(
                                            ringItem("Perfect", values[0], "-30%", "-20%"),
                                            ringItem("Good", values[1], "0%", "10%"),
                                            ringItem("Commonly", values[2], "30%", "40%"),
                                        )
                                },
                            )
                    },
                )
            }
        }
