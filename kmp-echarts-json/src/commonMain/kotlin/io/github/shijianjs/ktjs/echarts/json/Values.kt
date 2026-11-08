package io.github.shijianjs.ktjs.echarts.json

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.number
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.double
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.time.Instant

/**
 * A value ECharts accepts as either a number or a string.
 *
 * Typical uses are `min` / `max` (`"dataMin"`), `left` / `top` (`"center"`, `"50%"`) and
 * `symbolSize`. Use [num] and [str] to construct the variants concisely.
 */
@kotlinx.serialization.Serializable(with = NumberOrStringSerializer::class)
public sealed interface NumberOrString {
    /** Number variant. */
    public data class Number(public val value: Double) : NumberOrString

    /** String variant; ECharts interprets percentages and keywords such as `"auto"`. */
    public data class Text(public val value: String) : NumberOrString
}

/** Wraps a number as [NumberOrString]. */
public fun num(value: Number): NumberOrString = NumberOrString.Number(value.toDouble())

/** Wraps a string as [NumberOrString]. */
public fun str(value: String): NumberOrString = NumberOrString.Text(value)

/** A value ECharts accepts as a single number/string or as an array of them. */
@kotlinx.serialization.Serializable(with = NumberOrStringOrNumbersSerializer::class)
public sealed interface NumberOrStringOrNumbers {
    /** One value, encoded as a JSON number or string. */
    public data class Single(public val value: NumberOrString) : NumberOrStringOrNumbers

    /** Several values, encoded as a JSON array. */
    public data class Multiple(
        public val values: List<NumberOrString>,
    ) : NumberOrStringOrNumbers

    public companion object {
        /** Wraps [value] without an array. */
        public fun of(value: NumberOrString): NumberOrStringOrNumbers = Single(value)

        /** Wraps a number without an array. */
        public fun of(value: Number): NumberOrStringOrNumbers = Single(num(value))

        /** Wraps a string without an array. */
        public fun of(value: String): NumberOrStringOrNumbers = Single(str(value))

        /** Wraps [values] as an array. */
        public fun of(vararg values: NumberOrString): NumberOrStringOrNumbers =
            Multiple(values.toList())

        /** Wraps [values] as an array of numbers. */
        public fun numbers(vararg values: Number): NumberOrStringOrNumbers =
            Multiple(values.map(::num))
    }
}

/**
 * A boolean option that also accepts a string mode.
 *
 * ECharts uses this for `darkMode` (`true` / `false` / `"auto"`), `roam` (`true` / `"scale"` /
 * `"move"`) and `selectedMode` (`true` / `false` / `"single"` / `"multiple"` / `"series"`).
 */
@kotlinx.serialization.Serializable(with = BooleanOrStringSerializer::class)
public sealed interface BooleanOrString {
    /** Boolean variant. */
    public data class BooleanValue(public val value: Boolean) : BooleanOrString

    /** String variant. */
    public data class Text(public val value: String) : BooleanOrString

    public companion object {
        /** Wraps a boolean. */
        public fun of(value: Boolean): BooleanOrString = BooleanValue(value)

        /** Wraps a string mode. */
        public fun of(value: String): BooleanOrString = Text(value)
    }
}

/** A boolean option that also accepts a number, used by `smooth` and `progressive`. */
@kotlinx.serialization.Serializable(with = BooleanOrNumberSerializer::class)
public sealed interface BooleanOrNumber {
    /** Boolean variant. */
    public data class BooleanValue(public val value: Boolean) : BooleanOrNumber

    /** Number variant. */
    public data class NumberValue(public val value: Double) : BooleanOrNumber

    public companion object {
        /** Wraps a boolean. */
        public fun of(value: Boolean): BooleanOrNumber = BooleanValue(value)

        /** Wraps a number. */
        public fun of(value: Number): BooleanOrNumber = NumberValue(value.toDouble())
    }
}

/**
 * The `boundaryGap` of a category or value axis: either a boolean or an explicit two-value range.
 */
@kotlinx.serialization.Serializable(with = BoundaryGapSerializer::class)
public sealed interface BoundaryGap {
    /** Boolean form: rows or points sit between ticks (`true`) or on them (`false`). */
    public data class Enabled(public val value: Boolean) : BoundaryGap

    /** Explicit range, for example `["20%", "20%"]`. */
    public data class Range(public val values: List<NumberOrString>) : BoundaryGap

