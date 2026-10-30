package echartsgl

import echarts.ECharts
import echarts.EChartsInstance
import echarts.options.StringOrDouble
import echarts.options.one
import echartsgl.options.EChartsGlOption
import echartsgl.options.Geo3DOption
import echartsgl.options.Geo3DRegionOption
import echartsgl.options.GlobeLayerOption
import echartsgl.options.GlobeOption
import echartsgl.options.GlobeSunTime
import js.objects.unsafeJso
import kotlin.js.Promise
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
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
                            label = unsafeJso { show = true; fontSize = StringOrDouble(14.0) }
                            itemStyle = unsafeJso { borderColor = GlColor(ZRColor("#000")); borderWidth = 0.5 }
                            emphasis = unsafeJso {
                                label = unsafeJso { color = "#f00" }
                                itemStyle = unsafeJso { opacity = 0.6 }
                            }
                            regions =
                                arrayOf(
                                    unsafeJso<Geo3DRegionOption> {
                                        name = "Square"
                                        height = 1.5
                                        label = unsafeJso { show = false }
                                        itemStyle = unsafeJso { color = GlColor(ZRColor("#123456")) }
                                        emphasis = unsafeJso {
                                            itemStyle = unsafeJso { opacity = 1.0 }
                                        }
                                    },
                                )
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
                assertEquals(true, geo3D.label!!.show)
                assertEquals(StringOrDouble(14.0), geo3D.label!!.fontSize)
                assertEquals("#f00", geo3D.emphasis!!.label!!.color)
                assertEquals(0.6, geo3D.emphasis!!.itemStyle!!.opacity)

                val region = geo3D.regions!![0]!!
                assertEquals("Square", region.name)
                assertEquals(1.5, region.height)
                assertEquals(false, region.label!!.show)
                assertEquals(GlColor(ZRColor("#123456")), region.itemStyle!!.color)
                assertEquals(1.0, region.emphasis!!.itemStyle!!.opacity)
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
        val layerTexture = TextureSource(textureCanvas())
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
     * The size of the earth, the two textures that shape it and the light that is baked into the
     * environment.
     *
     * `globeRadius` / `globeOuterRadius` are the defaults of `GlobeModel` (`100` / `150`) and are read
     * by `globeCreator` to decide where the series float above the surface. `GlobeModel
     * .getDisplacementTexture` falls back to `heightTexture`, and the displacement pass only runs
     * when `displacementScale` is above zero, so both are set here to keep the two keys distinguishable.
     *
     * `light.ambientCubemap.texture` is left out on purpose: `SceneHelper.updateAmbientCubemap`
     * creates the cubemap only when that URL is set, and it then loads and pre-filters the panorama
     * asynchronously, which would still be pending when the chart is disposed.
     */
    @Test
    fun globeCarriesRadiusTexturesAndEnvironmentLight(): Promise<Unit> {
        ECharts.use(arrayOf(GlobeComponent))
        val (chart, container) = createChart()
        val heightMap = TextureSource(textureCanvas())
        val displacementMap = TextureSource(textureCanvas())
        chart.setOption(
            unsafeJso<EChartsGlOption> {
                globe =
                    one(
                        unsafeJso<GlobeOption> {
                            silent = true
                            globeRadius = 60.0
                            globeOuterRadius = 90.0
                            heightTexture = heightMap
                            displacementTexture = displacementMap
                            displacementScale = 0.05
                            shading = ShadingTypes.lambert
                            atmosphere = unsafeJso { show = true; innerGlowPower = 3.0 }
                            light =
                                unsafeJso {
                                    main = unsafeJso { shadowQuality = QualityTypes.ultra }
                                    ambientCubemap = unsafeJso {
                                        exposure = 0.5
                                        diffuseIntensity = 0.7
                                        specularIntensity = 0.9
                                    }
                                }
                            viewControl = unsafeJso { distance = 200.0 }
                        },
                    )
            },
        )
        return nextFrames(2).then {
            try {
                val globe = chart.getOption()
                    .unsafeCast<EChartsGlOption>()
                    .globe!![0]
                assertEquals(60.0, globe.globeRadius)
                assertEquals(90.0, globe.globeOuterRadius)
                assertSame(heightMap, globe.heightTexture)
                assertSame(displacementMap, globe.displacementTexture)
                assertEquals(0.05, globe.displacementScale)
                assertEquals(3.0, globe.atmosphere!!.innerGlowPower)
                assertEquals(QualityTypes.ultra, globe.light!!.main!!.shadowQuality)
                assertEquals(0.5, globe.light!!.ambientCubemap!!.exposure)
                assertEquals(0.7, globe.light!!.ambientCubemap!!.diffuseIntensity)
                assertEquals(0.9, globe.light!!.ambientCubemap!!.specularIntensity)
                assertNull(globe.light!!.ambientCubemap!!.texture)
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
