package demo.examples.dataset

import demo.CatalogEntry
import demo.Groups
import echarts.AxisTypes
import echarts.options.DimensionDefinitionLoose
import echarts.options.EChartsOption
import echarts.options.SeriesOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.barSeries
import echarts.options.datasetObjectRows
import echarts.options.one
import js.objects.unsafeJso
import kotlin.js.JsAny
import demo.examples.Category

private val datasetSimple1Category = Category(Groups.DATASET, "examples/dataset/DatasetSimple1.kt")

val datasetSimple1Entry: CatalogEntry =
    datasetSimple1Category.chart("dataset-simple1", "对象数组的输入格式", "Dataset in Object Array") {
            unsafeJso<EChartsOption> {
                legend = emptyLegend()
                tooltip = emptyTooltip()
                dataset =
                    one(
                        datasetOf {
                            dimensions =
                                arrayOf(
                                    DimensionDefinitionLoose("product"),
                                    DimensionDefinitionLoose("2015"),
                                    DimensionDefinitionLoose("2016"),
                                    DimensionDefinitionLoose("2017"),
                                )
                            source =
                                datasetObjectRows(
                                    arrayOf(
                                        objectRow("product" to "Matcha Latte", "2015" to 43.3, "2016" to 85.8, "2017" to 93.7),
                                        objectRow("product" to "Milk Tea", "2015" to 83.1, "2016" to 73.4, "2017" to 55.1),
                                        objectRow("product" to "Cheese Cocoa", "2015" to 86.4, "2016" to 65.2, "2017" to 82.5),
                                        objectRow("product" to "Walnut Brownie", "2015" to 72.4, "2016" to 53.9, "2017" to 39.1),
                                    ),
                                )
                        },
                    )
                xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.category })
                yAxis = one(unsafeJso<YAxisOption> {})
                series = arrayOf<SeriesOption>(barSeries {}, barSeries {}, barSeries {})
            }
        }
