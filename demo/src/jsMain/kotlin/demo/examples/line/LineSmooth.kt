package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import echarts.options.BooleanOrDouble
import echarts.options.EChartsOption
import echarts.options.SeriesOption
import echarts.options.lineSeries
import echarts.options.numbers
import js.objects.unsafeJso
import demo.examples.Category

private val lineSmoothCategory = Category(Groups.LINE, "examples/line/LineSmooth.kt")

val lineSmoothEntry: CatalogEntry =
    lineSmoothCategory.chart("line-smooth", "基础平滑折线图", "Smoothed Line Chart") {
            unsafeJso<EChartsOption> {
                xAxis = categoryAxis(*week)
                yAxis = valueAxis
                series =
                    arrayOf<SeriesOption>(
                        lineSeries {
                            smooth = BooleanOrDouble(true)
                            typedData = numbers(820.0, 932.0, 901.0, 934.0, 1290.0, 1330.0, 1320.0)
                        },
                    )
            }
        }
