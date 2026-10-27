package demo.examples

import demo.CatalogEntry
import echarts.*
import echarts.options.*
import demo.Groups
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import web.cssom.TextAlign
import zrender.ZRColor
import zrender.ZRLineTypes

/*
 * The small examples of the official documentation, ported from
 * the `doc-example/` folder of `public/examples/ts` (the `doc-example/` ids of `chart-list-data.json`).
 *
 * The manifest keeps these out of the chart families: they illustrate one option field each, so the
 * pages are intentionally short. Their ids keep the `doc-example/` prefix of the official site so
 * the "official example" link of a page points at the same page.
 *
 * Not ported yet:
 *
 * - `pie-highlight` and `barGrid-barGap` drive the chart from a `setInterval`; the demo builds a
 *   page once and has no timer lifetime, so they need the interactive plumbing first;
 * - `canvas-vs-svg`, `tutorial-async`, `tutorial-loading` and the `value-animation` family create a
 *   second renderer or set the option after an asynchronous step;
 * - the `aria-decal*`, `map-*`, `geo-*`, `graphic-*` and `pictorialBar-*` entries of the list are the
 *   longer documentation pages, kept for a later batch.
 *
 * The stored tutorials (`tutorial-styling-step*`) still use the ECharts 4 shape with a `normal`
 * state inside `itemStyle` / `label` / `labelLine`; ECharts 6 dropped that state, so the ports set
 * the same fields directly and note it per example.
 */

private val docExample = Category(Groups.DOC, "examples/DocExamples.kt")

/** The five slices the pie tutorials share. */
private fun visitSource(): Array<PieDataItem?> =
    dataItems(
        pieItem("视频广告", 235.0),
        pieItem("联盟广告", 274.0),
        pieItem("邮件营销", 310.0),
        pieItem("直接访问", 335.0),
        pieItem("搜索引擎", 400.0),
    )

/** A sunburst node with an optional colour and an optional list of children. */
private fun docSunburstNode(
    name: String? = null,
    value: Double? = null,
    color: String? = null,
    label: SunburstLabelOption? = null,
    children: Array<SunburstSeriesNodeItemOption>? = null,
): SunburstSeriesNodeItemOption {
    val nodeName = name
    val nodeValue = value
    val fill = color
    val nodeLabel = label
    val nodeChildren = children
    return unsafeJso {
        if (nodeName != null) this.name = OptionId(nodeName)
        if (nodeValue != null) this.value = OptionDataValue(nodeValue)
        if (fill != null) itemStyle = unsafeJso { this.color = ZRColor(fill) }
        if (nodeLabel != null) this.label = nodeLabel
        if (nodeChildren != null) this.children = nodeChildren
    }
}

/** `label: { align: <align>, position: 'inside' }` of `sunburst-label-align`. */
private fun insideLabel(align: TextAlign): SunburstLabelOption = unsafeJso {
    this.align = align
    position = LabelPosition("inside")
}

/** The hidden `-1..1` x axis the text examples sit on. */
private fun hiddenXAxis(): Array<XAxisOption> =
    one(
        unsafeJso<XAxisOption> {
            axisLabel = unsafeJso<AxisLabelOption> { show = false }
            axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
            splitLine = unsafeJso<SplitLineOption> { show = false }
            axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
            min = AxisMinMax(-1.0)
            max = AxisMinMax(1.0)
        },
    )

/** The hidden `-1..1` y axis the text examples sit on. */
private fun hiddenYAxis(): Array<YAxisOption> =
    one(
        unsafeJso<YAxisOption> {
            axisLabel = unsafeJso<AxisLabelOption> { show = false }
            axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
            splitLine = unsafeJso<SplitLineOption> { show = false }
            axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
            min = AxisMinMax(-1.0)
            max = AxisMinMax(1.0)
        },
    )

