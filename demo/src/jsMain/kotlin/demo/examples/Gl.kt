package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.AxisTypes
import echarts.ECharts
import echarts.EChartsExtensionInstaller
import echarts.options.*
import echartsgl.Bar3DChart
import echartsgl.GlColor
import echartsgl.Grid3DComponent
import echartsgl.ShadingTypes
import echartsgl.options.*
import js.objects.unsafeJso
import zrender.ZRColor

/*
 * 3D examples ported from `public/examples/ts/gl/` of apache/echarts-examples.
 *
 * The GL charts are not part of the `echarts` entry: they have to be installed with `ECharts.use`
 * before the first chart is created, which is what `main()` does through [useEChartsGl]. A page that
 * needs another chart or coordinate system adds it to the array there.
 *
 * Two differences against the 1.x-era option of the official examples:
 *
 * - echarts-gl 2.1.0 reads the text properties of a series label from `label.textStyle`, so the flat
 *   `label: { fontSize, borderWidth }` of `bar3d-punch-card` is written as a nested style here.
 * - the `viewControl` of that example is empty upstream (it only carries a commented-out projection),
 *   so it is omitted instead of writing a no-op component.
 */

/**
 * Installs the GL charts and coordinate systems used by the examples of this file.
 *
 * Reading one of the installers also loads the `echarts-gl` core module, which registers the
 * post-update hook that paints the WebGL layer; without it `setOption` succeeds and the page stays
 * blank. `ECharts.use` deduplicates, so calling this once at startup is enough.
 */
internal fun useEChartsGl() {
    ECharts.use(arrayOf<EChartsExtensionInstaller>(Bar3DChart, Grid3DComponent))
}

private val gl = Category(Groups.GL, "examples/Gl.kt")

/**
 * The `bar3D` items of the punch card: `[hour, day, commits]`.
 *
 * The source of the numbers is [punchCardValues], the same table the heatmap and the polar heatmap
 * expand; the 3D example swaps the first two coordinates so that hours run along the x axis.
 */
private fun punchBars(): Array<Bar3DDataItem?> =
    Array(punchCardValues.size * 24) { index ->
        val day = index / 24
        val hour = index % 24
        Bar3DDataItem(row(hour.toDouble(), day.toDouble(), punchCardValues[day][hour].toDouble()))
    }

val glEntries: List<CatalogEntry> =
    listOf(
        gl.chart(
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
                            inRange =
                                unsafeJso<VisualOptionUnit> {
                                    color =
                                        StringOrStringArray(
                                            arrayOf(
                                                "#313695",
                                                "#4575b4",
                                                "#74add1",
                                                "#abd9e9",
                                                "#e0f3f8",
                                                "#ffffbf",
                                                "#fee090",
                                                "#fdae61",
                                                "#f46d43",
                                                "#d73027",
                                                "#a50026",
                                            ),
                                        )
                                }
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
                                    main =
                                        unsafeJso<MainLightOption> {
                                            intensity = 1.2
                                            shadow = true
                                        }
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
                                unsafeJso<GlLabelOption> {
                                    textStyle =
                                        unsafeJso<TextStyleOption> {
                                            fontSize = StringOrDouble(16.0)
                                            borderWidth = 1.0
                                        }
                                }
                            typedEmphasis =
                                unsafeJso<GlEmphasisOption> {
                                    label =
                                        unsafeJso<GlLabelOption> {
                                            textStyle =
                                                unsafeJso<TextStyleOption> {
                                                    fontSize = StringOrDouble(20.0)
                                                    color = "#900"
                                                }
                                        }
                                    itemStyle = unsafeJso<GlItemStyleOption> { color = GlColor(ZRColor("#900")) }
                                }
                        },
                    )
            }
        },
    )
