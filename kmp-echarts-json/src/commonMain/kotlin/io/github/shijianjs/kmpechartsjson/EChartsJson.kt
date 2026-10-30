package io.github.shijianjs.kmpechartsjson

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

/**
 * Shared JSON configuration for every option model.
 *
 * Unset properties must not reach the chart: ECharts distinguishes an absent option from an
 * explicit `null`/`false`, and its own defaults only apply to absent keys. Encoding therefore
 * skips defaults and nulls, while every model property that carries a chart default is nullable.
 */
public object EChartsJson {
    /** Encoder used for compact output and for [EChartsOption.toJsonElement]. */
    public val format: Json = Json {
        encodeDefaults = false
        explicitNulls = false
        ignoreUnknownKeys = true
    }

    /** Encoder used when readability matters more than payload size. */
    public val pretty: Json = Json(format) {
        prettyPrint = true
        prettyPrintIndent = "  "
    }

    /**
     * Serializes [option] to the JSON string accepted by `echarts.init(...).setOption(...)`.
     *
     * @param prettyPrint indent the output with two spaces.
     */
    public fun encode(option: EChartsOption, prettyPrint: Boolean = false): String =
        (if (prettyPrint) pretty else format).encodeToString(
            JsonObject.serializer(),
            option.toJsonElement(),
        )

    /** Serializes [option] to a [JsonObject] for further editing. */
    public fun encodeToJsonElement(option: EChartsOption): JsonObject = option.toJsonElement()
}

/**
 * Converts the option to a [JsonObject], merging [EChartsOption.extra] on top.
 *
 * Keys in [EChartsOption.extra] win, so this is the typed escape hatch for options this model does
 * not declare yet.
 */
public fun EChartsOption.toJsonElement(): JsonObject {
    val element = EChartsJson.format.encodeToJsonElement(EChartsOption.serializer(), this).jsonObject
    val extras = extra ?: return element
    return JsonObject(element.toMutableMap() + extras)
}

/**
 * Serializes the option to the JSON string accepted by `setOption`.
 *
 * @param prettyPrint indent the output with two spaces.
 */
public fun EChartsOption.toJsonString(prettyPrint: Boolean = false): String =
    EChartsJson.encode(this, prettyPrint)

/** Returns the object with [key] set to [value], replacing an existing entry. */
public fun JsonObject.with(key: String, value: JsonElement): JsonObject =
    JsonObject(this + (key to value))

/** Returns the object with [key] set to [value], replacing an existing entry. */
public fun JsonObject.with(key: String, value: String): JsonObject =
    with(key, JsonPrimitive(value))

/** Returns the object with [key] set to [value], replacing an existing entry. */
public fun JsonObject.with(key: String, value: Number): JsonObject =
    with(key, JsonPrimitive(value))

/** Returns the object with [key] set to [value], replacing an existing entry. */
public fun JsonObject.with(key: String, value: Boolean): JsonObject =
    with(key, JsonPrimitive(value))

/** Returns the object with [key] set to [value], replacing an existing entry. */
public fun JsonObject.with(key: String, value: JsonObject): JsonObject =
    JsonObject(this + (key to value))

/** Returns the object with [key] set to [value], replacing an existing entry. */
public fun JsonObject.with(key: String, value: JsonArray): JsonObject =
    JsonObject(this + (key to value))

/** Returns the object with [other]'s entries merged in at the top level. */
public operator fun JsonObject.plus(other: Map<String, JsonElement>): JsonObject =
    JsonObject(toMutableMap() + other)

/**
 * Recursively merges [other] into this object. Nested objects are merged key by key; every other
 * value is replaced by the one in [other].
 */
public fun JsonObject.deepMerge(other: JsonObject): JsonObject {
    val result = toMutableMap()
    for ((key, incoming) in other) {
        val current = result[key]
        result[key] = if (current is JsonObject && incoming is JsonObject) {
            current.deepMerge(incoming)
        } else {
            incoming
        }
    }
    return JsonObject(result)
}

/** Serializes any tree of [JsonElement]s to a string, indented with two spaces. */
public fun JsonElement.prettyPrint(): String =
    EChartsJson.pretty.encodeToString(JsonElement.serializer(), this)

/** Serializes this object to a compact JSON string by default. */
public fun JsonObject.toEChartsJsonString(prettyPrint: Boolean = false): String =
    (if (prettyPrint) EChartsJson.pretty else EChartsJson.format)
        .encodeToString(JsonObject.serializer(), this)
