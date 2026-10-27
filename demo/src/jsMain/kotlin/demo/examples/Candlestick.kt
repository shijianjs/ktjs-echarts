package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.AxisTypes
import echarts.options.CandlestickDataItem
import echarts.options.EChartsOption
import echarts.options.SeriesOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.candlestickSeries
import echarts.options.dataItems
import echarts.options.one
import echarts.options.strings
import js.objects.unsafeJso

/*
 * Candlestick examples ported from `public/examples/ts/candlestick-*.ts` of apache/echarts-examples.
 *
 * `candlestick-sh`, `candlestick-sh-2015` and `candlestick-touch` are not ported: the first two read
 * a 26 KB table, the third one needs a real touch pointer.
 */

private val candlestick = Category(Groups.CANDLESTICK, "examples/Candlestick.kt")

/** One `[open, close, lowest, highest]` row. */
private fun candle(open: Double, close: Double, lowest: Double, highest: Double): CandlestickDataItem =
    CandlestickDataItem(row(open, close, lowest, highest))

val candlestickEntries: List<CatalogEntry> =
    listOf(
        candlestick.chart("candlestick-simple", "基础 K 线图", "Basic Candlestick") {
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
        },
    )
