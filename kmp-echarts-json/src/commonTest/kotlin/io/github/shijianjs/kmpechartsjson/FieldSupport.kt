package io.github.shijianjs.kmpechartsjson

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

/** Encodes [value] with [serializer] under the shared option configuration. */
internal fun <T> T.encodeWith(serializer: KSerializer<T>): JsonObject =
    EChartsJson.format.encodeToJsonElement(serializer, this).jsonObject
