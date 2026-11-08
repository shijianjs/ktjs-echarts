package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.AxisTypes
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.LegendOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.XAxisOption
import io.github.shijianjs.ktjs.echarts.options.YAxisOption
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.strings
import js.objects.unsafeJso
import kotlin.js.js
import demo.examples.Category

private val barYCategoryStackCategory = Category(Groups.BAR, "examples/bar/BarYCategoryStack.kt")

val barYCategoryStackEntry: CatalogEntry =
    barYCategoryStackCategory.chart("bar-y-category-stack", "堆叠条形图", "Stacked Horizontal Bar") {
            unsafeJso<EChartsOption> {
                tooltip = axisTooltip
                legend = one(unsafeJso<LegendOption> {})
                xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.value })
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.category; data = strings(*week) })
                series =
                    arrayOf<SeriesOption>(
                        stackedBar("Direct", 320.0, 302.0, 301.0, 334.0, 390.0, 330.0, 320.0),
                        stackedBar("Mail Ad", 120.0, 132.0, 101.0, 134.0, 90.0, 230.0, 210.0),
                        stackedBar("Affiliate Ad", 220.0, 182.0, 191.0, 234.0, 290.0, 330.0, 310.0),
                        stackedBar("Video Ad", 150.0, 212.0, 201.0, 154.0, 190.0, 330.0, 410.0),
                        stackedBar("Search Engine", 820.0, 832.0, 901.0, 934.0, 1290.0, 1330.0, 1320.0),
                    )
            }
        }
