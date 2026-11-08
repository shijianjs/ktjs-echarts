package demo.examples.geo

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.lines.coordinate
import demo.examples.shared.geoMapScatterOption
import demo.examples.shared.loadIntoChinaAqi

private val geoMapScatterCategory = Category(Groups.GEO, "examples/geo/GeoMapScatter.kt")

val geoMapScatterEntry: CatalogEntry =
    geoMapScatterCategory.interactive(
            "geo-map-scatter",
            "map 与 scatter 共用一个 geo",
            "Map and scatter sharing a geo",
            "散点与地图系列都挂在同一个 `geo` 组件上：`series.geoIndex` 让地图复用坐标，视觉映射只作用于它。",
            "A scatter and a map series share one `geo` component: `series.geoIndex` makes the map " +
                "reuse the coordinate system, and the visual map targets only it.",
        ) { chart ->
            loadIntoChinaAqi(chart, ::geoMapScatterOption)
        }
