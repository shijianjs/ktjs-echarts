package demo.examples.scatter

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.SymbolSize
import io.github.shijianjs.ktjs.echarts.options.XAxisOption
import io.github.shijianjs.ktjs.echarts.options.YAxisOption
import io.github.shijianjs.ktjs.echarts.options.dataItems
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.scatterSeries
import js.objects.unsafeJso
import kotlin.js.js
import demo.examples.Category

private val scatterSimpleCategory = Category(Groups.SCATTER, "examples/scatter/ScatterSimple.kt")

val scatterSimpleEntry: CatalogEntry =
    scatterSimpleCategory.chart("scatter-simple", "基础散点图", "Basic Scatter Chart") {
            unsafeJso<EChartsOption> {
                xAxis = one(unsafeJso<XAxisOption> {})
                yAxis = one(unsafeJso<YAxisOption> {})
                series =
                    arrayOf<SeriesOption>(
                        scatterSeries {
                            symbolSize = SymbolSize(20.0)
                            typedData = dataItems(*Array(anscombe.size) { point(anscombe[it][0], anscombe[it][1]) })
                        },
                    )
            }
        }
