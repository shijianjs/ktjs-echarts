package demo.json.examples

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

/*
 * Graph and chord examples carried over from `graph-simple.ts`, `graph-force2.ts`, `graph-grid.ts`
 * and the four `chord-*.ts` files of apache/echarts-examples.
 *
 * The pages keep the official ids, so the Kotlin/JS port and the JSON port of the same chart share
 * one route.
 *
 * Compacted on purpose, because the model has no field for them:
 *
 * - `LineStyleOption` has no `curveness`, so the `curveness: 0.2` of the first two edges of
 *   `graph-simple` is dropped and those edges are drawn straight instead of slightly bent; the
 *   series-level `curveness: 0` it also writes is ECharts' default, so losing it changes nothing;
 * - a graph edge is not a `DataItem`, so the per-edge `symbolSize: [5, 20]` of `graph-simple` is
 *   dropped as well; the arrow sizes of `edgeSymbolSize` still apply;
 * - nothing else is missing: every other property of the seven options has a model field.
 *
 * `graph-grid` replaces `Math.random()` with the values of the Kotlin/JS port's seeded generator, so
 * both modes of that page draw the same picture. `graph-force2` is deterministic already; the force
 * layout itself still animates, as it does upstream.
 */

private val graphChord = JsonCategory("json/examples/GraphChordJson.kt")

/** The seven days of `graph-grid`; the fourth label is deliberately long, as upstream. */
private val GRID_DAYS = arrayOf("Mon", "Tue", "Wed", "Very Loooong Thu", "Fri", "Sat", "Sun")

/**
 * The node values of `graph-grid`.
 *
 * Upstream generates them with `Math.round(Math.random() * 1000 * (index + 1))`; these are the
 * values the Kotlin/JS port of the same page draws with its `Lcg(20160601)`.
 */
private val GRID_VALUES = listOf(526.0, 1394.0, 299.0, 3406.0, 3434.0, 459.0, 6774.0)

/** The four hand-placed nodes of `graph-simple`, which uses `layout: 'none'`. */
private val SIMPLE_NODES: List<DataValue> =
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
private fun labelJson(option: LabelOption): JsonElement =
    EChartsJson.format.encodeToJsonElement(LabelOption.serializer(), option)

/** Encodes a line style for one graph edge; see [labelJson]. */
private fun lineStyleJson(option: LineStyleOption): JsonElement =
    EChartsJson.format.encodeToJsonElement(LineStyleOption.serializer(), option)

