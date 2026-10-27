package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.FunnelAligns
import echarts.FunnelSorts
import echarts.Orients
import echarts.TooltipTriggers
import echarts.options.EChartsOption
import echarts.options.EmphasisOption
import echarts.options.FunnelDataItem
import echarts.options.ItemStyleOption
import echarts.options.LabelLineOption
import echarts.options.LabelOption
import echarts.options.LabelPosition
import echarts.options.LegendOption
import echarts.options.LineStyleOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.TitleOption
import echarts.options.TooltipFormatter
import echarts.options.TooltipOption
import echarts.options.dataItems
import echarts.options.funnelSeries
import echarts.options.one
import js.objects.unsafeJso
import zrender.ZRColor
import zrender.ZRLineTypes

/*
 * Funnel examples ported from `public/examples/ts/funnel*.ts` of apache/echarts-examples.
 *
 * The `toolbox.feature.mark` key of the originals is dropped: ECharts 6 does not register such a
 * feature, so it never rendered anything either.
 */

private val funnel = Category(Groups.FUNNEL, "examples/Funnel.kt")

private val labels = listOf("Show", "Click", "Visit", "Inquiry", "Order")

private val tooltipPercent =
    one(
        unsafeJso<TooltipOption> {
            trigger = TooltipTriggers.item
            formatter = TooltipFormatter("{a} <br/>{b} : {c}%")
        },
    )

private val funnelToolbox = toolbox(dataView = true, restore = true, saveAsImage = true)

/** The five stages, in the order the originals declare them. */
private fun stages(vararg values: Double): Array<FunnelDataItem?> =
    dataItems(*Array(values.size) { index -> funnelItem(labels[index], values[index]) })

