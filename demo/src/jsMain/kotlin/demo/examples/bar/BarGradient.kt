package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import zrender.gradientColorStop
import zrender.linearGradient
import demo.examples.Category
import demo.examples.shared.axisLabel

private val barGradientCategory = Category(Groups.BAR, "examples/bar/BarGradient.kt")

val barGradientEntry: CatalogEntry =
    barGradientCategory.interactive("bar-gradient", "特性示例：渐变色 阴影 点击缩放", "Clickable Column Chart with Gradient") { chart ->
            chart.setOption(
                unsafeJso<EChartsOption> {
                    title =
                        one(
                            unsafeJso<TitleOption> {
                                text = "特性示例：渐变色 阴影 点击缩放"
                                subtext = "Feature Sample: Gradient Color, Shadow, Click Zoom"
                            },
                        )
                    xAxis =
                        one(
                            unsafeJso<XAxisOption> {
                                data = strings(*gradientAxis)
                                axisLabel = unsafeJso<AxisLabelOption> { inside = true; color = "#fff" }
                                axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                                axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
                                z = 10.0
                            },
                        )
                    yAxis =
                        one(
                            unsafeJso<YAxisOption> {
                                axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
                                axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                                axisLabel = unsafeJso<AxisLabelOption> { color = "#999" }
                            },
                        )
                    dataZoom = arrayOf(unsafeJso<DataZoomOption> { type = DataZoomTypes.inside })
                    series =
                        one(
                            barSeries {
                                showBackground = true
                                itemStyle =
                                    unsafeJso<ItemStyleOption> {
                                        color =
                                            linearGradient(
                                                0.0,
                                                0.0,
                                                0.0,
                                                1.0,
                                                arrayOf(
                                                    gradientColorStop(0.0, "#83bff6"),
                                                    gradientColorStop(0.5, "#188df0"),
                                                    gradientColorStop(1.0, "#188df0"),
                                                ),
                                            )
                                    }
                                emphasis =
                                    unsafeJso<EmphasisOption> {
                                        itemStyle =
                                            unsafeJso<ItemStyleOption> {
                                                color =
                                                    linearGradient(
                                                        0.0,
                                                        0.0,
                                                        0.0,
                                                        1.0,
                                                        arrayOf(
                                                            gradientColorStop(0.0, "#2378f7"),
                                                            gradientColorStop(0.7, "#2378f7"),
                                                            gradientColorStop(1.0, "#83bff6"),
                                                        ),
                                                    )
                                            }
                                    }
                                typedData = numbers(*gradientValues)
                            },
                        )
                },
            )
            // The original zooms to six bars around the clicked one.
            chart.on("click") { params ->
                val first = kotlin.math.max(params.dataIndex - 3, 0)
                val last = kotlin.math.min(params.dataIndex + 3, gradientAxis.size - 1)
                chart.dispatchAction(
                    unsafeJso<DataZoomAction> {
                        type = "dataZoom"
                        startValue = OptionDataValue(gradientAxis[first])
                        endValue = OptionDataValue(gradientAxis[last])
                    },
                )
            }
        }
