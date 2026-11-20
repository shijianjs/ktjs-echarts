package demo.examples.heatmap

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.shared.heatmapMapOption
import demo.examples.shared.loadIntoChinaAqi

internal class HeatmapMap {
    companion object {
        private val CATEGORY = Category(Groups.HEATMAP, "examples/heatmap/HeatmapMap.kt")

        val ENTRY: CatalogEntry =
            CATEGORY.interactive("heatmap-map", "全国主要城市空气质量 - 热力图", "Air Quality Heatmap") { chart ->
                    loadIntoChinaAqi(chart, ::heatmapMapOption)
                }
    }
}
