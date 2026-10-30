package demo.json.examples

import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.nodeItem
import demo.json.nodes
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

/*
 * Sunburst examples carried over from `sunburst-*.ts` / `sunburst-book.js` of
 * apache/echarts-examples. The ids match the official pages, so the Kotlin/JS port and the JSON port
 * of the same chart share one route.
 *
 * Fields the model cannot express are omitted, not replaced:
 *
 * - `label.rotate: 'radial'` / `'tangential'` on the series itself: `LabelOption.rotate` is a
 *   `Double`, so the keyword forms only survive inside `levels`, which is raw JSON here.
 * - `sort: undefined` / `sort` callbacks: omitted, ECharts falls back to its own ordering.
 * - `sunburst-label-rotate` also sets root-level `silent: true` and a `label.formatter` callback;
 *   `EChartsOption` has no `silent` and the model only models string formatters.
 * - `sunburst-book` ends its last level with a `downplay` state, a legacy key ECharts 6 no longer
 *   reads, so it is left out.
 * - Data of `sunburst-monochrome` and `sunburst-drink` is downsampled; the shape (three levels,
 *   per-node colors, values on both inner and outer nodes) is unchanged.
 */

private val sunburst = JsonCategory("json/examples/SunburstJson.kt")

/** The three node colors of `sunburst-monochrome`. */
private const val MONO_1 = "#F54F4A"
private const val MONO_2 = "#FF8C75"
private const val MONO_3 = "#FFB499"

/** Palette and background of `sunburst-drink`; the parent nodes reuse their subtree's base color. */
private const val DRINK_FLORA = "#da0d68"
private const val DRINK_FLORAL = "#e0719c"
private const val DRINK_FRUITY = "#da1d23"
private const val DRINK_BERRY = "#dd4c51"
private const val DRINK_DRIED = "#c94a44"

/** Palette of `sunburst-book`; `BOOK_COLORS[1]` is also the fiction node's fill. */
private val BOOK_COLORS: List<String> = listOf("#FFAE57", "#FF7853", "#EA5151", "#CC3F57", "#9A2555")
private const val BOOK_BACKGROUND = "#2E2733"

/** Wraps a level radius, which the model only exposes as raw JSON. */
private fun NumberOrString.toJson(): JsonElement = when (this) {
    is NumberOrString.Number -> JsonPrimitive(value)
    is NumberOrString.Text -> JsonPrimitive(value)
}

/** One `levels` entry; the keys are ECharts' own, so the whole entry is raw JSON. */
private fun level(
    r0: NumberOrString? = null,
    r: NumberOrString? = null,
    itemStyle: JsonObject? = null,
    label: JsonObject? = null,
): JsonElement = buildJsonObject {
    r0?.let { put("r0", it.toJson()) }
    r?.let { put("r", it.toJson()) }
    itemStyle?.let { put("itemStyle", it) }
    label?.let { put("label", it) }
}

/** `itemStyle` of a [level]; `color` is a plain string, `borderWidth` a number. */
private fun levelItemStyle(
    color: String? = null,
    borderWidth: Number? = null,
    shadowBlur: Number? = null,
    shadowColor: String? = null,
): JsonObject = buildJsonObject {
    color?.let { put("color", JsonPrimitive(it)) }
    borderWidth?.let { put("borderWidth", JsonPrimitive(it)) }
    shadowBlur?.let { put("shadowBlur", JsonPrimitive(it)) }
    shadowColor?.let { put("shadowColor", JsonPrimitive(it)) }
}

/** `label` of a [level]; [rotate] is either a number or `"radial"` / `"tangential"`. */
private fun levelLabel(
    rotate: JsonElement? = null,
    fontSize: Number? = null,
    color: String? = null,
    align: String? = null,
    position: String? = null,
    padding: Number? = null,
    silent: Boolean? = null,
    textShadowBlur: Number? = null,
    textShadowColor: String? = null,
): JsonObject = buildJsonObject {
    rotate?.let { put("rotate", it) }
    fontSize?.let { put("fontSize", JsonPrimitive(it)) }
    color?.let { put("color", JsonPrimitive(it)) }
    align?.let { put("align", JsonPrimitive(it)) }
    position?.let { put("position", JsonPrimitive(it)) }
    padding?.let { put("padding", JsonPrimitive(it)) }
    silent?.let { put("silent", JsonPrimitive(it)) }
    textShadowBlur?.let { put("textShadowBlur", JsonPrimitive(it)) }
    textShadowColor?.let { put("textShadowColor", JsonPrimitive(it)) }
}

