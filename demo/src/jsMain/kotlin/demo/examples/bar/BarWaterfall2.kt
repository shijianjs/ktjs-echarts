package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.matrix.names
import demo.examples.shared.legendNames

private val barWaterfall2Category = Category(Groups.BAR, "examples/bar/BarWaterfall2.kt")

val barWaterfall2Entry: CatalogEntry =
    barWaterfall2Category.chart(
            "bar-waterfall2",
            "阶梯瀑布图（柱状图模拟）",
            "Waterfall Chart",
            "一段透明的占位柱顶起收入与支出，tooltip 只报出当天可见的那一段。",
            "A transparent placeholder lifts each income and expense segment, and the tooltip names only " +
                "the visible segment of that day.",
        ) {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Accumulated Waterfall Chart" })
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.shadow }
                            formatter = waterfallTooltip()
                        },
                    )
                legend = one(unsafeJso<LegendOption> { data = legendNames("Expenses", "Income") })
                grid =
                    one(
                        unsafeJso<GridOption> {
                            left = StringOrDouble("3%")
                            right = StringOrDouble("4%")
                            bottom = StringOrDouble("3%")
                            containLabel = true
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            data = strings(*WATERFALL_DAYS)
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            name = "Placeholder"
                            stack = "Total"
                            silent = true
                            itemStyle = invisibleStyle()
                            emphasis = unsafeJso<EmphasisOption> { itemStyle = invisibleStyle() }
                            typedData = numbers(*WATERFALL_PLACEHOLDERS)
                        },
                        barSeries {
                            name = "Income"
                            stack = "Total"
                            label =
                                unsafeJso<LabelOption> {
                                    show = true
                                    position = LabelPosition("top")
                                }
                            typedData =
                                waterfallValues(
                                    900, 345, 393, "-", "-", 135, 178, 286, "-", "-", "-",
                                )
                        },
                        barSeries {
                            name = "Expenses"
                            stack = "Total"
                            label =
                                unsafeJso<LabelOption> {
                                    show = true
                                    position = LabelPosition("bottom")
                                }
                            typedData =
                                waterfallValues(
                                    "-", "-", "-", 108, 154, "-", "-", "-", 119, 361, 203,
                                )
                        },
                    )
            }
        }
