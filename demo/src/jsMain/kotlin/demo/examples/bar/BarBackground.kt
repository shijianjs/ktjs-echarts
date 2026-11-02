package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import echarts.options.EChartsOption
import echarts.options.ItemStyleOption
import echarts.options.SeriesOption
import echarts.options.barSeries
import echarts.options.numbers
import js.objects.unsafeJso
import kotlin.js.js
import zrender.ZRColor
import demo.examples.Category

private val barBackgroundCategory = Category(Groups.BAR, "examples/bar/BarBackground.kt")

val barBackgroundEntry: CatalogEntry =
    barBackgroundCategory.chart("bar-background", "带背景色的柱状图", "Bar with Background") {
            unsafeJso<EChartsOption> {
                xAxis = categoryAxis(*week)
                yAxis = valueAxis
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            showBackground = true
                            backgroundStyle = unsafeJso<ItemStyleOption> { color = ZRColor("rgba(180, 180, 180, 0.2)") }
                            typedData = numbers(120.0, 200.0, 150.0, 80.0, 70.0, 110.0, 130.0)
                        },
                    )
            }
        }
