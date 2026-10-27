package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.AnimationEasings
import echarts.AxisPointerTypes
import echarts.AxisTypes
import echarts.TooltipTriggers
import echarts.options.AnimationDelay
import echarts.options.AxisLabelOption
import echarts.options.AxisPointerOption
import echarts.options.BarDataItem
import echarts.options.BarDataItemOption
import echarts.options.BooleanOrString
import echarts.options.BooleanOrStringArray
import echarts.options.EChartsOption
import echarts.EmphasisFocuses
import echarts.options.EmphasisOption
import echarts.options.GridOption
import echarts.options.ItemStyleOption
import echarts.options.LabelOption
import echarts.options.LabelPosition
import echarts.options.LegendOption
import echarts.options.LineStyleOption
import echarts.options.MarkLine2DDataItemDimOption
import echarts.options.MarkLineDataItem
import echarts.options.MarkLineOption
import echarts.MarkerStatisticTypes
import echarts.options.OptionDataValue
import echarts.options.SeriesOption
import echarts.options.SplitLineOption
import echarts.options.SplitLineStyleOption
import echarts.options.StringOrDouble
import echarts.options.TitleOption
import echarts.options.TooltipFormatter
import echarts.options.TooltipFormatterCallback
import echarts.options.TooltipOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.barSeries
import echarts.options.dataItems
import echarts.options.lineSeries
import echarts.options.numbers
import echarts.options.one
import echarts.options.strings
import js.objects.unsafeJso
import kotlin.js.js
import kotlin.math.cos
import kotlin.math.sin
import zrender.ZRColor
import zrender.ZRLineTypes

/*
 * Bar examples ported from `public/examples/ts/bar-*.ts` of apache/echarts-examples.
 *
 * The drills, races and the polar bars are not ported in this pass; the remaining originals of the
 * category are listed in `demo/AGENTS.md`.
 */

private val bar = Category(Groups.BAR, "examples/Bar.kt")

private val week = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

private val shadowPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.shadow }

private fun categoryAxis(vararg names: String): Array<XAxisOption> =
    one(unsafeJso<XAxisOption> { type = AxisTypes.category; data = strings(*names) })

private val valueAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })

private val axisTooltip =
    one(
        unsafeJso<TooltipOption> {
            trigger = TooltipTriggers.axis
            axisPointer = shadowPointer
        },
    )

/** `focus: 'series'` on every series of the stacked examples. */
private val seriesFocus = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }

/** A bar with a single value overridden. */
private fun colouredBar(value: Double, colour: String): BarDataItem =
    BarDataItem(
        unsafeJso<BarDataItemOption> {
            this.value = OptionDataValue(value)
            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor(colour) }
        },
    )

/** `bar-animation-delay` generates 100 categories from a sine and a cosine. */
private fun delayedData(cosine: Boolean): DoubleArray =
    DoubleArray(100) { index ->
        val angle = index / 5.0
        val wave = if (cosine) cos(angle) else sin(angle)
        (wave * (angle - 10.0) + index / 6.0) * 5.0
    }

