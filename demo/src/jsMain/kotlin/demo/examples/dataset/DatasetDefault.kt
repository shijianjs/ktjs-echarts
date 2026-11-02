package demo.examples.dataset

import demo.CatalogEntry
import demo.Groups
import echarts.options.EChartsOption
import echarts.options.SeriesOption
import echarts.options.one
import js.objects.unsafeJso
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.chord.column
import demo.examples.shared.row

private val datasetDefaultCategory = Category(Groups.DATASET, "examples/dataset/DatasetDefault.kt")

val datasetDefaultEntry: CatalogEntry =
    datasetDefaultCategory.chart("dataset-default", "默认 encode 设置", "Default arrangement") {
            unsafeJso<EChartsOption> {
                legend = emptyLegend()
                tooltip = emptyTooltip()
                dataset =
                    one(
                        rowsDataset(
                            arrayOf(
                                row("product", "2012", "2013", "2014", "2015", "2016", "2017"),
                                row("Milk Tea", 86.5, 92.1, 85.7, 83.1, 73.4, 55.1),
                                row("Matcha Latte", 41.1, 30.4, 65.1, 53.3, 83.8, 98.7),
                                row("Cheese Cocoa", 24.1, 67.2, 79.5, 86.4, 65.2, 82.5),
                                row("Walnut Brownie", 55.2, 67.1, 69.2, 72.4, 53.9, 39.1),
                            ),
                        ),
                    )
                series =
                    arrayOf<SeriesOption>(
                        // No encode: the first column after `product` is used.
                        yearPie(null, "25%", "30%"),
                        yearPie("2013", "75%", "30%"),
                        yearPie("2014", "25%", "75%"),
                        yearPie("2015", "75%", "75%"),
                    )
            }
        }
