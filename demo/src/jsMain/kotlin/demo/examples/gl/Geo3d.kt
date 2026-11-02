package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import echartsgl.options.*
import kotlin.js.JsAny
import demo.examples.Category

private val geo3dCategory = Category(Groups.GL, "examples/gl/Geo3d.kt")

val geo3dEntry: CatalogEntry =
    geo3dCategory.interactive(
            "geo3d",
            "Geo3D",
            "Geo3D",
            "世界地图被挤出成一块块立体大陆，放在会接住阴影的地面上，凹处由屏幕空间环境光遮蔽压暗。",
            "The world map extruded into solid continents on a shadowed ground plane, with screen-space " +
                "ambient occlusion darkening the folds.",
        ) { chart ->
            registerWorldMap().then { chart.setOption(geo3DOption()) }
        }
