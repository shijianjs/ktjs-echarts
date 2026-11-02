package demo.examples.candlestick

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import demo.examples.Category

private val candlestickSimpleCategory = Category(Groups.CANDLESTICK, "examples/candlestick/CandlestickSimple.kt")

val candlestickSimpleEntry: CatalogEntry =
    candlestickSimpleCategory.chart("candlestick-simple", "基础 K 线图", "Basic Candlestick") {
            unsafeJso<EChartsOption> {
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            data = strings("2017-10-24", "2017-10-25", "2017-10-26", "2017-10-27")
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> {})
                series =
                    arrayOf<SeriesOption>(
                        candlestickSeries {
                            typedData =
                                dataItems(
                                    candle(20.0, 34.0, 10.0, 38.0),
                                    candle(40.0, 35.0, 30.0, 50.0),
                                    candle(31.0, 38.0, 33.0, 44.0),
                                    candle(38.0, 15.0, 5.0, 42.0),
                                )
                        },
                    )
            }
        }
