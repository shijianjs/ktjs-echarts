package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.ECharts
import echartsgl.options.*
import kotlin.js.JsAny
import demo.examples.Category

private val globeWithEchartsSurfaceCategory = Category(Groups.GL, "examples/gl/GlobeWithEchartsSurface.kt")

val globeWithEchartsSurfaceEntry: CatalogEntry =
    globeWithEchartsSurfaceCategory.interactive(
            "globe-with-echarts-surface",
            "ECharts 地图作为地球贴图",
            "Globe with ECharts Surface",
            "先把世界地图画进一张 4096×2048 的离屏图表，国家按人口上色，再把这整张画布当作球面贴图。",
            "A world map chart is painted onto an off-screen 4096×2048 canvas with the countries colored " +
                "by population, and that canvas becomes the surface texture of the globe.",
        ) { chart ->
            registerWorldMap().then {
                loadJson("data/world-population.json").then { population ->
                    val texture = echartsSurface ?: echartsSurfaceTexture(population).also { echartsSurface = it }
                    chart.setOption(echartsSurfaceOption(texture))
                }
            }
        }
