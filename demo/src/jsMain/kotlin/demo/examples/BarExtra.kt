package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.Date
import kotlin.js.unsafeCast
import web.cssom.TextAlign
import web.cssom.VerticalAlign
import zrender.ZRColor
import zrender.ZRFontWeights
import zrender.gradientColorStop
import zrender.linearGradient

/*
 * Bar examples whose interesting part is the option tree rather than the data.
 *
 * `bar-label-rotation` ships a control panel on the official page that re-applies the label options
 * on every change; the chart itself is static, so the port keeps the initial values and drops the
 * panel (and with it the `dataView` / `mark` toolbox buttons, which need that panel to be useful).
 *
 * `bar-breaks-simple` adds a "collapse axis breaks" button through the `axisbreakchanged` event on
 * the official page; the port keeps the breaks themselves and drops the button, which is optional
 * interaction logic by the comment of the original.
 */

private val barExtra = Category(Groups.BAR, "examples/BarExtra.kt")

/** The rotated labels shared by the four series of `bar-label-rotation`. */
private fun rotatedLabels(): LabelOption = unsafeJso {
    show = true
    position = LabelPosition("insideBottom")
    distance = 15.0
    align = TextAlign.left
    verticalAlign = VerticalAlign.middle
    rotate = 90.0
    formatter = LabelFormatter("{c}  {name|{a}}")
    fontSize = StringOrDouble(16.0)
    rich = recordOf("name" to unsafeJso<TextStyleOption> {}).unsafeCast<Record<String, TextStyleOption>>()
}

/** The categories of `bar-gradient`, one character per bar. */
private val gradientAxis =
    arrayOf("点", "击", "柱", "子", "或", "者", "两", "指", "在", "触", "屏", "上", "滑", "动", "能", "够", "自", "动", "缩", "放")

/** The values of `bar-gradient`. */
private val gradientValues =
    doubleArrayOf(
        220.0, 182.0, 191.0, 234.0, 290.0, 330.0, 310.0, 123.0, 442.0, 321.0,
        90.0, 149.0, 210.0, 122.0, 133.0, 334.0, 198.0, 123.0, 125.0, 220.0,
    )

/** `dataZoom` action with a value window; upstream types extra payload keys as `any`. */
private external interface DataZoomAction : Payload {
    var startValue: OptionDataValue?
    var endValue: OptionDataValue?
}

/** The `[category, value]` table of `bar-large`. */
private class LargeBarData(val categories: Array<OptionDataItem?>, val values: Array<BarDataItem?>) {
    val count: Int
        get() = values.size
}

/**
 * Generates the data of `bar-large`, following the original generator with a fixed random source.
 *
 * The original stores the values as two-decimal strings; numbers render the same and cost less.
 */
private fun largeBarData(count: Int): LargeBarData {
    val random = Lcg(2011)
    val categories = arrayOfNulls<OptionDataItem>(count)
    val values = arrayOfNulls<BarDataItem>(count)
    var baseValue = random.next() * 1000
    var smallBaseValue = 0.0
    val start = Date(2011, 0, 1).getTime()
    for (index in 0 until count) {
        smallBaseValue =
            if (index % 30 == 0) random.next() * 700 else smallBaseValue + random.next() * 500 - 250
        baseValue += random.next() * 20 - 10
        val value = kotlin.math.max(0.0, kotlin.math.round(baseValue + smallBaseValue) + 3000)
        val time = Date(start + index * 1000.0)
        categories[index] =
            OptionDataItem(OptionDataValue(ECharts.format.formatTime("yyyy-MM-dd\nhh:mm:ss", time, false)))
        values[index] = BarDataItem(OptionDataValue(value))
    }
    return LargeBarData(categories.unsafeCast<Array<OptionDataItem?>>(), values.unsafeCast<Array<BarDataItem?>>())
}

/** One break of the y axis of `bar-breaks-simple`. */
private fun axisBreak(start: Double, end: Double): AxisBreakOption =
    unsafeJso {
        this.start = OptionDataValue(start)
        this.end = OptionDataValue(end)
        gap = StringOrDouble("1.5%")
    }

