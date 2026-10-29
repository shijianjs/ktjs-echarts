package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.ChartHolder
import demo.ui.loadJson
import echarts.ColorMappingBy
import echarts.ECharts
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import zrender.ZRColor

/*
 * Treemap pages whose tree is too big to be written as Kotlin source, so it is shipped as a static
 * file (see the `SOURCE.md` of `demo/src/jsMain/resources/data/`):
 *
 * - `treemap-drill-down` reads the option-usage statistics of the 2016/04 documentation and derives
 *   the nesting from the dotted option paths of the table (`convert()` of the original).
 * - `treemap-show-parent` reuses `disk.tree.json` of `treemap-disk` but keeps the parent labels.
 * - `treemap-sunburst-transition` alternates between a treemap and a sunburst of the same tree with
 *   the universal transition, driven by [ChartHolder.setInterval].
 * - `treemap-visual` maps the budget change of every node onto the color, scaling the values per
 *   level like the original does.
 *
 * The tooltip formatters that build HTML in the originals are replaced by plain-text ones, and
 * `treemap-drill-down` drops the `treePathInfo` path prefix of the disk page for the same reason.
 */

private val treemapData = Category(Groups.TREEMAP, "examples/TreemapData.kt")

/** Keys of a raw JSON object of the statistics table. */
private fun statisticKeys(node: JsAny): Array<String> =
    js("(node) => Object.keys(node)")(node) as Array<String>

/** Value of [key] inside a raw JSON object of the statistics table. */
private fun statisticChild(node: JsAny, key: String): JsAny =
    js("(node, key) => node[key]")(node, key) as JsAny

/** The `$count` of a statistics node, or `null` where the table has none. */
private fun statisticCount(node: JsAny): Double? =
    js("(node) => typeof node['\$count'] === 'number' ? node['\$count'] : null")(node) as Double?

/**
 * One node of `ec-option-doc-statistics-201604.json` as a treemap node.
 *
 * The table nests plain objects keyed by the option path and marks every level with a `$count`; a
 * node without sub-keys is a leaf whose value is that count, and a node with sub-keys gets an extra
 * child carrying its own count (the `convert()` callback of the original) so that its total stays
 * visible next to its children.
 */
