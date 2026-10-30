package echartsgl

import echarts.ECharts
import echarts.EChartsInstance
import echarts.GeoJSON
import echarts.GeoJSONFeature
import echarts.GeoJSONGeometry
import echarts.GeoJSONMapInput
import echarts.GeoJSONProperties
import echarts.GeoJSONSourceInput
import echarts.MapInput
import echarts.options.one
import echartsgl.options.EChartsGlOption
import echartsgl.options.Geo3DOption
import echartsgl.options.Geo3DRegionOption
import echartsgl.options.GlobeLayerOption
import echartsgl.options.GlobeOption
import echartsgl.options.GlobeSunTime
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.Promise
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue
import web.dom.document
import web.html.HtmlTagName
import zrender.ZRColor

/**
 * Tests the `geo3D` and `globe` components.
 *
 * `echarts-gl` has no upstream unit suite for these types, so the cases cover the wrapper-specific
 * contract: the option is accepted against a registered map, the fields survive the `getOption`
 * round trip, and the WebGL layer is created and painted.
 */
class Geo3DGlobeTest {
    @Test
    fun geo3DRendersTheRegisteredMap(): Promise<Unit> {
        ECharts.use(arrayOf(Geo3DComponent))
        registerSquareMap()
        val (chart, container) = createChart()
        chart.setOption(
            unsafeJso<EChartsGlOption> {
                geo3D =
                    one(
                        unsafeJso<Geo3DOption> {
                            map = SQUARE_MAP
                            silent = true
                            boxHeight = 8.0
                            boxDepth = DoubleOrAutos.auto
                            aspectScale = 0.8
                            regionHeight = 2.0
                            groundPlane = unsafeJso { show = true }
                            itemStyle = unsafeJso { borderColor = GlColor(ZRColor("#000")); borderWidth = 0.5 }
                            regions = arrayOf(unsafeJso<Geo3DRegionOption> { name = "Square"; height = 1.5 })
                            viewControl = unsafeJso { distance = 50.0 }
                        },
                    )
            },
        )
        return nextFrames(2).then {
            try {
                val geo3D = chart.getOption()
                    .unsafeCast<EChartsGlOption>()
                    .geo3D!![0]
                assertEquals(SQUARE_MAP, geo3D.map)
                assertEquals(true, geo3D.silent)
                assertEquals(8.0, geo3D.boxHeight)
                assertEquals(DoubleOrAutos.auto, geo3D.boxDepth)
                assertEquals(0.8, geo3D.aspectScale)
                assertEquals(2.0, geo3D.regionHeight)
                assertEquals(true, geo3D.groundPlane!!.show)
                assertEquals(GlColor(ZRColor("#000")), geo3D.itemStyle!!.borderColor)
                assertEquals(0.5, geo3D.itemStyle!!.borderWidth)
                assertEquals("Square", geo3D.regions!![0].name)
                assertEquals(1.5, geo3D.regions!![0].height)
                assertEquals(50.0, geo3D.viewControl!!.distance)
                assertWebGlCanvas(container)
            } finally {
                destroyChart(chart, container)
            }
        }
    }

