package io.github.shijianjs.ktjs.echarts.json

import kotlin.test.Test
import kotlin.test.assertEquals

/** Field-level serialization coverage for [Grids]. */
class GridsFieldsTest {

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun gridOptionFields() {
        val value = GridOption()
        value.id = "s"
        value.show = true
        value.z = 1.0
        value.zlevel = 1.0
        value.left = num(1.0)
        value.top = num(1.0)
        value.right = num(1.0)
        value.bottom = num(1.0)
        value.width = num(1.0)
        value.height = num(1.0)
        value.containLabel = true
        value.backgroundColor = "red".asColor()
        value.borderColor = "red".asColor()
        value.borderWidth = 1.0
        value.shadowBlur = 1.0
        value.shadowColor = "red".asColor()
        value.shadowOffsetX = 1.0
        value.shadowOffsetY = 1.0
        value.tooltip = TooltipOption()
        assertEquals(
            setOf(
                "id",
                "show",
                "z",
                "zlevel",
                "left",
                "top",
                "right",
                "bottom",
                "width",
                "height",
                "containLabel",
                "backgroundColor",
                "borderColor",
                "borderWidth",
                "shadowBlur",
                "shadowColor",
                "shadowOffsetX",
                "shadowOffsetY",
                "tooltip",
            ),
            value.encodeWith(GridOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun polarOptionFields() {
        val value = PolarOption()
        value.id = "s"
        value.z = 1.0
        value.zlevel = 1.0
        value.center = listOf(num(1.0))
        value.radius = NumberOrStringOrNumbers.of(1.0)
        assertEquals(
            setOf(
                "id",
                "z",
                "zlevel",
                "center",
                "radius",
            ),
            value.encodeWith(PolarOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun parallelOptionFields() {
        val value = ParallelOption()
        value.id = "s"
        value.z = 1.0
        value.zlevel = 1.0
        value.left = num(1.0)
        value.top = num(1.0)
        value.right = num(1.0)
        value.bottom = num(1.0)
        value.width = num(1.0)
        value.height = num(1.0)
        value.parallelAxisDefault = AxisOption()
        assertEquals(
            setOf(
                "id",
                "z",
                "zlevel",
                "left",
                "top",
                "right",
                "bottom",
                "width",
                "height",
                "parallelAxisDefault",
            ),
            value.encodeWith(ParallelOption.serializer()).keys,
        )
    }
}
