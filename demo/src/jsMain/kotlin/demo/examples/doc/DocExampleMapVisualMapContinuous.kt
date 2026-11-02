package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.gl.registerWorldMap

private val docExampleMapVisualMapContinuousCategory = Category(Groups.DOC, "examples/doc/DocExampleMapVisualMapContinuous.kt")

val docExampleMapVisualMapContinuousEntry: CatalogEntry =
    docExampleMapVisualMapContinuousCategory.interactive(
            "doc-example/map-visualMap-continuous",
            "世界人口连续型视觉映射",
            "World Population with a Continuous Visual Map",
            "177 个国家按 2010 年人口落在连续色带上，色带两端可拖拽筛选。",
            "The 2010 population of 177 countries mapped onto a continuous color band whose handles " +
                "filter the range.",
        ) { chart ->
            registerWorldMap().then {
                loadJson("data/world-population.json").then { population ->
                    chart.setOption(worldPopulationOption(population))
                    null
                }
            }
        }
