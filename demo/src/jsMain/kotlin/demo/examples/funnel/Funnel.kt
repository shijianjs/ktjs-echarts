package demo.examples.funnel

import demo.CatalogEntry
import demo.Groups
import echarts.FunnelSorts
import echarts.options.EChartsOption
import echarts.options.EmphasisOption
import echarts.options.ItemStyleOption
import echarts.options.LabelLineOption
import echarts.options.LabelOption
import echarts.options.LabelPosition
import echarts.options.LegendOption
import echarts.options.LineStyleOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.TitleOption
import echarts.options.funnelSeries
import echarts.options.one
import js.objects.unsafeJso
import zrender.ZRColor
import zrender.ZRLineTypes
import demo.examples.Category
import demo.examples.shared.legendNames
import demo.examples.shared.toolbox

private val funnelCategory = Category(Groups.FUNNEL, "examples/funnel/Funnel.kt")

val funnelEntry: CatalogEntry =
    funnelCategory.chart("funnel", "漏斗图", "Funnel Chart") {
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
        }
