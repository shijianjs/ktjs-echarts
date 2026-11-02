package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echartsgl.options.*
import kotlin.js.JsAny
import demo.examples.Category

private val map3dBuildingsCategory = Category(Groups.GL, "examples/gl/Map3dBuildings.kt")

val map3dBuildingsEntry: CatalogEntry =
    map3dBuildingsCategory.interactive(
            "map3d-buildings",
            "三维建筑",
            "Buildings",
            "一座城市的每栋楼按 GeoJSON 里记录的高度挤出，景深让远处的楼群虚化，画面接近实景照片。",
            "Every building of a city extruded to the height stored in its GeoJSON, with the depth " +
                "of field blurring the blocks in the distance.",
        ) { chart ->
            loadJson("data/gl/buildings.json").then { buildings ->
                chart.setOption(buildingsMapOption(buildings))
            }
        }
