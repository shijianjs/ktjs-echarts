package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import echarts.options.one
import echartsgl.GlEnvironment
import echartsgl.GlobeBlendTargets
import echartsgl.GlobeLayerTypes
import echartsgl.ShadingTypes
import echartsgl.TextureSource
import echartsgl.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import zrender.ZRColor
import demo.examples.Category

private val globeLayersCategory = Category(Groups.GL, "examples/gl/GlobeLayers.kt")

val globeLayersEntry: CatalogEntry =
    globeLayersCategory.chart(
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
        }
