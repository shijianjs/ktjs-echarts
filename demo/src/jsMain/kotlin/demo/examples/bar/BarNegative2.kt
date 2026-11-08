package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.AxisTypes
import io.github.shijianjs.ktjs.echarts.options.AxisLabelOption
import io.github.shijianjs.ktjs.echarts.options.BarDataItem
import io.github.shijianjs.ktjs.echarts.options.BarDataItemOption
import io.github.shijianjs.ktjs.echarts.options.BooleanOrString
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.GridOption
import io.github.shijianjs.ktjs.echarts.options.LabelOption
import io.github.shijianjs.ktjs.echarts.options.OptionDataValue
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.SplitLineOption
import io.github.shijianjs.ktjs.echarts.options.SplitLineStyleOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.TitleOption
import io.github.shijianjs.ktjs.echarts.options.XAxisOption
import io.github.shijianjs.ktjs.echarts.options.YAxisOption
import io.github.shijianjs.ktjs.echarts.options.barSeries
import io.github.shijianjs.ktjs.echarts.options.dataItems
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.strings
import js.objects.unsafeJso
import kotlin.js.js
import io.github.shijianjs.ktjs.zrender.ZRLineTypes
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
                            position = io.github.shijianjs.ktjs.echarts.AxisPositions.top
                            splitLine = unsafeJso<SplitLineOption> { lineStyle = unsafeJso<SplitLineStyleOption> { type = ZRLineTypes.dashed } }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.category
                            axisLine = unsafeJso { show = io.github.shijianjs.ktjs.echarts.options.BooleanOrString(false) }
                            axisLabel = unsafeJso<AxisLabelOption> { show = false }
                            axisTick = unsafeJso { show = io.github.shijianjs.ktjs.echarts.options.BooleanOrString(false) }
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                            data = strings("ten", "nine", "eight", "seven", "six", "five", "four", "three", "two", "one")
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            name = "Cost"
                            stack = "Total"
                            label = unsafeJso<LabelOption> { show = true; formatter = io.github.shijianjs.ktjs.echarts.options.LabelFormatter("{b}") }
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
