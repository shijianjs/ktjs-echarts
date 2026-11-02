package demo.examples.geo

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import kotlin.js.JsAny
import demo.examples.Category

private val geoLinesCategory = Category(Groups.GEO, "examples/geo/GeoLines.kt")

val geoLinesEntry: CatalogEntry =
    geoLinesCategory.interactive(
            "geo-lines",
            "模拟迁徙",
            "Migration",
            "三组迁徙航线各画三遍：拖尾的 `lines`、带箭头与飞机符号的 `lines`，以及目的地上的涟漪散点。",
            "Each of the three migration groups is drawn three times: trailing `lines`, the same routes " +
                "with an arrow end and an airplane flying them, and a ripple scatter on the destinations.",
        ) { chart ->
            chart.showLoading(null)
            loadGeoLines().then { table ->
                if (!chart.isDisposed()) {
                    chart.hideLoading()
                    chart.setOption(migrationOption(table))
                }
                null
            }
        }
