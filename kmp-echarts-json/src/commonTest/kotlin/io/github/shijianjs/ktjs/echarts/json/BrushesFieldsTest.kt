package io.github.shijianjs.ktjs.echarts.json

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.JsonPrimitive

/** Field-level serialization coverage for [Brushes]. */
class BrushesFieldsTest {

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun brushOptionFields() {
        val value = BrushOption()
        value.toolbox = listOf("s")
        value.brushLink = JsonPrimitive("x")
        value.seriesIndex = listOf(1)
        value.geoIndex = listOf(1)
        value.xAxisIndex = listOf(1)
        value.yAxisIndex = listOf(1)
        value.brushType = "s"
        value.brushMode = "s"
        value.transformable = true
        value.brushStyle = ItemStyleOption()
        value.throttleType = "s"
        value.throttleDelay = 1.0
        value.removeOnClick = true
        value.inBrush = EmphasisOption()
        value.outOfBrush = EmphasisOption()
        value.z = 1.0
        assertEquals(
            setOf(
                "toolbox",
                "brushLink",
                "seriesIndex",
                "geoIndex",
                "xAxisIndex",
                "yAxisIndex",
                "brushType",
                "brushMode",
                "transformable",
                "brushStyle",
                "throttleType",
                "throttleDelay",
                "removeOnClick",
                "inBrush",
                "outOfBrush",
                "z",
            ),
            value.encodeWith(BrushOption.serializer()).keys,
        )
    }
}
