package demo.examples.geo

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import demo.examples.Category

internal class MapUsa {
    companion object {
        private val CATEGORY = Category(Groups.GEO, "examples/geo/MapUsa.kt")

        val ENTRY: CatalogEntry =
            CATEGORY.interactive("map-usa", "2012 年美国人口统计", "USA Population Estimates (2012)") { chart ->
                    chart.showLoading(null)
                    loadJson("data/USA.json").then { json ->
                        chart.hideLoading()
                        ECharts.registerMap(
                            "USA",
                            MapInput(unsafeJso<GeoJSONMapInput> { geoJSON = json.unsafeCast<GeoJSONSourceInput>() }),
                            usaSpecialAreas(),
                        )
                        chart.setOption(usaMapOption())
                        null
                    }
                }
    }
}
