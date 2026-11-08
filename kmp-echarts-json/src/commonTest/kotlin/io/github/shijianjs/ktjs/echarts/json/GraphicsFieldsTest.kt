package io.github.shijianjs.ktjs.echarts.json

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.JsonObject

/** Field-level serialization coverage for [Graphics]. */
class GraphicsFieldsTest {

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun graphicOptionFields() {
        val value = GraphicOption()
        value.id = "s"
        value.elements = listOf(GraphicElement())
        value.z = 1.0
        value.zlevel = 1.0
        value.left = num(1.0)
        value.top = num(1.0)
        value.right = num(1.0)
        value.bottom = num(1.0)
        assertEquals(
            setOf(
                "id",
                "elements",
                "z",
                "zlevel",
                "left",
                "top",
                "right",
                "bottom",
            ),
            value.encodeWith(GraphicOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun graphicElementFields() {
        val value = GraphicElement()
        value.type = "s"
        value.id = "s"
        value.name = "s"
        value.left = num(1.0)
        value.top = num(1.0)
        value.right = num(1.0)
        value.bottom = num(1.0)
        value.z = 1.0
        value.silent = true
        value.invisible = true
        value.shape = JsonObject(emptyMap())
        value.style = JsonObject(emptyMap())
        value.textContent = "s"
        value.children = listOf(GraphicElement())
        assertEquals(
            setOf(
                "type",
                "id",
                "name",
                "left",
                "top",
                "right",
                "bottom",
                "z",
                "silent",
                "invisible",
                "shape",
                "style",
                "textContent",
                "children",
            ),
            value.encodeWith(GraphicElement.serializer()).keys,
        )
    }
}
