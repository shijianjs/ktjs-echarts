package demo.examples.geo

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import demo.examples.Category

internal class MapIcelandPie {
    companion object {
        private val CATEGORY = Category(Groups.GEO, "examples/geo/MapIcelandPie.kt")

        val ENTRY: CatalogEntry =
            CATEGORY.interactive("map-iceland-pie", "在地图上显示饼图", "Pie Charts on GEO Map") { chart ->
                    chart.showLoading(null)
                    loadJson("data/iceland.geo.json").then { json ->
                        chart.hideLoading()
                        registerGeojsonMap("iceland", json)
                        chart.setOption(icelandOption())
                        null
                    }
                }
    }
}
