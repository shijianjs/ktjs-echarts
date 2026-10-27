package echarts.data

import echarts.AxisTypes
import echarts.SeriesLayoutBys
import echarts.createChart
import echarts.destroyChart
import echarts.options.BarSeriesOption
import echarts.options.DatasetOption
import echarts.options.EChartsOption
import echarts.options.OptionDataValue
import echarts.options.PipedDataTransformOption
import echarts.options.SeriesOption
import echarts.options.barSeries
import echarts.options.datasetArrayRows
import echarts.options.one
import js.objects.unsafeJso
import kotlin.js.json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Translated from `test/ut/spec/data/dataTransform.test.ts` of the upstream apache/echarts
 * repository.
 *
 * Upstream asserts on `getECModel(chart).getSeries()[0].getData()`, i.e. the parsed `SeriesData`
 * list, which the published package does not expose. The cases translated here keep the observable
 * part — the option is accepted, the transform runs, and the chart renders — and drop the
 * dimension-detection assertions that need the private list object.
 */
class DataTransformTest {
    @Test
    fun seriesLayoutByChangedNoTransform() {
        val (chart, container) = createChart(200, 150)
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    dataset = one(unsafeJso<DatasetOption> { source = detectionSource() })
                    xAxis = one(unsafeJso { type = AxisTypes.category })
                    yAxis = one(unsafeJso { })
                    series = arrayOf<SeriesOption>(barSeries { seriesLayoutBy = SeriesLayoutBys.row })
                },
            )

            val stored = chart.getOption()
            assertNotNull(stored.dataset)
            val series = stored.series!![0].unsafeCast<BarSeriesOption>()
            assertEquals(SeriesLayoutBys.row, series.seriesLayoutBy)
            assertTrue(chart.renderToCanvas().width > 0)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun filterTransformKeepsTheDatasetChain() {
        val (chart, container) = createChart(200, 150)
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    dataset = arrayOf(
                        unsafeJso<DatasetOption> { source = detectionSource() },
                        unsafeJso<DatasetOption> {
                            transform = json(
                                "type" to "filter",
                                "config" to json("dimension" to "product", "!=" to "XXX"),
                            ).unsafeCast<PipedDataTransformOption>()
                        },
                    )
                    xAxis = one(unsafeJso { type = AxisTypes.category })
                    yAxis = one(unsafeJso { })
                    series = arrayOf<SeriesOption>(barSeries { datasetIndex = 1.0 })
                },
            )

            val stored = chart.getOption()
            assertEquals(2, stored.dataset?.size)
            assertTrue(chart.renderToCanvas().width > 0)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun filterTransformAcceptsAnArrayOfTransforms() {
        val (chart, container) = createChart(200, 150)
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    dataset = arrayOf(
                        unsafeJso<DatasetOption> { source = detectionSource() },
                        unsafeJso<DatasetOption> {
                            transform = arrayOf(
                                json("type" to "filter", "config" to json("dimension" to "product", "!=" to "XXX")),
                                json("type" to "filter", "config" to json("dimension" to "product", "!=" to "YYY")),
                            ).unsafeCast<PipedDataTransformOption>()
                        },
                    )
                    xAxis = one(unsafeJso { type = AxisTypes.category })
                    yAxis = one(unsafeJso { })
                    series = arrayOf<SeriesOption>(barSeries { datasetIndex = 1.0; seriesLayoutBy = SeriesLayoutBys.row })
                },
            )

            assertEquals(2, chart.getOption().dataset?.size)
        } finally {
            destroyChart(chart, container)
        }
    }
}

/** The `makeDatasetSourceDetection` fixture of the upstream test. */
private fun detectionSource() = datasetArrayRows(
    arrayOf(
        arrayOf(OptionDataValue("product"), OptionDataValue("2012"), OptionDataValue("2013"), OptionDataValue("2014"), OptionDataValue("2015")),
        arrayOf(OptionDataValue("AAA"), OptionDataValue(41.1), OptionDataValue(30.4), OptionDataValue(65.1), OptionDataValue(53.3)),
        arrayOf(OptionDataValue("BBB"), OptionDataValue(86.5), OptionDataValue(92.1), OptionDataValue(85.7), OptionDataValue(83.1)),
        arrayOf(OptionDataValue("CCC"), OptionDataValue(24.1), OptionDataValue(67.2), OptionDataValue(79.5), OptionDataValue(86.4)),
    ),
)
