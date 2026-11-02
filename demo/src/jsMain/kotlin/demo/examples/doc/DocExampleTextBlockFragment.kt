package demo.examples.doc

import demo.CatalogEntry
import echarts.*
import echarts.options.*
import demo.Groups
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import zrender.ZRColor
import demo.examples.Category
import demo.examples.shared.recordOf

private val docExampleTextBlockFragmentCategory = Category(Groups.DOC, "examples/doc/DocExampleTextBlockFragment.kt")

val docExampleTextBlockFragmentEntry: CatalogEntry =
    docExampleTextBlockFragmentCategory.chart("doc-example/text-block-fragment", "文本块与文本片段", "Text Block and Fragment") {
            unsafeJso<EChartsOption> {
                xAxis = hiddenXAxis()
                yAxis = hiddenYAxis()
                series =
                    arrayOf<SeriesOption>(
                        scatterSeries {
                            symbolSize = SymbolSize(1.0)
                            typedData =
                                dataItems(
                                    ScatterDataItem(arrayOf<OptionDataValue?>(OptionDataValue(0.0), OptionDataValue(0.0))),
                                )
                            label =
                                unsafeJso<LabelOption> {
                                    show = true
                                    formatter =
                                        LabelFormatter(
                                            "The whole box is a {term|Text Block}, with\n" +
                                                "red border and grey background.\n" +
                                                "{fragment1|A Text Fragment} {fragment2|Another Text Fragment}\n" +
                                                "Text fragments can be customized.",
                                        )
                                    backgroundColor = ZRColor("#eee")
                                    borderColor = "rgb(199,86,83)"
                                    borderWidth = 2.0
                                    borderRadius = DoubleOrDoubleArray(5.0)
                                    padding = DoubleOrDoubleArray(10.0)
                                    color = "#000"
                                    fontSize = StringOrDouble(14.0)
                                    shadowBlur = 3.0
                                    shadowColor = ZRColor("#888")
                                    shadowOffsetX = 0.0
                                    shadowOffsetY = 3.0
                                    lineHeight = 30.0
                                    rich =
                                        recordOf(
                                            "term" to
                                                unsafeJso<TextStyleOption> {
                                                    fontSize = StringOrDouble(18.0)
                                                    color = "rgb(199,86,83)"
                                                },
                                            "fragment1" to
                                                unsafeJso<TextStyleOption> {
                                                    backgroundColor = ZRColor("#000")
                                                    color = "yellow"
                                                    padding = DoubleOrDoubleArray(5.0)
                                                },
                                            "fragment2" to
                                                unsafeJso<TextStyleOption> {
                                                    backgroundColor = ZRColor("#339911")
                                                    color = "#fff"
                                                    borderRadius = DoubleOrDoubleArray(15.0)
                                                    padding = DoubleOrDoubleArray(5.0)
                                                },
                                        ).unsafeCast<Record<String, TextStyleOption>>()
                                }
                        },
                    )
            }
        }
