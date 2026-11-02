package demo.examples.graphic

import demo.CatalogEntry
import demo.Groups
import demo.examples.Category

private val graphicLoadingCategory = Category(Groups.GRAPHIC, "examples/graphic/GraphicLoading.kt")

val graphicLoadingEntry: CatalogEntry =
    graphicLoadingCategory.chart(
            "graphic-loading",
            "加载动画",
            "Customized Loading Animation",
        ) {
            graphicOption(loadingAnimationElements())
        }
