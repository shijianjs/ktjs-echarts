package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.ECharts
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.gl.GlEnvironment
import io.github.shijianjs.ktjs.echarts.gl.NumberOrTexture
import io.github.shijianjs.ktjs.echarts.gl.ShadingTypes
import io.github.shijianjs.ktjs.echarts.gl.TextureSource
import io.github.shijianjs.ktjs.echarts.gl.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category

private val globeEchartsGlHelloWorldCategory = Category(Groups.GL, "examples/gl/GlobeEchartsGlHelloWorld.kt")

val globeEchartsGlHelloWorldEntry: CatalogEntry =
    globeEchartsGlHelloWorldCategory.chart(
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
        }
