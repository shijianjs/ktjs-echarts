package demo.json.examples.sunburst

import demo.json.nodeItem
import demo.json.nodes
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import demo.json.examples.shared.fill

/** The three node colors of `sunburst-monochrome`. */
internal const val MONO_1 = "#F54F4A"

internal const val MONO_2 = "#FF8C75"

internal const val MONO_3 = "#FFB499"

/** Palette and background of `sunburst-drink`; the parent nodes reuse their subtree's base color. */
internal const val DRINK_FLORA = "#da0d68"

internal const val DRINK_FLORAL = "#e0719c"

internal const val DRINK_FRUITY = "#da1d23"

internal const val DRINK_BERRY = "#dd4c51"

internal const val DRINK_DRIED = "#c94a44"

/** Palette of `sunburst-book`; `BOOK_COLORS[1]` is also the fiction node's fill. */
internal val BOOK_COLORS: List<String> = listOf("#FFAE57", "#FF7853", "#EA5151", "#CC3F57", "#9A2555")

internal const val BOOK_BACKGROUND = "#2E2733"

/** Wraps a level radius, which the model only exposes as raw JSON. */
internal fun NumberOrString.toJson(): JsonElement = when (this) {
    is NumberOrString.Number -> JsonPrimitive(value)
    is NumberOrString.Text -> JsonPrimitive(value)
}

/** One `levels` entry; the keys are ECharts' own, so the whole entry is raw JSON. */
internal fun level(
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
internal fun levelItemStyle(
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
internal fun levelLabel(
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
internal fun valueNode(value: Number? = null, vararg children: DataItem): DataItem =
    DataItem().apply {
        this.value = value?.let { valueOf(it) }
        if (children.isNotEmpty()) this.children = children.toList()
    }

/** A nameless hierarchy node that carries its own fill color. */
internal fun coloredValueNode(
    color: String,
    value: Number? = null,
    vararg children: DataItem,
): DataItem = valueNode(value, *children).apply {
    itemStyle { this.color = color.asColor() }
}

/** A named hierarchy node that carries its own fill color. */
internal fun coloredNameNode(
    name: String,
    color: String,
    value: Number? = null,
    vararg children: DataItem,
): DataItem = nodeItem(name, value, *children).apply {
    itemStyle { this.color = color.asColor() }
}

/** The Grandpa / Nancy family, shared by `sunburst-simple` and `sunburst-borderRadius`. */
internal val familyTree: List<DataValue> =
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
internal val visualMapTree: List<DataValue> =
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
internal val rotateTree: List<DataValue> =
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
internal val monochromeTree: List<DataValue> =
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
internal val drinkTree: List<DataValue> =
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
internal val bookTree: List<DataValue> =
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
