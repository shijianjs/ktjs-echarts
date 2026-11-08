package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.barSeries
import io.github.shijianjs.ktjs.echarts.options.numbers
import js.objects.unsafeJso
import kotlin.js.js
import demo.examples.Category

private val barSimpleCategory = Category(Groups.BAR, "examples/bar/BarSimple.kt")

val barSimpleEntry: CatalogEntry =
    barSimpleCategory.chart("bar-simple", "基础柱状图", "Basic Bar") {
            unsafeJso<EChartsOption> {
                xAxis = categoryAxis(*week)
                yAxis = valueAxis
                series = arrayOf<SeriesOption>(barSeries { typedData = numbers(120.0, 200.0, 150.0, 80.0, 70.0, 110.0, 130.0) })
            }
        }
