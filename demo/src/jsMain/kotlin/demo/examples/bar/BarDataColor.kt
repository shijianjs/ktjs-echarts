package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.BarDataItem
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.OptionDataValue
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.barSeries
import io.github.shijianjs.ktjs.echarts.options.dataItems
import js.objects.unsafeJso
import kotlin.js.js
import demo.examples.Category

private val barDataColorCategory = Category(Groups.BAR, "examples/bar/BarDataColor.kt")

val barDataColorEntry: CatalogEntry =
    barDataColorCategory.chart("bar-data-color", "自定义单个柱子颜色", "Set Style of Single Bar.") {
            unsafeJso<EChartsOption> {
                xAxis = categoryAxis(*week)
                yAxis = valueAxis
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            typedData =
                                dataItems(
                                    BarDataItem(OptionDataValue(120.0)),
                                    colouredBar(200.0, "#505372"),
                                    BarDataItem(OptionDataValue(150.0)),
                                    BarDataItem(OptionDataValue(80.0)),
                                    BarDataItem(OptionDataValue(70.0)),
                                    BarDataItem(OptionDataValue(110.0)),
                                    BarDataItem(OptionDataValue(130.0)),
                                )
                        },
                    )
            }
        }
