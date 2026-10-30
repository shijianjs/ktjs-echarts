package io.github.shijianjs.kmpechartsjson

import kotlin.test.Test
import kotlin.test.assertEquals

/** Field-level serialization coverage for [Legends]. */
class LegendsFieldsTest {

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun legendOptionFields() {
        val value = LegendOption()
        value.type = LegendType.Plain
        value.id = "s"
        value.show = true
        value.z = 1.0
        value.zlevel = 1.0
        value.left = num(1.0)
        value.top = num(1.0)
        value.right = num(1.0)
        value.bottom = num(1.0)
        value.orient = Orient.Horizontal
        value.align = "s"
        value.padding = NumberOrStringOrNumbers.of(1.0)
        value.itemGap = 1.0
        value.itemWidth = 1.0
        value.itemHeight = 1.0
        value.itemStyle = ItemStyleOption()
        value.lineStyle = LineStyleOption()
        value.symbol = "s"
        value.symbolSize = NumberOrStringOrNumbers.of(1.0)
        value.symbolRotate = 1.0
        value.symbolKeepAspect = true
        value.formatter = "s"
        value.selectedMode = BooleanOrString.of(true)
        value.textStyle = TextStyleOption()
        value.data = listOf(LegendItem.Name("n"))
        value.backgroundColor = "red".asColor()
        value.borderColor = "red".asColor()
        value.borderWidth = 1.0
        value.borderRadius = NumberOrStringOrNumbers.of(1.0)
        value.shadowBlur = 1.0
        value.shadowColor = "red".asColor()
        value.shadowOffsetX = 1.0
        value.shadowOffsetY = 1.0
        value.scrollDataIndex = 1
        value.pageButtonItemGap = 1.0
        value.pageButtonSize = NumberOrStringOrNumbers.of(1.0)
        value.pageButtonPosition = "s"
        value.pageFormatter = "s"
        value.pageIcons = LegendPageIcons()
        value.pageIconColor = "red".asColor()
        value.pageIconInactiveColor = "red".asColor()
        value.pageIconSize = NumberOrStringOrNumbers.of(1.0)
        value.pageTextStyle = TextStyleOption()
        value.animation = true
        value.animationDuration = 1.0
        value.animationEasing = "s"
        value.animationDurationUpdate = 1.0
        value.animationEasingUpdate = "s"
        assertEquals(
            setOf(
                "type",
                "id",
                "show",
                "z",
                "zlevel",
                "left",
                "top",
                "right",
                "bottom",
                "orient",
                "align",
                "padding",
                "itemGap",
                "itemWidth",
                "itemHeight",
                "itemStyle",
                "lineStyle",
                "symbol",
                "symbolSize",
                "symbolRotate",
                "symbolKeepAspect",
                "formatter",
                "selectedMode",
                "textStyle",
                "data",
                "backgroundColor",
                "borderColor",
                "borderWidth",
                "borderRadius",
                "shadowBlur",
                "shadowColor",
                "shadowOffsetX",
                "shadowOffsetY",
                "scrollDataIndex",
                "pageButtonItemGap",
                "pageButtonSize",
                "pageButtonPosition",
                "pageFormatter",
                "pageIcons",
                "pageIconColor",
                "pageIconInactiveColor",
                "pageIconSize",
                "pageTextStyle",
                "animation",
                "animationDuration",
                "animationEasing",
                "animationDurationUpdate",
                "animationEasingUpdate",
            ),
            value.encodeWith(LegendOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun legendPageIconsFields() {
        val value = LegendPageIcons()
        value.horizontal = listOf("s")
        value.vertical = listOf("s")
        assertEquals(
            setOf(
                "horizontal",
                "vertical",
            ),
            value.encodeWith(LegendPageIcons.serializer()).keys,
        )
    }
}