val barEntries: List<CatalogEntry> =
    listOf(
        bar.chart("bar-simple", "基础柱状图", "Basic Bar") {
            unsafeJso<EChartsOption> {
                xAxis = categoryAxis(*week)
                yAxis = valueAxis
                series = arrayOf<SeriesOption>(barSeries { typedData = numbers(120.0, 200.0, 150.0, 80.0, 70.0, 110.0, 130.0) })
            }
        },
        bar.chart("bar-tick-align", "坐标轴刻度与标签对齐", "Axis Align with Tick") {
            unsafeJso<EChartsOption> {
                tooltip = axisTooltip
                grid =
                    one(
                        unsafeJso<GridOption> {
                            left = StringOrDouble("3%")
                            right = StringOrDouble("4%")
                            bottom = StringOrDouble("3%")
                            containLabel = true
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            data = strings(*week)
                            axisTick = unsafeJso { alignWithLabel = true }
                        },
                    )
                yAxis = valueAxis
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            name = "Direct"
                            barWidth = StringOrDouble("60%")
                            typedData = numbers(10.0, 52.0, 200.0, 334.0, 390.0, 330.0, 220.0)
                        },
                    )
            }
        },
        bar.chart("bar-background", "带背景色的柱状图", "Bar with Background") {
            unsafeJso<EChartsOption> {
                xAxis = categoryAxis(*week)
                yAxis = valueAxis
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            showBackground = true
                            backgroundStyle = unsafeJso<ItemStyleOption> { color = ZRColor("rgba(180, 180, 180, 0.2)") }
                            typedData = numbers(120.0, 200.0, 150.0, 80.0, 70.0, 110.0, 130.0)
                        },
                    )
            }
        },
        bar.chart("bar-data-color", "自定义单个柱子颜色", "Set Style of Single Bar.") {
            unsafeJso<EChartsOption> {
                xAxis = categoryAxis(*week)
                yAxis = valueAxis
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            typedData =
                                dataItems(
                                    BarDataItem(OptionDataValue(120.0)),
                                    colouredBar(200.0, "#505372"),
                                    BarDataItem(OptionDataValue(150.0)),
                                    BarDataItem(OptionDataValue(80.0)),
                                    BarDataItem(OptionDataValue(70.0)),
                                    BarDataItem(OptionDataValue(110.0)),
                                    BarDataItem(OptionDataValue(130.0)),
                                )
                        },
                    )
            }
        },
        bar.chart("bar-y-category", "世界人口总量 - 条形图", "World Population") {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "World Population" })
                tooltip = axisTooltip
                legend = one(unsafeJso<LegendOption> {})
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.value
                            boundaryGap = BooleanOrStringArray(arrayOf(StringOrDouble(0.0), StringOrDouble(0.01)))
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.category
                            data = strings("Brazil", "Indonesia", "USA", "India", "China", "World")
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            name = "2011"
                            typedData = numbers(18203.0, 23489.0, 29034.0, 104970.0, 131744.0, 630230.0)
                        },
                        barSeries {
                            name = "2012"
                            typedData = numbers(19325.0, 23438.0, 31000.0, 121594.0, 134141.0, 681807.0)
                        },
                    )
            }
        },
        bar.chart("bar-negative", "正负条形图", "Bar Chart with Negative Value") {
            unsafeJso<EChartsOption> {
                tooltip = axisTooltip
                legend = one(unsafeJso<LegendOption> { data = legendNames("Profit", "Expenses", "Income") })
                xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.value })
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.category
                            axisTick = unsafeJso { show = echarts.options.BooleanOrString(false) }
                            data = strings(*week)
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            name = "Profit"
                            label = unsafeJso<LabelOption> { show = true; position = LabelPosition("inside") }
                            emphasis = seriesFocus
                            typedData = numbers(200.0, 170.0, 240.0, 244.0, 200.0, 220.0, 210.0)
                        },
                        barSeries {
                            name = "Income"
                            stack = "Total"
                            label = unsafeJso<LabelOption> { show = true }
                            emphasis = seriesFocus
                            typedData = numbers(320.0, 302.0, 341.0, 374.0, 390.0, 450.0, 420.0)
                        },
                        barSeries {
                            name = "Expenses"
                            stack = "Total"
                            label = unsafeJso<LabelOption> { show = true; position = LabelPosition("left") }
                            emphasis = seriesFocus
                            typedData = numbers(-120.0, -132.0, -101.0, -134.0, -190.0, -230.0, -210.0)
                        },
                    )
            }
        },
        bar.chart("bar-negative2", "交错正负轴标签", "Bar Chart with Negative Value") {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Bar Chart with Negative Value" })
                tooltip = axisTooltip
                grid = one(unsafeJso<GridOption> { top = StringOrDouble(80.0); bottom = StringOrDouble(30.0) })
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.value
                            position = echarts.AxisPositions.top
                            splitLine = unsafeJso<SplitLineOption> { lineStyle = unsafeJso<SplitLineStyleOption> { type = ZRLineTypes.dashed } }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.category
                            axisLine = unsafeJso { show = echarts.options.BooleanOrString(false) }
                            axisLabel = unsafeJso<AxisLabelOption> { show = false }
                            axisTick = unsafeJso { show = echarts.options.BooleanOrString(false) }
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                            data = strings("ten", "nine", "eight", "seven", "six", "five", "four", "three", "two", "one")
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            name = "Cost"
                            stack = "Total"
                            label = unsafeJso<LabelOption> { show = true; formatter = echarts.options.LabelFormatter("{b}") }
                            typedData =
                                dataItems(
                                    BarDataItem(unsafeJso<BarDataItemOption> { value = OptionDataValue(-0.07); label = rightLabel() }),
                                    BarDataItem(unsafeJso<BarDataItemOption> { value = OptionDataValue(-0.09); label = rightLabel() }),
                                    BarDataItem(OptionDataValue(0.2)),
                                    BarDataItem(OptionDataValue(0.44)),
                                    BarDataItem(unsafeJso<BarDataItemOption> { value = OptionDataValue(-0.23); label = rightLabel() }),
                                    BarDataItem(OptionDataValue(0.08)),
                                    BarDataItem(unsafeJso<BarDataItemOption> { value = OptionDataValue(-0.17); label = rightLabel() }),
                                    BarDataItem(OptionDataValue(0.47)),
                                    BarDataItem(unsafeJso<BarDataItemOption> { value = OptionDataValue(-0.36); label = rightLabel() }),
                                    BarDataItem(OptionDataValue(0.18)),
                                )
                        },
                    )
            }
        },
        bar.chart("bar-stack", "堆叠柱状图", "Stacked Column Chart") {
            unsafeJso<EChartsOption> {
                tooltip = axisTooltip
                legend = one(unsafeJso<LegendOption> {})
                xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.category; data = strings(*week) })
                yAxis = valueAxis
                series =
                    arrayOf<SeriesOption>(
                        barSeries { name = "Direct"; emphasis = seriesFocus; typedData = numbers(320.0, 332.0, 301.0, 334.0, 390.0, 330.0, 320.0) },
                        barSeries { name = "Email"; stack = "Ad"; emphasis = seriesFocus; typedData = numbers(120.0, 132.0, 101.0, 134.0, 90.0, 230.0, 210.0) },
                        barSeries { name = "Union Ads"; stack = "Ad"; emphasis = seriesFocus; typedData = numbers(220.0, 182.0, 191.0, 234.0, 290.0, 330.0, 310.0) },
                        barSeries { name = "Video Ads"; stack = "Ad"; emphasis = seriesFocus; typedData = numbers(150.0, 232.0, 201.0, 154.0, 190.0, 330.0, 410.0) },
                        barSeries {
                            name = "Search Engine"
                            emphasis = seriesFocus
                            typedData = numbers(862.0, 1018.0, 964.0, 1026.0, 1679.0, 1600.0, 1570.0)
                            markLine =
                                unsafeJso<MarkLineOption> {
                                    lineStyle = unsafeJso<LineStyleOption> { type = ZRLineTypes.dashed }
                                    data =
                                        arrayOf(
                                            MarkLineDataItem(
                                                arrayOf(
                                                    unsafeJso<MarkLine2DDataItemDimOption> { type = MarkerStatisticTypes.min },
                                                    unsafeJso<MarkLine2DDataItemDimOption> { type = MarkerStatisticTypes.max },
                                                ),
                                            ),
                                        )
                                }
                        },
                        barSeries { name = "Baidu"; barWidth = StringOrDouble(5.0); stack = "Search Engine"; emphasis = seriesFocus; typedData = numbers(620.0, 732.0, 701.0, 734.0, 1090.0, 1130.0, 1120.0) },
                        barSeries { name = "Google"; stack = "Search Engine"; emphasis = seriesFocus; typedData = numbers(120.0, 132.0, 101.0, 134.0, 290.0, 230.0, 220.0) },
                        barSeries { name = "Bing"; stack = "Search Engine"; emphasis = seriesFocus; typedData = numbers(60.0, 72.0, 71.0, 74.0, 190.0, 130.0, 110.0) },
                        barSeries { name = "Others"; stack = "Search Engine"; emphasis = seriesFocus; typedData = numbers(62.0, 82.0, 91.0, 84.0, 109.0, 110.0, 120.0) },
                    )
            }
        },
        bar.chart("bar-y-category-stack", "堆叠条形图", "Stacked Horizontal Bar") {
            unsafeJso<EChartsOption> {
                tooltip = axisTooltip
                legend = one(unsafeJso<LegendOption> {})
                xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.value })
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.category; data = strings(*week) })
                series =
                    arrayOf<SeriesOption>(
                        stackedBar("Direct", 320.0, 302.0, 301.0, 334.0, 390.0, 330.0, 320.0),
                        stackedBar("Mail Ad", 120.0, 132.0, 101.0, 134.0, 90.0, 230.0, 210.0),
                        stackedBar("Affiliate Ad", 220.0, 182.0, 191.0, 234.0, 290.0, 330.0, 310.0),
                        stackedBar("Video Ad", 150.0, 212.0, 201.0, 154.0, 190.0, 330.0, 410.0),
                        stackedBar("Search Engine", 820.0, 832.0, 901.0, 934.0, 1290.0, 1330.0, 1320.0),
                    )
            }
        },
        bar.chart("bar-waterfall", "瀑布图（柱状图模拟）", "Waterfall Chart") {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Waterfall Chart"
                            subtext = "Living Expenses in Shenzhen"
                        },
                    )
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer = shadowPointer
                            // The original formatter reads the second series of the axis trigger.
                            formatter =
                                TooltipFormatter(
                                    js("(params) => params[1].name + '<br/>' + params[1].seriesName + ' : ' + params[1].value") as TooltipFormatterCallback,
                                )
                        },
                    )
                grid = one(unsafeJso<GridOption> { left = StringOrDouble("3%"); right = StringOrDouble("4%"); bottom = StringOrDouble("3%"); containLabel = true })
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                            data = strings("Total", "Rent", "Utilities", "Transportation", "Meals", "Other")
                        },
                    )
                yAxis = valueAxis
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            name = "Placeholder"
                            stack = "Total"
                            itemStyle = unsafeJso<ItemStyleOption> { borderColor = ZRColor("transparent"); color = ZRColor("transparent") }
                            emphasis =
                                unsafeJso<EmphasisOption> {
                                    itemStyle = unsafeJso<ItemStyleOption> { borderColor = ZRColor("transparent"); color = ZRColor("transparent") }
                                }
                            typedData = numbers(0.0, 1700.0, 1400.0, 1200.0, 300.0, 0.0)
                        },
                        barSeries {
                            name = "Life Cost"
                            stack = "Total"
                            label = unsafeJso<LabelOption> { show = true; position = LabelPosition("inside") }
                            typedData = numbers(2900.0, 1200.0, 300.0, 200.0, 900.0, 300.0)
                        },
                    )
            }
        },
        bar.chart("mix-line-bar", "折柱混合", "Mixed Line and Bar") {
            unsafeJso<EChartsOption> {
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer =
                                unsafeJso<AxisPointerOption> {
                                    type = AxisPointerTypes.cross
                                    crossStyle = unsafeJso<LineStyleOption> { color = ZRColor("#999") }
                                }
                        },
                    )
                toolbox = toolbox(dataView = true, magicType = true, restore = true, saveAsImage = true)
                legend = one(unsafeJso<LegendOption> { data = legendNames("Evaporation", "Precipitation", "Temperature") })
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            data = strings(*week)
                            axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.shadow }
                        },
                    )
                yAxis =
                    arrayOf(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            name = "Precipitation"
                            min = echarts.options.AxisMinMax(0.0)
                            max = echarts.options.AxisMinMax(250.0)
                            interval = echarts.options.AxisInterval(50.0)
                            axisLabel = unsafeJso { formatter = echarts.options.LabelFormatter("{value} ml") }
                        },
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            name = "Temperature"
                            min = echarts.options.AxisMinMax(0.0)
                            max = echarts.options.AxisMinMax(25.0)
                            interval = echarts.options.AxisInterval(5.0)
                            axisLabel = unsafeJso { formatter = echarts.options.LabelFormatter("{value} °C") }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        barSeries { name = "Evaporation"; typedData = numbers(2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3) },
                        barSeries { name = "Precipitation"; typedData = numbers(2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3) },
                        lineSeries {
                            name = "Temperature"
                            yAxisIndex = 1.0
                            typedData = numbers(2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2)
                        },
                    )
            }
        },
        bar.chart("multiple-y-axis", "多 Y 轴示例", "Multiple Y Axes") {
            val colours = arrayOf("#5070dd", "#b6d634", "#505372")
            unsafeJso<EChartsOption> {
                color = Array(colours.size) { ZRColor(colours[it]) }
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.cross }
                        },
                    )
                grid = one(unsafeJso<GridOption> { right = StringOrDouble("20%") })
                toolbox = toolbox(dataView = true, restore = true, saveAsImage = true)
                legend = one(unsafeJso<LegendOption> { data = legendNames("Evaporation", "Precipitation", "Temperature") })
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            axisTick = unsafeJso { alignWithLabel = true }
                            data =
                                strings(
                                    "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
                                )
                        },
                    )
                yAxis =
                    arrayOf(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            name = "Evaporation"
                            position = echarts.AxisPositions.right
                            alignTicks = true
                            axisLine = unsafeJso { show = echarts.options.BooleanOrString(true); lineStyle = unsafeJso<LineStyleOption> { color = ZRColor(colours[0]) } }
                            axisLabel = unsafeJso { formatter = echarts.options.LabelFormatter("{value} ml") }
                        },
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            name = "Precipitation"
                            position = echarts.AxisPositions.right
                            alignTicks = true
                            offset = 80.0
                            axisLine = unsafeJso { show = echarts.options.BooleanOrString(true); lineStyle = unsafeJso<LineStyleOption> { color = ZRColor(colours[1]) } }
                            axisLabel = unsafeJso { formatter = echarts.options.LabelFormatter("{value} ml") }
                        },
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            name = "温度"
                            position = echarts.AxisPositions.left
                            alignTicks = true
                            axisLine = unsafeJso { show = echarts.options.BooleanOrString(true); lineStyle = unsafeJso<LineStyleOption> { color = ZRColor(colours[2]) } }
                            axisLabel = unsafeJso { formatter = echarts.options.LabelFormatter("{value} °C") }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        barSeries { name = "Evaporation"; typedData = numbers(2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3) },
                        barSeries { name = "Precipitation"; yAxisIndex = 1.0; typedData = numbers(2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3) },
                        lineSeries { name = "Temperature"; yAxisIndex = 2.0; typedData = numbers(2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2) },
                    )
            }
        },
        bar.chart("bar-animation-delay", "柱状图动画延迟", "Animation Delay") {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Bar Animation Delay" })
                legend = one(unsafeJso<LegendOption> { data = legendNames("bar", "bar2") })
                toolbox = toolbox(dataView = true, magicType = true, saveAsImage = true)
                tooltip = one(unsafeJso<TooltipOption> {})
                xAxis = one(unsafeJso<XAxisOption> { data = strings(*Array(100) { "A$it" }); splitLine = unsafeJso<SplitLineOption> { show = false } })
                yAxis = one(unsafeJso<YAxisOption> {})
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            name = "bar"
                            typedData = numbers(*delayedData(cosine = false))
                            emphasis = seriesFocus
                            animationDelay = AnimationDelay { index, _ -> index * 10.0 }
                        },
                        barSeries {
                            name = "bar2"
                            typedData = numbers(*delayedData(cosine = true))
                            emphasis = seriesFocus
                            animationDelay = AnimationDelay { index, _ -> index * 10.0 + 100.0 }
                        },
                    )
                animationEasing = AnimationEasings.elasticOut
                animationDelayUpdate = AnimationDelay { index, _ -> index * 5.0 }
            }
        },
    )

/** One series of `bar-y-category-stack`. */
private fun stackedBar(name: String, vararg values: Double): SeriesOption =
    barSeries {
        this.name = name
        stack = "total"
        label = unsafeJso<LabelOption> { show = true }
        emphasis = seriesFocus
        typedData = numbers(*values)
    }

/** The `{ position: 'right' }` label shared by the negative half of `bar-negative2`. */
private fun rightLabel(): LabelOption = unsafeJso { position = LabelPosition("right") }
