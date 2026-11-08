package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.AxisTypes
import io.github.shijianjs.ktjs.echarts.options.*
import io.github.shijianjs.ktjs.echarts.gl.GlColor
import io.github.shijianjs.ktjs.echarts.gl.ShadingTypes
import io.github.shijianjs.ktjs.echarts.gl.options.*
import js.objects.unsafeJso
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.custom.punchCardDays
import demo.examples.custom.punchCardHours

private val transparentBar3dCategory = Category(Groups.GL, "examples/gl/TransparentBar3d.kt")

val transparentBar3dEntry: CatalogEntry =
    transparentBar3dCategory.chart(
            "transparent-bar3d",
            "三维柱状图 - 透明效果",
            "Transparent Bar3D",
            "与打卡柱状图同一组数据，柱子改成半透明并按数值直接着色，悬停时高亮为深红。",
            "The punch card data again, with translucent bars coloured by value and highlighted " +
                "in dark red on hover.",
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
                                    main = unsafeJso<MainLightOption> { intensity = 1.2 }
                                    ambient = unsafeJso<AmbientLightOption> { intensity = 0.3 }
                                }
                        },
                    )
                series =
                    one(
                        bar3DSeries {
                            shading = ShadingTypes.color
                            typedData = punchBars()
                            typedLabel = unsafeJso<LabelOption> { show = false; fontSize = StringOrDouble(16.0); borderWidth = 1.0 }
                            typedItemStyle = unsafeJso<GlItemStyleOption> { opacity = 0.4 }
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