    public companion object {
        /** Wraps a boolean. */
        public fun of(value: Boolean): BoundaryGap = Enabled(value)

        /** Wraps an explicit range. */
        public fun of(vararg values: NumberOrString): BoundaryGap = Range(values.toList())
    }
}

/**
 * One entry of `series.data`, `dataset.source` or an axis' category list.
 *
 * A value is any JSON primitive, an array of values (a point or a row) or a JSON object (a typed
 * item such as [DataItem]). Use [valueOf], [textValue], [point] and [asDataValue] to build the
 * common shapes without touching [JsonElement].
 */
@kotlinx.serialization.Serializable(with = DataValueSerializer::class)
public sealed interface DataValue {
    /** JSON number. */
    public data class Number(public val value: Double) : DataValue

    /** JSON string. */
    public data class Text(public val value: String) : DataValue

    /** JSON boolean. */
    public data class Bool(public val value: Boolean) : DataValue

    /** JSON `null`, which ECharts renders as a gap. */
    public data object Null : DataValue

    /** [Instant] encoded as epoch milliseconds, the unit of a `time` axis. */
    public data class InstantValue(public val value: Instant) : DataValue

    /** Date-time encoded as `yyyy-MM-dd HH:mm:ss`, which ECharts parses on a `time` axis. */
    public data class LocalDateTimeValue(public val value: LocalDateTime) : DataValue

    /** Date encoded as `yyyy-MM-dd`, which ECharts parses on a `time` axis. */
    public data class LocalDateValue(public val value: LocalDate) : DataValue

    /** JSON array, used for points and rows. */
    public data class Array(public val values: List<DataValue>) : DataValue

    /** Raw JSON object, used for typed data items. */
    public data class Object(public val value: JsonObject) : DataValue
}

/** Wraps a number as a [DataValue]. */
public fun valueOf(value: Number): DataValue = DataValue.Number(value.toDouble())

/** Wraps a string as a [DataValue]. */
public fun textValue(value: String): DataValue = DataValue.Text(value)

/** Wraps a list of values as a JSON array. */
public fun arrayValue(vararg values: DataValue): DataValue =
    DataValue.Array(values.toList())

/** Builds a two-element point, the usual shape of scatter and line data. */
public fun point(x: Number, y: Number): DataValue = arrayValue(valueOf(x), valueOf(y))

/** Maps numbers to a series or axis data list. */
public fun dataValues(vararg values: Number): List<DataValue> =
    values.map { DataValue.Number(it.toDouble()) }

/** Maps strings to a category data list. */
public fun dataValues(vararg values: String): List<DataValue> =
    values.map { DataValue.Text(it) }

/** Encodes an [Instant] as epoch milliseconds for a `time` axis. */
public fun Instant.toEChartsTime(): Long = toEpochMilliseconds()

/** Formats a date-time as `yyyy-MM-dd HH:mm:ss` for a `time` axis. */
public fun LocalDateTime.toEChartsTime(): String = buildString {
    append(date.year.toString().padStart(4, '0'))
    append('-')
    append(date.month.number.toString().padStart(2, '0'))
    append('-')
    append(date.day.toString().padStart(2, '0'))
    append(' ')
    append(hour.toString().padStart(2, '0'))
    append(':')
    append(minute.toString().padStart(2, '0'))
    append(':')
    append(second.toString().padStart(2, '0'))
}

/** Formats a date as `yyyy-MM-dd` for a `time` axis. */
public fun LocalDate.toEChartsTime(): String = buildString {
    append(year.toString().padStart(4, '0'))
    append('-')
    append(month.number.toString().padStart(2, '0'))
    append('-')
    append(day.toString().padStart(2, '0'))
}

/** Wraps an [Instant] as a [DataValue] encoded as epoch milliseconds. */
public fun Instant.asDataValue(): DataValue = DataValue.InstantValue(this)

/** Wraps a date-time as a [DataValue] encoded as `yyyy-MM-dd HH:mm:ss`. */
public fun LocalDateTime.asDataValue(): DataValue = DataValue.LocalDateTimeValue(this)

/** Wraps a date as a [DataValue] encoded as `yyyy-MM-dd`. */
public fun LocalDate.asDataValue(): DataValue = DataValue.LocalDateValue(this)

