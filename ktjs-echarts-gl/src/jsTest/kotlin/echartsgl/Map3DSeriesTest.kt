package echartsgl

import echarts.ECharts
import echarts.GeoJSON
import echarts.GeoJSONFeature
import echarts.GeoJSONGeometry
import echarts.GeoJSONMapInput
import echarts.GeoJSONProperties
import echarts.GeoJSONSourceInput
import echarts.MapInput
import echarts.options.OptionDataValueOrArray
import echarts.options.OptionId
import echarts.options.one
import echartsgl.options.EChartsGlOption
import echartsgl.options.Geo3DOption
import echartsgl.options.Map3DDataItemOption
import echartsgl.options.Map3DSeriesOption
import echartsgl.options.Polygons3DCoords
import echartsgl.options.Polygons3DDataItem
import echartsgl.options.Polygons3DDataItemOption
import echartsgl.options.Polygons3DSeriesOption
import echartsgl.options.map3DSeries
import echartsgl.options.polygons3DSeries
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.Promise
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import zrender.ZRColor

/**
 * Tests the two series that extrude map regions: `polygons3D` and `map3D`.
 *
 * `echarts-gl` has no upstream unit suite for these types, so the cases cover the wrapper-specific
 * contract: the option is accepted, the fields survive the `getOption` round trip, and the WebGL
 * layer is created and painted.
 */
class Map3DSeriesTest {
    /**
     * `polygons3D` takes its polygons from the data instead of the GeoJSON, and extrudes each of
     * them to its own height inside the `geo3D` component.
     */
    @Test
    fun polygons3DRendersExtrudedPolygons(): Promise<Unit> {
        ECharts.use(arrayOf(Polygons3DChart, Geo3DComponent))
        registerMap3DSquareMap()
        val (chart, container) = createChart()
        chart.setOption(
            unsafeJso<EChartsGlOption> {
                geo3D =
                    one(
                        unsafeJso<Geo3DOption> {
                            map = MAP3D_SQUARE_MAP
                            silent = true
                            regionHeight = 1.0
                        },
                    )
                series =
                    one(
                        polygons3DSeries {
                            typedCoordinateSystem = CoordinateSystems3D.geo3D
                            geo3DIndex = 0.0
                            multiPolygon = true
                            shading = ShadingTypes.lambert
                            typedItemStyle = unsafeJso { borderWidth = 0.5; borderColor = GlColor(ZRColor("#333")) }
                            typedData =
                                arrayOf<Polygons3DDataItem?>(
                                    Polygons3DDataItem(
                                        unsafeJso<Polygons3DDataItemOption> {
                                            typedValue = OptionDataValueOrArray(3.0)
                                            // `multiPolygon` is set: one polygon, one ring.
                                            coords = Polygons3DCoords(arrayOf(arrayOf(innerSquareRing())))
                                            height = 3.0
                                            itemStyle = unsafeJso { color = GlColor(ZRColor("#f80")) }
                                        },
                                    ),
                                )
                        },
                    )
            },
        )
        return nextFrames(2).then {
            try {
                val series = chart.getOption()
                    .unsafeCast<EChartsGlOption>()
                    .series!![0]
                    .unsafeCast<Polygons3DSeriesOption>()
                assertEquals(EChartsGlSeriesTypes.polygons3D, series.type)
                assertEquals(CoordinateSystems3D.geo3D, series.typedCoordinateSystem)
                assertEquals(0.0, series.geo3DIndex)
                assertEquals(true, series.multiPolygon)
                assertEquals(ShadingTypes.lambert, series.shading)
                assertEquals(0.5, series.typedItemStyle!!.borderWidth)
                val item = series.typedData!![0]!!.unsafeCast<Polygons3DDataItemOption>()
                assertEquals(3.0, item.height)
                assertEquals(GlColor(ZRColor("#f80")), item.itemStyle!!.color)
                val polygons = item.coords!!.unsafeCast<Array<Array<Array<Array<Double>>>>>()
                assertEquals(1, polygons.size)
                assertEquals(1, polygons[0].size)
                assertEquals(5, polygons[0][0].size)
                assertEquals(2.0, polygons[0][0][0][0])
                assertWebGlCanvas(container)
            } finally {
                destroyChart(chart, container)
            }
        }
    }

