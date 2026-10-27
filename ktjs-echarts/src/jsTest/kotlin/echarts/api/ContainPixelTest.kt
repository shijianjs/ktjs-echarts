package echarts.api

import echarts.AxisTypes
import echarts.ModelFinder
import echarts.createChart
import echarts.destroyChart
import echarts.finder
import echarts.options.AxisMinMax
import echarts.options.EChartsOption
import echarts.options.GeoOption
import echarts.options.GraphNodeItemOption
import echarts.options.GridOption
import echarts.options.OptionDataItem
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
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Translated from `test/ut/spec/api/containPixel.test.ts` of the upstream apache/echarts
 * repository.
 *
 * The `testGeoJson1` / `testGeoJson2` fixtures are registered through [registerRectangleMap];
 * every assertion goes through the public `containPixel` API.
 */
class ContainPixelTest {
    private val chartWidth = 200
    private val chartHeight = 150

    @Test
    fun geo() {
        registerRectangleMap("contain-pixel-geo-1", 2000.0, 3000.0, 5000.0, 8000.0)
        registerRectangleMap("contain-pixel-geo-2", 200.0, 300.0, 500.0, 800.0)

        val (chart, container) = createChart(chartWidth, chartHeight)
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    geo = arrayOf(
                        unsafeJso<GeoOption> {
                            id = "aa"
                            left = StringOrDouble(10.0)
                            right = StringOrDouble("50%")
                            top = StringOrDouble(30.0)
                            bottom = StringOrDouble(40.0)
                            map = "contain-pixel-geo-1"
                        },
                        unsafeJso<GeoOption> {
                            id = "bb"
                            layoutCenter = arrayOf(StringOrDouble("50%"), StringOrDouble(50.0))
                            layoutSize = StringOrDouble(20.0)
                            aspectScale = 1.0
                            map = "contain-pixel-geo-2"
                        },
                        unsafeJso<GeoOption> {
                            id = "cc"
                            aspectScale = 1.0
                            left = StringOrDouble(0.0)
                            width = StringOrDouble("50%")
                            top = StringOrDouble(0.0)
                            height = StringOrDouble("50%")
                            zoom = 0.5 // test roam
                            map = "contain-pixel-geo-1"
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
                    )
                },
            )

            val width = chart.getWidth()

