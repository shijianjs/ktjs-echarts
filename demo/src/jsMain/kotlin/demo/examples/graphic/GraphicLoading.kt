package demo.examples.graphic

import demo.CatalogEntry
import demo.Groups
import demo.examples.Category

internal class GraphicLoading {
    companion object {
        private val CATEGORY = Category(Groups.GRAPHIC, "examples/graphic/GraphicLoading.kt")

        val ENTRY: CatalogEntry =
            CATEGORY.chart(
                    "graphic-loading",
                    "加载动画",
                    "Customized Loading Animation",
                ) {
                    graphicOption(loadingAnimationElements())
                }
    }
}
