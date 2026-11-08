package demo.examples.gl

import io.github.shijianjs.ktjs.echarts.ECharts
import io.github.shijianjs.ktjs.echarts.GeoJSONMapInput
import io.github.shijianjs.ktjs.echarts.GeoJSONSourceInput
import io.github.shijianjs.ktjs.echarts.MapInput
import io.github.shijianjs.ktjs.echarts.options.DoubleOrDoubleArray
import io.github.shijianjs.ktjs.echarts.options.OptionDataValue
import io.github.shijianjs.ktjs.echarts.options.OptionDataValueOrArray
import io.github.shijianjs.ktjs.echarts.options.OptionId
import io.github.shijianjs.ktjs.echarts.options.StringOrStringArray
import io.github.shijianjs.ktjs.echarts.options.VisualMapOption
import io.github.shijianjs.ktjs.echarts.options.VisualOptionUnit
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.gl.GlEnvironment
import io.github.shijianjs.ktjs.echarts.gl.GlColor
import io.github.shijianjs.ktjs.echarts.gl.MouseButtons
import io.github.shijianjs.ktjs.echarts.gl.NumberOrTexture
import io.github.shijianjs.ktjs.echarts.gl.QualityTypes
import io.github.shijianjs.ktjs.echarts.gl.ShadingTypes
import io.github.shijianjs.ktjs.echarts.gl.TextureSource
import io.github.shijianjs.ktjs.echarts.gl.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import kotlin.math.pow
import kotlin.math.sqrt
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.chord.column
import demo.examples.gauge.center
import demo.examples.geo.region
import demo.examples.matrix.cell
import demo.examples.shared.Lcg
import demo.examples.shared.row
import demo.examples.sunburst.radius
import demo.examples.tree.leaves

/** One feature of `buildings.json`; the region is named after [BuildingsProperties.name]. */
internal external interface BuildingsProperties {
    val name: String
    val height: Double
}

internal external interface BuildingsFeature {
    val properties: BuildingsProperties
}

internal external interface BuildingsGeoJson {
    val features: Array<BuildingsFeature>
}

/** One region of a `map3D` series: `{ name, value, height }`. */
internal fun map3DRegion(name: String, value: OptionDataValue, height: Double? = null): Map3DDataItemOption =
    unsafeJso {
        this.name = OptionId(name)
        typedValue = OptionDataValueOrArray(value)
        this.height = height
    }

/** The eleven-stop palette the alcohol and buildings pages map their values through. */
internal fun map3DVisualMap(min: Double, max: Double, show: Boolean = true): VisualMapOption =
    unsafeJso {
        this.min = min
        this.max = max
        this.show = show
        realtime = true
        calculable = true
        inRange =
            unsafeJso<VisualOptionUnit> {
                color =
                    StringOrStringArray(
                        arrayOf(
                            "#313695",
                            "#4575b4",
                            "#74add1",
                            "#abd9e9",
                            "#e0f3f8",
                            "#ffffbf",
                            "#fee090",
                            "#fdae61",
                            "#f46d43",
                            "#d73027",
                            "#a50026",
                        ),
                    )
            }
    }

/**
 * `alcohol.json` as `[country, litres]` rows; the values are strings in the file.
 *
 * The file ends with a one-element `[""]` row, so the second column is read with [getOrNull].
 */
internal fun alcoholValues(alcohol: JsAny): Array<Array<String>> = alcohol.unsafeCast<Array<Array<String>>>()

/** The regions of `map3d-alcohol-consumption`: the litres as the value of the `visualMap`. */
internal fun alcoholRegions(alcohol: JsAny): Array<Map3DDataItemOption?> {
    val rows = alcoholValues(alcohol)
    return Array(rows.size) { index ->
        val litres = rows[index].getOrNull(1)
        map3DRegion(regionName(rows[index][0]), OptionDataValue(litres ?: ""))
    }
}

/**
 * The regions of `map3d-wood-map`: the same litres, turned into the extrusion height.
 *
 * Upstream writes `Math.pow(item[1], 0.2) + 1`. An empty cell counts as `0` and becomes the height
 * `1`, while the missing value of the trailing `[""]` row becomes `NaN` and leaves the (empty-named)
 * region at its default height.
 */
internal fun alcoholHeights(alcohol: JsAny): Array<Map3DDataItemOption?> {
    val rows = alcoholValues(alcohol)
    return Array(rows.size) { index ->
        val litres = rows[index].getOrNull(1)
        val height = litres?.let { (it.toDoubleOrNull() ?: 0.0).pow(0.2) + 1.0 } ?: Double.NaN
        map3DRegion(regionName(rows[index][0]), OptionDataValue(litres ?: ""), height)
    }
}

/** Registers `buildings.json` as the `buildings` map and returns its parsed features. */
internal fun registerBuildingsMap(buildings: JsAny): Array<BuildingsFeature> {
    ECharts.registerMap(
        "buildings",
        MapInput(unsafeJso<GeoJSONMapInput> { geoJSON = buildings.unsafeCast<GeoJSONSourceInput>() }),
    )
    return buildings.unsafeCast<BuildingsGeoJson>().features
}

