package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.shared.every

private val barStackNormalizationCategory = Category(Groups.BAR, "examples/bar/BarStackNormalization.kt")

val barStackNormalizationEntry: CatalogEntry =
    barStackNormalizationCategory.chart(
            "bar-stack-normalization",
            "堆叠柱状图的归一化",
            "Stacked Bar Normalization",
            "五个投放渠道按天堆叠，每一根柱子的高度是当天各渠道占总量的比例，标签把该比例写成百分数。",
            "Five advertising channels stacked per day, where every bar is the share of the daily total " +
                "and the label writes that share as a percentage.",
        ) {
            val (xAxisOption, yAxisOption) = normalizationAxes()
            unsafeJso<EChartsOption> {
                legend = one(normalizationLegend())
                xAxis = one(xAxisOption)
                yAxis = one(yAxisOption)
                series = normalizedSeriesList()
            }
        }
