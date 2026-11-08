package demo.examples.scatter

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import demo.examples.Category

private val scatterLifeExpectancyTimelineCategory = Category(Groups.SCATTER, "examples/scatter/ScatterLifeExpectancyTimeline.kt")

val scatterLifeExpectancyTimelineEntry: CatalogEntry =
    scatterLifeExpectancyTimelineCategory.interactive(
            "scatter-life-expectancy-timeline",
            "各国人均寿命与GDP关系演变",
            "Life Expectancy and GDP",
            "时间轴逐年播放 1800 年以来的数据：横轴是人均收入的 log 轴，纵轴是平均寿命，气泡大小表示人口，" +
                "颜色按国家区分。",
            "A timeline walks the table from 1800 on: income on a log axis, life expectancy on the " +
                "value axis, the population as the bubble size and one colour per country.",
        ) { chart ->
            loadJson("data/life-expectancy.json").then { json ->
                chart.setOption(lifeTimelineOption(json.unsafeCast<LifeTimelineTable>()))
                null
            }
        }
