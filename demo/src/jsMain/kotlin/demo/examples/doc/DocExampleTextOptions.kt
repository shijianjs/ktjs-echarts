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
import demo.examples.scatter.point
import demo.examples.shared.recordOf

private val docExampleTextOptionsCategory = Category(Groups.DOC, "examples/doc/DocExampleTextOptions.kt")

val docExampleTextOptionsEntry: CatalogEntry =
    docExampleTextOptionsCategory.chart("doc-example/text-options", "文本样式选项", "Text Options") {
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
                            // The original puts the label on the single data item; a series-level
                            // label renders the same text for one point.
                            label =
                                unsafeJso<LabelOption> {
                                    show = true
                                    formatter =
                                        LabelFormatter(
                                            "Plain text\n" +
                                                "{textBorder|textBorderColor + textBorderWidth}\n" +
                                                "{textShadow|textShadowColor + textShadowBlur + textShadowOffsetX + textShadowOffsetY}\n" +
                                                "{bg|backgroundColor + borderRadius + padding}\n" +
                                                "{border|borderColor + borderWidth + borderRadius + padding}\n" +
                                                "{shadow|shadowColor + shadowBlur + shadowOffsetX + shadowOffsetY}",
                                        )
                                    backgroundColor = ZRColor("#eee")
                                    borderColor = "#333"
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
                                            "textBorder" to
                                                unsafeJso<TextStyleOption> {
                                                    fontSize = StringOrDouble(20.0)
                                                    textBorderColor = "#000"
                                                    textBorderWidth = 3.0
                                                    color = "#fff"
                                                },
                                            "textShadow" to
                                                unsafeJso<TextStyleOption> {
                                                    fontSize = StringOrDouble(16.0)
                                                    textShadowBlur = 5.0
                                                    textShadowColor = "#000"
                                                    textShadowOffsetX = 3.0
                                                    textShadowOffsetY = 3.0
                                                    color = "#fff"
                                                },
                                            "bg" to
                                                unsafeJso<TextStyleOption> {
                                                    backgroundColor = ZRColor("#339911")
                                                    color = "#fff"
                                                    borderRadius = DoubleOrDoubleArray(15.0)
                                                    padding = DoubleOrDoubleArray(5.0)
                                                },
                                            "border" to
                                                unsafeJso<TextStyleOption> {
                                                    color = "#000"
                                                    borderColor = "#449911"
                                                    borderWidth = 1.0
                                                    borderRadius = DoubleOrDoubleArray(3.0)
                                                    padding = DoubleOrDoubleArray(5.0)
                                                },
                                            "shadow" to
                                                unsafeJso<TextStyleOption> {
                                                    backgroundColor = ZRColor("#992233")
                                                    padding = DoubleOrDoubleArray(5.0)
                                                    color = "#fff"
                                                    shadowBlur = 5.0
                                                    shadowColor = ZRColor("#336699")
                                                    shadowOffsetX = 6.0
                                                    shadowOffsetY = 6.0
                                                },
                                        ).unsafeCast<Record<String, TextStyleOption>>()
                                }
                        },
                    )
            }
        }
