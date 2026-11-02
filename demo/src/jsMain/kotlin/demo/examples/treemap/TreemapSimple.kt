package demo.examples.treemap

import demo.CatalogEntry
import demo.Groups
import echarts.options.EChartsOption
import echarts.options.SeriesOption
import echarts.options.treemapSeries
import js.objects.unsafeJso
import demo.examples.Category

private val treemapSimpleCategory = Category(Groups.TREEMAP, "examples/treemap/TreemapSimple.kt")

val treemapSimpleEntry: CatalogEntry =
    treemapSimpleCategory.chart("treemap-simple", "基础矩形树图", "Basic Treemap") {
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
        }
