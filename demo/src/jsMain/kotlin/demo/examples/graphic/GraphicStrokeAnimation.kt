package demo.examples.graphic

import demo.CatalogEntry
import demo.Groups
import demo.examples.Category

private val graphicStrokeAnimationCategory = Category(Groups.GRAPHIC, "examples/graphic/GraphicStrokeAnimation.kt")

val graphicStrokeAnimationEntry: CatalogEntry =
    graphicStrokeAnimationCategory.chart(
            "graphic-stroke-animation",
            "关键帧描边动画",
            "Stroke Animation",
        ) {
            graphicOption(strokeAnimationElements())
        }
