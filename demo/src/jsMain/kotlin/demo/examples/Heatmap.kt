package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.AxisTypes
import echarts.Orients
import echarts.options.EChartsOption
import echarts.options.EmphasisOption
import echarts.options.GridOption
import echarts.options.HeatmapDataItem
import echarts.options.ItemStyleOption
import echarts.options.LabelOption
import echarts.options.SeriesOption
import echarts.options.SplitAreaOption
import echarts.options.StringOrDouble
import echarts.options.TooltipOption
import echarts.options.TooltipPosition
import echarts.options.VisualMapOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.dataItems
import echarts.options.heatmapSeries
import echarts.options.one
import echarts.options.strings
import js.objects.unsafeJso
import zrender.ZRColor

/*
 * Heatmap examples ported from `public/examples/ts/heatmap-*.ts` of apache/echarts-examples.
 *
 * `heatmap-large`, `heatmap-large-piecewise`, `heatmap-map` and `heatmap-bmap` are not ported: the
 * first two generate 20k cells, the others need a map or the Baidu Maps API.
 */

private val heatmap = Category(Groups.HEATMAP, "examples/Heatmap.kt")

private val hours =
    arrayOf(
        "12a", "1a", "2a", "3a", "4a", "5a", "6a", "7a", "8a", "9a", "10a", "11a",
        "12p", "1p", "2p", "3p", "4p", "5p", "6p", "7p", "8p", "9p", "10p", "11p",
    )

private val days = arrayOf("Saturday", "Friday", "Thursday", "Wednesday", "Tuesday", "Monday", "Sunday")

/** One row of the punch card, in the original `[day, hour, value]` order. */
private val punchCardValues =
    listOf(
        doubleArrayOf(5.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0, 4.0, 1.0, 1.0, 3.0, 4.0, 6.0, 4.0, 4.0, 3.0, 3.0, 2.0, 5.0),
        doubleArrayOf(7.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 5.0, 2.0, 2.0, 6.0, 9.0, 11.0, 6.0, 7.0, 8.0, 12.0, 5.0, 5.0, 7.0, 2.0),
        doubleArrayOf(1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 3.0, 2.0, 1.0, 9.0, 8.0, 10.0, 6.0, 5.0, 5.0, 5.0, 7.0, 4.0, 2.0, 4.0),
        doubleArrayOf(7.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 5.0, 4.0, 7.0, 14.0, 13.0, 12.0, 9.0, 5.0, 5.0, 10.0, 6.0, 4.0, 4.0, 1.0),
        doubleArrayOf(1.0, 3.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 2.0, 4.0, 4.0, 2.0, 4.0, 4.0, 14.0, 12.0, 1.0, 8.0, 5.0, 3.0, 7.0, 3.0, 0.0),
        doubleArrayOf(2.0, 1.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 4.0, 1.0, 5.0, 10.0, 5.0, 7.0, 11.0, 6.0, 0.0, 5.0, 3.0, 4.0, 2.0, 0.0),
        doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 2.0, 1.0, 3.0, 4.0, 0.0, 0.0, 0.0, 0.0, 1.0, 2.0, 2.0, 6.0),
    )

/** The original maps every row to `[hour, day, value || "-"]`. */
private fun punchCard(): Array<HeatmapDataItem?> =
    dataItems(
        *punchCardValues
            .flatMapIndexed { day, values ->
                values.mapIndexed { hour, value ->
                    HeatmapDataItem(row(hour.toDouble(), day.toDouble(), if (value > 0.0) value else "-"))
                }
            }.toTypedArray(),
    )

val heatmapEntries: List<CatalogEntry> =
    listOf(
        heatmap.chart("heatmap-cartesian", "笛卡尔坐标系上的热力图", "Heatmap on Cartesian") {
            unsafeJso<EChartsOption> {
                tooltip = one(unsafeJso<TooltipOption> { position = TooltipPosition("top") })
                grid =
                    one(
                        unsafeJso<GridOption> {
                            height = StringOrDouble("50%")
                            top = StringOrDouble("10%")
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            data = strings(*hours)
                            splitArea = unsafeJso<SplitAreaOption> { show = true }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.category
                            data = strings(*days)
                            splitArea = unsafeJso<SplitAreaOption> { show = true }
                        },
                    )
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            min = 0.0
                            max = 10.0
                            calculable = true
                            orient = Orients.horizontal
                            left = StringOrDouble("center")
                            bottom = StringOrDouble("15%")
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        heatmapSeries {
                            name = "Punch Card"
                            typedData = punchCard()
                            label = unsafeJso<LabelOption> { show = true }
                            emphasis =
                                unsafeJso<EmphasisOption> {
                                    itemStyle =
                                        unsafeJso<ItemStyleOption> {
                                            shadowBlur = 10.0
                                            shadowColor = ZRColor("rgba(0, 0, 0, 0.5)")
                                        }
                                }
                        },
                    )
            }
        },
    )
