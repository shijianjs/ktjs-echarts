package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.AxisTypes
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.LegendOption
import io.github.shijianjs.ktjs.echarts.options.LineStyleOption
import io.github.shijianjs.ktjs.echarts.options.MarkLine2DDataItemDimOption
import io.github.shijianjs.ktjs.echarts.options.MarkLineDataItem
import io.github.shijianjs.ktjs.echarts.options.MarkLineOption
import io.github.shijianjs.ktjs.echarts.MarkerStatisticTypes
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.XAxisOption
import io.github.shijianjs.ktjs.echarts.options.barSeries
import io.github.shijianjs.ktjs.echarts.options.numbers
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.strings
import js.objects.unsafeJso
import kotlin.js.js
import io.github.shijianjs.ktjs.zrender.ZRLineTypes
import demo.examples.Category

private val barStackCategory = Category(Groups.BAR, "examples/bar/BarStack.kt")

val barStackEntry: CatalogEntry =
    barStackCategory.chart("bar-stack", "堆叠柱状图", "Stacked Column Chart") {
            unsafeJso<EChartsOption> {
                tooltip = axisTooltip
                legend = one(unsafeJso<LegendOption> {})
                xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.category; data = strings(*week) })
                yAxis = valueAxis
                series =
                    arrayOf<SeriesOption>(
                        barSeries { name = "Direct"; emphasis = seriesFocus; typedData = numbers(320.0, 332.0, 301.0, 334.0, 390.0, 330.0, 320.0) },
                        barSeries { name = "Email"; stack = "Ad"; emphasis = seriesFocus; typedData = numbers(120.0, 132.0, 101.0, 134.0, 90.0, 230.0, 210.0) },
                        barSeries { name = "Union Ads"; stack = "Ad"; emphasis = seriesFocus; typedData = numbers(220.0, 182.0, 191.0, 234.0, 290.0, 330.0, 310.0) },
                        barSeries { name = "Video Ads"; stack = "Ad"; emphasis = seriesFocus; typedData = numbers(150.0, 232.0, 201.0, 154.0, 190.0, 330.0, 410.0) },
                        barSeries {
                            name = "Search Engine"
                            emphasis = seriesFocus
                            typedData = numbers(862.0, 1018.0, 964.0, 1026.0, 1679.0, 1600.0, 1570.0)
                            markLine =
                                unsafeJso<MarkLineOption> {
                                    lineStyle = unsafeJso<LineStyleOption> { type = ZRLineTypes.dashed }
                                    data =
                                        arrayOf(
                                            MarkLineDataItem(
                                                arrayOf(
                                                    unsafeJso<MarkLine2DDataItemDimOption> { type = MarkerStatisticTypes.min },
                                                    unsafeJso<MarkLine2DDataItemDimOption> { type = MarkerStatisticTypes.max },
                                                ),
                                            ),
                                        )
                                }
                        },
                        barSeries { name = "Baidu"; barWidth = StringOrDouble(5.0); stack = "Search Engine"; emphasis = seriesFocus; typedData = numbers(620.0, 732.0, 701.0, 734.0, 1090.0, 1130.0, 1120.0) },
                        barSeries { name = "Google"; stack = "Search Engine"; emphasis = seriesFocus; typedData = numbers(120.0, 132.0, 101.0, 134.0, 290.0, 230.0, 220.0) },
                        barSeries { name = "Bing"; stack = "Search Engine"; emphasis = seriesFocus; typedData = numbers(60.0, 72.0, 71.0, 74.0, 190.0, 130.0, 110.0) },
                        barSeries { name = "Others"; stack = "Search Engine"; emphasis = seriesFocus; typedData = numbers(62.0, 82.0, 91.0, 84.0, 109.0, 110.0, 120.0) },
                    )
            }
        }
