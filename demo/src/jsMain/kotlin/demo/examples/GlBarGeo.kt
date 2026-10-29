package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.options.DoubleOrDoubleArray
import echarts.options.StringOrStringArray
import echarts.options.VisualMapControllerOption
import echarts.options.VisualMapOption
import echarts.options.VisualOptionUnit
import echarts.options.one
import echartsgl.BooleanOrAuto
import echartsgl.CoordinateSystems3D
import echartsgl.GlColor
import echartsgl.GlEnvironment
import echartsgl.MouseButtons
import echartsgl.QualityTypes
import echartsgl.ShadingTypes
import echartsgl.TextureSource
import echartsgl.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import kotlin.math.sqrt
import zrender.ZRColor

/*
 * The two population pages that put `bar3D` on a geographic coordinate system.
 *
 * Both read `population.json` (the table `scatter3d-globe-population` scatters as symbols) and draw
 * one bar per populated cell: `[lng, lat, sqrt(population)]`. The `geo3D` page needs the world map
 * registered, the `globe` page only needs its textures.
 *
 * Both pages declare an orange `itemStyle.color` on the series, but only the globe page keeps it:
 * there the `visualMap` maps the value to `colorLightness` alone. On the `geo3D` page the visual
 * style is written to `itemStyle.color` (`visualStyleAccessPath`), so the bars take the 11-stop
 * palette instead of the orange, exactly like the official page.
 */

private val glBarGeo = Category(Groups.GL, "examples/GlBarGeo.kt")

/**
 * The `bar3D` items of a `population.json` table: one bar per populated cell.
 *
 * Upstream drops the cells with a population of zero first; every row of the file is positive, so
 * the filter does not change the data and is left out. The third value is the square root of the
 * cell population, which keeps the densest cells from dwarfing the rest of the map.
 */
private fun populationBars(population: JsAny): Array<Bar3DDataItem?> {
    val cells = population.unsafeCast<Array<Array<Double>>>()
    return Array(cells.size) { index ->
        val cell = cells[index]
        Bar3DDataItem(doubleRow(cell[0], cell[1], sqrt(cell[2])))
    }
}

/** The option of `bar3d-global-population`: orange bars on a lambert-shaded world. */
private fun bar3DPopulationOption(population: JsAny): EChartsGlOption =
    unsafeJso {
        backgroundColor = ZRColor("#cdcfd5")
        geo3D =
            one(
                unsafeJso<Geo3DOption> {
                    map = "world"
                    shading = ShadingTypes.lambert
                    regionHeight = 2.0
                    light =
                        unsafeJso<LightOption> {
                            main =
                                unsafeJso {
                                    intensity = 5.0
                                    shadow = true
                                    shadowQuality = QualityTypes.high
                                    alpha = 30.0
                                }
                            ambient = unsafeJso { intensity = 0.0 }
                            ambientCubemap =
                                unsafeJso<AmbientCubemapLightOption> {
                                    texture = "data/img/gl/canyon.hdr"
                                    exposure = 1.0
                                    diffuseIntensity = 0.5
                                }
                        }
                    viewControl =
                        unsafeJso {
                            distance = 50.0
                            panMouseButton = MouseButtons.left
                            rotateMouseButton = MouseButtons.right
                        }
                    groundPlane =
                        unsafeJso<Geo3DGroundPlaneOption> {
                            show = true
                            color = GlColor(ZRColor("#999"))
                        }
                    postEffect =
                        unsafeJso<PostEffectOption> {
                            enable = true
                            bloom = unsafeJso { enable = false }
                            ssao =
                                unsafeJso {
                                    enable = true
                                    radius = 1.0
                                    intensity = 1.0
                                }
                            depthOfField =
                                unsafeJso {
                                    enable = false
                                    focalRange = 10.0
                                    blurRadius = 10.0
                                    fstop = 1.0
                                }
                        }
                    temporalSuperSampling = unsafeJso { enable = BooleanOrAuto(true) }
                },
            )
        visualMap =
            one(
                unsafeJso<VisualMapOption> {
                    max = 40.0
                    calculable = true
                    realtime = false
                    inRange = unsafeJso<VisualOptionUnit> { color = StringOrStringArray(glPalette) }
                    outOfRange = unsafeJso { colorAlpha = DoubleOrDoubleArray(0.0) }
                },
            )
        series =
            one(
                bar3DSeries {
                    typedCoordinateSystem = CoordinateSystems3D.geo3D
                    shading = ShadingTypes.lambert
                    barSize = DoubleOrDoubleArray(0.1)
                    minHeight = 0.2
                    silent = true
                    typedItemStyle = unsafeJso { color = GlColor(ZRColor("orange")) }
                    typedData = populationBars(population)
                },
            )
    }

