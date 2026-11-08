package demo.examples.matrix

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import kotlin.js.js
import demo.examples.Category
import demo.examples.shared.Lcg

private val matrixStockCategory = Category(Groups.MATRIX, "examples/matrix/MatrixStock.kt")

val matrixStockEntry: CatalogEntry =
    matrixStockCategory.interactive(
            "matrix-stock",
            "股市矩阵图",
            "Matrix Stock Application",
            summaryZh =
                "一个交易日的看板铺在 5×6 的矩阵上：价格走势、成交量、MACD、买卖十档和深度图各占一格，" +
                    "网格和标题都钉在矩阵格子里，午休时段由断轴折掉。",
            summaryEn =
                "One trading day as a 5 by 6 matrix: price, volume, MACD, an order book and a depth " +
                    "chart in cells of their own, with grids and titles pinned to cells and the lunch " +
                    "break folded away by an axis break.",
        ) { chart ->
            val walk = walkStock(Lcg(20251016))
            chart.setOption(stockOption(walk, chart.getWidth(), chart.getHeight()))
        }
