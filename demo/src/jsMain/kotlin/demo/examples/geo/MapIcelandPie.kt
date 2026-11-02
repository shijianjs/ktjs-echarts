package demo.examples.geo

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import demo.examples.Category

private val mapIcelandPieCategory = Category(Groups.GEO, "examples/geo/MapIcelandPie.kt")

val mapIcelandPieEntry: CatalogEntry =
    mapIcelandPieCategory.interactive("map-iceland-pie", "在地图上显示饼图", "Pie Charts on GEO Map") { chart ->
            chart.showLoading(null)
            loadJson("data/iceland.geo.json").then { json ->
                chart.hideLoading()
                registerGeojsonMap("iceland", json)
                chart.setOption(icelandOption())
                null
            }
        }
