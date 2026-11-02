package demo.examples.funnel

import demo.CatalogEntry
import demo.Groups
import echarts.options.EChartsOption
import echarts.options.EmphasisOption
import echarts.options.ItemStyleOption
import echarts.options.LabelLineOption
import echarts.options.LabelOption
import echarts.options.LabelPosition
import echarts.options.LegendOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.TitleOption
import echarts.options.funnelSeries
import echarts.options.one
import js.objects.unsafeJso
import zrender.ZRColor
import demo.examples.Category
import demo.examples.shared.legendNames
import demo.examples.shared.toolbox

private val funnelCustomizeCategory = Category(Groups.FUNNEL, "examples/funnel/FunnelCustomize.kt")

val funnelCustomizeEntry: CatalogEntry =
    funnelCustomizeCategory.chart("funnel-customize", "漏斗图", "Customized Funnel") {
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
        }
