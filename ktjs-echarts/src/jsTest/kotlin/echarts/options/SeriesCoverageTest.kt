package echarts.options

import echarts.EChartsInstance
import echarts.SeriesLayoutBys
import echarts.SeriesType
import echarts.SeriesTypes
import echarts.createChart
import echarts.destroyChart
import echarts.options.one
import echarts.registerRectangleMap
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Acceptance coverage for every series type modelled by the wrapper.
 *
 * The upstream unit suite only covers `bar`, `custom` and `aria` series, and those cases assert on
 * `getECModel(chart)` internals. This file covers the wrapper-specific part instead: every series
 * factory produces an option that ECharts accepts, keeps its `type` after the option round trip,
 * and renders.
 */
class SeriesCoverageTest {
    @Test
    fun cartesianSeriesAreAccepted() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis = one(unsafeJso<XAxisOption> { type = echarts.AxisTypes.category; data = numbers(1.0, 2.0, 3.0) })
                    yAxis = one(unsafeJso<YAxisOption> { type = echarts.AxisTypes.value })
                    series = arrayOf<SeriesOption>(
                        lineSeries { typedData = numbers(1.0, 2.0, 3.0) },
                        barSeries { typedData = numbers(1.0, 2.0, 3.0) },
                        scatterSeries { typedData = points(doubleArrayOf(1.0, 1.0)) },
                        effectScatterSeries { typedData = points(doubleArrayOf(2.0, 2.0)) },
                        candlestickSeries { typedData = points(doubleArrayOf(1.0, 2.0, 0.5, 3.0)) },
                        boxplotSeries { typedData = points(doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0)) },
                        pictorialBarSeries { typedData = numbers(1.0, 2.0, 3.0) },
                    )
                },
            )

            assertSeriesTypes(
                chart,
                SeriesTypes.line,
                SeriesTypes.bar,
                SeriesTypes.scatter,
                SeriesTypes.effectScatter,
                SeriesTypes.candlestick,
                SeriesTypes.boxplot,
                SeriesTypes.pictorialBar,
            )
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun heatmapSeriesIsAccepted() {
        val (chart, container) = createChart()
        try {
            // A cartesian heatmap needs two category axes.
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis = one(unsafeJso<XAxisOption> { type = echarts.AxisTypes.category; data = numbers(1.0, 2.0) })
                    yAxis = one(unsafeJso<YAxisOption> { type = echarts.AxisTypes.category; data = numbers(1.0, 2.0) })
                    visualMap = one(unsafeJso<VisualMapOption> { show = false; min = 0.0; max = 10.0 })
                    series = one(heatmapSeries { typedData = points(doubleArrayOf(0.0, 0.0, 5.0)) })
                },
            )

            assertSeriesTypes(chart, SeriesTypes.heatmap)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun partSeriesAreAccepted() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    series = arrayOf<SeriesOption>(
                        pieSeries { typedData = items(unsafeJso<OptionDataItemObject> { name = OptionId("a"); value = OptionDataValue(1.0) }) },
                        funnelSeries { typedData = numbers(1.0, 2.0) },
                        gaugeSeries { typedData = numbers(1.0) },
                    )
                },
            )

            assertSeriesTypes(chart, SeriesTypes.pie, SeriesTypes.funnel, SeriesTypes.gauge)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun radarSeriesIsAccepted() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    radar = one(
                        unsafeJso<RadarOption> {
                            indicator = arrayOf(
                                unsafeJso<RadarIndicator> { name = "a"; max = 10.0 },
                                unsafeJso<RadarIndicator> { name = "b"; max = 10.0 },
                            )
                        },
                    )
                    series = one(radarSeries { typedData = items(unsafeJso<OptionDataItemObject> { value = OptionDataValue(1.0) }) })
                },
            )

            assertSeriesTypes(chart, SeriesTypes.radar)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun mapSeriesIsAccepted() {
        registerRectangleMap("coverage-map", 0.0, 0.0, 100.0, 100.0)

        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    series = one(mapSeries { map = "coverage-map"; typedData = numbers(1.0) })
                },
            )

            assertSeriesTypes(chart, SeriesTypes.map)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun hierarchySeriesAreAccepted() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    series = arrayOf<SeriesOption>(
                        treeSeries {
                            typedData = items(
                                unsafeJso<TreeSeriesNodeItemOption> {
                                    name = OptionId("root")
                                    children = arrayOf(unsafeJso { name = OptionId("leaf") })
                                },
                            )
                        },
                        treemapSeries {
                            typedData = items(
                                unsafeJso<TreemapSeriesNodeItemOption> {
                                    name = OptionId("root")
                                    value = OptionDataValue(1.0)
                                    children = arrayOf(unsafeJso { name = OptionId("leaf"); value = OptionDataValue(1.0) })
                                },
                            )
                        },
                        sunburstSeries {
                            typedData = items(
                                unsafeJso<SunburstSeriesNodeItemOption> {
                                    name = OptionId("root")
                                    children = arrayOf(unsafeJso { name = OptionId("leaf"); value = OptionDataValue(1.0) })
                                },
                            )
                        },
                    )
                },
            )

            assertSeriesTypes(chart, SeriesTypes.tree, SeriesTypes.treemap, SeriesTypes.sunburst)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun relationSeriesAreAccepted() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    series = arrayOf<SeriesOption>(
                        graphSeries {
                            typedData = arrayOf(
                                unsafeJso<GraphNodeItemOption> { name = "a"; x = 0.0; y = 0.0 }.asGraphDataItem(),
                                unsafeJso<GraphNodeItemOption> { name = "b"; x = 1.0; y = 1.0 }.asGraphDataItem(),
                            )
                            links = arrayOf(unsafeJso<GraphEdgeItemOption> { source = StringOrDouble("a"); target = StringOrDouble("b") })
                        },
                        sankeySeries {
                            typedData = items(
                                unsafeJso<SankeyNodeItemOption> { name = OptionId("a") },
                                unsafeJso<SankeyNodeItemOption> { name = OptionId("b") },
                            )
                            links = arrayOf(unsafeJso<SankeyEdgeItemOption> { source = StringOrDouble("a"); target = StringOrDouble("b"); value = OptionDataValue(1.0) })
                        },
                        chordSeries {
                            typedData = arrayOf(
                                unsafeJso<ChordNodeItemOption> { name = "a" }.asChordDataItem(),
                                unsafeJso<ChordNodeItemOption> { name = "b" }.asChordDataItem(),
                            )
                            links = arrayOf(unsafeJso<ChordEdgeItemOption> { source = StringOrDouble("a"); target = StringOrDouble("b"); value = OptionDataValue(1.0) })
                        },
                    )
                },
            )

            assertSeriesTypes(chart, SeriesTypes.graph, SeriesTypes.sankey, SeriesTypes.chord)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun themeRiverSeriesIsAccepted() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    singleAxis = one(unsafeJso<SingleAxisOption> { type = echarts.AxisTypes.time })
                    series = one(
                        themeRiverSeries {
                            typedData = arrayOf(
                                themeRiverDataItem(OptionDataValue("2026-01-01"), OptionDataValue(1.0), OptionDataValue("a")),
                                themeRiverDataItem(OptionDataValue("2026-01-02"), OptionDataValue(2.0), OptionDataValue("a")),
                            )
                        },
                    )
                },
            )

            assertSeriesTypes(chart, SeriesTypes.themeRiver)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun linesSeriesIsAccepted() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis = one(unsafeJso<XAxisOption> { type = echarts.AxisTypes.value })
                    yAxis = one(unsafeJso<YAxisOption> { type = echarts.AxisTypes.value })
                    series = one(
                        linesSeries {
                            coordinateSystem = "cartesian2d"
                            typedData = arrayOf(
                                unsafeJso<LinesDataItemOption> {
                                    coords = arrayOf(arrayOf(0.0, 0.0), arrayOf(10.0, 10.0))
                                },
                            )
                        },
                    )
                },
            )

            assertSeriesTypes(chart, SeriesTypes.lines)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun customSeriesIsAccepted() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis = one(unsafeJso<XAxisOption> { type = echarts.AxisTypes.category })
                    yAxis = one(unsafeJso<YAxisOption> { type = echarts.AxisTypes.value })
                    series = one(
                        customSeries {
                            typedData = points(doubleArrayOf(0.0, 1.0), doubleArrayOf(1.0, 2.0))
                            // A render item that draws nothing still has to be accepted.
                            renderItem = { _, _ -> null }
                        },
                    )
                },
            )

            assertSeriesTypes(chart, SeriesTypes.custom)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun parallelSeriesIsAccepted() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    parallel = one(unsafeJso<ParallelOption> { })
                    parallelAxis = arrayOf(unsafeJso<ParallelAxisOption> { })
                    series = one(parallelSeries { typedData = points(doubleArrayOf(1.0, 2.0)) })
                },
            )

            assertSeriesTypes(chart, SeriesTypes.parallel)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun seriesExposeTheCoordinateMixinsUpstreamDeclares() {
        // The assignments only type-check when the series really implements the mixin upstream
        // declares for it, so this pins the inheritance graph.
        val cartesian: List<SeriesOnCartesianOptionMixin> = listOf(
            lineSeries(), barSeries(), scatterSeries(), candlestickSeries(), boxplotSeries(),
            heatmapSeries(), linesSeries(), customSeries(), pieSeries(), graphSeries(), chordSeries(),
        )
        val polar: List<SeriesOnPolarOptionMixin> = listOf(
            lineSeries(), barSeries(), scatterSeries(), linesSeries(), customSeries(), graphSeries(), chordSeries(),
        )
        val geo: List<SeriesOnGeoOptionMixin> = listOf(
            scatterSeries(), heatmapSeries(), linesSeries(), customSeries(), pieSeries(), mapSeries(),
            graphSeries(), chordSeries(),
        )
        val single: List<SeriesOnSingleOptionMixin> = listOf(
            scatterSeries(), customSeries(), themeRiverSeries(), graphSeries(), chordSeries(),
        )
        val calendar: List<ComponentOnCalendarOptionMixin> = listOf(
            scatterSeries(), heatmapSeries(), linesSeries(), parallelSeries(), customSeries(), pieSeries(),
            funnelSeries(), gaugeSeries(), mapSeries(), graphSeries(), chordSeries(), sankeySeries(),
            treeSeries(), treemapSeries(), sunburstSeries(),
        )
        val matrix: List<ComponentOnMatrixOptionMixin> = listOf(
            scatterSeries(), heatmapSeries(), linesSeries(), parallelSeries(), customSeries(), pieSeries(),
            funnelSeries(), gaugeSeries(), mapSeries(), graphSeries(), chordSeries(), sankeySeries(),
            treeSeries(), treemapSeries(), sunburstSeries(),
        )
        val radar: List<SeriesOnRadarOptionMixin> = listOf(radarSeries())

        assertEquals(11, cartesian.size)
        assertEquals(7, polar.size)
        assertEquals(8, geo.size)
        assertEquals(5, single.size)
        assertEquals(15, calendar.size)
        assertEquals(15, matrix.size)
        assertEquals(1, radar.size)

        // The mixin members are the ones upstream declares.
        val scatter = scatterSeries()
        scatter.xAxisIndex = 0.0
        scatter.xAxisId = "x0"
        scatter.yAxisIndex = 0.0
        scatter.yAxisId = "y0"
        scatter.polarIndex = 0.0
        scatter.polarId = "polar0"
        scatter.geoIndex = 0.0
        scatter.geoId = "geo0"
        scatter.singleAxisIndex = 0.0
        scatter.singleAxisId = "single0"
        scatter.calendarIndex = 0.0
        scatter.calendarId = "calendar0"
        scatter.matrixIndex = 0.0
        scatter.matrixId = "matrix0"
        assertEquals("geo0", scatter.geoId)
    }

    @Test
    fun coordinateIdsSurviveTheOptionRoundTrip() {
        registerRectangleMap("coverage-ids", 0.0, 0.0, 100.0, 100.0)

        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    grid = one(unsafeJso<GridOption> { id = "grid0" })
                    xAxis = one(unsafeJso<XAxisOption> { id = "x0"; type = echarts.AxisTypes.category; gridId = "grid0" })
                    yAxis = one(unsafeJso<YAxisOption> { id = "y0"; type = echarts.AxisTypes.value; gridId = "grid0" })
                    geo = one(unsafeJso<GeoOption> { id = "geo0"; map = "coverage-ids" })
                    series = arrayOf<SeriesOption>(
                        scatterSeries { xAxisId = "x0"; yAxisId = "y0"; typedData = points(doubleArrayOf(1.0, 2.0)) },
                        scatterSeries { coordinateSystem = "geo"; geoId = "geo0" },
                    )
                },
            )

            val stored = chart.getOption().series!!
            val cartesian = stored[0].unsafeCast<ScatterSeriesOption>()
            assertEquals("x0", cartesian.xAxisId)
            assertEquals("y0", cartesian.yAxisId)
            val geo = stored[1].unsafeCast<ScatterSeriesOption>()
            assertEquals("geo0", geo.geoId)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun seriesFieldsSurviveTheOptionRoundTrip() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis = one(unsafeJso<XAxisOption> { type = echarts.AxisTypes.category; data = numbers(1.0, 2.0) })
                    yAxis = one(unsafeJso<YAxisOption> { type = echarts.AxisTypes.value })
                    series = arrayOf<SeriesOption>(
                        lineSeries {
                            id = "line"
                            name = "line-name"
                            stack = "total"
                            smooth = BooleanOrDouble(true)
                            showSymbol = false
                            typedData = numbers(1.0, 2.0)
                        },
                        barSeries {
                            id = "bar"
                            name = "bar-name"
                            barWidth = StringOrDouble("40%")
                            seriesLayoutBy = SeriesLayoutBys.column
                            typedData = numbers(2.0, 1.0)
                        },
                    )
                },
            )

            val stored = chart.getOption().series!!
            val line = stored[0].unsafeCast<LineSeriesOption>()
            assertEquals("line", line.id)
            assertEquals("line-name", line.name)
            assertEquals("total", line.stack)
            assertEquals(true, line.smooth?.unsafeCast<Boolean>())
            assertEquals(false, line.showSymbol)

            val bar = stored[1].unsafeCast<BarSeriesOption>()
            assertEquals("bar", bar.id)
            assertEquals("bar-name", bar.name)
            assertEquals("40%", bar.barWidth?.unsafeCast<String>())
            assertEquals(SeriesLayoutBys.column, bar.seriesLayoutBy)
        } finally {
            destroyChart(chart, container)
        }
    }
}

private fun assertSeriesTypes(chart: EChartsInstance, vararg expected: SeriesType?) {
    val stored = chart.getOption().series
    assertNotNull(stored, "the chart should keep its series")
    assertEquals(expected.toList(), stored.map { it.type })
    assertTrue(chart.renderToCanvas().width > 0, "the chart should render")
}

/**
 * `GraphNodeItemOption` keeps its own `value` instead of extending `OptionDataItemObject`, so graph
 * needs an explicit cast — the same thing upstream does with its `GraphNodeItemOption` union.
 */
private fun JsAny.asGraphDataItem(): GraphDataItem? = unsafeCast<GraphDataItem?>()

private fun JsAny.asChordDataItem(): ChordDataItem? = unsafeCast<ChordDataItem?>()
