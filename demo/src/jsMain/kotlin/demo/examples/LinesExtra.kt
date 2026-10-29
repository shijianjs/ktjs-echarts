package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import zrender.ZRColor

/*
 * Lines examples that need a map: `lines-airline` draws 65k flight routes on the world map.
 *
 * Both the GeoJSON of the world and the flight table live in `demo/src/jsMain/resources/data/`
 * (see its `SOURCE.md`), and the page registers the map before it applies the option — exactly the
 * order of the official example, which loads them with `$.get`.
 */

private val linesExtra = Category(Groups.LINES, "examples/LinesExtra.kt")

/**
 * Shape of `flights.json`: airports as `[name, city, country, lng, lat]` rows, airlines as
 * `[name, country]` rows and routes as `[airline, from, to]` indices into the first two. The GL
 * pages read the smaller `data-gl` copy of the same table, whose `airlines` has 11 entries.
 */
internal external interface FlightsData {
    val airlines: Array<Array<JsAny>>
    val airports: Array<Array<JsAny>>
    val routes: Array<Array<Double>>
}

/** Reads one number of an airport row. */
internal fun Array<JsAny>.coordinate(index: Int): Double = this[index].unsafeCast<Double>()

val linesExtraEntries: List<CatalogEntry> =
    listOf(
        linesExtra.interactive("lines-airline", "65k+ 飞机航线", "65k+ Airline") { chart ->
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
        },
    )
