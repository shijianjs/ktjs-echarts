package demo.json.examples.shared

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import demo.json.linkItem
import demo.json.nodes
import demo.json.objectValue

/** The seven days of `graph-grid`; the fourth label is deliberately long, as upstream. */
internal val GRID_DAYS = arrayOf("Mon", "Tue", "Wed", "Very Loooong Thu", "Fri", "Sat", "Sun")

/**
 * The node values of `graph-grid`.
 *
 * Upstream generates them with `Math.round(Math.random() * 1000 * (index + 1))`; these are the
 * values the Kotlin/JS port of the same page draws with its `Lcg(20160601)`.
 */
internal val GRID_VALUES = listOf(526.0, 1394.0, 299.0, 3406.0, 3434.0, 459.0, 6774.0)

/** The four hand-placed nodes of `graph-simple`, which uses `layout: 'none'`. */
internal val SIMPLE_NODES: List<DataValue> =
    listOf(
        dataItem {
            name = "Node 1"
            x = 300.0
            y = 300.0
        },
        dataItem {
            name = "Node 2"
            x = 800.0
            y = 300.0
        },
        dataItem {
            name = "Node 3"
            x = 550.0
            y = 100.0
        },
        dataItem {
            name = "Node 4"
            x = 550.0
            y = 500.0
        },
    )

/**
 * Encodes a label for one graph edge.
 *
 * `series.links` is a `List<DataValue>` and an edge carries `source` / `target` instead of a value,
 * so it is assembled by [graphLink]; the sub-objects still come from the model classes.
 */
internal fun labelJson(option: LabelOption): JsonElement =
    EChartsJson.format.encodeToJsonElement(LabelOption.serializer(), option)

/** Encodes a line style for one graph edge; see [labelJson]. */
internal fun lineStyleJson(option: LineStyleOption): JsonElement =
    EChartsJson.format.encodeToJsonElement(LineStyleOption.serializer(), option)

/** One `{ source, target }` graph edge, with an optional label and line style. */
internal fun graphLink(
    source: String,
    target: String,
    label: LabelOption? = null,
    lineStyle: LineStyleOption? = null,
): DataValue {
    val entries = mutableListOf<Pair<String, JsonElement>>(
        "source" to JsonPrimitive(source),
        "target" to JsonPrimitive(target),
    )
    label?.let { entries += "label" to labelJson(it) }
    lineStyle?.let { entries += "lineStyle" to lineStyleJson(it) }
    return objectValue(*entries.toTypedArray())
}

/** The six links of `graph-simple`; the first two also show their label. */
internal val SIMPLE_LINKS: List<DataValue> =
    listOf(
        graphLink(
            "Node 1",
            "Node 2",
            label = LabelOption().apply { show = true },
            lineStyle = LineStyleOption().apply { width = 5.0 },
        ),
        graphLink("Node 2", "Node 1", label = LabelOption().apply { show = true }),
        graphLink("Node 1", "Node 3"),
        graphLink("Node 2", "Node 3"),
        graphLink("Node 2", "Node 4"),
        graphLink("Node 1", "Node 4"),
    )

/** The `count` nodes of one `graph-force2` panel, named by their index like upstream ids. */
internal fun forceNodes(count: Int): List<DataValue> =
    List(count) { node -> dataItem { id = "$node" } }

/**
 * The `count` edges of one `graph-force2` panel: a cycle, except for the two-node panel, which has a
 * single edge (upstream special-cases `count === 2` so the pair is not linked twice).
 */
internal fun forceLinks(count: Int): List<DataValue> =
    if (count == 2) {
        listOf(graphLink("0", "1"))
    } else {
        List(count) { index -> graphLink("$index", "${(index + 1) % count}") }
    }

/** The node list of a chord series: names only, the links carry the values. */
internal fun chordNodes(vararg names: String): List<DataValue> =
    names.map { nodeName -> dataItem { name = nodeName } }

/** One column of `chord-lineStyle-color`: the same links, a different edge color. */
internal fun SeriesOption.chordColumn(index: Int, edgeColor: String) {
    label { show = true }
    center = listOf(str("${(index * 2 + 1) / 6.0 * 100}%"), str("50%"))
    radius = NumberOrStringOrNumbers.of(str("28%"), str("32%"))
    lineStyle = LineStyleOption().apply { color = edgeColor.asColor() }
    data = chordNodes("A", "B", "C", "D")
    links =
        listOf(
            linkItem("A", "B", 30),
            linkItem("A", "C", 20),
            linkItem("B", "D", 10),
            linkItem("C", "A", 15),
            linkItem("D", "A", 25),
        )
}

/** Caption above one column of `chord-lineStyle-color`. */
internal fun EChartsOption.chordColumnTitle(index: Int, caption: String) {
    title {
        text = caption
        left = str("${(index * 2 + 1) / 6.0 * 100}%")
        top = str("25%")
        textAlign = "center"
        padding = NumberOrStringOrNumbers.of(0)
    }
}
