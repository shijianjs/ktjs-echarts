package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import web.cssom.TextAlign
import web.cssom.VerticalAlign
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.shared.recordOf

private val docExampleTextFregmentAlignCategory = Category(Groups.DOC, "examples/doc/DocExampleTextFregmentAlign.kt")

val docExampleTextFregmentAlignEntry: CatalogEntry =
    docExampleTextFregmentAlignCategory.chart(
            "doc-example/text-fregment-align",
            "文本片段的对齐",
            "Text fragment alignment",
        ) {
            unsafeJso<EChartsOption> {
                xAxis = hiddenXAxis()
                yAxis = hiddenYAxis()
                series =
                    arrayOf<SeriesOption>(
                        scatterSeries {
                            symbolSize = SymbolSize(1.0)
                            typedData =
                                dataItems(
                                    ScatterDataItem(
                                        arrayOf<OptionDataValue?>(
                                            OptionDataValue(0.0),
                                            OptionDataValue(0.0),
                                        ),
                                    ),
                                )
                            label =
                                unsafeJso<LabelOption> {
                                    show = true
                                    formatter =
                                        LabelFormatter(
                                            listOf(
                                                "{tc|height is specified as 30}{box|}",
                                                "{lh|Biggest lineHeight} {vt|verticalAlign \"top\"} " +
                                                    "{vb|verticalAlign \"bottom\"} {vm|verticalAlign \"middle\"}",
                                                "{l1|align \"left\"}{l2|align \"left\"}{c1|align \"center\"}" +
                                                    "{c2|align \"center\"}{r2|align \"right\"}{r1|align \"right\"}",
                                                "{tc|height is specified as 30}{box|}",
                                            ).joinToString("\n"),
                                        )
                                    backgroundColor = ZRColor("#ddd")
                                    borderColor = "#555"
                                    borderWidth = 1.0
                                    borderRadius = DoubleOrDoubleArray(5.0)
                                    padding = DoubleOrDoubleArray(arrayOf(0.0, 20.0, 0.0, 20.0))
                                    color = "#000"
                                    fontSize = StringOrDouble(14.0)
                                    rich =
                                        recordOf(
                                            "box" to
                                                unsafeJso<TextStyleOption> {
                                                    backgroundColor = ZRColor("#000")
                                                    height = 30.0
                                                    padding =
                                                        DoubleOrDoubleArray(arrayOf(0.0, 20.0, 0.0, 20.0))
                                                    align = TextAlign.right
                                                    width = StringOrDouble("100%")
                                                    color = "#eee"
                                                },
                                            "tc" to
                                                unsafeJso<TextStyleOption> {
                                                    align = TextAlign.center
                                                    color = "#eee"
                                                },
                                            "lh" to
                                                unsafeJso<TextStyleOption> {
                                                    fontSize = StringOrDouble(30.0)
                                                    color = "rgb(199,86,83)"
                                                    borderWidth = 1.0
                                                    borderColor = "rgb(199,86,83)"
                                                },
                                            "vt" to verticalTag(VerticalAlign.top),
                                            "vb" to verticalTag(VerticalAlign.bottom),
                                            "vm" to verticalTag(VerticalAlign.middle),
                                            "l1" to alignTag(null, rounded = false),
                                            "l2" to alignTag(null, rounded = true),
                                            "r1" to alignTag(TextAlign.right, rounded = false),
                                            "r2" to alignTag(TextAlign.right, rounded = true),
                                            "c1" to alignTag(TextAlign.center, rounded = false, width = 100.0),
                                            "c2" to alignTag(TextAlign.center, rounded = true, width = 100.0),
                                        ).unsafeCast<Record<String, TextStyleOption>>()
                                }
                        },
                    )
            }
        }
