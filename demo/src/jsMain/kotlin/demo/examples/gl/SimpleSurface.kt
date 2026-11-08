package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.*
import io.github.shijianjs.ktjs.echarts.gl.options.*
import js.objects.unsafeJso
import kotlin.math.sin
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category

private val simpleSurfaceCategory = Category(Groups.GL, "examples/gl/SimpleSurface.kt")

val simpleSurfaceEntry: CatalogEntry =
    simpleSurfaceCategory.chart(
            "simple-surface",
            "Simple Surface",
            "Simple Surface",
            "函数曲面 z = sin(πx) sin(πy)，原点附近挖掉一块，因此曲面中间是空的。",
            "The function surface z = sin(pi x) sin(pi y) with a hole punched around the origin.",
        ) {
            unsafeJso<EChartsGlOption> {
                tooltip = one(unsafeJso<TooltipOption> {})
                backgroundColor = ZRColor("#fff")
                visualMap = one(glVisualMap(min = -1.0, max = 1.0))
                valueAxes()
                grid3D = one(unsafeJso<Grid3DOption> {})
                series =
                    one(
                        surfaceSeries {
                            wireframe = unsafeJso<SurfaceWireframeOption> {}
                            equation = unsafeJso<SurfaceEquationOption> { sinProductSurface() }
                        },
                    )
            }
        }