/** A hierarchy node without a name, as used by the monochrome and label-rotate pages. */
private fun valueNode(value: Number? = null, vararg children: DataItem): DataItem =
    DataItem().apply {
        this.value = value?.let { valueOf(it) }
        if (children.isNotEmpty()) this.children = children.toList()
    }

/** A nameless hierarchy node that carries its own fill color. */
private fun coloredValueNode(
    color: String,
    value: Number? = null,
    vararg children: DataItem,
): DataItem = valueNode(value, *children).apply {
    itemStyle { this.color = color.asColor() }
}

/** A named hierarchy node that carries its own fill color. */
private fun coloredNameNode(
    name: String,
    color: String,
    value: Number? = null,
    vararg children: DataItem,
): DataItem = nodeItem(name, value, *children).apply {
    itemStyle { this.color = color.asColor() }
}

/** The Grandpa / Nancy family, shared by `sunburst-simple` and `sunburst-borderRadius`. */
private val familyTree: List<DataValue> =
    nodes(
        nodeItem(
            "Grandpa",
            null,
            nodeItem(
                "Uncle Leo",
                15,
                nodeItem("Cousin Jack", 2),
                nodeItem("Cousin Mary", 5, nodeItem("Jackson", 2)),
                nodeItem("Cousin Ben", 4),
            ),
            nodeItem(
                "Father",
                10,
                nodeItem("Me", 5),
                nodeItem("Brother Peter", 1),
            ),
        ),
        nodeItem(
            "Nancy",
            null,
            nodeItem(
                "Uncle Nike",
                null,
                nodeItem("Cousin Betty", 1),
                nodeItem("Cousin Jenny", 2),
            ),
        ),
    )

/** The same family plus Aunt Jane and the Mike branch, used by `sunburst-visualMap`. */
private val visualMapTree: List<DataValue> =
    nodes(
        nodeItem(
            "Grandpa",
            null,
            nodeItem(
                "Uncle Leo",
                15,
                nodeItem("Cousin Jack", 2),
                nodeItem("Cousin Mary", 5, nodeItem("Jackson", 2)),
                nodeItem("Cousin Ben", 4),
            ),
            nodeItem("Aunt Jane", null, nodeItem("Cousin Kate", 4)),
            nodeItem(
                "Father",
                10,
                coloredNameNode("Me", "red", 5),
                nodeItem("Brother Peter", 1),
            ),
        ),
        nodeItem(
            "Mike",
            null,
            nodeItem(
                "Uncle Dan",
                null,
                nodeItem("Cousin Lucy", 3),
                nodeItem("Cousin Luck", 4, nodeItem("Nephew", 2)),
            ),
        ),
        nodeItem(
            "Nancy",
            null,
            nodeItem(
                "Uncle Nike",
                null,
                nodeItem("Cousin Betty", 1),
                nodeItem("Cousin Jenny", 2),
            ),
        ),
    )

/**
 * Four nameless roots of `sunburst-label-rotate`, sized so the outer nodes land on distinct
 * rotation modes.
 */
private val rotateTree: List<DataValue> =
    nodes(
        valueNode(
            8,
            valueNode(
                4,
                valueNode(2),
                valueNode(1),
                valueNode(1),
                valueNode(0.5),
            ),
            valueNode(2),
        ),
        valueNode(4, valueNode(null, valueNode(2))),
        valueNode(4, valueNode(null, valueNode(2))),
        valueNode(3, valueNode(null, valueNode(1))),
    )

/*
 * `sunburst-monochrome`: the page overrides the default level styles with `levels: []` and relies on
 * the series `itemStyle` (`#ddd`) plus a few per-node colors, so the hierarchy is built from
 * nameless nodes with and without a value.
 */
