package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import kotlin.js.JsAny
import demo.examples.Category

private val docExampleSunburstHighlightAncestorCategory = Category(Groups.DOC, "examples/doc/DocExampleSunburstHighlightAncestor.kt")

val docExampleSunburstHighlightAncestorEntry: CatalogEntry =
    docExampleSunburstHighlightAncestorCategory.interactive(
            "doc-example/sunburst-highlight-ancestor",
            "旭日图：高亮祖先节点",
            "Sunburst: highlight the ancestors",
        ) { chart, holder ->
            chart.setOption(highlightOption(EmphasisFocuses.ancestor, ancestorTree()))
            holder.setTimeout({ highlightTarget(chart, ANCESTOR_TARGET_DATA_INDEX) }, 0)
        }
