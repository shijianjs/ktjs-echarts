package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import echarts.options.*
import echartsgl.options.*
import js.objects.unsafeJso
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import demo.examples.Category
import demo.examples.shared.every

private val sphereParametricSurfaceCategory = Category(Groups.GL, "examples/gl/SphereParametricSurface.kt")

val sphereParametricSurfaceEntry: CatalogEntry =
    sphereParametricSurfaceCategory.chart(
            "sphere-parametric-surface",
            "Sphere Parametric Surface",
            "Sphere Parametric Surface",
            "用参数方程 (u, v) 画一个球：u 是经度、v 是纬度，步长都是 π/20。",
            "A sphere from a parametric equation: u is the longitude and v the latitude, both " +
                "sampled every pi/20.",
        ) {
            unsafeJso<EChartsGlOption> {
                tooltip = one(unsafeJso<TooltipOption> {})
                visualMap = one(glVisualMap(min = -1.0, max = 1.0))
                valueAxes()
                grid3D = one(unsafeJso<Grid3DOption> {})
                series =
                    one(
                        surfaceSeries {
                            parametric = true
                            parametricEquation =
                                unsafeJso<SurfaceParametricEquationOption> {
                                    u =
                                        unsafeJso<SurfaceEquationDimensionOption> {
                                            min = -PI
                                            max = PI
                                            step = PI / 20
                                        }
                                    v =
                                        unsafeJso<SurfaceEquationDimensionOption> {
                                            min = 0.0
                                            max = PI
                                            step = PI / 20
                                        }
                                    x = SurfaceFunction { u, v -> sin(v) * sin(u) }
                                    y = SurfaceFunction { u, v -> sin(v) * cos(u) }
                                    z = SurfaceFunction { _, v -> cos(v) }
                                }
                        },
                    )
            }
        }
