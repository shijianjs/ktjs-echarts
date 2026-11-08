package io.github.shijianjs.ktjs.echarts.json

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

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
        value.margin = num(1.0)
        value.offsetCenter = NumberOrStringOrNumbers.of(1.0)
        value.precision = 1
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
                "margin",
                "offsetCenter",
                "precision",
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
        value.color0 = "red".asColor()
        value.borderColor = "red".asColor()
        value.borderColor0 = "red".asColor()
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
                "color0",
                "borderColor",
                "borderColor0",
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
        value.valueIndex = 1
        value.valueDim = "s"
        value.xAxis = num(1.0)
        value.yAxis = num(1.0)
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
                "valueIndex",
                "valueDim",
                "xAxis",
                "yAxis",
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
        value.data = listOf(MarkLineDataItem.of(MarkLineData()))
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

    /** A 1D line is written as one object and a 2D line as an array of two end objects. */
    @Test
    fun markLineDataItemForms() {
        val single = MarkLineDataItem.of(MarkLineData().apply { type = "average" })
        val pair =
            MarkLineDataItem.of(
                MarkLineData().apply { coord = listOf(num(0), num(1)) },
                MarkLineData().apply { coord = listOf(num(2), num(3)) },
            )
        val data =
            echartsOption {
                series { line { markLine { data = listOf(single, pair) } } }
            }.toJsonElement()["series"]!!.jsonArray[0].jsonObject["markLine"]!!.jsonObject["data"]!!
                .jsonArray
        assertEquals("average", data[0].jsonObject["type"]!!.jsonPrimitive.content)
        assertEquals(
            listOf(0.0, 1.0, 2.0, 3.0),
            data[1].jsonArray.flatMap { end ->
                end.jsonObject["coord"]!!.jsonArray.map { it.jsonPrimitive.double }
            },
        )
    }

    /** The pair form survives an encode/decode round trip and rejects a wrong number of ends. */
    @Test
    fun markLineDataItemRoundTrips() {
        val item =
            MarkLineDataItem.of(
                MarkLineData().apply {
                    name = "start"
                    coord = listOf(num(0), num(1))
                },
                MarkLineData().apply { name = "end"; coord = listOf(num(2), num(3)) },
            )
        val element = EChartsJson.format.encodeToJsonElement(MarkLineDataItem.serializer(), item)
        val decoded =
            EChartsJson.format.decodeFromJsonElement(MarkLineDataItem.serializer(), element)
        assertEquals(element, EChartsJson.format.encodeToJsonElement(MarkLineDataItem.serializer(), decoded))
        assertFailsWith<SerializationException> {
            EChartsJson.format.decodeFromJsonElement(
                MarkLineDataItem.serializer(),
                JsonArray(listOf(JsonObject(emptyMap()))),
            )
        }
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun markLineDataFields() {
        val value = MarkLineData()
        value.name = "s"
        value.type = "s"
        value.xAxis = num(1.0)
        value.yAxis = num(1.0)
        value.coord = listOf(num(1.0), num(2.0))
        value.x = str("90%")
        value.y = num(1.0)
        value.relativeTo = "coordinate"
        value.radiusAxis = num(1.0)
        value.angleAxis = num(1.0)
        value.value = num(1.0)
        value.valueIndex = 1
        value.valueDim = "s"
        value.symbol = "circle"
        value.symbolSize = NumberOrStringOrNumbers.of(1.0)
        value.symbolRotate = 1.0
        value.symbolOffset = listOf(num(1.0), num(2.0))
        value.lineStyle = LineStyleOption()
        value.itemStyle = ItemStyleOption()
        value.label = LabelOption()
        value.emphasis = EmphasisOption()
        value.z2 = 1
        assertEquals(
            setOf(
                "name",
                "type",
                "xAxis",
                "yAxis",
                "coord",
                "x",
                "y",
                "relativeTo",
                "radiusAxis",
                "angleAxis",
                "value",
                "valueIndex",
                "valueDim",
                "symbol",
                "symbolSize",
                "symbolRotate",
                "symbolOffset",
                "lineStyle",
                "itemStyle",
                "label",
                "emphasis",
                "z2",
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
        value.data = listOf(MarkAreaDataItem.of(MarkAreaData()))
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

    /** A 1D range is written as one object and an area as an array of two corner objects. */
    @Test
    fun markAreaDataItemForms() {
        val single = MarkAreaDataItem.of(MarkAreaData().apply { type = "min" })
        val pair =
            MarkAreaDataItem.of(
                MarkAreaData().apply { coord = listOf(str("min"), str("min")) },
                MarkAreaData().apply { coord = listOf(str("max"), str("max")) },
            )
        val data =
            echartsOption {
                series { line { markArea { data = listOf(single, pair) } } }
            }.toJsonElement()["series"]!!.jsonArray[0].jsonObject["markArea"]!!.jsonObject["data"]!!
                .jsonArray
        assertEquals("min", data[0].jsonObject["type"]!!.jsonPrimitive.content)
        assertEquals(
            listOf("min", "min", "max", "max"),
            data[1].jsonArray.flatMap { corner ->
                corner.jsonObject["coord"]!!.jsonArray.map { it.jsonPrimitive.content }
            },
        )
    }

    /** The pair form survives an encode/decode round trip and rejects a wrong number of corners. */
    @Test
    fun markAreaDataItemRoundTrips() {
        val item =
            MarkAreaDataItem.of(
                MarkAreaData().apply {
                    name = "from"
                    coord = listOf(num(0), num(0))
                },
                MarkAreaData().apply { name = "to"; coord = listOf(num(1), num(1)) },
            )
        val element = EChartsJson.format.encodeToJsonElement(MarkAreaDataItem.serializer(), item)
        val decoded =
            EChartsJson.format.decodeFromJsonElement(MarkAreaDataItem.serializer(), element)
        assertEquals(element, EChartsJson.format.encodeToJsonElement(MarkAreaDataItem.serializer(), decoded))
        assertFailsWith<SerializationException> {
            EChartsJson.format.decodeFromJsonElement(
                MarkAreaDataItem.serializer(),
                JsonArray(listOf(JsonObject(emptyMap()), JsonObject(emptyMap()), JsonObject(emptyMap()))),
            )
        }
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun markAreaDataFields() {
        val value = MarkAreaData()
        value.name = "s"
        value.type = "s"
        value.xAxis = num(1.0)
        value.yAxis = num(1.0)
        value.coord = listOf(num(1.0), num(2.0))
        value.x = str("90%")
        value.y = num(1.0)
        value.relativeTo = "coordinate"
        value.radiusAxis = num(1.0)
        value.angleAxis = num(1.0)
        value.value = num(1.0)
        value.valueIndex = 1
        value.valueDim = "s"
        value.itemStyle = ItemStyleOption()
        value.label = LabelOption()
        value.emphasis = EmphasisOption()
        value.z2 = 1
        assertEquals(
            setOf(
                "name",
                "type",
                "xAxis",
                "yAxis",
                "coord",
                "x",
                "y",
                "relativeTo",
                "radiusAxis",
                "angleAxis",
                "value",
                "valueIndex",
                "valueDim",
                "itemStyle",
                "label",
                "emphasis",
                "z2",
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
