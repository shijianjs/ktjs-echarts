package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.lineSeries
import io.github.shijianjs.ktjs.echarts.options.numbers
import js.objects.unsafeJso
import demo.examples.Category

private val lineSimpleCategory = Category(Groups.LINE, "examples/line/LineSimple.kt")

val lineSimpleEntry: CatalogEntry =
    lineSimpleCategory.chart("line-simple", "基础折线图", "Basic Line Chart") {
            unsafeJso<EChartsOption> {
                xAxis = categoryAxis(*week)
                yAxis = valueAxis
                series = arrayOf<SeriesOption>(lineSeries { typedData = numbers(150.0, 230.0, 224.0, 218.0, 135.0, 147.0, 260.0) })
            }
        }
