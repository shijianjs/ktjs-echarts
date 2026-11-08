package demo.examples.scatter

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import demo.examples.Category
import demo.examples.shared.every

private val scatterLabelAlignTopCategory = Category(Groups.SCATTER, "examples/scatter/ScatterLabelAlignTop.kt")

val scatterLabelAlignTopEntry: CatalogEntry =
    scatterLabelAlignTopCategory.chart(
            "scatter-label-align-top",
            "散点图标签顶部对齐",
            "Align Label on the Top",
            "人均 GDP 与期望寿命的气泡散点，标签是国名，`labelLayout` 把它们统一挪到气泡上方并横向错开。",
            "Bubbles of income against life expectancy labelled with the country name; `labelLayout` " +
                "lifts every label to the top of its bubble and shifts them apart horizontally.",
        ) {
            labelTopOption()
        }
