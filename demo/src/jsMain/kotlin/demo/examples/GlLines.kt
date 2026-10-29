package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.listen
import demo.ui.loadJson
import echarts.Orients
import echarts.Payload
import echarts.options.LegendOption
import echarts.options.SelectionModes
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.SymbolSize
import echarts.options.one
import echartsgl.BooleanOrAuto
import echartsgl.CoordinateSystems3D
import echartsgl.EChartsGlActions
import echartsgl.GlBlendModes
import echartsgl.GlColor
import echartsgl.GlEnvironment
import echartsgl.MouseButtons
import echartsgl.NumberOrTexture
import echartsgl.QualityTypes
import echartsgl.ShadingTypes
import echartsgl.TextureSource
import echartsgl.options.AmbientCubemapLightOption
import echartsgl.options.EChartsGlOption
import echartsgl.options.Geo3DOption
import echartsgl.options.Geo3DGroundPlaneOption
import echartsgl.options.GlobeOption
import echartsgl.options.LightOption
import echartsgl.options.Lines3DDataItem
import echartsgl.options.PostEffectOption
import echartsgl.options.RealisticMaterialOption
import echartsgl.options.Scatter3DDataItem
import echartsgl.options.lines3DSeries
import echartsgl.options.scatter3DSeries
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import web.events.Event
import web.events.EventType
import web.events.removeEventListener
import web.window.window
import zrender.ZRColor

/*
 * The two `lines3D` pages of the `gl/` directory of apache/echarts-examples.
 *
 * Both draw the same table of 17k+ routes of `flights.json` (the `data-gl` copy, which is smaller
 * than the 65k-route table of the 2D `lines-airline` page): one on a globe, one on a `geo3D`.
 *
 * The geo3D page of the original toggles the trail effect on any key press; here the listener is
 * released through `ChartHolder.onUnmount`, so it does not outlive the page.
 */

private val glLines = Category(Groups.GL, "examples/GlLines.kt")

/** `keydown` as a typed event. */
private val KEYDOWN: EventType<Event> = EventType("keydown")

/** The curve between the two airports of [route], a `[airline, from, to]` row of the table. */
private fun routeCurve(flights: FlightsData, route: Array<Double>): Lines3DDataItem {
    val from = flights.airports[route[1].toInt()]
    val to = flights.airports[route[2].toInt()]
    return Lines3DDataItem(
        arrayOf(
            arrayOf(from.coordinate(3), from.coordinate(4)),
            arrayOf(to.coordinate(3), to.coordinate(4)),
        ),
    )
}

/**
 * The routes of [flights] as `lines3D` items: one curve between the coordinates of the two airports
 * of every route.
 */
private fun flightRoutes(flights: FlightsData): Array<Lines3DDataItem?> =
    Array(flights.routes.size) { index -> routeCurve(flights, flights.routes[index]) }

/**
 * The `lines3D` series of `lines3d-flights`, one per airline that has routes, and the names of those
 * airlines in the order the legend has to list them.
 *
 * Upstream groups the routes by airline *name* and drops the names without routes; the index of the
 * `airlines` table is used here instead, which gives the same grouping because the names are unique.
 * The series keep the airline-table order, while the legend names follow the order in which the
 * airlines first appear in the route table — upstream builds the legend from the keys of its
 * grouping object. The difference matters: with `selectedMode: "single"` the first legend entry is
 * the one initially shown, so the page opens on a different airline otherwise.
 */
private class AirlineSeries(val series: List<SeriesOption>, val names: List<String>)

