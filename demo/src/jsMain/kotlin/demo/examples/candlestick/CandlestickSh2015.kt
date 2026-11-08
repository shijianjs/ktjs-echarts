package demo.examples.candlestick

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.shared.legendNames

private val candlestickSh2015Category = Category(Groups.CANDLESTICK, "examples/candlestick/CandlestickSh2015.kt")

val candlestickSh2015Entry: CatalogEntry =
    candlestickSh2015Category.chart(
            "candlestick-sh-2015",
            "2015 年上证指数",
            "ShangHai Index, 2015",
            "2015 年全年的上证指数日 K：上游表倒序存放，并留下两行空记录，所以图上开头是两根零值蜡烛。" +
                "滑块用自定义把手图标与数据阴影，缩放到 50% 可以看到 6 月的高点。",
            "The Shanghai Composite for the whole of 2015. The upstream table is stored newest first " +
                "and keeps two empty rows, so the chart opens with two zero candles; the slider uses " +
                "a custom handle icon and a data shadow.",
        ) {
            unsafeJso<EChartsOption> {
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            data = legendNames("日K", "MA5", "MA10", "MA20", "MA30")
                            inactiveColor = "#777"
                            top = StringOrDouble(20.0)
                        },
                    )
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer =
                                unsafeJso<AxisPointerOption> {
                                    animation = BooleanOrString(false)
                                    type = AxisPointerTypes.cross
                                    lineStyle =
                                        unsafeJso<LineStyleOption> {
                                            color = ZRColor(CROSS_2015_COLOR)
                                            width = 2.0
                                            opacity = 1.0
                                        }
                                }
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            data = dateLabels(SHANGHAI_2015)
                            axisLine =
                                unsafeJso<AxisLineOption> {
                                    lineStyle = unsafeJso<LineStyleOption> { color = ZRColor(AXIS_2015_COLOR) }
                                }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            scale = true
                            axisLine =
                                unsafeJso<AxisLineOption> {
                                    lineStyle = unsafeJso<LineStyleOption> { color = ZRColor(AXIS_2015_COLOR) }
                                }
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                        },
                    )
                grid = one(unsafeJso<GridOption> { bottom = StringOrDouble(80.0) })
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> {
                            textStyle = unsafeJso<TextStyleOption> { color = AXIS_2015_COLOR }
                            handleIcon = HANDLE_2015_ICON
                            dataBackground =
                                unsafeJso<DataZoomDataBackgroundOption> {
                                    areaStyle = unsafeJso<AreaStyleOption> { color = ZRColor(AXIS_2015_COLOR) }
                                    lineStyle =
                                        unsafeJso<LineStyleOption> {
                                            opacity = 0.8
                                            color = ZRColor(AXIS_2015_COLOR)
                                        }
                                }
                            brushSelect = true
                        },
                        unsafeJso<DataZoomOption> { type = DataZoomTypes.inside },
                    )
                series =
                    arrayOf<SeriesOption>(
                        candlestickSeries {
                            name = "Day"
                            typedData = candleRows(SHANGHAI_2015)
                            typedItemStyle =
                                unsafeJso<CandlestickItemStyleOption> {
                                    color = ZRColor("#FD1050")
                                    color0 = ZRColor("#0CF49B")
                                    borderColor = ZRColor("#FD1050")
                                    borderColor0 = ZRColor("#0CF49B")
                                }
                        },
                    ) + movingAverageSeries(SHANGHAI_2015, showSymbol = false) { width = 1.0 }
            }
        }
