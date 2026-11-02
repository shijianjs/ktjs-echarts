package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import demo.examples.Category
import demo.examples.funnel.labels
import demo.examples.scatter.point

private val lineRaceCategory = Category(Groups.LINE, "examples/line/LineRace.kt")

val lineRaceEntry: CatalogEntry =
    lineRaceCategory.interactive(
            "line-race",
            "动态排序折线图",
            "Line Race",
            summaryZh = "八个国家的收入折线从 1950 年起同时生长，线尾标签跟着最后一点滑动；悬停任一折线会淡化其余折线。",
            summaryEn = "Eight countries grow their post-1950 income lines at once, end labels trailing the last point; hovering one line fades the others.",
        ) { chart ->
            loadJson("data/life-expectancy-table.json").then { json ->
                chart.setOption(lineRaceOption(json.unsafeCast<DatasetSource>()))
                null
            }
        }
