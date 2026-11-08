package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.geo.region
import demo.examples.geo.registerGeojsonMap
import demo.examples.matrix.names

private val docExampleMapVisualMapContinuousTextCategory = Category(Groups.DOC, "examples/doc/DocExampleMapVisualMapContinuousText.kt")

val docExampleMapVisualMapContinuousTextEntry: CatalogEntry =
    docExampleMapVisualMapContinuousTextCategory.interactive(
            "doc-example/map-visualMap-continuous-text",
            "香港人口密度（连续型视觉映射）",
            "Hong Kong Density with a Continuous Visual Map",
            "香港 18 区人口密度用连续色带映射，`nameMap` 把英文区名换成中文。",
            "The density of the 18 districts of Hong Kong on a continuous color band, with `nameMap` " +
                "translating the English region names.",
        ) { chart ->
            chart.showLoading(null)
            loadJson("data/HK.json").then { hk ->
                chart.hideLoading()
                registerGeojsonMap("HK", hk)
                chart.setOption(hkDensityOption(VisualMapTypes.continuous))
                null
            }
        }
