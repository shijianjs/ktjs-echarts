package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.AxisPointerTypes
import echarts.AxisTypes
import echarts.TooltipTriggers
import echarts.options.AreaStyleOption
import echarts.options.AxisPointerOption
import echarts.options.AxisLabelOption
import echarts.options.AxisPointerLabelOption
import echarts.options.BooleanOrDouble
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
import echarts.options.SeriesOption
import echarts.options.SplitLineOption
import echarts.options.StringOrDouble
import echarts.options.SymbolShape
import echarts.options.SymbolSize
import echarts.options.TitleOption
import echarts.options.TooltipFormatter
import echarts.options.TooltipOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.lineSeries
import echarts.options.numbers
import echarts.options.one
import echarts.options.points
import echarts.options.strings
import js.objects.unsafeJso
import zrender.ZRLineTypes

/*
 * Line and area examples ported from `public/examples/ts/line-*.ts`, `area-*.ts` and
 * `line-in-cartesian-coordinate-system.ts` of apache/echarts-examples.
 *
 * The large-data, easing, fisheye and draggable originals are not ported in this pass; see
 * `demo/AGENTS.md` for the list.
 */

private val line = Category(Groups.LINE, "examples/Line.kt")

private val week = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

private val axisTooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.axis })

private val seriesFocus = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }

private fun categoryAxis(vararg names: String, boundaryGap: Boolean? = null): Array<XAxisOption> =
    one(
        unsafeJso<XAxisOption> {
            type = AxisTypes.category
            if (boundaryGap != null) this.boundaryGap = BooleanOrStringArray(boundaryGap)
            data = strings(*names)
        },
    )

private val valueAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })

/** One series of the stacked line and area examples. */
private fun stackedSeries(
    name: String,
    values: DoubleArray,
    area: Boolean,
    label: Boolean = false,
): SeriesOption =
    lineSeries {
        this.name = name
        stack = "Total"
        if (area) areaStyle = unsafeJso<AreaStyleOption> {}
        emphasis = seriesFocus
        if (label) this.label = unsafeJso<LabelOption> { show = true; position = LabelPosition("top") }
        typedData = numbers(*values)
    }

