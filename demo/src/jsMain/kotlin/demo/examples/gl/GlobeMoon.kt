package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import echarts.options.one
import echartsgl.BooleanOrAuto
import echartsgl.GlEnvironment
import echartsgl.NumberOrTexture
import echartsgl.QualityTypes
import echartsgl.ShadingTypes
import echartsgl.TextureSource
import echartsgl.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.sunburst.radius

private val globeMoonCategory = Category(Groups.GL, "examples/gl/GlobeMoon.kt")

val globeMoonEntry: CatalogEntry =
    globeMoonCategory.chart(
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
        }