    /**
     * `map3D` on its own `geo3D` coordinate system does not need a `geo3D` component: upstream
     * creates the coordinate system from the series model, using the series `map`.
     */
    @Test
    fun map3DWithoutGeo3DComponentExtrudesItsMap(): Promise<Unit> {
        ECharts.use(arrayOf(Map3DChart))
        registerMap3DSquareMap()
        val (chart, container) = createChart()
        chart.setOption(
            unsafeJso<EChartsGlOption> {
                series =
                    one(
                        map3DSeries {
                            typedCoordinateSystem = CoordinateSystems3D.geo3D
                            map = MAP3D_SQUARE_MAP
                            boxHeight = 10.0
                            regionHeight = 2.0
                            shading = ShadingTypes.lambert
                            typedItemStyle = unsafeJso { color = GlColor(ZRColor("#36c")); opacity = 0.8 }
                            viewControl = unsafeJso { distance = 120.0 }
                            typedData =
                                arrayOf<Map3DDataItemOption?>(
                                    unsafeJso {
                                        name = OptionId("Square")
                                        typedValue = OptionDataValueOrArray(5.0)
                                        height = 3.0
                                        itemStyle = unsafeJso { opacity = 0.9 }
                                    },
                                )
                        },
                    )
            },
        )
        return nextFrames(2).then {
            try {
                val series = chart.getOption()
                    .unsafeCast<EChartsGlOption>()
                    .series!![0]
                    .unsafeCast<Map3DSeriesOption>()
                assertEquals(EChartsGlSeriesTypes.map3D, series.type)
                assertEquals(CoordinateSystems3D.geo3D, series.typedCoordinateSystem)
                assertEquals(MAP3D_SQUARE_MAP, series.map)
                assertEquals(10.0, series.boxHeight)
                assertEquals(2.0, series.regionHeight)
                assertEquals(ShadingTypes.lambert, series.shading)
                assertEquals(0.8, series.typedItemStyle!!.opacity)
                assertEquals(120.0, series.viewControl!!.distance)
                val item = series.typedData!![0]!!
                assertEquals(OptionId("Square"), item.name)
                assertEquals(3.0, item.height)
                assertEquals(0.9, item.itemStyle!!.opacity)
                assertWebGlCanvas(container)
            } finally {
                destroyChart(chart, container)
            }
        }
    }

}

/** Name of the map registered by [registerSquareMap]. */
private const val MAP3D_SQUARE_MAP = "ktjs-echarts-gl-test-map3d-square"

/**
 * A closed ring inside the square of [MAP3D_SQUARE_MAP], so that the extruded polygon is visible.
 */
private fun innerSquareRing(): Array<Array<Double>> =
    arrayOf(
        arrayOf(2.0, 2.0),
        arrayOf(2.0, 8.0),
        arrayOf(8.0, 8.0),
        arrayOf(8.0, 2.0),
        arrayOf(2.0, 2.0),
    )

/**
 * Registers a single square region, so that `geo3D` and `map3D` have a map to extrude.
 */
private fun registerMap3DSquareMap() {
    val ring =
        arrayOf(
            arrayOf(0.0, 0.0),
            arrayOf(0.0, 10.0),
            arrayOf(10.0, 10.0),
            arrayOf(10.0, 0.0),
            arrayOf(0.0, 0.0),
        )
    val feature =
        unsafeJso<GeoJSONFeature> {
            type = "Feature"
            properties = unsafeJso<GeoJSONProperties> { name = "Square" }
            geometry =
                unsafeJso<GeoJSONGeometry> {
                    type = "Polygon"
                    coordinates = arrayOf(ring).unsafeCast<JsAny>()
                }
        }
    val geoJson =
        unsafeJso<GeoJSON> {
            type = "FeatureCollection"
            features = arrayOf(feature)
        }
    ECharts.registerMap(
        MAP3D_SQUARE_MAP,
        MapInput(unsafeJso<GeoJSONMapInput> { geoJSON = GeoJSONSourceInput(geoJson) }),
    )
}
