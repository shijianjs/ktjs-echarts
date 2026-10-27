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

/** The four categories of `bar-polar-label-radial` / `bar-polar-label-tangential`. */
private fun polarLabelCategories(): Array<OptionDataItem?> = strings("a", "b", "c", "d")

/**
 * The label of the polar bars of `bar-polar-label-radial` / `bar-polar-label-tangential`.
 *
 * Upstream comments the position as a teaching point: `"middle"`, or `"start"`, `"insideStart"`,
 * `"end"`, `"insideEnd"`.
 */
private fun polarBarLabel(): LabelOption =
    unsafeJso {
        show = true
        position = LabelPosition("middle")
        formatter = LabelFormatter("{b}: {c}")
    }

/** The values of the polar bars of `bar-polar-label-radial` / `bar-polar-label-tangential`. */
private fun polarLabelValues(): Array<BarDataItem?> = numbers(2.0, 1.2, 2.4, 3.6)

/** One series of `bar-polar-stack` / `bar-polar-stack-radial`, which share the `stack` group `a`. */
private fun polarStackSeries(name: String, values: Array<BarDataItem?>): BarSeriesOption =
    barSeries {
        this.name = name
        stack = "a"
        coordinateSystem = "polar"
        emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }
        typedData = values
    }

/** The cities of `bar-polar-real-estate`; they are also the categories of its angle axis. */
private val realEstateCities =
    arrayOf(
        "北京", "上海", "深圳", "广州", "苏州", "杭州", "南京", "福州", "青岛", "济南",
        "长春", "大连", "温州", "郑州", "武汉", "成都", "东莞", "沈阳", "烟台",
    )

/** `[lowest, highest, average]` monthly rent per city of `bar-polar-real-estate`. */
private val realEstateRows =
    arrayOf(
        doubleArrayOf(5000.0, 10000.0, 6785.71),
        doubleArrayOf(4000.0, 10000.0, 6825.0),
        doubleArrayOf(3000.0, 6500.0, 4463.33),
        doubleArrayOf(2500.0, 5600.0, 3793.83),
        doubleArrayOf(2000.0, 4000.0, 3060.0),
        doubleArrayOf(2000.0, 4000.0, 3222.33),
        doubleArrayOf(2500.0, 4000.0, 3133.33),
        doubleArrayOf(1800.0, 4000.0, 3100.0),
        doubleArrayOf(2000.0, 3500.0, 2750.0),
        doubleArrayOf(2000.0, 3000.0, 2500.0),
        doubleArrayOf(1800.0, 3000.0, 2433.33),
        doubleArrayOf(2000.0, 2700.0, 2375.0),
        doubleArrayOf(1500.0, 2800.0, 2150.0),
        doubleArrayOf(1500.0, 2300.0, 2100.0),
        doubleArrayOf(1600.0, 3500.0, 2057.14),
        doubleArrayOf(1500.0, 2600.0, 2037.5),
        doubleArrayOf(1500.0, 2417.54, 1905.85),
        doubleArrayOf(1500.0, 2000.0, 1775.0),
        doubleArrayOf(1500.0, 1800.0, 1650.0),
    )

/** Thickness of the average band of `bar-polar-real-estate`, in data units. */
private const val REAL_ESTATE_BAR_HEIGHT = 50.0

/**
 * One polar bar of `bar-polar-real-estate`.
 *
 * The chart draws two stacked bands: a transparent base up to the lower value, then the visible
 * segment, so the two series of a band share one `stack` group. [value] picks the number out of the
 * `[lowest, highest, average]` row of one city.
 */
