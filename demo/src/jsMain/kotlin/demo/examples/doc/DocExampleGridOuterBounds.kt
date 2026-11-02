package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.radar.indicator

private val docExampleGridOuterBoundsCategory = Category(Groups.DOC, "examples/doc/DocExampleGridOuterBounds.kt")

val docExampleGridOuterBoundsEntry: CatalogEntry =
    docExampleGridOuterBoundsCategory.interactive(
            "doc-example/grid-outerBounds",
            "网格外边界（grid.outerBounds）",
            "Grid outerBounds and its indicator rectangles",
        ) { chart, holder ->
            // The two tutorial rectangles are pixel-sized, so they follow the container.
            fun repaint() {
                chart.setOption(outerBoundsOption(chart.getWidth(), chart.getHeight()))
            }
            repaint()
            holder.onResize(::repaint)
        }
