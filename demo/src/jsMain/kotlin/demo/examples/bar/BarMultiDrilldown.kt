package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.EChartsEvents
import io.github.shijianjs.ktjs.echarts.options.one
import demo.examples.Category

private val barMultiDrilldownCategory = Category(Groups.BAR, "examples/bar/BarMultiDrilldown.kt")

val barMultiDrilldownEntry: CatalogEntry =
    barMultiDrilldownCategory.interactive(
            "bar-multi-drilldown",
            "柱状图多层下钻动画",
            "Bar Chart Multi-level Drilldown Animation",
            "点击柱体进入下一层：数据按 `groupId` / `childGroupId` 两列声明，`universalTransition` " +
                "把上一层的柱子克隆成下一层的柱子；左上角的 Back 逐层返回。",
            "Clicking a bar drills one level down: the rows declare `groupId` / `childGroupId` " +
                "columns and `universalTransition` clones the old bars into the new ones; the Back " +
                "caption walks back level by level.",
        ) { chart ->
            // Ids of the levels behind the current one; upstream reads them back from the option.
            val stack = ArrayDeque<String>()
            var current = drillLevel("things")

            fun show(level: DrillLevel) {
                current = level
                chart.setOption(
                    drillOption(level) {
                        stack.removeLastOrNull()?.let { show(drillLevel(it)) }
                    },
                )
            }

            show(current)
            chart.on(EChartsEvents.Click) { params ->
                val child = current.rows.getOrNull(params.dataIndex)?.child
                if (child != null) {
                    stack.addLast(current.id)
                    show(drillLevel(child))
                }
            }
        }
