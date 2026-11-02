package demo.examples.gl

import demo.ui.loadJson
import echarts.ECharts
import echarts.EChartsInitOpts
import echarts.GeoJSONMapInput
import echarts.GeoJSONSourceInput
import echarts.MapInput
import echarts.options.DoubleOrDoubleArray
import echarts.options.EChartsOption
import echarts.options.MapDataItem
import echarts.options.MapDataItemOption
import echarts.options.OptionDataValue
import echarts.options.OptionId
import echarts.options.StringOrDouble
import echarts.options.StringOrStringArray
import echarts.options.SymbolSize
import echarts.options.VisualMapOption
import echarts.options.VisualOptionUnit
import echarts.options.mapSeries
import echarts.options.one
import echartsgl.BooleanOrAuto
import echartsgl.CoordinateSystems3D
import echartsgl.GlBlendModes
import echartsgl.GlColor
import echartsgl.GlEnvironment
import echartsgl.NumberOrTexture
import echartsgl.QualityTypes
import echartsgl.ShadingTypes
import echartsgl.TextureSource
import echartsgl.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.Promise
import kotlin.js.unsafeCast
import kotlin.math.ln
import kotlin.math.sqrt
import web.dom.document
import web.html.HtmlTagName
import zrender.ZRColor
import demo.examples.geo.region
import demo.examples.matrix.cell
import demo.examples.matrix.names
import demo.examples.shared.every
import demo.examples.shared.fixed
import demo.examples.shared.row
import demo.examples.sunburst.radius

/** One row of `world-population.json`. */
internal external interface WorldPopulationItem {
    val name: String
    val value: Double
}

/**
 * `data/world.json` abbreviates a few region names (`Czech Rep.`, `Korea`, `Dem. Rep. Congo`, ...)
 * while `world-population.json`, and the world map the upstream gallery registers, spell them out.
 * The table translates the names of the data to the names of the map this site registers; the five
 * names with no counterpart in `world.json` (`Kosovo`, `Somaliland`, `West Bank`, `French Guiana`,
 * `French Southern and Antarctic Lands`) match no region and are simply not drawn.
 */
internal val worldRegionNames: Map<String, String> =
    mapOf(
        "The Bahamas" to "Bahamas",
        "Bosnia and Herzegovina" to "Bosnia and Herz.",
        "Central African Republic" to "Central African Rep.",
        "Ivory Coast" to "Côte d'Ivoire",
        "Democratic Republic of the Congo" to "Dem. Rep. Congo",
        "Republic of the Congo" to "Congo",
        "Northern Cyprus" to "N. Cyprus",
        "Czech Republic" to "Czech Rep.",
        "Dominican Republic" to "Dominican Rep.",
        "Falkland Islands" to "Falkland Is.",
        "Guinea Bissau" to "Guinea-Bissau",
        "Equatorial Guinea" to "Eq. Guinea",
        "South Korea" to "Korea",
        "Laos" to "Lao PDR",
        "North Korea" to "Dem. Rep. Korea",
        "Western Sahara" to "W. Sahara",
        "South Sudan" to "S. Sudan",
        "Solomon Islands" to "Solomon Is.",
        "Republic of Serbia" to "Serbia",
        "East Timor" to "Timor-Leste",
        "United Republic of Tanzania" to "Tanzania",
    )

/** Name of the region of `world.json` that [name] of `world-population.json` refers to. */
internal fun regionName(name: String): String = worldRegionNames[name] ?: name

/**
 * Registers `data/world.json` as the `world` map; resolves to the loaded GeoJSON.
 */
internal fun registerWorldMap(): Promise<JsAny> =
    loadJson("data/world.json").then { world ->
        ECharts.registerMap(
            "world",
            MapInput(unsafeJso<GeoJSONMapInput> { geoJSON = world.unsafeCast<GeoJSONSourceInput>() }),
        )
        world
    }

