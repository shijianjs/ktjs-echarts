package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import echarts.AxisTypes
import echarts.options.BooleanOrString
import echarts.options.EChartsOption
import echarts.options.LabelOption
import echarts.options.LabelPosition
import echarts.options.LegendOption
import echarts.options.SeriesOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.barSeries
import echarts.options.numbers
import echarts.options.one
import echarts.options.strings
import js.objects.unsafeJso
import kotlin.js.js
import demo.examples.Category
import demo.examples.shared.legendNames

private val barNegativeCategory = Category(Groups.BAR, "examples/bar/BarNegative.kt")

val barNegativeEntry: CatalogEntry =
    barNegativeCategory.chart("bar-negative", "正负条形图", "Bar Chart with Negative Value") {
            unsafeJso<EChartsOption> {
                tooltip = axisTooltip
                legend = one(unsafeJso<LegendOption> { data = legendNames("Profit", "Expenses", "Income") })
                xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.value })
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.category
                            axisTick = unsafeJso { show = echarts.options.BooleanOrString(false) }
                            data = strings(*week)
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            name = "Profit"
                            label = unsafeJso<LabelOption> { show = true; position = LabelPosition("inside") }
                            emphasis = seriesFocus
                            typedData = numbers(200.0, 170.0, 240.0, 244.0, 200.0, 220.0, 210.0)
                        },
                        barSeries {
                            name = "Income"
                            stack = "Total"
                            label = unsafeJso<LabelOption> { show = true }
                            emphasis = seriesFocus
                            typedData = numbers(320.0, 302.0, 341.0, 374.0, 390.0, 450.0, 420.0)
                        },
                        barSeries {
                            name = "Expenses"
                            stack = "Total"
                            label = unsafeJso<LabelOption> { show = true; position = LabelPosition("left") }
                            emphasis = seriesFocus
                            typedData = numbers(-120.0, -132.0, -101.0, -134.0, -190.0, -230.0, -210.0)
                        },
                    )
            }
        }
