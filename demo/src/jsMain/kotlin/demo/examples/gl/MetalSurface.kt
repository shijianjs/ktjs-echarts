package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import echarts.options.*
import echartsgl.BooleanOrAuto
import echartsgl.QualityTypes
import echartsgl.options.*
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.sunburst.radius

private val metalSurfaceCategory = Category(Groups.GL, "examples/gl/MetalSurface.kt")

val metalSurfaceEntry: CatalogEntry =
    metalSurfaceCategory.chart(
            "metal-surface",
            "Metal Surface",
            "Metal Surface",
            "呼吸子曲面镀上一层金属：环境光取自峡谷全景图，泛光遮蔽让凹处变暗。",
            "The breather surface plated in metal: the canyon panorama lights it and screen-space " +
                "ambient occlusion darkens the folds.",
        ) {
            unsafeJso<EChartsGlOption> {
                tooltip = one(unsafeJso<TooltipOption> {})
                visualMap = one(surfaceRamp(-5.0, 5.0))
                surfaceAxes()
                grid3D =
                    one(
                        unsafeJso<Grid3DOption> {
                            show = false
                            postEffect =
                                unsafeJso<PostEffectOption> {
                                    enable = true
                                    screenSpaceAmbientOcclusion =
                                        unsafeJso {
                                            enable = true
                                            radius = 4.0
                                            quality = QualityTypes.high
                                            intensity = 1.5
                                        }
                                }
                            temporalSuperSampling = unsafeJso { enable = BooleanOrAuto(true) }
                            light =
                                unsafeJso<LightOption> {
                                    main = unsafeJso { intensity = 2.0; shadow = true }
                                    ambient = unsafeJso { intensity = 0.0 }
                                    ambientCubemap = cubemap("data/img/gl/canyon.hdr", 2.0, 0.2, 3.0)
                                }
                        },
                    )
                series = one(realisticSurface(breatherEquation(), realistic(0.2, 1.0)))
            }
        }
