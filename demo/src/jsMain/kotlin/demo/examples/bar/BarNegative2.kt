package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import echarts.AxisTypes
import echarts.options.AxisLabelOption
import echarts.options.BarDataItem
import echarts.options.BarDataItemOption
import echarts.options.BooleanOrString
import echarts.options.EChartsOption
import echarts.options.GridOption
import echarts.options.LabelOption
import echarts.options.OptionDataValue
import echarts.options.SeriesOption
import echarts.options.SplitLineOption
import echarts.options.SplitLineStyleOption
import echarts.options.StringOrDouble
import echarts.options.TitleOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.barSeries
import echarts.options.dataItems
import echarts.options.one
import echarts.options.strings
import js.objects.unsafeJso
import kotlin.js.js
import zrender.ZRLineTypes
import demo.examples.Category
import demo.examples.shared.axisLabel

private val barNegative2Category = Category(Groups.BAR, "examples/bar/BarNegative2.kt")

val barNegative2Entry: CatalogEntry =
    barNegative2Category.chart("bar-negative2", "交错正负轴标签", "Bar Chart with Negative Value") {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Bar Chart with Negative Value" })
                tooltip = axisTooltip
                grid = one(unsafeJso<GridOption> { top = StringOrDouble(80.0); bottom = StringOrDouble(30.0) })
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.value
                            position = echarts.AxisPositions.top
                            splitLine = unsafeJso<SplitLineOption> { lineStyle = unsafeJso<SplitLineStyleOption> { type = ZRLineTypes.dashed } }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.category
                            axisLine = unsafeJso { show = echarts.options.BooleanOrString(false) }
                            axisLabel = unsafeJso<AxisLabelOption> { show = false }
                            axisTick = unsafeJso { show = echarts.options.BooleanOrString(false) }
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                            data = strings("ten", "nine", "eight", "seven", "six", "five", "four", "three", "two", "one")
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            name = "Cost"
                            stack = "Total"
                            label = unsafeJso<LabelOption> { show = true; formatter = echarts.options.LabelFormatter("{b}") }
                            typedData =
                                dataItems(
                                    BarDataItem(unsafeJso<BarDataItemOption> { value = OptionDataValue(-0.07); label = rightLabel() }),
                                    BarDataItem(unsafeJso<BarDataItemOption> { value = OptionDataValue(-0.09); label = rightLabel() }),
                                    BarDataItem(OptionDataValue(0.2)),
                                    BarDataItem(OptionDataValue(0.44)),
                                    BarDataItem(unsafeJso<BarDataItemOption> { value = OptionDataValue(-0.23); label = rightLabel() }),
                                    BarDataItem(OptionDataValue(0.08)),
                                    BarDataItem(unsafeJso<BarDataItemOption> { value = OptionDataValue(-0.17); label = rightLabel() }),
                                    BarDataItem(OptionDataValue(0.47)),
                                    BarDataItem(unsafeJso<BarDataItemOption> { value = OptionDataValue(-0.36); label = rightLabel() }),
                                    BarDataItem(OptionDataValue(0.18)),
                                )
                        },
                    )
            }
        }
