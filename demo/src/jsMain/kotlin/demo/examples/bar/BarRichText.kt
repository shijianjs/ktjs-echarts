package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import web.cssom.TextAlign
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.shared.axisLabel
import demo.examples.shared.iconRow
import demo.examples.shared.legendNames
import demo.examples.shared.richOf
import demo.examples.shared.toolbox
import demo.examples.themeriver.days

private val barRichTextCategory = Category(Groups.BAR, "examples/bar/BarRichText.kt")

val barRichTextEntry: CatalogEntry =
    barRichTextCategory.chart("bar-rich-text", "天气统计（富文本）", "Weather Statistics") {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Weather Statistics" })
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.shadow }
                        },
                    )
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            data = legendNames("City Alpha", "City Beta", "City Gamma")
                        },
                    )
                grid = one(unsafeJso<GridOption> { left = StringOrDouble(100.0) })
                toolbox =
                    one(
                        unsafeJso<ToolboxOption> {
                            show = true
                            feature = unsafeJso<ToolboxFeature> { saveAsImage = unsafeJso<SaveAsImageOption> {} }
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.value
                            name = "Days"
                            axisLabel = unsafeJso<AxisLabelOption> { formatter = LabelFormatter("{value}") }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.category
                            inverse = true
                            data = strings("Sunny", "Cloudy", "Showers")
                            axisLabel =
                                unsafeJso<AxisLabelOption> {
                                    formatter =
                                        LabelFormatter(
                                            js("(value) => '{' + value + '| }' + String.fromCharCode(10) + '{value|' + value + '}'")
                                                as LabelFormatterCallback,
                                        )
                                    margin = 20.0
                                    rich =
                                        richOf(
                                            "value" to unsafeJso<TextStyleOption> {
                                                lineHeight = 30.0
                                                align = TextAlign.center
                                            },
                                            "Sunny" to iconRow("Sunny"),
                                            "Cloudy" to iconRow("Cloudy"),
                                            "Showers" to iconRow("Showers"),
                                        )
                                }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            name = "City Alpha"
                            typedData = numbers(165.0, 170.0, 30.0)
                            label = unsafeJso<LabelOption> { show = true }
                            markPoint =
                                unsafeJso<MarkPointOption> {
                                    symbolSize = SymbolSize(1.0)
                                    symbolOffset = SymbolOffset(arrayOf<StringOrDouble>(StringOrDouble(0.0), StringOrDouble("50%")))
                                    label =
                                        unsafeJso<LabelOption> {
                                            formatter = LabelFormatter("{a|{a}\n}{b|{b} }{c|{c}}")
                                            backgroundColor = ZRColor("rgb(242,242,242)")
                                            borderColor = "#aaa"
                                            borderWidth = 1.0
                                            borderRadius = DoubleOrDoubleArray(4.0)
                                            padding = DoubleOrDoubleArray(arrayOf(4.0, 10.0))
                                            lineHeight = 26.0
                                            position = LabelPosition("right")
                                            distance = 20.0
                                            rich =
                                                richOf(
                                                    "a" to unsafeJso<TextStyleOption> {
                                                        align = TextAlign.center
                                                        color = "#fff"
                                                        fontSize = StringOrDouble(18.0)
                                                        textShadowBlur = 2.0
                                                        textShadowColor = "#000"
                                                        textShadowOffsetX = 0.0
                                                        textShadowOffsetY = 1.0
                                                        textBorderColor = "#333"
                                                        textBorderWidth = 2.0
                                                    },
                                                    "b" to unsafeJso<TextStyleOption> { color = "#333" },
                                                    "c" to unsafeJso<TextStyleOption> {
                                                        color = "#ff8811"
                                                        textBorderColor = "#000"
                                                        textBorderWidth = 1.0
                                                        fontSize = StringOrDouble(22.0)
                                                    },
                                                )
                                        }
                                    data =
                                        arrayOf(
                                            unsafeJso<MarkPointDataItemOption> {
                                                type = MarkerStatisticTypes.max
                                                name = "max days: "
                                            },
                                            unsafeJso<MarkPointDataItemOption> {
                                                type = MarkerStatisticTypes.min
                                                name = "min days: "
                                            },
                                        )
                                }
                        },
                        barSeries {
                            name = "City Beta"
                            label = unsafeJso<LabelOption> { show = true }
                            typedData = numbers(150.0, 105.0, 110.0)
                        },
                        barSeries {
                            name = "City Gamma"
                            label = unsafeJso<LabelOption> { show = true }
                            typedData = numbers(220.0, 82.0, 63.0)
                        },
                    )
            }
        }
