package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import kotlin.js.unsafeCast
import demo.examples.Category
import demo.examples.doc.ObamaBudgetJson

private val mixZoomOnValueCategory = Category(Groups.BAR, "examples/bar/MixZoomOnValue.kt")

val mixZoomOnValueEntry: CatalogEntry =
    mixZoomOnValueCategory.interactive(
            "mix-zoom-on-value",
            "多数值轴轴缩放",
            "Mix Zoom On Value",
            summaryZh = "两年前的预算对比条形图；横向与纵向三个 dataZoom 同时缩放，纵向滑杆用 `filterMode: 'empty'` 保留类目轴。",
            summaryEn = "Two budget proposals over 2011 and 2012; three dataZooms zoom both axes, and the vertical one keeps the category axis alive with `filterMode: 'empty'`.",
        ) { chart ->
            loadJson("data/obama_budget_proposal_2012.list.json").then { json ->
                chart.setOption(mixZoomOnValueOption(json.unsafeCast<ObamaBudgetJson>()))
                null
            }
        }
