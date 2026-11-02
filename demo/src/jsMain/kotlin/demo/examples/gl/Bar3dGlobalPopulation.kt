package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echartsgl.options.*
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.matrix.cell

private val bar3dGlobalPopulationCategory = Category(Groups.GL, "examples/gl/Bar3dGlobalPopulation.kt")

val bar3dGlobalPopulationEntry: CatalogEntry =
    bar3dGlobalPopulationCategory.interactive(
            "bar3d-global-population",
            "Bar3D - 全球人口分布",
            "Bar3D - Global Population",
            "把全球五公里见方的人口格点画成橙色细柱，立在接受阴影的世界地图上，柱高取人口的平方根。",
            "Every five-kilometre population cell becomes a thin orange bar standing on a shadowed " +
                "world map, its height taken from the square root of the population.",
        ) { chart ->
            registerWorldMap().then {
                loadJson("data/population.json").then { population ->
                    chart.setOption(bar3DPopulationOption(population))
                }
            }
        }
