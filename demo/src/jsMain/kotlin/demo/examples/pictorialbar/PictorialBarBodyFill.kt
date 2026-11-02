package demo.examples.pictorialbar

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.shared.axisLabel
import demo.examples.shared.legendNames

private val pictorialBarBodyFillCategory = Category(Groups.PICTORIAL_BAR, "examples/pictorialbar/PictorialBarBodyFill.kt")

val pictorialBarBodyFillEntry: CatalogEntry =
    pictorialBarBodyFillCategory.chart(
            "pictorialBar-body-fill",
            "人体含水量",
            "Water Content",
            "五个人形剪影用 `symbolClip` 按数值裁切，后面一层灰色的 `full` 系列画出完整身高，" +
                "标签把数值换算成百分比，`markLine` 标出两组数据的最大值与最小值。",
            "Five body silhouettes clipped to their value with `symbolClip`; a grey `full` series draws " +
                "the whole body behind them, and mark lines show the min and max of each group.",
        ) {
            unsafeJso<EChartsOption> {
                tooltip = one(unsafeJso<TooltipOption> {})
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            data = legendNames("typeA", "typeB")
                            selectedMode = SelectionMode("single")
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            data = strings(*BODY_NAMES)
                            axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                            axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
                            axisLabel = unsafeJso<AxisLabelOption> { show = false }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            max = AxisMinMax(BODY_MAX)
                            offset = 20.0
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                        },
                    )
                grid =
                    one(
                        unsafeJso<GridOption> {
                            top = StringOrDouble("center")
                            height = StringOrDouble(230.0)
                        },
                    )
                series =
                    arrayOf(
                        bodySeries("typeA", BODY_A),
                        bodySeries("typeB", BODY_B),
                        fullBodySeries(),
                    )
            }
        }
