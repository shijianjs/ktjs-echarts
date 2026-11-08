package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.AxisTypes
import io.github.shijianjs.ktjs.echarts.options.BooleanOrStringArray
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.LegendOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.TitleOption
import io.github.shijianjs.ktjs.echarts.options.XAxisOption
import io.github.shijianjs.ktjs.echarts.options.YAxisOption
import io.github.shijianjs.ktjs.echarts.options.barSeries
import io.github.shijianjs.ktjs.echarts.options.numbers
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.strings
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
