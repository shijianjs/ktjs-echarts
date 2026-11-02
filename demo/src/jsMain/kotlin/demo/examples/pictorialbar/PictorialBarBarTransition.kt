package demo.examples.pictorialbar

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import demo.examples.Category
import demo.examples.shared.every

private val pictorialBarBarTransitionCategory = Category(Groups.PICTORIAL_BAR, "examples/pictorialbar/PictorialBarBarTransition.kt")

val pictorialBarBarTransitionEntry: CatalogEntry =
    pictorialBarBarTransitionCategory.interactive(
            "pictorialBar-bar-transition",
            "象形柱图变形为柱状图",
            "Transition between pictorialBar and bar",
            "同一张图每 2.5 秒在象形柱、普通柱和菱形柱之间切换，两个系列保持相同的 `id`，" +
                "`universalTransition` 按数据下标依次把旧形状拆成新形状。",
            "Every 2.5 seconds the chart switches between pictorial bars, plain bars and diamond " +
                "bars; the two series keep their `id`, so `universalTransition` morphs every item " +
                "into its next shape in data order.",
        ) { chart, holder ->
            val options =
                arrayOf(
                    transitionOption(bar = false),
                    transitionOption(bar = true),
                    transitionOption(bar = false, symbol = "diamond"),
                )
            var index = 0
            chart.setOption(options[index])
            holder.setInterval(
                {
                    if (chart.isDisposed()) return@setInterval
                    index = (index + 1) % options.size
                    chart.setOption(options[index])
                },
                TRANSITION_MILLIS,
            )
        }