val lineEntries: List<CatalogEntry> =
    listOf(
        line.chart("line-simple", "基础折线图", "Basic Line Chart") {
            unsafeJso<EChartsOption> {
                xAxis = categoryAxis(*week)
                yAxis = valueAxis
                series = arrayOf<SeriesOption>(lineSeries { typedData = numbers(150.0, 230.0, 224.0, 218.0, 135.0, 147.0, 260.0) })
            }
        },
        line.chart("line-smooth", "基础平滑折线图", "Smoothed Line Chart") {
            unsafeJso<EChartsOption> {
                xAxis = categoryAxis(*week)
                yAxis = valueAxis
                series =
                    arrayOf<SeriesOption>(
                        lineSeries {
                            smooth = BooleanOrDouble(true)
                            typedData = numbers(820.0, 932.0, 901.0, 934.0, 1290.0, 1330.0, 1320.0)
                        },
                    )
            }
        },
        line.chart("area-basic", "基础面积图", "Basic area chart") {
            unsafeJso<EChartsOption> {
                xAxis = categoryAxis(*week, boundaryGap = false)
                yAxis = valueAxis
                series =
                    arrayOf<SeriesOption>(
                        lineSeries {
                            areaStyle = unsafeJso<AreaStyleOption> {}
                            typedData = numbers(820.0, 932.0, 901.0, 934.0, 1290.0, 1330.0, 1320.0)
                        },
                    )
            }
        },
        line.chart("line-stack", "堆叠折线图", "Stacked Line Chart") {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Stacked Line" })
                tooltip = axisTooltip
                legend = one(unsafeJso<LegendOption> { data = legendNames("Email", "Union Ads", "Video Ads", "Direct", "Search Engine") })
                grid = one(unsafeJso<GridOption> { left = StringOrDouble("3%"); right = StringOrDouble("4%"); bottom = StringOrDouble("3%"); containLabel = true })
                toolbox = toolbox(saveAsImage = true)
                xAxis = categoryAxis(*week, boundaryGap = false)
                yAxis = valueAxis
                series =
                    arrayOf<SeriesOption>(
                        stackedSeries("Email", doubleArrayOf(120.0, 132.0, 101.0, 134.0, 90.0, 230.0, 210.0), area = false),
                        stackedSeries("Union Ads", doubleArrayOf(220.0, 182.0, 191.0, 234.0, 290.0, 330.0, 310.0), area = false),
                        stackedSeries("Video Ads", doubleArrayOf(150.0, 232.0, 201.0, 154.0, 190.0, 330.0, 410.0), area = false),
                        stackedSeries("Direct", doubleArrayOf(320.0, 332.0, 301.0, 334.0, 390.0, 330.0, 320.0), area = false),
                        stackedSeries("Search Engine", doubleArrayOf(820.0, 932.0, 901.0, 934.0, 1290.0, 1330.0, 1320.0), area = false),
                    )
            }
        },
        line.chart("area-stack", "堆叠面积图", "Stacked Area Chart") {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Stacked Area Chart" })
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer =
                                unsafeJso<AxisPointerOption> {
                                    type = AxisPointerTypes.cross
                                    label = unsafeJso<AxisPointerLabelOption> { backgroundColor = zrender.ZRColor("#6a7985") }
                                }
                        },
                    )
                legend = one(unsafeJso<LegendOption> { data = legendNames("Email", "Union Ads", "Video Ads", "Direct", "Search Engine") })
                toolbox = toolbox(saveAsImage = true)
                xAxis = categoryAxis(*week, boundaryGap = false)
                yAxis = valueAxis
                series =
                    arrayOf<SeriesOption>(
                        stackedSeries("Email", doubleArrayOf(120.0, 132.0, 101.0, 134.0, 90.0, 230.0, 210.0), area = true),
                        stackedSeries("Union Ads", doubleArrayOf(220.0, 182.0, 191.0, 234.0, 290.0, 330.0, 310.0), area = true),
                        stackedSeries("Video Ads", doubleArrayOf(150.0, 232.0, 201.0, 154.0, 190.0, 330.0, 410.0), area = true),
                        stackedSeries("Direct", doubleArrayOf(320.0, 332.0, 301.0, 334.0, 390.0, 330.0, 320.0), area = true),
                        stackedSeries("Search Engine", doubleArrayOf(820.0, 932.0, 901.0, 934.0, 1290.0, 1330.0, 1320.0), area = true, label = true),
                    )
            }
        },
        line.chart("line-step", "阶梯折线图", "Step Line") {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Step Line" })
                tooltip = axisTooltip
                legend = one(unsafeJso<LegendOption> { data = legendNames("Step Start", "Step Middle", "Step End") })
                grid = one(unsafeJso<GridOption> { left = StringOrDouble("3%"); right = StringOrDouble("4%"); bottom = StringOrDouble("3%"); containLabel = true })
                toolbox = toolbox(saveAsImage = true)
                xAxis = categoryAxis(*week)
                yAxis = valueAxis
                series =
                    arrayOf<SeriesOption>(
                        lineSeries { name = "Step Start"; step = echarts.options.BooleanOrString("start"); typedData = numbers(120.0, 132.0, 101.0, 134.0, 90.0, 230.0, 210.0) },
                        lineSeries { name = "Step Middle"; step = echarts.options.BooleanOrString("middle"); typedData = numbers(220.0, 282.0, 201.0, 234.0, 290.0, 430.0, 410.0) },
                        lineSeries { name = "Step End"; step = echarts.options.BooleanOrString("end"); typedData = numbers(450.0, 432.0, 401.0, 454.0, 590.0, 530.0, 510.0) },
                    )
            }
        },
        line.chart("line-style", "自定义折线图样式", "Line Style and Item Style") {
            unsafeJso<EChartsOption> {
                xAxis = categoryAxis(*week)
                yAxis = valueAxis
                series =
                    arrayOf<SeriesOption>(
                        lineSeries {
                            symbol = SymbolShape("triangle")
                            symbolSize = SymbolSize(20.0)
                            lineStyle = unsafeJso<LineStyleOption> { color = zrender.ZRColor("#5470C6"); width = 4.0; type = ZRLineTypes.dashed }
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    borderWidth = 3.0
                                    borderColor = zrender.ZRColor("#EE6666")
                                    color = zrender.ZRColor("yellow")
                                }
                            typedData = numbers(120.0, 200.0, 150.0, 80.0, 70.0, 110.0, 130.0)
                        },
                    )
            }
        },
        line.chart("line-log", "对数轴示例", "Log Axis") {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Log Axis"; left = StringOrDouble("center") })
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.item
                            formatter = TooltipFormatter("{a} <br/>{b} : {c}")
                        },
                    )
                legend = one(unsafeJso<LegendOption> { left = StringOrDouble("left") })
                grid = one(unsafeJso<GridOption> { left = StringOrDouble("3%"); right = StringOrDouble("4%"); bottom = StringOrDouble("3%"); containLabel = true })
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            name = "x"
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                            data = strings("A", "B", "C", "D", "E", "F", "G", "H", "I")
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.log
                            name = "y"
                            minorSplitLine = unsafeJso<SplitLineOption> { show = true }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        lineSeries { name = "Log2"; typedData = numbers(1.0, 3.0, 9.0, 27.0, 81.0, 247.0, 741.0, 2223.0, 6669.0) },
                        lineSeries { name = "Log3"; typedData = numbers(1.0, 2.0, 4.0, 8.0, 16.0, 32.0, 64.0, 128.0, 256.0) },
                        lineSeries {
                            name = "Log1/2"
                            typedData = numbers(0.5, 0.25, 0.125, 0.0625, 0.03125, 0.015625, 0.0078125, 0.00390625, 0.001953125)
                        },
                    )
            }
        },
        line.chart("line-y-category", "垂直折线图（Y轴为类目轴）", "Line Y Category") {
            unsafeJso<EChartsOption> {
                legend = one(unsafeJso<LegendOption> { data = legendNames("Altitude (km) vs. temperature (°C)") })
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            formatter = TooltipFormatter("Temperature : <br/>{b}km : {c}°C")
                        },
                    )
                grid = one(unsafeJso<GridOption> { left = StringOrDouble("3%"); right = StringOrDouble("4%"); bottom = StringOrDouble("3%"); containLabel = true })
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.value
                            axisLabel = unsafeJso<AxisLabelOption> { formatter = echarts.options.LabelFormatter("{value} °C") }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.category
                            axisLine = unsafeJso { onZero = echarts.options.BooleanOrString(false) }
                            axisLabel = unsafeJso<AxisLabelOption> { formatter = echarts.options.LabelFormatter("{value} km") }
                            boundaryGap = BooleanOrStringArray(false)
                            data = strings("0", "10", "20", "30", "40", "50", "60", "70", "80")
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        lineSeries {
                            name = "Altitude (km) vs. temperature (°C)"
                            symbolSize = SymbolSize(10.0)
                            symbol = SymbolShape("circle")
                            smooth = BooleanOrDouble(true)
                            lineStyle =
                                unsafeJso<LineStyleOption> {
                                    width = 3.0
                                    shadowColor = zrender.ZRColor("rgba(0,0,0,0.3)")
                                    shadowBlur = 10.0
                                    shadowOffsetY = 8.0
                                }
                            typedData = numbers(15.0, -50.0, -56.5, -46.5, -22.1, -2.5, -27.7, -55.7, -76.5)
                        },
                    )
            }
        },
        line.chart("line-in-cartesian-coordinate-system", "双数值轴折线图", "Line Chart in Cartesian Coordinate System") {
            unsafeJso<EChartsOption> {
                xAxis = one(unsafeJso<XAxisOption> {})
                yAxis = one(unsafeJso<YAxisOption> {})
                series =
                    arrayOf<SeriesOption>(
                        lineSeries { typedData = points(doubleArrayOf(10.0, 40.0), doubleArrayOf(50.0, 100.0), doubleArrayOf(40.0, 20.0)) },
                    )
            }
        },
    )
