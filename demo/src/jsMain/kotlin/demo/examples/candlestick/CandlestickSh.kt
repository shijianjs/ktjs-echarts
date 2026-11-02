package demo.examples.candlestick

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.scatter.extremes
import demo.examples.shared.legendNames

private val candlestickShCategory = Category(Groups.CANDLESTICK, "examples/candlestick/CandlestickSh.kt")

val candlestickShEntry: CatalogEntry =
    candlestickShCategory.chart(
            "candlestick-sh",
            "上证指数",
            "ShangHai Index",
            "2013 年上半年的上证指数日 K，叠了四条均线；标记点写出四个价格维度的极值，" +
                "标记线把最低价与最高价连起来，另有两条收盘价的极值线。",
            "The Shanghai Composite in the first half of 2013 with four moving averages: the mark " +
                "points label the extremes of all four price dimensions, one mark line joins the " +
                "lowest low to the highest high, and two more trace the closing extremes.",
        ) {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "上证指数"
                            left = StringOrDouble(0.0)
                        },
                    )
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.cross }
                        },
                    )
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            data = legendNames("日K", "MA5", "MA10", "MA20", "MA30")
                            top = StringOrDouble(20.0)
                        },
                    )
                grid =
                    one(
                        unsafeJso<GridOption> {
                            left = StringOrDouble("10%")
                            right = StringOrDouble("10%")
                            bottom = StringOrDouble("15%")
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            data = dateLabels(SHANGHAI_2013)
                            boundaryGap = BooleanOrStringArray(false)
                            axisLine = unsafeJso<AxisLineOption> { onZero = BooleanOrString(false) }
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                            min = AxisMinMax("dataMin")
                            max = AxisMinMax("dataMax")
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            scale = true
                            splitArea = unsafeJso<SplitAreaOption> { show = true }
                        },
                    )
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.inside
                            start = 50.0
                            end = 100.0
                        },
                        unsafeJso<DataZoomOption> {
                            show = true
                            type = DataZoomTypes.slider
                            top = StringOrDouble("90%")
                            start = 50.0
                            end = 100.0
                        },
                    )
                series =
                    arrayOf<SeriesOption>(shanghaiCandle()) +
                        movingAverageSeries(SHANGHAI_2013, showSymbol = null) { opacity = 0.5 }
            }
        }
