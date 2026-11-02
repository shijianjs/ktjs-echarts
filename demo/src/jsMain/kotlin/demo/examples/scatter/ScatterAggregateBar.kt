package demo.examples.scatter

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import demo.examples.Category
import demo.examples.shared.every

private val scatterAggregateBarCategory = Category(Groups.SCATTER, "examples/scatter/ScatterAggregateBar.kt")

val scatterAggregateBarEntry: CatalogEntry =
    scatterAggregateBarCategory.interactive(
            "scatter-aggregate-bar",
            "散点图聚合为柱状图动画",
            "Aggregate Scatter to Bar",
            "两组身高体重散点每两秒折叠成两根平均身高柱，再展开回散点；" +
                "`dataGroupId` 与 `seriesKey` 把散点的分组和柱子的 `groupId` 对上。",
            "Two clouds of heights and weights fold into two average bars every two seconds and " +
                "unfold again; `dataGroupId` and `seriesKey` match the scatter groups with the " +
                "`groupId` of the bars.",
        ) { chart, holder ->
            val scatter = aggregateScatterOption()
            val bars = aggregateBarOption()
            var current = scatter
            chart.setOption(current)
            holder.setInterval({
                if (chart.isDisposed()) return@setInterval
                current = if (current === scatter) bars else scatter
                chart.setOption(current, true)
            }, AGGREGATE_INTERVAL_MILLIS)
        }