private val monochromeTree: List<DataValue> =
    nodes(
        coloredValueNode(
            MONO_1,
            null,
            coloredValueNode(
                MONO_1,
                5,
                coloredValueNode(MONO_1, 1),
                coloredValueNode(MONO_2, 2, coloredValueNode(MONO_2, 1)),
                valueNode(null, valueNode(1)),
            ),
            coloredValueNode(
                MONO_1,
                10,
                coloredValueNode(
                    MONO_3,
                    6,
                    coloredValueNode(MONO_1, 1),
                    valueNode(1),
                    coloredValueNode(MONO_2, 1),
                    valueNode(1),
                ),
                coloredValueNode(MONO_3, 2, valueNode(1)),
                valueNode(null, coloredValueNode(MONO_2, 1)),
            ),
        ),
        coloredValueNode(
            MONO_2,
            9,
            coloredValueNode(
                MONO_1,
                4,
                coloredValueNode(MONO_2, 2),
                valueNode(null, coloredValueNode(MONO_1, 1)),
            ),
            coloredValueNode(MONO_3, null, valueNode(3, valueNode(1), coloredValueNode(MONO_2, 1))),
        ),
        coloredValueNode(
            MONO_1,
            7,
            coloredValueNode(
                MONO_3,
                null,
                coloredValueNode(MONO_3, 1),
                coloredValueNode(MONO_2, 3, coloredValueNode(MONO_2, 1), valueNode(1)),
                coloredValueNode(MONO_1, 2, valueNode(1), coloredValueNode(MONO_1, 1)),
            ),
        ),
        coloredValueNode(
            MONO_1,
            null,
            coloredValueNode(
                MONO_3,
                6,
                coloredValueNode(MONO_2, 1),
                coloredValueNode(MONO_1, 2, coloredValueNode(MONO_2, 2)),
                coloredValueNode(MONO_3, 1),
            ),
            coloredValueNode(
                MONO_3,
                3,
                valueNode(1),
                valueNode(null, coloredValueNode(MONO_2, 1)),
                valueNode(1),
            ),
        ),
    )

/*
 * `sunburst-drink`: Flora and Fruity kept from the original lexicon, each with its category nodes
 * and one subtree of flavors. Values are all 1, as upstream.
 */
private val drinkTree: List<DataValue> =
    nodes(
        coloredNameNode(
            "Flora",
            DRINK_FLORA,
            null,
            coloredNameNode("Black Tea", "#975e6d", 1),
            coloredNameNode(
                "Floral",
                DRINK_FLORAL,
                null,
                coloredNameNode("Chamomile", "#f99e1c", 1),
                coloredNameNode("Rose", "#ef5a78", 1),
                coloredNameNode("Jasmine", "#f7f1bd", 1),
            ),
        ),
        coloredNameNode(
            "Fruity",
            DRINK_FRUITY,
            null,
            coloredNameNode(
                "Berry",
                DRINK_BERRY,
                null,
                coloredNameNode("Blackberry", "#3e0317", 1),
                coloredNameNode("Raspberry", "#e62969", 1),
                coloredNameNode("Blueberry", "#6569b0", 1),
                coloredNameNode("Strawberry", "#ef2d36", 1),
            ),
            coloredNameNode("Dried Fruit", DRINK_DRIED, null, coloredNameNode("Raisin", "#b53b54", 1)),
        ),
    )

/*
 * `sunburst-book`: the fiction branch, from genre to star rating to title.
 *
 * Every leaf carries `value: 1`, the shape the Kotlin/JS port uses: ECharts 6 draws nothing when no
 * node in the tree has a value, because the sector sizes are summed from the leaves.
 */
private val bookTree: List<DataValue> =
    nodes(
        coloredNameNode(
            "虚构",
            BOOK_COLORS[1],
            null,
            nodeItem(
                "小说",
                null,
                nodeItem("5☆", null, nodeItem("疼", 1), nodeItem("慈悲", 1), nodeItem("楼下的房客", 1)),
                nodeItem("4☆", null, nodeItem("虚无的十字架", 1), nodeItem("无声告白", 1), nodeItem("童年的终结", 1)),
                nodeItem("3☆", null, nodeItem("疯癫老人日记", 1)),
            ),
        ),
    )

