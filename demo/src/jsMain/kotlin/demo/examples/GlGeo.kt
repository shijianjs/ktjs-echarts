package demo.examples

import demo.CatalogEntry
import demo.Groups
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
import echartsgl.GlobeBlendTargets
import echartsgl.GlobeLayerTypes
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

/*
 * The `geo3D` and `globe` pages of the `gl/` directory of apache/echarts-examples.
 *
 * Both coordinate systems are components of `echarts-gl` and have to be installed before the first
 * chart is created (`useEChartsGl` in `Gl.kt`). The two `geo3D` pages read the same `world.json` the
 * 2D geo pages register; the globe pages only need the textures of `data/img/gl/`.
 *
 * The `geo3d` page of the original puts a `baseTexture` on `lambertMaterial`, which no material of
 * `echarts-gl` 2.1.0 reads (`setMaterialFromModel` only looks at `detailTexture`), so the map is
 * white exactly like the official thumbnail. The `series: []` some globe pages declare is a no-op
 * and is left out.
 *
 * `scatter3d-globe-population` reads `population.json`; `globe-with-echarts-surface` paints a map
 * chart into an off-screen canvas and hands that chart to the globe as its base texture.
 */

private val glGeo = Category(Groups.GL, "examples/GlGeo.kt")

/** One row of `world-population.json`. */
private external interface WorldPopulationItem {
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
private val worldRegionNames: Map<String, String> =
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
private fun geo3DOption(): EChartsGlOption =
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
private fun populationRegions(population: JsAny): Array<Geo3DRegionOption> {
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
private fun geo3DHeightOption(population: JsAny): EChartsGlOption =
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
private fun populationSymbols(population: JsAny): Array<Scatter3DDataItem?> {
    val cells = population.unsafeCast<Array<Array<Double>>>()
    return Array(cells.size) { index ->
        val cell = cells[index]
        Scatter3DDataItem(row(cell[0], cell[1], sqrt(cell[2])))
    }
}

/** The option of `scatter3d-globe-population`: the globe under a cloud of population symbols. */
private fun globePopulationOption(population: JsAny): EChartsGlOption =
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
private fun countryPopulation(population: JsAny): Array<MapDataItem?> {
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
private var echartsSurface: TextureSource? = null

/**
 * The map chart whose canvas becomes the `baseTexture` of `globe-with-echarts-surface`.
 *
 * The chart is initialized on a detached 4096×2048 canvas and drawn once; use [echartsSurface] to
 * share it between visits.
 */
private fun echartsSurfaceTexture(population: JsAny): TextureSource {
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
private fun echartsSurfaceOption(baseTexture: TextureSource): EChartsGlOption =
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

val glGeoEntries: List<CatalogEntry> =
    listOf(
        glGeo.interactive(
            "geo3d",
            "Geo3D",
            "Geo3D",
            "世界地图被挤出成一块块立体大陆，放在会接住阴影的地面上，凹处由屏幕空间环境光遮蔽压暗。",
            "The world map extruded into solid continents on a shadowed ground plane, with screen-space " +
                "ambient occlusion darkening the folds.",
        ) { chart ->
            registerWorldMap().then { chart.setOption(geo3DOption()) }
        },
        glGeo.interactive(
            "geo3d-with-different-height",
            "Geo3D 不同高度",
            "Geo3D with Different Height",
            "每个国家的挤出高度按人口的对数缩放，木纹贴图随高度铺满侧面。",
            "Every country is extruded to a height proportional to the logarithm of its population, " +
                "with a wood texture tiling over the sides.",
        ) { chart ->
            registerWorldMap().then {
                loadJson("data/world-population.json").then { population ->
                    chart.setOption(geo3DHeightOption(population))
                }
            }
        },
        glGeo.chart(
            "globe-echarts-gl-hello-world",
            "ECharts GL 入门",
            "ECharts-GL Hello World",
            "最简地球：地形贴图同时作为高度图，顶点沿法线移位，星空当背景。",
            "The simplest globe: one terrain texture doubles as the height map, the vertices are " +
                "displaced along their normals, and a starfield is the background.",
        ) {
            unsafeJso<EChartsGlOption> {
                backgroundColor = ZRColor("#000")
                globe =
                    one(
                        unsafeJso<GlobeOption> {
                            baseTexture = TextureSource("data/img/gl/world.topo.bathy.200401.jpg")
                            heightTexture = TextureSource("data/img/gl/world.topo.bathy.200401.jpg")
                            displacementScale = 0.04
                            shading = ShadingTypes.realistic
                            environment = GlEnvironment("data/img/gl/starfield.jpg")
                            realisticMaterial = unsafeJso<RealisticMaterialOption> { roughness = NumberOrTexture(0.9) }
                            postEffect = unsafeJso<PostEffectOption> { enable = true }
                            light =
                                unsafeJso<LightOption> {
                                    main = unsafeJso { intensity = 5.0; shadow = true }
                                    ambientCubemap =
                                        unsafeJso<AmbientCubemapLightOption> {
                                            texture = "data/img/gl/pisa.hdr"
                                            diffuseIntensity = 0.2
                                        }
                                }
                        },
                    )
            }
        },
        glGeo.chart(
            "globe-atmosphere",
            "大气层显示",
            "Globe with Atmosphere",
            "地球边缘加一圈渐变辉光，模拟大气散射。",
            "A fading glow around the edge of the earth stands for the scattering of the atmosphere.",
        ) {
            unsafeJso<EChartsGlOption> {
                backgroundColor = ZRColor("#000")
                globe =
                    one(
                        unsafeJso<GlobeOption> {
                            baseTexture = TextureSource("data/img/gl/earth.jpg")
                            shading = ShadingTypes.lambert
                            environment = GlEnvironment("data/img/gl/starfield.jpg")
                            atmosphere = unsafeJso { show = true }
                            light =
                                unsafeJso<LightOption> {
                                    ambient = unsafeJso { intensity = 0.1 }
                                    main = unsafeJso { intensity = 1.5 }
                                }
                        },
                    )
            }
        },
        glGeo.chart(
            "globe-displacement",
            "地形位移",
            "Globe Displacement",
            "用海陆高度图沿法线推移顶点，打开线框以便看清多边形的疏密。",
            "The bathymetry map pushes the vertices along their normals; the wireframe overlay makes " +
                "the density of the polygons visible.",
        ) {
            unsafeJso<EChartsGlOption> {
                globe =
                    one(
                        unsafeJso<GlobeOption> {
                            displacementTexture = TextureSource("data/img/gl/bathymetry_bw_composite_4k.jpg")
                            displacementScale = 0.1
                            displacementQuality = QualityTypes.ultra
                            shading = ShadingTypes.realistic
                            realisticMaterial =
                                unsafeJso<RealisticMaterialOption> {
                                    roughness = NumberOrTexture(0.8)
                                    metalness = NumberOrTexture(0.0)
                                }
                            postEffect =
                                unsafeJso<PostEffectOption> {
                                    enable = true
                                    ssao =
                                        unsafeJso {
                                            enable = true
                                            radius = 2.0
                                            intensity = 1.5
                                            quality = QualityTypes.high
                                        }
                                }
                            temporalSuperSampling = unsafeJso { enable = BooleanOrAuto(true) }
                            light =
                                unsafeJso<LightOption> {
                                    ambient = unsafeJso { intensity = 0.0 }
                                    main = unsafeJso { intensity = 1.0; shadow = true }
                                    ambientCubemap =
                                        unsafeJso<AmbientCubemapLightOption> {
                                            texture = "data/img/gl/pisa.hdr"
                                            exposure = 1.0
                                            diffuseIntensity = 0.2
                                        }
                                }
                            viewControl = unsafeJso { autoRotate = false }
                            debug = unsafeJso { wireframe = unsafeJso { show = true } }
                        },
                    )
            }
        },
        glGeo.chart(
            "globe-moon",
            "月球",
            "Moon",
            "同一套位移与凹凸贴图换成月面数据，环境光几乎关掉，只留一束带阴影的直射光。",
            "The same displacement and bump pipeline with lunar maps, the environment light almost " +
                "off and a single shadow-casting sun.",
        ) {
            unsafeJso<EChartsGlOption> {
                globe =
                    one(
                        unsafeJso<GlobeOption> {
                            baseTexture = TextureSource("data/img/gl/moon-base.jpg")
                            heightTexture = TextureSource("data/img/gl/moon-bump.jpg")
                            displacementScale = 0.05
                            displacementQuality = QualityTypes.medium
                            environment = GlEnvironment("data/img/gl/starfield.jpg")
                            shading = ShadingTypes.realistic
                            realisticMaterial =
                                unsafeJso<RealisticMaterialOption> {
                                    roughness = NumberOrTexture(0.8)
                                    metalness = NumberOrTexture(0.0)
                                }
                            postEffect =
                                unsafeJso<PostEffectOption> {
                                    enable = true
                                    ssao =
                                        unsafeJso {
                                            enable = true
                                            radius = 2.0
                                            intensity = 1.0
                                            quality = QualityTypes.high
                                        }
                                }
                            temporalSuperSampling = unsafeJso { enable = BooleanOrAuto(true) }
                            light =
                                unsafeJso<LightOption> {
                                    ambient = unsafeJso { intensity = 0.0 }
                                    main = unsafeJso { intensity = 2.0; shadow = true }
                                    ambientCubemap =
                                        unsafeJso<AmbientCubemapLightOption> {
                                            texture = "data/img/gl/pisa.hdr"
                                            exposure = 0.0
                                            diffuseIntensity = 0.02
                                        }
                                }
                            viewControl = unsafeJso { autoRotate = false }
                        },
                    )
            }
        },
        glGeo.chart(
            "globe-layers",
            "地球图层",
            "Globe Layers",
            "两层贴图叠在球面上：夜灯图混入自发光通道，云图浮在地表之上。",
            "Two extra textures: the night lights blend into the emission channel and the clouds " +
                "float above the surface.",
        ) {
            unsafeJso<EChartsGlOption> {
                backgroundColor = ZRColor("#000")
                globe =
                    one(
                        unsafeJso<GlobeOption> {
                            baseTexture = TextureSource("data/img/gl/earth.jpg")
                            heightTexture = TextureSource("data/img/gl/bathymetry_bw_composite_4k.jpg")
                            displacementScale = 0.1
                            shading = ShadingTypes.lambert
                            environment = GlEnvironment("data/img/gl/starfield.jpg")
                            light =
                                unsafeJso<LightOption> {
                                    ambient = unsafeJso { intensity = 0.1 }
                                    main = unsafeJso { intensity = 1.5 }
                                }
                            layers =
                                arrayOf(
                                    unsafeJso<GlobeLayerOption> {
                                        type = GlobeLayerTypes.blend
                                        blendTo = GlobeBlendTargets.emission
                                        texture = TextureSource("data/img/gl/night.jpg")
                                    },
                                    unsafeJso<GlobeLayerOption> {
                                        type = GlobeLayerTypes.overlay
                                        texture = TextureSource("data/img/gl/clouds.png")
                                        shading = ShadingTypes.lambert
                                        distance = 5.0
                                    },
                                )
                        },
                    )
            }
        },
        glGeo.interactive(
            "scatter3d-globe-population",
            "三维散点图 - 全球人口分布",
            "Scatter3D - Global Population",
            "三万多个人口格点在地球外层用加色混合叠加，点的大小按人口平方根映射，景深让远处的小点化开。",
            "Thirty-odd thousand population cells float above the globe with additive blending; their " +
                "size follows the square root of the population, and the depth of field blurs the far ones.",
        ) { chart ->
            loadJson("data/population.json").then { population ->
                chart.setOption(globePopulationOption(population))
            }
        },
        glGeo.interactive(
            "globe-with-echarts-surface",
            "ECharts 地图作为地球贴图",
            "Globe with ECharts Surface",
            "先把世界地图画进一张 4096×2048 的离屏图表，国家按人口上色，再把这整张画布当作球面贴图。",
            "A world map chart is painted onto an off-screen 4096×2048 canvas with the countries colored " +
                "by population, and that canvas becomes the surface texture of the globe.",
        ) { chart ->
            registerWorldMap().then {
                loadJson("data/world-population.json").then { population ->
                    val texture = echartsSurface ?: echartsSurfaceTexture(population).also { echartsSurface = it }
                    chart.setOption(echartsSurfaceOption(texture))
                }
            }
        },
    )
