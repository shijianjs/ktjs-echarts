package demo.examples.pie

import demo.CatalogEntry
import demo.Groups
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
import demo.examples.matrix.names
import demo.examples.shared.legendNames
import demo.examples.shared.pieItem
import demo.examples.sunburst.radius

private val pieHighlightCategory = Category(Groups.PIE, "examples/pie/PieHighlight.kt")

val pieHighlightEntry: CatalogEntry =
    pieHighlightCategory.interactive("pie-highlight", "饼图程序调用高亮示例", "Pie Highlight") { chart, holder ->
            val names = arrayOf("直接访问", "邮件营销", "联盟广告", "视频广告", "搜索引擎")
            val values = arrayOf(335.0, 310.0, 234.0, 135.0, 1548.0)
            var currentIndex = -1
            chart.setOption(
                unsafeJso<EChartsOption> {
                    title =
                        one(
                            unsafeJso<TitleOption> {
                                text = "饼图程序调用高亮示例"
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
                                orient = Orients.vertical
                                left = StringOrDouble("left")
                                data = legendNames(*names)
                            },
                        )
                    series =
                        arrayOf<SeriesOption>(
                            pieSeries {
                                name = "访问来源"
                                radius = StringOrDoubleOrArray("55%")
                                center = arrayOf(StringOrDouble("50%"), StringOrDouble("60%"))
                                typedData = dataItems(*Array(names.size) { index -> pieItem(names[index], values[index]) })
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
                },
            )
            holder.setInterval({
                val len = names.size
                chart.dispatchAction(seriesAction("downplay", currentIndex))
                currentIndex = (currentIndex + 1) % len
                chart.dispatchAction(seriesAction("highlight", currentIndex))
                chart.dispatchAction(seriesAction("showTip", currentIndex))
            }, 1000)
        }
