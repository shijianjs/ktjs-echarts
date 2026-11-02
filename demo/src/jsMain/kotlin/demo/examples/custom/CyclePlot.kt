package demo.examples.custom

import demo.CatalogEntry
import demo.Groups
import echarts.options.one
import demo.examples.Category

private val cyclePlotCategory = Category(Groups.CUSTOM, "examples/custom/CyclePlot.kt")

val cyclePlotEntry: CatalogEntry =
    cyclePlotCategory.chart(
            "cycle-plot",
            "周期图",
            "Cycle Plot",
            summaryZh = "x 轴是月份，每年在各自的月份带里画一条折线，用来比较同一个月在不同年份的走势。",
            summaryEn = "Months on the x axis with one trend line per year inside each band, so a month can be compared across years.",
        ) {
            cyclePlotOption()
        }