private fun statisticNode(raw: JsAny, path: String): TreemapSeriesNodeItemOption {
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
private fun statisticNodes(root: JsAny): Array<TreemapSeriesNodeItemOption> =
    statisticKeys(root)
        .filterNot { it.startsWith("$") }
        .map { key -> statisticNode(statisticChild(root, key), key) }
        .toTypedArray()

/** One level of the four-level ladder of `treemap-drill-down`. */
private fun statisticLevel(
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
private external interface VisualBudgetNode {
    val name: String
    val id: String
    val value: Array<JsAny?>?
    val children: Array<VisualBudgetNode>?
}

/** `visualMin` / `visualMax` of the color scale, and the bounds the mapped values stay inside. */
private const val VISUAL_MIN = -100.0
private const val VISUAL_MAX = 100.0
private const val VISUAL_MIN_BOUND = -40.0
private const val VISUAL_MAX_BOUND = 40.0

/** The numbers of one budget node, ignoring dimensions the table leaves empty. */
private fun budgetValues(node: VisualBudgetNode): List<Double?> =
    node.value.orEmpty().map { it?.unsafeCast<Double?>() }

/**
 * Maps the change of [node] onto the fourth dimension, the `convertData()` callback of the original.
 *
 * [min] and [max] are taken over the siblings of the node, so every level gets its own scale.
 */
private fun budgetMappedValue(change: Double?, min: Double, max: Double): Double {
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
private fun budgetNodes(nodes: Array<VisualBudgetNode>): Array<TreemapSeriesNodeItemOption> {
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
private fun budgetAmount(value: Double?): String =
    if (value == null || !value.isFinite()) {
        "-"
    } else {
        ECharts.format.addCommas(StringOrDouble(value * 1000)) + "$"
    }

/** Plain-text replacement of the HTML formatter of `treemap-visual`. */
private fun budgetTooltip(params: TopLevelFormatterParams): TooltipContent {
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
private fun budgetVisualLevels(): Array<TreemapSeriesLevelOption> =
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

val treemapDataEntries: List<CatalogEntry> =
    listOf(
        treemapData.interactive("treemap-drill-down", "ECharts 配置项查询分布", "ECharts Option Query") { chart ->
            loadJson("data/ec-option-doc-statistics-201604.json").then { json ->
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        title =
                            one(
                                unsafeJso<TitleOption> {
                                    text = "ECharts Options"
                                    subtext = "2016/04"
                                    // The original passes the name of the drill-down field here.
                                    left = StringOrDouble("leafDepth")
                                },
                            )
                        tooltip = one(unsafeJso<TooltipOption> {})
                        series =
                            arrayOf<SeriesOption>(
                                treemapSeries {
                                    name = "option"
                                    visibleMin = 300.0
                                    leafDepth = 2.0
                                    typedData = statisticNodes(json).unsafeCast<Array<TreemapSeriesNodeItemOption?>>()
                                    levels =
                                        arrayOf(
                                            statisticLevel(4.0, 4.0, "#555"),
                                            statisticLevel(2.0, 2.0, saturation = arrayOf(0.3, 0.6), borderColorSaturation = 0.7),
                                            statisticLevel(1.0, saturation = arrayOf(0.3, 0.5), borderColorSaturation = 0.6),
                                            statisticLevel(0.0, saturation = arrayOf(0.3, 0.5)),
                                        )
                                },
                            )
                    },
                )
                null
            }
        },
        treemapData.interactive("treemap-show-parent", "显示父层级标签", "Show Parent Labels") { chart ->
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
                                    upperLabel =
                                        unsafeJso<TreemapSeriesLabelOption> {
                                            show = true
                                            height = 30.0
                                        }
                                    itemStyle = unsafeJso<TreemapSeriesItemStyleOption> { borderColor = ZRColor("#fff") }
                                    levels = parentLevels()
                                    typedData = json.unsafeCast<Array<TreemapSeriesNodeItemOption?>>()
                                },
                            )
                    },
                )
                null
            }
        },
        treemapData.interactive(
            "treemap-sunburst-transition",
            "矩形树图和旭日图的动画过渡",
            "Transition between Treemap and Sunburst",
            setup = { chart, holder ->
                loadJson("data/echarts-package-size.json").then { json ->
                    val node = json.unsafeCast<PackageSizeTree>()
                    val treemap =
                        unsafeJso<EChartsOption> {
                            series =
                                arrayOf<SeriesOption>(
                                    treemapSeries {
                                        id = "echarts-package-size"
                                        animationDurationUpdate = AnimationDuration(1000.0)
                                        roam = BooleanOrString(false)
                                        universalTransition = BooleanOrUniversalTransitionOption(true)
                                        label = unsafeJso<LabelOption> { show = true }
                                        breadcrumb = unsafeJso<TreemapBreadcrumbOption> { show = false }
                                        typedData = node.children
                                    },
                                )
                        }
                    val sunburst =
                        unsafeJso<EChartsOption> {
                            series =
                                arrayOf<SeriesOption>(
                                    sunburstSeries {
                                        id = "echarts-package-size"
                                        radius =
                                            StringOrDoubleOrArray(
                                                arrayOf(StringOrDouble("20%"), StringOrDouble("90%")),
                                            )
                                        animationDurationUpdate = AnimationDuration(1000.0)
                                        universalTransition = BooleanOrUniversalTransitionOption(true)
                                        itemStyle =
                                            unsafeJso<ItemStyleOption> {
                                                borderWidth = 1.0
                                                borderColor = ZRColor("rgba(255,255,255,.5)")
                                            }
                                        label = unsafeJso<LabelOption> { show = false }
                                        typedData = node.children.unsafeCast<Array<SunburstSeriesNodeItemOption?>>()
                                    },
                                )
                        }
                    var current = treemap
                    chart.setOption(current)
                    holder.setInterval(
                        {
                            current = if (current === treemap) sunburst else treemap
                            chart.setOption(current)
                        },
                        3000,
                    )
                    null
                }
            },
        ),
        treemapData.interactive("treemap-visual", "映射为渐变色", "Gradient Mapping") { chart ->
            loadJson("data/obama_budget_proposal_2012.json").then { json ->
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        title =
                            one(
                                unsafeJso<TitleOption> {
                                    left = StringOrDouble("center")
                                    text = "Gradient Mapping"
                                    subtext = "Growth > 0: green; Growth < 0: red; Growth = 0: grey"
                                },
                            )
                        tooltip =
                            one(
                                unsafeJso<TooltipOption> {
                                    formatter = TooltipFormatter(::budgetTooltip)
                                },
                            )
                        series =
                            arrayOf<SeriesOption>(
                                treemapSeries {
                                    name = "ALL"
                                    top = StringOrDouble(80.0)
                                    typedData =
                                        budgetNodes(json.unsafeCast<Array<VisualBudgetNode>>())
                                            .unsafeCast<Array<TreemapSeriesNodeItemOption?>>()
                                    label =
                                        unsafeJso<LabelOption> {
                                            show = true
                                            formatter = LabelFormatter("{b}")
                                        }
                                    itemStyle = unsafeJso<TreemapSeriesItemStyleOption> { borderColor = ZRColor("black") }
                                    visualMin = VISUAL_MIN
                                    visualMax = VISUAL_MAX
                                    visualDimension = StringOrDouble(3.0)
                                    levels = budgetVisualLevels()
                                },
                            )
                    },
                )
                null
            }
        },
    )

/** The three levels of `treemap-show-parent`; the third one also applies to the deeper levels. */
private fun parentLevels(): Array<TreemapSeriesLevelOption> =
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
private external interface PackageSizeTree {
    val children: Array<TreemapSeriesNodeItemOption?>
}
