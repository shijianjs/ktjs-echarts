package io.github.shijianjs.kmpechartsjson

import kotlin.test.Test
import kotlin.test.assertEquals

/** Field-level serialization coverage for [Styles]. */
class StylesFieldsTest {

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun textStyleOptionFields() {
        val value = TextStyleOption()
        value.color = "red".asColor()
        value.fontStyle = "s"
        value.fontWeight = num(1.0)
        value.fontFamily = "s"
        value.fontSize = 1.0
        value.lineHeight = 1.0
        value.width = num(1.0)
        value.height = num(1.0)
        value.align = "s"
        value.verticalAlign = "s"
        value.backgroundColor = "red".asColor()
        value.borderColor = "red".asColor()
        value.borderWidth = 1.0
        value.borderType = "s"
        value.borderDashOffset = 1.0
        value.borderRadius = NumberOrStringOrNumbers.of(1.0)
        value.padding = NumberOrStringOrNumbers.of(1.0)
        value.shadowBlur = 1.0
        value.shadowColor = "red".asColor()
        value.shadowOffsetX = 1.0
        value.shadowOffsetY = 1.0
        value.textBorderColor = "red".asColor()
        value.textBorderWidth = 1.0
        value.textBorderType = "s"
        value.textBorderDashOffset = 1.0
        value.textShadowBlur = 1.0
        value.textShadowColor = "red".asColor()
        value.textShadowOffsetX = 1.0
        value.textShadowOffsetY = 1.0
        value.overflow = "s"
        value.ellipsis = "s"
        value.rich = mapOf("k" to TextStyleOption())
        assertEquals(
            setOf(
                "color",
                "fontStyle",
                "fontWeight",
                "fontFamily",
                "fontSize",
                "lineHeight",
                "width",
                "height",
                "align",
                "verticalAlign",
                "backgroundColor",
                "borderColor",
                "borderWidth",
                "borderType",
                "borderDashOffset",
                "borderRadius",
                "padding",
                "shadowBlur",
                "shadowColor",
                "shadowOffsetX",
                "shadowOffsetY",
                "textBorderColor",
                "textBorderWidth",
                "textBorderType",
                "textBorderDashOffset",
                "textShadowBlur",
                "textShadowColor",
                "textShadowOffsetX",
                "textShadowOffsetY",
                "overflow",
                "ellipsis",
                "rich",
            ),
            value.encodeWith(TextStyleOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun labelOptionFields() {
        val value = LabelOption()
        value.show = true
        value.position = "s"
        value.distance = 1.0
        value.rotate = 1.0
        value.offset = listOf(num(1.0))
        value.formatter = "s"
        value.valueAnimation = true
        value.alignTo = "s"
        value.bleedMargin = 1.0
        assertEquals(
            setOf(
                "show",
                "position",
                "distance",
                "rotate",
                "offset",
                "formatter",
                "valueAnimation",
                "alignTo",
                "bleedMargin",
            ),
            value.encodeWith(LabelOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun labelLineOptionFields() {
        val value = LabelLineOption()
        value.show = true
        value.showAbove = true
        value.length = 1.0
        value.length2 = num(1.0)
        value.smooth = NumberOrStringOrNumbers.of(1.0)
        value.minTurnAngle = 1.0
        value.lineStyle = LineStyleOption()
        assertEquals(
            setOf(
                "show",
                "showAbove",
                "length",
                "length2",
                "smooth",
                "minTurnAngle",
                "lineStyle",
            ),
            value.encodeWith(LabelLineOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun itemStyleOptionFields() {
        val value = ItemStyleOption()
        value.color = "red".asColor()
        value.borderColor = "red".asColor()
        value.borderWidth = 1.0
        value.borderType = "s"
        value.borderDashOffset = 1.0
        value.borderRadius = NumberOrStringOrNumbers.of(1.0)
        value.opacity = 1.0
        value.shadowBlur = 1.0
        value.shadowColor = "red".asColor()
        value.shadowOffsetX = 1.0
        value.shadowOffsetY = 1.0
        value.decal = DecalOption()
        assertEquals(
            setOf(
                "color",
                "borderColor",
                "borderWidth",
                "borderType",
                "borderDashOffset",
                "borderRadius",
                "opacity",
                "shadowBlur",
                "shadowColor",
                "shadowOffsetX",
                "shadowOffsetY",
                "decal",
            ),
            value.encodeWith(ItemStyleOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun lineStyleOptionFields() {
        val value = LineStyleOption()
        value.color = "red".asColor()
        value.width = 1.0
        value.opacity = 1.0
        value.type = "s"
        value.cap = "s"
        value.join = "s"
        value.dashOffset = 1.0
        value.miterLimit = 1.0
        value.shadowBlur = 1.0
        value.shadowColor = "red".asColor()
        value.shadowOffsetX = 1.0
        value.shadowOffsetY = 1.0
        assertEquals(
            setOf(
                "color",
                "width",
                "opacity",
                "type",
                "cap",
                "join",
                "dashOffset",
                "miterLimit",
                "shadowBlur",
                "shadowColor",
                "shadowOffsetX",
                "shadowOffsetY",
            ),
            value.encodeWith(LineStyleOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun areaStyleOptionFields() {
        val value = AreaStyleOption()
        value.color = "red".asColor()
        value.opacity = 1.0
        value.origin = num(1.0)
        value.shadowBlur = 1.0
        value.shadowColor = "red".asColor()
        value.shadowOffsetX = 1.0
        value.shadowOffsetY = 1.0
        assertEquals(
            setOf(
                "color",
                "opacity",
                "origin",
                "shadowBlur",
                "shadowColor",
                "shadowOffsetX",
                "shadowOffsetY",
            ),
            value.encodeWith(AreaStyleOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun emphasisOptionFields() {
        val value = EmphasisOption()
        value.disabled = true
        value.focus = "s"
        value.blurScope = "s"
        value.scale = BooleanOrNumber.of(1.0)
        value.itemStyle = ItemStyleOption()
        value.label = LabelOption()
        value.lineStyle = LineStyleOption()
        value.areaStyle = AreaStyleOption()
        value.endLabel = LabelOption()
        assertEquals(
            setOf(
                "disabled",
                "focus",
                "blurScope",
                "scale",
                "itemStyle",
                "label",
                "lineStyle",
                "areaStyle",
                "endLabel",
            ),
            value.encodeWith(EmphasisOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun markPointOptionFields() {
        val value = MarkPointOption()
        value.symbol = "s"
        value.symbolSize = NumberOrStringOrNumbers.of(1.0)
        value.symbolRotate = 1.0
        value.symbolOffset = listOf(num(1.0))
        value.silent = true
        value.label = LabelOption()
        value.itemStyle = ItemStyleOption()
        value.emphasis = EmphasisOption()
        value.data = listOf(MarkPointData())
        assertEquals(
            setOf(
                "symbol",
                "symbolSize",
                "symbolRotate",
                "symbolOffset",
                "silent",
                "label",
                "itemStyle",
                "emphasis",
                "data",
            ),
            value.encodeWith(MarkPointOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun markPointDataFields() {
        val value = MarkPointData()
        value.name = "s"
        value.type = "s"
        value.value = valueOf(1.0)
        value.coord = listOf(num(1.0))
        value.x = num(1.0)
        value.y = num(1.0)
        value.symbol = "s"
        value.symbolSize = NumberOrStringOrNumbers.of(1.0)
        value.symbolRotate = 1.0
        value.symbolOffset = listOf(num(1.0))
        value.itemStyle = ItemStyleOption()
        value.label = LabelOption()
        assertEquals(
            setOf(
                "name",
                "type",
                "value",
                "coord",
                "x",
                "y",
                "symbol",
                "symbolSize",
                "symbolRotate",
                "symbolOffset",
                "itemStyle",
                "label",
            ),
            value.encodeWith(MarkPointData.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun markLineOptionFields() {
        val value = MarkLineOption()
        value.silent = true
        value.symbol = listOf("s")
        value.symbolSize = NumberOrStringOrNumbers.of(1.0)
        value.precision = 1
        value.label = LabelOption()
        value.lineStyle = LineStyleOption()
        value.emphasis = EmphasisOption()
        value.data = listOf(MarkLineData())
        assertEquals(
            setOf(
                "silent",
                "symbol",
                "symbolSize",
                "precision",
                "label",
                "lineStyle",
                "emphasis",
                "data",
            ),
            value.encodeWith(MarkLineOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun markLineDataFields() {
        val value = MarkLineData()
        value.name = "s"
        value.type = "s"
        value.xAxis = num(1.0)
        value.yAxis = num(1.0)
        value.coord = listOf(listOf(num(1.0)))
        value.valueIndex = 1
        value.valueDim = "s"
        value.lineStyle = LineStyleOption()
        value.label = LabelOption()
        assertEquals(
            setOf(
                "name",
                "type",
                "xAxis",
                "yAxis",
                "coord",
                "valueIndex",
                "valueDim",
                "lineStyle",
                "label",
            ),
            value.encodeWith(MarkLineData.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun markAreaOptionFields() {
        val value = MarkAreaOption()
        value.silent = true
        value.itemStyle = ItemStyleOption()
        value.label = LabelOption()
        value.emphasis = EmphasisOption()
        value.data = listOf(MarkAreaData())
        assertEquals(
            setOf(
                "silent",
                "itemStyle",
                "label",
                "emphasis",
                "data",
            ),
            value.encodeWith(MarkAreaOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun markAreaDataFields() {
        val value = MarkAreaData()
        value.name = "s"
        value.type = "s"
        value.xAxis = num(1.0)
        value.yAxis = num(1.0)
        value.coord = listOf(listOf(num(1.0)))
        value.valueIndex = 1
        value.valueDim = "s"
        value.itemStyle = ItemStyleOption()
        value.label = LabelOption()
        assertEquals(
            setOf(
                "name",
                "type",
                "xAxis",
                "yAxis",
                "coord",
                "valueIndex",
                "valueDim",
                "itemStyle",
                "label",
            ),
            value.encodeWith(MarkAreaData.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun rippleEffectOptionFields() {
        val value = RippleEffectOption()
        value.period = 1.0
        value.scale = 1.0
        value.brushType = "s"
        value.number = 1.0
        value.color = "red".asColor()
        assertEquals(
            setOf(
                "period",
                "scale",
                "brushType",
                "number",
                "color",
            ),
            value.encodeWith(RippleEffectOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun forceOptionFields() {
        val value = ForceOption()
        value.initLayout = "s"
        value.repulsion = NumberOrStringOrNumbers.of(1.0)
        value.gravity = 1.0
        value.edgeLength = NumberOrStringOrNumbers.of(1.0)
        value.layoutAnimation = true
        value.friction = 1.0
        assertEquals(
            setOf(
                "initLayout",
                "repulsion",
                "gravity",
                "edgeLength",
                "layoutAnimation",
                "friction",
            ),
            value.encodeWith(ForceOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun scaleLimitFields() {
        val value = ScaleLimit()
        value.min = 1.0
        value.max = 1.0
        assertEquals(
            setOf(
                "min",
                "max",
            ),
            value.encodeWith(ScaleLimit.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun decalOptionFields() {
        val value = DecalOption()
        value.symbol = "s"
        value.symbolSize = 1.0
        value.symbolKeepAspect = true
        value.color = "red".asColor()
        value.backgroundColor = "red".asColor()
        value.dashArrayX = NumberOrStringOrNumbers.of(1.0)
        value.dashArrayY = NumberOrStringOrNumbers.of(1.0)
        value.rotation = 1.0
        value.maxTileWidth = 1.0
        value.maxTileHeight = 1.0
        value.dot = true
        assertEquals(
            setOf(
                "symbol",
                "symbolSize",
                "symbolKeepAspect",
                "color",
                "backgroundColor",
                "dashArrayX",
                "dashArrayY",
                "rotation",
                "maxTileWidth",
                "maxTileHeight",
                "dot",
            ),
            value.encodeWith(DecalOption.serializer()).keys,
        )
    }
}
