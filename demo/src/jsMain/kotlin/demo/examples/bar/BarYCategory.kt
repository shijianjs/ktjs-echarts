package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import echarts.AxisTypes
import echarts.options.BooleanOrStringArray
import echarts.options.EChartsOption
import echarts.options.LegendOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.TitleOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.barSeries
import echarts.options.numbers
import echarts.options.one
import echarts.options.strings
import js.objects.unsafeJso
import kotlin.js.js
import demo.examples.Category

private val barYCategoryCategory = Category(Groups.BAR, "examples/bar/BarYCategory.kt")

val barYCategoryEntry: CatalogEntry =
    barYCategoryCategory.chart("bar-y-category", "世界人口总量 - 条形图", "World Population") {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "World Population" })
                tooltip = axisTooltip
                legend = one(unsafeJso<LegendOption> {})
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.value
                            boundaryGap = BooleanOrStringArray(arrayOf(StringOrDouble(0.0), StringOrDouble(0.01)))
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.category
                            data = strings("Brazil", "Indonesia", "USA", "India", "China", "World")
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            name = "2011"
                            typedData = numbers(18203.0, 23489.0, 29034.0, 104970.0, 131744.0, 630230.0)
                        },
                        barSeries {
                            name = "2012"
                            typedData = numbers(19325.0, 23438.0, 31000.0, 121594.0, 134141.0, 681807.0)
                        },
                    )
            }
        }
