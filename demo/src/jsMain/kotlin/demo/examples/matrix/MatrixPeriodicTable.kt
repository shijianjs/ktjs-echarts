package demo.examples.matrix

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import demo.examples.Category

private val matrixPeriodicTableCategory = Category(Groups.MATRIX, "examples/matrix/MatrixPeriodicTable.kt")

val matrixPeriodicTableEntry: CatalogEntry =
    matrixPeriodicTableCategory.interactive(
            "matrix-periodic-table",
            "元素周期表",
            "Periodic Table",
            summaryZh = "19 组 × 10 周期的矩阵坐标上，custom 系列按 api.layout 铺出 118 个元素，三行类目说明用 graphic 文字标注。",
            summaryEn = "A custom series lays out 118 elements on a 19 × 10 matrix with api.layout; the block captions are graphic text elements.",
        ) { chart, holder ->
            chart.setOption(periodicTableOption(kotlin.math.max(480.0, chart.getWidth() - 160)))
            // The captions need the laid-out matrix, which only exists after the first render.
            holder.setTimeout({ chart.setOption(periodicCaptionsOption(chart)) }, 0)
        }
