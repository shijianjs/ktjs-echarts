package echarts.options

import echarts.AxisTypes
import echarts.SeriesTypes
import echarts.createChart
import echarts.destroyChart
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertSame
import kotlin.test.assertTrue
import zrender.ZRColor

/**
 * Tests for the per-series `typedData` views and the read-only `SeriesOption.data` view.
 *
 * Upstream narrows `data` in every concrete series, which Kotlin cannot do on an overridden `var`.
 * The wrapper therefore keeps a read-only generic view on [SeriesOption] and gives every series its
 * own `typedData` property, mapped to the same JavaScript property by `@JsName` (see
 * `TYPE-MAPPING.md` §12). These tests pin both halves of that contract.
 */
class SeriesTypedDataTest {
    @Test
    fun typedDataAndTheReadOnlyViewShareOneJavaScriptProperty() {
        val series = lineSeries { typedData = numbers(1.0, 2.0, 3.0) }
        val generic: SeriesOption = series

        assertEquals(3, series.typedData?.size)
        assertEquals(3, generic.data?.size, "the base view reads the same JavaScript property")
        assertEquals(1.0, generic.data!![0].unsafeCast<Double>())

        series.typedData = arrayOf<LineDataItem?>(LineDataItem(OptionDataValue(9.0)))
        assertEquals(1, generic.data?.size, "writing through typedData is visible on the base view")
    }

    @Test
    fun everySeriesDeclaresATypedDataViewOverItsOwnItemType() {
        // This only compiles when every series exposes `typedData` with the item type upstream
        // declares for it, which is what pins the per-series narrowing.
        val series = arrayOf<SeriesOption>(
            lineSeries { typedData = dataItems(LineDataItem(OptionDataValue(1.0))) },
            barSeries { typedData = dataItems(BarDataItem(OptionDataValue(1.0))) },
            scatterSeries { typedData = dataItems(ScatterDataItem(OptionDataValue(1.0))) },
            effectScatterSeries { typedData = dataItems(EffectScatterDataItem(OptionDataValue(1.0))) },
            candlestickSeries { typedData = points(doubleArrayOf(1.0, 2.0, 0.5, 3.0)) },
            boxplotSeries { typedData = points(doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0)) },
            heatmapSeries { typedData = points(doubleArrayOf(0.0, 0.0, 5.0)) },
            linesSeries {
                typedData = dataItems(
                    unsafeJso<LinesDataItemOption> {
                        coords = arrayOf(arrayOf(0.0, 0.0), arrayOf(1.0, 1.0))
                    },
                )
            },
            parallelSeries { typedData = points(doubleArrayOf(1.0, 2.0)) },
            pictorialBarSeries { typedData = numbers(1.0) },
            pieSeries { typedData = dataItems(PieDataItem(OptionDataValueNumeric(1.0))) },
            funnelSeries { typedData = dataItems(FunnelDataItem(OptionDataValueNumeric(1.0))) },
            gaugeSeries { typedData = numbers(72.0) },
            radarSeries { typedData = points(doubleArrayOf(1.0, 2.0, 3.0)) },
            mapSeries { typedData = dataItems(MapDataItem(OptionDataValueNumeric(1.0))) },
            themeRiverSeries {
                typedData = dataItems(
                    themeRiverDataItem(
                        OptionDataValue("2026-01-01"),
                        OptionDataValue(1.0),
                        OptionDataValue("a"),
                    ),
                )
            },
            customSeries { typedData = numbers(1.0, 2.0) },
            treeSeries { typedData = items(unsafeJso<TreeSeriesNodeItemOption> { name = OptionId("root") }) },
            treemapSeries { typedData = items(unsafeJso<TreemapSeriesNodeItemOption> { name = OptionId("root") }) },
            sunburstSeries { typedData = items(unsafeJso<SunburstSeriesNodeItemOption> { name = OptionId("root") }) },
            graphSeries { typedData = dataItems(GraphDataItem(unsafeJso<GraphNodeItemOption> { name = "a" })) },
            chordSeries { typedData = dataItems(ChordDataItem(unsafeJso<ChordNodeItemOption> { name = "a" })) },
            sankeySeries { typedData = items(unsafeJso<SankeyNodeItemOption> { name = OptionId("a") }) },
        )

