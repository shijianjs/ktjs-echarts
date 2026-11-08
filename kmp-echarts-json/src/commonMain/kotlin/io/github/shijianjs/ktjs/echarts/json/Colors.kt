package io.github.shijianjs.ktjs.echarts.json

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

/**
 * A color accepted by ECharts: a CSS color string, a canvas gradient or a pattern.
 *
 * ECharts also accepts `CanvasGradient` / `CanvasPattern` / `HTMLImageElement` objects, which have
 * no meaning in a JSON payload; use [Css] with a data URL or a plain URL string instead.
 */
@kotlinx.serialization.Serializable(with = EChartsColorSerializer::class)
public sealed interface EChartsColor {
    /** Any CSS color string, for example `#5470c6`, `rgba(0, 0, 0, .3)` or `red`. */
    public data class Css(public val value: String) : EChartsColor

    /**
     * Linear gradient. [x], [y], [x2] and [y2] are relative to the shape's bounding box unless
     * [global] is `true`, in which case they are canvas coordinates.
     */
    public data class LinearGradient(
        public val x: Double = 0.0,
        public val y: Double = 0.0,
        public val x2: Double = 0.0,
        public val y2: Double = 1.0,
        public val colorStops: List<GradientColorStop>,
        public val global: Boolean? = null,
    ) : EChartsColor

    /** Radial gradient, with the center and radius relative to the shape's bounding box. */
    public data class RadialGradient(
        public val x: Double = 0.5,
        public val y: Double = 0.5,
        public val r: Double = 0.5,
        public val colorStops: List<GradientColorStop>,
        public val global: Boolean? = null,
    ) : EChartsColor

    /** Repeating image pattern. [image] is a URL or a data URL. */
    public data class Pattern(
        public val image: String,
        public val repeat: String? = null,
    ) : EChartsColor
}

/** One stop of a linear or radial gradient. [offset] is in the range `0..1`. */
public data class GradientColorStop(public val offset: Double, public val color: String)

/** Wraps a CSS color string. */
public fun String.asColor(): EChartsColor = EChartsColor.Css(this)

/** Serializer for [EChartsColor]; not needed directly in normal use. */
public object EChartsColorSerializer : KSerializer<EChartsColor> {
    override val descriptor: SerialDescriptor = JsonElement.serializer().descriptor

    override fun serialize(encoder: Encoder, value: EChartsColor) {
        encoder.encodeSerializableValue(JsonElement.serializer(), value.toJsonElement())
    }

    override fun deserialize(decoder: Decoder): EChartsColor =
        decoder.decodeSerializableValue(JsonElement.serializer()).toColor()
}

internal fun EChartsColor.toJsonElement(): JsonElement = when (this) {
    is EChartsColor.Css -> JsonPrimitive(value)

    is EChartsColor.LinearGradient -> buildJsonObject {
        put("type", "linear")
        put("x", x)
        put("y", y)
        put("x2", x2)
        put("y2", y2)
        put("colorStops", colorStops.toJson())
        global?.let { put("global", it) }
    }

    is EChartsColor.RadialGradient -> buildJsonObject {
        put("type", "radial")
        put("x", x)
        put("y", y)
        put("r", r)
        put("colorStops", colorStops.toJson())
        global?.let { put("global", it) }
    }

    is EChartsColor.Pattern -> buildJsonObject {
        put("image", image)
        repeat?.let { put("repeat", it) }
    }
}

private fun List<GradientColorStop>.toJson(): JsonArray = buildJsonArray {
    for (stop in this@toJson) {
        add(buildJsonObject {
            put("offset", stop.offset)
            put("color", stop.color)
        })
    }
}

private fun JsonElement.toColor(): EChartsColor {
    if (this is JsonPrimitive) {
        if (!isString) {
            throw SerializationException("Expected a CSS color string, got $this")
        }
        return EChartsColor.Css(content)
    }
    val obj = asObjectOrThrow()
    val type = obj["type"]?.jsonPrimitive?.content
    return when (type) {
        "linear" -> EChartsColor.LinearGradient(
            x = obj.doubleOr("x", 0.0),
            y = obj.doubleOr("y", 0.0),
            x2 = obj.doubleOr("x2", 0.0),
            y2 = obj.doubleOr("y2", 1.0),
            colorStops = obj.colorStops(),
            global = obj.booleanOrNull("global"),
        )

        "radial" -> EChartsColor.RadialGradient(
            x = obj.doubleOr("x", 0.5),
            y = obj.doubleOr("y", 0.5),
            r = obj.doubleOr("r", 0.5),
            colorStops = obj.colorStops(),
            global = obj.booleanOrNull("global"),
        )

        else -> {
            val image = obj["image"]?.jsonPrimitive?.content
                ?: throw SerializationException("Unknown color object: $obj")
            EChartsColor.Pattern(
                image = image,
                repeat = obj["repeat"]?.jsonPrimitive?.content,
            )
        }
    }
}

private fun JsonObject.colorStops(): List<GradientColorStop> =
    (this["colorStops"] as? JsonArray)?.map { element ->
        val stop = element.asObjectOrThrow()
        GradientColorStop(
            offset = stop["offset"]?.jsonPrimitive?.double ?: 0.0,
            color = stop["color"]?.jsonPrimitive?.content ?: "transparent",
        )
    } ?: throw SerializationException("Gradient without colorStops: $this")

private fun JsonElement.asObjectOrThrow(): JsonObject =
    this as? JsonObject ?: throw SerializationException("Expected a JSON object, got $this")

private fun JsonObject.doubleOr(key: String, fallback: Double): Double =
    this[key]?.jsonPrimitive?.double ?: fallback

private fun JsonObject.booleanOrNull(key: String): Boolean? =
    this[key]?.jsonPrimitive?.content?.toBooleanStrictOrNull()
