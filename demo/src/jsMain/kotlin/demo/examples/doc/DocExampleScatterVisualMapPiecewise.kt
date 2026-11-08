package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.shared.docScatterPiecewiseOption
import demo.examples.shared.loadIntoChinaAqi

private val docExampleScatterVisualMapPiecewiseCategory = Category(Groups.DOC, "examples/doc/DocExampleScatterVisualMapPiecewise.kt")

val docExampleScatterVisualMapPiecewiseEntry: CatalogEntry =
    docExampleScatterVisualMapPiecewiseCategory.interactive(
            "doc-example/scatter-visualMap-piecewise",
            "全国主要城市空气质量（分段视觉映射）",
            "China Air Quality with a Piecewise Visual Map",
            "与 `scatter-map` 同一份数据，视觉映射改成 `splitNumber` 切分的色带，颜色用旧版 `color` 键。",
            "The same table as `scatter-map`, mapped by a visual map split into `splitNumber` bands " +
                "and colored through the legacy `color` key.",
        ) { chart ->
            loadIntoChinaAqi(chart, ::docScatterPiecewiseOption)
        }
