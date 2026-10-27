package echarts.api

import echarts.AxisTypes
import echarts.ModelFinder
import echarts.OuterBoundsModes
import echarts.asList
import echarts.assertPointEquals
import echarts.asScalar
import echarts.assertScalarEquals
import echarts.coordinate
import echarts.createChart
import echarts.destroyChart
import echarts.finder
import echarts.options.AxisMinMax
import echarts.options.DoubleOrDoubleArray
import echarts.options.EChartsOption
import echarts.options.GeoOption
import echarts.options.GraphNodeItemOption
import echarts.options.GridOption
import echarts.options.GraphDataItem
import echarts.options.OptionDataValueOrArray
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.graphSeries
import echarts.options.mapSeries
import echarts.options.points
import echarts.options.scatterSeries
import echarts.registerRectangleMap
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.test.Test

/**
 * Translated from `test/ut/spec/api/converter.test.ts` of the upstream apache/echarts repository.
 *
 * The upstream `testGeoJson1` / `testGeoJson2` fixtures are registered through
 * [registerRectangleMap]. Coordinates are compared with the same `1e-3` tolerance upstream uses.
 */
class ConverterTest {
    private val chartWidth = 500
    private val chartHeight = 400

    @Test
    fun geo() {
        // Upstream registers the fixtures at module scope, before any chart exists.
        registerRectangleMap("converter-geo-1", 2000.0, 3000.0, 5000.0, 8000.0)
        registerRectangleMap("converter-geo-2", 200.0, 300.0, 500.0, 800.0)

        val (chart, container) = createChart(chartWidth, chartHeight)
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    geo = arrayOf(
                        unsafeJso<GeoOption> {
                            id = "aa"
                            left = StringOrDouble(10.0)
                            right = StringOrDouble(20.0)
                            top = StringOrDouble(30.0)
                            bottom = StringOrDouble(40.0)
                            map = "converter-geo-1"
                        },
                        unsafeJso<GeoOption> {
                            id = "bb"
                            left = StringOrDouble(10.0)
                            right = StringOrDouble(20.0)
                            top = StringOrDouble(30.0)
                            bottom = StringOrDouble(40.0)
                            map = "converter-geo-2"
                        },
                    )
                    series = arrayOf<SeriesOption>(
                        scatterSeries {
                            id = "k1"
                            coordinateSystem = "geo"
                            geoIndex = 1.0
                        },
                        scatterSeries {
                            id = "k2"
                            coordinateSystem = "geo"
                        },
                        // Should not be affected by map.
                        mapSeries {
                            id = "m1"
                            map = "converter-geo-1"
                            left = StringOrDouble(10.0)
                            right = StringOrDouble(20.0)
                            top = StringOrDouble(30.0)
                            bottom = StringOrDouble(40.0)
                        },
                    )
                },
            )

            val width = chart.getWidth()
            val height = chart.getHeight()

            assertPointEquals(
                listOf(width - 20, height - 40),
                chart.convertToPixel(ModelFinder("geo"), coordinate(5000.0, 3000.0)).asList(),
            )
            assertPointEquals(
                listOf(5000.0, 3000.0),
                chart.convertFromPixel(ModelFinder("geo"), DoubleOrDoubleArray(arrayOf(width - 20, height - 40))).asList(),
            )

            assertPointEquals(
                listOf(width - 20, 30.0),
                chart.convertToPixel(finder { geoIndex = 1.0 }, coordinate(500.0, 800.0)).asList(),
            )
            assertPointEquals(
                listOf(500.0, 800.0),
                chart.convertFromPixel(finder { geoIndex = 1.0 }, DoubleOrDoubleArray(arrayOf(width - 20, 30.0))).asList(),
            )

            assertPointEquals(
                listOf(10.0, height - 40),
                chart.convertToPixel(finder { geoId = "bb" }, coordinate(200.0, 300.0)).asList(),
            )
            assertPointEquals(
                listOf(200.0, 300.0),
                chart.convertFromPixel(finder { geoId = "bb" }, DoubleOrDoubleArray(arrayOf(10.0, height - 40))).asList(),
            )

            assertPointEquals(
                listOf(10.0, 30.0),
                chart.convertToPixel(finder { seriesIndex = 0.0 }, coordinate(200.0, 800.0)).asList(),
            )
            assertPointEquals(
                listOf(200.0, 800.0),
                chart.convertFromPixel(finder { seriesIndex = 0.0 }, DoubleOrDoubleArray(arrayOf(10.0, 30.0))).asList(),
            )

            assertPointEquals(
                listOf(10.0, 30.0),
                chart.convertToPixel(finder { seriesId = "k2" }, coordinate(2000.0, 8000.0)).asList(),
            )
            assertPointEquals(
                listOf(2000.0, 8000.0),
                chart.convertFromPixel(finder { seriesId = "k2" }, DoubleOrDoubleArray(arrayOf(10.0, 30.0))).asList(),
            )
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun map() {
        registerRectangleMap("converter-map-1", 2000.0, 3000.0, 5000.0, 8000.0)
        registerRectangleMap("converter-map-2", 200.0, 300.0, 500.0, 800.0)

        val (chart, container) = createChart(chartWidth, chartHeight)
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    geo = arrayOf(
                        // Should not be affected by geo
                        unsafeJso<GeoOption> {
                            id = "aa"
                            left = StringOrDouble(10.0)
                            right = StringOrDouble(20.0)
                            top = StringOrDouble(30.0)
                            bottom = StringOrDouble(40.0)
                            map = "converter-map-1"
                        },
                    )
                    series = arrayOf<SeriesOption>(
                        mapSeries {
                            id = "k1"
                            map = "converter-map-1"
                            left = StringOrDouble(10.0)
                            right = StringOrDouble(20.0)
                            top = StringOrDouble(30.0)
                            bottom = StringOrDouble(40.0)
                        },
                        mapSeries {
                            id = "k2"
                            map = "converter-map-2"
                            left = StringOrDouble(10.0)
                            right = StringOrDouble(20.0)
                            top = StringOrDouble(30.0)
                            bottom = StringOrDouble(40.0)
                        },
                    )
                },
            )

            assertPointEquals(
                listOf(10.0, 30.0),
                chart.convertToPixel(finder { seriesIndex = 0.0 }, coordinate(2000.0, 8000.0)).asList(),
            )
            assertPointEquals(
                listOf(2000.0, 8000.0),
                chart.convertFromPixel(finder { seriesIndex = 0.0 }, DoubleOrDoubleArray(arrayOf(10.0, 30.0))).asList(),
            )

            assertPointEquals(
                listOf(10.0, 30.0),
                chart.convertToPixel(finder { seriesId = "k2" }, coordinate(200.0, 800.0)).asList(),
            )
            assertPointEquals(
                listOf(200.0, 800.0),
                chart.convertFromPixel(finder { seriesId = "k2" }, DoubleOrDoubleArray(arrayOf(10.0, 30.0))).asList(),
            )
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun cartesian() {
        registerRectangleMap("converter-cartesian", 2000.0, 3000.0, 5000.0, 8000.0)

        val (chart, container) = createChart(chartWidth, chartHeight)
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    geo = arrayOf(
                        // Should not affect grid converter.
                        unsafeJso<GeoOption> { map = "converter-cartesian" },
                    )
                    grid = arrayOf(
                        unsafeJso<GridOption> {
                            id = "g0"
                            left = StringOrDouble(10.0)
                            right = StringOrDouble("50%")
                            top = StringOrDouble(30.0)
                            bottom = StringOrDouble(40.0)
                            containLabel = false
                            outerBoundsMode = OuterBoundsModes.none
                        },
                        unsafeJso<GridOption> {
                            id = "g1"
                            left = StringOrDouble("50%")
                            right = StringOrDouble(20.0)
                            top = StringOrDouble(30.0)
                            bottom = StringOrDouble(40.0)
                            containLabel = false
                            outerBoundsMode = OuterBoundsModes.none
                        },
                    )
                    xAxis = arrayOf(
                        unsafeJso<XAxisOption> {
                            id = "x0"
                            type = AxisTypes.value
                            min = AxisMinMax(-500.0)
                            max = AxisMinMax(3000.0)
                            gridId = "g0"
                        },
                        unsafeJso<XAxisOption> {
                            id = "x1"
                            type = AxisTypes.value
                            min = AxisMinMax(-50.0)
                            max = AxisMinMax(300.0)
                            gridId = "g0"
                        },
                        unsafeJso<XAxisOption> {
                            id = "x2"
                            type = AxisTypes.value
                            min = AxisMinMax(-50.0)
                            max = AxisMinMax(300.0)
                            gridId = "g1"
                        },
                    )
                    yAxis = arrayOf(
                        unsafeJso<YAxisOption> {
                            id = "y0"
                            type = AxisTypes.value
                            min = AxisMinMax(6000.0)
                            max = AxisMinMax(9000.0)
                            gridId = "g0"
                        },
                        unsafeJso<YAxisOption> {
                            id = "y1"
                            type = AxisTypes.value
                            inverse = true // test inverse
                            min = AxisMinMax(600.0)
                            max = AxisMinMax(900.0)
                            gridId = "g1"
                        },
                    )
                    series = arrayOf<SeriesOption>(
                        scatterSeries {
                            id = "k1"
                            typedData = points(doubleArrayOf(1000.0, 700.0))
                        },
                        scatterSeries {
                            id = "k2"
                            typedData = points(doubleArrayOf(100.0, 800.0))
                        },
                        scatterSeries {
                            id = "j1"
                            typedData = points(doubleArrayOf(100.0, 800.0))
                            xAxisIndex = 1.0
                        },
                        scatterSeries {
                            id = "i1"
                            typedData = arrayOf()
                            xAxisId = "x2"
                            yAxisId = "y1"
                        },
                    )
                },
            )

            val width = chart.getWidth()
            val height = chart.getHeight()

            // Upstream expects `16` here: jsdom's canvas mock reports zero-width axis labels, so
            // the grid keeps the `left: 10` edge plus a 6px artifact. With real text measurement
            // the edge is exactly `left`, which `outerBoundsMode: "none"` makes deterministic.
            assertPointEquals(
                listOf(10.0, height - 40),
                chart.convertToPixel(finder { seriesIndex = 1.0 }, coordinate(-500.0, 6000.0)).asList(),
            )
            assertPointEquals(
                listOf(-500.0, 6000.0),
                chart.convertFromPixel(finder { seriesIndex = 1.0 }, DoubleOrDoubleArray(arrayOf(10.0, height - 40)))
                    .asList(),
            )

            assertPointEquals(
                listOf(width - 20, height - 40),
                chart.convertToPixel(finder { seriesId = "i1" }, coordinate(300.0, 900.0)).asList(),
            )
            assertPointEquals(
                listOf(300.0, 900.0),
                chart.convertFromPixel(finder { seriesId = "i1" }, DoubleOrDoubleArray(arrayOf(width - 20, height - 40)))
                    .asList(),
            )

            assertPointEquals(
                listOf(width - 20, height - 40),
                chart.convertToPixel(finder { xAxisIndex = 2.0; yAxisId = "y1" }, coordinate(300.0, 900.0)).asList(),
            )
            assertPointEquals(
                listOf(300.0, 900.0),
                chart.convertFromPixel(
                    finder { xAxisIndex = 2.0; yAxisId = "y1" },
                    DoubleOrDoubleArray(arrayOf(width - 20, height - 40)),
                ).asList(),
            )

            assertPointEquals(
                listOf(width - 20, height - 40),
                chart.convertToPixel(finder { gridId = "g1" }, coordinate(300.0, 900.0)).asList(),
            )
            assertPointEquals(
                listOf(300.0, 900.0),
                chart.convertFromPixel(finder { gridId = "g1" }, DoubleOrDoubleArray(arrayOf(width - 20, height - 40)))
                    .asList(),
            )

            assertScalarEquals(width / 2, chart.convertToPixel(finder { xAxisId = "x0" }, OptionDataValueOrArray(3000.0)).asScalar())
            assertScalarEquals(
                3000.0,
                chart.convertFromPixel(finder { xAxisId = "x0" }, DoubleOrDoubleArray(width / 2)).asScalar(),
            )

            assertScalarEquals(30.0, chart.convertToPixel(finder { yAxisIndex = 1.0 }, OptionDataValueOrArray(600.0)).asScalar())
            assertScalarEquals(600.0, chart.convertFromPixel(finder { yAxisIndex = 1.0 }, DoubleOrDoubleArray(30.0)).asScalar())
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun graph() {
        registerRectangleMap("converter-graph", 2000.0, 3000.0, 5000.0, 8000.0)

        val (chart, container) = createChart(chartWidth, chartHeight)
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    geo = arrayOf(
                        // Should not affect graph converter.
                        unsafeJso<GeoOption> { map = "converter-graph" },
                    )
                    series = arrayOf<SeriesOption>(
                        graphSeries {
                            id = "k1"
                            left = StringOrDouble(10.0)
                            right = StringOrDouble(20.0)
                            top = StringOrDouble(30.0)
                            bottom = StringOrDouble(40.0)
                            typedData = arrayOf(
                                node(1000.0, 2000.0),
                                node(1000.0, 5000.0),
                                node(3000.0, 5000.0),
                                node(3000.0, 2000.0),
                            )
                            links = arrayOf()
                        },
                        graphSeries {
                            id = "k2"
                            left = StringOrDouble(10.0)
                            right = StringOrDouble(20.0)
                            top = StringOrDouble(30.0)
                            bottom = StringOrDouble(40.0)
                            typedData = arrayOf(
                                node(100.0, 200.0),
                                node(100.0, 500.0),
                                node(300.0, 500.0),
                                node(300.0, 200.0),
                            )
                            links = arrayOf()
                        },
                    )
                },
            )

            val width = chart.getWidth()
            val height = chart.getHeight()

            assertPointEquals(
                listOf(10 + (width - 30) / 2, 30 + (height - 70) / 2),
                chart.convertToPixel(finder { seriesIndex = 0.0 }, coordinate(2000.0, 3500.0)).asList(),
            )
            assertPointEquals(
                listOf(2000.0, 3500.0),
                chart.convertFromPixel(
                    finder { seriesIndex = 0.0 },
                    DoubleOrDoubleArray(arrayOf(10 + (width - 30) / 2, 30 + (height - 70) / 2)),
                ).asList(),
            )

            assertPointEquals(
                listOf(10.0, height - 40),
                chart.convertToPixel(finder { seriesId = "k2" }, coordinate(100.0, 500.0)).asList(),
            )
            assertPointEquals(
                listOf(100.0, 500.0),
                chart.convertFromPixel(finder { seriesId = "k2" }, DoubleOrDoubleArray(arrayOf(10.0, height - 40)))
                    .asList(),
            )
        } finally {
            destroyChart(chart, container)
        }
    }
}

/**
 * Builds a graph node with fixed coordinates; see `ContainPixelTest` for the `GraphDataItem` wrap.
 */
private fun node(x: Double, y: Double): GraphDataItem? =
    unsafeJso<GraphNodeItemOption> {
        this.x = x
        this.y = y
    }.unsafeCast<GraphDataItem?>()
