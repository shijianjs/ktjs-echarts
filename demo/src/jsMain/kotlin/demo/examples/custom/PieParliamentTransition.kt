package demo.examples.custom

import demo.CatalogEntry
import demo.Groups
import demo.examples.Category
import demo.examples.shared.every
import demo.examples.tree.leaves

private val pieParliamentTransitionCategory = Category(Groups.CUSTOM, "examples/custom/PieParliamentTransition.kt")

val pieParliamentTransitionEntry: CatalogEntry =
    pieParliamentTransitionCategory.interactive(
            "pie-parliament-transition",
            "自定义议会图与饼图过渡动画",
            "Transition of Parliament and Pie Chart",
            "同一个 `id` 的饼图与自定义系列每两秒互相切换，`universalTransition` 把扇形拆成议会的" +
                "席位圆点；定时器由 ChartHolder 托管，离开页面即清理。",
            "A pie and a custom series with the same `id` alternate every two seconds; " +
                "`universalTransition` splits the sectors into parliament seats, and the page's " +
                "ChartHolder owns the timer so it stops when the visitor leaves.",
        ) { chart, holder ->
            val pie = parliamentPieOption(PARLIAMENT_SLICES)
            val parliament = parliamentSeatsOption(PARLIAMENT_SLICES)
            var current = pie
            chart.setOption(current)
            holder.setInterval({
                current = if (current === pie) parliament else pie
                chart.setOption(current)
            }, PARLIAMENT_SWITCH_MILLIS)
        }
