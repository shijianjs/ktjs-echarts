package demo.json.examples.candlestick

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.SHANGHAI_2013
import demo.json.examples.shared.candleCategories
import demo.json.examples.shared.candleRows
import demo.json.examples.shared.movingAverageLines

private val candlestickShJsonCategory = JsonCategory("json/examples/candlestick/CandlestickShJson.kt")

// ---------------------------------------------------------------------------------------------
// Dates and generated prices
// ---------------------------------------------------------------------------------------------

val candlestickShJsonExample: JsonExample =
    candlestickShJsonCategory.example("candlestick-sh") {
            val rows = SHANGHAI_2013
            title {
                text = "上证指数"
                left = num(0)
            }
            tooltip {
                trigger = TooltipTrigger.Axis
                axisPointer = AxisPointerOption().apply { type = "cross" }
            }
            legend {
                entries("日K", "MA5", "MA10", "MA20", "MA30")
                top = num(20)
            }
            grid {
                left = str("10%")
                right = str("10%")
                bottom = str("15%")
            }
            xAxis {
                type = AxisType.Category
                categories(*candleCategories(rows))
                boundaryGap = BoundaryGap.of(false)
                axisLine { onZero = false }
                splitLine { show = false }
                min = str("dataMin")
                max = str("dataMax")
            }
            yAxis {
                scale = true
                splitArea { show = true }
            }
            dataZoom {
                type = DataZoomType.Inside
                start = 50.0
                end = 100.0
            }
            dataZoom {
                show = true
                type = DataZoomType.Slider
                top = str("90%")
                start = 50.0
                end = 100.0
            }
            series {
                candlestick {
                    name = "日K"
                    data = candleRows(rows)
                    itemStyle =
                        ItemStyleOption().apply {
                            color = "#ec0000".asColor()
                            borderColor = "#8A0000".asColor()
                        }
                    markPoint {
                        data =
                            listOf(
                                MarkPointData().apply {
                                    name = "Mark"
                                    coord = listOf(str("2013/5/31"), num(2300.0))
                                    value = valueOf(2300.0)
                                    itemStyle =
                                        ItemStyleOption().apply {
                                            color = "rgb(41,60,85)".asColor()
                                        }
                                },
                                MarkPointData().apply {
                                    name = "highest value"
                                    type = "max"
                                    valueDim = "highest"
                                },
                                MarkPointData().apply {
                                    name = "lowest value"
                                    type = "min"
                                    valueDim = "lowest"
                                },
                                MarkPointData().apply {
                                    name = "average value on close"
                                    type = "average"
                                    valueDim = "close"
                                },
                            )
                    }
                    markLine {
                        symbol = listOf("none", "none")
                        data =
                            listOf(
                                MarkLineDataItem.of(
                                    MarkLineData().apply {
                                        name = "from lowest to highest"
                                        type = "min"
                                        valueDim = "lowest"
                                        symbol = "circle"
                                        symbolSize = NumberOrStringOrNumbers.of(10)
                                        label = LabelOption().apply { show = false }
                                        emphasis =
                                            EmphasisOption().apply {
                                                label = LabelOption().apply { show = false }
                                            }
                                    },
                                    MarkLineData().apply {
                                        type = "max"
                                        valueDim = "highest"
                                        symbol = "circle"
                                        symbolSize = NumberOrStringOrNumbers.of(10)
                                        label = LabelOption().apply { show = false }
                                        emphasis =
                                            EmphasisOption().apply {
                                                label = LabelOption().apply { show = false }
                                            }
                                    },
                                ),
                                MarkLineDataItem.of(
                                    MarkLineData().apply {
                                        name = "min line on close"
                                        type = "min"
                                        valueDim = "close"
                                    },
                                ),
                                MarkLineDataItem.of(
                                    MarkLineData().apply {
                                        name = "max line on close"
                                        type = "max"
                                        valueDim = "close"
                                    },
                                ),
                            )
                    }
                }
                movingAverageLines(rows, lineOpacity = 0.5, lineWidth = null, symbols = null)
            }
        }
