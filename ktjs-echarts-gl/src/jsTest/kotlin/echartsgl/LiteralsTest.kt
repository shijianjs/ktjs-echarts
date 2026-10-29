package echartsgl

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
    fun seriesTypesMatchTheUpstreamNames() {
        assertEquals("bar3D", EChartsGlSeriesTypes.bar3D.unsafeCast<String>())
    }

    @Test
    fun unionsWrapEveryAcceptedForm() {
        assertEquals("auto", GlEnvironments.auto.unsafeCast<String>())
        assertEquals("#000", GlEnvironment("#000").unsafeCast<String>())
        assertEquals("starfield.jpg", GlEnvironment("starfield.jpg").unsafeCast<String>())
        assertEquals("#fff", GlEnvironment(ZRColor("#fff")).unsafeCast<String>())

        assertEquals("auto", BooleanOrAutos.auto.unsafeCast<String>())
        assertEquals(true, BooleanOrAuto(true).unsafeCast<Boolean>())

        assertEquals("texture.jpg", TextureSource("texture.jpg").unsafeCast<String>())
        assertEquals("normal.jpg", NumberOrTexture("normal.jpg").unsafeCast<String>())
        assertEquals(0.5, NumberOrTexture(0.5).unsafeCast<Double>())

        assertEquals("red", GlColor(ZRColor("red")).unsafeCast<String>())
    }
}
