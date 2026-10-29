package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.listen
import demo.ui.loadJson
import echarts.Payload
import echarts.options.one
import echartsgl.CoordinateSystems3D
import echartsgl.EChartsGlActions
import echartsgl.GlBlendModes
import echartsgl.GlColor
import echartsgl.GlEnvironment
import echartsgl.ShadingTypes
import echartsgl.TextureSource
import echartsgl.options.EChartsGlOption
import echartsgl.options.Geo3DOption
import echartsgl.options.Geo3DGroundPlaneOption
import echartsgl.options.GlobeOption
import echartsgl.options.LightOption
import echartsgl.options.Lines3DDataItem
import echartsgl.options.lines3DSeries
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

/**
 * The routes of [flights] as `lines3D` items: one curve between the coordinates of the two airports
 * of every route.
 */
private fun flightRoutes(flights: FlightsData): Array<Lines3DDataItem?> =
    Array(flights.routes.size) { index ->
        val route = flights.routes[index]
        val from = flights.airports[route[1].toInt()]
        val to = flights.airports[route[2].toInt()]
        Lines3DDataItem(
            arrayOf(
                arrayOf(from.coordinate(3), from.coordinate(4)),
                arrayOf(to.coordinate(3), to.coordinate(4)),
            ),
        )
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
    )
