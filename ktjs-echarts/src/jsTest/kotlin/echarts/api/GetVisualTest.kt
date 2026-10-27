package echarts.api

import echarts.ModelFinder
import echarts.asText
import echarts.createChart
import echarts.destroyChart
import echarts.finder
import echarts.options.DataZoomOption
import echarts.options.EChartsOption
import echarts.options.DataZoomValue
import echarts.options.IndexOrIndexArray
import echarts.options.ItemStyleOption
import echarts.options.ScatterDataItem
import echarts.options.ScatterDataItemOption
import echarts.options.OptionDataValue
import echarts.options.OptionDataValueOrArray
import echarts.options.SeriesOption
import echarts.options.StringOrStringArray
import echarts.options.SymbolShape
import echarts.options.SymbolSize
import echarts.options.VisualMapOption
import echarts.options.VisualOptionUnit
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.points
import echarts.options.scatterSeries
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import zrender.ZRColor

/**
 * Translated from `test/ut/spec/api/getVisual.test.ts` of the upstream apache/echarts repository.
 *
 * The upstream `data` arrays mix plain `[x, y]` tuples with item objects that carry their own
 * `itemStyle`; the wrapper has no per-series data item type yet, so [coloredPoint] describes that
 * shape locally.
 */
class GetVisualTest {
    @Test
    fun scatter() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis = arrayOf(unsafeJso<XAxisOption> { })
                    yAxis = arrayOf(unsafeJso<YAxisOption> { })
                    color = arrayOf(ZRColor("#000"), ZRColor("#111"), ZRColor("#222"))
                    visualMap = arrayOf(
                        unsafeJso<VisualMapOption> {
                            seriesIndex = IndexOrIndexArray(3.0)
                            dimension = 1.0
                            min = 0.0
                            max = 10000.0
                            inRange = unsafeJso<VisualOptionUnit> { color = StringOrStringArray("red") }
                        },
                    )
                    series = arrayOf<SeriesOption>(
                        scatterSeries {
                            id = "k0"
                            typedData = points(doubleArrayOf(1000.0, 700.0), doubleArrayOf(333.0, 222.0))
                        },
                        scatterSeries {
                            id = "k1"
                            typedData = points(doubleArrayOf(10.0, 7.0))
                            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#fff") }
                        },
                        scatterSeries {
                            id = "k2"
                            typedData = arrayOf(
                                plainPoint(10.0, 7.0),
                                coloredPoint(333.0, 222.0, "#ff0"),
                            )
                            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#eee") }
                        },
                        scatterSeries {
                            id = "k3"
                            typedData = arrayOf(
                                plainPoint(10.0, 7.0),
                                coloredPoint(333.0, 9999.0, "red"),
                            )
                            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#eee") }
                        },
                    )
                },
            )

            assertEquals("#000", chart.getVisual(finder { dataIndex = 1.0 }, "color").asText())

            assertEquals("#fff", chart.getVisual(finder { dataIndex = 0.0; seriesIndex = 1.0 }, "color").asText())
            assertEquals("#fff", chart.getVisual(finder { seriesIndex = 1.0 }, "color").asText())

            assertEquals("#ff0", chart.getVisual(finder { dataIndex = 1.0; seriesId = "k2" }, "color").asText())
            assertEquals("#eee", chart.getVisual(finder { seriesId = "k2" }, "color").asText())

            assertEquals("red", chart.getVisual(finder { dataIndex = 1.0; seriesId = "k3" }, "color").asText())
            assertEquals("#eee", chart.getVisual(finder { seriesId = "k3" }, "color").asText())
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun dataZoom() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis = arrayOf(unsafeJso<XAxisOption> { })
                    yAxis = arrayOf(unsafeJso<YAxisOption> { })
                    color = arrayOf(ZRColor("#000"), ZRColor("#111"), ZRColor("#222"))
                    dataZoom = arrayOf(
                        unsafeJso<DataZoomOption> {
                            xAxisIndex = IndexOrIndexArray(0.0)
                            startValue = DataZoomValue(45.0)
                        },
                    )
                    series = arrayOf<SeriesOption>(
                        scatterSeries {
                            id = "k2"
                            typedData = arrayOf(
                                plainPoint(10.0, 7.0),
                                plainPoint(20.0, 7.0),
                                plainPoint(30.0, 7.0),
                                plainPoint(40.0, 7.0),
                                coloredPoint(50.0, 222.0, "#ff0"),
                            )
                            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#eee") }
                        },
                    )
                },
            )

            assertEquals("#ff0", chart.getVisual(finder { dataIndex = 4.0; seriesId = "k2" }, "color").asText())
            assertEquals(
                "#ff0",
                chart.getVisual(finder { dataIndexInside = 0.0; seriesId = "k2" }, "color").asText(),
            )
            assertEquals("#eee", chart.getVisual(finder { dataIndex = 1.0; seriesId = "k2" }, "color").asText())
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun symbolAndSymbolSizeAreReadable() {
        // Upstream marks this as TODO ("symbol and symbolSize should be tested"); the wrapper
        // exercises both so the `VisualValue` union is covered on the non-color branches too.
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis = arrayOf(unsafeJso<XAxisOption> { })
                    yAxis = arrayOf(unsafeJso<YAxisOption> { })
                    series = arrayOf<SeriesOption>(
                        scatterSeries {
                            typedData = points(doubleArrayOf(1.0, 2.0))
                            symbol = SymbolShape("diamond")
                            symbolSize = SymbolSize(12.0)
                        },
                    )
                },
            )

            assertEquals("diamond", chart.getVisual(ModelFinder("series"), "symbol").asText())
            assertEquals(12.0, chart.getVisual(ModelFinder("series"), "symbolSize").unsafeCast<Double>())
        } finally {
            destroyChart(chart, container)
        }
    }
}

/**
 * The data item shape used by the upstream `getVisual` cases: a value tuple plus its own style.
 *
 * Upstream declares this as `ScatterDataItemOption`, which the wrapper now models.
 */
private fun plainPoint(x: Double, y: Double): ScatterDataItem? =
    ScatterDataItem(arrayOf<OptionDataValue?>(OptionDataValue(x), OptionDataValue(y)))

private fun coloredPoint(x: Double, y: Double, color: String): ScatterDataItem? =
    unsafeJso<ScatterDataItemOption> {
        typedValue = OptionDataValueOrArray(arrayOf<OptionDataValue?>(OptionDataValue(x), OptionDataValue(y)))
        itemStyle = unsafeJso<ItemStyleOption> { this.color = ZRColor(color) }
    }.unsafeCast<ScatterDataItem?>()
