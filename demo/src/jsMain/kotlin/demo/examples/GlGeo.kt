package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.ECharts
import echarts.GeoJSONMapInput
import echarts.GeoJSONSourceInput
import echarts.MapInput
import echarts.options.DoubleOrDoubleArray
import echarts.options.one
import echartsgl.BooleanOrAuto
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
 */

private val glGeo = Category(Groups.GL, "examples/GlGeo.kt")

/** One row of `world-population.json`. */
private external interface WorldPopulationItem {
    val name: String
    val value: Double
}

/**
 * Registers `data/world.json` as the `world` map; resolves to the loaded GeoJSON.
 */
private fun registerWorldMap(): Promise<JsAny> =
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
            name = item.name
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
    )