public val sunburstJsonExamples: List<JsonExample> =
    listOf(
        sunburst.example("sunburst-simple") {
            series {
                sunburst {
                    data = familyTree
                    radius = NumberOrStringOrNumbers.of(num(0), str("90%"))
                }
            }
        },
        sunburst.example("sunburst-monochrome") {
            series {
                sunburst {
                    radius = NumberOrStringOrNumbers.of(str("15%"), str("80%"))
                    emphasis { focus = "ancestor" }
                    data = monochromeTree
                    levels = emptyList()
                    itemStyle {
                        color = "#ddd".asColor()
                        borderWidth = 2.0
                    }
                }
            }
        },
        sunburst.example("sunburst-drink") {
            title {
                text = "WORLD COFFEE RESEARCH SENSORY LEXICON"
                subtext = "Source: https://worldcoffeeresearch.org/work/sensory-lexicon/"
                sublink = "https://worldcoffeeresearch.org/work/sensory-lexicon/"
                textStyle {
                    fontSize = 14.0
                    align = "center"
                }
                subtextStyle { align = "center" }
            }
            series {
                sunburst {
                    data = drinkTree
                    radius = NumberOrStringOrNumbers.of(num(0), str("95%"))
                    emphasis { focus = "ancestor" }
                    levels = listOf(
                        level(),
                        level(
                            r0 = str("15%"),
                            r = str("35%"),
                            itemStyle = levelItemStyle(borderWidth = 2),
                            label = levelLabel(rotate = JsonPrimitive("tangential")),
                        ),
                        level(
                            r0 = str("35%"),
                            r = str("70%"),
                            label = levelLabel(align = "right"),
                        ),
                        level(
                            r0 = str("70%"),
                            r = str("72%"),
                            itemStyle = levelItemStyle(borderWidth = 3),
                            label = levelLabel(position = "outside", padding = 3, silent = false),
                        ),
                    )
                }
            }
        },
        sunburst.example("sunburst-book") {
            backgroundColor = BOOK_BACKGROUND.asColor()
            color = BOOK_COLORS.map { it.asColor() }
            series {
                sunburst {
                    center = listOf(str("50%"), str("48%"))
                    data = bookTree
                    label { color = BOOK_BACKGROUND.asColor() }
                    itemStyle {
                        borderColor = BOOK_BACKGROUND.asColor()
                        borderWidth = 2.0
                    }
                    levels = listOf(
                        level(),
                        level(
                            r0 = num(0),
                            r = num(40),
                            label = levelLabel(rotate = JsonPrimitive(0)),
                        ),
                        level(r0 = num(40), r = num(105)),
                        level(
                            r0 = num(115),
                            r = num(140),
                            itemStyle = levelItemStyle(
                                color = "transparent",
                                shadowBlur = 2,
                                shadowColor = BOOK_COLORS[2],
                            ),
                            label = levelLabel(
                                rotate = JsonPrimitive("tangential"),
                                fontSize = 10,
                                color = BOOK_COLORS[0],
                            ),
                        ),
                        level(
                            r0 = num(140),
                            r = num(145),
                            itemStyle = levelItemStyle(shadowBlur = 80, shadowColor = BOOK_COLORS[0]),
                            label = levelLabel(
                                position = "outside",
                                textShadowBlur = 5,
                                textShadowColor = "#333",
                            ),
                        ),
                    )
                }
            }
        },
        sunburst.example("sunburst-label-rotate") {
            series {
                sunburst {
                    radius = NumberOrStringOrNumbers.of(str("15%"), str("80%"))
                    emphasis { focus = "ancestor" }
                    data = rotateTree
                    label {
                        color = "#000".asColor()
                        textBorderColor = "#fff".asColor()
                        textBorderWidth = 2.0
                    }
                    levels = listOf(
                        level(),
                        level(
                            itemStyle = levelItemStyle(color = "#CD4949"),
                            label = levelLabel(rotate = JsonPrimitive("radial")),
                        ),
                        level(
                            itemStyle = levelItemStyle(color = "#F47251"),
                            label = levelLabel(rotate = JsonPrimitive("tangential")),
                        ),
                        level(
                            itemStyle = levelItemStyle(color = "#FFC75F"),
                            label = levelLabel(rotate = JsonPrimitive(0)),
                        ),
                    )
                }
            }
        },
        sunburst.example("sunburst-borderRadius") {
            series {
                sunburst {
                    data = familyTree
                    radius = NumberOrStringOrNumbers.of(num(60), str("90%"))
                    itemStyle {
                        borderRadius = NumberOrStringOrNumbers.of(7)
                        borderWidth = 2.0
                    }
                    label { show = false }
                }
            }
        },
        sunburst.example("sunburst-visualMap") {
            visualMap {
                type = VisualMapType.Continuous
                min = 0.0
                max = 10.0
                inRange {
                    color = colors("#2F93C8", "#AEC48F", "#FFDB5C", "#F98862")
                }
            }
            series {
                sunburst {
                    data = visualMapTree
                    radius = NumberOrStringOrNumbers.of(num(0), str("90%"))
                }
            }
        },
    )
