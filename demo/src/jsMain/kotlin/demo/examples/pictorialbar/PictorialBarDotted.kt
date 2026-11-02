package demo.examples.pictorialbar

import demo.CatalogEntry
import demo.Groups
import echarts.TooltipTriggers
import echarts.AxisPointerTypes
import echarts.options.AxisPointerOption
import echarts.options.BooleanOrDoubleOrString
import echarts.options.BooleanOrDouble
import echarts.options.EChartsOption
import echarts.options.ItemStyleOption
import echarts.options.LegendOption
import echarts.options.SeriesOption
import echarts.options.SplitLineOption
import echarts.options.StringOrDouble
import echarts.options.StringOrDoubleOrArray
import echarts.options.SymbolSize
import echarts.options.TooltipOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.barSeries
import echarts.options.lineSeries
import echarts.options.numbers
import echarts.options.one
import echarts.options.pictorialBarSeries
import js.objects.unsafeJso
import zrender.ZRColor
import zrender.gradientColorStop
import zrender.linearGradient
import demo.examples.Category
import demo.examples.shared.legendNames

private val pictorialBarDottedCategory = Category(Groups.PICTORIAL_BAR, "examples/pictorialbar/PictorialBarDotted.kt")

val pictorialBarDottedEntry: CatalogEntry =
    pictorialBarDottedCategory.chart("pictorialBar-dotted", "虚线柱状图效果", "Dotted bar") {
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
        }
