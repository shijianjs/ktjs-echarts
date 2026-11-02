package demo.examples.scatter

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.js
import demo.examples.Category

private val scatterNutrientsMatrixCategory = Category(Groups.SCATTER, "examples/scatter/ScatterNutrientsMatrix.kt")

val scatterNutrientsMatrixEntry: CatalogEntry =
    scatterNutrientsMatrixCategory.interactive(
            "scatter-nutrients-matrix",
            "营养分布散点矩阵",
            "Scatter Nutrients Matrix",
            "同样的营养表分成四块 2×2 的散点图，四块共享同一个类别着色；坐标轴指示器按 id 联动，" +
                "提示框把同一食品在四个窗口里的数值合成一张表。",
            "The same table in four linked panes: the crosshair follows the hovered value across panes " +
                "and the tooltip merges the values of one food into a single block.",
        ) { chart ->
            loadJson("data/nutrients.json").then { json ->
                chart.getZr()?.configLayer(1.0, unsafeJso { motionBlur = true })
                chart.setOption(nutrientMatrixOption(normalizeNutrients(json, NUTRIENT_SCATTER_ROWS)))
                null
            }
        }
