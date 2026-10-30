package io.github.shijianjs.kmpechartsjson

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

/** Field-level serialization coverage for [Aria]. */
class AriaFieldsTest {

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun ariaOptionFields() {
        val value = AriaOption()
        value.enabled = true
        value.label = AriaLabelOption()
        value.decal = AriaDecalOption()
        assertEquals(
            setOf(
                "enabled",
                "label",
                "decal",
            ),
            value.encodeWith(AriaOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun ariaLabelOptionFields() {
        val value = AriaLabelOption()
        value.enabled = true
        value.description = "s"
        value.general = JsonObject(emptyMap())
        assertEquals(
            setOf(
                "enabled",
                "description",
                "general",
            ),
            value.encodeWith(AriaLabelOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun ariaDecalOptionFields() {
        val value = AriaDecalOption()
        value.show = true
        value.decals = listOf(JsonPrimitive("x"))
        assertEquals(
            setOf(
                "show",
                "decals",
            ),
            value.encodeWith(AriaDecalOption.serializer()).keys,
        )
    }
}
