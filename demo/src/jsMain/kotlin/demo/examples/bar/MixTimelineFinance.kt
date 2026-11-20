package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.shared.mixTimelineYears

internal class MixTimelineFinance {
    companion object {
        private val CATEGORY = Category(Groups.BAR, "examples/bar/MixTimelineFinance.kt")

        val ENTRY: CatalogEntry =
            CATEGORY.chart(
                    "mix-timeline-finance",
                    "2002全国宏观经济指标",
                    "Finance Indices 2002",
                    summaryZh = "时间轴逐年播放十个年份，六根省级柱与一张三产业占比饼图同步换数据；图例默认隐藏 GDP、金融、房地产三项。",
                    summaryEn = "A timeline plays ten years of provincial bars plus a pie of the three industries; the legend starts with GDP, finance and real estate switched off.",
                ) {
                    unsafeJso {
                        baseOption = financeBaseOption()
                        options = Array(mixTimelineYears.size) { index -> financeYearOption(index) }
                    }
                }
    }
}
