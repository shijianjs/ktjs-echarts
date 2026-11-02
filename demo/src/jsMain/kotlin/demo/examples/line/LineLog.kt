package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import echarts.AxisTypes
import echarts.TooltipTriggers
import echarts.options.EChartsOption
import echarts.options.GridOption
import echarts.options.LegendOption
import echarts.options.SeriesOption
import echarts.options.SplitLineOption
import echarts.options.StringOrDouble
import echarts.options.TitleOption
import echarts.options.TooltipFormatter
import echarts.options.TooltipOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.lineSeries
import echarts.options.numbers
import echarts.options.one
import echarts.options.strings
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