/** The `geo3D` block of `geo3d`: a white extruded world on a shadowed ground plane. */
internal fun geo3DOption(): EChartsGlOption =
    unsafeJso {
        geo3D =
            one(
                unsafeJso<Geo3DOption> {
                    map = "world"
                    boxHeight = 1.0
                    itemStyle = unsafeJso { borderColor = GlColor(ZRColor("#000")); borderWidth = 0.5 }
                    groundPlane = unsafeJso { show = true }
                    postEffect = unsafeJso<PostEffectOption> { enable = true; ssao = unsafeJso { enable = true; radius = 1.0 } }
                    light =
                        unsafeJso<LightOption> {
                            main = unsafeJso { intensity = 1.0; shadow = true }
                            ambientCubemap = unsafeJso<AmbientCubemapLightOption> { texture = "data/img/gl/canyon.hdr" }
                        }
                    viewControl = unsafeJso { distance = 50.0 }
                },
            )
    }

/**
 * The `regions` of `geo3d-with-different-height`: the height of a region grows with the logarithm
 * of its population, scaled into `0..3`.
 */
internal fun populationRegions(population: JsAny): Array<Geo3DRegionOption> {
    val items = population.unsafeCast<Array<WorldPopulationItem>>()
    var max = Double.NEGATIVE_INFINITY
    var min = Double.POSITIVE_INFINITY
    for (item in items) {
        val value = ln(item.value)
        if (value > max) max = value
        if (value < min) min = value
    }
    return Array(items.size) { index ->
        val item = items[index]
        unsafeJso<Geo3DRegionOption> {
            name = regionName(item.name)
            height = (ln(item.value) - min) / (max - min) * 3
        }
    }
}

/** The `geo3D` block of `geo3d-with-different-height`. */
internal fun geo3DHeightOption(population: JsAny): EChartsGlOption =
    unsafeJso {
        backgroundColor = ZRColor("#cdcfd5")
        geo3D =
            one(
                unsafeJso<Geo3DOption> {
                    map = "world"
                    shading = ShadingTypes.lambert
                    lambertMaterial =
                        unsafeJso<LambertMaterialOption> {
                            detailTexture = TextureSource("data/img/gl/woods.jpg")
                            textureTiling = DoubleOrDoubleArray(20.0)
                        }
                    groundPlane = unsafeJso { show = true }
                    postEffect =
                        unsafeJso<PostEffectOption> {
                            enable = true
                            ssao =
                                unsafeJso {
                                    enable = true
                                    radius = 3.0
                                    quality = QualityTypes.high
                                }
                        }
                    light =
                        unsafeJso<LightOption> {
                            main = unsafeJso { intensity = 1.0; shadow = true; shadowQuality = QualityTypes.high; alpha = 30.0 }
                            ambient = unsafeJso { intensity = 0.0 }
                            ambientCubemap =
                                unsafeJso<AmbientCubemapLightOption> {
                                    texture = "data/img/gl/canyon.hdr"
                                    exposure = 2.0
                                    diffuseIntensity = 0.3
                                }
                        }
                    viewControl = unsafeJso { distance = 50.0 }
                    regionHeight = 0.5
                    regions = populationRegions(population)
                },
            )
    }

/**
 * The `data` of `scatter3d-globe-population`: one symbol per row of `population.json`, placed at
 * `[lng, lat]` and carrying the square root of the cell population as its third value.
 *
 * Upstream drops the cells with a population of zero first; every row of the file is positive, so
 * the filter does not change the data and is left out.
 */
internal fun populationSymbols(population: JsAny): Array<Scatter3DDataItem?> {
    val cells = population.unsafeCast<Array<Array<Double>>>()
    return Array(cells.size) { index ->
        val cell = cells[index]
        Scatter3DDataItem(row(cell[0], cell[1], sqrt(cell[2])))
    }
}

