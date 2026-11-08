package demo.examples.pictorialbar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.TooltipTriggers
import io.github.shijianjs.ktjs.echarts.AxisPointerTypes
import io.github.shijianjs.ktjs.echarts.options.AxisPointerOption
import io.github.shijianjs.ktjs.echarts.options.BooleanOrDoubleOrString
import io.github.shijianjs.ktjs.echarts.options.BooleanOrDouble
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.ItemStyleOption
import io.github.shijianjs.ktjs.echarts.options.LegendOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.SplitLineOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.StringOrDoubleOrArray
import io.github.shijianjs.ktjs.echarts.options.SymbolSize
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.XAxisOption
import io.github.shijianjs.ktjs.echarts.options.YAxisOption
import io.github.shijianjs.ktjs.echarts.options.barSeries
import io.github.shijianjs.ktjs.echarts.options.lineSeries
import io.github.shijianjs.ktjs.echarts.options.numbers
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.pictorialBarSeries
import js.objects.unsafeJso
import io.github.shijianjs.ktjs.zrender.ZRColor
import io.github.shijianjs.ktjs.zrender.gradientColorStop
import io.github.shijianjs.ktjs.zrender.linearGradient
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
                            textStyle = unsafeJso<io.github.shijianjs.ktjs.echarts.options.LabelOption> { color = "#ccc" }
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
                            showAllSymbol = io.github.shijianjs.ktjs.echarts.options.BooleanOrString("auto")
                            symbol = io.github.shijianjs.ktjs.echarts.options.SymbolShape("emptyCircle")
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
