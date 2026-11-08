package io.github.shijianjs.ktjs.echarts.json

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

/**
 * The `source` of a dataset.
 *
 * The three shapes correspond to ECharts' row/column layouts; [Raw] accepts anything else,
 * including arrays typed as `JsonElement`.
 */
@Serializable(with = DatasetSourceSerializer::class)
public sealed interface DatasetSource {
    /** Rows, each row an array; the first row is the header when `sourceHeader` is enabled. */
    public data class Rows(
        public val rows: List<List<DataValue>>,
    ) : DatasetSource

    /** Rows of named columns. */
    public data class ObjectRows(
        public val rows: List<Map<String, DataValue>>,
    ) : DatasetSource

    /** Columns of values, keyed by column name. */
    public data class Columns(
        public val columns: Map<String, List<DataValue>>,
    ) : DatasetSource

    /** Any other JSON shape, for example a typed array. */
    public data class Raw(public val value: JsonElement) : DatasetSource
}

/**
 * A dataset shared by several series.
 *
 * A dataset can either carry [source] data or transform another dataset with [transform].
 */
@Serializable
public class DatasetOption {
    /** Component id used by `series.datasetId` cross references. */
    public var id: String? = null

    /** Inline data. */
    public var source: DatasetSource? = null

    /** Dimension names or definitions, in data order. */
    public var dimensions: List<JsonElement>? = null

    /** Whether each row is a series (`"row"`) or each column is a series (`"column"`). */
    public var seriesLayoutBy: String? = null

    /** Whether the first row or column contains the dimension names. */
    public var sourceHeader: Boolean? = null

    /** Index of the dataset this one transforms. */
    public var fromDatasetIndex: Int? = null

    /** Id of the dataset this one transforms. */
    public var fromDatasetId: String? = null

    /** Index of the transform result to use when the source has several. */
    public var fromTransformResult: Int? = null

    /** Transform pipeline, for example `{"type": "filter", "config": {"dimension": "x"}}`. */
    public var transform: JsonElement? = null
}

/** Serializer for [DatasetSource]; not needed directly in normal use. */
public object DatasetSourceSerializer : KSerializer<DatasetSource> {
    override val descriptor: SerialDescriptor = JsonElement.serializer().descriptor

    override fun serialize(encoder: Encoder, value: DatasetSource) {
        val element = when (value) {
            is DatasetSource.Rows -> JsonArray(value.rows.map { row ->
                JsonArray(row.map { it.toJsonElement() })
            })

            is DatasetSource.ObjectRows -> JsonArray(value.rows.map { row ->
                JsonObject(row.mapValues { (_, item) -> item.toJsonElement() })
            })

            is DatasetSource.Columns -> JsonObject(value.columns.mapValues { (_, column) ->
                JsonArray(column.map { it.toJsonElement() })
            })

            is DatasetSource.Raw -> value.value
        }
        encoder.encodeSerializableValue(JsonElement.serializer(), element)
    }

    override fun deserialize(decoder: Decoder): DatasetSource {
        val element = decoder.decodeSerializableValue(JsonElement.serializer())
        return when (element) {
            is JsonObject -> DatasetSource.Columns(
                element.mapValues { (_, column) ->
                    (column as? JsonArray)?.map { it.toDataValue() }
                        ?: throw SerializationException("Dataset column must be an array: $column")
                },
            )

            is JsonArray -> if (element.all { it is JsonObject }) {
                DatasetSource.ObjectRows(
                    element.map { row ->
                        (row as JsonObject).mapValues { (_, item) -> item.toDataValue() }
                    },
                )
            } else {
                DatasetSource.Rows(
                    element.map { row ->
                        (row as? JsonArray)?.map { it.toDataValue() }
                            ?: throw SerializationException("Dataset row must be an array: $row")
                    },
                )
            }

            else -> DatasetSource.Raw(element)
        }
    }
}

/** Builds a row-oriented dataset source from arrays of values. */
public fun datasetRows(vararg rows: List<DataValue>): DatasetSource =
    DatasetSource.Rows(rows.toList())

/** Builds a row-oriented dataset source from numbers. */
public fun datasetNumbers(vararg rows: List<Number>): DatasetSource =
    DatasetSource.Rows(rows.map { row -> row.map { DataValue.Number(it.toDouble()) } })

/** Builds a column-oriented dataset source from named columns. */
public fun datasetColumns(columns: Map<String, List<DataValue>>): DatasetSource =
    DatasetSource.Columns(columns)
