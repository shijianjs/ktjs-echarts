package demo.examples.graph

import demo.ui.ChartHolder
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import kotlin.math.round
import demo.examples.matrix.names
import demo.examples.scatter.point
import demo.examples.shared.Lcg
import demo.examples.shared.every
import demo.examples.shared.fixed
import demo.examples.shared.row
import demo.examples.themeriver.days

/** Shape of `life-expectancy.json`: country names plus one row per point. */
internal external interface LifeExpectancyData {
    val counties: Array<String>
    val series: Array<Array<Array<JsAny>>>
}

/** One point of a country chain: the year is the label, the row itself the value. */
internal fun lifeExpectancyNode(row: Array<JsAny>): GraphDataItem {
    val year = row[4].unsafeCast<Double>()
    val value = row.unsafeCast<OptionDataValueOrArray>()
    val showLabel = year.toInt() % 20 == 0 && year > 1940
    return GraphDataItem(
        unsafeJso<GraphNodeItemOption> {
            name = year.toInt().toString()
            this.value = value
            label =
                unsafeJso<LabelOption> {
                    show = showLabel
                    position = LabelPosition("top")
                }
        },
    )
}

/** The days of `graph-grid`; one label is deliberately long, as in the original. */
internal val GRID_DAYS = arrayOf("Mon", "Tue", "Wed", "Very Loooong Thu", "Fri", "Sat", "Sun")

/** The node values of `graph-grid`, `round(random * 1000 * (index + 1))` of the original. */
internal val GRID_VALUES =
    run {
        val random = Lcg(20160601)
        GRID_DAYS.indices.map { index -> round(random.next() * 1000 * (index + 1)) }
    }

/** The links of `graph-grid`: every node points at the next day. */
internal val GRID_LINKS =
    Array(GRID_DAYS.size - 1) { index ->
        unsafeJso<GraphEdgeItemOption> {
            source = StringOrDouble(index.toDouble())
            target = StringOrDouble((index + 1).toDouble())
        }
    }

/**
 * The growing node and edge lists of `graph-force-dynamic`.
 *
 * The state belongs to one page view: the field is constructed with the chart and dropped when the page
 * unmounts, so revisiting the route starts from the single fixed node again. Upstream picks the two
 * ends of each new edge with `Math.random()`; a [Lcg] keeps the graph identical across visits, which is
 * what lets the end-to-end suite compare frames.
 *
 * The node ids follow upstream literally, including the gap it produces: upstream reads the id as
 * `data.length + ''` after the push, so the first generated node is `"2"` and `"0"`/`"1"` never appear.
 * Here the same value is the list size plus one, taken before the add.
 */
internal class ForceField(
    private val chart: EChartsInstance,
    private val random: Lcg,
    private val holder: ChartHolder,
) {
    private val nodeList = mutableListOf<GraphDataItem>()
    private val edgeList = mutableListOf<GraphEdgeItemOption>()

    /** Publish the single fixed node and start the 200 ms growth loop. */
    fun start() {
        nodeList.add(
            GraphDataItem(
                unsafeJso<GraphNodeItemOption> {
                    fixed = true
                    x = chart.getWidth() / 2
                    y = chart.getHeight() / 2
                    symbolSize = SymbolSize(20.0)
                    id = "-1"
                },
            ),
        )
        chart.setOption(
            unsafeJso<EChartsOption> {
                series =
                    arrayOf<SeriesOption>(
                        graphSeries {
                            layout = GraphLayouts.force
                            animation = false
                            force =
                                unsafeJso<GraphForceOption> {
                                    repulsion = DoubleOrDoubleArray(100.0)
                                    edgeLength = DoubleOrDoubleArray(5.0)
                                }
                            typedData = nodeList.toTypedArray()
                            edges = arrayOf()
                        },
                    )
            },
        )
        holder.setInterval({ tick() }, 200)
    }

    /** Add one node and, when the two ends differ, the edge between them. */
    private fun tick() {
        nodeList.add(
            GraphDataItem(
                unsafeJso<GraphNodeItemOption> { id = "${nodeList.size + 1}" },
            ),
        )
        val source = round((nodeList.size - 1) * random.next())
        val target = round((nodeList.size - 1) * random.next())
        if (source != target) {
            edgeList.add(
                unsafeJso<GraphEdgeItemOption> {
                    this.source = StringOrDouble(source)
                    this.target = StringOrDouble(target)
                },
            )
        }
        chart.setOption(
            unsafeJso<EChartsOption> {
                series =
                    one<SeriesOption>(
                        unsafeJso<GraphSeriesOption> {
                            roam = BooleanOrString(true)
                            typedData = nodeList.toTypedArray()
                            edges = edgeList.toTypedArray()
                        },
                    )
            },
        )
    }
}
