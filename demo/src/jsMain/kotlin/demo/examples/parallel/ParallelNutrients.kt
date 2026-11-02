package demo.examples.parallel

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.scatter.normalizeNutrients

private val parallelNutrientsCategory = Category(Groups.PARALLEL, "examples/parallel/ParallelNutrients.kt")

val parallelNutrientsEntry: CatalogEntry =
    parallelNutrientsCategory.interactive(
            "parallel-nutrients",
            "营养结构（平行坐标）",
            "Parallel Nutrients",
            "整张营养表铺在十五条平行轴上，每条折线是一种食品；右侧的 visualMap 按 25 个食品类别着色，" +
                "鼠标悬停会把其它折线淡出。",
            "The whole nutrient table laid across fifteen parallel axes, one polyline per food; the " +
                "piecewise visual map colours the lines by food group and hovering fades the rest.",
        ) { chart ->
            loadJson("data/nutrients.json").then { json ->
                chart.setOption(parallelNutrientsOption(normalizeNutrients(json, Int.MAX_VALUE)))
                null
            }
        }
