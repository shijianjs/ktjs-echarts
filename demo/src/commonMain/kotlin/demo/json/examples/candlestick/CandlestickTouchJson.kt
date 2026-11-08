package demo.json.examples.candlestick

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.TOUCH_COLORS
import demo.json.examples.shared.TOUCH_DATES
import demo.json.examples.shared.TOUCH_HANDLE_ICON
import demo.json.examples.shared.TOUCH_ROWS
import demo.json.examples.shared.TOUCH_VOLUMES
import demo.json.examples.shared.candleRows
import demo.json.examples.shared.touchMovingAverages

private val candlestickTouchJsonCategory = JsonCategory("json/examples/candlestick/CandlestickTouchJson.kt")

// ---------------------------------------------------------------------------------------------
// Dates and generated prices
// ---------------------------------------------------------------------------------------------

val candlestickTouchJsonExample: JsonExample =
    candlestickTouchJsonCategory.example("candlestick-touch") {
            val rows = TOUCH_ROWS
            animation = false
            color = colors(*TOUCH_COLORS)
            title {
                left = str("center")
                text = "Candlestick on Mobile"
            }
            legend {
                top = num(30)
                entries("日K", "MA5", "MA10", "MA20", "MA30")
            }
            tooltip {
                triggerOn = "none"
                transitionDuration = 0.0
                confine = true
                borderWidth = 1.0
                borderColor = "#333".asColor()
                backgroundColor = "rgba(255,255,255,0.9)".asColor()
                textStyle {
                    fontSize = 12.0
                    color = "#333".asColor()
                }
            }
            dataZoom {
                type = DataZoomType.Slider
                xAxisIndex = listOf(0, 1)
                realtime = false
                start = 20.0
                end = 70.0
                top = num(65)
                height = num(20)
                handleIcon = TOUCH_HANDLE_ICON
                handleSize = NumberOrStringOrNumbers.of("120%")
            }
            dataZoom {
                type = DataZoomType.Inside
                xAxisIndex = listOf(0, 1)
                start = 40.0
                end = 70.0
                top = num(30)
                height = num(20)
            }
            grid {
                left = num(20)
                right = num(20)
                top = num(110)
                height = num(120)
            }
            grid {
                left = num(20)
                right = num(20)
                top = num(260)
                height = num(40)
            }
            xAxis {
                type = AxisType.Category
                categories(*TOUCH_DATES)
                boundaryGap = BoundaryGap.of(false)
                axisLine { lineStyle = LineStyleOption().apply { color = "#777".asColor() } }
                min = str("dataMin")
                max = str("dataMax")
                axisPointer = AxisPointerOption().apply { show = true }
            }
            xAxis {
                type = AxisType.Category
                gridIndex = 1
                categories(*TOUCH_DATES)
                boundaryGap = BoundaryGap.of(false)
                splitLine { show = false }
                axisLabel { show = false }
                axisTick { show = false }
                axisLine { lineStyle = LineStyleOption().apply { color = "#777".asColor() } }
                min = str("dataMin")
                max = str("dataMax")
                axisPointer =
                    AxisPointerOption().apply {
                        type = "shadow"
                        label = AxisPointerLabelOption().apply { show = false }
                        triggerTooltip = true
                    }
            }
            yAxis {
                scale = true
                splitNumber = 2
                axisLine { lineStyle = LineStyleOption().apply { color = "#777".asColor() } }
                splitLine { show = true }
                axisTick { show = false }
                axisLabel {
                    inside = true
                    formatter = "{value}\n"
                }
            }
            yAxis {
                scale = true
                gridIndex = 1
                splitNumber = 2
                axisLabel { show = false }
                axisLine { show = false }
                axisTick { show = false }
                splitLine { show = false }
            }
            series {
                bar {
                    name = "Volume"
                    xAxisIndex = 1
                    yAxisIndex = 1
                    itemStyle = ItemStyleOption().apply { color = "#7fbe9e".asColor() }
                    emphasis { itemStyle = ItemStyleOption().apply { color = "#140".asColor() } }
                    values(*TOUCH_VOLUMES)
                }
                candlestick {
                    name = "日K"
                    data = candleRows(rows)
                    itemStyle =
                        ItemStyleOption().apply {
                            color = "#ef232a".asColor()
                            borderColor = "#ef232a".asColor()
                        }
                    emphasis {
                        itemStyle =
                            ItemStyleOption().apply {
                                color = "black".asColor()
                                borderColor = "black".asColor()
                            }
                    }
                }
                touchMovingAverages(rows)
            }
        }
