package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.options.EChartsOption
import echarts.options.OptionDataValue
import echarts.options.OptionId
import echarts.options.SeriesOption
import echarts.options.TreemapSeriesNodeItemOption
import echarts.options.one
import echarts.options.treemapSeries
import js.objects.unsafeJso

/*
 * Treemap examples ported from `public/examples/ts/treemap-*.ts` of apache/echarts-examples.
 *
 * `treemap-disk`, `treemap-obama` and `treemap-visual` are not ported: they build their tree from
 * data fetched from the example server.
 */

private val treemap = Category(Groups.TREEMAP, "examples/Treemap.kt")

/** A node of a treemap, with optional children. */
private fun treemapNode(
    name: String,
    value: Double,
    vararg children: TreemapSeriesNodeItemOption,
): TreemapSeriesNodeItemOption =
    unsafeJso {
        this.name = OptionId(name)
        this.value = OptionDataValue(value)
        if (children.isNotEmpty()) this.children = Array(children.size) { children[it] }
    }

val treemapEntries: List<CatalogEntry> =
    listOf(
        treemap.chart("treemap-simple", "基础矩形树图", "Basic Treemap") {
            unsafeJso<EChartsOption> {
                series =
                    arrayOf<SeriesOption>(
                        treemapSeries {
                            typedData =
                                arrayOf(
                                    treemapNode(
                                        "nodeA",
                                        10.0,
                                        treemapNode("nodeAa", 4.0),
                                        treemapNode("nodeAb", 6.0),
                                    ),
                                    treemapNode("nodeB", 20.0, treemapNode("nodeBa", 20.0, treemapNode("nodeBa1", 20.0))),
                                )
                        },
                    )
            }
        },
    )
