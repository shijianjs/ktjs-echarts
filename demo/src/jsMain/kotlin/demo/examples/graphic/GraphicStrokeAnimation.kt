package demo.examples.graphic

import demo.CatalogEntry
import demo.Groups
import demo.examples.Category

internal class GraphicStrokeAnimation {
    companion object {
        private val CATEGORY = Category(Groups.GRAPHIC, "examples/graphic/GraphicStrokeAnimation.kt")

        val ENTRY: CatalogEntry =
            CATEGORY.chart(
                    "graphic-stroke-animation",
                    "关键帧描边动画",
                    "Stroke Animation",
                ) {
                    graphicOption(strokeAnimationElements())
                }
    }
}
