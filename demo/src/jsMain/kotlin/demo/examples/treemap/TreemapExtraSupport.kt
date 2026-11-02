package demo.examples.treemap

import kotlin.js.JsAny
import kotlin.js.unsafeCast
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import demo.examples.matrix.dimension
import demo.examples.shared.toolbox

/** The `levels` of `treemap-disk`. */
internal fun diskLevels(): Array<TreemapSeriesLevelOption> =
    arrayOf(
        unsafeJso {
            itemStyle =
                unsafeJso<TreemapSeriesItemStyleOption> {
                    borderWidth = 0.0
                    gapWidth = 5.0
                }
        },
        unsafeJso {
            itemStyle = unsafeJso<TreemapSeriesItemStyleOption> { gapWidth = 1.0 }
        },
        unsafeJso {
            colorSaturation = DoubleArrayOrNone(arrayOf(0.35, 0.5))
            itemStyle =
                unsafeJso<TreemapSeriesItemStyleOption> {
                    gapWidth = 1.0
                    borderColorSaturation = 0.6
                }
        },
    )

internal const val HOUSEHOLDS_IN_AMERICA_2012 = 113616229.0

/** One node of `obama_budget_proposal_2012.json`; `value` is `[amount, previous, ratio]`. */
internal external interface BudgetNode {
    val name: String
    val id: String
    val value: Array<JsAny?>?
    val children: Array<BudgetNode>?
}

/** The two levels of the Obama budget treemap. */
internal fun budgetLevels(): Array<TreemapSeriesLevelOption> =
    arrayOf(
        unsafeJso {
            colorMappingBy = ColorMappingBy("id")
            itemStyle =
                unsafeJso<TreemapSeriesItemStyleOption> {
                    borderWidth = 3.0
                    gapWidth = 3.0
                }
        },
        unsafeJso {
            itemStyle = unsafeJso<TreemapSeriesItemStyleOption> { gapWidth = 1.0 }
        },
    )

/**
 * Copies one node of the table, adding the amount per household as the fourth dimension.
 *
 * The original also offers two re-orderings (`change from 2010` first, and a colour-by-id variant);
 * they are switched by the toolbox of the official page, which the port does not carry.
 */
internal fun budgetNode(node: BudgetNode): TreemapSeriesNodeItemOption {
    val values = node.value?.map { it?.unsafeCast<Double>() }.orEmpty()
    val amount = values.firstOrNull() ?: 0.0
    val dimensions = mutableListOf<OptionDataValue?>()
    values.forEach { value -> dimensions += value?.let { OptionDataValue(it) } }
    dimensions += OptionDataValue(amount / HOUSEHOLDS_IN_AMERICA_2012)
    val copy = node
    return unsafeJso {
        name = OptionId(copy.name)
        id = OptionId(copy.id)
        // The node value may be an array of dimensions; upstream allows that on the same field.
        value = OptionDataValueOrArray(arrayOf(*dimensions.toTypedArray())).unsafeCast<OptionDataValue>()
        if (copy.children != null) children = Array(copy.children!!.size) { budgetNode(copy.children!![it]) }
    }
}
