package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import echarts.options.one
import echartsgl.BooleanOrAuto
import echartsgl.NumberOrTexture
import echartsgl.QualityTypes
import echartsgl.ShadingTypes
import echartsgl.TextureSource
import echartsgl.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.sunburst.radius

private val globeDisplacementCategory = Category(Groups.GL, "examples/gl/GlobeDisplacement.kt")

val globeDisplacementEntry: CatalogEntry =
    globeDisplacementCategory.chart(
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
        }
