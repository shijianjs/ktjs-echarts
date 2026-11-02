package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import demo.examples.Category

private val areaRainfallCategory = Category(Groups.LINE, "examples/line/AreaRainfall.kt")

val areaRainfallEntry: CatalogEntry =
    areaRainfallCategory.interactive(
            "area-rainfall",
            "雨量流量关系图",
            "Rainfall and Flow Relationship",
            "流量与雨量的双轴面积图，九月两段降雨用 markArea 标出，双 y 轴自动对齐刻度。",
            "Flow and rainfall as two axes of area series, with the September rain spells marked.",
        ) { chart ->
            loadJson("data/rainfall-flow.json").then { json ->
                chart.setOption(areaRainfallOption(json.unsafeCast<RainfallFlowData>()))
                null
            }
        }
