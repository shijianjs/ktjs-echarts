package io.github.shijianjs.ktjs.echarts.json

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.JsonPrimitive

/** Field-level serialization coverage for [Calendars]. */
class CalendarsFieldsTest {

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun calendarOptionFields() {
        val value = CalendarOption()
        value.id = "s"
        value.range = JsonPrimitive("x")
        value.orient = Orient.Horizontal
        value.cellSize = NumberOrStringOrNumbers.of(1.0)
        value.width = num(1.0)
        value.height = num(1.0)
        value.top = num(1.0)
        value.left = num(1.0)
        value.right = num(1.0)
        value.bottom = num(1.0)
        value.dayLabel = CalendarLabelOption()
        value.monthLabel = CalendarLabelOption()
        value.yearLabel = CalendarLabelOption()
        value.itemStyle = ItemStyleOption()
        value.splitLine = CalendarSplitLineOption()
        value.z = 1.0
        value.zlevel = 1.0
        value.silent = true
        assertEquals(
            setOf(
                "id",
                "range",
                "orient",
                "cellSize",
                "width",
                "height",
                "top",
                "left",
                "right",
                "bottom",
                "dayLabel",
                "monthLabel",
                "yearLabel",
                "itemStyle",
                "splitLine",
                "z",
                "zlevel",
                "silent",
            ),
            value.encodeWith(CalendarOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun calendarLabelOptionFields() {
        val value = CalendarLabelOption()
        value.show = true
        value.position = "s"
        value.margin = 1.0
        assertEquals(
            setOf(
                "show",
                "position",
                "margin",
            ),
            value.encodeWith(CalendarLabelOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun calendarSplitLineOptionFields() {
        val value = CalendarSplitLineOption()
        value.show = true
        value.lineStyle = LineStyleOption()
        assertEquals(
            setOf(
                "show",
                "lineStyle",
            ),
            value.encodeWith(CalendarSplitLineOption.serializer()).keys,
        )
    }
}
