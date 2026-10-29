package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.ECharts
import echarts.GeoJSONMapInput
import echarts.GeoJSONSourceInput
import echarts.MapInput
import echarts.options.DoubleOrDoubleArray
import echarts.options.OptionDataValue
import echarts.options.OptionDataValueOrArray
import echarts.options.OptionId
import echarts.options.StringOrStringArray
import echarts.options.VisualMapOption
import echarts.options.VisualOptionUnit
import echarts.options.one
import echartsgl.GlEnvironment
import echartsgl.GlColor
import echartsgl.MouseButtons
import echartsgl.NumberOrTexture
import echartsgl.QualityTypes
import echartsgl.ShadingTypes
import echartsgl.TextureSource
import echartsgl.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import kotlin.math.pow
import kotlin.math.sqrt
import zrender.ZRColor

/*
 * The four `map3D` pages of the `gl/` directory of apache/echarts-examples.
 *
 * `map3D` is a series of `echarts-gl`, not a component: it registers its own `geo3D` coordinate
 * system from the `map` option, so the pages only have to register the map (the world map, or the
 * buildings GeoJSON) and give it the data. `alcohol.json` lists `[country, litres]` rows and
 * `buildings.json` is a GeoJSON whose features carry a `name` and a `height`; both are copied
 * verbatim from `public/data-gl/asset/data/`.
 *
 * Changed on purpose:
 *
 * - `map3d-buildings` shuffles `Math.random()` for the region colors; the port draws them from
 *   [Lcg], so the same buildings get the same colors on every visit;
 * - the `itemStyle.areaColor` / `label.color` of the two building pages and of the wood city are
 *   dropped: `areaColor` is a field of the 2D map series that no `echarts-gl` view reads, and a
 *   region label stays hidden unless `label.show` is turned on;
 * - `instancing` is dropped as well — the `map3D` series of 2.1.0 never reads it;
 * - the `realisticMaterial` of the alcohol page sits next to `shading: 'lambert'`, so it is not the
 *   material in use; it is kept because it is what the original declares;
 * - the country names of `alcohol.json` are spelled out (`Czech Republic`, `South Korea`, ...) while
 *   the `world.json` this site registers abbreviates them, so the two world pages run the names
 *   through [regionName]; without it those countries keep the default color.
 */

private val glMap = Category(Groups.GL, "examples/GlMap.kt")

/** One feature of `buildings.json`; the region is named after [BuildingsProperties.name]. */
private external interface BuildingsProperties {
    val name: String
    val height: Double
}

private external interface BuildingsFeature {
    val properties: BuildingsProperties
}

private external interface BuildingsGeoJson {
    val features: Array<BuildingsFeature>
}

/** One region of a `map3D` series: `{ name, value, height }`. */
private fun map3DRegion(name: String, value: OptionDataValue, height: Double? = null): Map3DDataItemOption =
    unsafeJso {
        this.name = OptionId(name)
        typedValue = OptionDataValueOrArray(value)
        this.height = height
    }

/** The eleven-stop palette the alcohol and buildings pages map their values through. */
private fun map3DVisualMap(min: Double, max: Double, show: Boolean = true): VisualMapOption =
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
private fun alcoholValues(alcohol: JsAny): Array<Array<String>> = alcohol.unsafeCast<Array<Array<String>>>()

