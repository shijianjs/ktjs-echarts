package demo.examples

import demo.CatalogEntry
import echarts.*
import echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import web.cssom.TextAlign
import zrender.ZRColor

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
    )
