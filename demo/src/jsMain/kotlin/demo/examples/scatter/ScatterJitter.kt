package demo.examples.scatter

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.AxisTypes
import io.github.shijianjs.ktjs.echarts.options.AxisMinMax
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.GridOption
import io.github.shijianjs.ktjs.echarts.options.ItemStyleOption
import io.github.shijianjs.ktjs.echarts.options.ScatterDataItem
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.TitleOption
import io.github.shijianjs.ktjs.echarts.options.XAxisOption
import io.github.shijianjs.ktjs.echarts.options.YAxisOption
import io.github.shijianjs.ktjs.echarts.options.dataItems
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.scatterSeries
import io.github.shijianjs.ktjs.echarts.options.strings
import js.objects.unsafeJso
import kotlin.js.js
import demo.examples.Category
import demo.examples.shared.fixed
import demo.examples.shared.row

private val scatterJitterCategory = Category(Groups.SCATTER, "examples/scatter/ScatterJitter.kt")

val scatterJitterEntry: CatalogEntry =
    scatterJitterCategory.interactive("scatter-jitter", "带抖动的散点图", "Scatter with Jittering") { chart ->
            // The original derives the jitter amplitude from the chart width, so the option is built
            // on mount; a fixed seed keeps the cloud reproducible.
            var seed = 48151623L
            fun nextRandom(): Double {
                seed = (seed * 6364136223846793005L + 1442695040888963407L) and Long.MAX_VALUE
                return (seed % 100000L).toDouble() / 100000.0
            }
            val cloud =
                dataItems(
                    *buildList {
                        for (day in 0 until 7) {
                            for (index in 0 until 1000) {
                                val y = kotlin.math.tan(index.toDouble()) / 2.0 + 7.0
                                add(ScatterDataItem(row(day.toDouble(), y, nextRandom())))
                            }
                        }
                    }.toTypedArray(),
                )
            val gridLeft = 80.0
            val gridRight = 50.0
            chart.setOption(
                unsafeJso<EChartsOption> {
                    title = one(unsafeJso<TitleOption> { text = "Scatter with Jittering" })
                    grid = one(unsafeJso<GridOption> { left = StringOrDouble(gridLeft); right = StringOrDouble(gridRight) })
                    xAxis =
                        one(
                            unsafeJso<XAxisOption> {
                                type = AxisTypes.category
                                jitter = (chart.getWidth() - gridLeft - gridRight) / 7.0 * 0.8
                                data = strings("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                            },
                        )
                    yAxis =
                        one(
                            unsafeJso<YAxisOption> {
                                type = AxisTypes.value
                                max = AxisMinMax(10.0)
                                min = AxisMinMax(0.0)
                            },
                        )
                    series =
                        arrayOf<SeriesOption>(
                            scatterSeries {
                                name = "Sleeping Hours"
                                typedData = cloud
                                colorBy = "data"
                                itemStyle = unsafeJso<ItemStyleOption> { opacity = 0.4 }
                            },
                        )
                },
            )
        }