/** One region per building, its height one tenth of the value stored in the GeoJSON. */
internal fun buildingRegions(features: Array<BuildingsFeature>, random: Lcg): Array<Map3DDataItemOption?> =
    Array(features.size) { index ->
        val properties = features[index].properties
        map3DRegion(properties.name, OptionDataValue(random.next()), properties.height / 10.0)
    }

/** One region per building, extruded to the square root of its height as the wood city does. */
internal fun woodCityRegions(features: Array<BuildingsFeature>): Array<Map3DDataItemOption?> =
    Array(features.size) { index ->
        val properties = features[index].properties
        val height = maxOf(sqrt(properties.height), 0.1)
        map3DRegion(properties.name, OptionDataValue(height), height)
    }

/** The option of `map3d-alcohol-consumption`: a lambert-shaded world colored by litres drunk. */
internal fun alcoholMapOption(alcohol: JsAny): EChartsGlOption =
    unsafeJso {
        backgroundColor = ZRColor("#cdcfd5")
        visualMap = one(map3DVisualMap(0.0, 15.0))
        series =
            one(
                map3DSeries {
                    map = "world"
                    shading = ShadingTypes.lambert
                    realisticMaterial =
                        unsafeJso<RealisticMaterialOption> {
                            roughness = NumberOrTexture(0.2)
                            metalness = NumberOrTexture(0.0)
                        }
                    postEffect =
                        unsafeJso<PostEffectOption> {
                            enable = true
                            ssao =
                                unsafeJso<ScreenSpaceAmbientOcclusionOption> {
                                    enable = true
                                    radius = 2.0
                                    intensity = 1.0
                                }
                        }
                    groundPlane = unsafeJso<Geo3DGroundPlaneOption> { show = true }
                    light =
                        unsafeJso<LightOption> {
                            main =
                                unsafeJso {
                                    intensity = 2.0
                                    shadow = true
                                    shadowQuality = QualityTypes.high
                                    alpha = 30.0
                                }
                            ambient = unsafeJso { intensity = 0.0 }
                            ambientCubemap =
                                unsafeJso<AmbientCubemapLightOption> {
                                    texture = "data/img/gl/canyon.hdr"
                                    exposure = 1.0
                                    diffuseIntensity = 1.0
                                }
                        }
                    viewControl = unsafeJso<ViewControlOption> { distance = 50.0 }
                    regionHeight = 1.0
                    typedData = alcoholRegions(alcohol)
                },
            )
    }

/** The option of `map3d-buildings`: the buildings GeoJSON extruded and softly out of focus. */
internal fun buildingsMapOption(buildings: JsAny): EChartsGlOption =
    unsafeJso {
        visualMap = one(map3DVisualMap(0.4, 1.0, show = false))
        series =
            one(
                map3DSeries {
                    map = "buildings"
                    shading = ShadingTypes.realistic
                    environment = GlEnvironment("#000")
                    realisticMaterial =
                        unsafeJso<RealisticMaterialOption> {
                            roughness = NumberOrTexture(0.6)
                            textureTiling = DoubleOrDoubleArray(20.0)
                        }
                    postEffect =
                        unsafeJso<PostEffectOption> {
                            enable = true
                            ssao =
                                unsafeJso<ScreenSpaceAmbientOcclusionOption> {
                                    enable = true
                                    intensity = 1.3
                                    radius = 5.0
                                }
                            screenSpaceReflection =
                                unsafeJso<ScreenSpaceReflectionOption> { enable = false }
                            depthOfField =
                                unsafeJso<DepthOfFieldOption> {
                                    enable = true
                                    blurRadius = 4.0
                                    focalDistance = 30.0
                                }
                        }
                    light =
                        unsafeJso<LightOption> {
                            main =
                                unsafeJso {
                                    intensity = 3.0
                                    alpha = 40.0
                                    shadow = true
                                    shadowQuality = QualityTypes.high
                                }
                            ambient = unsafeJso { intensity = 0.0 }
                            ambientCubemap =
                                unsafeJso<AmbientCubemapLightOption> {
                                    texture = "data/img/gl/pisa.hdr"
                                    exposure = 1.0
                                    diffuseIntensity = 0.5
                                    specularIntensity = 1.0
                                }
                        }
                    groundPlane =
                        unsafeJso<Geo3DGroundPlaneOption> {
                            show = false
                            color = GlColor(ZRColor("#333"))
                        }
                    viewControl =
                        unsafeJso<ViewControlOption> {
                            minBeta = -360.0
                            maxBeta = 360.0
                            alpha = 50.0
                            center = arrayOf(50.0, 0.0, -10.0)
                            distance = 30.0
                            minDistance = 5.0
                            panMouseButton = MouseButtons.left
                            rotateMouseButton = MouseButtons.middle
                            zoomSensitivity = 0.5
                        }
                    silent = true
                    boxWidth = 200.0
                    boxHeight = 1.0
                    typedData = buildingRegions(registerBuildingsMap(buildings), Lcg(20261030))
                },
            )
    }

