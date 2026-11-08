package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.geo.registerGeojsonMap

private val docExampleMapExampleCategory = Category(Groups.DOC, "examples/doc/DocExampleMapExample.kt")

val docExampleMapExampleEntry: CatalogEntry =
    docExampleMapExampleCategory.interactive(
            "doc-example/map-example",
            "中国地图（iphone 销量）",
            "China Map (iphone Sales)",
            "三个地图系列叠加在同一张中国地图上，图例切换、视觉映射按销量从高到低着色。",
            "Three map series stacked on one China map: the legend switches between them and a " +
                "visual map colors the provinces by the sales figures.",
        ) { chart ->
            chart.showLoading(null)
            loadJson("data/china.json").then { china ->
                chart.hideLoading()
                registerGeojsonMap("china", china)
                chart.setOption(chinaSalesOption())
                null
            }
        }
