package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import kotlin.js.JsAny
import demo.examples.Category

private val docExampleSunburstHighlightDescendantCategory = Category(Groups.DOC, "examples/doc/DocExampleSunburstHighlightDescendant.kt")

val docExampleSunburstHighlightDescendantEntry: CatalogEntry =
    docExampleSunburstHighlightDescendantCategory.interactive(
            "doc-example/sunburst-highlight-descendant",
            "旭日图：高亮子孙节点",
            "Sunburst: highlight the descendants",
        ) { chart, holder ->
            chart.setOption(highlightOption(EmphasisFocuses.descendant, descendantTree()))
            holder.setTimeout({ highlightTarget(chart, DESCENDANT_TARGET_DATA_INDEX) }, 0)
        }