/** Groups the routes of [flights] into the series of `lines3d-flights`. */
private fun airlineSeries(flights: FlightsData): AirlineSeries {
    val grouped = Array(flights.airlines.size) { mutableListOf<Lines3DDataItem?>() }
    val order = mutableListOf<Int>()
    for (route in flights.routes) {
        val airline = route[0].toInt()
        if (grouped[airline].isEmpty()) order.add(airline)
        grouped[airline].add(routeCurve(flights, route))
    }

    val series = mutableListOf<SeriesOption>()
    for (index in grouped.indices) {
        val routes = grouped[index]
        if (routes.isEmpty()) continue
        val airline = flights.airlines[index][0].unsafeCast<String>()
        series.add(
            lines3DSeries {
                name = airline
                effect =
                    unsafeJso {
                        show = true
                        trailWidth = 2.0
                        trailLength = 0.15
                        trailOpacity = 1.0
                        trailColor = GlColor(ZRColor("rgb(30, 30, 60)"))
                    }
                typedLineStyle =
                    unsafeJso {
                        width = 1.0
                        color = GlColor(ZRColor("rgb(50, 50, 150)"))
                        opacity = 0.1
                    }
                typedBlendMode = GlBlendModes.lighter
                typedData = routes.toTypedArray()
            },
        )
    }
    return AirlineSeries(series, order.map { flights.airlines[it][0].unsafeCast<String>() })
}

/**
 * The airports of every route of [flights], twice per route, the data of the `scatter3D` series
 * that pins the two ends of each curve.
 */
private fun airportPoints(flights: FlightsData): Array<Scatter3DDataItem?> =
    Array(flights.routes.size * 2) { index ->
        val route = flights.routes[index / 2]
        val airport = flights.airports[route[if (index % 2 == 0) 1 else 2].toInt()]
        Scatter3DDataItem(row(airport.coordinate(3), airport.coordinate(4)))
    }

/**
 * The option of `lines3d-flights`: one trail colour per airline over a dark bathymetry globe, and a
 * legend that shows a single airline at a time.
 */
private fun flightsByAirlineOption(airlines: AirlineSeries, flights: FlightsData): EChartsGlOption {
    val allSeries = airlines.series + scatter3DSeries {
        typedCoordinateSystem = CoordinateSystems3D.globe
        typedBlendMode = GlBlendModes.lighter
        symbolSize = SymbolSize(2.0)
        typedItemStyle =
            unsafeJso {
                color = GlColor(ZRColor("rgb(50, 50, 150)"))
                opacity = 0.2
            }
        typedData = airportPoints(flights)
    }
    return unsafeJso {
        legend =
            one(
                unsafeJso<LegendOption> {
                    selectedMode = SelectionModes.single
                    left = StringOrDouble("left")
                    orient = Orients.vertical
                    data = legendNames(*airlines.names.toTypedArray())
                    textStyle = unsafeJso { color = "#fff" }
                },
            )
        globe =
            one(
                unsafeJso<GlobeOption> {
                    environment = GlEnvironment("data/img/gl/starfield.jpg")
                    heightTexture = TextureSource("data/img/gl/bathymetry_bw_composite_4k.jpg")
                    displacementScale = 0.1
                    displacementQuality = QualityTypes.high
                    baseColor = ZRColor("#000")
                    shading = ShadingTypes.realistic
                    realisticMaterial =
                        unsafeJso<RealisticMaterialOption> {
                            roughness = NumberOrTexture(0.2)
                            metalness = NumberOrTexture(0.0)
                        }
                    postEffect =
                        unsafeJso<PostEffectOption> {
                            enable = true
                            depthOfField = unsafeJso { enable = false; focalDistance = 150.0 }
                        }
                    temporalSuperSampling = unsafeJso { enable = BooleanOrAuto(true) }
                    light =
                        unsafeJso<LightOption> {
                            ambient = unsafeJso { intensity = 0.0 }
                            main = unsafeJso { intensity = 0.1; shadow = false }
                            ambientCubemap =
                                unsafeJso<AmbientCubemapLightOption> {
                                    texture = "data/img/gl/lake.hdr"
                                    exposure = 1.0
                                    diffuseIntensity = 0.5
                                    specularIntensity = 2.0
                                }
                        }
                    viewControl = unsafeJso { autoRotate = false }
                    silent = true
                },
            )
        series = allSeries.toTypedArray()
    }
}

