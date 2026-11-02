package demo.examples.scatter

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.shared.loadIntoChinaAqi
import demo.examples.shared.scatterMapOption

private val scatterMapCategory = Category(Groups.SCATTER, "examples/scatter/ScatterMap.kt")

val scatterMapEntry: CatalogEntry =
    scatterMapCategory.interactive("scatter-map", "全国主要城市空气质量", "Air Quality") { chart ->
            loadIntoChinaAqi(chart, ::scatterMapOption)
        }
