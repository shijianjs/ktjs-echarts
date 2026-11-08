package demo.examples.parallel

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.AxisTypes
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.LineStyleOption
import io.github.shijianjs.ktjs.echarts.options.ParallelAxisOption
import io.github.shijianjs.ktjs.echarts.options.ParallelSeriesDataItem
import io.github.shijianjs.ktjs.echarts.options.dataItems
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.parallelSeries
import io.github.shijianjs.ktjs.echarts.options.strings
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.custom.dim
import demo.examples.shared.row

private val parallelSimpleCategory = Category(Groups.PARALLEL, "examples/parallel/ParallelSimple.kt")

val parallelSimpleEntry: CatalogEntry =
    parallelSimpleCategory.chart("parallel-simple", "基础平行坐标", "Basic Parallel") {
            unsafeJso<EChartsOption> {
                parallelAxis =
                    arrayOf(
                        unsafeJso<ParallelAxisOption> { dim = io.github.shijianjs.ktjs.echarts.options.IndexOrIndexArray(0.0); name = "Price" },
                        unsafeJso<ParallelAxisOption> { dim = io.github.shijianjs.ktjs.echarts.options.IndexOrIndexArray(1.0); name = "Net Weight" },
                        unsafeJso<ParallelAxisOption> { dim = io.github.shijianjs.ktjs.echarts.options.IndexOrIndexArray(2.0); name = "Amount" },
                        unsafeJso<ParallelAxisOption> {
                            dim = io.github.shijianjs.ktjs.echarts.options.IndexOrIndexArray(3.0)
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
        }
