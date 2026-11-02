package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import echarts.options.*
import echartsgl.options.*
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.sunburst.radius

private val surfaceBreatherCategory = Category(Groups.GL, "examples/gl/SurfaceBreather.kt")

val surfaceBreatherEntry: CatalogEntry =
    surfaceBreatherCategory.chart(
            "surface-breather",
            "Breather",
            "Breather",
            "呼吸子曲面：可见的坐标轴、青色到红色的高度色带，金属度为 0 的哑光表面。",
            "The breather surface with visible axes and a blue-to-red height ramp on a matte, " +
                "non-metallic material.",
        ) {
            unsafeJso<EChartsGlOption> {
                tooltip = one(unsafeJso<TooltipOption> {})
                visualMap = one(surfaceRamp(-3.0, 3.0))
                surfaceAxes()
                grid3D =
                    one(
                        unsafeJso<Grid3DOption> {
                            show = true
                            postEffect =
                                unsafeJso<PostEffectOption> {
                                    enable = true
                                    screenSpaceAmbientOcclusion = unsafeJso { enable = true; radius = 4.0 }
                                }
                            viewControl = unsafeJso { distance = 130.0; beta = 50.0 }
                            light =
                                unsafeJso<LightOption> {
                                    main = unsafeJso { intensity = 2.0; shadow = true }
                                    ambient = unsafeJso { intensity = 0.0 }
                                    ambientCubemap = cubemap("data/img/gl/canyon.hdr", 2.0, 0.2, 1.0)
                                }
                        },
                    )
                series = one(realisticSurface(breatherEquation(), realistic(0.3, 0.0), silent = false))
            }
        }
