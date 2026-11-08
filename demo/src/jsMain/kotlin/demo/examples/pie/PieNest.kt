package demo.examples.pie

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.DoubleOrDoubleArray
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.LabelFormatter
import io.github.shijianjs.ktjs.echarts.options.LabelLineOption
import io.github.shijianjs.ktjs.echarts.options.LabelOption
import io.github.shijianjs.ktjs.echarts.options.LabelPosition
import io.github.shijianjs.ktjs.echarts.options.PieLabelOption
import io.github.shijianjs.ktjs.echarts.options.SelectionModes
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.StringOrDoubleOrArray
import io.github.shijianjs.ktjs.echarts.options.TextStyleOption
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.pieSeries
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import web.cssom.TextAlign
import io.github.shijianjs.ktjs.zrender.ZRColor
import io.github.shijianjs.ktjs.zrender.ZRFontWeights
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.shared.recordOf
import demo.examples.sunburst.radius

private val pieNestCategory = Category(Groups.PIE, "examples/pie/PieNest.kt")

val pieNestEntry: CatalogEntry =
    pieNestCategory.chart("pie-nest", "嵌套环形图", "Nested Pies") {
            unsafeJso<EChartsOption> {
                tooltip = one(tooltipWithPercent())
                legend =
                    legendOf(
                        "Direct",
                        "Marketing",
                        "Search Engine",
                        "Email",
                        "Union Ads",
                        "Video Ads",
                        "Baidu",
                        "Google",
                        "Bing",
                        "Others",
                    )
                series =
                    arrayOf<SeriesOption>(
                        pieSeries {
                            name = "Access From"
                            selectedMode = SelectionModes.single
                            radius = StringOrDoubleOrArray(arrayOf(StringOrDouble(0.0), StringOrDouble("30%")))
                            label =
                                unsafeJso<LabelOption> {
                                    position = LabelPosition("inner")
                                    fontSize = StringOrDouble(14.0)
                                }
                            labelLine = unsafeJso<LabelLineOption> { show = false }
                            typedData = nestInner()
                        },
                        pieSeries {
                            name = "Access From"
                            radius = StringOrDoubleOrArray(arrayOf(StringOrDouble("45%"), StringOrDouble("60%")))
                            labelLine = unsafeJso<LabelLineOption> { length = 30.0 }
                            typedLabel =
                                unsafeJso<PieLabelOption> {
                                    // The `{tag|...}` sections only render when the tags are declared
                                    // in `rich`; without them ECharts prints the markup as text.
                                    formatter = LabelFormatter("{a|{a}}{abg|}\n{hr|}\n  {b|{b}：}{c}  {per|{d}%}  ")
                                    backgroundColor = ZRColor("#F6F8FC")
                                    borderColor = "#8C8D8E"
                                    borderWidth = 1.0
                                    borderRadius = DoubleOrDoubleArray(4.0)
                                    rich =
                                        recordOf(
                                            "a" to
                                                unsafeJso<TextStyleOption> {
                                                    color = "#6E7079"
                                                    lineHeight = 22.0
                                                    align = TextAlign.center
                                                },
                                            "hr" to
                                                unsafeJso<TextStyleOption> {
                                                    borderColor = "#8C8D8E"
                                                    width = StringOrDouble("100%")
                                                    borderWidth = 1.0
                                                    height = 0.0
                                                },
                                            "b" to
                                                unsafeJso<TextStyleOption> {
                                                    color = "#4C5058"
                                                    fontSize = StringOrDouble(14.0)
                                                    fontWeight = ZRFontWeights.bold
                                                    lineHeight = 33.0
                                                },
                                            "per" to
                                                unsafeJso<TextStyleOption> {
                                                    color = "#fff"
                                                    backgroundColor = ZRColor("#4C5058")
                                                    padding = DoubleOrDoubleArray(arrayOf(3.0, 4.0))
                                                    borderRadius = DoubleOrDoubleArray(4.0)
                                                },
                                        ).unsafeCast<Record<String, TextStyleOption>>()
                                }
                            typedData = nestOuter()
                        },
                    )
            }
        }
