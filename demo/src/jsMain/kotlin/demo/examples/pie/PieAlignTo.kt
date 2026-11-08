package demo.examples.pie

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.PieLabelAlignTos
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.TitleOption
import io.github.shijianjs.ktjs.echarts.options.dataItems
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.shared.pieItem

private val pieAlignToCategory = Category(Groups.PIE, "examples/pie/PieAlignTo.kt")

val pieAlignToEntry: CatalogEntry =
    pieAlignToCategory.chart("pie-alignTo", "饼图标签对齐", "Pie Label Align") {
            val data =
                dataItems(
                    pieItem("Apples", 70.0),
                    pieItem("Strawberries", 68.0),
                    pieItem("Bananas", 48.0),
                    pieItem("Oranges", 40.0),
                    pieItem("Pears", 32.0),
                    pieItem("Pineapples", 27.0),
                    pieItem("Grapes", 18.0),
                )
            unsafeJso<EChartsOption> {
                title =
                    arrayOf(
                        unsafeJso<TitleOption> { text = "Pie label alignTo"; left = StringOrDouble("center") },
                        unsafeJso<TitleOption> {
                            subtext = "alignTo: \"none\" (default)"
                            left = StringOrDouble("16.67%")
                            top = StringOrDouble("75%")
                            textAlign = "center"
                        },
                        unsafeJso<TitleOption> {
                            subtext = "alignTo: \"labelLine\""
                            left = StringOrDouble("50%")
                            top = StringOrDouble("75%")
                            textAlign = "center"
                        },
                        unsafeJso<TitleOption> {
                            subtext = "alignTo: \"edge\""
                            left = StringOrDouble("83.33%")
                            top = StringOrDouble("75%")
                            textAlign = "center"
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        pieAlignToSeries(
                            data,
                            PieLabelAlignTos.none,
                            StringOrDouble(0.0),
                            StringOrDouble("66.6667%"),
                            bleedMargin = 5.0,
                        ),
                        pieAlignToSeries(
                            data,
                            PieLabelAlignTos.labelLine,
                            StringOrDouble("33.3333%"),
                            StringOrDouble("33.3333%"),
                            bleedMargin = 5.0,
                        ),
                        pieAlignToSeries(
                            data,
                            PieLabelAlignTos.edge,
                            StringOrDouble("66.6667%"),
                            StringOrDouble(0.0),
                            margin = 20.0,
                        ),
                    )
            }
        }
