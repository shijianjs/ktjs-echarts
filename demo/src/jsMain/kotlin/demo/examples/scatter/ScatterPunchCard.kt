package demo.examples.scatter

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import demo.examples.Category
import demo.examples.chord.column
import demo.examples.heatmap.punchCard
import demo.examples.shared.row
import demo.examples.themeriver.days

private val scatterPunchCardCategory = Category(Groups.SCATTER, "examples/scatter/ScatterPunchCard.kt")

val scatterPunchCardEntry: CatalogEntry =
    scatterPunchCardCategory.chart(
            "scatter-punchCard",
            "GitHub 打卡气泡图",
            "Punch Card of Github",
            "一周七天 × 一天的 24 小时铺成网格，气泡大小就是那个小时的提交数；" +
                "`symbolSize` 读的是数据行的第三列，提示框把行号翻译成星期与小时。",
            "A week of days against the 24 hours of the day, the bubble area showing the commits of " +
                "that hour; `symbolSize` reads the third column of the row and the tooltip turns the " +
                "row numbers back into a weekday and an hour.",
        ) {
            punchCardOption()
        }
