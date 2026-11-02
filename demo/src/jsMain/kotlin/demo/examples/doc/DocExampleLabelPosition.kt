package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import web.cssom.TextAlign
import web.cssom.VerticalAlign
import zrender.ZRColor
import demo.examples.Category
import demo.examples.gauge.center

private val docExampleLabelPositionCategory = Category(Groups.DOC, "examples/doc/DocExampleLabelPosition.kt")

val docExampleLabelPositionEntry: CatalogEntry =
    docExampleLabelPositionCategory.chart("doc-example/label-position", "标签位置", "Label position") {
            unsafeJso<EChartsOption> {
                xAxis = one(unsafeJso<XAxisOption> { max = AxisMinMax(2.0) })
                yAxis = one(unsafeJso<YAxisOption> { max = AxisMinMax(2.0) })
                series =
                    arrayOf<SeriesOption>(
                        scatterSeries {
                            symbolSize = SymbolSize(360.0)
                            symbol = SymbolShape("roundRect")
                            typedData =
                                dataItems(
                                    ScatterDataItem(
                                        arrayOf<OptionDataValue?>(
                                            OptionDataValue(1.0),
                                            OptionDataValue(1.0),
                                        ),
                                    ),
                                )
                            // The official page drives these four values from a configuration
                            // panel; the demo has no panel and shows its defaults.
                            label =
                                unsafeJso<LabelOption> {
                                    position = LabelPosition("top")
                                    align = TextAlign.center
                                    verticalAlign = VerticalAlign.middle
                                    distance = 5.0
                                    show = true
                                    formatter = LabelFormatter("Label Text")
                                    width = StringOrDouble(200.0)
                                    height = 60.0
                                    backgroundColor = ZRColor("rgba(0, 255, 255, 0.3)")
                                    borderColor = "#555"
                                    borderWidth = 2.0
                                    borderRadius = DoubleOrDoubleArray(5.0)
                                    padding = DoubleOrDoubleArray(10.0)
                                    fontSize = StringOrDouble(18.0)
                                    shadowBlur = 3.0
                                    shadowColor = ZRColor("#888")
                                    shadowOffsetX = 0.0
                                    shadowOffsetY = 3.0
                                    textBorderColor = "#000"
                                    textBorderWidth = 3.0
                                    color = "#fff"
                                }
                        },
                    )
            }
        }
