package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import zrender.ZRColor

/*
 * Treemap examples that read their tree from a dataset instead of an inline table.
 *
 * The data lives in `demo/src/jsMain/resources/data/` (see its `SOURCE.md`): `disk.tree.json` is a
 * 750 KB tree of a scanned disk, which is why it is shipped as a static file rather than as Kotlin
 * source. Both pages therefore mount the chart first and apply the option once the fetch settles.
 *
 * The tooltip of `treemap-disk` shows the folder path built from `treePathInfo`; that formatter
 * returns HTML, which the project's UI rules keep out of the sources, so the port shows the name and
 * the size instead.
 */

private val treemapExtra = Category(Groups.TREEMAP, "examples/TreemapExtra.kt")

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

private const val HOUSEHOLDS_IN_AMERICA_2012 = 113616229.0

/** One node of `obama_budget_proposal_2012.json`; `value` is `[amount, previous, ratio]`. */
private external interface BudgetNode {
    val name: String
    val id: String
    val value: Array<JsAny?>?
    val children: Array<BudgetNode>?
}

/** The two levels of the Obama budget treemap. */
private fun budgetLevels(): Array<TreemapSeriesLevelOption> =
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
private fun budgetNode(node: BudgetNode): TreemapSeriesNodeItemOption {
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

val treemapExtraEntries: List<CatalogEntry> =
    listOf(
        treemapExtra.interactive("treemap-disk", "磁盘占用", "Disk Usage") { chart ->
            loadJson("data/disk.tree.json").then { json ->
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        title =
                            one(
                                unsafeJso<TitleOption> {
                                    text = "Disk Usage"
                                    left = StringOrDouble("center")
                                },
                            )
                        tooltip = one(unsafeJso<TooltipOption> { formatter = TooltipFormatter("{b}: {c} KB") })
                        series =
                            arrayOf<SeriesOption>(
                                treemapSeries {
                                    name = "Disk Usage"
                                    visibleMin = 300.0
                                    label =
                                        unsafeJso<LabelOption> {
                                            show = true
                                            formatter = LabelFormatter("{b}")
                                        }
                                    itemStyle = unsafeJso<TreemapSeriesItemStyleOption> { borderColor = ZRColor("#fff") }
                                    levels = diskLevels()
                                    typedData = json.unsafeCast<Array<TreemapSeriesNodeItemOption?>>()
                                },
                            )
                    },
                )
                null
            }
        },
        treemapExtra.interactive("treemap-obama", "3.7 万亿美元支出构成", "How $3.7 Trillion is Spent") { chart ->
            loadJson("data/obama_budget_proposal_2012.json").then { json ->
                val tree = json.unsafeCast<Array<BudgetNode>>()
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        title =
                            one(
                                unsafeJso<TitleOption> {
                                    text = "How $3.7 Trillion is Spent"
                                    left = StringOrDouble("center")
                                },
                            )
                        tooltip =
                            one(
                                unsafeJso<TooltipOption> {
                                    formatter = TooltipFormatter("{b}: {c}")
                                },
                            )
                        series =
                            arrayOf<SeriesOption>(
                                treemapSeries {
                                    name = "Where \$3.7 Trillion Goes"
                                    top = StringOrDouble(40.0)
                                    bottom = StringOrDouble(10.0)
                                    left = StringOrDouble("center")
                                    width = StringOrDouble("90%")
                                    // The colour of a node follows its id, not its position.
                                    levels = budgetLevels()
                                    label =
                                        unsafeJso<LabelOption> {
                                            show = true
                                            formatter = LabelFormatter("{b}")
                                        }
                                    typedData = Array(tree.size) { index -> budgetNode(tree[index]) }
                                },
                            )
                    },
                )
                null
            }
        },
    )
