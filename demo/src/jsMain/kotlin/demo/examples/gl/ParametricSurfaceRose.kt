package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.*
import io.github.shijianjs.ktjs.echarts.gl.BooleanOrAuto
import io.github.shijianjs.ktjs.echarts.gl.GlColor
import io.github.shijianjs.ktjs.echarts.gl.options.*
import js.objects.unsafeJso
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.shared.toolbox
import demo.examples.sunburst.radius

private val parametricSurfaceRoseCategory = Category(Groups.GL, "examples/gl/ParametricSurfaceRose.kt")

val parametricSurfaceRoseEntry: CatalogEntry =
    parametricSurfaceRoseCategory.chart(
            "parametric-surface-rose",
            "Parametric Surface Rose",
            "Parametric Surface Rose",
            "单朵白色玫瑰：边缘描线加泛光遮蔽，在暗色环境光下勾出花瓣的轮廓。",
            "A single white rose: the edge pass and ambient occlusion trace the outline of the petals.",
        ) {
            unsafeJso<EChartsGlOption> {
                toolbox = toolbox(saveAsImage = true)
                surfaceAxes()
                grid3D =
                    one(
                        unsafeJso<Grid3DOption> {
                            show = false
                            axisPointer = unsafeJso { show = false }
                            postEffect =
                                unsafeJso<PostEffectOption> {
                                    enable = true
                                    ssao = unsafeJso { enable = true; radius = 10.0; intensity = 2.0 }
                                    edge = unsafeJso { enable = true }
                                }
                            temporalSuperSampling = unsafeJso { enable = BooleanOrAuto(true) }
                            light =
                                unsafeJso<LightOption> {
                                    main = unsafeJso { intensity = 3.0; shadow = true }
                                    ambient = unsafeJso { intensity = 0.0 }
                                    ambientCubemap = cubemap("data/img/gl/canyon.hdr", 0.0, 1.0, 0.5)
                                }
                        },
                    )
                series =
                    one(
                        realisticSurface(
                            roseEquation(0.0, 0.0),
                            realistic(0.7, 0.0, arrayOf(200.0, 20.0)),
                        ).apply { typedItemStyle = unsafeJso { color = GlColor(ZRColor("#fff")) } },
                    )
            }
        }
