package demo.json

import io.github.shijianjs.ktjs.echarts.json.DataItem
import io.github.shijianjs.ktjs.echarts.json.DataValue
import io.github.shijianjs.ktjs.echarts.json.asDataValue
import io.github.shijianjs.ktjs.echarts.json.dataItem
import io.github.shijianjs.ktjs.echarts.json.point
import io.github.shijianjs.ktjs.echarts.json.valueOf
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

/**
 * Small builders shared by the JSON examples.
 *
 * They only wrap the model's generic value types, so the examples stay focused on the option
 * structure instead of repeating `DataItem().apply { ... }` for every named value.
 */

/** One `{ name, value }` data entry, the shape used by pie, funnel and gauge data. */
public fun namedValue(name: String, value: Number): DataValue =
    dataItem {
        this.name = name
        this.value = valueOf(value)
    }

/** Maps `name to value` pairs to `{ name, value }` data entries. */
public fun namedValues(vararg entries: Pair<String, Number>): List<DataValue> =
    entries.map { (name, value) -> namedValue(name, value) }

/** Maps `x to y` pairs to two-element points. */
public fun points(vararg entries: Pair<Number, Number>): List<DataValue> =
    entries.map { (x, y) -> point(x, y) }

/** Maps triples to three-element points, the shape of heatmap data. */
public fun triples(vararg entries: Triple<Number, Number, Number>): List<DataValue> =
    entries.map { (x, y, value) ->
        DataValue.Array(listOf(valueOf(x), valueOf(y), valueOf(value)))
    }

/** Builds a hierarchy node: every child is a [DataItem], so nodes can be nested. */
public fun nodeItem(
    name: String,
    value: Number? = null,
    vararg children: DataItem,
): DataItem =
    DataItem().apply {
        this.name = name
        this.value = value?.let(::valueOf)
        if (children.isNotEmpty()) this.children = children.toList()
    }

/** Converts hierarchy nodes to the value list `series.data` expects. */
public fun nodes(vararg children: DataItem): List<DataValue> =
    children.map(DataItem::asDataValue)

/** Maps rows of plain numbers to array data values, used by candlestick and box plots. */
public fun rowsOf(vararg rows: List<Number>): List<DataValue> =
    rows.map { row -> DataValue.Array(row.map(::valueOf)) }

/** Builds a JSON object data value from named entries, the shape of graph and sankey links. */
public fun objectValue(vararg entries: Pair<String, JsonElement>): DataValue =
    DataValue.Object(JsonObject(entries.toMap()))

/** One `{ source, target, value }` edge of a graph, sankey or chord example. */
public fun linkItem(source: String, target: String, value: Number): DataValue =
    objectValue(
        "source" to JsonPrimitive(source),
        "target" to JsonPrimitive(target),
        "value" to JsonPrimitive(value),
    )

/** One `[time, value, name]` row of a theme river or `[date, value]` pair of a calendar. */
public fun timedValue(time: String, value: Number, name: String? = null): DataValue =
    DataValue.Array(
        buildList {
            add(DataValue.Text(time))
            add(valueOf(value))
            if (name != null) add(DataValue.Text(name))
        },
    )
