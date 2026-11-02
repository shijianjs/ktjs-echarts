package demo.examples.scatter

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.shared.effectScatterMapOption
import demo.examples.shared.loadIntoChinaAqi

private val effectScatterMapCategory = Category(Groups.SCATTER, "examples/scatter/EffectScatterMap.kt")

val effectScatterMapEntry: CatalogEntry =
    effectScatterMapCategory.interactive(
            "effectScatter-map",
            "全国主要城市空气质量 - 涟漪散点图",
            "Air Quality (effect scatter)",
            "同样的 190 个城市：普通散点按 pm2.5 定大小，前六名换成带涟漪的 `effectScatter`。",
            "The same 190 cities: the scatter sizes its symbols by pm2.5, and the six worst become " +
                "rippling effect-scatter symbols.",
        ) { chart ->
            loadIntoChinaAqi(chart, ::effectScatterMapOption)
        }
