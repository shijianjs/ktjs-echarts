package io.github.shijianjs.kmpechartsjson

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.JsonPrimitive

/** Field-level serialization coverage for [VisualMaps]. */
class VisualMapsFieldsTest {

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun visualMapOptionFields() {
        val value = VisualMapOption()
        value.type = VisualMapType.Continuous
        value.show = true
        value.id = "s"
        value.seriesIndex = listOf(1)
        value.min = 1.0
        value.max = 1.0
        value.minOpen = true
        value.maxOpen = true
        value.splitNumber = 1
        value.pieces = listOf(VisualMapPiece())
        value.orient = Orient.Horizontal
        value.itemWidth = 1.0
        value.itemHeight = 1.0
        value.align = "s"
        value.text = listOf("s")
        value.textGap = 1.0
        value.precision = 1
        value.itemSymbol = "s"
        value.dimension = num(1.0)
        value.inRange = VisualMapRange()
        value.outOfRange = VisualMapRange()
        value.controller = JsonPrimitive("x")
        value.calculable = true
        value.realtime = true
        value.selectedMode = BooleanOrString.of(true)
        value.hoverLink = true
        value.range = listOf(1.0)
        value.z = 1.0
        value.zlevel = 1.0
        value.left = num(1.0)
        value.top = num(1.0)
        value.right = num(1.0)
        value.bottom = num(1.0)
        value.textStyle = TextStyleOption()
        value.itemStyle = ItemStyleOption()
        value.emphasis = EmphasisOption()
        assertEquals(
            setOf(
                "type",
                "show",
                "id",
                "seriesIndex",
                "min",
                "max",
                "minOpen",
                "maxOpen",
                "splitNumber",
                "pieces",
                "orient",
                "itemWidth",
                "itemHeight",
                "align",
                "text",
                "textGap",
                "precision",
                "itemSymbol",
                "dimension",
                "inRange",
                "outOfRange",
                "controller",
                "calculable",
                "realtime",
                "selectedMode",
                "hoverLink",
                "range",
                "z",
                "zlevel",
                "left",
                "top",
                "right",
                "bottom",
                "textStyle",
                "itemStyle",
                "emphasis",
            ),
            value.encodeWith(VisualMapOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun visualMapPieceFields() {
        val value = VisualMapPiece()
        value.min = 1.0
        value.max = 1.0
        value.lt = 1.0
        value.lte = 1.0
        value.gt = 1.0
        value.gte = 1.0
        value.value = 1.0
        value.label = "s"
        value.color = "red".asColor()
        value.symbolSize = NumberOrStringOrNumbers.of(1.0)
        value.opacity = 1.0
        assertEquals(
            setOf(
                "min",
                "max",
                "lt",
                "lte",
                "gt",
                "gte",
                "value",
                "label",
                "color",
                "symbolSize",
                "opacity",
            ),
            value.encodeWith(VisualMapPiece.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun visualMapRangeFields() {
        val value = VisualMapRange()
        value.color = listOf("red".asColor())
        value.colorAlpha = listOf(1.0)
        value.opacity = listOf(1.0)
        value.symbolSize = listOf(1.0)
        value.colorLightness = listOf(1.0)
        value.colorSaturation = listOf(1.0)
        value.colorHue = listOf(1.0)
        value.symbol = "s"
        assertEquals(
            setOf(
                "color",
                "colorAlpha",
                "opacity",
                "symbolSize",
                "colorLightness",
                "colorSaturation",
                "colorHue",
                "symbol",
            ),
            value.encodeWith(VisualMapRange.serializer()).keys,
        )
    }
}
