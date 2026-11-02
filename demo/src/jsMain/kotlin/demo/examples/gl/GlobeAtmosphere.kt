package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import echarts.options.one
import echartsgl.GlEnvironment
import echartsgl.ShadingTypes
import echartsgl.TextureSource
import echartsgl.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import zrender.ZRColor
import demo.examples.Category

private val globeAtmosphereCategory = Category(Groups.GL, "examples/gl/GlobeAtmosphere.kt")

val globeAtmosphereEntry: CatalogEntry =
    globeAtmosphereCategory.chart(
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
        }
