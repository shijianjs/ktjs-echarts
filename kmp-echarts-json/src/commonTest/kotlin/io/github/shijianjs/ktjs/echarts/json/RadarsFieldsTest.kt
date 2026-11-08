package io.github.shijianjs.ktjs.echarts.json

import kotlin.test.Test
import kotlin.test.assertEquals

/** Field-level serialization coverage for [Radars]. */
class RadarsFieldsTest {

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun radarOptionFields() {
        val value = RadarOption()
        value.id = "s"
        value.z = 1.0
        value.zlevel = 1.0
        value.center = listOf(num(1.0))
        value.radius = NumberOrStringOrNumbers.of(1.0)
        value.startAngle = 1.0
        value.shape = "s"
        value.axisName = LabelOption()
        value.nameGap = 1.0
        value.splitNumber = 1
        value.scale = true
        value.axisLine = AxisLineOption()
        value.axisTick = AxisTickOption()
        value.axisLabel = AxisLabelOption()
        value.splitLine = SplitLineOption()
        value.splitArea = SplitAreaOption()
        value.indicator = listOf(RadarIndicator())
        value.triggerEvent = true
        value.silent = true
        assertEquals(
            setOf(
                "id",
                "z",
                "zlevel",
                "center",
                "radius",
                "startAngle",
                "shape",
                "axisName",
                "nameGap",
                "splitNumber",
                "scale",
                "axisLine",
                "axisTick",
                "axisLabel",
                "splitLine",
                "splitArea",
                "indicator",
                "triggerEvent",
                "silent",
            ),
            value.encodeWith(RadarOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun radarIndicatorFields() {
        val value = RadarIndicator()
        value.name = "s"
        value.max = 1.0
        value.min = 1.0
        value.color = "red".asColor()
        assertEquals(
            setOf(
                "name",
                "max",
                "min",
                "color",
            ),
            value.encodeWith(RadarIndicator.serializer()).keys,
        )
    }
}
