package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.TooltipTriggers
import echarts.options.AxisLineOption
import echarts.AxisPointerTypes
import echarts.options.AxisPointerOption
import echarts.options.BooleanOrDoubleOrString
import echarts.options.BooleanOrDouble
import echarts.options.EChartsOption
import echarts.options.ItemStyleOption
import echarts.options.LegendOption
import echarts.options.LineStyleOption
import echarts.options.SeriesOption
import echarts.options.SplitLineOption
import echarts.options.StringOrDouble
import echarts.options.StringOrDoubleOrArray
import echarts.options.OptionDataItem
import echarts.options.SymbolSize
import echarts.options.TooltipOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.barSeries
import echarts.options.lineSeries
import echarts.options.numbers
import echarts.options.strings
import echarts.options.one
import echarts.options.pictorialBarSeries
import echarts.options.strings
import js.objects.unsafeJso
import zrender.ZRColor
import zrender.gradientColorStop
import zrender.linearGradient

/*
 * Pictorial bar examples ported from `public/examples/ts/pictorialBar-*.ts` of apache/echarts-examples.
 *
 * `pictorialBar-forest` and the other image-backed bars are not ported: they draw with `image://`
 * symbols of the example server, which this page does not ship.
 */

private val pictorialBar = Category(Groups.PICTORIAL_BAR, "examples/PictorialBar.kt")

/** The 20 dates of `pictorialBar-dotted`, starting from a fixed day. */
private val dottedDays: List<String> =
    (0 until 20).map { index -> "2026/10/${(27 + index).coerceAtMost(31).toString().padStart(2, '0')}" }

/** The bar values; the original uses `Math.random()`, a fixed sequence keeps the page stable. */
private val dottedBarValues: DoubleArray =
    DoubleArray(20) { index -> ((index * 37 + 11) % 200).toDouble() }

/** The line values: the same sequence plus a smaller one. */
private val dottedLineValues: DoubleArray =
    DoubleArray(20) { index -> dottedBarValues[index] + ((index * 53 + 7) % 200).toDouble() }

private val dottedLabels: Array<OptionDataItem?> = strings(*dottedDays.toTypedArray())

private val dottedAxisLine = unsafeJso<AxisLineOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("#ccc") } }

val pictorialBarEntries: List<CatalogEntry> =
    listOf(
        pictorialBar.chart("pictorialBar-dotted", "虚线柱状图效果", "Dotted bar") {
            unsafeJso<EChartsOption> {
                backgroundColor = ZRColor("#0f375f")
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.shadow }
                        },
                    )
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            data = legendNames("line", "bar")
                            textStyle = unsafeJso<echarts.options.LabelOption> { color = "#ccc" }
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            data = dottedLabels
                            axisLine = dottedAxisLine
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                            axisLine = dottedAxisLine
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        lineSeries {
                            name = "line"
                            smooth = BooleanOrDouble(true)
                            showAllSymbol = echarts.options.BooleanOrString("auto")
                            symbol = echarts.options.SymbolShape("emptyCircle")
                            symbolSize = SymbolSize(15.0)
                            typedData = numbers(*dottedLineValues)
                        },
                        barSeries {
                            name = "bar"
                            barWidth = StringOrDouble(10.0)
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    borderRadius = StringOrDoubleOrArray(5.0)
                                    color =
                                        linearGradient(
                                            0.0,
                                            0.0,
                                            0.0,
                                            1.0,
                                            arrayOf(
                                                gradientColorStop(0.0, "#14c8d4"),
                                                gradientColorStop(1.0, "#43eec6"),
                                            ),
                                        )
                                }
                            typedData = numbers(*dottedBarValues)
                        },
                        barSeries {
                            name = "line"
                            barGap = StringOrDouble("-100%")
                            barWidth = StringOrDouble(10.0)
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    color =
                                        linearGradient(
                                            0.0,
                                            0.0,
                                            0.0,
                                            1.0,
                                            arrayOf(
                                                gradientColorStop(0.0, "rgba(20,200,212,0.5)"),
                                                gradientColorStop(0.2, "rgba(20,200,212,0.2)"),
                                                gradientColorStop(1.0, "rgba(20,200,212,0)"),
                                            ),
                                        )
                                }
                            z = -12.0
                            typedData = numbers(*dottedLineValues)
                        },
                        pictorialBarSeries {
                            name = "dotted"
                            symbol = "rect"
                            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#0f375f") }
                            symbolRepeat = BooleanOrDoubleOrString(true)
                            symbolSize = StringOrDoubleOrArray(arrayOf(StringOrDouble(12.0), StringOrDouble(4.0)))
                            symbolMargin = StringOrDoubleOrArray(1.0)
                            z = -10.0
                            typedData = numbers(*dottedLineValues)
                        },
                    )
            }
        },
    )
