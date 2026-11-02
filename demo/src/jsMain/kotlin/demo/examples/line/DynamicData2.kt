package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import demo.examples.Category

private val dynamicData2Category = Category(Groups.LINE, "examples/line/DynamicData2.kt")

val dynamicData2Entry: CatalogEntry =
    dynamicData2Category.interactive("dynamic-data2", "动态数据 + 时间坐标轴", "Dynamic Data + Time Axis") { chart, holder ->
            val window = DynamicDataWindow(19971003)
            chart.setOption(
                unsafeJso<EChartsOption> {
                    title = one(unsafeJso<TitleOption> { text = "Dynamic Data & Time Axis" })
                    tooltip =
                        one(
                            unsafeJso<TooltipOption> {
                                trigger = TooltipTriggers.axis
                                formatter = dynamicDataTooltip()
                                axisPointer = unsafeJso<AxisPointerOption> { animation = BooleanOrString(false) }
                            },
                        )
                    xAxis =
                        one(
                            unsafeJso<XAxisOption> {
                                type = AxisTypes.time
                                splitLine = unsafeJso<SplitLineOption> { show = false }
                            },
                        )
                    yAxis =
                        one(
                            unsafeJso<YAxisOption> {
                                type = AxisTypes.value
                                boundaryGap = BooleanOrStringArray(arrayOf(StringOrDouble(0.0), StringOrDouble("100%")))
                                splitLine = unsafeJso<SplitLineOption> { show = false }
                            },
                        )
                    series =
                        arrayOf<SeriesOption>(
                            lineSeries {
                                name = "Fake Data"
                                showSymbol = false
                                typedData = window.data()
                            },
                        )
                },
            )
            // Every second the window drops its five oldest points and gains five new ones.
            holder.setInterval(
                {
                    window.advance(5)
                    chart.setOption(
                        unsafeJso<EChartsOption> {
                            series =
                                arrayOf<SeriesOption>(
                                    lineSeries { typedData = window.data() },
                                )
                        },
                    )
                },
                1000,
            )
        }
