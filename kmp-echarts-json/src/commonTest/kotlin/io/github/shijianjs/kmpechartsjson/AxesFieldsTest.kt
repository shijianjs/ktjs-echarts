package io.github.shijianjs.kmpechartsjson

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.JsonPrimitive

/** Field-level serialization coverage for [Axes]. */
class AxesFieldsTest {

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun axisOptionFields() {
        val value = AxisOption()
        value.id = "s"
        value.show = true
        value.gridIndex = 1
        value.polarIndex = 1
        value.parallelIndex = 1
        value.position = "s"
        value.offset = 1.0
        value.type = AxisType.Value
        value.name = "s"
        value.nameLocation = "s"
        value.nameTextStyle = TextStyleOption()
        value.nameGap = 1.0
        value.nameRotate = 1.0
        value.nameTruncate = TextTruncateOption()
        value.min = num(1.0)
        value.max = num(1.0)
        value.minInterval = 1.0
        value.maxInterval = 1.0
        value.interval = 1.0
        value.splitNumber = 1
        value.scale = true
        value.logBase = 1.0
        value.boundaryGap = BoundaryGap.of(true)
        value.inverse = true
        value.axisLine = AxisLineOption()
        value.axisTick = AxisTickOption()
        value.axisLabel = AxisLabelOption()
        value.splitLine = SplitLineOption()
        value.splitArea = SplitAreaOption()
        value.axisPointer = AxisPointerOption()
        value.data = listOf(valueOf(1.0))
        value.z = 1.0
        value.zlevel = 1.0
        value.silent = true
        value.triggerEvent = true
        value.animation = true
        value.animationDuration = 1.0
        value.animationEasing = "s"
        assertEquals(
            setOf(
                "id",
                "show",
                "gridIndex",
                "polarIndex",
                "parallelIndex",
                "position",
                "offset",
                "type",
                "name",
                "nameLocation",
                "nameTextStyle",
                "nameGap",
                "nameRotate",
                "nameTruncate",
                "min",
                "max",
                "minInterval",
                "maxInterval",
                "interval",
                "splitNumber",
                "scale",
                "logBase",
                "boundaryGap",
                "inverse",
                "axisLine",
                "axisTick",
                "axisLabel",
                "splitLine",
                "splitArea",
                "axisPointer",
                "data",
                "z",
                "zlevel",
                "silent",
                "triggerEvent",
                "animation",
                "animationDuration",
                "animationEasing",
            ),
            value.encodeWith(AxisOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun textTruncateOptionFields() {
        val value = TextTruncateOption()
        value.maxWidth = 1.0
        value.ellipsis = "s"
        assertEquals(
            setOf(
                "maxWidth",
                "ellipsis",
            ),
            value.encodeWith(TextTruncateOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun axisLabelOptionFields() {
        val value = AxisLabelOption()
        value.show = true
        value.interval = num(1.0)
        value.inside = true
        value.rotate = 1.0
        value.margin = 1.0
        value.formatter = "s"
        value.showMinLabel = true
        value.showMaxLabel = true
        value.hideOverlap = true
        assertEquals(
            setOf(
                "show",
                "interval",
                "inside",
                "rotate",
                "margin",
                "formatter",
                "showMinLabel",
                "showMaxLabel",
                "hideOverlap",
            ),
            value.encodeWith(AxisLabelOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun axisLineOptionFields() {
        val value = AxisLineOption()
        value.show = true
        value.onZero = true
        value.onZeroAxisIndex = 1
        value.symbol = "s"
        value.symbolSize = NumberOrStringOrNumbers.of(1.0)
        value.symbolOffset = listOf(num(1.0))
        value.lineStyle = LineStyleOption()
        assertEquals(
            setOf(
                "show",
                "onZero",
                "onZeroAxisIndex",
                "symbol",
                "symbolSize",
                "symbolOffset",
                "lineStyle",
            ),
            value.encodeWith(AxisLineOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun axisTickOptionFields() {
        val value = AxisTickOption()
        value.show = true
        value.alignWithLabel = true
        value.interval = num(1.0)
        value.inside = true
        value.length = 1.0
        value.lineStyle = LineStyleOption()
        assertEquals(
            setOf(
                "show",
                "alignWithLabel",
                "interval",
                "inside",
                "length",
                "lineStyle",
            ),
            value.encodeWith(AxisTickOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun splitLineOptionFields() {
        val value = SplitLineOption()
        value.show = true
        value.interval = num(1.0)
        value.lineStyle = LineStyleOption()
        assertEquals(
            setOf(
                "show",
                "interval",
                "lineStyle",
            ),
            value.encodeWith(SplitLineOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun splitAreaOptionFields() {
        val value = SplitAreaOption()
        value.show = true
        value.interval = num(1.0)
        value.areaStyle = AreaStyleOption()
        assertEquals(
            setOf(
                "show",
                "interval",
                "areaStyle",
            ),
            value.encodeWith(SplitAreaOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun axisPointerOptionFields() {
        val value = AxisPointerOption()
        value.show = true
        value.type = "s"
        value.axis = "s"
        value.snap = true
        value.z = 1.0
        value.label = AxisPointerLabelOption()
        value.lineStyle = LineStyleOption()
        value.shadowStyle = AreaStyleOption()
        value.handle = JsonPrimitive("x")
        value.link = JsonPrimitive("x")
        value.triggerTooltip = true
        value.triggerOn = "s"
        value.animation = true
        value.animationDurationUpdate = 1.0
        value.animationEasingUpdate = "s"
        assertEquals(
            setOf(
                "show",
                "type",
                "axis",
                "snap",
                "z",
                "label",
                "lineStyle",
                "shadowStyle",
                "handle",
                "link",
                "triggerTooltip",
                "triggerOn",
                "animation",
                "animationDurationUpdate",
                "animationEasingUpdate",
            ),
            value.encodeWith(AxisPointerOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun axisPointerLabelOptionFields() {
        val value = AxisPointerLabelOption()
        value.precision = 1
        value.margin = num(1.0)
        assertEquals(
            setOf(
                "precision",
                "margin",
            ),
            value.encodeWith(AxisPointerLabelOption.serializer()).keys,
        )
    }
}
