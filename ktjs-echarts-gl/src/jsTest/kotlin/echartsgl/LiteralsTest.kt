package echartsgl

import echarts.options.CallbackDataParams
import echartsgl.options.GlobeSunTime
import kotlin.js.Date
import kotlin.js.js
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import zrender.ZRColor

/**
 * Tests for the string-literal unions of `echarts-gl`.
 *
 * The constants must compile to the bare strings that the upstream option documentation uses, so
 * every case compares the constant with the literal it replaces.
 */
class LiteralsTest {
    @Test
    fun coordinateSystemsMatchTheUpstreamNames() {
        assertEquals("cartesian3D", CoordinateSystems3D.cartesian3D.unsafeCast<String>())
        assertEquals("geo3D", CoordinateSystems3D.geo3D.unsafeCast<String>())
        assertEquals("globe", CoordinateSystems3D.globe.unsafeCast<String>())
        assertEquals("mapbox3D", CoordinateSystems3D.mapbox3D.unsafeCast<String>())
        assertEquals("maptalks3D", CoordinateSystems3D.maptalks3D.unsafeCast<String>())
    }

    @Test
    fun shadingAndProjectionMatchTheUpstreamNames() {
        assertEquals("color", ShadingTypes.color.unsafeCast<String>())
        assertEquals("lambert", ShadingTypes.lambert.unsafeCast<String>())
        assertEquals("realistic", ShadingTypes.realistic.unsafeCast<String>())
        assertEquals("hatching", ShadingTypes.hatching.unsafeCast<String>())

        assertEquals("perspective", ProjectionTypes.perspective.unsafeCast<String>())
        assertEquals("orthographic", ProjectionTypes.orthographic.unsafeCast<String>())
    }

    @Test
    fun qualityAndControlLiteralsMatchTheUpstreamNames() {
        assertEquals("low", QualityTypes.low.unsafeCast<String>())
        assertEquals("medium", QualityTypes.medium.unsafeCast<String>())
        assertEquals("high", QualityTypes.high.unsafeCast<String>())
        assertEquals("ultra", QualityTypes.ultra.unsafeCast<String>())

        assertEquals("cw", AutoRotateDirections.cw.unsafeCast<String>())
        assertEquals("ccw", AutoRotateDirections.ccw.unsafeCast<String>())

        assertEquals("left", MouseButtons.left.unsafeCast<String>())
        assertEquals("middle", MouseButtons.middle.unsafeCast<String>())
        assertEquals("right", MouseButtons.right.unsafeCast<String>())
    }

    @Test
    fun blendModesAndGridPlanesMatchTheUpstreamNames() {
        assertEquals("source-over", GlBlendModes.sourceOver.unsafeCast<String>())
        assertEquals("lighter", GlBlendModes.lighter.unsafeCast<String>())

        assertEquals("xy", GridPlaneTypes.xy.unsafeCast<String>())
        assertEquals("yz", GridPlaneTypes.yz.unsafeCast<String>())
        assertEquals("xz", GridPlaneTypes.xz.unsafeCast<String>())
    }

    @Test
    fun globeLiteralsMatchTheUpstreamNames() {
        assertEquals("overlay", GlobeLayerTypes.overlay.unsafeCast<String>())
        assertEquals("blend", GlobeLayerTypes.blend.unsafeCast<String>())

        assertEquals("albedo", GlobeBlendTargets.albedo.unsafeCast<String>())
        assertEquals("emission", GlobeBlendTargets.emission.unsafeCast<String>())
    }

    @Test
    fun seriesTypesMatchTheUpstreamNames() {
        assertEquals("bar3D", EChartsGlSeriesTypes.bar3D.unsafeCast<String>())
        assertEquals("line3D", EChartsGlSeriesTypes.line3D.unsafeCast<String>())
        assertEquals("scatter3D", EChartsGlSeriesTypes.scatter3D.unsafeCast<String>())
        assertEquals("lines3D", EChartsGlSeriesTypes.lines3D.unsafeCast<String>())
        assertEquals("surface", EChartsGlSeriesTypes.surface.unsafeCast<String>())
    }

    @Test
    fun effectActionsMatchTheUpstreamNames() {
        assertEquals("lines3DPauseEffect", EChartsGlActions.Lines3DPauseEffect)
        assertEquals("lines3DResumeEffect", EChartsGlActions.Lines3DResumeEffect)
        assertEquals("lines3DToggleEffect", EChartsGlActions.Lines3DToggleEffect)
    }

    @Test
    fun unionsWrapEveryAcceptedForm() {
        assertEquals("auto", GlEnvironments.auto.unsafeCast<String>())
        assertEquals("#000", GlEnvironment("#000").unsafeCast<String>())
        assertEquals("starfield.jpg", GlEnvironment("starfield.jpg").unsafeCast<String>())
        assertEquals("#fff", GlEnvironment(ZRColor("#fff")).unsafeCast<String>())

        assertEquals("auto", BooleanOrAutos.auto.unsafeCast<String>())
        assertEquals(true, BooleanOrAuto(true).unsafeCast<Boolean>())

        assertEquals("auto", DoubleOrAutos.auto.unsafeCast<String>())
        assertEquals(10.0, DoubleOrAuto(10.0).unsafeCast<Double>())

        assertEquals("texture.jpg", TextureSource("texture.jpg").unsafeCast<String>())
        assertEquals("normal.jpg", NumberOrTexture("normal.jpg").unsafeCast<String>())
        assertEquals(0.5, NumberOrTexture(0.5).unsafeCast<Double>())

        assertEquals("red", GlColor(ZRColor("red")).unsafeCast<String>())
    }

    @Test
    fun glColorWrapsAnHdrArrayAndACallback() {
        val rgba = GlColor(arrayOf(0.5, 0.25, 2.0, 1.0)).unsafeCast<Array<Double>>()
        assertEquals(4, rgba.size)
        assertEquals(2.0, rgba[2])

        var dataIndex = -1
        val color =
            GlColor { params ->
                dataIndex = params.dataIndex
                arrayOf(0.0, 0.5, 1.0, 1.0)
            }
        val callback = color.unsafeCast<(CallbackDataParams) -> Array<Double>>()
        val channels = callback(js("({ dataIndex: 7 })") as CallbackDataParams)
        assertEquals(7, dataIndex)
        assertEquals(1.0, channels[2])
    }

    @Test
    fun globeSunTimeWrapsEveryAcceptedForm() {
        val date = Date(0.0)
        assertEquals(date, GlobeSunTime(date).unsafeCast<Date>())
        assertEquals(0.0, GlobeSunTime(0.0).unsafeCast<Double>())
        assertEquals("2026-01-01", GlobeSunTime("2026-01-01").unsafeCast<String>())
    }
}
