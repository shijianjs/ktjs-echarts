package echartsgl

import echarts.ECharts
import echarts.options.DoubleOrDoubleArray
import echarts.options.OptionId
import echarts.options.one
import echartsgl.options.Bar3DSeriesOption
import echartsgl.options.ColorMaterialOption
import echartsgl.options.EChartsGlOption
import echartsgl.options.Geo3DGroundPlaneOption
import echartsgl.options.Geo3DOption
import echartsgl.options.GlobeOption
import echartsgl.options.Grid3DOption
import echartsgl.options.HatchingMaterialOption
import echartsgl.options.LambertMaterialOption
import echartsgl.options.Map3DSeriesOption
import echartsgl.options.RealisticMaterialOption
import echartsgl.options.bar3DSeries
import echartsgl.options.map3DSeries
import js.objects.unsafeJso
import kotlin.js.Promise
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import web.html.HTMLCanvasElement
import zrender.ZRColor

/**
 * Tests the materials a shaded GL surface is built from.
 *
 * `graphicGL.setMaterialFromModel` reads the material of the shading model currently in use from
 * `<shading>Material`, so all four keys of [GlMaterialSetOption][echartsgl.options.GlMaterialSetOption]
 * can be given at once and only the selected one reaches the GPU. The components that own a
 * `Geo3DBuilder` additionally give the ground plane a material set of its own.
 *
 * The textures are canvases: `graphicGL.loadTexture` builds them synchronously, so nothing is still
 * loading when the case disposes its chart.
 */
class MaterialOptionTest {
    /**
     * A `bar3D` series keeps all four material sets, and the `"lambert"` one is the one painted.
     */
    @Test
    fun bar3DCarriesEveryShadingMaterialSet(): Promise<Unit> {
        ECharts.use(arrayOf(Bar3DChart, Grid3DComponent))
        val canvas = textureCanvas()
        val (chart, container) = createChart()
        chart.setOption(
            unsafeJso<EChartsGlOption> {
                cartesian3DAxes()
                grid3D = one(unsafeJso<Grid3DOption> { boxHeight = 20.0 })
                series =
                    one(
                        bar3DSeries {
                            typedData = bar3DItems(0.0, 0.0, 1.0, 1.0, 1.0, 2.0, 2.0, 0.0, 3.0)
                            shading = ShadingTypes.lambert
                            lambertMaterial =
                                unsafeJso<LambertMaterialOption> {
                                    detailTexture = TextureSource(canvas)
                                    textureTiling = DoubleOrDoubleArray(2.0)
                                    textureOffset = DoubleOrDoubleArray(arrayOf(0.25, 0.5))
                                }
                            colorMaterial =
                                unsafeJso<ColorMaterialOption> { detailTexture = TextureSource(canvas) }
                            realisticMaterial =
                                unsafeJso<RealisticMaterialOption> {
                                    roughness = NumberOrTexture(0.3)
                                    metalness = NumberOrTexture(0.7)
                                }
                            hatchingMaterial =
                                unsafeJso<HatchingMaterialOption> {
                                    hatchingTextures = textureCanvases(6)
                                }
                        },
                    )
            },
        )
        return nextFrames(2).then {
            try {
                val series = chart.getOption()
                    .unsafeCast<EChartsGlOption>()
                    .series!![0]
                    .unsafeCast<Bar3DSeriesOption>()
                assertEquals(ShadingTypes.lambert, series.shading)
                val lambert = series.lambertMaterial!!
                assertSame(canvas, lambert.detailTexture!!.unsafeCast<HTMLCanvasElement>())
                val offset = lambert.textureOffset!!.unsafeCast<Array<Double>>()
                assertEquals(0.25, offset[0])
                assertEquals(0.5, offset[1])
                // Only the material of the selected shading reaches the GPU, the others are kept.
                assertSame(canvas, series.colorMaterial!!.detailTexture!!.unsafeCast<HTMLCanvasElement>())
                assertEquals(0.3, series.realisticMaterial!!.roughness!!.unsafeCast<Double>())
                assertEquals(6, series.hatchingMaterial!!.hatchingTextures!!.size)
                assertWebGlCanvas(container)
            } finally {
                destroyChart(chart, container)
            }
        }
    }

