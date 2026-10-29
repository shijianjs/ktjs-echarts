package echarts.options

import echarts.AxisTypes
import echarts.SeriesTypes
import echarts.createChart
import echarts.destroyChart
import js.buffer.ArrayBuffer
import js.objects.unsafeJso
import js.typedarrays.Float32Array
import js.typedarrays.Float64Array
import kotlin.js.JsAny
import kotlin.js.js
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

        val bar =
            unsafeJso<BarDataItemOption> {
                typedValue =
                    OptionDataValueOrArray(
                        arrayOf(OptionDataValue(1_700_000_000_000.0), OptionDataValue(12.5)),
                    )
            }
        assertEquals(
            2,
            bar.value!!.unsafeCast<Array<*>>().size,
            "a bar on a time axis carries its [time, value] row through the shared property",
        )
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
    fun scatterDataCanBeAFlatTypedArray() {
        // Upstream types scatter data as `(...)[] | ArrayLike<number>`; the flat branch is a typed
        // array of consecutive `[x, y]` rows and needs `dimensions`.
        val flat = js("new Float32Array([1, 2, 3, 4])").unsafeCast<Float32Array<ArrayBuffer>>()
        val series = scatterSeries {
            dimensions = arrayOf(DimensionDefinitionLoose("x"), DimensionDefinitionLoose("y"))
            typedFlatData = flat
            large = true
            largeThreshold = 500.0
        }

        assertSame(flat.unsafeCast<JsAny>(), series.typedFlatData!!.unsafeCast<JsAny>())

        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.value })
                    yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                    this.series = one<SeriesOption>(series)
                },
            )

            val stored = assertNotNull(chart.getOption().series)
            assertEquals(4, stored[0].data?.size, "the flat array is the data of the series")
            assertTrue(chart.renderToCanvas().width > 0, "the chart renders the flat data")
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun linesDataCanBeAFlatTypedArray() {
        // Upstream types lines data as `LinesDataItemOption[] | ArrayLike<number>`; a polylines row
        // is `[count, x0, y0, x1, y1, ...]`, which is what the million-street page appends from a
        // binary file.
        val flat = js("new Float64Array([3, 0, 0, 1, 2, 2, 0, 2, 1, 1, 3, 4])").unsafeCast<Float64Array<ArrayBuffer>>()
        val series = linesSeries {
            polyline = true
            dimensions = arrayOf(DimensionDefinitionLoose("value"))
            typedFlatData = flat
            large = true
        }

        assertSame(flat.unsafeCast<JsAny>(), series.typedFlatData!!.unsafeCast<JsAny>())

        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.value })
                    yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                    this.series = one<SeriesOption>(series)
                },
            )

            val stored = assertNotNull(chart.getOption().series)
            assertEquals(2, stored[0].data?.size, "the flat array is read as two polylines")
            assertTrue(chart.renderToCanvas().width > 0, "the chart renders the flat polylines")
        } finally {
            destroyChart(chart, container)
        }
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

    /**
     * Upstream `CircleLayoutOptionMixin` types `center` as `(string | number)[] | string | number`:
     * a pie on a geo coordinate system may be centered on a region by name. [PieSeriesOption.center]
     * keeps the array branch, [PieSeriesOption.typedCenter] the scalar one, and both address the
     * same JavaScript property.
     */
    @Test
    fun pieCenterHasAnArrayAndAScalarBranch() {
        val pie = pieSeries { typedCenter = StringOrDouble("Vestfirðir") }

        assertEquals("Vestfirðir", pie.typedCenter!!.unsafeCast<String>())
        assertEquals(
            "Vestfirðir",
            pie.center!!.unsafeCast<String>(),
            "both properties address the same JavaScript property",
        )

        pie.center = arrayOf(StringOrDouble("50%"), StringOrDouble("50%"))
        assertEquals(2, pie.typedCenter!!.unsafeCast<Array<*>>().size, "writing the array branch is visible")
    }

    @Test
    fun theCandlestickItemStyleViewSharesTheBaseProperty() {
        // Upstream narrows `CandlestickSeriesOption.itemStyle` to the item style that adds the
        // falling colors; `typedItemStyle` is that view, mapped to the same JavaScript property.
        val series = candlestickSeries {
            typedItemStyle = unsafeJso<CandlestickItemStyleOption> {
                color = ZRColor("#ec0000")
                color0 = ZRColor("#00da3c")
                borderColor = ZRColor("#8A0000")
                borderColor0 = ZRColor("#008F28")
                borderColorDoji = ZRColor("#000")
            }
        }

        assertEquals("#00da3c", series.typedItemStyle!!.color0!!.unsafeCast<String>())
        assertTrue(series.typedItemStyle === series.itemStyle, "both properties address one object")

        series.itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#123456") }
        assertEquals(
            "#123456",
            series.typedItemStyle!!.color!!.unsafeCast<String>(),
            "writing through the base view is visible on the typed view",
        )
    }
}
