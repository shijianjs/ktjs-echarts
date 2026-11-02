package demo.examples.graph

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import zrender.ZRColor
import demo.examples.Category
import demo.examples.matrix.dimension
import demo.examples.scatter.point
import demo.examples.shared.legendNames

private val graphLifeExpectancyCategory = Category(Groups.GRAPH, "examples/graph/GraphLifeExpectancy.kt")

val graphLifeExpectancyEntry: CatalogEntry =
    graphLifeExpectancyCategory.interactive("graph-life-expectancy", "预期寿命", "Graph Life Expectancy") { chart ->
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
        }
