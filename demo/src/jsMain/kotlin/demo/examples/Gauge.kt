package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.TooltipTriggers
import echarts.options.EChartsOption
import echarts.options.GaugeAnchorOption
import echarts.options.GaugeAxisLabelOption
import echarts.options.GaugeAxisLineOption
import echarts.options.GaugeAxisLineStyleOption
import echarts.options.GaugeAxisTickOption
import echarts.options.GaugeDataItem
import echarts.options.GaugeDataItemOption
import echarts.options.GaugeDetailOption
import echarts.options.GaugePointerOption
import echarts.options.GaugeProgressOption
import echarts.options.GaugeSplitLineOption
import echarts.options.GaugeTitleOption
import echarts.options.ItemStyleOption
import echarts.options.LabelFormatter
import echarts.options.LabelFormatterCallback
import echarts.options.LineStyleOption
import echarts.options.OptionDataValue
import echarts.options.OptionId
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.TooltipFormatter
import echarts.options.TooltipOption
import echarts.options.gaugeColorStop
import echarts.options.gaugeSeries
import echarts.options.one
import js.objects.unsafeJso
import kotlin.js.js
import zrender.ZRColor

/*
 * Gauge examples ported from `public/examples/ts/gauge*.ts` of apache/echarts-examples.
 *
 * The gauge label callbacks are typed `(value: number) => string` upstream, while the wrapper maps
 * every `formatter` to `LabelFormatter` (template or `CallbackDataParams` callback). The callbacks
 * below therefore go through a small JavaScript adapter, which the surrounding KDoc explains.
 */

private val gauge = Category(Groups.GAUGE, "examples/Gauge.kt")

/** A gauge data item, optionally with its own title and detail blocks (`gauge-ring`). */
private fun gaugeData(
    name: String?,
    value: Double,
    title: GaugeTitleOption? = null,
    detail: GaugeDetailOption? = null,
): GaugeDataItem =
    GaugeDataItem(
        unsafeJso<GaugeDataItemOption> {
            if (name != null) this.name = OptionId(name)
            this.value = OptionDataValue(value)
            this.title = title
            this.detail = detail
        },
    )

/** `offsetCenter` of a title or detail block. */
private fun center(vararg values: String): Array<StringOrDouble> = Array(values.size) { StringOrDouble(values[it]) }

/** The `{a} <br/>{b} : {c}%` tooltip of the basic gauges. */
private val gaugeTooltip =
    one(unsafeJso<TooltipOption> { formatter = TooltipFormatter("{a} <br/>{b} : {c}%") })

/** `Math.round(value * 100) + ''`, the detail formatter of `gauge-grade`. */
private val roundedPercent =
    js("(value) => Math.round(value * 100) + ''") as LabelFormatterCallback

/** The grade letters of `gauge-grade`: only the four split values have a label. */
private val gradeLabels =
    js(
        "(value) => value === 0.875 ? 'Grade A' : value === 0.625 ? 'Grade B' : " +
            "value === 0.375 ? 'Grade C' : value === 0.125 ? 'Grade D' : ''",
    ) as LabelFormatterCallback

val gaugeEntries: List<CatalogEntry> =
    listOf(
        gauge.chart("gauge", "基础仪表盘", "Gauge Basic chart") {
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
        },
        gauge.chart("gauge-simple", "带标签数字动画的基础仪表盘", "Simple Gauge") {
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
        },
        gauge.chart("gauge-progress", "进度仪表盘", "Progress Gauge") {
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
        },
        gauge.chart("gauge-grade", "等级仪表盘", "Grade Gauge") {
            unsafeJso<EChartsOption> {
                series =
                    arrayOf<SeriesOption>(
                        gaugeSeries {
                            startAngle = 180.0
                            endAngle = 0.0
                            center = center("50%", "75%")
                            radius = StringOrDouble("90%")
                            min = 0.0
                            max = 1.0
                            splitNumber = 8.0
                            axisLine =
                                unsafeJso<GaugeAxisLineOption> {
                                    lineStyle =
                                        unsafeJso<GaugeAxisLineStyleOption> {
                                            width = 6.0
                                            color =
                                                arrayOf(
                                                    gaugeColorStop(0.25, "#FF6E76"),
                                                    gaugeColorStop(0.5, "#FDDD60"),
                                                    gaugeColorStop(0.75, "#58D9F9"),
                                                    gaugeColorStop(1.0, "#7CFFB2"),
                                                )
                                        }
                                }
                            pointer =
                                unsafeJso<GaugePointerOption> {
                                    icon = "path://M12.8,0.7l12,40.1H0.7L12.8,0.7z"
                                    length = StringOrDouble("12%")
                                    width = 20.0
                                    offsetCenter = center("0", "-60%")
                                    itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("auto") }
                                }
                            axisTick =
                                unsafeJso<GaugeAxisTickOption> {
                                    length = 12.0
                                    lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("auto"); width = 2.0 }
                                }
                            splitLine =
                                unsafeJso<GaugeSplitLineOption> {
                                    length = 20.0
                                    lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("auto"); width = 5.0 }
                                }
                            axisLabel =
                                unsafeJso<GaugeAxisLabelOption> {
                                    color = "#464646"
                                    fontSize = StringOrDouble(20.0)
                                    distance = -60.0
                                    typedRotate = echarts.options.LabelRotates.tangential
                                    formatter = LabelFormatter(gradeLabels)
                                }
                            title = unsafeJso<GaugeTitleOption> { offsetCenter = center("0", "-10%"); fontSize = StringOrDouble(20.0) }
                            detail =
                                unsafeJso<GaugeDetailOption> {
                                    fontSize = StringOrDouble(30.0)
                                    offsetCenter = center("0", "-35%")
                                    valueAnimation = true
                                    formatter = LabelFormatter(roundedPercent)
                                    color = "inherit"
                                }
                            typedData = arrayOf(gaugeData("Grade Rating", 0.7))
                        },
                    )
            }
        },
        gauge.interactive("gauge-ring", "得分环", "Ring Gauge") { chart ->
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
        },
    )
