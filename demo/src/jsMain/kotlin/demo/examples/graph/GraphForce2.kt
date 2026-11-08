package demo.examples.graph

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.GraphLayouts
import io.github.shijianjs.ktjs.echarts.options.DoubleOrDoubleArray
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.graphSeries
import js.objects.unsafeJso
import demo.examples.Category

private val graphForce2Category = Category(Groups.GRAPH, "examples/graph/GraphForce2.kt")

val graphForce2Entry: CatalogEntry =
    graphForce2Category.chart("graph-force2", "力引导布局", "Force Layout") {
            unsafeJso<EChartsOption> {
                series = Array<SeriesOption>(16) { index ->
                    val count = index + 2
                    graphSeries {
                        layout = GraphLayouts.force
                        animation = false
                        typedData = Array(count) { position -> idNode(position) }
                        left = StringOrDouble("${index % 4 * 25}%")
                        top = StringOrDouble("${index / 4 * 25}%")
                        width = StringOrDouble("25%")
                        height = StringOrDouble("25%")
                        force =
                            unsafeJso {
                                repulsion = DoubleOrDoubleArray(60.0)
                                edgeLength = DoubleOrDoubleArray(2.0)
                            }
                        edges =
                            Array(count) { position ->
                                link("$position", "${(position + 1) % count}")
                            }
                    }
                }
            }
        }
