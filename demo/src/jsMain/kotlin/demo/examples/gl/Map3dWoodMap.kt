package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echartsgl.options.*
import kotlin.js.JsAny
import demo.examples.Category

private val map3dWoodMapCategory = Category(Groups.GL, "examples/gl/Map3dWoodMap.kt")

val map3dWoodMapEntry: CatalogEntry =
    map3dWoodMapCategory.interactive(
            "map3d-wood-map",
            "木质世界地图",
            "Wood Map",
            "世界地图的每个国家按饮酒量开方后挤出，整块大陆铺着木纹，红砖地面在斜阳下接住长影。",
            "Every country is extruded to the square root of its alcohol consumption, the continents " +
                "covered in wood grain and standing on a red-brick ground.",
        ) { chart ->
            registerWorldMap().then {
                loadJson("data/gl/alcohol.json").then { alcohol ->
                    chart.setOption(woodMapOption(alcohol))
                }
            }
        }
