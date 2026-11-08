package demo.examples.funnel

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.EmphasisOption
import io.github.shijianjs.ktjs.echarts.options.ItemStyleOption
import io.github.shijianjs.ktjs.echarts.options.LabelLineOption
import io.github.shijianjs.ktjs.echarts.options.LabelOption
import io.github.shijianjs.ktjs.echarts.options.LabelPosition
import io.github.shijianjs.ktjs.echarts.options.LegendOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.TitleOption
import io.github.shijianjs.ktjs.echarts.options.funnelSeries
import io.github.shijianjs.ktjs.echarts.options.one
import js.objects.unsafeJso
import io.github.shijianjs.ktjs.zrender.ZRColor
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
                            label = unsafeJso<LabelOption> { formatter = io.github.shijianjs.ktjs.echarts.options.LabelFormatter("{b}Expected") }
                            labelLine = unsafeJso<LabelLineOption> { show = false }
                            itemStyle = unsafeJso<ItemStyleOption> { opacity = 0.7 }
                            emphasis =
                                unsafeJso<EmphasisOption> {
                                    label =
                                        unsafeJso<LabelOption> {
                                            position = LabelPosition("inside")
                                            formatter = io.github.shijianjs.ktjs.echarts.options.LabelFormatter("{b}Expected: {c}%")
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
                                    formatter = io.github.shijianjs.ktjs.echarts.options.LabelFormatter("{c}%")
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
                                            formatter = io.github.shijianjs.ktjs.echarts.options.LabelFormatter("{b}Actual: {c}%")
                                        }
                                }
                            z = 100.0
                            typedData = stages(30.0, 10.0, 5.0, 50.0, 80.0)
                        },
                    )
            }
        }
