package demo.examples.pie

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.shared.legendNames
import demo.examples.shared.pieItem
import demo.examples.shared.pieItemWithLabel
import demo.examples.shared.richTableLabel
import demo.examples.sunburst.radius

private val pieRichTextCategory = Category(Groups.PIE, "examples/pie/PieRichText.kt")

val pieRichTextEntry: CatalogEntry =
    pieRichTextCategory.chart("pie-rich-text", "富文本标签", "Pie Special Label") {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Weather Statistics"
                            subtext = "Fake Data"
                            left = StringOrDouble("center")
                        },
                    )
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.item
                            formatter = TooltipFormatter("{a} <br/>{b} : {c} ({d}%)")
                        },
                    )
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            bottom = StringOrDouble(10.0)
                            left = StringOrDouble("center")
                            data = legendNames("CityA", "CityB", "CityD", "CityC", "CityE")
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        pieSeries {
                            radius = StringOrDoubleOrArray("65%")
                            center = arrayOf(StringOrDouble("50%"), StringOrDouble("50%"))
                            selectedMode = SelectionModes.single
                            typedData =
                                arrayOf(
                                    pieItemWithLabel("CityE", 1548.0, richTableLabel()),
                                    pieItem("CityC", 735.0),
                                    pieItem("CityD", 510.0),
                                    pieItem("CityB", 434.0),
                                    pieItem("CityA", 335.0),
                                )
                            emphasis =
                                unsafeJso<EmphasisOption> {
                                    itemStyle =
                                        unsafeJso<ItemStyleOption> {
                                            shadowBlur = 10.0
                                            shadowOffsetX = 0.0
                                            shadowColor = ZRColor("rgba(0, 0, 0, 0.5)")
                                        }
                                }
                        },
                    )
            }
        }
