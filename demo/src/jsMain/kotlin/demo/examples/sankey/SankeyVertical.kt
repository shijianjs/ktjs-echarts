package demo.examples.sankey

import demo.CatalogEntry
import demo.Groups
import echarts.Orients
import echarts.options.EChartsOption
import echarts.EmphasisFocuses
import echarts.options.EmphasisOption
import echarts.options.LabelOption
import echarts.options.LabelPosition
import echarts.options.LineStyleOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.sankeySeries
import js.objects.unsafeJso
import zrender.ZRColor
import demo.examples.Category

private val sankeyVerticalCategory = Category(Groups.SANKEY, "examples/sankey/SankeyVertical.kt")

val sankeyVerticalEntry: CatalogEntry =
    sankeyVerticalCategory.chart("sankey-vertical", "垂直方向的桑基图", "Sankey Orient Vertical") {
            unsafeJso<EChartsOption> {
                tooltip = sankeyTooltip
                animation = false
                series =
                    arrayOf<SeriesOption>(
                        sankeySeries {
                            bottom = StringOrDouble("10%")
                            orient = Orients.vertical
                            label = unsafeJso<LabelOption> { position = LabelPosition("top") }
                            lineStyle = unsafeJso<LineStyleOption> { color = zrender.ZRColor("source"); curveness = 0.5 }
                            emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.adjacency }
                            typedData =
                                arrayOf(
                                    node("a"),
                                    node("b"),
                                    node("a1"),
                                    node("b1"),
                                    node("c"),
                                    node("e"),
                                )
                            links =
                                arrayOf(
                                    link("a", "a1", 5.0),
                                    link("e", "b", 3.0),
                                    link("a", "b1", 3.0),
                                    link("b1", "a1", 1.0),
                                    link("b1", "c", 2.0),
                                    link("b", "c", 1.0),
                                )
                        },
                    )
            }
        }
