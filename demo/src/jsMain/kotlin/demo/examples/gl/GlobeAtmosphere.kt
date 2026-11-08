package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.gl.GlEnvironment
import io.github.shijianjs.ktjs.echarts.gl.ShadingTypes
import io.github.shijianjs.ktjs.echarts.gl.TextureSource
import io.github.shijianjs.ktjs.echarts.gl.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import io.github.shijianjs.ktjs.zrender.ZRColor
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
