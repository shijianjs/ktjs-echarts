package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import echarts.options.*
import echartsgl.ProjectionTypes
import echartsgl.options.*
import js.objects.unsafeJso
import zrender.ZRColor
import demo.examples.Category

private val line3dOrthographicCategory = Category(Groups.GL, "examples/gl/Line3dOrthographic.kt")

val line3dOrthographicEntry: CatalogEntry =
    line3dOrthographicCategory.chart(
            "line3d-orthographic",
            "三维折线图 - 正交投影",
            "Line3D - Orthographic Projection",
            "一条绕了 25 圈的三维参数曲线，用正交相机画出，颜色由 visualMap 按 z 值分配。",
            "A parametric curve wound into 25 turns, drawn with an orthographic camera and " +
                "coloured by z through a visualMap.",
        ) {
            unsafeJso<EChartsGlOption> {
                tooltip = one(unsafeJso<TooltipOption> {})
                backgroundColor = ZRColor("#fff")
                visualMap = one(glVisualMap(min = 0.0, max = 30.0))
                valueAxes()
                grid3D =
                    one(
                        unsafeJso<Grid3DOption> {
                            viewControl = unsafeJso<ViewControlOption> { projection = ProjectionTypes.orthographic }
                        },
                    )
                series =
                    one(
                        line3DSeries {
                            typedData = helixPoints()
                            lineStyle = unsafeJso<LineStyleOption> { width = 4.0 }
                        },
                    )
            }
        }