val docExampleEntries: List<CatalogEntry> =
    listOf(
        docExample.chart("doc-example/polar-anticlockwise", "逆时针排列的极坐标", "Polar anti-clockwise") {
            unsafeJso<EChartsOption> {
                polar = one(unsafeJso<PolarOption> {})
                angleAxis =
                    one(
                        unsafeJso<AngleAxisOption> {
                            min = AxisMinMax(0.0)
                            max = AxisMinMax(360.0)
                            interval = AxisInterval(30.0)
                            clockwise = false
                        },
                    )
                radiusAxis =
                    one(
                        unsafeJso<RadiusAxisOption> {
                            min = AxisMinMax(0.0)
                            max = AxisMinMax(10.0)
                            interval = AxisInterval(2.0)
                        },
                    )
                series = emptyArray()
            }
        },
        docExample.chart("doc-example/polar-start-angle", "极坐标起始角度", "Polar start angle") {
            unsafeJso<EChartsOption> {
                polar = one(unsafeJso<PolarOption> {})
                angleAxis =
                    one(
                        unsafeJso<AngleAxisOption> {
                            min = AxisMinMax(0.0)
                            max = AxisMinMax(360.0)
                            interval = AxisInterval(30.0)
                            startAngle = 45.0
                        },
                    )
                radiusAxis =
                    one(
                        unsafeJso<RadiusAxisOption> {
                            min = AxisMinMax(0.0)
                            max = AxisMinMax(10.0)
                            interval = AxisInterval(2.0)
                        },
                    )
                series = emptyArray()
            }
        },
        docExample.chart("doc-example/polar-end-angle", "极坐标结束角度", "Polar end angle") {
            unsafeJso<EChartsOption> {
                tooltip = one(unsafeJso<TooltipOption> {})
                angleAxis =
                    one(
                        unsafeJso<AngleAxisOption> {
                            type = AxisTypes.category
                            endAngle = -180.0
                            data = strings("S1", "S2", "S3")
                        },
                    )
                radiusAxis = one(unsafeJso<RadiusAxisOption> {})
                polar = one(unsafeJso<PolarOption> {})
                series =
                    one(
                        barSeries {
                            coordinateSystem = "polar"
                            polarIndex = 0.0
                            typedData = numbers(1.0, 2.0, 3.0)
                        },
                    )
            }
        },
        docExample.chart("doc-example/getting-started", "快速上手示例", "Getting started") {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "ECharts Getting Started Example" })
                tooltip = one(unsafeJso<TooltipOption> {})
                legend = one(unsafeJso<LegendOption> { data = legendNames("sales") })
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            data = strings("Shirts", "Cardigans", "Chiffons", "Pants", "Heels", "Socks")
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> {})
                series =
                    one(
                        barSeries {
                            name = "sales"
                            typedData = numbers(5.0, 20.0, 36.0, 10.0, 10.0, 20.0)
                        },
                    )
            }
        },
        docExample.chart("doc-example/aria-pie", "无障碍饼图", "Aria pie") {
            unsafeJso<EChartsOption> {
                aria = unsafeJso<AriaOption> { enabled = true }
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "某站点用户访问来源"
                            left = StringOrDouble("center")
                        },
                    )
                series =
                    one(
                        pieSeries {
                            name = "访问来源"
                            typedData =
                                dataItems(
                                    pieItem("直接访问", 335.0),
                                    pieItem("邮件营销", 310.0),
                                    pieItem("联盟广告", 234.0),
                                    pieItem("视频广告", 135.0),
                                    pieItem("搜索引擎", 1548.0),
                                )
                        },
                    )
            }
        },
        docExample.chart("doc-example/sunburst-color", "旭日图配色", "Sunburst colour") {
            val a =
                docSunburstNode(
                    name = "A",
                    value = 10.0,
                    children =
                        arrayOf(
                            docSunburstNode(name = "Aa", value = 3.0, color = "red"),
                            docSunburstNode(name = "Ab", value = 5.0),
                        ),
                )
            val b =
                docSunburstNode(
                    name = "B",
                    color = "red",
                    children = arrayOf(docSunburstNode(name = "Ba", value = 4.0), docSunburstNode(name = "Bb", value = 2.0)),
                )
            unsafeJso<EChartsOption> {
                series =
                    one(
                        sunburstSeries {
                            typedData = dataItems(a, b, docSunburstNode(name = "C", value = 3.0))
                            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#aaa") }
                            levels =
                                arrayOf(
                                    unsafeJso<SunburstSeriesLevelOption> {},
                                    unsafeJso<SunburstSeriesLevelOption> {
                                        itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("blue") }
                                    },
                                )
                        },
                    )
            }
        },
        docExample.chart("doc-example/sunburst-label-align", "旭日图标签对齐", "Sunburst label align") {
            val data =
                Array(8) {
                    docSunburstNode(
                        name = "left",
                        label = insideLabel(TextAlign.left),
                        children = arrayOf(docSunburstNode(name = "right", value = 1.0, label = insideLabel(TextAlign.right))),
                    )
                }
            unsafeJso<EChartsOption> {
                series =
                    one(
                        sunburstSeries {
                            radius = StringOrDoubleOrArray(arrayOf(StringOrDouble("20%"), StringOrDouble("90%")))
                            // `sort: null` keeps the order of the data, which is what the original
                            // asks for; ECharts types it as a nullable sort order.
                            sort = null
                            emphasis = unsafeJso { focus = EmphasisFocuses.none }
                            typedData = dataItems(*data)
                            label = unsafeJso<SunburstLabelOption> { fontSize = StringOrDouble(14.0) }
                        },
                    )
            }
        },
        docExample.chart("doc-example/line-stack-tiled", "折线图堆叠与平铺", "Stacked and tiled lines") {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "某楼盘销售情况"
                            subtext = "纯属虚构"
                        },
                    )
                tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.axis })
                legend = one(unsafeJso<LegendOption> { data = legendNames("意向", "预购", "成交") })
                toolbox = toolbox(magicType = true, saveAsImage = true)
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            boundaryGap = BooleanOrStringArray(false)
                            data = strings("周一", "周二", "周三", "周四", "周五", "周六", "周日")
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                series =
                    arrayOf<SeriesOption>(
                        lineSeries {
                            name = "成交"
                            smooth = BooleanOrDouble(true)
                            typedData = numbers(10.0, 12.0, 21.0, 54.0, 260.0, 830.0, 710.0)
                        },
                        lineSeries {
                            name = "预购"
                            smooth = BooleanOrDouble(true)
                            typedData = numbers(30.0, 182.0, 434.0, 791.0, 390.0, 30.0, 10.0)
                        },
                        lineSeries {
                            name = "意向"
                            smooth = BooleanOrDouble(true)
                            typedData = numbers(1320.0, 1132.0, 601.0, 234.0, 120.0, 90.0, 20.0)
                        },
                    )
            }
        },
        docExample.chart("doc-example/axis-label-align-min-max", "轴首尾标签对齐", "Axis min/max label alignment") {
            // The original fills the series with `Math.random()`; a fixed sequence keeps the page
            // reproducible, the point of the example is the alignment of the two end labels.
            val values = DoubleArray(200) { index -> (index * 37 % 100).toDouble() }
            unsafeJso<EChartsOption> {
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            axisLabel =
                                unsafeJso<AxisLabelOption> {
                                    formatter = LabelFormatter("label of {value}")
                                    showMinLabel = true
                                    showMaxLabel = true
                                    alignMinLabel = TextAlign.left
                                    alignMaxLabel = TextAlign.right
                                }
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> {})
                series =
                    one(
                        lineSeries {
                            typedData = numbers(*values)
                        },
                    )
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.slider
                            start = 20.0
                            end = 42.0
                        },
                    )
            }
        },
        docExample.chart("doc-example/tutorial-styling-step0", "教程：朴素饼图", "Tutorial: plain pie") {
            unsafeJso<EChartsOption> {
                series = one(pieSeries { name = "访问来源"; radius = StringOrDoubleOrArray("55%"); typedData = visitSource() })
            }
        },
        docExample.chart("doc-example/tutorial-styling-step1", "教程：南丁格尔玫瑰图", "Tutorial: rose pie") {
            unsafeJso<EChartsOption> {
                series =
                    one(
                        pieSeries {
                            name = "访问来源"
                            radius = StringOrDoubleOrArray("55%")
                            roseType = RoseTypes.area
                            typedData = visitSource()
                        },
                    )
            }
        },
        docExample.chart("doc-example/tutorial-styling-step2", "教程：阴影", "Tutorial: shadow") {
            unsafeJso<EChartsOption> {
                series =
                    one(
                        pieSeries {
                            name = "访问来源"
                            radius = StringOrDoubleOrArray("55%")
                            roseType = RoseTypes.area
                            typedData = visitSource()
                            // The original writes `itemStyle.normal`; ECharts 6 has no `normal`
                            // state any more, so the shadow is set on the style itself.
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    shadowBlur = 200.0
                                    shadowColor = ZRColor("rgba(0, 0, 0, 0.5)")
                                }
                        },
                    )
            }
        },
        docExample.chart("doc-example/tutorial-styling-step3", "教程：暗色背景", "Tutorial: dark background") {
            unsafeJso<EChartsOption> {
                backgroundColor = ZRColor("#2c343c")
                series =
                    one(
                        pieSeries {
                            name = "访问来源"
                            radius = StringOrDoubleOrArray("55%")
                            roseType = RoseTypes.area
                            typedData = visitSource()
                            label = unsafeJso<LabelOption> { color = "rgba(255, 255, 255, 0.3)" }
                            labelLine = unsafeJso<PieLabelLineOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("rgba(255, 255, 255, 0.3)") } }
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    shadowBlur = 200.0
                                    shadowColor = ZRColor("rgba(0, 0, 0, 0.5)")
                                }
                        },
                    )
            }
        },
        docExample.chart("doc-example/tutorial-styling-step4", "教程：统一颜色", "Tutorial: single colour") {
            unsafeJso<EChartsOption> {
                backgroundColor = ZRColor("#2c343c")
                series =
                    one(
                        pieSeries {
                            name = "访问来源"
                            radius = StringOrDoubleOrArray("55%")
                            roseType = RoseTypes.area
                            typedData = visitSource()
                            label = unsafeJso<LabelOption> { color = "rgba(255, 255, 255, 0.3)" }
                            labelLine = unsafeJso<PieLabelLineOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("rgba(255, 255, 255, 0.3)") } }
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    color = ZRColor("#c23531")
                                    shadowBlur = 200.0
                                    shadowColor = ZRColor("rgba(0, 0, 0, 0.5)")
                                }
                        },
                    )
            }
        },
        docExample.chart("doc-example/tutorial-styling-step5", "教程：按亮度着色", "Tutorial: lightness by value") {
            unsafeJso<EChartsOption> {
                backgroundColor = ZRColor("#2c343c")
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            show = false
                            min = 80.0
                            max = 600.0
                            inRange =
                                unsafeJso {
                                    colorLightness = DoubleOrDoubleArray(arrayOf(0.0, 1.0))
                                }
                        },
                    )
                series =
                    one(
                        pieSeries {
                            name = "访问来源"
                            radius = StringOrDoubleOrArray("55%")
                            roseType = RoseTypes.area
                            typedData = visitSource()
                            label = unsafeJso<LabelOption> { color = "rgba(255, 255, 255, 0.3)" }
                            labelLine = unsafeJso<PieLabelLineOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("rgba(255, 255, 255, 0.3)") } }
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    color = ZRColor("#c23531")
                                    shadowBlur = 200.0
                                    shadowColor = ZRColor("rgba(0, 0, 0, 0.5)")
                                }
                        },
                    )
            }
        },
        docExample.chart("doc-example/aria-decal-simple", "无障碍花纹（简单）", "Aria decal (simple)") {
            unsafeJso<EChartsOption> {
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            data = strings("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                series =
                    arrayOf<SeriesOption>(
                        barSeries { typedData = numbers(120.0, 200.0, 150.0, 80.0, 70.0, 110.0, 130.0) },
                        barSeries { typedData = numbers(20.0, 40.0, 90.0, 40.0, 30.0, 70.0, 120.0) },
                        barSeries { typedData = numbers(140.0, 230.0, 120.0, 50.0, 30.0, 150.0, 120.0) },
                    )
                aria =
                    unsafeJso<AriaOption> {
                        enabled = true
                        decal = unsafeJso<AriaDecalOption> { show = true }
                    }
            }
        },
        docExample.chart("doc-example/aria-decal-newspaper", "无障碍花纹（报纸风格）", "Aria decal (newspaper)") {
            val blackBorder = unsafeJso<ItemStyleOption> {
                borderColor = ZRColor("black")
                borderWidth = 2.0
            }
            unsafeJso<EChartsOption> {
                color =
                    arrayOf(
                        ZRColor("white"),
                        ZRColor("black"),
                        ZRColor("white"),
                        ZRColor("white"),
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            data = strings("Q1", "Q2", "Q3", "Q4")
                            axisLine = unsafeJso<AxisLineOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("black") } }
                            axisTick = unsafeJso<AxisTickOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("black") } }
                            axisLabel = unsafeJso<AxisLabelOption> { color = "black" }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            axisLine = unsafeJso<AxisLineOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("black") } }
                            axisTick = unsafeJso<AxisTickOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("black") } }
                            axisLabel = unsafeJso<AxisLabelOption> { color = "black" }
                            splitLine =
                                unsafeJso<SplitLineOption> {
                                    lineStyle =
                                        unsafeJso<SplitLineStyleOption> {
                                            color = ZRColor("black")
                                            type = ZRLineTypes.dashed
                                        }
                                }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            name = "Apples"
                            typedData = numbers(108.0, 26.0, 39.0, 24.0)
                            itemStyle = blackBorder
                        },
                        barSeries {
                            name = "Oranges"
                            typedData = numbers(23.0, 40.0, 60.0, 70.0)
                            itemStyle = blackBorder
                        },
                        barSeries {
                            name = "Bananas"
                            typedData = numbers(129.0, 40.0, 40.0, 50.0)
                            itemStyle = blackBorder
                        },
                        barSeries {
                            name = "Pears"
                            typedData = numbers(40.0, 60.0, 50.0, 89.0)
                            itemStyle = blackBorder
                        },
                    )
                aria =
                    unsafeJso<AriaOption> {
                        decal =
                            unsafeJso<AriaDecalOption> {
                                show = true
                                decals =
                                    DecalList(
                                        arrayOf(
                                            unsafeJso<DecalObject> {
                                                dashArrayX = DecalDashArrayX(0.0)
                                                dashArrayY = DoubleOrDoubleArray(0.0)
                                                color = "black"
                                            },
                                            unsafeJso<DecalObject> {
                                                dashArrayX = DecalDashArrayX(0.0)
                                                dashArrayY = DoubleOrDoubleArray(0.0)
                                                color = "black"
                                            },
                                            unsafeJso<DecalObject> {
                                                dashArrayX =
                                                    DecalDashArrayX(
                                                        arrayOf(DoubleOrDoubleArray(1.0), DoubleOrDoubleArray(0.0)),
                                                    )
                                                dashArrayY = DoubleOrDoubleArray(arrayOf(2.0, 5.0))
                                                symbolSize = 1.0
                                                rotation = kotlin.math.PI / 6
                                                color = "black"
                                            },
                                            unsafeJso<DecalObject> {
                                                symbol = StringOrStringArray("circle")
                                                dashArrayX =
                                                    DecalDashArrayX(
                                                        arrayOf(
                                                            DoubleOrDoubleArray(arrayOf(8.0, 8.0)),
                                                            DoubleOrDoubleArray(arrayOf(0.0, 8.0, 8.0, 0.0)),
                                                        ),
                                                    )
                                                dashArrayY = DoubleOrDoubleArray(arrayOf(6.0, 0.0))
                                                symbolSize = 0.8
                                                color = "black"
                                            },
                                        ),
                                    )
                            }
                    }
            }
        },
        docExample.chart("doc-example/text-block-fragment", "文本块与文本片段", "Text Block and Fragment") {
            unsafeJso<EChartsOption> {
                xAxis = hiddenXAxis()
                yAxis = hiddenYAxis()
                series =
                    arrayOf<SeriesOption>(
                        scatterSeries {
                            symbolSize = SymbolSize(1.0)
                            typedData =
                                dataItems(
                                    ScatterDataItem(arrayOf<OptionDataValue?>(OptionDataValue(0.0), OptionDataValue(0.0))),
                                )
                            label =
                                unsafeJso<LabelOption> {
                                    show = true
                                    formatter =
                                        LabelFormatter(
                                            "The whole box is a {term|Text Block}, with\n" +
                                                "red border and grey background.\n" +
                                                "{fragment1|A Text Fragment} {fragment2|Another Text Fragment}\n" +
                                                "Text fragments can be customized.",
                                        )
                                    backgroundColor = ZRColor("#eee")
                                    borderColor = "rgb(199,86,83)"
                                    borderWidth = 2.0
                                    borderRadius = DoubleOrDoubleArray(5.0)
                                    padding = DoubleOrDoubleArray(10.0)
                                    color = "#000"
                                    fontSize = StringOrDouble(14.0)
                                    shadowBlur = 3.0
                                    shadowColor = ZRColor("#888")
                                    shadowOffsetX = 0.0
                                    shadowOffsetY = 3.0
                                    lineHeight = 30.0
                                    rich =
                                        recordOf(
                                            "term" to
                                                unsafeJso<TextStyleOption> {
                                                    fontSize = StringOrDouble(18.0)
                                                    color = "rgb(199,86,83)"
                                                },
                                            "fragment1" to
                                                unsafeJso<TextStyleOption> {
                                                    backgroundColor = ZRColor("#000")
                                                    color = "yellow"
                                                    padding = DoubleOrDoubleArray(5.0)
                                                },
                                            "fragment2" to
                                                unsafeJso<TextStyleOption> {
                                                    backgroundColor = ZRColor("#339911")
                                                    color = "#fff"
                                                    borderRadius = DoubleOrDoubleArray(15.0)
                                                    padding = DoubleOrDoubleArray(5.0)
                                                },
                                        ).unsafeCast<Record<String, TextStyleOption>>()
                                }
                        },
                    )
            }
        },
        docExample.chart("doc-example/text-options", "文本样式选项", "Text Options") {
            unsafeJso<EChartsOption> {
                xAxis = hiddenXAxis()
                yAxis = hiddenYAxis()
                series =
                    arrayOf<SeriesOption>(
                        scatterSeries {
                            symbolSize = SymbolSize(1.0)
                            typedData =
                                dataItems(
                                    ScatterDataItem(arrayOf<OptionDataValue?>(OptionDataValue(0.0), OptionDataValue(0.0))),
                                )
                            // The original puts the label on the single data item; a series-level
                            // label renders the same text for one point.
                            label =
                                unsafeJso<LabelOption> {
                                    show = true
                                    formatter =
                                        LabelFormatter(
                                            "Plain text\n" +
                                                "{textBorder|textBorderColor + textBorderWidth}\n" +
                                                "{textShadow|textShadowColor + textShadowBlur + textShadowOffsetX + textShadowOffsetY}\n" +
                                                "{bg|backgroundColor + borderRadius + padding}\n" +
                                                "{border|borderColor + borderWidth + borderRadius + padding}\n" +
                                                "{shadow|shadowColor + shadowBlur + shadowOffsetX + shadowOffsetY}",
                                        )
                                    backgroundColor = ZRColor("#eee")
                                    borderColor = "#333"
                                    borderWidth = 2.0
                                    borderRadius = DoubleOrDoubleArray(5.0)
                                    padding = DoubleOrDoubleArray(10.0)
                                    color = "#000"
                                    fontSize = StringOrDouble(14.0)
                                    shadowBlur = 3.0
                                    shadowColor = ZRColor("#888")
                                    shadowOffsetX = 0.0
                                    shadowOffsetY = 3.0
                                    lineHeight = 30.0
                                    rich =
                                        recordOf(
                                            "textBorder" to
                                                unsafeJso<TextStyleOption> {
                                                    fontSize = StringOrDouble(20.0)
                                                    textBorderColor = "#000"
                                                    textBorderWidth = 3.0
                                                    color = "#fff"
                                                },
                                            "textShadow" to
                                                unsafeJso<TextStyleOption> {
                                                    fontSize = StringOrDouble(16.0)
                                                    textShadowBlur = 5.0
                                                    textShadowColor = "#000"
                                                    textShadowOffsetX = 3.0
                                                    textShadowOffsetY = 3.0
                                                    color = "#fff"
                                                },
                                            "bg" to
                                                unsafeJso<TextStyleOption> {
                                                    backgroundColor = ZRColor("#339911")
                                                    color = "#fff"
                                                    borderRadius = DoubleOrDoubleArray(15.0)
                                                    padding = DoubleOrDoubleArray(5.0)
                                                },
                                            "border" to
                                                unsafeJso<TextStyleOption> {
                                                    color = "#000"
                                                    borderColor = "#449911"
                                                    borderWidth = 1.0
                                                    borderRadius = DoubleOrDoubleArray(3.0)
                                                    padding = DoubleOrDoubleArray(5.0)
                                                },
                                            "shadow" to
                                                unsafeJso<TextStyleOption> {
                                                    backgroundColor = ZRColor("#992233")
                                                    padding = DoubleOrDoubleArray(5.0)
                                                    color = "#fff"
                                                    shadowBlur = 5.0
                                                    shadowColor = ZRColor("#336699")
                                                    shadowOffsetX = 6.0
                                                    shadowOffsetY = 6.0
                                                },
                                        ).unsafeCast<Record<String, TextStyleOption>>()
                                }
                        },
                    )
            }
        },
        docExample.chart("doc-example/dataset-encode-simple0", "数据集编码（简单）", "Dataset encode (simple)") {
            unsafeJso<EChartsOption> {
                dataset =
                    one(
                        unsafeJso<DatasetOption> {
                            source =
                                datasetArrayRows(
                                    arrayOf(
                                        row("score", "amount", "product"),
                                        row(89.3, 58212.0, "Matcha Latte"),
                                        row(57.1, 78254.0, "Milk Tea"),
                                        row(74.4, 41032.0, "Cheese Cocoa"),
                                        row(50.1, 12755.0, "Cheese Brownie"),
                                        row(89.7, 20145.0, "Matcha Cocoa"),
                                        row(68.1, 79146.0, "Tea"),
                                        row(19.6, 91852.0, "Orange Juice"),
                                        row(10.6, 101852.0, "Lemon Juice"),
                                        row(32.7, 20112.0, "Walnut Brownie"),
                                    ).unsafeCast<Array<Array<OptionDataValue>>>(),
                                )
                        },
                    )
                grid = one(unsafeJso<GridOption> { containLabel = true })
                xAxis = one(unsafeJso<XAxisOption> {})
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.category })
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            // The dataset columns drive both axes; nothing is encoded on the data.
                            encode =
                                unsafeJso<OptionEncode> {
                                    this.x = OptionEncodeValue(DimensionLoose("amount"))
                                    this.y = OptionEncodeValue(DimensionLoose("product"))
                                }
                        },
                    )
            }
        },
        docExample.chart("doc-example/scatter-tutorial-dataZoom-1", "散点图与缩放（一）", "Scatter with dataZoom (1)") {
            val values =
                arrayOf(
                    doubleArrayOf(14.616, 7.241, 0.896),
                    doubleArrayOf(3.958, 5.701, 0.955),
                    doubleArrayOf(2.768, 8.971, 0.669),
                    doubleArrayOf(9.051, 9.710, 0.171),
                    doubleArrayOf(14.046, 4.182, 0.536),
                    doubleArrayOf(12.295, 1.429, 0.962),
                    doubleArrayOf(4.417, 8.167, 0.113),
                    doubleArrayOf(0.492, 4.771, 0.785),
                    doubleArrayOf(7.632, 2.605, 0.645),
                    doubleArrayOf(14.242, 5.042, 0.368),
                )
            unsafeJso<EChartsOption> {
                xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.value })
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.slider
                            start = 1.0
                            end = 35.0
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        scatterSeries {
                            name = "scatter"
                            itemStyle = unsafeJso<ItemStyleOption> { opacity = 0.8 }
                            symbolSize = SymbolSize { value, _ -> DoubleOrDoubleArray(numberAt(value, 2) * 40) }
                            typedData = points(*values)
                        },
                    )
            }
        },
    )