/** The regions of `map3d-alcohol-consumption`: the litres as the value of the `visualMap`. */
private fun alcoholRegions(alcohol: JsAny): Array<Map3DDataItemOption?> {
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
private fun alcoholHeights(alcohol: JsAny): Array<Map3DDataItemOption?> {
    val rows = alcoholValues(alcohol)
    return Array(rows.size) { index ->
        val litres = rows[index].getOrNull(1)
        val height = litres?.let { (it.toDoubleOrNull() ?: 0.0).pow(0.2) + 1.0 } ?: Double.NaN
        map3DRegion(regionName(rows[index][0]), OptionDataValue(litres ?: ""), height)
    }
}

/** Registers `buildings.json` as the `buildings` map and returns its parsed features. */
private fun registerBuildingsMap(buildings: JsAny): Array<BuildingsFeature> {
    ECharts.registerMap(
        "buildings",
        MapInput(unsafeJso<GeoJSONMapInput> { geoJSON = buildings.unsafeCast<GeoJSONSourceInput>() }),
    )
    return buildings.unsafeCast<BuildingsGeoJson>().features
}

/** One region per building, its height one tenth of the value stored in the GeoJSON. */
private fun buildingRegions(features: Array<BuildingsFeature>, random: Lcg): Array<Map3DDataItemOption?> =
    Array(features.size) { index ->
        val properties = features[index].properties
        map3DRegion(properties.name, OptionDataValue(random.next()), properties.height / 10.0)
    }

/** One region per building, extruded to the square root of its height as the wood city does. */
private fun woodCityRegions(features: Array<BuildingsFeature>): Array<Map3DDataItemOption?> =
    Array(features.size) { index ->
        val properties = features[index].properties
        val height = maxOf(sqrt(properties.height), 0.1)
        map3DRegion(properties.name, OptionDataValue(height), height)
    }

/** The option of `map3d-alcohol-consumption`: a lambert-shaded world colored by litres drunk. */
private fun alcoholMapOption(alcohol: JsAny): EChartsGlOption =
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
private fun buildingsMapOption(buildings: JsAny): EChartsGlOption =
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
private fun woodCityMapOption(buildings: JsAny): EChartsGlOption =
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
private fun woodMapOption(alcohol: JsAny): EChartsGlOption =
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

val glMapEntries: List<CatalogEntry> =
    listOf(
        glMap.interactive(
            "map3d-alcohol-consumption",
            "Map3D - 酒精消费量",
            "Map3D - Alcohol Consumption",
            "世界地图按人均饮酒量着色并挤出成立体，视线从正上方略偏的角度俯瞰。",
            "The world map colored by alcohol consumption per person and extruded, seen from a " +
                "slightly tilted top-down view.",
        ) { chart ->
            registerWorldMap().then {
                loadJson("data/gl/alcohol.json").then { alcohol ->
                    chart.setOption(alcoholMapOption(alcohol))
                }
            }
        },
        glMap.interactive(
            "map3d-buildings",
            "三维建筑",
            "Buildings",
            "一座城市的每栋楼按 GeoJSON 里记录的高度挤出，景深让远处的楼群虚化，画面接近实景照片。",
            "Every building of a city extruded to the height stored in its GeoJSON, with the depth " +
                "of field blurring the blocks in the distance.",
        ) { chart ->
            loadJson("data/gl/buildings.json").then { buildings ->
                chart.setOption(buildingsMapOption(buildings))
            }
        },
        glMap.interactive(
            "map3d-wood-city",
            "木质风格城市",
            "Wood City",
            "同一座城市换成木纹材质立在木质地面上，柔和的阴影把楼与楼之间压出层次。",
            "The same city drawn in a wood material on a wooden ground, the soft shadows separating " +
                "the blocks from each other.",
        ) { chart ->
            loadJson("data/gl/buildings.json").then { buildings ->
                chart.setOption(woodCityMapOption(buildings))
            }
        },
        glMap.interactive(
            "map3d-wood-map",
            "木质世界地图",
            "Wood Map",
            "世界地图的每个国家按饮酒量开方后挤出，整块大陆铺着木纹，红砖地面在斜阳下接住长影。",
            "Every country is extruded to the square root of its alcohol consumption, the continents " +
                "covered in wood grain and standing on a red-brick ground.",
        ) { chart ->
            registerWorldMap().then {
                loadJson("data/gl/alcohol.json").then { alcohol ->
                    chart.setOption(woodMapOption(alcohol))
                }
            }
        },
    )
