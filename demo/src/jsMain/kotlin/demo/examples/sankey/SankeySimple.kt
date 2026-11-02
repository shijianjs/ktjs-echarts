package demo.examples.sankey

import demo.CatalogEntry
import demo.Groups
import echarts.options.EChartsOption
import echarts.EmphasisFocuses
import echarts.options.EmphasisOption
import echarts.options.SeriesOption
import echarts.options.sankeySeries
import js.objects.unsafeJso
import demo.examples.Category

private val sankeySimpleCategory = Category(Groups.SANKEY, "examples/sankey/SankeySimple.kt")

val sankeySimpleEntry: CatalogEntry =
    sankeySimpleCategory.chart("sankey-simple", "基础桑基图", "Basic Sankey") {
            unsafeJso<EChartsOption> {
                series =
                    arrayOf<SeriesOption>(
                        sankeySeries {
                            emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.adjacency }
                            typedData =
                                arrayOf(
                                    node("a"),
                                    node("b"),
                                    node("a1"),
                                    node("a2"),
                                    node("b1"),
                                    node("c"),
                                )
                            links =
                                arrayOf(
                                    link("a", "a1", 5.0),
                                    link("a", "a2", 3.0),
                                    link("b", "b1", 8.0),
                                    link("a", "b1", 3.0),
                                    link("b1", "a1", 1.0),
                                    link("b1", "c", 2.0),
                                )
                        },
                    )
            }
        }
