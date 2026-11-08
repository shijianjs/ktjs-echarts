package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.*
import io.github.shijianjs.ktjs.echarts.gl.BooleanOrAuto
import io.github.shijianjs.ktjs.echarts.gl.GlColor
import io.github.shijianjs.ktjs.echarts.gl.options.*
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.shared.toolbox
import demo.examples.sunburst.radius

private val surfaceGoldenRoseCategory = Category(Groups.GL, "examples/gl/SurfaceGoldenRose.kt")

val surfaceGoldenRoseEntry: CatalogEntry =
    surfaceGoldenRoseCategory.chart(
            "surface-golden-rose",
            "Golden Rose",
            "Golden Rose",
            "五朵金色玫瑰拼成一束：金属度为 1 的镀金材质，焦外虚化让远景略微模糊。",
            "Five golden roses in one bunch: a fully metallic gold material, softened by a shallow " +
                "depth of field.",
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
                                    depthOfField = unsafeJso { enable = true }
                                }
                            temporalSuperSampling = unsafeJso { enable = BooleanOrAuto(true) }
                            light =
                                unsafeJso<LightOption> {
                                    main = unsafeJso { intensity = 1.0; shadow = true }
                                    ambient = unsafeJso { intensity = 0.0 }
                                    ambientCubemap = cubemap("data/img/gl/pisa.hdr", 1.0, 1.0, 1.0)
                                }
                            viewControl = unsafeJso {}
                        },
                    )
                series =
                    roseBouquet(
                        realistic(0.3, 1.0),
                        unsafeJso { color = GlColor(arrayOf(2.0, 1.5, 0.2)) },
                    )
            }
        }
