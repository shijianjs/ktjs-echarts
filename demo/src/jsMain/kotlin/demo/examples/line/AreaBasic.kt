package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.AreaStyleOption
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.lineSeries
import io.github.shijianjs.ktjs.echarts.options.numbers
import js.objects.unsafeJso
import demo.examples.Category

private val areaBasicCategory = Category(Groups.LINE, "examples/line/AreaBasic.kt")

val areaBasicEntry: CatalogEntry =
    areaBasicCategory.chart("area-basic", "基础面积图", "Basic area chart") {
            unsafeJso<EChartsOption> {
                xAxis = categoryAxis(*week, boundaryGap = false)
                yAxis = valueAxis
                series =
                    arrayOf<SeriesOption>(
                        lineSeries {
                            areaStyle = unsafeJso<AreaStyleOption> {}
                            typedData = numbers(820.0, 932.0, 901.0, 934.0, 1290.0, 1330.0, 1320.0)
                        },
                    )
            }
        }
