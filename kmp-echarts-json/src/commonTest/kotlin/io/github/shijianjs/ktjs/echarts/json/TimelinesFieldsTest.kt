package io.github.shijianjs.ktjs.echarts.json

import kotlin.test.Test
import kotlin.test.assertEquals

/** Field-level serialization coverage for [Timelines]. */
class TimelinesFieldsTest {

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun timelineOptionFields() {
        val value = TimelineOption()
        value.id = "s"
        value.show = true
        value.type = "s"
        value.axisType = AxisType.Value
        value.currentIndex = 1
        value.autoPlay = true
        value.rewind = true
        value.loop = true
        value.playbackInterval = 1.0
        value.realtime = true
        value.controlPosition = "s"
        value.z = 1.0
        value.zlevel = 1.0
        value.left = num(1.0)
        value.top = num(1.0)
        value.right = num(1.0)
        value.bottom = num(1.0)
        value.width = num(1.0)
        value.height = num(1.0)
        value.padding = NumberOrStringOrNumbers.of(1.0)
        value.orient = Orient.Horizontal
        value.inverse = true
        value.symbol = "s"
        value.symbolSize = NumberOrStringOrNumbers.of(1.0)
        value.symbolRotate = 1.0
        value.symbolKeepAspect = true
        value.symbolOffset = listOf(num(1.0))
        value.lineStyle = LineStyleOption()
        value.label = LabelOption()
        value.itemStyle = ItemStyleOption()
        value.checkpointStyle = TimelineCheckpointStyle()
        value.controlStyle = TimelineControlStyle()
        value.progress = TimelineProgress()
        value.data = listOf(valueOf(1.0))
        value.animation = true
        value.animationDuration = 1.0
        value.animationEasing = "s"
        value.animationDurationUpdate = 1.0
        value.animationEasingUpdate = "s"
        assertEquals(
            setOf(
                "id",
                "show",
                "type",
                "axisType",
                "currentIndex",
                "autoPlay",
                "rewind",
                "loop",
                "playbackInterval",
                "realtime",
                "controlPosition",
                "z",
                "zlevel",
                "left",
                "top",
                "right",
                "bottom",
                "width",
                "height",
                "padding",
                "orient",
                "inverse",
                "symbol",
                "symbolSize",
                "symbolRotate",
                "symbolKeepAspect",
                "symbolOffset",
                "lineStyle",
                "label",
                "itemStyle",
                "checkpointStyle",
                "controlStyle",
                "progress",
                "data",
                "animation",
                "animationDuration",
                "animationEasing",
                "animationDurationUpdate",
                "animationEasingUpdate",
            ),
            value.encodeWith(TimelineOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun timelineCheckpointStyleFields() {
        val value = TimelineCheckpointStyle()
        value.symbol = "s"
        value.symbolSize = NumberOrStringOrNumbers.of(1.0)
        value.color = "red".asColor()
        value.borderColor = "red".asColor()
        value.borderWidth = 1.0
        value.animation = true
        value.animationDuration = 1.0
        value.animationEasing = "s"
        value.label = LabelOption()
        assertEquals(
            setOf(
                "symbol",
                "symbolSize",
                "color",
                "borderColor",
                "borderWidth",
                "animation",
                "animationDuration",
                "animationEasing",
                "label",
            ),
            value.encodeWith(TimelineCheckpointStyle.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun timelineControlStyleFields() {
        val value = TimelineControlStyle()
        value.show = true
        value.showPlayBtn = true
        value.showPrevBtn = true
        value.showNextBtn = true
        value.itemSize = 1.0
        value.itemGap = 1.0
        value.position = "s"
        value.color = "red".asColor()
        value.borderColor = "red".asColor()
        value.borderWidth = 1.0
        value.borderRadius = NumberOrStringOrNumbers.of(1.0)
        assertEquals(
            setOf(
                "show",
                "showPlayBtn",
                "showPrevBtn",
                "showNextBtn",
                "itemSize",
                "itemGap",
                "position",
                "color",
                "borderColor",
                "borderWidth",
                "borderRadius",
            ),
            value.encodeWith(TimelineControlStyle.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun timelineProgressFields() {
        val value = TimelineProgress()
        value.lineStyle = LineStyleOption()
        value.itemStyle = ItemStyleOption()
        value.label = LabelOption()
        assertEquals(
            setOf(
                "lineStyle",
                "itemStyle",
                "label",
            ),
            value.encodeWith(TimelineProgress.serializer()).keys,
        )
    }
}
