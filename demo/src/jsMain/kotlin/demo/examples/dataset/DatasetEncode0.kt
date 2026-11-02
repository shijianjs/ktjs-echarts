package demo.examples.dataset

import demo.CatalogEntry
import demo.Groups
import echarts.AxisTypes
import echarts.Orients
import echarts.options.EChartsOption
import echarts.options.GridOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.StringOrStringArray
import echarts.options.VisualMapOption
import echarts.options.VisualOptionUnit
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.barSeries
import echarts.options.one
import js.objects.unsafeJso
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.chord.column
import demo.examples.gauge.center
import demo.examples.matrix.dimension
import demo.examples.shared.row

private val datasetEncode0Category = Category(Groups.DATASET, "examples/dataset/DatasetEncode0.kt")

val datasetEncode0Entry: CatalogEntry =
    datasetEncode0Category.chart("dataset-encode0", "指定数据到坐标轴的映射", "Simple Encode") {
            unsafeJso<EChartsOption> {
                dataset =
                    one(
                        rowsDataset(
                            arrayOf(
                                row("score", "amount", "product"),
                                row(89.3, 58212, "Matcha Latte"),
                                row(57.1, 78254, "Milk Tea"),
                                row(74.4, 41032, "Cheese Cocoa"),
                                row(50.1, 12755, "Cheese Brownie"),
                                row(89.7, 20145, "Matcha Cocoa"),
                                row(68.1, 79146, "Tea"),
                                row(19.6, 91852, "Orange Juice"),
                                row(10.6, 101852, "Lemon Juice"),
                                row(32.7, 20112, "Walnut Brownie"),
                            ),
                        ),
                    )
                grid = one(unsafeJso<GridOption> { containLabel = true })
                xAxis = one(unsafeJso<XAxisOption> { name = "amount" })
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.category })
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            orient = Orients.horizontal
                            left = StringOrDouble("center")
                            min = 10.0
                            max = 100.0
                            text = arrayOf("High Score", "Low Score")
                            // The score column drives the color.
                            dimension = 0.0
                            inRange =
                                unsafeJso<VisualOptionUnit> {
                                    color = StringOrStringArray(arrayOf("#65B581", "#FFCE34", "#FD665F"))
                                }
                        },
                    )
                series = arrayOf<SeriesOption>(barSeries { encode = xyEncode("amount", "product") })
            }
        }
