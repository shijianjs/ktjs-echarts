package echartsgl

import echarts.AxisTypes
import echarts.ECharts
import echarts.options.one
import echartsgl.options.Axis3DOption
import echartsgl.options.EChartsGlOption
import echartsgl.options.GlobeOption
import echartsgl.options.Grid3DOption
import echartsgl.options.PostEffectOption
import echartsgl.options.bar3DSeries
import js.objects.unsafeJso
import kotlin.js.Promise
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import web.html.HTMLCanvasElement
import web.html.HTMLElement
import zrender.GradientColorStop
import zrender.LinearGradientObject
import zrender.linearGradient

/**
 * Tests `postEffect`, `temporalSuperSampling` and `environment` of a 3D component.
 *
 * `echarts-gl` has no upstream unit suite for these types, so the cases cover the wrapper-specific
 * contract: the option is accepted, the fields survive the `getOption` round trip, and the keys are
 * spelled the way `ViewGL.setPostEffect` reads them.
 */
class PostEffectTest {
    /**
     * `ViewGL.setPostEffect` asks for `DOF`, `SSAO` and `SSR` first and only then for the documented
     * camelCase name, and `FXAA` has no camelCase fallback at all. A wrong `@JsName` would write a key
     * that nothing reads and still look correct through the typed accessor, so the raw property names
     * are checked as well.
     */
    @Test
    fun postEffectWritesTheAliasKeysUpstreamReadsFirst() {
        val aliases = unsafeJso<PostEffectOption> {
            dof = unsafeJso { enable = true; focalDistance = 300.0 }
            ssao = unsafeJso { enable = true; radius = 3.0 }
            ssr = unsafeJso { enable = true; maxRoughness = 0.5 }
            fxaa = unsafeJso { enable = true }
        }
        assertTrue(hasOwn(aliases, "DOF"), "dof must land on the key ViewGL reads")
        assertTrue(hasOwn(aliases, "SSAO"), "ssao must land on the key ViewGL reads")
        assertTrue(hasOwn(aliases, "SSR"), "ssr must land on the key ViewGL reads")
        assertTrue(hasOwn(aliases, "FXAA"), "fxaa must land on the key ViewGL reads")
        assertFalse(hasOwn(aliases, "dof"), "the Kotlin property name is not a JavaScript key")
        assertFalse(hasOwn(aliases, "ssao"))
        assertFalse(hasOwn(aliases, "ssr"))
        assertFalse(hasOwn(aliases, "fxaa"))

        // The documented spelling is the fallback of the same pass, so both are real keys.
        val documented = unsafeJso<PostEffectOption> {
            depthOfField = unsafeJso { enable = true }
            screenSpaceAmbientOcclusion = unsafeJso { enable = true }
            screenSpaceReflection = unsafeJso { enable = true }
            colorCorrection = unsafeJso { enable = true }
            edge = unsafeJso { enable = true }
            bloom = unsafeJso { enable = true }
        }
        assertTrue(hasOwn(documented, "depthOfField"))
        assertTrue(hasOwn(documented, "screenSpaceAmbientOcclusion"))
        assertTrue(hasOwn(documented, "screenSpaceReflection"))
        assertTrue(hasOwn(documented, "colorCorrection"))
        assertTrue(hasOwn(documented, "edge"))
        assertTrue(hasOwn(documented, "bloom"))
        assertFalse(hasOwn(documented, "DOF"))
        assertFalse(hasOwn(documented, "SSAO"))
        assertFalse(hasOwn(documented, "SSR"))
    }

    /**
     * Every pass of a `grid3D` post effect, plus `temporalSuperSampling`, survives the `getOption`
     * round trip.
     */
    @Test
    fun postEffectOptionsRoundTrip() {
        ECharts.use(arrayOf(Bar3DChart, Grid3DComponent))
        val (chart, container) = createChart()
        try {
            chart.setOption(postEffectScene())

            val grid = chart.getOption().unsafeCast<EChartsGlOption>().grid3D!![0]
            assertEquals("auto", grid.environment!!.unsafeCast<String>())
            assertEquals(BooleanOrAutos.auto, grid.temporalSuperSampling!!.enable)

            val post = grid.postEffect!!
            assertEquals(true, post.enable)
            assertEquals(true, post.bloom!!.enable)
            assertEquals(0.2, post.bloom!!.intensity)
            assertEquals(120.0, post.dof!!.focalDistance)
            assertEquals(30.0, post.dof!!.focalRange)
            assertEquals(0.28, post.dof!!.fstop)
            assertEquals(4.0, post.dof!!.blurRadius)
            assertEquals(QualityTypes.high, post.dof!!.quality)
            assertEquals(2.5, post.ssao!!.radius)
            assertEquals(0.8, post.ssao!!.intensity)
            assertEquals(QualityTypes.medium, post.ssao!!.quality)
            assertEquals(false, post.ssr!!.enable)
            assertEquals(0.6, post.ssr!!.maxRoughness)
            assertEquals(true, post.ssr!!.physical)
            assertEquals(QualityTypes.ultra, post.ssr!!.quality)
            assertEquals(1.5, post.colorCorrection!!.exposure)
            assertEquals(0.1, post.colorCorrection!!.brightness)
            assertEquals(1.2, post.colorCorrection!!.contrast)
            assertEquals(0.9, post.colorCorrection!!.saturation)
            assertEquals("lut.png", post.colorCorrection!!.lookupTexture!!.unsafeCast<String>())
            assertEquals(true, post.edge!!.enable)
            assertEquals(1.0, post.edge!!.color!![3])
            assertEquals(true, post.fxaa!!.enable)
        } finally {
            destroyChart(chart, container)
        }
    }

