package io.github.shijianjs.kmpechartsjson

import kotlin.test.Test
import kotlin.test.assertEquals

/** Field-level serialization coverage for [Values]. */
class ValuesFieldsTest {

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun dataItemFields() {
        val value = DataItem()
        value.name = "s"
        value.value = valueOf(1.0)
        value.id = "s"
        value.groupId = "s"
        value.selected = true
        value.itemStyle = ItemStyleOption()
        value.label = LabelOption()
        value.emphasis = EmphasisOption()
        value.symbol = "s"
        value.symbolSize = NumberOrStringOrNumbers.of(1.0)
        value.symbolRotate = 1.0
        value.symbolOffset = listOf(num(1.0))
        value.children = listOf(DataItem())
        value.collapsed = true
        assertEquals(
            setOf(
                "name",
                "value",
                "id",
                "groupId",
                "selected",
                "itemStyle",
                "label",
                "emphasis",
                "symbol",
                "symbolSize",
                "symbolRotate",
                "symbolOffset",
                "children",
                "collapsed",
            ),
            value.encodeWith(DataItem.serializer()).keys,
        )
    }
}