val barExtraEntries: List<CatalogEntry> =
    listOf(
        barExtra.chart("bar-label-rotation", "柱状图标签旋转", "Bar Label Rotation") {
            unsafeJso<EChartsOption> {
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.shadow }
                        },
                    )
                legend = one(unsafeJso<LegendOption> { data = legendNames("Forest", "Steppe", "Desert", "Wetland") })
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                            data = strings("2012", "2013", "2014", "2015", "2016")
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            name = "Forest"
                            barGap = StringOrDouble("0")
                            label = rotatedLabels()
                            emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }
                            typedData = numbers(320.0, 332.0, 301.0, 334.0, 390.0)
                        },
                        barSeries {
                            name = "Steppe"
                            label = rotatedLabels()
                            emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }
                            typedData = numbers(220.0, 182.0, 191.0, 234.0, 290.0)
                        },
                        barSeries {
                            name = "Desert"
                            label = rotatedLabels()
                            emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }
                            typedData = numbers(150.0, 232.0, 201.0, 154.0, 190.0)
                        },
                        barSeries {
                            name = "Wetland"
                            label = rotatedLabels()
                            emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }
                            typedData = numbers(98.0, 77.0, 101.0, 99.0, 40.0)
                        },
                    )
                color =
                    arrayOf(
                        ZRColor("#5470c6"),
                        ZRColor("#91cc75"),
                        ZRColor("#fac858"),
                        ZRColor("#ee6666"),
                    )
            }
        },
        barExtra.interactive("bar-gradient", "特性示例：渐变色 阴影 点击缩放", "Clickable Column Chart with Gradient") { chart ->
            chart.setOption(
                unsafeJso<EChartsOption> {
                    title =
                        one(
                            unsafeJso<TitleOption> {
                                text = "特性示例：渐变色 阴影 点击缩放"
                                subtext = "Feature Sample: Gradient Color, Shadow, Click Zoom"
                            },
                        )
                    xAxis =
                        one(
                            unsafeJso<XAxisOption> {
                                data = strings(*gradientAxis)
                                axisLabel = unsafeJso<AxisLabelOption> { inside = true; color = "#fff" }
                                axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                                axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
                                z = 10.0
                            },
                        )
                    yAxis =
                        one(
                            unsafeJso<YAxisOption> {
                                axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
                                axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                                axisLabel = unsafeJso<AxisLabelOption> { color = "#999" }
                            },
                        )
                    dataZoom = arrayOf(unsafeJso<DataZoomOption> { type = DataZoomTypes.inside })
                    series =
                        one(
                            barSeries {
                                showBackground = true
                                itemStyle =
                                    unsafeJso<ItemStyleOption> {
                                        color =
                                            linearGradient(
                                                0.0,
                                                0.0,
                                                0.0,
                                                1.0,
                                                arrayOf(
                                                    gradientColorStop(0.0, "#83bff6"),
                                                    gradientColorStop(0.5, "#188df0"),
                                                    gradientColorStop(1.0, "#188df0"),
                                                ),
                                            )
                                    }
                                emphasis =
                                    unsafeJso<EmphasisOption> {
                                        itemStyle =
                                            unsafeJso<ItemStyleOption> {
                                                color =
                                                    linearGradient(
                                                        0.0,
                                                        0.0,
                                                        0.0,
                                                        1.0,
                                                        arrayOf(
                                                            gradientColorStop(0.0, "#2378f7"),
                                                            gradientColorStop(0.7, "#2378f7"),
                                                            gradientColorStop(1.0, "#83bff6"),
                                                        ),
                                                    )
                                            }
                                    }
                                typedData = numbers(*gradientValues)
                            },
                        )
                },
            )
            // The original zooms to six bars around the clicked one.
            chart.on("click") { params ->
                val first = kotlin.math.max(params.dataIndex - 3, 0)
                val last = kotlin.math.min(params.dataIndex + 3, gradientAxis.size - 1)
                chart.dispatchAction(
                    unsafeJso<DataZoomAction> {
                        type = "dataZoom"
                        startValue = OptionDataValue(gradientAxis[first])
                        endValue = OptionDataValue(gradientAxis[last])
                    },
                )
            }
        },
        barExtra.chart("bar-large", "大数据量柱图", "Large Scale Bar Chart") {
            val largeData = largeBarData(500_000)
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = ECharts.format.addCommas(StringOrDouble(largeData.count.toDouble())) + " Data"
                            left = StringOrDouble(10.0)
                        },
                    )
                // The original also configures the `dataZoom` toolbox button (`yAxisIndex: false`)
                // and the pixel ratio of `saveAsImage`; the port keeps the default buttons.
                toolbox = toolbox(dataZoom = true, saveAsImage = true)
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.shadow }
                        },
                    )
                grid = one(unsafeJso<GridOption> { bottom = StringOrDouble(90.0) })
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> { type = DataZoomTypes.inside },
                        unsafeJso<DataZoomOption> { type = DataZoomTypes.slider },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            data = largeData.categories
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                            splitArea = unsafeJso<SplitAreaOption> { show = false }
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> { splitArea = unsafeJso<SplitAreaOption> { show = false } })
                series =
                    one(
                        barSeries {
                            typedData = largeData.values
                            large = true
                        },
                    )
            }
        },
        barExtra.chart("bar-breaks-simple", "断轴上的柱状图", "Bar Chart with Axis Breaks") {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Bar Chart with Axis Breaks"
                            subtext = "Click the break area to expand it"
                            left = StringOrDouble("center")
                            textStyle = unsafeJso<TextStyleOption> { fontSize = StringOrDouble(20.0) }
                            subtextStyle =
                                unsafeJso<TextStyleOption> {
                                    color = "#175ce5"
                                    fontSize = StringOrDouble(15.0)
                                    fontWeight = ZRFontWeights.bold
                                }
                        },
                    )
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.shadow }
                        },
                    )
                legend = one(unsafeJso<LegendOption> {})
                grid = one(unsafeJso<GridOption> { top = StringOrDouble(120.0) })
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            data = strings("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            breaks = arrayOf(axisBreak(5000.0, 100000.0), axisBreak(105000.0, 3100000.0))
                            breakArea =
                                unsafeJso<AxisBreakAreaOption> {
                                    itemStyle = unsafeJso<ItemStyleOption> { opacity = 1.0 }
                                    zigzagZ = 200.0
                                }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            name = "Data A"
                            emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }
                            typedData = numbers(1500.0, 2032.0, 2001.0, 3154.0, 2190.0, 4330.0, 2410.0)
                        },
                        barSeries {
                            name = "Data B"
                            emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }
                            typedData = numbers(1200.0, 1320.0, 1010.0, 1340.0, 900.0, 2300.0, 2100.0)
                        },
                        barSeries {
                            name = "Data C"
                            emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }
                            typedData = numbers(103200.0, 100320.0, 103010.0, 102340.0, 103900.0, 103300.0, 103200.0)
                        },
                        barSeries {
                            name = "Data D"
                            emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }
                            typedData = numbers(3106212.0, 3102118.0, 3102643.0, 3104631.0, 3106679.0, 3100130.0, 3107022.0)
                        },
                    )
            }
        },
    )
