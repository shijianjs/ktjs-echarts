package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.BooleanOrDouble
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.lineSeries
import io.github.shijianjs.ktjs.echarts.options.numbers
import js.objects.unsafeJso
import demo.examples.Category

internal class LineSmooth {
    companion object {
        private val CATEGORY = Category(Groups.LINE, "examples/line/LineSmooth.kt")

        val ENTRY: CatalogEntry =
            CATEGORY.chart("line-smooth", "基础平滑折线图", "Smoothed Line Chart") {
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
    }
}
