package demo.examples.lines

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.gauge.center

private val linesAirlineCategory = Category(Groups.LINES, "examples/lines/LinesAirline.kt")

val linesAirlineEntry: CatalogEntry =
    linesAirlineCategory.interactive("lines-airline", "65k+ 飞机航线", "65k+ Airline") { chart ->
            loadJson("data/world.json").then { world ->
                ECharts.registerMap(
                    "world",
                    MapInput(
                        unsafeJso<GeoJSONMapInput> {
                            geoJSON = world.unsafeCast<GeoJSONSourceInput>()
                        },
                    ),
                )
                loadJson("data/flights.json").then { json ->
                val flights = json.unsafeCast<FlightsData>()
                val routes =
                    Array(flights.routes.size) { index ->
                        val route = flights.routes[index]
                        val from = flights.airports[route[1].toInt()]
                        val to = flights.airports[route[2].toInt()]
                        arrayOf(
                            arrayOf(from.coordinate(3), from.coordinate(4)),
                            arrayOf(to.coordinate(3), to.coordinate(4)),
                        )
                    }
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        backgroundColor = ZRColor("#003")
                        title =
                            one(
                                unsafeJso<TitleOption> {
                                    text = "World Flights"
                                    left = StringOrDouble("center")
                                    textStyle = unsafeJso<TextStyleOption> { color = "#eee" }
                                },
                            )
                        geo =
                            one(
                                unsafeJso<GeoOption> {
                                    map = "world"
                                    left = StringOrDouble(0.0)
                                    right = StringOrDouble(0.0)
                                    silent = true
                                    roam = BooleanOrString(true)
                                    itemStyle =
                                        unsafeJso<ItemStyleOption> {
                                            borderColor = ZRColor("#003")
                                            color = ZRColor("#005")
                                        }
                                },
                            )
                        series =
                            arrayOf<SeriesOption>(
                                linesSeries {
                                    coordinateSystem = "geo"
                                    typedData =
                                        Array(routes.size) { index ->
                                            unsafeJso<LinesDataItemOption> { coords = routes[index] }
                                        }
                                    large = true
                                    largeThreshold = 100.0
                                    lineStyle =
                                        unsafeJso<LineStyleOption> {
                                            opacity = 0.05
                                            width = 0.5
                                            curveness = 0.3
                                        }
                                    blendMode = "lighter"
                                },
                            )
                    },
                )
                null
                }
                null
            }
        }
