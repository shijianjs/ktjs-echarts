package demo.examples.doc

import demo.CatalogEntry
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import web.cssom.TextAlign
import web.cssom.VerticalAlign
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.chord.column
import demo.examples.dataset.transform
import demo.examples.shared.axisLabel

private val docExampleDataTransformMultipleSortBarCategory = Category(Groups.DOC, "examples/doc/DocExampleDataTransformMultipleSortBar.kt")

val docExampleDataTransformMultipleSortBarEntry: CatalogEntry =
    docExampleDataTransformMultipleSortBarCategory.chart("doc-example/data-transform-multiple-sort-bar", "数据转换：多重排序", "Data transform: multiple sort") {
            unsafeJso<EChartsOption> {
                dataset =
                    arrayOf(
                        unsafeJso<DatasetOption> {
                            dimensions =
                                arrayOf(
                                    DimensionDefinitionLoose("name"),
                                    DimensionDefinitionLoose("age"),
                                    DimensionDefinitionLoose("profession"),
                                    DimensionDefinitionLoose("score"),
                                    DimensionDefinitionLoose("date"),
                                )
                            source = sortBarSource()
                        },
                        unsafeJso<DatasetOption> {
                            transform =
                                arrayOf(
                                    unsafeJso<DataTransformOption> {
                                        type = "sort"
                                        config = sortDescriptors("profession" to "desc", "score" to "desc")
                                    },
                                )
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            axisLabel =
                                unsafeJso<AxisLabelOption> {
                                    interval = AxisInterval(0.0)
                                    rotate = 30.0
                                }
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> {})
                series =
                    one(
                        barSeries {
                            label =
                                unsafeJso<LabelOption> {
                                    show = true
                                    rotate = 90.0
                                    position = LabelPosition("insideBottom")
                                    align = TextAlign.left
                                    verticalAlign = VerticalAlign.middle
                                }
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    // The original colours the bars by the profession column.
                                    color =
                                        js(
                                            "(params) => {" +
                                                " const colors = { Engineer: '#5470c6', Teacher: '#91cc75', Musician: '#fac858' };" +
                                                " return colors[params.data[2]];" +
                                                " }",
                                        )
                                            .unsafeCast<ZRColor>()
                                }
                            encode =
                                unsafeJso<OptionEncode> {
                                    x = OptionEncodeValue(DimensionLoose("name"))
                                    y = OptionEncodeValue(DimensionLoose("score"))
                                    label = OptionEncodeValue(arrayOf(DimensionLoose("profession")))
                                }
                            datasetIndex = 1.0
                        },
                    )
            }
        }
