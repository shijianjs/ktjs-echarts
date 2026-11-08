package demo.examples.gl

import io.github.shijianjs.ktjs.echarts.options.DoubleOrDoubleArray
import io.github.shijianjs.ktjs.echarts.options.StringOrStringArray
import io.github.shijianjs.ktjs.echarts.options.VisualMapControllerOption
import io.github.shijianjs.ktjs.echarts.options.VisualMapOption
import io.github.shijianjs.ktjs.echarts.options.VisualOptionUnit
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.gl.BooleanOrAuto
import io.github.shijianjs.ktjs.echarts.gl.CoordinateSystems3D
import io.github.shijianjs.ktjs.echarts.gl.GlColor
import io.github.shijianjs.ktjs.echarts.gl.GlEnvironment
import io.github.shijianjs.ktjs.echarts.gl.MouseButtons
import io.github.shijianjs.ktjs.echarts.gl.QualityTypes
import io.github.shijianjs.ktjs.echarts.gl.ShadingTypes
import io.github.shijianjs.ktjs.echarts.gl.TextureSource
import io.github.shijianjs.ktjs.echarts.gl.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import kotlin.math.sqrt
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.matrix.cell
import demo.examples.shared.doubleRow
import demo.examples.shared.every
import demo.examples.shared.row
import demo.examples.sunburst.radius

/**
 * The `bar3D` items of a `population.json` table: one bar per populated cell.
 *
 * Upstream drops the cells with a population of zero first; every row of the file is positive, so
 * the filter does not change the data and is left out. The third value is the square root of the
 * cell population, which keeps the densest cells from dwarfing the rest of the map.
 */
internal fun populationBars(population: JsAny): Array<Bar3DDataItem?> {
    val cells = population.unsafeCast<Array<Array<Double>>>()
    return Array(cells.size) { index ->
        val cell = cells[index]
        Bar3DDataItem(doubleRow(cell[0], cell[1], sqrt(cell[2])))
    }
}

/** The option of `bar3d-global-population`: orange bars on a lambert-shaded world. */
internal fun bar3DPopulationOption(population: JsAny): EChartsGlOption =
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
internal fun bar3DPopulationOnGlobeOption(population: JsAny): EChartsGlOption =
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
