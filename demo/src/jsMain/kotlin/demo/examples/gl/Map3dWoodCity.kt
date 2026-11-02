package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echartsgl.options.*
import kotlin.js.JsAny
import demo.examples.Category

private val map3dWoodCityCategory = Category(Groups.GL, "examples/gl/Map3dWoodCity.kt")

val map3dWoodCityEntry: CatalogEntry =
    map3dWoodCityCategory.interactive(
            "map3d-wood-city",
            "木质风格城市",
            "Wood City",
            "同一座城市换成木纹材质立在木质地面上，柔和的阴影把楼与楼之间压出层次。",
            "The same city drawn in a wood material on a wooden ground, the soft shadows separating " +
                "the blocks from each other.",
        ) { chart ->
            loadJson("data/gl/buildings.json").then { buildings ->
                chart.setOption(woodCityMapOption(buildings))
            }
        }
