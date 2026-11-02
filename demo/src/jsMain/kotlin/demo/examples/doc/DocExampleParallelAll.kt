package demo.examples.doc

import demo.CatalogEntry
import echarts.*
import echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import zrender.ZRColor
import demo.examples.Category
import demo.examples.custom.dim
import demo.examples.shared.legendNames

private val docExampleParallelAllCategory = Category(Groups.DOC, "examples/doc/DocExampleParallelAll.kt")

val docExampleParallelAllEntry: CatalogEntry =
    docExampleParallelAllCategory.chart("doc-example/parallel-all", "平行坐标（多指标）", "Parallel (all)") {
            unsafeJso<EChartsOption> {
                color = arrayOf(ZRColor("#c23531"), ZRColor("#91c7ae"), ZRColor("#dd8668"))
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            top = StringOrDouble(10.0)
                            data = legendNames("北京", "上海", "广州")
                            itemGap = 20.0
                        },
                    )
                parallelAxis =
                    arrayOf(
                        unsafeJso<ParallelAxisOption> {
                            dim = IndexOrIndexArray(0.0)
                            name = "日期"
                            inverse = true
                            max = AxisMinMax(31.0)
                            nameLocation = AxisNameLocations.start
                        },
                        unsafeJso<ParallelAxisOption> { dim = IndexOrIndexArray(1.0); name = "AQI" },
                        unsafeJso<ParallelAxisOption> { dim = IndexOrIndexArray(2.0); name = "PM2.5" },
                        unsafeJso<ParallelAxisOption> { dim = IndexOrIndexArray(3.0); name = "PM10" },
                        unsafeJso<ParallelAxisOption> { dim = IndexOrIndexArray(4.0); name = " CO" },
                        unsafeJso<ParallelAxisOption> { dim = IndexOrIndexArray(5.0); name = "NO2" },
                        unsafeJso<ParallelAxisOption> { dim = IndexOrIndexArray(6.0); name = "SO2" },
                        unsafeJso<ParallelAxisOption> {
                            dim = IndexOrIndexArray(7.0)
                            name = "等级"
                            type = AxisTypes.category
                            data = strings("优", "良", "轻度污染", "中度污染", "重度污染", "严重污染")
                        },
                    )
                parallel =
                    one(
                        unsafeJso<ParallelOption> {
                            left = StringOrDouble("5%")
                            right = StringOrDouble("13%")
                            bottom = StringOrDouble("10%")
                            top = StringOrDouble("20%")
                            parallelAxisDefault =
                                unsafeJso<ParallelAxisOption> {
                                    type = AxisTypes.value
                                    name = "AQI指数"
                                    nameLocation = AxisNameLocations.end
                                    nameGap = 20.0
                                    nameTextStyle = unsafeJso<LabelOption> { fontSize = StringOrDouble(12.0) }
                                }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        parallelSeries {
                            name = "北京"
                            lineStyle = unsafeJso<LineStyleOption> { width = 1.0; opacity = 0.5 }
                            typedData = dataItems(*parallelBeijing.toTypedArray())
                        },
                        parallelSeries {
                            name = "上海"
                            lineStyle = unsafeJso<LineStyleOption> { width = 1.0; opacity = 0.5 }
                            typedData = dataItems(*parallelShanghai.toTypedArray())
                        },
                        parallelSeries {
                            name = "广州"
                            lineStyle = unsafeJso<LineStyleOption> { width = 1.0; opacity = 0.5 }
                            typedData = dataItems(*parallelGuangzhou.toTypedArray())
                        },
                    )
            }
        }
