package demo.examples.treemap

import io.github.shijianjs.ktjs.echarts.ColorMappingBy
import io.github.shijianjs.ktjs.echarts.ECharts
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.matrix.dimension
import demo.examples.matrix.linearMap
import demo.examples.shared.every
import demo.examples.shared.row
import demo.examples.tree.leaves

/** Keys of a raw JSON object of the statistics table. */
internal fun statisticKeys(node: JsAny): Array<String> =
    js("(node) => Object.keys(node)")(node) as Array<String>

/** Value of [key] inside a raw JSON object of the statistics table. */
internal fun statisticChild(node: JsAny, key: String): JsAny =
    js("(node, key) => node[key]")(node, key) as JsAny

/** The `$count` of a statistics node, or `null` where the table has none. */
internal fun statisticCount(node: JsAny): Double? =
    js("(node) => typeof node['\$count'] === 'number' ? node['\$count'] : null")(node) as Double?

/**
 * One node of `ec-option-doc-statistics-201604.json` as a treemap node.
 *
 * The table nests plain objects keyed by the option path and marks every level with a `$count`; a
 * node without sub-keys is a leaf whose value is that count, and a node with sub-keys gets an extra
 * child carrying its own count (the `convert()` callback of the original) so that its total stays
 * visible next to its children.
 */
internal fun statisticNode(raw: JsAny, path: String): TreemapSeriesNodeItemOption {
    val children =
        statisticKeys(raw)
            .filterNot { it.startsWith("$") }
            .map { key -> statisticNode(statisticChild(raw, key), "$path.$key") }
    val count = statisticCount(raw)
    return unsafeJso {
        name = OptionId(path)
        if (children.isEmpty()) {
            value = OptionDataValue(count ?: 1.0)
        } else {
            this.children =
                (children +
                    unsafeJso<TreemapSeriesNodeItemOption> {
                        name = OptionId(path)
                        value = OptionDataValue(count ?: 0.0)
                    })
                    .toTypedArray()
        }
    }
}

/** The top-level nodes of the statistics table; the root object itself is not drawn. */
internal fun statisticNodes(root: JsAny): Array<TreemapSeriesNodeItemOption> =
    statisticKeys(root)
        .filterNot { it.startsWith("$") }
        .map { key -> statisticNode(statisticChild(root, key), key) }
        .toTypedArray()

/** One level of the four-level ladder of `treemap-drill-down`. */
internal fun statisticLevel(
    gapWidth: Double,
    borderWidth: Double? = null,
    borderColor: String? = null,
    saturation: Array<Double>? = null,
    borderColorSaturation: Double? = null,
): TreemapSeriesLevelOption =
    unsafeJso {
        if (saturation != null) colorSaturation = DoubleArrayOrNone(saturation)
        itemStyle =
            unsafeJso<TreemapSeriesItemStyleOption> {
                if (borderWidth != null) this.borderWidth = borderWidth
                if (borderColor != null) this.borderColor = ZRColor(borderColor)
                this.gapWidth = gapWidth
                if (borderColorSaturation != null) this.borderColorSaturation = borderColorSaturation
            }
    }

/** One node of `obama_budget_proposal_2012.json`; `value` is `[amount, previous, change]`. */
internal external interface VisualBudgetNode {
    val name: String
    val id: String
    val value: Array<JsAny?>?
    val children: Array<VisualBudgetNode>?
}

/** `visualMin` / `visualMax` of the color scale, and the bounds the mapped values stay inside. */
internal const val VISUAL_MIN = -100.0

internal const val VISUAL_MAX = 100.0

internal const val VISUAL_MIN_BOUND = -40.0

internal const val VISUAL_MAX_BOUND = 40.0

/** The numbers of one budget node, ignoring dimensions the table leaves empty. */
internal fun budgetValues(node: VisualBudgetNode): List<Double?> =
    node.value.orEmpty().map { it?.unsafeCast<Double?>() }

/**
 * Maps the change of [node] onto the fourth dimension, the `convertData()` callback of the original.
 *
 * [min] and [max] are taken over the siblings of the node, so every level gets its own scale.
 */
internal fun budgetMappedValue(change: Double?, min: Double, max: Double): Double {
    val mapped =
        when {
            change == null -> 0.0
            change > 0 ->
                ECharts.number.linearMap(
                    change,
                    arrayOf(0.0, max),
                    arrayOf(VISUAL_MAX_BOUND, VISUAL_MAX),
                    true,
                )
            change < 0 ->
                ECharts.number.linearMap(
                    change,
                    arrayOf(min, 0.0),
                    arrayOf(VISUAL_MIN, VISUAL_MIN_BOUND),
                    true,
                )
            else -> 0.0
        }
    return if (mapped.isFinite()) mapped else 0.0
}