    /**
     * `shading = "hatching"` compiles the shader that samples `hatch1` to `hatch6`, so the six
     * textures of the material are really uploaded.
     */
    @Test
    fun bar3DWithHatchingShadingSamplesSixTextures(): Promise<Unit> {
        ECharts.use(arrayOf(Bar3DChart, Grid3DComponent))
        val textures = textureCanvases(6)
        val (chart, container) = createChart()
        chart.setOption(
            unsafeJso<EChartsGlOption> {
                cartesian3DAxes()
                grid3D = one(unsafeJso<Grid3DOption> { boxHeight = 20.0 })
                series =
                    one(
                        bar3DSeries {
                            typedData = bar3DItems(0.0, 0.0, 1.0, 1.0, 1.0, 2.0)
                            shading = ShadingTypes.hatching
                            hatchingMaterial =
                                unsafeJso<HatchingMaterialOption> {
                                    hatchingTextures = textures
                                    textureTiling = DoubleOrDoubleArray(2.0)
                                }
                        },
                    )
            },
        )
        return nextFrames(2).then {
            try {
                val series = chart.getOption()
                    .unsafeCast<EChartsGlOption>()
                    .series!![0]
                    .unsafeCast<Bar3DSeriesOption>()
                val material = series.hatchingMaterial!!
                assertEquals(ShadingTypes.hatching, series.shading)
                assertEquals(6, material.hatchingTextures!!.size)
                assertSame(
                    textures[0]!!.unsafeCast<HTMLCanvasElement>(),
                    material.hatchingTextures!![0]!!.unsafeCast<HTMLCanvasElement>(),
                )
                assertEquals(2.0, material.textureTiling!!.unsafeCast<Double>())
                assertWebGlCanvas(container)
            } finally {
                destroyChart(chart, container)
            }
        }
    }

    /**
     * The ground plane of a `geo3D` component is shaded by its own material when it declares one;
     * `Geo3DBuilder` models it with the component as the parent, so the members it leaves out are
     * inherited instead.
     */
    @Test
    fun geo3DGroundPlaneOverridesTheComponentMaterial(): Promise<Unit> {
        ECharts.use(arrayOf(Geo3DComponent))
        registerSquareMap()
        val groundCanvas = textureCanvas()
        val componentCanvas = textureCanvas()
        val (chart, container) = createChart()
        chart.setOption(
            unsafeJso<EChartsGlOption> {
                geo3D =
                    one(
                        unsafeJso<Geo3DOption> {
                            map = SQUARE_MAP
                            silent = true
                            shading = ShadingTypes.lambert
                            lambertMaterial = unsafeJso { detailTexture = TextureSource(componentCanvas) }
                            groundPlane =
                                unsafeJso<Geo3DGroundPlaneOption> {
                                    show = true
                                    color = GlColor(ZRColor("#223344"))
                                    lambertMaterial =
                                        unsafeJso<LambertMaterialOption> {
                                            detailTexture = TextureSource(groundCanvas)
                                            textureTiling = DoubleOrDoubleArray(4.0)
                                        }
                                }
                            viewControl = unsafeJso { distance = 90.0 }
                        },
                    )
            },
        )
        return nextFrames(2).then {
            try {
                val geo3D = chart.getOption()
                    .unsafeCast<EChartsGlOption>()
                    .geo3D!![0]
                val ground = geo3D.groundPlane!!
                assertEquals(true, ground.show)
                assertEquals(GlColor(ZRColor("#223344")), ground.color)
                assertSame(
                    groundCanvas,
                    ground.lambertMaterial!!.detailTexture!!.unsafeCast<HTMLCanvasElement>(),
                )
                assertEquals(4.0, ground.lambertMaterial!!.textureTiling!!.unsafeCast<Double>())
                // The component keeps its own material: the ground plane overrode only its detail texture.
                assertSame(
                    componentCanvas,
                    geo3D.lambertMaterial!!.detailTexture!!.unsafeCast<HTMLCanvasElement>(),
                )
                assertWebGlCanvas(container)
            } finally {
                destroyChart(chart, container)
            }
        }
    }