/** Converts a [DataValue] to the JSON tree ECharts consumes. */
public fun DataValue.toJsonElement(): JsonElement = when (this) {
    is DataValue.Number -> JsonPrimitive(value)
    is DataValue.Text -> JsonPrimitive(value)
    is DataValue.Bool -> JsonPrimitive(value)
    DataValue.Null -> JsonNull
    is DataValue.InstantValue -> JsonPrimitive(value.toEChartsTime())
    is DataValue.LocalDateTimeValue -> JsonPrimitive(value.toEChartsTime())
    is DataValue.LocalDateValue -> JsonPrimitive(value.toEChartsTime())
    is DataValue.Array -> JsonArray(values.map { it.toJsonElement() })
    is DataValue.Object -> value
}

/** Parses any JSON value back into a [DataValue]. */
public fun JsonElement.toDataValue(): DataValue = when (this) {
    JsonNull -> DataValue.Null
    is JsonArray -> DataValue.Array(jsonArray.map { it.toDataValue() })
    is JsonObject -> DataValue.Object(jsonObject)
    is JsonPrimitive -> when {
        isString -> DataValue.Text(content)
        content == "true" -> DataValue.Bool(true)
        content == "false" -> DataValue.Bool(false)
        else -> content.toDoubleOrNull()?.let { DataValue.Number(it) }
            ?: throw SerializationException("Not a data value: $content")
    }
}

/**
 * A typed `series.data` item.
 *
 * Items carrying only a value can be written as [valueOf]; this class is for items that also set
 * per-point styling, a name or tree children. Convert it with [asDataValue] when assigning to a
 * series, or use [dataItem] to build one inline.
 */
@kotlinx.serialization.Serializable
public class DataItem {
    /** Name shown in the tooltip and legend. */
    public var name: String? = null

    /** Value; may be a number, a string or an array for multi-dimensional series. */
    public var value: DataValue? = null

    /** Stable identifier used by transitions and `datasetId`. */
    public var id: String? = null

    /** Group identifier shared by items that animate together. */
    public var groupId: String? = null

    /** Whether a pie or map item starts selected. */
    public var selected: Boolean? = null

    /** Per-item style. */
    public var itemStyle: ItemStyleOption? = null

    /** Per-item label. */
    public var label: LabelOption? = null

    /** Per-item emphasis state. */
    public var emphasis: EmphasisOption? = null

    /** Per-item symbol name. */
    public var symbol: String? = null

    /** Fixed x position on a `graph` or `tree` layout that does not compute positions. */
    public var x: Double? = null

    /** Fixed y position on a `graph` or `tree` layout that does not compute positions. */
    public var y: Double? = null

    /** Category index of a `graph` node. */
    public var category: Int? = null

    /** Per-item symbol size. */
    public var symbolSize: NumberOrStringOrNumbers? = null

    /** Per-item symbol rotation in degrees. */
    public var symbolRotate: Double? = null

    /** Per-item symbol offset as `[x, y]`. */
    public var symbolOffset: List<NumberOrString>? = null

    /** Children of a tree or treemap node. */
    public var children: List<DataItem>? = null

    /** Whether a tree node starts collapsed. */
    public var collapsed: Boolean? = null
}

/** Encodes the item as a JSON object, ready to be put into a series' data list. */
public fun DataItem.asDataValue(): DataValue =
    DataValue.Object(EChartsJson.format.encodeToJsonElement(DataItem.serializer(), this).jsonObject)

/** Builds a typed data item. */
public fun dataItem(block: DataItem.() -> Unit): DataValue = DataItem().apply(block).asDataValue()

/**
 * A value ECharts accepts as one number or an array of numbers.
 *
 * Used by `symbolBoundingData`, whose array form sets the lower and upper bound of a pictorial bar
 * in one value.
 */
@kotlinx.serialization.Serializable(with = NumberOrNumbersSerializer::class)
public sealed interface NumberOrNumbers {
    /** One number. */
    public data class Single(public val value: Double) : NumberOrNumbers

    /** Several numbers, encoded as a JSON array. */
    public data class Multiple(public val values: List<Double>) : NumberOrNumbers

    public companion object {
        /** Wraps [value] without an array. */
        public fun of(value: Number): NumberOrNumbers = Single(value.toDouble())

        /** Wraps [values] as a number array. */
        public fun of(vararg values: Number): NumberOrNumbers =
            Multiple(values.map { it.toDouble() })
    }
}

/**
 * A boolean option that also accepts a number or a string mode.
 *
 * ECharts uses this for `symbolRepeat`: a boolean, a repeat count or the `"fixed"` keyword that
 * repeats as often as the symbol fits without clipping it to the data value.
 */
