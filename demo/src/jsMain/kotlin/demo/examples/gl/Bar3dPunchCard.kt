package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import echarts.AxisTypes
import echarts.options.*
import echartsgl.GlColor
import echartsgl.ShadingTypes
import echartsgl.options.*
import js.objects.unsafeJso
import zrender.ZRColor
import demo.examples.Category
import demo.examples.custom.punchCardDays
import demo.examples.custom.punchCardHours

private val bar3dPunchCardCategory = Category(Groups.GL, "examples/gl/Bar3dPunchCard.kt")

val bar3dPunchCardEntry: CatalogEntry =
    bar3dPunchCardCategory.chart(
            "bar3d-punch-card",
            "Bar3D - 打卡统计柱状图",
            "Bar3D - Punch Card",
            "打卡数据画成三维柱：横轴是小时、纵轴是星期、柱高与颜色为提交次数，拖动可旋转视角。",
            "The punch card data drawn as 3D bars: hours along x, weekdays along y, bar height and " +
                "color are the commit count. Drag the scene to rotate it.",
        ) {
            unsafeJso<EChartsGlOption> {
                tooltip = one(unsafeJso<TooltipOption> {})
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            max = 20.0
                            inRange = unsafeJso<VisualOptionUnit> { color = StringOrStringArray(glPalette) }
                        },
                    )
                xAxis3D =
                    one(
                        unsafeJso<Axis3DOption> {
                            type = AxisTypes.category
                            data = strings(*punchCardHours)
                        },
                    )
                yAxis3D =
                    one(
                        unsafeJso<Axis3DOption> {
                            type = AxisTypes.category
                            data = strings(*punchCardDays)
                        },
                    )
                zAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
                grid3D =
                    one(
                        unsafeJso<Grid3DOption> {
                            boxWidth = 200.0
                            boxDepth = 80.0
                            light =
                                unsafeJso<LightOption> {
                                    main = unsafeJso<MainLightOption> { intensity = 1.2; shadow = true }
                                    ambient = unsafeJso<AmbientLightOption> { intensity = 0.3 }
                                }
                        },
                    )
                series =
                    one(
                        bar3DSeries {
                            shading = ShadingTypes.lambert
                            typedData = punchBars()
                            typedLabel =
                                unsafeJso<LabelOption> {
                                    fontSize = StringOrDouble(16.0)
                                    borderWidth = 1.0
                                }
                            typedEmphasis =
                                unsafeJso<GlEmphasisOption> {
                                    label =
                                        unsafeJso<LabelOption> {
                                            fontSize = StringOrDouble(20.0)
                                            color = "#900"
                                        }
                                    itemStyle = unsafeJso<GlItemStyleOption> { color = GlColor(ZRColor("#900")) }
                                }
                        },
                    )
            }
        }