    /**
     * A `globe` with realistic shading takes its roughness and metalness either as numbers or as
     * maps; a non-numeric member is uploaded as a texture and the matching `*Adjust` becomes the
     * scalar applied to it.
     */
    @Test
    fun globeRealisticMaterialTakesRoughnessAndMetalnessMaps(): Promise<Unit> {
        ECharts.use(arrayOf(GlobeComponent))
        val roughnessCanvas = textureCanvas()
        val metalnessCanvas = textureCanvas()
        val normalCanvas = textureCanvas()
        val (chart, container) = createChart()
        chart.setOption(
            unsafeJso<EChartsGlOption> {
                globe =
                    one(
                        unsafeJso<GlobeOption> {
                            silent = true
                            baseTexture = TextureSource(textureCanvas())
                            shading = ShadingTypes.realistic
                            realisticMaterial =
                                unsafeJso<RealisticMaterialOption> {
                                    roughness = NumberOrTexture(roughnessCanvas)
                                    metalness = NumberOrTexture(metalnessCanvas)
                                    roughnessAdjust = 0.2
                                    metalnessAdjust = 0.8
                                    normalTexture = TextureSource(normalCanvas)
                                    textureTiling = DoubleOrDoubleArray(2.0)
                                }
                        },
                    )
            },
        )
        return nextFrames(2).then {
            try {
                val globe = chart.getOption()
                    .unsafeCast<EChartsGlOption>()
                    .globe!![0]
                val material = globe.realisticMaterial!!
                assertEquals(ShadingTypes.realistic, globe.shading)
                assertSame(roughnessCanvas, material.roughness!!.unsafeCast<HTMLCanvasElement>())
                assertSame(metalnessCanvas, material.metalness!!.unsafeCast<HTMLCanvasElement>())
                assertEquals(0.2, material.roughnessAdjust)
                assertEquals(0.8, material.metalnessAdjust)
                assertSame(normalCanvas, material.normalTexture!!.unsafeCast<HTMLCanvasElement>())
                assertWebGlCanvas(container)
            } finally {
                destroyChart(chart, container)
            }
        }
    }

    /**
     * A `map3D` series is a `geo3D` component with the map inside it, so it carries the same
     * material, ground plane and region style members; upstream fills the data from the GeoJSON, so
     * the regions are styled through the data items rather than a `regions` option.
     */
    @Test
    fun map3DCarriesShadingMaterialsAndGroundPlane(): Promise<Unit> {
        ECharts.use(arrayOf(Map3DChart))
        registerSquareMap()
        val hatchTextures = textureCanvases(6)
        val (chart, container) = createChart()
        chart.setOption(
            unsafeJso<EChartsGlOption> {
                series =
                    one(
                        map3DSeries {
                            typedCoordinateSystem = CoordinateSystems3D.geo3D
                            map = SQUARE_MAP
                            boxHeight = 10.0
                            regionHeight = 2.0
                            shading = ShadingTypes.hatching
                            hatchingMaterial =
                                unsafeJso<HatchingMaterialOption> { hatchingTextures = hatchTextures }
                            typedLabel = unsafeJso { show = false }
                            typedEmphasis = unsafeJso { itemStyle = unsafeJso { opacity = 0.7 } }
                            groundPlane = unsafeJso { show = true }
                            viewControl = unsafeJso { distance = 120.0 }
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
                assertEquals(ShadingTypes.hatching, series.shading)
                assertEquals(6, series.hatchingMaterial!!.hatchingTextures!!.size)
                assertSame(
                    hatchTextures[5]!!.unsafeCast<HTMLCanvasElement>(),
                    series.hatchingMaterial!!.hatchingTextures!![5]!!.unsafeCast<HTMLCanvasElement>(),
                )
                assertEquals(false, series.typedLabel!!.show)
                assertEquals(0.7, series.typedEmphasis!!.itemStyle!!.opacity)
                assertEquals(true, series.groundPlane!!.show)
                // `getFilledRegions` added the region the GeoJSON declares, even without data.
                assertEquals(OptionId("Square"), series.typedData!![0]!!.name)
                assertWebGlCanvas(container)
            } finally {
                destroyChart(chart, container)
            }
        }
    }

}
