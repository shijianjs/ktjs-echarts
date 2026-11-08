package io.github.shijianjs.ktjs.echarts.json

import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class EChartsJsonTest {
    @Test
    fun omitsUnsetKeys() {
        assertEquals("{}", echartsOption { }.toJsonString())
    }

    @Test
    fun omitsNestedUnsetKeys() {
        val element = echartsOption { title { text = "Sales" } }.toJsonElement()
        val title = element["title"]!!.jsonArray[0].jsonObject
        assertEquals("Sales", title["text"]!!.jsonPrimitive.content)
        assertEquals(listOf("text"), title.keys.toList())
    }

    @Test
    fun encodesBooleansThatMatchTheDefault() {
        val element = echartsOption { animation = false }.toJsonElement()
        assertFalse(element["animation"]!!.jsonPrimitive.boolean)
    }

    @Test
    fun encodeMatchesEncodeToJsonElement() {
        val option = echartsOption {
            title { text = "Sales" }
            series { bar { values(1, 2, 3) } }
        }
        assertEquals(
            EChartsJson.encodeToJsonElement(option).toString(),
            EChartsJson.encode(option),
        )
        assertEquals(option.toJsonElement(), EChartsJson.encodeToJsonElement(option))
    }

    @Test
    fun prettyPrintIndentsTheOutput() {
        val text = echartsOption { title { text = "Sales" } }.toJsonString(prettyPrint = true)
        assertTrue(text.contains("\n"))
        assertTrue(text.contains("\n  \"title\""))
    }

    @Test
    fun mergesExtraKeysAtTheTopLevel() {
        val option = echartsOption { animation = false }
        option.extra = buildJsonObject {
            put("myPlugin", 1)
            put("animation", true)
        }
        val element = option.toJsonElement()
        assertEquals(1, element["myPlugin"]!!.jsonPrimitive.int)
        assertTrue(element["animation"]!!.jsonPrimitive.boolean)
    }

    @Test
    fun withReplacesAnExistingKey() {
        val base = buildJsonObject { put("a", 1) }
        assertEquals(2, base.with("a", 2)["a"]!!.jsonPrimitive.int)
        assertEquals("x", base.with("b", "x")["b"]!!.jsonPrimitive.content)
        assertEquals(1, base["a"]!!.jsonPrimitive.int)
    }

    @Test
    fun plusMergesTopLevelEntries() {
        val base = buildJsonObject { put("a", 1) }
        val merged = base + mapOf("b" to JsonPrimitive(2))
        assertEquals(1, merged["a"]!!.jsonPrimitive.int)
        assertEquals(2, merged["b"]!!.jsonPrimitive.int)
    }

    @Test
    fun deepMergeKeepsSiblingKeys() {
        val base = buildJsonObject {
            put(
                "grid",
                buildJsonObject {
                    put("left", 10)
                    put("top", 20)
                },
            )
            put("animation", true)
        }
        val merge = buildJsonObject {
            put(
                "grid",
                buildJsonObject {
                    put("left", 30)
                },
            )
        }
        val merged = base.deepMerge(merge)
        val grid = merged["grid"]!!.jsonObject
        assertEquals(30, grid["left"]!!.jsonPrimitive.int)
        assertEquals(20, grid["top"]!!.jsonPrimitive.int)
        assertTrue(merged["animation"]!!.jsonPrimitive.boolean)
    }

    @Test
    fun prettyPrintSerializesAnyElement() {
        val text = buildJsonObject { put("a", 1) }.prettyPrint()
        assertTrue(text.contains("\n"))
        assertTrue(text.contains("\"a\""))
    }

    @Test
    fun objectStringUsesTheFormattingOption() {
        val obj = buildJsonObject { put("a", 1) }
        assertFalse(obj.toEChartsJsonString().contains("\n"))
        assertTrue(obj.toEChartsJsonString(prettyPrint = true).contains("\n"))
    }

    @Test
    fun unsetExtraLeavesTheElementUnchanged() {
        val element = echartsOption { animation = true }.toJsonElement()
        assertNull(element["extra"])
        assertEquals(1, element.size)
    }
}
