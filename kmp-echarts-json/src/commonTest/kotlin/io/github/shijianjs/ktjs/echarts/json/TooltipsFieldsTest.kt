package io.github.shijianjs.ktjs.echarts.json

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.JsonPrimitive

/** Field-level serialization coverage for [Tooltips]. */
class TooltipsFieldsTest {

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun tooltipOptionFields() {
        val value = TooltipOption()
        value.show = true
        value.trigger = TooltipTrigger.Item
        value.triggerOn = "s"
        value.alwaysShowContent = true
        value.showDelay = 1.0
        value.hideDelay = 1.0
        value.enterable = true
        value.renderMode = TooltipRenderMode.Html
        value.confine = true
        value.appendToBody = true
        value.className = "s"
        value.transitionDuration = 1.0
        value.position = JsonPrimitive("x")
        value.formatter = "s"
        value.backgroundColor = "red".asColor()
        value.borderColor = "red".asColor()
        value.borderWidth = 1.0
        value.padding = NumberOrStringOrNumbers.of(1.0)
        value.textStyle = TextStyleOption()
        value.extraCssText = "s"
        value.order = 1
        value.axisPointer = AxisPointerOption()
        value.z = 1.0
        value.zlevel = 1.0
        value.shadowBlur = 1.0
        value.shadowColor = "red".asColor()
        value.shadowOffsetX = 1.0
        value.shadowOffsetY = 1.0
        assertEquals(
            setOf(
                "show",
                "trigger",
                "triggerOn",
                "alwaysShowContent",
                "showDelay",
                "hideDelay",
                "enterable",
                "renderMode",
                "confine",
                "appendToBody",
                "className",
                "transitionDuration",
                "position",
                "formatter",
                "backgroundColor",
                "borderColor",
                "borderWidth",
                "padding",
                "textStyle",
                "extraCssText",
                "order",
                "axisPointer",
                "z",
                "zlevel",
                "shadowBlur",
                "shadowColor",
                "shadowOffsetX",
                "shadowOffsetY",
            ),
            value.encodeWith(TooltipOption.serializer()).keys,
        )
    }
}
