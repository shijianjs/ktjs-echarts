package demo.examples.scatter

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import io.github.shijianjs.ktjs.echarts.ECharts
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import demo.examples.Category

private val scatterWorldPopulationCategory = Category(Groups.SCATTER, "examples/scatter/ScatterWorldPopulation.kt")

val scatterWorldPopulationEntry: CatalogEntry =
    scatterWorldPopulationCategory.interactive(
            "scatter-world-population",
            "世界人口（2011）",
            "World Population (2011)",
            "169 个国家按人口数量映射气泡直径，落在可缩放平移的世界地图上。",
            "169 countries as bubbles sized by their 2011 population, on a roamable world map.",
        ) { chart ->
            loadJson("data/world.json").then { world ->
                ECharts.registerMap(
                    "world",
                    MapInput(unsafeJso<GeoJSONMapInput> { geoJSON = world.unsafeCast<GeoJSONSourceInput>() }),
                )
                chart.setOption(worldPopulationOption())
                null
            }
        }
