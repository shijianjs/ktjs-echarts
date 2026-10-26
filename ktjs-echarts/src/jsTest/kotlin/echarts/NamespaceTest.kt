package echarts

import echarts.options.StringOrDouble
import kotlin.js.JsAny
import kotlin.js.json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Tests for the namespace objects re-exported by the `echarts` package.
 *
 * The namespaces are documented in the "API" section of the ECharts documentation; the assertions
 * below pin the return shapes that the wrapper exposes.
 */
class NamespaceTest {
    @Test
    fun formatNamespaceHelpers() {
        assertEquals("1,234,567", ECharts.format.addCommas(StringOrDouble(1234567.0)))
        assertEquals("12.5", ECharts.format.addCommas(StringOrDouble(12.5)))
        assertEquals("aB", ECharts.format.toCamelCase("a-b"))
        assertEquals("AB", ECharts.format.toCamelCase("a-b", true))
        assertEquals("&lt;a&gt;", ECharts.format.encodeHTML("<a>"))
        assertEquals("Abc", ECharts.format.capitalFirst("abc"))
        assertEquals("Abc", ECharts.format.capitalFirst("Abc"))
    }

    @Test
    fun formatTemplateAndTimeHelpers() {
        val formatted = ECharts.format.formatTpl(
            "{a}: {b} = {c}",
            json("\$vars" to arrayOf("a", "b", "c"), "a" to "series", "b" to "Jan", "c" to 12),
        )
        assertEquals("series: Jan = 12", formatted)

        val marker = ECharts.format.getTooltipMarker("#ff0000")
        assertTrue(marker.contains("#ff0000"))
        assertTrue(marker.contains("<span"))
    }

    @Test
    fun formatNormalizeCssArrayExpandsShorthands() {
        val single = ECharts.format.normalizeCssArray(StringOrDouble(5.0))
        assertEquals(4, single.size)
        assertEquals(5.0, single[0])
        assertEquals(5.0, single[3])
    }

    @Test
    fun timeNamespaceHelpers() {
        val epoch = ECharts.time.parse(0.0)
        assertEquals(1970, epoch.getUTCFullYear())
        assertEquals(0, epoch.getUTCMonth())

        assertEquals("1970-01-01", ECharts.time.format(0.0, "{yyyy}-{MM}-{dd}", true))
        assertEquals("1970-01-01 00:00:00", ECharts.time.format(0.0, "{yyyy}-{MM}-{dd} {HH}:{mm}:{ss}", true))

        val parsed = ECharts.time.parse("2020-06-15T10:20:30Z")
        assertFalse(parsed.getTime().isNaN())

        val rounded = ECharts.time.roundTime(parsed, "month", true)
        assertEquals(2020, rounded.getUTCFullYear())
        assertEquals(5, rounded.getUTCMonth())
        assertEquals(1, rounded.getUTCDate())
    }

    @Test
    fun numberNamespaceHelpers() {
        assertEquals(50.0, ECharts.number.linearMap(5.0, arrayOf(0.0, 10.0), arrayOf(0.0, 100.0)))
        assertEquals(100.0, ECharts.number.linearMap(20.0, arrayOf(0.0, 10.0), arrayOf(0.0, 100.0), true))
        assertEquals(1.23, ECharts.number.round(StringOrDouble(1.2345), 2.0))
        assertEquals(2.0, ECharts.number.getPrecision(StringOrDouble(1.23)))
        assertTrue(ECharts.number.isNumeric("12"))
        assertFalse(ECharts.number.isNumeric("abc"))
        assertEquals(9007199254740991.0, ECharts.number.MAX_SAFE_INTEGER)
    }

    @Test
    fun colorNamespaceHelpers() {
        val red = ECharts.color.parse("#ff0000")
        assertEquals(4, red.size)
        assertEquals(255.0, red[0])
        assertEquals(0.0, red[1])
        assertEquals(0.0, red[2])
        assertEquals(1.0, red[3])

        val translucent = ECharts.color.parse("rgba(0, 128, 255, 0.5)")
        assertEquals(128.0, translucent[1])
        assertEquals(0.5, translucent[3])

        assertEquals("ff0000", ECharts.color.toHex("red"))
        assertTrue(ECharts.color.lift("#000000", 0.5).isNotEmpty())
        assertEquals("rgba(255,0,0,0.5)", ECharts.color.modifyAlpha("#ff0000", 0.5))
    }

    @Test
    fun graphicNamespaceExposesShapeClasses() {
        assertEquals("function", kotlin.js.jsTypeOf(ECharts.graphic.getShapeClass("rect")))
        assertEquals("function", kotlin.js.jsTypeOf(ECharts.graphic.getShapeClass("circle")))
    }

    @Test
    fun helperNamespaceComputesLayoutRects() {
        val rect = ECharts.helper.getLayoutRect(
            json("left" to 0, "top" to 0, "width" to 100, "height" to 50),
            json("width" to 200, "height" to 100),
        ).unsafeCast<TestRect>()
        assertEquals(0.0, rect.x)
        assertEquals(0.0, rect.y)
        assertEquals(100.0, rect.width)
        assertEquals(50.0, rect.height)
    }
}

private external interface TestRect : JsAny {
    val x: Double
    val y: Double
    val width: Double
    val height: Double
}
