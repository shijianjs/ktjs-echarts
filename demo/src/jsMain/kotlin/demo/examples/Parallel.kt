package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.AxisTypes
import echarts.options.EChartsOption
import echarts.options.LineStyleOption
import echarts.options.ParallelAxisOption
import echarts.options.ParallelSeriesDataItem
import echarts.options.SeriesOption
import echarts.options.dataItems
import echarts.options.one
import echarts.options.parallelSeries
import echarts.options.strings
import js.objects.unsafeJso

/*
 * Parallel examples ported from `public/examples/ts/parallel-*.ts` of apache/echarts-examples.
 *
 * `parallel-aqi` is not ported: it reads the AQI table of the example server.
 */

private val parallel = Category(Groups.PARALLEL, "examples/Parallel.kt")

val parallelEntries: List<CatalogEntry> =
    listOf(
        parallel.chart("parallel-simple", "基础平行坐标", "Basic Parallel") {
            unsafeJso<EChartsOption> {
                parallelAxis =
                    arrayOf(
                        unsafeJso<ParallelAxisOption> { dim = echarts.options.IndexOrIndexArray(0.0); name = "Price" },
                        unsafeJso<ParallelAxisOption> { dim = echarts.options.IndexOrIndexArray(1.0); name = "Net Weight" },
                        unsafeJso<ParallelAxisOption> { dim = echarts.options.IndexOrIndexArray(2.0); name = "Amount" },
                        unsafeJso<ParallelAxisOption> {
                            dim = echarts.options.IndexOrIndexArray(3.0)
                            name = "Score"
                            type = AxisTypes.category
                            data = strings("Excellent", "Good", "OK", "Bad")
                        },
                    )
                series =
                    one(
                        parallelSeries {
                            lineStyle = unsafeJso<LineStyleOption> { width = 4.0 }
                            typedData =
                                dataItems(
                                    ParallelSeriesDataItem(row(12.99, 100.0, 82.0, "Good")),
                                    ParallelSeriesDataItem(row(9.99, 80.0, 77.0, "OK")),
                                    ParallelSeriesDataItem(row(20.0, 120.0, 60.0, "Excellent")),
                                )
                        },
                    )
            }
        },
    )