@kotlinx.serialization.Serializable(with = BooleanOrNumberOrStringSerializer::class)
public sealed interface BooleanOrNumberOrString {
    /** Boolean variant. */
    public data class BooleanValue(public val value: Boolean) : BooleanOrNumberOrString

    /** Number variant. */
    public data class NumberValue(public val value: Double) : BooleanOrNumberOrString

    /** String variant. */
    public data class Text(public val value: String) : BooleanOrNumberOrString

    public companion object {
        /** Wraps a boolean. */
        public fun of(value: Boolean): BooleanOrNumberOrString = BooleanValue(value)

        /** Wraps a number. */
        public fun of(value: Number): BooleanOrNumberOrString = NumberValue(value.toDouble())

        /** Wraps a string mode. */
        public fun of(value: String): BooleanOrNumberOrString = Text(value)
    }
}

/** Serializer for [NumberOrString]; not needed directly in normal use. */
public object NumberOrStringSerializer : KSerializer<NumberOrString> {
    override val descriptor: SerialDescriptor = JsonElement.serializer().descriptor

    override fun serialize(encoder: Encoder, value: NumberOrString) {
        encoder.encodeSerializableValue(
            JsonElement.serializer(),
            when (value) {
                is NumberOrString.Number -> JsonPrimitive(value.value)
                is NumberOrString.Text -> JsonPrimitive(value.value)
            },
        )
    }

    override fun deserialize(decoder: Decoder): NumberOrString =
        decoder.decodeSerializableValue(JsonElement.serializer()).toNumberOrString()
}

/** Serializer for [NumberOrStringOrNumbers]; not needed directly in normal use. */
public object NumberOrStringOrNumbersSerializer : KSerializer<NumberOrStringOrNumbers> {
    override val descriptor: SerialDescriptor = JsonElement.serializer().descriptor

    override fun serialize(encoder: Encoder, value: NumberOrStringOrNumbers) {
        val element = when (value) {
            is NumberOrStringOrNumbers.Single -> value.value.toJsonElement()
            is NumberOrStringOrNumbers.Multiple ->
                JsonArray(value.values.map { it.toJsonElement() })
        }
        encoder.encodeSerializableValue(JsonElement.serializer(), element)
    }

    override fun deserialize(decoder: Decoder): NumberOrStringOrNumbers {
        val element = decoder.decodeSerializableValue(JsonElement.serializer())
        return if (element is JsonArray) {
            NumberOrStringOrNumbers.Multiple(element.map { it.toNumberOrString() })
        } else {
            NumberOrStringOrNumbers.Single(element.toNumberOrString())
        }
    }
}

/** Serializer for [BooleanOrString]; not needed directly in normal use. */
public object BooleanOrStringSerializer : KSerializer<BooleanOrString> {
    override val descriptor: SerialDescriptor = JsonElement.serializer().descriptor

    override fun serialize(encoder: Encoder, value: BooleanOrString) {
        encoder.encodeSerializableValue(
            JsonElement.serializer(),
            when (value) {
                is BooleanOrString.BooleanValue -> JsonPrimitive(value.value)
                is BooleanOrString.Text -> JsonPrimitive(value.value)
            },
        )
    }

    override fun deserialize(decoder: Decoder): BooleanOrString {
        val primitive = decoder.decodeSerializableValue(JsonElement.serializer()).jsonPrimitive
        return if (primitive.isString) {
            BooleanOrString.Text(primitive.content)
        } else {
            BooleanOrString.BooleanValue(primitive.booleanOrNull ?: false)
        }
    }
}

/** Serializer for [BooleanOrNumber]; not needed directly in normal use. */
public object BooleanOrNumberSerializer : KSerializer<BooleanOrNumber> {
    override val descriptor: SerialDescriptor = JsonElement.serializer().descriptor

    override fun serialize(encoder: Encoder, value: BooleanOrNumber) {
        encoder.encodeSerializableValue(
            JsonElement.serializer(),
            when (value) {
                is BooleanOrNumber.BooleanValue -> JsonPrimitive(value.value)
                is BooleanOrNumber.NumberValue -> JsonPrimitive(value.value)
            },
        )
    }

    override fun deserialize(decoder: Decoder): BooleanOrNumber {
        val primitive = decoder.decodeSerializableValue(JsonElement.serializer()).jsonPrimitive
        val number = if (primitive.isString) null else primitive.doubleOrNull
        return if (number != null) {
            BooleanOrNumber.NumberValue(number)
        } else {
            BooleanOrNumber.BooleanValue(primitive.booleanOrNull ?: false)
        }
    }
}

