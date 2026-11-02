package demo.examples.doc

import demo.CatalogEntry
import echarts.*
import echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import zrender.ZRColor
import demo.examples.Category
import demo.examples.shared.axisLabel

private val docExampleTitleBlockCategory = Category(Groups.DOC, "examples/doc/DocExampleTitleBlock.kt")

val docExampleTitleBlockEntry: CatalogEntry =
    docExampleTitleBlockCategory.chart("doc-example/title-block", "富文本标题块", "Title Block") {
            unsafeJso<EChartsOption> {
                xAxis = hiddenXAxis()
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            axisLabel = unsafeJso<AxisLabelOption> { show = false }
                            axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                            axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                            min = AxisMinMax(0.0)
                            max = AxisMinMax(2.0)
                            inverse = true
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        scatterSeries {
                            symbolSize = SymbolSize(1.0)
                            typedData =
                                dataItems(
                                    titleBlockItem(
                                        0.0,
                                        "{tc|Center Title}{titleBg|}\n" +
                                            "  Content text xxxxxxxx {sunny|} xxxxxxxx {cloudy|}  \n" +
                                            "{hr|}\n" +
                                            "  xxxxx {showers|} xxxxxxxx  xxxxxxxxx  ",
                                        alignTitle = true,
                                    ),
                                    titleBlockItem(
                                        1.0,
                                        "{titleBg|Left Title}\n" +
                                            "  Content text xxxxxxxx {sunny|} xxxxxxxx {cloudy|}  \n" +
                                            "{hr|}\n" +
                                            "  xxxxx {showers|} xxxxxxxx  xxxxxxxxx  ",
                                        alignTitle = false,
                                    ),
                                    titleBlockItem(
                                        2.0,
                                        "{titleBg|Right Title}\n" +
                                            "  Content text xxxxxxxx {sunny|} xxxxxxxx {cloudy|}  \n" +
                                            "{hr|}\n" +
                                            "  xxxxx {showers|} xxxxxxxx  xxxxxxxxx  ",
                                        alignTitle = true,
                                    ),
                                )
                            label =
                                unsafeJso<LabelOption> {
                                    show = true
                                    backgroundColor = ZRColor("#ddd")
                                    borderColor = "#555"
                                    borderWidth = 1.0
                                    borderRadius = DoubleOrDoubleArray(5.0)
                                    color = "#000"
                                    fontSize = StringOrDouble(14.0)
                                    rich = titleBlockRich()
                                }
                        },
                    )
            }
        }
