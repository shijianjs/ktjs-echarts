package io.github.shijianjs.ktjs.echarts.json

import kotlin.test.Test
import kotlin.test.assertEquals

/** Field-level serialization coverage for [Titles]. */
class TitlesFieldsTest {

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun titleOptionFields() {
        val value = TitleOption()
        value.id = "s"
        value.show = true
        value.text = "s"
        value.subtext = "s"
        value.link = "s"
        value.sublink = "s"
        value.target = "s"
        value.textAlign = "s"
        value.textVerticalAlign = "s"
        value.triggerEvent = true
        value.padding = NumberOrStringOrNumbers.of(1.0)
        value.itemGap = 1.0
        value.textStyle = TextStyleOption()
        value.subtextStyle = TextStyleOption()
        value.z = 1.0
        value.zlevel = 1.0
        value.left = num(1.0)
        value.top = num(1.0)
        value.right = num(1.0)
        value.bottom = num(1.0)
        value.width = num(1.0)
        value.height = num(1.0)
        value.backgroundColor = "red".asColor()
        value.borderColor = "red".asColor()
        value.borderWidth = 1.0
        value.borderRadius = NumberOrStringOrNumbers.of(1.0)
        value.shadowBlur = 1.0
        value.shadowColor = "red".asColor()
        value.shadowOffsetX = 1.0
        value.shadowOffsetY = 1.0
        assertEquals(
            setOf(
                "id",
                "show",
                "text",
                "subtext",
                "link",
                "sublink",
                "target",
                "textAlign",
                "textVerticalAlign",
                "triggerEvent",
                "padding",
                "itemGap",
                "textStyle",
                "subtextStyle",
                "z",
                "zlevel",
                "left",
                "top",
                "right",
                "bottom",
                "width",
                "height",
                "backgroundColor",
                "borderColor",
                "borderWidth",
                "borderRadius",
                "shadowBlur",
                "shadowColor",
                "shadowOffsetX",
                "shadowOffsetY",
            ),
            value.encodeWith(TitleOption.serializer()).keys,
        )
    }
}
