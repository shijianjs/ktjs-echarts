package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import io.github.shijianjs.ktjs.echarts.gl.options.*
import kotlin.js.JsAny
import demo.examples.Category

private val geo3dWithDifferentHeightCategory = Category(Groups.GL, "examples/gl/Geo3dWithDifferentHeight.kt")

val geo3dWithDifferentHeightEntry: CatalogEntry =
    geo3dWithDifferentHeightCategory.interactive(
            "geo3d-with-different-height",
            "Geo3D 不同高度",
            "Geo3D with Different Height",
            "每个国家的挤出高度按人口的对数缩放，木纹贴图随高度铺满侧面。",
            "Every country is extruded to a height proportional to the logarithm of its population, " +
                "with a wood texture tiling over the sides.",
        ) { chart ->
            registerWorldMap().then {
                loadJson("data/world-population.json").then { population ->
                    chart.setOption(geo3DHeightOption(population))
                }
            }
        }
