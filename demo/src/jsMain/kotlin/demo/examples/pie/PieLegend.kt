package demo.examples.pie

import demo.CatalogEntry
import demo.Groups
import echarts.LegendTypes
import echarts.Orients
import echarts.TooltipTriggers
import echarts.options.EChartsOption
import echarts.options.EmphasisOption
import echarts.options.ItemStyleOption
import echarts.options.LegendOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.StringOrDoubleOrArray
import echarts.options.TitleOption
import echarts.options.TooltipFormatter
import echarts.options.TooltipOption
import echarts.options.dataItems
import echarts.options.one
import echarts.options.pieSeries
import js.objects.unsafeJso
import zrender.ZRColor
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.shared.legendNames
import demo.examples.shared.pieItem
import demo.examples.sunburst.radius

private val pieLegendCategory = Category(Groups.PIE, "examples/pie/PieLegend.kt")

val pieLegendEntry: CatalogEntry =
    pieLegendCategory.chart("pie-legend", "可滚动的图例", "Pie with Scrollable Legend") {
            val surnames = surnames(50)
            val items =
                dataItems(*Array(surnames.size) { index -> pieItem(surnames[index].first, surnames[index].second) })
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "同名数量统计"
                            subtext = "纯属虚构"
                            left = StringOrDouble("center")
                        },
                    )
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.item
                            formatter = TooltipFormatter("{a} <br/>{b} : {c} ({d}%)")
                        },
                    )
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            type = LegendTypes.scroll
                            orient = Orients.vertical
                            right = StringOrDouble(10.0)
                            top = StringOrDouble(20.0)
                            bottom = StringOrDouble(20.0)
                            data = legendNames(*Array(surnames.size) { surnames[it].first })
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        pieSeries {
                            name = "姓名"
                            radius = StringOrDoubleOrArray("55%")
                            center = arrayOf(StringOrDouble("40%"), StringOrDouble("50%"))
                            typedData = items
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
        }
