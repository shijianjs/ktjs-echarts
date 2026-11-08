package demo.examples.pie

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.LegendTypes
import io.github.shijianjs.ktjs.echarts.Orients
import io.github.shijianjs.ktjs.echarts.TooltipTriggers
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.EmphasisOption
import io.github.shijianjs.ktjs.echarts.options.ItemStyleOption
import io.github.shijianjs.ktjs.echarts.options.LegendOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.StringOrDoubleOrArray
import io.github.shijianjs.ktjs.echarts.options.TitleOption
import io.github.shijianjs.ktjs.echarts.options.TooltipFormatter
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.dataItems
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.pieSeries
import js.objects.unsafeJso
import io.github.shijianjs.ktjs.zrender.ZRColor
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
