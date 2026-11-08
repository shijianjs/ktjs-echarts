package demo.examples.dataset

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.AxisTypes
import io.github.shijianjs.ktjs.echarts.SeriesLayoutBys
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.GridOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.XAxisOption
import io.github.shijianjs.ktjs.echarts.options.YAxisOption
import io.github.shijianjs.ktjs.echarts.options.barSeries
import io.github.shijianjs.ktjs.echarts.options.one
import js.objects.unsafeJso
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.chord.column
import demo.examples.shared.row

private val datasetSeriesLayoutByCategory = Category(Groups.DATASET, "examples/dataset/DatasetSeriesLayoutBy.kt")

val datasetSeriesLayoutByEntry: CatalogEntry =
    datasetSeriesLayoutByCategory.chart("dataset-series-layout-by", "系列按行和按列排布", "Series Layout By Column or Row") {
            unsafeJso<EChartsOption> {
                legend = emptyLegend()
                tooltip = emptyTooltip()
                dataset =
                    one(
                        rowsDataset(
                            arrayOf(
                                row("product", "2012", "2013", "2014", "2015"),
                                row("Matcha Latte", 41.1, 30.4, 65.1, 53.3),
                                row("Milk Tea", 86.5, 92.1, 85.7, 83.1),
                                row("Cheese Cocoa", 24.1, 67.2, 79.5, 86.4),
                            ),
                        ),
                    )
                xAxis =
                    arrayOf(
                        unsafeJso<XAxisOption> { type = AxisTypes.category; gridIndex = 0.0 },
                        unsafeJso<XAxisOption> { type = AxisTypes.category; gridIndex = 1.0 },
                    )
                yAxis =
                    arrayOf(
                        unsafeJso<YAxisOption> { gridIndex = 0.0 },
                        unsafeJso<YAxisOption> { gridIndex = 1.0 },
                    )
                grid =
                    arrayOf(
                        unsafeJso<GridOption> { bottom = StringOrDouble("55%") },
                        unsafeJso<GridOption> { top = StringOrDouble("55%") },
                    )
                this.series =
                    arrayOf<SeriesOption>(
                        // The first three series are laid out by row, in the first grid.
                        barSeries { seriesLayoutBy = SeriesLayoutBys.row },
                        barSeries { seriesLayoutBy = SeriesLayoutBys.row },
                        barSeries { seriesLayoutBy = SeriesLayoutBys.row },
                        // The others are laid out by column, in the second grid.
                        gridBar(),
                        gridBar(),
                        gridBar(),
                        gridBar(),
                    )
            }
        }