            assertTrue(chart.containPixel(ModelFinder("geo"), arrayOf(15.0, 30.0)))
            assertFalse(chart.containPixel(ModelFinder("geo"), arrayOf(9.5, 30.0)))
            assertTrue(chart.containPixel(finder { geoIndex = 1.0 }, arrayOf(width / 2, 50.0)))
            assertFalse(chart.containPixel(finder { geoIndex = 1.0 }, arrayOf(10.0, 20.0)))
            assertFalse(chart.containPixel(finder { geoId = "cc" }, arrayOf(0.0, 0.0)))
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun map() {
        registerRectangleMap("contain-pixel-map-1", 2000.0, 3000.0, 5000.0, 8000.0)
        registerRectangleMap("contain-pixel-map-2", 200.0, 300.0, 500.0, 800.0)

        val (chart, container) = createChart(chartWidth, chartHeight)
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    series = arrayOf<SeriesOption>(
                        mapSeries {
                            id = "k1"
                            map = "contain-pixel-map-1"
                            left = StringOrDouble(10.0)
                            right = StringOrDouble("50%")
                            top = StringOrDouble(30.0)
                            bottom = StringOrDouble(40.0)
                        },
                        mapSeries {
                            id = "k2"
                            map = "contain-pixel-map-2"
                            layoutCenter = arrayOf(StringOrDouble("50%"), StringOrDouble(50.0))
                            layoutSize = StringOrDouble(20.0)
                            aspectScale = 1.0
                        },
                    )
                },
            )

            val width = chart.getWidth()

            assertTrue(chart.containPixel(ModelFinder("series"), arrayOf(15.0, 30.0)))
            assertFalse(chart.containPixel(ModelFinder("series"), arrayOf(9.5, 30.0)))
            assertTrue(chart.containPixel(finder { seriesId = "k2" }, arrayOf(width / 2, 50.0)))
            // Upstream writes `{seriesId: 1}`, i.e. an index in an id field; the point is outside
            // the map either way, so the expected value is `false` in both readings.
            assertFalse(chart.containPixel(finder { seriesId = "k2" }, arrayOf(10.0, 20.0)))
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun cartesian() {
        registerRectangleMap("contain-pixel-cartesian", 2000.0, 3000.0, 5000.0, 8000.0)

        val (chart, container) = createChart(chartWidth, chartHeight)
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    geo = arrayOf(
                        // Should not affect grid converter.
                        unsafeJso<GeoOption> { map = "contain-pixel-cartesian" },
                    )
                    grid = arrayOf(
                        unsafeJso<GridOption> {
                            id = "g0"
                            left = StringOrDouble(10.0)
                            right = StringOrDouble("50%")
                            top = StringOrDouble(30.0)
                            bottom = StringOrDouble(40.0)
                            containLabel = false
                            outerBoundsMode = "none"
                        },
                        unsafeJso<GridOption> {
                            id = "g1"
                            left = StringOrDouble("50%")
                            right = StringOrDouble(20.0)
                            top = StringOrDouble(30.0)
                            bottom = StringOrDouble(40.0)
                            containLabel = false
                            outerBoundsMode = "none"
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
                            data = points(doubleArrayOf(1000.0, 700.0))
                        },
                        scatterSeries {
                            id = "k2"
                            data = points(doubleArrayOf(100.0, 800.0))
                        },
                        scatterSeries {
                            id = "j1"
                            data = points(doubleArrayOf(100.0, 800.0))
                            xAxisIndex = 1.0
                        },
                        scatterSeries {
                            id = "i1"
                            data = arrayOf()
                            xAxisId = "x2"
                            yAxisId = "y1"
                        },
                    )
                },
            )

            val width = chart.getWidth()

            assertTrue(chart.containPixel(ModelFinder("grid"), arrayOf(15.0, 30.0)))
            assertFalse(chart.containPixel(ModelFinder("grid"), arrayOf(9.5, 30.0)))
            assertTrue(chart.containPixel(finder { gridIndex = 1.0 }, arrayOf(width / 2, 50.0)))
            assertFalse(chart.containPixel(finder { gridIndex = 1.0 }, arrayOf(10.0, 20.0)))
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun graph() {
        registerRectangleMap("contain-pixel-graph", 2000.0, 3000.0, 5000.0, 8000.0)

        val (chart, container) = createChart(chartWidth, chartHeight)
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    geo = arrayOf(
                        // Should not affect graph converter.
                        unsafeJso<GeoOption> { map = "contain-pixel-graph" },
                    )
                    series = arrayOf<SeriesOption>(
                        graphSeries {
                            id = "k1"
                            left = StringOrDouble(10.0)
                            right = StringOrDouble("50%")
                            top = StringOrDouble(30.0)
                            bottom = StringOrDouble(40.0)
                            data = arrayOf(
                                node(1000.0, 2000.0),
                                node(1000.0, 5000.0),
                                node(3000.0, 5000.0),
                                node(3000.0, 2000.0),
                            )
                            links = arrayOf()
                        },
                    )
                },
            )

            assertTrue(chart.containPixel(ModelFinder("series"), arrayOf(15.0, 35.0)))
            assertFalse(chart.containPixel(ModelFinder("series"), arrayOf(3.0, 4.0)))
        } finally {
            destroyChart(chart, container)
        }
    }
}

/**
 * Builds a graph node with fixed coordinates.
 *
 * `GraphNodeItemOption` is not an `OptionDataItemObject` (its `value` is wider), so the series
 * `data` slot needs an explicit cast — the same thing upstream does with its `GraphNodeItemOption`
 * union.
 */
private fun node(x: Double, y: Double): OptionDataItem? =
    unsafeJso<GraphNodeItemOption> {
        this.x = x
        this.y = y
    }.unsafeCast<OptionDataItem?>()
