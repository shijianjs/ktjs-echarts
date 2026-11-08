package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.AxisTypes
import io.github.shijianjs.ktjs.echarts.TooltipTriggers
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.GridOption
import io.github.shijianjs.ktjs.echarts.options.LegendOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.SplitLineOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.TitleOption
import io.github.shijianjs.ktjs.echarts.options.TooltipFormatter
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.XAxisOption
import io.github.shijianjs.ktjs.echarts.options.YAxisOption
import io.github.shijianjs.ktjs.echarts.options.lineSeries
import io.github.shijianjs.ktjs.echarts.options.numbers
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.strings
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.gauge.center

private val lineLogCategory = Category(Groups.LINE, "examples/line/LineLog.kt")

val lineLogEntry: CatalogEntry =
    lineLogCategory.chart("line-log", "对数轴示例", "Log Axis") {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Log Axis"; left = StringOrDouble("center") })
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.item
                            formatter = TooltipFormatter("{a} <br/>{b} : {c}")
                        },
                    )
                legend = one(unsafeJso<LegendOption> { left = StringOrDouble("left") })
                grid = one(unsafeJso<GridOption> { left = StringOrDouble("3%"); right = StringOrDouble("4%"); bottom = StringOrDouble("3%"); containLabel = true })
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            name = "x"
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                            data = strings("A", "B", "C", "D", "E", "F", "G", "H", "I")
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.log
                            name = "y"
                            minorSplitLine = unsafeJso<SplitLineOption> { show = true }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        lineSeries { name = "Log2"; typedData = numbers(1.0, 3.0, 9.0, 27.0, 81.0, 247.0, 741.0, 2223.0, 6669.0) },
                        lineSeries { name = "Log3"; typedData = numbers(1.0, 2.0, 4.0, 8.0, 16.0, 32.0, 64.0, 128.0, 256.0) },
                        lineSeries {
                            name = "Log1/2"
                            typedData = numbers(0.5, 0.25, 0.125, 0.0625, 0.03125, 0.015625, 0.0078125, 0.00390625, 0.001953125)
                        },
                    )
            }
        }
