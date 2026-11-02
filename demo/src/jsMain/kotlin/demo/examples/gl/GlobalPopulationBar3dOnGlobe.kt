package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echartsgl.options.*
import kotlin.js.JsAny
import demo.examples.Category

private val globalPopulationBar3dOnGlobeCategory = Category(Groups.GL, "examples/gl/GlobalPopulationBar3dOnGlobe.kt")

val globalPopulationBar3dOnGlobeEntry: CatalogEntry =
    globalPopulationBar3dOnGlobeCategory.interactive(
            "global-population-bar3d-on-globe",
            "全球人口分布 - 地球上的 Bar3D",
            "Global Population - Bar3D on Globe",
            "同一份人口数据立在会自转停下的地球上，柱子在星空下沿球面铺开。",
            "The same population data stands on a globe under a starfield, the bars fanning out " +
                "along the surface of the sphere.",
        ) { chart ->
            loadJson("data/population.json").then { population ->
                chart.setOption(bar3DPopulationOnGlobeOption(population))
            }
        }