/**
 * The option of `global-population-bar3d-on-globe`: the same bars standing on a globe.
 *
 * The `visualMap` maps the value to `colorLightness` only, which varies the brightness of the orange
 * the series declares instead of replacing it with the palette; its controller is orange as well,
 * and the out-of-range bars are hidden.
 */
private fun bar3DPopulationOnGlobeOption(population: JsAny): EChartsGlOption =
    unsafeJso {
        backgroundColor = ZRColor("#000")
        globe =
            one(
                unsafeJso<GlobeOption> {
                    baseTexture = TextureSource("data/img/gl/world.topo.bathy.200401.jpg")
                    heightTexture = TextureSource("data/img/gl/world.topo.bathy.200401.jpg")
                    shading = ShadingTypes.lambert
                    environment = GlEnvironment("data/img/gl/starfield.jpg")
                    light = unsafeJso<LightOption> { main = unsafeJso { intensity = 2.0 } }
                    viewControl = unsafeJso { autoRotate = false }
                },
            )
        visualMap =
            one(
                unsafeJso<VisualMapOption> {
                    max = 40.0
                    calculable = true
                    realtime = false
                    inRange =
                        unsafeJso<VisualOptionUnit> {
                            colorLightness = DoubleOrDoubleArray(arrayOf(0.2, 0.9))
                        }
                    textStyle = unsafeJso { color = "#fff" }
                    controller =
                        unsafeJso<VisualMapControllerOption> {
                            inRange = unsafeJso<VisualOptionUnit> { color = StringOrStringArray("orange") }
                        }
                    outOfRange = unsafeJso { colorAlpha = DoubleOrDoubleArray(0.0) }
                },
            )
        series =
            one(
                bar3DSeries {
                    typedCoordinateSystem = CoordinateSystems3D.globe
                    barSize = DoubleOrDoubleArray(0.6)
                    minHeight = 0.2
                    silent = true
                    typedItemStyle = unsafeJso { color = GlColor(ZRColor("orange")) }
                    typedData = populationBars(population)
                },
            )
    }

val glBarGeoEntries: List<CatalogEntry> =
    listOf(
        glBarGeo.interactive(
            "bar3d-global-population",
            "Bar3D - 全球人口分布",
            "Bar3D - Global Population",
            "把全球五公里见方的人口格点画成橙色细柱，立在接受阴影的世界地图上，柱高取人口的平方根。",
            "Every five-kilometre population cell becomes a thin orange bar standing on a shadowed " +
                "world map, its height taken from the square root of the population.",
        ) { chart ->
            registerWorldMap().then {
                loadJson("data/population.json").then { population ->
                    chart.setOption(bar3DPopulationOption(population))
                }
            }
        },
        glBarGeo.interactive(
            "global-population-bar3d-on-globe",
            "全球人口分布 - 地球上的 Bar3D",
            "Global Population - Bar3D on Globe",
            "同一份人口数据立在会自转停下的地球上，柱子在星空下沿球面铺开。",
            "The same population data stands on a globe under a starfield, the bars fanning out " +
                "along the surface of the sphere.",
        ) { chart ->
            loadJson("data/population.json").then { population ->
                chart.setOption(bar3DPopulationOnGlobeOption(population))
            }
        },
    )
