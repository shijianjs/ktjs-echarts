package demo.examples.geo

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import demo.examples.Category

private val mapBarMorphCategory = Category(Groups.GEO, "examples/geo/MapBarMorph.kt")

val mapBarMorphEntry: CatalogEntry =
    mapBarMorphCategory.interactive("map-bar-morph", "地图柱状图变形动画", "Morphing between Map and Bar") { chart, holder ->
            chart.showLoading(null)
            loadJson("data/USA.json").then { json ->
                chart.hideLoading()
                ECharts.registerMap(
                    "USA",
                    MapInput(unsafeJso<GeoJSONMapInput> { geoJSON = json.unsafeCast<GeoJSONSourceInput>() }),
                    usaSpecialAreas(),
                )
                val sorted = USA_POPULATION.sortedBy { it.second }.toTypedArray()
                val mapOption = morphMapOption(sorted)
                val barOption = morphBarOption(sorted)
                var current = mapOption
                chart.setOption(mapOption)
                holder.setInterval(
                    {
                        current = if (current === mapOption) barOption else mapOption
                        chart.setOption(current, true)
                    },
                    2000,
                )
                null
            }
        }
