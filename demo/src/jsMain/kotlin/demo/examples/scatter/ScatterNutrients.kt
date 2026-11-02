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
import demo.examples.shared.row

private val scatterNutrientsCategory = Category(Groups.SCATTER, "examples/scatter/ScatterNutrients.kt")

val scatterNutrientsEntry: CatalogEntry =
    scatterNutrientsCategory.interactive(
            "scatter-nutrients",
            "营养分布散点图",
            "Scatter Nutrients",
            "七千多种食品的营养表，蛋白质与钙两个维度上的 1000 个点；颜色按食品类别，明暗按第三个维度，" +
                "第一个 zlevel 打开了尾迹模糊。",
            "A thousand points of a seven-thousand-row nutrient table on the protein/calcium plane; " +
                "the group sets the colour and the first zlevel turns on the motion-blur trail.",
        ) { chart ->
            loadJson("data/nutrients.json").then { json ->
                chart.getZr()?.configLayer(1.0, unsafeJso { motionBlur = true })
                chart.setOption(nutrientScatterOption(normalizeNutrients(json, NUTRIENT_SCATTER_ROWS)))
                null
            }
        }