/** One `{ source, target }` graph edge, with an optional label and line style. */
private fun graphLink(
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
private val SIMPLE_LINKS: List<DataValue> =
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
private fun forceNodes(count: Int): List<DataValue> =
    List(count) { node -> dataItem { id = "$node" } }

/**
 * The `count` edges of one `graph-force2` panel: a cycle, except for the two-node panel, which has a
 * single edge (upstream special-cases `count === 2` so the pair is not linked twice).
 */
private fun forceLinks(count: Int): List<DataValue> =
    if (count == 2) {
        listOf(graphLink("0", "1"))
    } else {
        List(count) { index -> graphLink("$index", "${(index + 1) % count}") }
    }

/** The node list of a chord series: names only, the links carry the values. */
private fun chordNodes(vararg names: String): List<DataValue> =
    names.map { nodeName -> dataItem { name = nodeName } }

/** One column of `chord-lineStyle-color`: the same links, a different edge color. */
private fun SeriesOption.chordColumn(index: Int, edgeColor: String) {
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
private fun EChartsOption.chordColumnTitle(index: Int, caption: String) {
    title {
        text = caption
        left = str("${(index * 2 + 1) / 6.0 * 100}%")
        top = str("25%")
        textAlign = "center"
        padding = NumberOrStringOrNumbers.of(0)
    }
}

public val graphChordJsonExamples: List<JsonExample> =
    listOf(
        graphChord.example("graph-simple") {
            title { text = "Basic Graph" }
            tooltip {}
            animationDurationUpdate = 1500.0
            animationEasingUpdate = "quinticInOut"
            series {
                graph {
                    layout = "none"
                    symbolSize = NumberOrStringOrNumbers.of(50)
                    roam = BooleanOrString.of(true)
                    label { show = true }
                    edgeSymbol = listOf("circle", "arrow")
                    edgeSymbolSize = NumberOrStringOrNumbers.numbers(4, 10)
                    edgeLabel = LabelOption().apply { fontSize = 20.0 }
                    data = SIMPLE_NODES
                    links = SIMPLE_LINKS
                    lineStyle = LineStyleOption().apply {
                        opacity = 0.9
                        width = 2.0
                    }
                }
            }
        },
        graphChord.example("graph-force2") {
            series {
                for (index in 0 until 16) {
                    val count = index + 2
                    graph {
                        layout = "force"
                        animation = false
                        data = forceNodes(count)
                        left = str("${(index % 4) * 25}%")
                        top = str("${(index / 4) * 25}%")
                        width = str("25%")
                        height = str("25%")
                        force =
                            ForceOption().apply {
                                repulsion = NumberOrStringOrNumbers.of(60)
                                edgeLength = NumberOrStringOrNumbers.of(2)
                            }
                        links = forceLinks(count)
                    }
                }
            }
        },
        graphChord.example("graph-grid") {
            title { text = "Graph on Cartesian" }
            tooltip {}
            xAxis {
                type = AxisType.Category
                boundaryGap = BoundaryGap.of(false)
                categories(*GRID_DAYS)
            }
            yAxis { type = AxisType.Value }
            series {
                graph {
                    layout = "none"
                    coordinateSystem = "cartesian2d"
                    symbolSize = NumberOrStringOrNumbers.of(40)
                    label { show = true }
                    edgeSymbol = listOf("circle", "arrow")
                    edgeSymbolSize = NumberOrStringOrNumbers.numbers(4, 10)
                    data = GRID_VALUES.map { value -> valueOf(value) }
                    links = List(GRID_VALUES.size - 1) { index -> graphLink("$index", "${index + 1}") }
                    lineStyle = LineStyleOption().apply { color = "#2f4554".asColor() }
                }
            }
        },
        graphChord.example("chord-simple") {
            tooltip {}
            legend {}
            series {
                chord {
                    clockwise = false
                    label { show = true }
                    lineStyle = LineStyleOption().apply { color = "target".asColor() }
                    data = chordNodes("A", "B", "C", "D")
                    links =
                        listOf(
                            linkItem("A", "B", 40),
                            linkItem("A", "C", 20),
                            linkItem("B", "D", 20),
                        )
                }
            }
        },
        graphChord.example("chord-style") {
            tooltip {}
            legend {}
            series {
                chord {
                    padAngle = 1.0
                    center = listOf(str("50%"), str("48%"))
                    radius = NumberOrStringOrNumbers.of(str("70%"), str("80%"))
                    data = chordNodes("A", "B", "C", "D", "E", "F", "G")
                    itemStyle =
                        ItemStyleOption().apply {
                            borderRadius = NumberOrStringOrNumbers.numbers(0, 15)
                            borderWidth = 2.0
                            borderColor = "#fff".asColor()
                        }
                    lineStyle = LineStyleOption().apply {
                        opacity = 0.3
                        color = "gradient".asColor()
                    }
                    emphasis { focus = "self" }
                    label {
                        show = true
                        position = "inside"
                        color = "#fff".asColor()
                        fontWeight = str("bold")
                    }
                    links =
                        listOf(
                            linkItem("A", "B", 14),
                            linkItem("A", "C", 8),
                            linkItem("B", "C", 20),
                            linkItem("B", "E", 15),
                            linkItem("C", "B", 8),
                            linkItem("C", "E", 3),
                            linkItem("D", "A", 12),
                            linkItem("D", "B", 3),
                            linkItem("E", "A", 15),
                            linkItem("E", "C", 5),
                            linkItem("F", "C", 5),
                            linkItem("G", "A", 6),
                            linkItem("G", "B", 8),
                            linkItem("G", "D", 4),
                        )
                }
            }
        },
        graphChord.example("chord-minAngle") {
            tooltip {}
            legend {}
            series {
                chord {
                    label { show = true }
                    minAngle = 30.0
                    data = chordNodes("A", "B", "C", "D", "E", "F")
                    links =
                        listOf(
                            linkItem("A", "B", 40),
                            linkItem("B", "C", 20),
                            linkItem("E", "A", 5),
                        )
                }
            }
        },
        graphChord.example("chord-lineStyle-color") {
            tooltip {}
            legend {}
            title {
                text = "lineStyle.color"
                textStyle { fontSize = 24.0 }
            }
            chordColumnTitle(0, "source")
            chordColumnTitle(1, "target")
            chordColumnTitle(2, "gradient")
            series {
                chord { chordColumn(0, "source") }
                chord { chordColumn(1, "target") }
                chord { chordColumn(2, "gradient") }
            }
        },
    )
