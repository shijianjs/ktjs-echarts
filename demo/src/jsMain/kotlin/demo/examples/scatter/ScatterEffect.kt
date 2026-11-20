package demo.examples.scatter

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import demo.examples.Category

internal class ScatterEffect {
    companion object {
        private val CATEGORY = Category(Groups.SCATTER, "examples/scatter/ScatterEffect.kt")

        val ENTRY: CatalogEntry =
            CATEGORY.chart("scatter-effect", "涟漪特效散点图", "Effect Scatter Chart") {
                    unsafeJso<EChartsOption> {
                        xAxis = one(unsafeJso<XAxisOption> { scale = true })
                        yAxis = one(unsafeJso<YAxisOption> { scale = true })
                        series =
                            arrayOf<SeriesOption>(
                                effectScatterSeries {
                                    symbolSize = SymbolSize(20.0)
                                    typedData = EFFECT_BUBBLES.unsafeCast<Array<EffectScatterDataItem?>>()
                                },
                                scatterSeries { typedData = EFFECT_CLOUD },
                            )
                    }
                }
    }
}
