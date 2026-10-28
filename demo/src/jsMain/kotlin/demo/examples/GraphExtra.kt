package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.ChartHolder
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import kotlin.math.round
import zrender.ZRColor

/*
 * Graph examples that read their node list from a dataset: `graph-life-expectancy` draws one chain
 * of points per country on a cartesian coordinate system.
 *
 * The table is `demo/src/jsMain/resources/data/life-expectancy.json` (see its `SOURCE.md`).
 *
 * `graph-grid` needs no data file at all: it is the shortest page of the family that places nodes on
 * a `cartesian2d` coordinate system by their value alone, so it lives here as the family's remaining
 * cartesian page. Its `Math.random()` magnitudes are replaced by [Lcg], which keeps the drawing
 * stable across visits.
 *
 * `graph-force-dynamic` grows a force layout one node at a time; it is the family's page that needs the
 * chart's own box (`getWidth`/`getHeight` for the fixed center node), so it goes through
 * `Category.interactive`.
 */

private val graphExtra = Category(Groups.GRAPH, "examples/GraphExtra.kt")

/** Shape of `life-expectancy.json`: country names plus one row per point. */
private external interface LifeExpectancyData {
    val counties: Array<String>
    val series: Array<Array<Array<JsAny>>>
}

/** One point of a country chain: the year is the label, the row itself the value. */
private fun lifeExpectancyNode(row: Array<JsAny>): GraphDataItem {
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
private val GRID_DAYS = arrayOf("Mon", "Tue", "Wed", "Very Loooong Thu", "Fri", "Sat", "Sun")

/** The node values of `graph-grid`, `round(random * 1000 * (index + 1))` of the original. */
private val GRID_VALUES =
    run {
        val random = Lcg(20160601)
        GRID_DAYS.indices.map { index -> round(random.next() * 1000 * (index + 1)) }
    }

/** The links of `graph-grid`: every node points at the next day. */
private val GRID_LINKS =
    Array(GRID_DAYS.size - 1) { index ->
        unsafeJso<GraphEdgeItemOption> {
            source = StringOrDouble(index.toDouble())
            target = StringOrDouble((index + 1).toDouble())
        }
    }

val graphExtraEntries: List<CatalogEntry> =
    listOf(
        graphExtra.interactive("graph-life-expectancy", "预期寿命", "Graph Life Expectancy") { chart ->
            loadJson("data/life-expectancy.json").then { json ->
                val table = json.unsafeCast<LifeExpectancyData>()
                val series =
                    Array(table.counties.size) { countryIndex ->
                        val country = table.counties[countryIndex]
                        // One point per year, in the order of the rows of the table.
                        val rows =
                            table.series
                                .mapNotNull { yearData -> yearData.firstOrNull { it[3].unsafeCast<String>() == country } }
                        val links =
                            (0 until rows.size - 1).map { index ->
                                unsafeJso<GraphEdgeItemOption> {
                                    source = StringOrDouble(index.toDouble())
                                    target = StringOrDouble((index + 1).toDouble())
                                }
                            }
                        graphSeries {
                            name = country
                            coordinateSystem = "cartesian2d"
                            typedData = Array(rows.size) { index -> lifeExpectancyNode(rows[index]) }
                            this.links = links.toTypedArray()
                            edgeSymbol = StringOrStringArray(arrayOf("none", "arrow"))
                            edgeSymbolSize = DoubleOrDoubleArray(arrayOf(5.0))
                            legendHoverLink = false
                            lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("#333") }
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    borderWidth = 1.0
                                    borderColor = ZRColor("#333")
                                }
                            label =
                                unsafeJso<LabelOption> {
                                    color = "#333"
                                    position = LabelPosition("right")
                                }
                            symbolSize = SymbolSize(10.0)
                            animationDelay = AnimationDelay { index, _ -> index * 100 }
                        }
                    }
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        visualMap =
                            one(
                                unsafeJso<VisualMapOption> {
                                    show = false
                                    min = 0.0
                                    max = 100.0
                                    dimension = 1.0
                                },
                            )
                        legend =
                            one(
                                unsafeJso<LegendOption> {
                                    data = legendNames(*table.counties)
                                    selectedMode = SelectionMode("single")
                                    right = StringOrDouble(100.0)
                                },
                            )
                        grid =
                            one(
                                unsafeJso<GridOption> {
                                    left = StringOrDouble(0.0)
                                    bottom = StringOrDouble(0.0)
                                    containLabel = true
                                    top = StringOrDouble(80.0)
                                },
                            )
                        xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.value })
                        yAxis =
                            one(
                                unsafeJso<YAxisOption> {
                                    type = AxisTypes.value
                                    scale = true
                                },
                            )
                        dataZoom = arrayOf(unsafeJso<DataZoomOption> { type = DataZoomTypes.inside })
                        this.series = Array(series.size) { series[it] as SeriesOption }
                    },
                )
                null
            }
        },
        graphExtra.chart(
            "graph-grid",
            "笛卡尔坐标系上的 Graph",
            "Graph on Cartesian",
            "节点直接落在直角坐标系的取值上，链子从周一走到周日，`layout: 'none'` 让位置完全由数据决定。",
            "Nodes placed on a `cartesian2d` coordinate system with `layout: 'none'`, chained from " +
                "Monday to Sunday.",
        ) {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Graph on Cartesian" })
                tooltip = one(unsafeJso<TooltipOption> {})
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            boundaryGap = BooleanOrStringArray(false)
                            data = strings(*GRID_DAYS)
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                series =
                    arrayOf<SeriesOption>(
                        graphSeries {
                            layout = GraphLayouts.none
                            coordinateSystem = "cartesian2d"
                            symbolSize = SymbolSize(40.0)
                            label = unsafeJso<LabelOption> { show = true }
                            edgeSymbol = StringOrStringArray(arrayOf("circle", "arrow"))
                            edgeSymbolSize = DoubleOrDoubleArray(arrayOf(4.0, 10.0))
                            typedData = numbers(*GRID_VALUES.toDoubleArray())
                            links = GRID_LINKS
                            lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("#2f4554") }
                        },
                    )
            }
        },
        graphExtra.interactive(
            "graph-force-dynamic",
            "动态增加图节点",
            "Graph Dynamic",
            summaryZh = "每 200ms 往力导向布局里加一个节点和一条边，节点从画布中心的那颗定住不动。",
            summaryEn = "A force layout that gains a node and an edge every 200 ms, growing out of one fixed node at the center.",
        ) { chart, holder ->
            ForceField(chart, Lcg(20160613), holder).start()
        },
    )

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
private class ForceField(
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
