package demo.examples.geo

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.math.PI
import kotlin.math.cos
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category

private val geoGraphCategory = Category(Groups.GEO, "examples/geo/GeoGraph.kt")

val geoGraphEntry: CatalogEntry =
    geoGraphCategory.interactive("geo-graph", "地理坐标系上的关系图", "Geo Graph") { chart ->
            loadJson("data/ch.geo.json").then { json ->
                ECharts.registerMap(
                    "ch",
                    MapInput(unsafeJso<GeoJSONMapInput> { geoJSON = json.unsafeCast<GeoJSONSourceInput>() }),
                )
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        title = one(unsafeJso<TitleOption> { text = "Travel Routes" })
                        geo =
                            one(
                                unsafeJso<GeoOption> {
                                    map = "ch"
                                    roam = BooleanOrString(true)
                                    // The map is laid out for the latitude of Switzerland.
                                    aspectScale = cos(47 * PI / 180)
                                    label =
                                        unsafeJso<LabelOption> {
                                            show = true
                                            textBorderColor = "#fff"
                                            textBorderWidth = 2.0
                                        }
                                },
                            )
                        tooltip = one(unsafeJso<TooltipOption> {})
                        series =
                            one(
                                graphSeries {
                                    coordinateSystem = "geo"
                                    typedData =
                                        dataItems(
                                            routeNode("a", 7.667821250000001, 46.791734269956265),
                                            routeNode("b", 7.404848750000001, 46.516308805996054),
                                            routeNode("c", 7.376673125000001, 46.24628858538375),
                                            routeNode("d", 8.015320625000001, 46.39460918238572),
                                            routeNode("e", 8.616400625, 46.7020608630855),
                                            routeNode("f", 8.869981250000002, 46.37539345234199),
                                            routeNode("g", 9.546196250000001, 46.58676648282309),
                                            routeNode("h", 9.311399375, 47.182454114178896),
                                            routeNode("i", 9.085994375000002, 47.55395822835779),
                                            routeNode("j", 8.653968125000002, 47.47709530818285),
                                            routeNode("k", 8.203158125000002, 47.44506909144329),
                                        )
                                    edges =
                                        arrayOf(
                                            routeEdge("a", "b"),
                                            routeEdge("b", "c"),
                                            routeEdge("c", "d"),
                                            routeEdge("d", "e"),
                                            routeEdge("e", "f"),
                                            routeEdge("f", "g"),
                                            routeEdge("g", "h"),
                                            routeEdge("h", "i"),
                                            routeEdge("i", "j"),
                                            routeEdge("j", "k"),
                                        )
                                    edgeSymbol = StringOrStringArray(arrayOf("none", "arrow"))
                                    edgeSymbolSize = DoubleOrDoubleArray(arrayOf(5.0))
                                    lineStyle =
                                        unsafeJso<LineStyleOption> {
                                            color = ZRColor("#718adbff")
                                            opacity = 1.0
                                        }
                                },
                            )
                    },
                )
                null
            }
        }