    @Test
    fun globeRendersWithAtmosphereAndLayers(): Promise<Unit> {
        ECharts.use(arrayOf(GlobeComponent))
        val (chart, container) = createChart()
        // A canvas texture loads synchronously, so nothing is pending when the chart is disposed.
        val layerTexture = TextureSource(document.createElement(HtmlTagName.canvas))
        chart.setOption(
            unsafeJso<EChartsGlOption> {
                globe =
                    one(
                        unsafeJso<GlobeOption> {
                            silent = true
                            baseColor = ZRColor("#123")
                            displacementScale = 0.05
                            displacementQuality = QualityTypes.medium
                            shading = ShadingTypes.lambert
                            atmosphere = unsafeJso { show = true; offset = 4.0; glowPower = 4.0 }
                            layers =
                                arrayOf(
                                    unsafeJso<GlobeLayerOption> {
                                        type = GlobeLayerTypes.blend
                                        blendTo = GlobeBlendTargets.emission
                                        name = "night"
                                        intensity = 0.8
                                        texture = layerTexture
                                    },
                                    unsafeJso<GlobeLayerOption> {
                                        type = GlobeLayerTypes.overlay
                                        shading = ShadingTypes.lambert
                                        distance = 5.0
                                        texture = layerTexture
                                    },
                                )
                            light =
                                unsafeJso {
                                    main = unsafeJso { intensity = 2.0; time = GlobeSunTime("2026-01-01") }
                                }
                            viewControl =
                                unsafeJso {
                                    autoRotate = true
                                    targetCoord = arrayOf(116.46, 39.92)
                                }
                            debug = unsafeJso { wireframe = unsafeJso { show = true } }
                        },
                    )
            },
        )
        return nextFrames(2).then {
            try {
                val globe = chart.getOption()
                    .unsafeCast<EChartsGlOption>()
                    .globe!![0]
                assertEquals(true, globe.silent)
                assertEquals(0.05, globe.displacementScale)
                assertEquals(QualityTypes.medium, globe.displacementQuality)
                assertEquals(true, globe.atmosphere!!.show)
                assertEquals(4.0, globe.atmosphere!!.glowPower)
                assertEquals("2026-01-01", globe.light!!.main!!.time!!.unsafeCast<String>())
                assertEquals(true, globe.viewControl!!.autoRotate)
                assertEquals(116.46, globe.viewControl!!.targetCoord!![0])
                assertEquals(39.92, globe.viewControl!!.targetCoord!![1])
                assertEquals(true, globe.debug!!.wireframe!!.show)

                val layers = globe.layers!!
                assertEquals(2, layers.size)
                assertEquals(GlobeLayerTypes.blend, layers[0].type)
                assertEquals(GlobeBlendTargets.emission, layers[0].blendTo)
                assertEquals("night", layers[0].name)
                assertEquals(0.8, layers[0].intensity)
                assertEquals(GlobeLayerTypes.overlay, layers[1].type)
                assertEquals(5.0, layers[1].distance)
                assertWebGlCanvas(container)
            } finally {
                destroyChart(chart, container)
            }
        }
    }

    /**
     * A globe texture can be another chart instance; upstream links the mouse actions of the globe
     * to it (`graphicGL.loadTexture` checks for an ECharts instance). The texture chart has to be
     * initialized on a canvas, otherwise upstream refuses to use it.
     */
    @Test
    fun textureSourceWrapsAnEChartsInstance() {
        ECharts.use(arrayOf(GlobeComponent))
        val (chart, container) = createChart()
        val textureCanvas =
            document.createElement(HtmlTagName.canvas)
                .apply {
                    width = 64
                    height = 32
                }
        document.body.appendChild(textureCanvas)
        val textureChart = ECharts.init(textureCanvas)
        try {
            chart.setOption(
                unsafeJso<EChartsGlOption> {
                    globe = one(unsafeJso<GlobeOption> { baseTexture = TextureSource(textureChart) })
                },
            )
            val globe = chart.getOption()
                .unsafeCast<EChartsGlOption>()
                .globe!![0]
            assertSame(textureChart, globe.baseTexture!!.unsafeCast<EChartsInstance>())
        } finally {
            destroyChart(chart, container)
            destroyChart(textureChart, textureCanvas)
        }
    }

    /**
     * `Geo3DBuilder` creates the ground-plane material of every shading model at construction,
     * including `"shadow"`, whose chunks upstream only registers from the mapbox3D view.
     */
    @Test
    fun installingGeo3DRegistersTheDisplayShadowShader() {
        ECharts.use(arrayOf(Geo3DComponent))
        assertTrue(graphicGlNamespace.shader.source("ecgl.displayShadow.vertex").isNotEmpty())
        assertTrue(graphicGlNamespace.shader.source("ecgl.displayShadow.fragment").isNotEmpty())
    }

}

/** Name of the map registered by [registerSquareMap]. */
private const val SQUARE_MAP = "ktjs-echarts-gl-test-square"

/**
 * Registers a single square region, so that `geo3D` has a map to extrude.
 */
private fun registerSquareMap() {
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
        SQUARE_MAP,
        MapInput(unsafeJso<GeoJSONMapInput> { geoJSON = GeoJSONSourceInput(geoJson) }),
    )
}