/** The option of `lines3d-airline-on-globe`: faint routes over a bathymetry globe. */
private fun airlineOnGlobeOption(routes: Array<Lines3DDataItem?>): EChartsGlOption =
    unsafeJso {
        backgroundColor = ZRColor("#000")
        globe =
            one(
                unsafeJso<GlobeOption> {
                    baseTexture = TextureSource("data/img/gl/world.topo.bathy.200401.jpg")
                    heightTexture = TextureSource("data/img/gl/bathymetry_bw_composite_4k.jpg")
                    shading = ShadingTypes.lambert
                    light =
                        unsafeJso<LightOption> {
                            ambient = unsafeJso { intensity = 0.4 }
                            main = unsafeJso { intensity = 0.4 }
                        }
                    viewControl = unsafeJso { autoRotate = false }
                },
            )
        series =
            one(
                lines3DSeries {
                    typedCoordinateSystem = CoordinateSystems3D.globe
                    typedBlendMode = GlBlendModes.lighter
                    typedLineStyle =
                        unsafeJso {
                            width = 1.0
                            color = GlColor(ZRColor("rgb(50, 50, 150)"))
                            opacity = 0.1
                        }
                    typedData = routes
                },
            )
    }

/**
 * The option of `lines3d-flights-on-geo3d`: a dark flat map with a glowing trail on every route.
 */
private fun flightsOnGeo3DOption(routes: Array<Lines3DDataItem?>): EChartsGlOption =
    unsafeJso {
        geo3D =
            one(
                unsafeJso<Geo3DOption> {
                    map = "world"
                    shading = ShadingTypes.color
                    environment = GlEnvironment("data/img/gl/starfield.jpg")
                    silent = true
                    groundPlane = unsafeJso<Geo3DGroundPlaneOption> { show = false }
                    light =
                        unsafeJso<LightOption> {
                            main = unsafeJso { intensity = 0.0 }
                            ambient = unsafeJso { intensity = 0.0 }
                        }
                    viewControl = unsafeJso { distance = 50.0 }
                    itemStyle = unsafeJso { color = GlColor(ZRColor("#111")) }
                    boxHeight = 0.5
                },
            )
        series =
            one(
                lines3DSeries {
                    typedCoordinateSystem = CoordinateSystems3D.geo3D
                    effect = unsafeJso { show = true; trailWidth = 2.0; trailLength = 0.2 }
                    typedBlendMode = GlBlendModes.lighter
                    typedLineStyle =
                        unsafeJso {
                            width = 0.0
                            color = GlColor(ZRColor("rgb(50, 50, 150)"))
                            opacity = 0.2
                        }
                    typedData = routes
                },
            )
    }

/**
 * The option of `lines3d-flights-gl`: the same routes seen from straight above, on a nearly flat
 * realistic map, with the trails running at a constant speed.
 */
private fun flightsGlOption(routes: Array<Lines3DDataItem?>): EChartsGlOption =
    unsafeJso {
        geo3D =
            one(
                unsafeJso<Geo3DOption> {
                    map = "world"
                    shading = ShadingTypes.realistic
                    silent = true
                    environment = GlEnvironment("#333")
                    realisticMaterial =
                        unsafeJso<RealisticMaterialOption> {
                            roughness = NumberOrTexture(0.8)
                            metalness = NumberOrTexture(0.0)
                        }
                    postEffect = unsafeJso<PostEffectOption> { enable = true }
                    groundPlane = unsafeJso<Geo3DGroundPlaneOption> { show = false }
                    light =
                        unsafeJso<LightOption> {
                            main = unsafeJso { intensity = 1.0; alpha = 30.0 }
                            ambient = unsafeJso { intensity = 0.0 }
                        }
                    viewControl =
                        unsafeJso {
                            distance = 70.0
                            alpha = 89.0
                            panMouseButton = MouseButtons.left
                            rotateMouseButton = MouseButtons.right
                        }
                    itemStyle = unsafeJso { color = GlColor(ZRColor("#000")) }
                    regionHeight = 0.5
                },
            )
        series =
            one(
                lines3DSeries {
                    typedCoordinateSystem = CoordinateSystems3D.geo3D
                    effect =
                        unsafeJso {
                            show = true
                            trailWidth = 1.0
                            trailOpacity = 0.5
                            trailLength = 0.2
                            constantSpeed = 5.0
                        }
                    typedBlendMode = GlBlendModes.lighter
                    typedLineStyle = unsafeJso { width = 0.2; opacity = 0.05 }
                    typedData = routes
                },
            )
    }

