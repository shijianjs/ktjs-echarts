package demo.examples.matrix

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import demo.examples.Category

private val matrixMiniBarGeoCategory = Category(Groups.MATRIX, "examples/matrix/MatrixMiniBarGeo.kt")

val matrixMiniBarGeoEntry: CatalogEntry =
    matrixMiniBarGeoCategory.interactive("matrix-mini-bar-geo", "矩阵坐标系下的微型条形图和地图", "Mini Bars and Geo in Matrix") { chart ->
            chart.showLoading(null)
            loadJson("data/ch.geo.json").then { json ->
                chart.hideLoading()
                ECharts.registerMap(
                    "target_map",
                    MapInput(unsafeJso<GeoJSONMapInput> { geoJSON = json.unsafeCast<GeoJSONSourceInput>() }),
                )
                chart.setOption(miniBarGeoOption())
                null
            }
        }
