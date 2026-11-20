package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import io.github.shijianjs.ktjs.echarts.gl.options.*
import kotlin.js.JsAny
import demo.examples.Category

internal class Map3dWoodCity {
    companion object {
        private val CATEGORY = Category(Groups.GL, "examples/gl/Map3dWoodCity.kt")

        val ENTRY: CatalogEntry =
            CATEGORY.interactive(
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
    }
}
