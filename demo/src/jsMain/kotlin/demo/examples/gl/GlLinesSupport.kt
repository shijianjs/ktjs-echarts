package demo.examples.gl

import demo.Groups
import io.github.shijianjs.ktjs.echarts.Orients
import io.github.shijianjs.ktjs.echarts.options.LegendOption
import io.github.shijianjs.ktjs.echarts.options.SelectionModes
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.SymbolSize
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.gl.BooleanOrAuto
import io.github.shijianjs.ktjs.echarts.gl.CoordinateSystems3D
import io.github.shijianjs.ktjs.echarts.gl.GlBlendModes
import io.github.shijianjs.ktjs.echarts.gl.GlColor
import io.github.shijianjs.ktjs.echarts.gl.GlEnvironment
import io.github.shijianjs.ktjs.echarts.gl.MouseButtons
import io.github.shijianjs.ktjs.echarts.gl.NumberOrTexture
import io.github.shijianjs.ktjs.echarts.gl.QualityTypes
import io.github.shijianjs.ktjs.echarts.gl.ShadingTypes
import io.github.shijianjs.ktjs.echarts.gl.TextureSource
import io.github.shijianjs.ktjs.echarts.gl.options.AmbientCubemapLightOption
import io.github.shijianjs.ktjs.echarts.gl.options.EChartsGlOption
import io.github.shijianjs.ktjs.echarts.gl.options.Geo3DOption
import io.github.shijianjs.ktjs.echarts.gl.options.Geo3DGroundPlaneOption
import io.github.shijianjs.ktjs.echarts.gl.options.GlobeOption
import io.github.shijianjs.ktjs.echarts.gl.options.LightOption
import io.github.shijianjs.ktjs.echarts.gl.options.Lines3DDataItem
import io.github.shijianjs.ktjs.echarts.gl.options.PostEffectOption
import io.github.shijianjs.ktjs.echarts.gl.options.RealisticMaterialOption
import io.github.shijianjs.ktjs.echarts.gl.options.Scatter3DDataItem
import io.github.shijianjs.ktjs.echarts.gl.options.lines3DSeries
import io.github.shijianjs.ktjs.echarts.gl.options.scatter3DSeries
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import web.events.Event
import web.events.EventType
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.lines.FlightsData
import demo.examples.lines.coordinate
import demo.examples.matrix.names
import demo.examples.shared.every
import demo.examples.shared.legendNames
import demo.examples.shared.row

/** `keydown` as a typed event. */
internal val KEYDOWN: EventType<Event> = EventType("keydown")

/** The curve between the two airports of [route], a `[airline, from, to]` row of the table. */
internal fun routeCurve(flights: FlightsData, route: Array<Double>): Lines3DDataItem {
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
internal fun flightRoutes(flights: FlightsData): Array<Lines3DDataItem?> =
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
internal class AirlineSeries(val series: List<SeriesOption>, val names: List<String>)

/** Groups the routes of [flights] into the series of `lines3d-flights`. */
internal fun airlineSeries(flights: FlightsData): AirlineSeries {
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
internal fun airportPoints(flights: FlightsData): Array<Scatter3DDataItem?> =
    Array(flights.routes.size * 2) { index ->
        val route = flights.routes[index / 2]
        val airport = flights.airports[route[if (index % 2 == 0) 1 else 2].toInt()]
        Scatter3DDataItem(row(airport.coordinate(3), airport.coordinate(4)))
    }

/**
 * The option of `lines3d-flights`: one trail colour per airline over a dark bathymetry globe, and a
 * legend that shows a single airline at a time.
 */
internal fun flightsByAirlineOption(airlines: AirlineSeries, flights: FlightsData): EChartsGlOption {
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
internal fun airlineOnGlobeOption(routes: Array<Lines3DDataItem?>): EChartsGlOption =
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
internal fun flightsOnGeo3DOption(routes: Array<Lines3DDataItem?>): EChartsGlOption =
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
internal fun flightsGlOption(routes: Array<Lines3DDataItem?>): EChartsGlOption =
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