private fun realEstateSeries(
    name: String?,
    value: (DoubleArray) -> Double,
    transparent: Boolean,
    stack: String,
    barGap: String? = null,
    z: Double? = null,
): BarSeriesOption =
    barSeries {
        this.name = name
        this.stack = stack
        coordinateSystem = "polar"
        this.z = z
        typedData = numbers(*realEstateRows.map(value).toDoubleArray())
        if (transparent) {
            silent = true
            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("transparent") }
        }
        if (barGap != null) this.barGap = StringOrDouble(barGap)
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
        barExtra.chart(
            "bar-polar-label-radial",
            "极坐标柱状图标签（角度轴为类别轴）",
            "Radial Polar Bar Label Position",
        ) {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Radial Polar Bar Label Position (middle)" })
                polar =
                    one(
                        unsafeJso<PolarOption> {
                            radius = StringOrDoubleOrArray(arrayOf(StringOrDouble(30.0), StringOrDouble("80%")))
                        },
                    )
                radiusAxis = one(unsafeJso<RadiusAxisOption> { max = AxisMinMax(4.0) })
                angleAxis =
                    one(
                        unsafeJso<AngleAxisOption> {
                            type = AxisTypes.category
                            data = polarLabelCategories()
                            startAngle = 75.0
                        },
                    )
                tooltip = one(unsafeJso<TooltipOption> {})
                series =
                    one(
                        barSeries {
                            coordinateSystem = "polar"
                            label = polarBarLabel()
                            typedData = polarLabelValues()
                        },
                    )
                animation = false
            }
        },
        barExtra.chart(
            "bar-polar-label-tangential",
            "极坐标柱状图标签（半径轴为类别轴）",
            "Tangential Polar Bar Label Position",
        ) {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Tangential Polar Bar Label Position (middle)" })
                polar =
                    one(
                        unsafeJso<PolarOption> {
                            radius = StringOrDoubleOrArray(arrayOf(StringOrDouble(30.0), StringOrDouble("80%")))
                        },
                    )
                angleAxis =
                    one(
                        unsafeJso<AngleAxisOption> {
                            max = AxisMinMax(4.0)
                            startAngle = 75.0
                        },
                    )
                radiusAxis =
                    one(
                        unsafeJso<RadiusAxisOption> {
                            type = AxisTypes.category
                            data = polarLabelCategories()
                        },
                    )
                tooltip = one(unsafeJso<TooltipOption> {})
                series =
                    one(
                        barSeries {
                            coordinateSystem = "polar"
                            label = polarBarLabel()
                            typedData = polarLabelValues()
                        },
                    )
            }
        },
        barExtra.chart("bar-polar-stack", "极坐标系下的堆叠柱状图", "Stacked Bar Chart on Polar") {
            unsafeJso<EChartsOption> {
                angleAxis = one(unsafeJso<AngleAxisOption> {})
                radiusAxis =
                    one(
                        unsafeJso<RadiusAxisOption> {
                            type = AxisTypes.category
                            data = strings("Mon", "Tue", "Wed", "Thu")
                            z = 10.0
                        },
                    )
                polar = one(unsafeJso<PolarOption> {})
                series =
                    arrayOf<SeriesOption>(
                        polarStackSeries("A", numbers(1.0, 2.0, 3.0, 4.0)),
                        polarStackSeries("B", numbers(2.0, 4.0, 6.0, 8.0)),
                        polarStackSeries("C", numbers(1.0, 2.0, 3.0, 4.0)),
                    )
                legend = one(unsafeJso<LegendOption> { show = true; data = legendNames("A", "B", "C") })
            }
        },
        barExtra.chart(
            "bar-polar-stack-radial",
            "极坐标系下的堆叠柱状图（径向）",
            "Stacked Bar Chart on Polar (Radial)",
        ) {
            unsafeJso<EChartsOption> {
                angleAxis =
                    one(
                        unsafeJso<AngleAxisOption> {
                            type = AxisTypes.category
                            data = strings("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                        },
                    )
                radiusAxis = one(unsafeJso<RadiusAxisOption> {})
                polar = one(unsafeJso<PolarOption> {})
                series =
                    arrayOf<SeriesOption>(
                        polarStackSeries("A", numbers(1.0, 2.0, 3.0, 4.0, 3.0, 5.0, 1.0)),
                        polarStackSeries("B", numbers(2.0, 4.0, 6.0, 1.0, 3.0, 2.0, 1.0)),
                        polarStackSeries("C", numbers(1.0, 2.0, 3.0, 4.0, 1.0, 2.0, 5.0)),
                    )
                legend = one(unsafeJso<LegendOption> { show = true; data = legendNames("A", "B", "C") })
            }
        },
        barExtra.chart("bar-polar-real-estate", "极坐标系下的房租区间图", "Bar Chart on Polar") {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "How expensive is it to rent an apartment in China?"
                            subtext = "Data from https://www.numbeo.com"
                        },
                    )
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            show = true
                            top = StringOrDouble("bottom")
                            data = legendNames("Range", "Average")
                        },
                    )
                grid = one(unsafeJso<GridOption> { top = StringOrDouble(100.0) })
                angleAxis =
                    one(
                        unsafeJso<AngleAxisOption> {
                            type = AxisTypes.category
                            data = strings(*realEstateCities)
                        },
                    )
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            show = true
                            formatter =
                                TooltipFormatter { params ->
                                    val index = params.unsafeCast<CallbackDataParams>().dataIndex
                                    val row = realEstateRows[index]
                                    TooltipContent(
                                        realEstateCities[index] +
                                            "<br>Lowest：" + textOf(row[0]) +
                                            "<br>Highest：" + textOf(row[1]) +
                                            "<br>Average：" + textOf(row[2]),
                                    )
                                }
                        },
                    )
                radiusAxis = one(unsafeJso<RadiusAxisOption> {})
                polar = one(unsafeJso<PolarOption> {})
                series =
                    arrayOf<SeriesOption>(
                        realEstateSeries(null, { it[0] }, transparent = true, stack = "Min Max"),
                        realEstateSeries("Range", { it[1] - it[0] }, transparent = false, stack = "Min Max"),
                        realEstateSeries(
                            null,
                            { it[2] - REAL_ESTATE_BAR_HEIGHT },
                            transparent = true,
                            stack = "Average",
                            z = 10.0,
                        ),
                        realEstateSeries(
                            "Average",
                            { REAL_ESTATE_BAR_HEIGHT * 2 },
                            transparent = false,
                            stack = "Average",
                            barGap = "-100%",
                            z = 10.0,
                        ),
                    )
            }
        },
        barExtra.chart("polar-roundCap", "圆角极坐标柱状图", "Rounded Bar on Polar") {
            unsafeJso<EChartsOption> {
                angleAxis =
                    one(
                        unsafeJso<AngleAxisOption> {
                            max = AxisMinMax(2.0)
                            startAngle = 30.0
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                        },
                    )
                radiusAxis =
                    one(
                        unsafeJso<RadiusAxisOption> {
                            type = AxisTypes.category
                            data = strings("v", "w", "x", "y", "z")
                            z = 10.0
                        },
                    )
                polar = one(unsafeJso<PolarOption> {})
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            name = "Without Round Cap"
                            coordinateSystem = "polar"
                            typedData = numbers(4.0, 3.0, 2.0, 1.0, 0.0)
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    borderColor = ZRColor("red")
                                    opacity = 0.8
                                    borderWidth = 1.0
                                }
                        },
                        barSeries {
                            name = "With Round Cap"
                            coordinateSystem = "polar"
                            roundCap = true
                            typedData = numbers(4.0, 3.0, 2.0, 1.0, 0.0)
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    borderColor = ZRColor("green")
                                    opacity = 0.8
                                    borderWidth = 1.0
                                }
                        },
                    )
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            show = true
                            data = legendNames("Without Round Cap", "With Round Cap")
                        },
                    )
            }
        },
        barExtra.chart("polar-endAngle", "极坐标系 endAngle", "Polar endAngle") {
            unsafeJso<EChartsOption> {
                tooltip = one(unsafeJso<TooltipOption> {})
                angleAxis =
                    arrayOf(
                        unsafeJso<AngleAxisOption> {
                            type = AxisTypes.category
                            polarIndex = 0.0
                            startAngle = 90.0
                            endAngle = 0.0
                            data = strings("S1", "S2", "S3")
                        },
                        unsafeJso<AngleAxisOption> {
                            type = AxisTypes.category
                            polarIndex = 1.0
                            startAngle = -90.0
                            endAngle = -180.0
                            data = strings("T1", "T2", "T3")
                        },
                    )
                radiusAxis =
                    arrayOf(
                        unsafeJso<RadiusAxisOption> { polarIndex = 0.0 },
                        unsafeJso<RadiusAxisOption> { polarIndex = 1.0 },
                    )
                polar = arrayOf(unsafeJso<PolarOption> {}, unsafeJso<PolarOption> {})
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            polarIndex = 0.0
                            coordinateSystem = "polar"
                            typedData = numbers(1.0, 2.0, 3.0)
                        },
                        barSeries {
                            polarIndex = 1.0
                            coordinateSystem = "polar"
                            typedData = numbers(1.0, 2.0, 3.0)
                        },
                    )
            }
        },
    )
