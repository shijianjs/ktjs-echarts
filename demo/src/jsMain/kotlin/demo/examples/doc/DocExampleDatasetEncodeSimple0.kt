package demo.examples.doc

import demo.CatalogEntry
import echarts.*
import echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import demo.examples.Category
import demo.examples.shared.row

private val docExampleDatasetEncodeSimple0Category = Category(Groups.DOC, "examples/doc/DocExampleDatasetEncodeSimple0.kt")

val docExampleDatasetEncodeSimple0Entry: CatalogEntry =
    docExampleDatasetEncodeSimple0Category.chart("doc-example/dataset-encode-simple0", "数据集编码（简单）", "Dataset encode (simple)") {
            unsafeJso<EChartsOption> {
                dataset =
                    one(
                        unsafeJso<DatasetOption> {
                            source =
                                datasetArrayRows(
                                    arrayOf(
                                        row("score", "amount", "product"),
                                        row(89.3, 58212.0, "Matcha Latte"),
                                        row(57.1, 78254.0, "Milk Tea"),
                                        row(74.4, 41032.0, "Cheese Cocoa"),
                                        row(50.1, 12755.0, "Cheese Brownie"),
                                        row(89.7, 20145.0, "Matcha Cocoa"),
                                        row(68.1, 79146.0, "Tea"),
                                        row(19.6, 91852.0, "Orange Juice"),
                                        row(10.6, 101852.0, "Lemon Juice"),
                                        row(32.7, 20112.0, "Walnut Brownie"),
                                    ).unsafeCast<Array<Array<OptionDataValue>>>(),
                                )
                        },
                    )
                grid = one(unsafeJso<GridOption> { containLabel = true })
                xAxis = one(unsafeJso<XAxisOption> {})
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.category })
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            // The dataset columns drive both axes; nothing is encoded on the data.
                            encode =
                                unsafeJso<OptionEncode> {
                                    this.x = OptionEncodeValue(DimensionLoose("amount"))
                                    this.y = OptionEncodeValue(DimensionLoose("product"))
                                }
                        },
                    )
            }
        }
