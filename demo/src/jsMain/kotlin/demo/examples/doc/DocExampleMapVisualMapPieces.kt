package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import demo.examples.Category
import demo.examples.funnel.labels
import demo.examples.geo.region
import demo.examples.geo.usaSpecialAreas

private val docExampleMapVisualMapPiecesCategory = Category(Groups.DOC, "examples/doc/DocExampleMapVisualMapPieces.kt")

val docExampleMapVisualMapPiecesEntry: CatalogEntry =
    docExampleMapVisualMapPiecesCategory.interactive(
            "doc-example/map-visualMap-pieces",
            "美国订单分段视觉映射",
            "USA Orders with Piecewise Visual Map",
            "虚构订单量按自定义分段着色，两段带自己的标签与颜色，区间外用灰色；标签按区域宽度缩放并隐藏重叠。",
            "Fake order volumes colored by custom pieces — two of them carry their own label and " +
                "color, everything else falls out of range — with labels scaled to the region width.",
        ) { chart ->
            chart.showLoading(null)
            loadJson("data/USA.json").then { usa ->
                chart.hideLoading()
                ECharts.registerMap(
                    "USA",
                    MapInput(unsafeJso<GeoJSONMapInput> { geoJSON = usa.unsafeCast<GeoJSONSourceInput>() }),
                    usaSpecialAreas(),
                )
                chart.setOption(usaOrderOption(usaRegionNames(usa)))
                null
            }
        }
