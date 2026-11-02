package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echartsgl.options.*
import kotlin.js.JsAny
import demo.examples.Category

private val scatter3dGlobePopulationCategory = Category(Groups.GL, "examples/gl/Scatter3dGlobePopulation.kt")

val scatter3dGlobePopulationEntry: CatalogEntry =
    scatter3dGlobePopulationCategory.interactive(
            "scatter3d-globe-population",
            "三维散点图 - 全球人口分布",
            "Scatter3D - Global Population",
            "三万多个人口格点在地球外层用加色混合叠加，点的大小按人口平方根映射，景深让远处的小点化开。",
            "Thirty-odd thousand population cells float above the globe with additive blending; their " +
                "size follows the square root of the population, and the depth of field blurs the far ones.",
        ) { chart ->
            loadJson("data/population.json").then { population ->
                chart.setOption(globePopulationOption(population))
            }
        }
