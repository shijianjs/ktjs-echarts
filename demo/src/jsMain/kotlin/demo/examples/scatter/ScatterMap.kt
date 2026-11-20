package demo.examples.scatter

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.shared.loadIntoChinaAqi
import demo.examples.shared.scatterMapOption

internal class ScatterMap {
    companion object {
        private val CATEGORY = Category(Groups.SCATTER, "examples/scatter/ScatterMap.kt")

        val ENTRY: CatalogEntry =
            CATEGORY.interactive("scatter-map", "全国主要城市空气质量", "Air Quality") { chart ->
                    loadIntoChinaAqi(chart, ::scatterMapOption)
                }
    }
}
