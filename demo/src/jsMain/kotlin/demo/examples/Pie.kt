package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.AnimationEasings
import echarts.AnimationTypes
import echarts.Orients
import echarts.RoseTypes
import echarts.TooltipTriggers
import echarts.options.AnimationDelay
import echarts.options.BooleanOrDouble
import echarts.options.DoubleOrDoubleArray
import echarts.options.EChartsOption
import echarts.options.EmphasisOption
import echarts.options.ItemStyleOption
import echarts.options.LabelFormatter
import echarts.options.LabelLineOption
import echarts.options.LabelOption
import echarts.options.LabelPosition
import echarts.options.LegendOption
import echarts.options.LineStyleOption
import echarts.options.PieDataItem
import echarts.options.PieLabelAlignTos
import echarts.options.PieLabelOption
import echarts.options.SelectionModes
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.StringOrDoubleOrArray
import echarts.options.TextStyleOption
import echarts.options.TitleOption
import echarts.options.TooltipFormatter
import echarts.options.TooltipOption
import echarts.options.VisualMapOption
import echarts.options.VisualOptionUnit
import echarts.options.dataItems
import echarts.options.one
import echarts.options.pieSeries
import js.objects.unsafeJso
import zrender.ZRColor
import zrender.ZRFontWeights

/*
 * Pie examples ported from `public/examples/ts/pie-*.ts` of apache/echarts-examples.
 *
 * Two deliberate deviations, both about assets rather than options:
 *  - `toolbox.feature.mark` is not reproduced: ECharts 6 registers only brush, dataView, dataZoom,
 *    magicType, restore and saveAsImage, so the key never rendered a button.
 *  - `pie-pattern` (300 KB of embedded textures) and `pie-rich-text` (weather icons served by the
 *    example site) need image assets this page does not ship.
 */

private val pie = Category(Groups.PIE, "examples/Pie.kt")

/** The `{ name, value }` set shared by the doughnut-family examples. */
private fun accessFrom(): Array<PieDataItem?> =
    dataItems(
        pieItem("Search Engine", 1048.0),
        pieItem("Direct", 735.0),
        pieItem("Email", 580.0),
        pieItem("Union Ads", 484.0),
        pieItem("Video Ads", 300.0),
    )

/** Inner ring of `pie-nest`. */
private fun nestInner(): Array<PieDataItem?> =
    dataItems(pieItem("Search Engine", 1548.0), pieItem("Direct", 775.0), pieItem("Marketing", 679.0))

/** Outer ring of `pie-nest`. */
private fun nestOuter(): Array<PieDataItem?> =
    dataItems(
        pieItem("Baidu", 1048.0),
        pieItem("Direct", 335.0),
        pieItem("Email", 310.0),
        pieItem("Google", 251.0),
        pieItem("Union Ads", 234.0),
        pieItem("Bing", 147.0),
        pieItem("Video Ads", 135.0),
        pieItem("Others", 102.0),
    )

/** Eight rose values, labelled `rose 1` .. `rose 8`. */
private fun roseData(vararg values: Double): Array<PieDataItem?> =
    dataItems(*Array(values.size) { index -> pieItem("rose ${index + 1}", values[index]) })

/** Legend entries named after [names]. */
private fun legendOf(vararg names: String): Array<LegendOption> = one(unsafeJso<LegendOption> { data = legendNames(*names) })

private val tooltipItem = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.item })

private val centerLegend =
    one(
        unsafeJso<LegendOption> {
            top = StringOrDouble("5%")
            left = StringOrDouble("center")
        },
    )

private val doughnutRadius = StringOrDoubleOrArray(arrayOf(StringOrDouble("40%"), StringOrDouble("70%")))

