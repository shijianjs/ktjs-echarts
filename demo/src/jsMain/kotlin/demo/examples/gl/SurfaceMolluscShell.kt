package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import echarts.options.*
import echartsgl.BooleanOrAuto
import echartsgl.options.*
import js.objects.unsafeJso
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import demo.examples.Category

private val surfaceMolluscShellCategory = Category(Groups.GL, "examples/gl/SurfaceMolluscShell.kt")

val surfaceMolluscShellEntry: CatalogEntry =
    surfaceMolluscShellCategory.chart(
            "surface-mollusc-shell",
            "Mollusc Shell",
            "Mollusc Shell",
            "软体动物的螺壳：一条指数螺线绕成壳，高度色带从深蓝渐变到暗红。",
            "A mollusc shell: an exponential spiral coiled into a shell, shaded by a blue-to-red " +
                "height ramp.",
        ) {
            unsafeJso<EChartsGlOption> {
                tooltip = one(unsafeJso<TooltipOption> {})
                visualMap = one(surfaceRamp(-5.0, 0.0))
                surfaceAxes()
                grid3D =
                    one(
                        unsafeJso<Grid3DOption> {
                            show = true
                            postEffect = unsafeJso<PostEffectOption> { enable = true }
                            temporalSuperSampling = unsafeJso { enable = BooleanOrAuto(true) }
                            light =
                                unsafeJso<LightOption> {
                                    main = unsafeJso { intensity = 3.0; shadow = true }
                                    ambient = unsafeJso { intensity = 0.0 }
                                    ambientCubemap = cubemap("data/img/gl/canyon.hdr", 2.0, 1.0, 1.0)
                                }
                        },
                    )
                series =
                    one(
                        realisticSurface(
                            unsafeJso {
                                u = unsafeJso { min = -PI; max = PI; step = PI / 40 }
                                v = unsafeJso { min = -15.0; max = 6.0; step = 0.21 }
                                x =
                                    SurfaceFunction { uValue, vValue ->
                                        pow(1.16, vValue) * cos(vValue) * (1 + cos(uValue))
                                    }
                                y =
                                    SurfaceFunction { uValue, vValue ->
                                        -pow(1.16, vValue) * sin(vValue) * (1 + cos(uValue))
                                    }
                                z =
                                    SurfaceFunction { uValue, vValue ->
                                        -2 * pow(1.16, vValue) * (1 + sin(uValue))
                                    }
                            },
                            realistic(0.4, 0.0),
                            silent = false,
                        ),
                    )
            }
        }
