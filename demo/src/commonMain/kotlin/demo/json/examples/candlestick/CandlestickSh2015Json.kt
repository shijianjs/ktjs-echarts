package demo.json.examples.candlestick

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.HANDLE_2015_ICON
import demo.json.examples.shared.SHANGHAI_2015
import demo.json.examples.shared.candleCategories
import demo.json.examples.shared.candleRows
import demo.json.examples.shared.movingAverageLines

private val candlestickSh2015JsonCategory = JsonCategory("json/examples/candlestick/CandlestickSh2015Json.kt")

// ---------------------------------------------------------------------------------------------
// Dates and generated prices
// ---------------------------------------------------------------------------------------------

val candlestickSh2015JsonExample: JsonExample =
    candlestickSh2015JsonCategory.example("candlestick-sh-2015") {
            val rows = SHANGHAI_2015
            legend {
                entries("日K", "MA5", "MA10", "MA20", "MA30")
                top = num(20)
            }
            tooltip {
                trigger = TooltipTrigger.Axis
                axisPointer =
                    AxisPointerOption().apply {
                        animation = false
                        type = "cross"
                        lineStyle =
                            LineStyleOption().apply {
                                color = "#376df4".asColor()
                                width = 2.0
                                opacity = 1.0
                            }
                    }
            }
            xAxis {
                type = AxisType.Category
                categories(*candleCategories(rows))
                axisLine { lineStyle = LineStyleOption().apply { color = "#8392A5".asColor() } }
            }
            yAxis {
                scale = true
                axisLine { lineStyle = LineStyleOption().apply { color = "#8392A5".asColor() } }
                splitLine { show = false }
            }
            grid { bottom = num(80) }
            dataZoom {
                textStyle = TextStyleOption().apply { color = "#8392A5".asColor() }
                handleIcon = HANDLE_2015_ICON
                dataBackground =
                    DataBackgroundOption().apply {
                        areaStyle = AreaStyleOption().apply { color = "#8392A5".asColor() }
                        lineStyle =
                            LineStyleOption().apply {
                                opacity = 0.8
                                color = "#8392A5".asColor()
                            }
                    }
                brushSelect = true
            }
            dataZoom { type = DataZoomType.Inside }
            series {
                candlestick {
                    name = "Day"
                    data = candleRows(rows)
                    itemStyle =
                        ItemStyleOption().apply {
                            color = "#FD1050".asColor()
                            borderColor = "#FD1050".asColor()
                        }
                }
                movingAverageLines(rows, lineOpacity = null, lineWidth = 1.0, symbols = false)
            }
        }
