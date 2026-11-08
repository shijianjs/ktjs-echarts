package io.github.shijianjs.ktjs.echarts.json

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

/** Legend layout. */
@Serializable
public enum class LegendType {
    /** Plain legend, no paging. */
    @SerialName("plain")
    Plain,

    /** Scrollable legend with paging controls. */
    @SerialName("scroll")
    Scroll,
}

/** One `legend.data` entry: either a name only or an item with per-entry overrides. */
@Serializable(with = LegendItemSerializer::class)
public sealed interface LegendItem {
    /** Name-only entry. */
    public data class Name(public val name: String) : LegendItem

    /** Entry with an icon and style overrides. */
    public data class Entry(
        public val name: String,
        public val icon: String? = null,
        public val itemStyle: ItemStyleOption? = null,
        public val lineStyle: LineStyleOption? = null,
        public val textStyle: TextStyleOption? = null,
    ) : LegendItem
}

/** A legend and the series it collects. */
@Serializable
public class LegendOption {
    /** Layout type. */
    public var type: LegendType? = null

    /** Component id used by cross references. */
    public var id: String? = null

    /** Whether the legend is shown. */
    public var show: Boolean? = null

    /** z-order; larger values draw on top. */
    public var z: Double? = null

    /** Canvas layer; components on a higher level are drawn in a separate canvas. */
    public var zlevel: Double? = null

    /** Distance to the left edge. */
    public var left: NumberOrString? = null

    /** Distance to the top edge. */
    public var top: NumberOrString? = null

    /** Distance to the right edge. */
    public var right: NumberOrString? = null

    /** Distance to the bottom edge. */
    public var bottom: NumberOrString? = null

    /** Layout direction. */
    public var orient: Orient? = null

    /** Horizontal alignment: `"auto"`, `"left"` or `"right"`. */
    public var align: String? = null

    /** Inner padding, a number or `[top, right, bottom, left]`. */
    public var padding: NumberOrStringOrNumbers? = null

    /** Gap between entries. */
    public var itemGap: Double? = null

    /** Width of one legend icon. */
    public var itemWidth: Double? = null

    /** Height of one legend icon. */
    public var itemHeight: Double? = null

    /** Icon style. */
    public var itemStyle: ItemStyleOption? = null

    /** Line style of line-series icons. */
    public var lineStyle: LineStyleOption? = null

    /** Legend icon shape. */
    public var symbol: String? = null

    /** Legend icon size, a number or `[width, height]`. */
    public var symbolSize: NumberOrStringOrNumbers? = null

    /** Icon rotation in degrees. */
    public var symbolRotate: Double? = null

    /** Whether the icon keeps its aspect ratio. */
    public var symbolKeepAspect: Boolean? = null

    /** Formatter template for entry names. */
    public var formatter: String? = null

    /** Selection mode: `true`, `false`, `"single"` or `"multiple"`. */
    public var selectedMode: BooleanOrString? = null

    /** Entry text style. */
    public var textStyle: TextStyleOption? = null

    /** Entries to show; defaults to the series names. */
    public var data: List<LegendItem>? = null

    /** Background color. */
    public var backgroundColor: EChartsColor? = null

    /** Border color. */
    public var borderColor: EChartsColor? = null

    /** Border width. */
    public var borderWidth: Double? = null

    /** Border radius, a number or an array of radii. */
    public var borderRadius: NumberOrStringOrNumbers? = null

    /** Shadow blur radius. */
    public var shadowBlur: Double? = null

    /** Shadow color. */
    public var shadowColor: EChartsColor? = null

    /** Horizontal shadow offset. */
    public var shadowOffsetX: Double? = null

    /** Vertical shadow offset. */
    public var shadowOffsetY: Double? = null

    /** Index of the first entry shown in a scroll legend. */
    public var scrollDataIndex: Int? = null

    /** Gap between the paging buttons. */
    public var pageButtonItemGap: Double? = null

    /** Size of a paging button. */
    public var pageButtonSize: NumberOrStringOrNumbers? = null

    /** Paging button position: `"start"` or `"end"`. */
    public var pageButtonPosition: String? = null

    /** Paging label formatter. */
    public var pageFormatter: String? = null

    /** Paging button images, as data URLs. */
    public var pageIcons: LegendPageIcons? = null

    /** Paging button color. */
    public var pageIconColor: EChartsColor? = null

    /** Paging button color when the page is unavailable. */
    public var pageIconInactiveColor: EChartsColor? = null

    /** Paging button size. */
    public var pageIconSize: NumberOrStringOrNumbers? = null

    /** Paging text style. */
    public var pageTextStyle: TextStyleOption? = null

    /** Whether entry changes animate. */
    public var animation: Boolean? = null

    /** Animation duration in milliseconds. */
    public var animationDuration: Double? = null

    /** Animation easing name. */
    public var animationEasing: String? = null

    /** Update animation duration in milliseconds. */
    public var animationDurationUpdate: Double? = null

    /** Update animation easing name. */
    public var animationEasingUpdate: String? = null
}

/** Images of the two paging buttons of a scroll legend. */
@Serializable
public class LegendPageIcons {
    /** `[normal, active]` image data URLs for the horizontal layout. */
    public var horizontal: List<String>? = null

    /** `[normal, active]` image data URLs for the vertical layout. */
    public var vertical: List<String>? = null
}

/** Serializer for [LegendItem]; not needed directly in normal use. */
public object LegendItemSerializer : KSerializer<LegendItem> {
    override val descriptor: SerialDescriptor = JsonElement.serializer().descriptor

    override fun serialize(encoder: Encoder, value: LegendItem) {
        val element = when (value) {
            is LegendItem.Name -> JsonPrimitive(value.name)
            is LegendItem.Entry -> buildJsonObject {
                put("name", value.name)
                value.icon?.let { put("icon", it) }
                value.itemStyle?.let { put("itemStyle", encode(it, ItemStyleOption.serializer())) }
                value.lineStyle?.let { put("lineStyle", encode(it, LineStyleOption.serializer())) }
                value.textStyle?.let { put("textStyle", encode(it, TextStyleOption.serializer())) }
            }
        }
        encoder.encodeSerializableValue(JsonElement.serializer(), element)
    }

    override fun deserialize(decoder: Decoder): LegendItem {
        val element = decoder.decodeSerializableValue(JsonElement.serializer())
        if (element is JsonPrimitive) {
            if (!element.isString) {
                throw SerializationException("Legend entry must be a name or an object: $element")
            }
            return LegendItem.Name(element.content)
        }
        val obj = element as? JsonObject
            ?: throw SerializationException("Legend entry must be a name or an object: $element")
        return LegendItem.Entry(
            name = obj["name"]?.jsonPrimitive?.content.orEmpty(),
            icon = obj["icon"]?.jsonPrimitive?.content,
            itemStyle = obj["itemStyle"]?.let {
                EChartsJson.format.decodeFromJsonElement(ItemStyleOption.serializer(), it)
            },
            lineStyle = obj["lineStyle"]?.let {
                EChartsJson.format.decodeFromJsonElement(LineStyleOption.serializer(), it)
            },
            textStyle = obj["textStyle"]?.let {
                EChartsJson.format.decodeFromJsonElement(TextStyleOption.serializer(), it)
            },
        )
    }

    private fun <T> encode(value: T, serializer: KSerializer<T>): JsonElement =
        EChartsJson.format.encodeToJsonElement(serializer, value)
}