/** The option of `map3d-wood-city`: the same buildings with a wood grain detail texture. */
internal fun woodCityMapOption(buildings: JsAny): EChartsGlOption =
    unsafeJso {
        series =
            one(
                map3DSeries {
                    map = "buildings"
                    shading = ShadingTypes.realistic
                    realisticMaterial =
                        unsafeJso<RealisticMaterialOption> {
                            roughness = NumberOrTexture(0.6)
                            textureTiling = DoubleOrDoubleArray(20.0)
                            detailTexture = TextureSource("data/img/gl/woods.jpg")
                        }
                    postEffect =
                        unsafeJso<PostEffectOption> {
                            enable = true
                            bloom = unsafeJso<BloomOption> { enable = false }
                            ssao =
                                unsafeJso<ScreenSpaceAmbientOcclusionOption> {
                                    enable = true
                                    quality = QualityTypes.medium
                                    radius = 10.0
                                    intensity = 1.2
                                }
                            depthOfField =
                                unsafeJso<DepthOfFieldOption> {
                                    enable = false
                                    focalRange = 5.0
                                    fstop = 1.0
                                    blurRadius = 6.0
                                }
                        }
                    groundPlane =
                        unsafeJso<Geo3DGroundPlaneOption> {
                            show = true
                            color = GlColor(ZRColor("#333"))
                        }
                    light =
                        unsafeJso<LightOption> {
                            main =
                                unsafeJso {
                                    intensity = 6.0
                                    shadow = true
                                    shadowQuality = QualityTypes.high
                                    alpha = 30.0
                                }
                            ambient = unsafeJso { intensity = 0.0 }
                            ambientCubemap =
                                unsafeJso<AmbientCubemapLightOption> {
                                    texture = "data/img/gl/canyon.hdr"
                                    exposure = 2.0
                                    diffuseIntensity = 1.0
                                    specularIntensity = 1.0
                                }
                        }
                    viewControl =
                        unsafeJso<ViewControlOption> {
                            minBeta = -360.0
                            maxBeta = 360.0
                        }
                    silent = true
                    boxWidth = 200.0
                    boxHeight = 1.0
                    typedData = woodCityRegions(registerBuildingsMap(buildings))
                },
            )
    }

/** The option of `map3d-wood-map`: the world with wood materials on a red-brick ground. */
internal fun woodMapOption(alcohol: JsAny): EChartsGlOption =
    unsafeJso {
        series =
            one(
                map3DSeries {
                    map = "world"
                    shading = ShadingTypes.realistic
                    realisticMaterial =
                        unsafeJso<RealisticMaterialOption> {
                            roughness = NumberOrTexture("data/img/gl/wood/roughness.png")
                            normalTexture = TextureSource("data/img/gl/wood/normal.jpg")
                            detailTexture = TextureSource("data/img/gl/wood/diffuse.jpg")
                            textureTiling = DoubleOrDoubleArray(arrayOf(2.0, 2.0))
                        }
                    postEffect =
                        unsafeJso<PostEffectOption> {
                            enable = true
                            ssao =
                                unsafeJso<ScreenSpaceAmbientOcclusionOption> {
                                    enable = true
                                    radius = 3.0
                                    intensity = 1.4
                                    quality = QualityTypes.high
                                }
                        }
                    light =
                        unsafeJso<LightOption> {
                            main =
                                unsafeJso {
                                    intensity = 2.0
                                    shadow = true
                                    shadowQuality = QualityTypes.high
                                    alpha = 150.0
                                    beta = 0.0
                                }
                            ambient = unsafeJso { intensity = 0.0 }
                            ambientCubemap =
                                unsafeJso<AmbientCubemapLightOption> {
                                    diffuseIntensity = 2.0
                                    specularIntensity = 2.0
                                    texture = "data/img/gl/canyon.hdr"
                                }
                        }
                    viewControl =
                        unsafeJso<ViewControlOption> {
                            alpha = 89.0
                            rotateMouseButton = MouseButtons.right
                            panMouseButton = MouseButtons.left
                            distance = 80.0
                        }
                    groundPlane =
                        unsafeJso<Geo3DGroundPlaneOption> {
                            show = true
                            color = GlColor(ZRColor("#333"))
                            realisticMaterial =
                                unsafeJso<RealisticMaterialOption> {
                                    roughness = NumberOrTexture("data/img/gl/redbricks/roughness.jpg")
                                    normalTexture = TextureSource("data/img/gl/redbricks/normal.jpg")
                                    detailTexture = TextureSource("data/img/gl/redbricks/diffuse.jpg")
                                    textureTiling = DoubleOrDoubleArray(arrayOf(8.0, 4.0))
                                }
                        }
                    typedData = alcoholHeights(alcohol)
                },
            )
    }