val funnelEntries: List<CatalogEntry> =
    listOf(
        funnel.chart("funnel", "漏斗图", "Funnel Chart") {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Funnel" })
                tooltip = tooltipPercent
                toolbox = funnelToolbox
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            data = legendNames(*labels.toTypedArray())
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        funnelSeries {
                            name = "Funnel"
                            left = StringOrDouble("10%")
                            top = StringOrDouble(60.0)
                            bottom = StringOrDouble(60.0)
                            width = StringOrDouble("80%")
                            min = 0.0
                            max = 100.0
                            minSize = StringOrDouble("0%")
                            maxSize = StringOrDouble("100%")
                            sort = FunnelSorts.descending
                            gap = 2.0
                            label = unsafeJso<LabelOption> { show = true; position = LabelPosition("inside") }
                            labelLine =
                                unsafeJso<LabelLineOption> {
                                    length = 10.0
                                    lineStyle = unsafeJso<LineStyleOption> { width = 1.0; type = ZRLineTypes.solid }
                                }
                            itemStyle = unsafeJso<ItemStyleOption> { borderColor = ZRColor("#fff"); borderWidth = 1.0 }
                            emphasis = unsafeJso<EmphasisOption> { label = unsafeJso<LabelOption> { fontSize = StringOrDouble(20.0) } }
                            typedData = stages(60.0, 40.0, 20.0, 80.0, 100.0)
                        },
                    )
            }
        },
        funnel.chart("funnel-align", "漏斗图(对比)", "Funnel Compare") {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Funnel Compare"
                            subtext = "Fake Data"
                            left = StringOrDouble("left")
                            top = StringOrDouble("bottom")
                        },
                    )
                tooltip = tooltipPercent
                toolbox = toolbox(orient = Orients.vertical, top = StringOrDouble("center"), dataView = true, restore = true, saveAsImage = true)
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            orient = Orients.vertical
                            left = StringOrDouble("left")
                            data = legendNames("Prod A", "Prod B", "Prod C", "Prod D", "Prod E")
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        compareFunnel("Funnel", "5%", "50%", FunnelAligns.right, ascending = false),
                        compareFunnel("Pyramid", "5%", "5%", FunnelAligns.right, ascending = true),
                        compareFunnel("Funnel", "55%", "5%", FunnelAligns.left, ascending = false),
                        compareFunnel("Pyramid", "55%", "50%", FunnelAligns.left, ascending = true),
                    )
            }
        },
        funnel.chart("funnel-customize", "漏斗图", "Customized Funnel") {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Funnel" })
                tooltip = tooltipPercent
                toolbox = funnelToolbox
                legend = one(unsafeJso<LegendOption> { data = legendNames(*labels.toTypedArray()) })
                series =
                    arrayOf<SeriesOption>(
                        funnelSeries {
                            name = "Expected"
                            left = StringOrDouble("10%")
                            width = StringOrDouble("80%")
                            label = unsafeJso<LabelOption> { formatter = echarts.options.LabelFormatter("{b}Expected") }
                            labelLine = unsafeJso<LabelLineOption> { show = false }
                            itemStyle = unsafeJso<ItemStyleOption> { opacity = 0.7 }
                            emphasis =
                                unsafeJso<EmphasisOption> {
                                    label =
                                        unsafeJso<LabelOption> {
                                            position = LabelPosition("inside")
                                            formatter = echarts.options.LabelFormatter("{b}Expected: {c}%")
                                        }
                                }
                            typedData = stages(60.0, 40.0, 20.0, 80.0, 100.0)
                        },
                        funnelSeries {
                            name = "Actual"
                            left = StringOrDouble("10%")
                            width = StringOrDouble("80%")
                            maxSize = StringOrDouble("80%")
                            label =
                                unsafeJso<LabelOption> {
                                    position = LabelPosition("inside")
                                    formatter = echarts.options.LabelFormatter("{c}%")
                                    color = "#fff"
                                }
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    opacity = 0.5
                                    borderColor = ZRColor("#fff")
                                    borderWidth = 2.0
                                }
                            emphasis =
                                unsafeJso<EmphasisOption> {
                                    label =
                                        unsafeJso<LabelOption> {
                                            position = LabelPosition("inside")
                                            formatter = echarts.options.LabelFormatter("{b}Actual: {c}%")
                                        }
                                }
                            z = 100.0
                            typedData = stages(30.0, 10.0, 5.0, 50.0, 80.0)
                        },
                    )
            }
        },
        funnel.chart("funnel-mutiple", "多漏斗图", "Multiple Funnels") {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Funnel"
                            left = StringOrDouble("left")
                            top = StringOrDouble("bottom")
                        },
                    )
                tooltip = tooltipPercent
                toolbox =
                    toolbox(
                        orient = Orients.vertical,
                        top = StringOrDouble("center"),
                        dataView = true,
                        restore = true,
                        saveAsImage = true,
                    )
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            orient = Orients.vertical
                            left = StringOrDouble("left")
                            data = legendNames(*labels.toTypedArray())
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        quadrantFunnel("Funnel", "5%", "50%", labelled = false),
                        quadrantFunnel("Pyramid", "5%", "5%", labelled = false),
                        quadrantFunnel("Funnel", "55%", "5%", labelled = true),
                        quadrantFunnel("Pyramid", "55%", "50%", labelled = true),
                    )
            }
        },
    )

/** One quadrant of `funnel-align`. */
private fun compareFunnel(
    name: String,
    left: String,
    top: String,
    align: echarts.FunnelAlign,
    ascending: Boolean,
): SeriesOption =
    funnelSeries {
        this.name = name
        width = StringOrDouble("40%")
        height = StringOrDouble("45%")
        this.left = StringOrDouble(left)
        this.top = StringOrDouble(top)
        funnelAlign = align
        if (ascending) sort = FunnelSorts.ascending
        typedData =
            dataItems(
                funnelItem("Prod C", 60.0),
                funnelItem("Prod D", 30.0),
                funnelItem("Prod E", 10.0),
                funnelItem("Prod B", 80.0),
                funnelItem("Prod A", 100.0),
            )
    }

/** One quadrant of `funnel-mutiple`; the right-hand ones put the label outside. */
private fun quadrantFunnel(name: String, left: String, top: String, labelled: Boolean): SeriesOption =
    funnelSeries {
        this.name = name
        width = StringOrDouble("40%")
        height = StringOrDouble("45%")
        this.left = StringOrDouble(left)
        this.top = StringOrDouble(top)
        if (name == "Pyramid") sort = FunnelSorts.ascending
        if (labelled) label = unsafeJso<LabelOption> { position = LabelPosition("left") }
        typedData = stages(60.0, 30.0, 10.0, 80.0, 100.0)
    }