val glLinesEntries: List<CatalogEntry> =
    listOf(
        glLines.interactive(
            "lines3d-airline-on-globe",
            "航空线 - 地球",
            "Airline on Globe",
            "一万七千条航线贴着黑底地球，用加色混合叠成一层淡蓝色薄雾。",
            "Seventeen thousand flight routes cling to a dark globe, additive blending turning them " +
                "into a faint blue haze.",
        ) { chart ->
            loadJson("data/gl/flights.json").then { json ->
                chart.setOption(airlineOnGlobeOption(flightRoutes(json.unsafeCast<FlightsData>())))
            }
        },
        glLines.interactive(
            "lines3d-flights-on-geo3d",
            "航空线 - Geo3D",
            "Flights on Geo3D",
            "扁平的世界地图上，每道拖尾沿航线飞行；按任意键暂停或继续动画。",
            "On the flat map a trail runs along every route; any key press pauses or resumes the animation.",
        ) { chart, holder ->
            registerWorldMap().then {
                loadJson("data/gl/flights.json").then { json ->
                    chart.setOption(flightsOnGeo3DOption(flightRoutes(json.unsafeCast<FlightsData>())))
                }
            }
            val toggleEffect: (Event) -> Unit = {
                chart.dispatchAction(
                    unsafeJso<Payload> {
                        type = EChartsGlActions.Lines3DToggleEffect
                        seriesIndex = 0.0
                    },
                )
            }
            window.listen(KEYDOWN, toggleEffect)
            holder.onUnmount { window.removeEventListener(KEYDOWN, toggleEffect) }
        },
        glLines.interactive(
            "lines3d-flights",
            "航线",
            "Flights",
            "一万七千条航线按航空公司拆成十一条系列，图例一次只显示一家；每道拖尾沿航线飞行，按任意键暂停。",
            "Seventeen thousand routes split into one series per airline, the legend showing a single " +
                "airline at a time; a trail runs along every route and any key press pauses them.",
        ) { chart, holder ->
            loadJson("data/gl/flights.json").then { json ->
                val flights = json.unsafeCast<FlightsData>()
                val airlines = airlineSeries(flights)
                chart.setOption(flightsByAirlineOption(airlines, flights))
                val toggleEffect: (Event) -> Unit = {
                    airlines.series.indices.forEach { index ->
                        chart.dispatchAction(
                            unsafeJso<Payload> {
                                type = EChartsGlActions.Lines3DToggleEffect
                                seriesIndex = index.toDouble()
                            },
                        )
                    }
                }
                window.listen(KEYDOWN, toggleEffect)
                holder.onUnmount { window.removeEventListener(KEYDOWN, toggleEffect) }
            }
        },
        glLines.interactive(
            "lines3d-flights-gl",
            "航线 - 俯视",
            "Flights GL",
            "从正上方俯瞰几乎压平的黑色世界地图，所有航线同时飞行，拖尾以恒定速度前进。",
            "A nearly flat black world map seen from straight above, with every route flying at once " +
                "and the trails advancing at a constant speed.",
        ) { chart ->
            registerWorldMap().then {
                loadJson("data/gl/flights.json").then { json ->
                    chart.setOption(
                        flightsGlOption(flightRoutes(json.unsafeCast<FlightsData>())),
                    )
                }
            }
        },
    )
