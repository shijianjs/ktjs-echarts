package demo.json.examples.candlestick

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.LARGE_CANDLES
import demo.json.examples.shared.LARGE_CANDLE_COUNT
import demo.json.examples.shared.candleCategories
import demo.json.examples.shared.candleRows

private val candlestickLargeJsonCategory = JsonCategory("json/examples/candlestick/CandlestickLargeJson.kt")

// ---------------------------------------------------------------------------------------------
// Dates and generated prices
// ---------------------------------------------------------------------------------------------

val candlestickLargeJsonExample: JsonExample =
    candlestickLargeJsonCategory.example("candlestick-large") {
            val rows = LARGE_CANDLES
            title { text = "Data Amount: $LARGE_CANDLE_COUNT" }
            tooltip {
                trigger = TooltipTrigger.Axis
                axisPointer = AxisPointerOption().apply { type = "line" }
            }
            toolbox {
                feature = ToolboxFeatureOption().apply { dataZoom = ToolboxFeature() }
            }
            grid {
                left = str("10%")
                right = str("10%")
                bottom = num(200)
            }
            grid {
                left = str("10%")
                right = str("10%")
                height = num(80)
                bottom = num(80)
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
            xAxis {
                type = AxisType.Category
                gridIndex = 1
                categories(*candleCategories(rows))
                boundaryGap = BoundaryGap.of(false)
                axisLine { onZero = false }
                axisTick { show = false }
                splitLine { show = false }
                axisLabel { show = false }
                min = str("dataMin")
                max = str("dataMax")
            }
            yAxis {
                scale = true
                splitArea { show = true }
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
            dataZoom {
                type = DataZoomType.Inside
                xAxisIndex = listOf(0, 1)
                start = 10.0
                end = 100.0
            }
            dataZoom {
                show = true
                type = DataZoomType.Slider
                xAxisIndex = listOf(0, 1)
                bottom = num(10)
                start = 10.0
                end = 100.0
            }
            series {
                candlestick {
                    data = candleRows(rows)
                    itemStyle =
                        ItemStyleOption().apply {
                            color = "#ec0000".asColor()
                            borderColor = "#8A0000".asColor()
                        }
                }
                bar {
                    name = "Volumn"
                    xAxisIndex = 1
                    yAxisIndex = 1
                    itemStyle = ItemStyleOption().apply { color = "#7fbe9e".asColor() }
                    large = true
                    data = rows.map { valueOf(it.volume) }
                }
            }
        }
