package demo.examples.matrix

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import demo.examples.Category
import demo.examples.chord.column
import demo.examples.custom.coord
import demo.examples.shared.every

private val matrixGridLayoutCategory = Category(Groups.MATRIX, "examples/matrix/MatrixGridLayout.kt")

val matrixGridLayoutEntry: CatalogEntry =
    matrixGridLayoutCategory.chart(
            "matrix-grid-layout",
            "矩阵中响应式网格布局",
            "Responsive grid layout based on matrix",
            "矩阵只负责定义行列，五个区块各自的网格通过 `grid.coord` 落在指定单元格上；" +
                "两个 `media` 单元分别描述窄屏（单列堆叠）与宽屏（标题带 + 侧栏 + 内容列），" +
                "拖动改变画布宽度即可看到重排。",
            "The matrix only defines columns and rows; every section grid is pinned to its cells by " +
                "`grid.coord`, and two `media` units describe the narrow (one stacked column) and the " +
                "wide (title band, sidebar, content column) arrangements.",
        ) {
            layoutOption()
        }
