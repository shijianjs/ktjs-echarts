package demo.examples.heatmap

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.shared.heatmapMapOption
import demo.examples.shared.loadIntoChinaAqi

private val heatmapMapCategory = Category(Groups.HEATMAP, "examples/heatmap/HeatmapMap.kt")

val heatmapMapEntry: CatalogEntry =
    heatmapMapCategory.interactive("heatmap-map", "全国主要城市空气质量 - 热力图", "Air Quality Heatmap") { chart ->
            loadIntoChinaAqi(chart, ::heatmapMapOption)
        }