/** Serializer for [NumberOrNumbers]; not needed directly in normal use. */
public object NumberOrNumbersSerializer : KSerializer<NumberOrNumbers> {
    override val descriptor: SerialDescriptor = JsonElement.serializer().descriptor

    override fun serialize(encoder: Encoder, value: NumberOrNumbers) {
        val element = when (value) {
            is NumberOrNumbers.Single -> JsonPrimitive(value.value)
            is NumberOrNumbers.Multiple -> JsonArray(value.values.map { JsonPrimitive(it) })
        }
        encoder.encodeSerializableValue(JsonElement.serializer(), element)
    }

    override fun deserialize(decoder: Decoder): NumberOrNumbers {
        val element = decoder.decodeSerializableValue(JsonElement.serializer())
        return if (element is JsonArray) {
            NumberOrNumbers.Multiple(element.map { it.jsonPrimitive.double })
        } else {
            NumberOrNumbers.Single(element.jsonPrimitive.double)
        }
    }
}

/** Serializer for [BooleanOrNumberOrString]; not needed directly in normal use. */
public object BooleanOrNumberOrStringSerializer : KSerializer<BooleanOrNumberOrString> {
    override val descriptor: SerialDescriptor = JsonElement.serializer().descriptor

    override fun serialize(encoder: Encoder, value: BooleanOrNumberOrString) {
        encoder.encodeSerializableValue(
            JsonElement.serializer(),
            when (value) {
                is BooleanOrNumberOrString.BooleanValue -> JsonPrimitive(value.value)
                is BooleanOrNumberOrString.NumberValue -> JsonPrimitive(value.value)
                is BooleanOrNumberOrString.Text -> JsonPrimitive(value.value)
            },
        )
    }

    override fun deserialize(decoder: Decoder): BooleanOrNumberOrString {
        val primitive = decoder.decodeSerializableValue(JsonElement.serializer()).jsonPrimitive
        if (primitive.isString) return BooleanOrNumberOrString.Text(primitive.content)
        val number = primitive.doubleOrNull
        return if (number != null) {
            BooleanOrNumberOrString.NumberValue(number)
        } else {
            BooleanOrNumberOrString.BooleanValue(primitive.booleanOrNull ?: false)
        }
    }
}

/** Serializer for [BoundaryGap]; not needed directly in normal use. */
public object BoundaryGapSerializer : KSerializer<BoundaryGap> {
    override val descriptor: SerialDescriptor = JsonElement.serializer().descriptor

    override fun serialize(encoder: Encoder, value: BoundaryGap) {
        val element = when (value) {
            is BoundaryGap.Enabled -> JsonPrimitive(value.value)
            is BoundaryGap.Range -> JsonArray(value.values.map { it.toJsonElement() })
        }
        encoder.encodeSerializableValue(JsonElement.serializer(), element)
    }

    override fun deserialize(decoder: Decoder): BoundaryGap {
        val element = decoder.decodeSerializableValue(JsonElement.serializer())
        return if (element is JsonArray) {
            BoundaryGap.Range(element.map { it.toNumberOrString() })
        } else {
            BoundaryGap.Enabled(element.jsonPrimitive.booleanOrNull ?: false)
        }
    }
}

/** Serializer for [DataValue]; not needed directly in normal use. */
public object DataValueSerializer : KSerializer<DataValue> {
    override val descriptor: SerialDescriptor = JsonElement.serializer().descriptor

    override fun serialize(encoder: Encoder, value: DataValue) {
        encoder.encodeSerializableValue(JsonElement.serializer(), value.toJsonElement())
    }

    override fun deserialize(decoder: Decoder): DataValue =
        decoder.decodeSerializableValue(JsonElement.serializer()).toDataValue()
}

internal fun NumberOrString.toJsonElement(): JsonElement = when (this) {
    is NumberOrString.Number -> JsonPrimitive(value)
    is NumberOrString.Text -> JsonPrimitive(value)
}

private fun JsonElement.toNumberOrString(): NumberOrString {
    val primitive = jsonPrimitive
    return if (primitive.isString) {
        NumberOrString.Text(primitive.content)
    } else {
        NumberOrString.Number(
            primitive.doubleOrNull
                ?: throw SerializationException("Not a number or string: $primitive"),
        )
    }
}
