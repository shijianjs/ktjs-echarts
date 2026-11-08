package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.geo.registerGeojsonMap

private val docExampleMapVisualMapPiecewiseTextCategory = Category(Groups.DOC, "examples/doc/DocExampleMapVisualMapPiecewiseText.kt")

val docExampleMapVisualMapPiecewiseTextEntry: CatalogEntry =
    docExampleMapVisualMapPiecewiseTextCategory.interactive(
            "doc-example/map-visualMap-piecewise-text",
            "香港人口密度（分段型视觉映射）",
            "Hong Kong Density with a Piecewise Visual Map",
            "与连续型一页同样的数据，视觉映射改成分段型：按 `min`/`max` 自动切成若干段。",
            "The same data as the continuous page, mapped by a piecewise visual map that splits the " +
                "`min`/`max` range into pieces.",
        ) { chart ->
            chart.showLoading(null)
            loadJson("data/HK.json").then { hk ->
                chart.hideLoading()
                registerGeojsonMap("HK", hk)
                chart.setOption(hkDensityOption(VisualMapTypes.piecewise))
                null
            }
        }
