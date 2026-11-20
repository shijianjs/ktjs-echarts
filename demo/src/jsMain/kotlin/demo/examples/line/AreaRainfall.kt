package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import demo.examples.Category

internal class AreaRainfall {
    companion object {
        private val CATEGORY = Category(Groups.LINE, "examples/line/AreaRainfall.kt")

        val ENTRY: CatalogEntry =
            CATEGORY.interactive(
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
    }
}