/** The option of `scatter3d-globe-population`: the globe under a cloud of population symbols. */
internal fun globePopulationOption(population: JsAny): EChartsGlOption =
    unsafeJso {
        visualMap =
            one(
                unsafeJso<VisualMapOption> {
                    show = false
                    min = 0.0
                    max = 60.0
                    inRange = unsafeJso<VisualOptionUnit> { symbolSize = DoubleOrDoubleArray(arrayOf(1.0, 10.0)) }
                },
            )
        globe =
            one(
                unsafeJso<GlobeOption> {
                    environment = GlEnvironment("data/img/gl/starfield.jpg")
                    heightTexture = TextureSource("data/img/gl/bathymetry_bw_composite_4k.jpg")
                    displacementScale = 0.05
                    displacementQuality = QualityTypes.high
                    globeOuterRadius = 100.0
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
                            depthOfField =
                                unsafeJso<DepthOfFieldOption> {
                                    enable = true
                                    focalRange = 15.0
                                    focalDistance = 100.0
                                }
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
                    viewControl =
                        unsafeJso {
                            autoRotate = false
                            beta = 180.0
                            alpha = 20.0
                            distance = 100.0
                        }
                },
            )
        series =
            one(
                scatter3DSeries {
                    typedCoordinateSystem = CoordinateSystems3D.globe
                    typedBlendMode = GlBlendModes.lighter
                    symbolSize = SymbolSize(2.0)
                    typedItemStyle =
                        unsafeJso<GlItemStyleOption> {
                            color = GlColor(ZRColor("rgb(50, 50, 150)"))
                            opacity = 1.0
                        }
                    typedData = populationSymbols(population)
                },
            )
    }

/** The `data` of the map of `globe-with-echarts-surface`. */
internal fun countryPopulation(population: JsAny): Array<MapDataItem?> {
    val items = population.unsafeCast<Array<WorldPopulationItem>>()
    return Array(items.size) { index ->
        val item = items[index]
        MapDataItem(
            unsafeJso<MapDataItemOption> {
                name = OptionId(regionName(item.name))
                value = OptionDataValue(item.value)
            },
        )
    }
}

/**
 * The map chart of `globe-with-echarts-surface`, built once per session.
 *
 * `echarts-gl` wraps an ECharts instance in a texture but the instance itself stays registered in
 * ECharts, so a fresh 4096×2048 chart per visit would only pile up canvases; the map draws a fixed
 * file on our own white background, so one instance serves every visit.
 */
internal var echartsSurface: TextureSource? = null

/**
 * The map chart whose canvas becomes the `baseTexture` of `globe-with-echarts-surface`.
 *
 * The chart is initialized on a detached 4096×2048 canvas and drawn once; use [echartsSurface] to
 * share it between visits.
 */
internal fun echartsSurfaceTexture(population: JsAny): TextureSource {
    val canvas = document.createElement(HtmlTagName.canvas)
    val mapChart =
        ECharts.init(
            canvas,
            null,
            unsafeJso<EChartsInitOpts> {
                width = StringOrDouble(4096.0)
                height = StringOrDouble(2048.0)
            },
        )
    mapChart.setOption(
        unsafeJso<EChartsOption> {
            backgroundColor = ZRColor("#fff")
            visualMap =
                one(
                    unsafeJso<VisualMapOption> {
                        show = false
                        min = 0.0
                        max = 500000.0
                        inRange = unsafeJso<VisualOptionUnit> { color = StringOrStringArray(glPalette) }
                    },
                )
            series =
                one(
                    mapSeries {
                        map = "world"
                        top = StringOrDouble(0.0)
                        left = StringOrDouble(0.0)
                        right = StringOrDouble(0.0)
                        bottom = StringOrDouble(0.0)
                        boundingCoords = arrayOf(arrayOf(-180.0, 90.0), arrayOf(180.0, -90.0))
                        typedData = countryPopulation(population)
                    },
                )
        },
    )
    return TextureSource(mapChart)
}

/** The option of `globe-with-echarts-surface`: the map chart wrapped onto a sphere. */
internal fun echartsSurfaceOption(baseTexture: TextureSource): EChartsGlOption =
    unsafeJso {
        globe =
            one(
                unsafeJso<GlobeOption> {
                    this.baseTexture = baseTexture
                    heightTexture = TextureSource("data/img/gl/bathymetry_bw_composite_4k.jpg")
                    displacementScale = 0.1
                    shading = ShadingTypes.lambert
                    light =
                        unsafeJso<LightOption> {
                            ambient = unsafeJso { intensity = 0.5 }
                            main = unsafeJso { intensity = 0.6 }
                        }
                    viewControl = unsafeJso { autoRotate = false }
                },
            )
    }
