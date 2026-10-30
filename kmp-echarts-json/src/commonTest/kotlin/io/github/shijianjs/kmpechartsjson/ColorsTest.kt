package io.github.shijianjs.kmpechartsjson

import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ColorsTest {
    private fun colorJson(color: EChartsColor) = echartsOption {
        series { line { itemStyle { this.color = color } } }
    }.toJsonElement()["series"]!!.jsonArray[0].jsonObject["itemStyle"]!!.jsonObject["color"]!!

    @Test
    fun encodesCssColors() {
        assertEquals("red", colorJson("red".asColor()).jsonPrimitive.content)
        assertEquals(
            "rgba(0, 0, 0, .3)",
            colorJson("rgba(0, 0, 0, .3)".asColor()).jsonPrimitive.content,
        )
    }

    @Test
    fun encodesLinearGradient() {
        val json = colorJson(
            EChartsColor.LinearGradient(
                x = 0.0,
                y = 0.0,
                x2 = 1.0,
                y2 = 1.0,
                colorStops = listOf(
                    GradientColorStop(0.0, "#fff"),
                    GradientColorStop(1.0, "#000"),
                ),
            ),
        ).jsonObject
        assertEquals("linear", json["type"]!!.jsonPrimitive.content)
        assertEquals(1.0, json["x2"]!!.jsonPrimitive.double)
        assertEquals(1.0, json["y2"]!!.jsonPrimitive.double)
        val stops = json["colorStops"]!!.jsonArray
        assertEquals(2, stops.size)
        assertEquals("#fff", stops[0].jsonObject["color"]!!.jsonPrimitive.content)
        assertEquals(1.0, stops[1].jsonObject["offset"]!!.jsonPrimitive.double)
        assertNull(json["global"])
    }

    @Test
    fun encodesRadialGradientDefaults() {
        val json = colorJson(
            EChartsColor.RadialGradient(
                colorStops = listOf(GradientColorStop(0.0, "#fff")),
            ),
        ).jsonObject
        assertEquals("radial", json["type"]!!.jsonPrimitive.content)
        assertEquals(0.5, json["x"]!!.jsonPrimitive.double)
        assertEquals(0.5, json["y"]!!.jsonPrimitive.double)
        assertEquals(0.5, json["r"]!!.jsonPrimitive.double)
    }

    @Test
    fun encodesGlobalGradientFlag() {
        val json = colorJson(
            EChartsColor.LinearGradient(
                global = true,
                colorStops = listOf(GradientColorStop(0.0, "#fff")),
            ),
        ).jsonObject
        assertTrue(json["global"]!!.jsonPrimitive.content.toBoolean())
    }

    @Test
    fun encodesPattern() {
        val json = colorJson(EChartsColor.Pattern("data:image/png;base64,AAA")).jsonObject
        assertEquals("data:image/png;base64,AAA", json["image"]!!.jsonPrimitive.content)
        assertNull(json["repeat"])
        assertFalse(json.containsKey("type"))
    }

    @Test
    fun roundTripsColors() {
        val colors = listOf(
            EChartsColor.Css("#5470c6"),
            EChartsColor.LinearGradient(
                x = 0.1,
                y = 0.2,
                x2 = 0.9,
                y2 = 0.8,
                colorStops = listOf(GradientColorStop(0.25, "red")),
                global = false,
            ),
            EChartsColor.RadialGradient(
                colorStops = listOf(GradientColorStop(1.0, "blue")),
            ),
            EChartsColor.Pattern("pattern.png", repeat = "repeat-x"),
        )
        for (color in colors) {
            val text = EChartsJson.format.encodeToString(EChartsColorSerializer, color)
            assertEquals(color, EChartsJson.format.decodeFromString(EChartsColorSerializer, text))
        }
    }

    @Test
    fun paletteIsEncodedAsAnArray() {
        val palette = echartsOption { color = colors("#5470c6", "#91cc75") }
            .toJsonElement()["color"]!!.jsonArray
        assertEquals(
            listOf("#5470c6", "#91cc75"),
            palette.map { it.jsonPrimitive.content },
        )
    }
}