/** Converts one level of the budget tree, recursing into the children with their own scale. */
internal fun budgetNodes(nodes: Array<VisualBudgetNode>): Array<TreemapSeriesNodeItemOption> {
    var min = Double.POSITIVE_INFINITY
    var max = Double.NEGATIVE_INFINITY
    for (node in nodes) {
        val change = budgetValues(node).getOrNull(2)
        if (change != null && change < min) min = change
        if (change != null && change > max) max = change
    }
    return Array(nodes.size) { index ->
        val node = nodes[index]
        val values = budgetValues(node)
        val dimensions =
            values.map { value -> value?.let { OptionDataValue(it) } } +
                OptionDataValue(budgetMappedValue(values.getOrNull(2), min, max))
        unsafeJso {
            name = OptionId(node.name)
            id = OptionId(node.id)
            // The node value is a row of dimensions; upstream allows that on the same field.
            value = OptionDataValueOrArray(arrayOf(*dimensions.toTypedArray())).unsafeCast<OptionDataValue>()
            if (node.children != null) children = budgetNodes(node.children!!)
        }
    }
}

/** `echarts.format.addCommas(amount * 1000) + '$'`, or `-` when the dimension is missing. */
internal fun budgetAmount(value: Double?): String =
    if (value == null || !value.isFinite()) {
        "-"
    } else {
        ECharts.format.addCommas(StringOrDouble(value * 1000)) + "$"
    }

/** Plain-text replacement of the HTML formatter of `treemap-visual`. */
internal fun budgetTooltip(params: TopLevelFormatterParams): TooltipContent {
    val info = params.unsafeCast<CallbackDataParams>()
    val values = info.value.unsafeCast<Array<JsAny?>>().map { it?.unsafeCast<Double?>() }
    val change =
        values.getOrNull(2)?.takeIf { it.isFinite() }
            ?.let { "${ECharts.number.round(StringOrDouble(it), 2.0)}%" }
            ?: "-"
    val text =
        "${info.name}  2012: ${budgetAmount(values.getOrNull(0))}, " +
            "2011: ${budgetAmount(values.getOrNull(1))}, change: $change"
    return TooltipContent(text)
}

/** The two levels of `treemap-visual`: the outer border, then the three-color scale. */
internal fun budgetVisualLevels(): Array<TreemapSeriesLevelOption> =
    arrayOf(
        unsafeJso {
            itemStyle =
                unsafeJso<TreemapSeriesItemStyleOption> {
                    borderWidth = 3.0
                    borderColor = ZRColor("#333")
                    gapWidth = 3.0
                }
        },
        unsafeJso {
            color =
                ColorArrayOrNone(
                    arrayOf(ZRColor("#942e38"), ZRColor("#aaa"), ZRColor("#269f3c")),
                )
            colorMappingBy = ColorMappingBy("value")
            itemStyle = unsafeJso<TreemapSeriesItemStyleOption> { gapWidth = 1.0 }
        },
    )

/** The three levels of `treemap-show-parent`; the third one also applies to the deeper levels. */
internal fun parentLevels(): Array<TreemapSeriesLevelOption> =
    arrayOf(
        unsafeJso {
            itemStyle =
                unsafeJso<TreemapSeriesItemStyleOption> {
                    borderColor = ZRColor("#777")
                    borderWidth = 0.0
                    gapWidth = 1.0
                }
            upperLabel = unsafeJso<TreemapSeriesLabelOption> { show = false }
        },
        unsafeJso {
            itemStyle =
                unsafeJso<TreemapSeriesItemStyleOption> {
                    borderColor = ZRColor("#555")
                    borderWidth = 5.0
                    gapWidth = 1.0
                }
            emphasis =
                unsafeJso<EmphasisOption> {
                    itemStyle = unsafeJso<ItemStyleOption> { borderColor = ZRColor("#ddd") }
                }
        },
        unsafeJso {
            colorSaturation = DoubleArrayOrNone(arrayOf(0.35, 0.5))
            itemStyle =
                unsafeJso<TreemapSeriesItemStyleOption> {
                    borderWidth = 5.0
                    gapWidth = 1.0
                    borderColorSaturation = 0.6
                }
        },
    )

/** `echarts-package-size.json`: a package tree whose nodes carry their size as the value. */
internal external interface PackageSizeTree {
    val children: Array<TreemapSeriesNodeItemOption?>
}
