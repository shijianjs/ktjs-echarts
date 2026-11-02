package demo.examples.matrix

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import demo.examples.Category
import demo.examples.doc.decal

private val matrixMbtiCategory = Category(Groups.MATRIX, "examples/matrix/MatrixMbti.kt")

val matrixMbtiEntry: CatalogEntry =
    matrixMbtiCategory.interactive(
            "matrix-mbti",
            "MBTI 伴侣相容性",
            "MBTI Partner Compatibility",
            summaryZh = "16×16 的伴侣相容矩阵，每个格子用双色 decal 图案区分两边的类型，点击画布可切换到四组的中位数汇总。",
            summaryEn = "A 16 by 16 partner matrix whose cells carry two-tone decals; click the chart to swap in the four-group median summary.",
        ) { chart ->
            var grouped = false
            chart.setOption(mbtiOption(chart.getWidth(), chart.getHeight(), grouped))
            chart.on(EChartsEvents.Click) {
                grouped = !grouped
                chart.setOption(mbtiOption(chart.getWidth(), chart.getHeight(), grouped), true)
            }
        }
