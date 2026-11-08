package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.AxisTypes
import io.github.shijianjs.ktjs.echarts.options.*
import io.github.shijianjs.ktjs.echarts.gl.ShadingTypes
import io.github.shijianjs.ktjs.echarts.gl.options.*
import js.objects.unsafeJso
import kotlin.math.sin
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category

private val surfaceWaveCategory = Category(Groups.GL, "examples/gl/SurfaceWave.kt")

val surfaceWaveEntry: CatalogEntry =
    surfaceWaveCategory.chart(
            "surface-wave",
            "Surface Wave",
            "Surface Wave",
            "函数曲面 z = sin(x² + y²) x 在 -3..3 上的涟漪，关掉线框、按高度着色。",
            "The ripple z = sin(x² + y²) x over -3..3, drawn without a wireframe and coloured by " +
                "height.",
        ) {
            unsafeJso<EChartsGlOption> {
                tooltip = one(unsafeJso<TooltipOption> {})
                backgroundColor = ZRColor("#fff")
                visualMap = one(glVisualMap(min = -1.0, max = 1.0))
                xAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
                yAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
                zAxis3D =
                    one(
                        unsafeJso<Axis3DOption> {
                            type = AxisTypes.value
                            max = AxisMinMax(1.0)
                            splitNumber = 2.0
                        },
                    )
                grid3D = one(unsafeJso<Grid3DOption> { boxHeight = 40.0 })
                series =
                    one(
                        surfaceSeries {
                            shading = ShadingTypes.color
                            wireframe = unsafeJso<SurfaceWireframeOption> { show = false }
                            equation = unsafeJso<SurfaceEquationOption> { rippledSurface() }
                        },
                    )
            }
        }