val pieEntries: List<CatalogEntry> =
    listOf(
        pie.chart("pie-simple", "某站点用户访问来源", "Referer of a Website") {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Referer of a Website"
                            subtext = "Fake Data"
                            left = StringOrDouble("center")
                        },
                    )
                tooltip = tooltipItem
                legend = one(unsafeJso<LegendOption> { orient = Orients.vertical; left = StringOrDouble("left") })
                series =
                    arrayOf<SeriesOption>(
                        pieSeries {
                            name = "Access From"
                            radius = StringOrDoubleOrArray("50%")
                            typedData = accessFrom()
                            emphasis =
                                unsafeJso<EmphasisOption> {
                                    itemStyle =
                                        unsafeJso<ItemStyleOption> {
                                            shadowBlur = 10.0
                                            shadowOffsetX = 0.0
                                            shadowColor = ZRColor("rgba(0, 0, 0, 0.5)")
                                        }
                                }
                        },
                    )
            }
        },
        pie.chart("pie-doughnut", "环形图", "Doughnut Chart") {
            unsafeJso<EChartsOption> {
                tooltip = tooltipItem
                legend = centerLegend
                series =
                    arrayOf<SeriesOption>(
                        pieSeries {
                            name = "Access From"
                            radius = doughnutRadius
                            avoidLabelOverlap = false
                            label = unsafeJso<LabelOption> { show = false; position = LabelPosition("center") }
                            emphasis = centerLabelEmphasis()
                            labelLine = unsafeJso<LabelLineOption> { show = false }
                            typedData = accessFrom()
                        },
                    )
            }
        },
        pie.chart("pie-half-donut", "半环形图", "Half Doughnut Chart") {
            unsafeJso<EChartsOption> {
                tooltip = tooltipItem
                legend = centerLegend
                series =
                    arrayOf<SeriesOption>(
                        pieSeries {
                            name = "Access From"
                            radius = doughnutRadius
                            center = arrayOf(StringOrDouble("50%"), StringOrDouble("70%"))
                            startAngle = 180.0
                            endAngle = 360.0
                            typedData = accessFrom()
                        },
                    )
            }
        },
        pie.chart("pie-borderRadius", "圆角环形图", "Doughnut Chart with Rounded Corner") {
            unsafeJso<EChartsOption> {
                tooltip = tooltipItem
                legend = centerLegend
                series =
                    arrayOf<SeriesOption>(
                        pieSeries {
                            name = "Access From"
                            radius = doughnutRadius
                            avoidLabelOverlap = false
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    borderRadius = StringOrDoubleOrArray(10.0)
                                    borderColor = ZRColor("#fff")
                                    borderWidth = 2.0
                                }
                            label = unsafeJso<LabelOption> { show = false; position = LabelPosition("center") }
                            emphasis = centerLabelEmphasis()
                            labelLine = unsafeJso<LabelLineOption> { show = false }
                            typedData = accessFrom()
                        },
                    )
            }
        },
        pie.chart("pie-padAngle", "饼图扇区间隙", "Pie with padAngle") {
            unsafeJso<EChartsOption> {
                tooltip = tooltipItem
                legend = centerLegend
                series =
                    arrayOf<SeriesOption>(
                        pieSeries {
                            name = "Access From"
                            radius = doughnutRadius
                            avoidLabelOverlap = false
                            padAngle = 5.0
                            itemStyle = unsafeJso<ItemStyleOption> { borderRadius = StringOrDoubleOrArray(10.0) }
                            label = unsafeJso<LabelOption> { show = false; position = LabelPosition("center") }
                            emphasis = centerLabelEmphasis()
                            labelLine = unsafeJso<LabelLineOption> { show = false }
                            typedData = accessFrom()
                        },
                    )
            }
        },
        pie.chart("pie-roseType", "南丁格尔玫瑰图", "Nightingale Chart") {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Nightingale Chart"
                            subtext = "Fake Data"
                            left = StringOrDouble("center")
                        },
                    )
                tooltip = one(tooltipWithPercent())
                legend = legendOf("rose1", "rose2", "rose3", "rose4", "rose5", "rose6", "rose7", "rose8")
                series =
                    arrayOf<SeriesOption>(
                        pieSeries {
                            name = "Radius Mode"
                            radius = StringOrDoubleOrArray(arrayOf(StringOrDouble(20.0), StringOrDouble(140.0)))
                            center = arrayOf(StringOrDouble("25%"), StringOrDouble("50%"))
                            roseType = RoseTypes.radius
                            itemStyle = unsafeJso<ItemStyleOption> { borderRadius = StringOrDoubleOrArray(5.0) }
                            label = unsafeJso<LabelOption> { show = false }
                            emphasis = unsafeJso<EmphasisOption> { label = unsafeJso<LabelOption> { show = true } }
                            typedData = roseData(40.0, 33.0, 28.0, 22.0, 20.0, 15.0, 12.0, 10.0)
                        },
                        pieSeries {
                            name = "Area Mode"
                            radius = StringOrDoubleOrArray(arrayOf(StringOrDouble(20.0), StringOrDouble(140.0)))
                            center = arrayOf(StringOrDouble("75%"), StringOrDouble("50%"))
                            roseType = RoseTypes.area
                            itemStyle = unsafeJso<ItemStyleOption> { borderRadius = StringOrDoubleOrArray(5.0) }
                            typedData = roseData(30.0, 28.0, 26.0, 24.0, 22.0, 20.0, 18.0, 16.0)
                        },
                    )
            }
        },
        pie.chart("pie-roseType-simple", "基础南丁格尔玫瑰图", "Nightingale Chart") {
            unsafeJso<EChartsOption> {
                legend = one(unsafeJso<LegendOption> { top = StringOrDouble("bottom") })
                series =
                    arrayOf<SeriesOption>(
                        pieSeries {
                            name = "Nightingale Chart"
                            radius = StringOrDoubleOrArray(arrayOf(StringOrDouble(50.0), StringOrDouble(250.0)))
                            center = arrayOf(StringOrDouble("50%"), StringOrDouble("50%"))
                            roseType = RoseTypes.area
                            itemStyle = unsafeJso<ItemStyleOption> { borderRadius = StringOrDoubleOrArray(8.0) }
                            typedData = roseData(40.0, 38.0, 32.0, 30.0, 28.0, 26.0, 22.0, 18.0)
                        },
                    )
            }
        },
        pie.chart("pie-alignTo", "饼图标签对齐", "Pie Label Align") {
            val data =
                dataItems(
                    pieItem("Apples", 70.0),
                    pieItem("Strawberries", 68.0),
                    pieItem("Bananas", 48.0),
                    pieItem("Oranges", 40.0),
                    pieItem("Pears", 32.0),
                    pieItem("Pineapples", 27.0),
                    pieItem("Grapes", 18.0),
                )
            unsafeJso<EChartsOption> {
                title =
                    arrayOf(
                        unsafeJso<TitleOption> { text = "Pie label alignTo"; left = StringOrDouble("center") },
                        unsafeJso<TitleOption> {
                            subtext = "alignTo: \"none\" (default)"
                            left = StringOrDouble("16.67%")
                            top = StringOrDouble("75%")
                            textAlign = "center"
                        },
                        unsafeJso<TitleOption> {
                            subtext = "alignTo: \"labelLine\""
                            left = StringOrDouble("50%")
                            top = StringOrDouble("75%")
                            textAlign = "center"
                        },
                        unsafeJso<TitleOption> {
                            subtext = "alignTo: \"edge\""
                            left = StringOrDouble("83.33%")
                            top = StringOrDouble("75%")
                            textAlign = "center"
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        pieAlignToSeries(
                            data,
                            PieLabelAlignTos.none,
                            StringOrDouble(0.0),
                            StringOrDouble("66.6667%"),
                            bleedMargin = 5.0,
                        ),
                        pieAlignToSeries(
                            data,
                            PieLabelAlignTos.labelLine,
                            StringOrDouble("33.3333%"),
                            StringOrDouble("33.3333%"),
                            bleedMargin = 5.0,
                        ),
                        pieAlignToSeries(
                            data,
                            PieLabelAlignTos.edge,
                            StringOrDouble("66.6667%"),
                            StringOrDouble(0.0),
                            margin = 20.0,
                        ),
                    )
            }
        },
        pie.chart("pie-nest", "嵌套环形图", "Nested Pies") {
            unsafeJso<EChartsOption> {
                tooltip = one(tooltipWithPercent())
                legend =
                    legendOf(
                        "Direct",
                        "Marketing",
                        "Search Engine",
                        "Email",
                        "Union Ads",
                        "Video Ads",
                        "Baidu",
                        "Google",
                        "Bing",
                        "Others",
                    )
                series =
                    arrayOf<SeriesOption>(
                        pieSeries {
                            name = "Access From"
                            selectedMode = SelectionModes.single
                            radius = StringOrDoubleOrArray(arrayOf(StringOrDouble(0.0), StringOrDouble("30%")))
                            label =
                                unsafeJso<LabelOption> {
                                    position = LabelPosition("inner")
                                    fontSize = StringOrDouble(14.0)
                                }
                            labelLine = unsafeJso<LabelLineOption> { show = false }
                            typedData = nestInner()
                        },
                        pieSeries {
                            name = "Access From"
                            radius = StringOrDoubleOrArray(arrayOf(StringOrDouble("45%"), StringOrDouble("60%")))
                            labelLine = unsafeJso<LabelLineOption> { length = 30.0 }
                            typedLabel =
                                unsafeJso<PieLabelOption> {
                                    formatter = LabelFormatter("{b|{b}：}{c}  {per|{d}%}  ")
                                    backgroundColor = ZRColor("#F6F8FC")
                                    borderColor = "#8C8D8E"
                                    borderWidth = 1.0
                                    borderRadius = DoubleOrDoubleArray(4.0)
                                }
                            typedData = nestOuter()
                        },
                    )
            }
        },
        pie.chart("pie-custom", "饼图自定义样式", "Customized Pie") {
            unsafeJso<EChartsOption> {
                backgroundColor = ZRColor("#2c343c")
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Customized Pie"
                            left = StringOrDouble("center")
                            top = StringOrDouble(20.0)
                            textStyle = unsafeJso<TextStyleOption> { color = "#ccc" }
                        },
                    )
                tooltip = tooltipItem
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            show = false
                            min = 80.0
                            max = 600.0
                            inRange =
                                unsafeJso<VisualOptionUnit> {
                                    colorLightness = DoubleOrDoubleArray(arrayOf(0.0, 1.0))
                                }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        pieSeries {
                            name = "Access From"
                            radius = StringOrDoubleOrArray("55%")
                            center = arrayOf(StringOrDouble("50%"), StringOrDouble("50%"))
                            // The original sorts the items by value before rendering.
                            typedData =
                                dataItems(
                                    pieItem("Video Ads", 235.0),
                                    pieItem("Union Ads", 274.0),
                                    pieItem("Email", 310.0),
                                    pieItem("Direct", 335.0),
                                    pieItem("Search Engine", 400.0),
                                )
                            roseType = RoseTypes.radius
                            label = unsafeJso<LabelOption> { color = "rgba(255, 255, 255, 0.3)" }
                            labelLine =
                                unsafeJso<LabelLineOption> {
                                    lineStyle =
                                        unsafeJso<LineStyleOption> {
                                            color = ZRColor("rgba(255, 255, 255, 0.3)")
                                        }
                                    smooth = BooleanOrDouble(0.2)
                                    length = 10.0
                                    length2 = 20.0
                                }
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    color = ZRColor("#c23531")
                                    shadowBlur = 200.0
                                    shadowColor = ZRColor("rgba(0, 0, 0, 0.5)")
                                }
                            animationType = AnimationTypes.scale
                            animationEasing = AnimationEasings.elasticOut
                            // The original uses Math.random() per item; a fixed sequence keeps the
                            // page reproducible.
                            animationDelay = AnimationDelay { index, _ -> index * 17.0 % 200.0 }
                        },
                    )
            }
        },
    )

