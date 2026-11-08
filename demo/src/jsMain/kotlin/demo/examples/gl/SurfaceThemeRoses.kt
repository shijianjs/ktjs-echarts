package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.*
import io.github.shijianjs.ktjs.echarts.gl.BooleanOrAuto
import io.github.shijianjs.ktjs.echarts.gl.options.*
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.shared.toolbox
import demo.examples.sunburst.radius

private val surfaceThemeRosesCategory = Category(Groups.GL, "examples/gl/SurfaceThemeRoses.kt")

val surfaceThemeRosesEntry: CatalogEntry =
    surfaceThemeRosesCategory.chart(
            "surface-theme-roses",
            "Theme Roses",
            "Theme Roses",
            "同一束玫瑰换成哑光的默认配色，纹理平铺 200×20 次。",
            "The same bouquet in the default matte palette, with the material textures tiled " +
                "200×20 times.",
        ) {
            unsafeJso<EChartsGlOption> {
                toolbox = toolbox(saveAsImage = true)
                surfaceAxes()
                grid3D =
                    one(
                        unsafeJso<Grid3DOption> {
                            show = false
                            boxWidth = 200.0
                            boxDepth = 200.0
                            axisPointer = unsafeJso { show = false }
                            postEffect =
                                unsafeJso<PostEffectOption> {
                                    enable = true
                                    screenSpaceAmbientOcclusion = unsafeJso {
                                        enable = true
                                        radius = 10.0
                                        intensity = 1.0
                                    }
                                }
                            temporalSuperSampling = unsafeJso { enable = BooleanOrAuto(true) }
                            light =
                                unsafeJso<LightOption> {
                                    main = unsafeJso { intensity = 1.0; shadow = true }
                                    ambient = unsafeJso { intensity = 0.0 }
                                    ambientCubemap = cubemap("data/img/gl/pisa.hdr", 1.0, 1.0, 1.0)
                                }
                        },
                    )
                series = roseBouquet(realistic(0.7, 0.0, arrayOf(200.0, 20.0)), null)
            }
        }
