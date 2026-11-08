package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.AxisTypes
import io.github.shijianjs.ktjs.echarts.TooltipTriggers
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.EmphasisOption
import io.github.shijianjs.ktjs.echarts.options.GridOption
import io.github.shijianjs.ktjs.echarts.options.ItemStyleOption
import io.github.shijianjs.ktjs.echarts.options.LabelOption
import io.github.shijianjs.ktjs.echarts.options.LabelPosition
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.SplitLineOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.TitleOption
import io.github.shijianjs.ktjs.echarts.options.TooltipFormatter
import io.github.shijianjs.ktjs.echarts.options.TooltipFormatterCallback
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.XAxisOption
import io.github.shijianjs.ktjs.echarts.options.barSeries
import io.github.shijianjs.ktjs.echarts.options.numbers
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.strings
import js.objects.unsafeJso
import kotlin.js.js
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category

private val barWaterfallCategory = Category(Groups.BAR, "examples/bar/BarWaterfall.kt")

val barWaterfallEntry: CatalogEntry =
    barWaterfallCategory.chart("bar-waterfall", "瀑布图（柱状图模拟）", "Waterfall Chart") {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Waterfall Chart"
                            subtext = "Living Expenses in Shenzhen"
                        },
                    )
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer = shadowPointer
                            // The original formatter reads the second series of the axis trigger.
                            formatter =
                                TooltipFormatter(
                                    js("(params) => params[1].name + '<br/>' + params[1].seriesName + ' : ' + params[1].value") as TooltipFormatterCallback,
                                )
                        },
                    )
                grid = one(unsafeJso<GridOption> { left = StringOrDouble("3%"); right = StringOrDouble("4%"); bottom = StringOrDouble("3%"); containLabel = true })
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                            data = strings("Total", "Rent", "Utilities", "Transportation", "Meals", "Other")
                        },
                    )
                yAxis = valueAxis
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            name = "Placeholder"
                            stack = "Total"
                            itemStyle = unsafeJso<ItemStyleOption> { borderColor = ZRColor("transparent"); color = ZRColor("transparent") }
                            emphasis =
                                unsafeJso<EmphasisOption> {
                                    itemStyle = unsafeJso<ItemStyleOption> { borderColor = ZRColor("transparent"); color = ZRColor("transparent") }
                                }
                            typedData = numbers(0.0, 1700.0, 1400.0, 1200.0, 300.0, 0.0)
                        },
                        barSeries {
                            name = "Life Cost"
                            stack = "Total"
                            label = unsafeJso<LabelOption> { show = true; position = LabelPosition("inside") }
                            typedData = numbers(2900.0, 1200.0, 300.0, 200.0, 900.0, 300.0)
                        },
                    )
            }
        }