    /**
     * A `globe` carries the same scene options without having any axes, so the passes are not a
     * `grid3D` speciality; supersampling also accepts a plain `true` next to `"auto"`.
     */
    @Test
    fun postEffectRoundTripOnAGlobeComponent(): Promise<Unit> {
        ECharts.use(arrayOf(GlobeComponent))
        val (chart, container) = createChart()
        chart.setOption(
            unsafeJso<EChartsGlOption> {
                globe = one(
                    unsafeJso<GlobeOption> {
                        environment = GlEnvironment("#1b2b3a")
                        postEffect = unsafeJso {
                            enable = false
                            edge = unsafeJso { enable = true; color = arrayOf(0.0, 0.0, 0.0, 1.0) }
                        }
                        temporalSuperSampling = unsafeJso { enable = BooleanOrAuto(true) }
                    },
                )
            },
        )
        return nextFrames(2).then {
            try {
                val globe = chart.getOption().unsafeCast<EChartsGlOption>().globe!![0]
                assertEquals("#1b2b3a", globe.environment!!.unsafeCast<String>())
                assertEquals(true, globe.postEffect!!.edge!!.enable)
                assertEquals(0.0, globe.postEffect!!.edge!!.color!![0])
                assertEquals(true, globe.temporalSuperSampling!!.enable!!.unsafeCast<Boolean>())
                assertWebGlCanvas(container)
            } finally {
                destroyChart(chart, container)
            }
        }
    }

    /**
     * `environment` is `"auto"`, a color, a gradient object or a panoramic texture URL, and
     * `SceneHelper` tells the branches apart by their runtime shape, so each one has to reach
     * JavaScript unchanged.
     */
    @Test
    fun environmentAcceptsEveryDocumentedBranch() {
        val auto = unsafeJso<Grid3DOption> { environment = GlEnvironments.auto }
        assertEquals("auto", auto.environment!!.unsafeCast<String>())

        val solid = unsafeJso<Grid3DOption> { environment = GlEnvironment("#1b2b3a") }
        assertEquals("#1b2b3a", solid.environment!!.unsafeCast<String>())

        val panorama = unsafeJso<Grid3DOption> { environment = GlEnvironment("panorama.jpg") }
        assertEquals("panorama.jpg", panorama.environment!!.unsafeCast<String>())

        val sky = unsafeJso<Grid3DOption> {
            environment = GlEnvironment(
                linearGradient(
                    0.0,
                    0.0,
                    0.0,
                    1.0,
                    arrayOf<GradientColorStop>(
                        unsafeJso { offset = 0.0; color = "#000011" },
                        unsafeJso { offset = 1.0; color = "#336699" },
                    ),
                ),
            )
        }
        // SceneHelper recognizes the object branch by `colorStops`.
        val gradient = sky.environment!!.unsafeCast<LinearGradientObject>()
        assertEquals("linear", gradient.type)
        assertEquals(2, gradient.colorStops!!.size)
        assertEquals("#336699", gradient.colorStops!![1].color)
    }

    /**
     * Builds a `bar3D` scene whose `grid3D` turns on every post-processing pass.
     *
     * Screen space reflection stays disabled: with `echarts-gl` 2.1.0 its pass paints an all-black
     * frame in Chrome, which would make the WebGL assertions of any later test meaningless. Its other
     * fields are still set, because a round trip does not need the pass to run.
     */
    private fun postEffectScene(): EChartsGlOption = unsafeJso {
        grid3D = one(
            unsafeJso<Grid3DOption> {
                environment = GlEnvironments.auto
                postEffect = unsafeJso {
                    enable = true
                    bloom = unsafeJso { enable = true; intensity = 0.2 }
                    dof = unsafeJso {
                        enable = true
                        focalDistance = 120.0
                        focalRange = 30.0
                        fstop = 0.28
                        blurRadius = 4.0
                        quality = QualityTypes.high
                    }
                    ssao = unsafeJso {
                        enable = true
                        radius = 2.5
                        intensity = 0.8
                        quality = QualityTypes.medium
                    }
                    ssr = unsafeJso {
                        enable = false
                        maxRoughness = 0.6
                        physical = true
                        quality = QualityTypes.ultra
                    }
                    colorCorrection = unsafeJso {
                        enable = true
                        exposure = 1.5
                        brightness = 0.1
                        contrast = 1.2
                        saturation = 0.9
                        lookupTexture = TextureSource("lut.png")
                    }
                    edge = unsafeJso { enable = true; color = arrayOf(0.0, 0.0, 0.0, 1.0) }
                    fxaa = unsafeJso { enable = true }
                }
                temporalSuperSampling = unsafeJso { enable = BooleanOrAutos.auto }
            },
        )
        xAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
        yAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
        zAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
        series = one(
            bar3DSeries {
                typedData = bar3DItems(0.0, 0.0, 3.0, 1.0, 1.0, 5.0)
            },
        )
    }

    /**
     * Asserts that the WebGL layer was created and painted into the only canvas of the container.
     */
    private fun assertWebGlCanvas(container: HTMLElement) {
        assertNull(container.querySelector(".ecgl-nowebgl"), "WebGL is not available")
        val canvases = container.querySelectorAll("canvas")
        assertEquals(1, canvases.length)
        val canvas = canvases.item(0).unsafeCast<HTMLCanvasElement>()
        assertTrue(canvas.width > 0 && canvas.height > 0, "${canvas.width}x${canvas.height}")
    }
}