/** The hover label of the doughnut family: a large percentage in the middle of the ring. */
private fun centerLabelEmphasis(): EmphasisOption =
    unsafeJso {
        label =
            unsafeJso<LabelOption> {
                show = true
                fontSize = StringOrDouble(40.0)
                fontWeight = ZRFontWeights.bold
            }
    }

/** `{a} <br/>{b} : {c} ({d}%)`, the tooltip of the rose and nested pies. */
private fun tooltipWithPercent(): TooltipOption =
    unsafeJso {
        trigger = TooltipTriggers.item
        formatter = TooltipFormatter("{a} <br/>{b} : {c} ({d}%)")
    }

/** One of the three widths of `pie-alignTo`. */
private fun pieAlignToSeries(
    data: Array<PieDataItem?>,
    alignTo: echarts.options.PieLabelAlignTo,
    left: StringOrDouble,
    right: StringOrDouble,
    bleedMargin: Double? = null,
    margin: Double? = null,
): SeriesOption =
    pieSeries {
        radius = StringOrDoubleOrArray("25%")
        center = arrayOf(StringOrDouble("50%"), StringOrDouble("50%"))
        typedData = data
        typedLabel =
            unsafeJso<PieLabelOption> {
                position = LabelPosition("outer")
                this.alignTo = alignTo
                this.bleedMargin = bleedMargin
                this.margin = margin?.let { StringOrDouble(it) }
            }
        this.left = left
        this.right = right
        top = StringOrDouble(0.0)
        bottom = StringOrDouble(0.0)
    }