        assertEquals(23, series.size, "every series type declares a typed data view")
        for (item in series) {
            assertNotNull(item.data, "the base view keeps the data of ${item.type?.unsafeCast<String>()}")
        }
    }

    @Test
    fun unionsWrapTheValueRowAndObjectBranches() {
        assertEquals(1.0, BarDataItem(OptionDataValue(1.0)).unsafeCast<Double>())
        assertEquals(
            2,
            BarDataItem(arrayOf<OptionDataValue?>(OptionDataValue(1.0), OptionDataValue(2.0)))
                .unsafeCast<Array<*>>()
                .size,
        )

        val objectBranch = unsafeJso<BarDataItemOption> { name = OptionId("a") }
        assertSame(objectBranch, BarDataItem(objectBranch).unsafeCast<BarDataItemOption>())

        assertEquals(
            1.0,
            PieDataItem(OptionDataValueNumeric(1.0)).unsafeCast<Double>(),
            "the numeric factory keeps the raw value",
        )
    }

    @Test
    fun typedValueMapsToTheGenericValueProperty() {
        val candle = unsafeJso<CandlestickDataItemOption> {
            typedValue = arrayOf<OptionDataValue?>(
                OptionDataValue(1.0),
                OptionDataValue(2.0),
                OptionDataValue(0.5),
                OptionDataValue(3.0),
            )
            itemStyle = unsafeJso<CandlestickItemStyleOption> { color0 = ZRColor("#00ff00") }
        }

        assertEquals(4, candle.value!!.unsafeCast<Array<*>>().size, "typedValue writes the shared property")
        assertEquals("#00ff00", candle.itemStyle!!.color0!!.unsafeCast<String>())

        val line = unsafeJso<LineDataItemOption> { typedValue = OptionDataValueOrArray(1.0) }
        assertEquals(1.0, line.value!!.unsafeCast<Double>(), "the inherited value reads the same property")
    }

    @Test
    fun narrowedRotationsAcceptTheUpstreamKeywords() {
        val gauge = gaugeSeries {
            axisLabel = unsafeJso<GaugeAxisLabelOption> { typedRotate = LabelRotates.tangential }
        }
        assertEquals("tangential", gauge.axisLabel!!.typedRotate!!.unsafeCast<String>())

        val pie = pieSeries {
            typedLabel = unsafeJso<PieLabelOption> { typedRotate = PieLabelRotates.radial }
        }
        assertEquals("radial", pie.typedLabel!!.typedRotate!!.unsafeCast<String>())

        val sunburst = sunburstSeries {
            typedLabel = unsafeJso<SunburstLabelOption> { typedRotate = LabelRotates.radial }
        }
        assertEquals(
            "radial",
            sunburst.typedLabel!!.typedRotate!!.unsafeCast<String>(),
        )
    }

    @Test
    fun typedDataSurvivesARealChart() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.category })
                    yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                    series = one(
                        candlestickSeries {
                            typedData = points(
                                doubleArrayOf(1.0, 2.0, 0.5, 3.0),
                                doubleArrayOf(2.0, 3.0, 1.5, 4.0),
                            )
                        },
                    )
                },
            )

            val stored = assertNotNull(chart.getOption().series)
            assertEquals(SeriesTypes.candlestick, stored[0].type)
            assertEquals(2, stored[0].data?.size, "the read-only view sees the data after a round trip")
            assertTrue(chart.renderToCanvas().width > 0, "the chart renders the typed data")
        } finally {
            destroyChart(chart, container)
        }
    }
}
