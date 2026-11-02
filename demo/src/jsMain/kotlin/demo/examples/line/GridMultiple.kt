package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import demo.examples.Category

private val gridMultipleCategory = Category(Groups.LINE, "examples/line/GridMultiple.kt")

val gridMultipleEntry: CatalogEntry =
    gridMultipleCategory.interactive(
            "grid-multiple",
            "雨量蒸发量关系图",
            "Rainfall vs Evaporation",
            "上下两个网格共享 3079 个小时级刻度，上轴蒸发量、下轴雨量，dataZoom 与轴线联动。",
            "Two grids over 3079 hourly ticks: evaporation above, rainfall below, linked zoom and axes.",
        ) { chart ->
            loadJson("data/rainfall-evaporation.json").then { json ->
                chart.setOption(gridMultipleOption(json.unsafeCast<RainfallEvaporationData>()))
                null
            }
        }
