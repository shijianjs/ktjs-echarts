package demo.examples.dataset

import demo.CatalogEntry
import demo.Groups
import echarts.AxisTypes
import echarts.options.EChartsOption
import echarts.options.SeriesOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.barSeries
import echarts.options.one
import js.objects.unsafeJso
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.chord.column
import demo.examples.shared.row

private val datasetSimple0Category = Category(Groups.DATASET, "examples/dataset/DatasetSimple0.kt")

val datasetSimple0Entry: CatalogEntry =
    datasetSimple0Category.chart("dataset-simple0", "最简单的数据集（dataset）", "Simple Example of Dataset") {
            unsafeJso<EChartsOption> {
                legend = emptyLegend()
                tooltip = emptyTooltip()
                dataset =
                    one(
                        rowsDataset(
                            arrayOf(
                                row("product", "2015", "2016", "2017"),
                                row("Matcha Latte", 43.3, 85.8, 93.7),
                                row("Milk Tea", 83.1, 73.4, 55.1),
                                row("Cheese Cocoa", 86.4, 65.2, 82.5),
                                row("Walnut Brownie", 72.4, 53.9, 39.1),
                            ),
                        ),
                    )
                xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.category })
                yAxis = one(unsafeJso<YAxisOption> {})
                // Each bar series is mapped to a column of `dataset.source` by default.
                series = arrayOf<SeriesOption>(barSeries {}, barSeries {}, barSeries {})
            }
        }
