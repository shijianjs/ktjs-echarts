package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.listen
import demo.ui.loadJson
import echarts.AxisTypes
import echarts.ECharts
import echarts.EChartsExtensionInstaller
import echarts.options.*
import echartsgl.Bar3DChart
import echartsgl.Geo3DComponent
import echartsgl.GlColor
import echartsgl.GlEnvironment
import echartsgl.GlobeComponent
import echartsgl.Grid3DComponent
import echartsgl.Line3DChart
import echartsgl.Lines3DChart
import echartsgl.ProjectionTypes
import echartsgl.Scatter3DChart
import echartsgl.ShadingTypes
import echartsgl.SurfaceChart
import echartsgl.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import web.canvas.CanvasImageSource
import web.canvas.CanvasRenderingContext2D
import web.canvas.ID
import web.dom.document
import web.events.Event
import web.events.EventType
import web.html.HTMLImageElement
import web.html.HtmlTagName
import zrender.ZRColor

/*
 * 3D examples ported from `public/examples/ts/gl/` of apache/echarts-examples.
 *
 * The GL charts are not part of the `echarts` entry: they have to be installed with `ECharts.use`
 * before the first chart is created, which is what `main()` does through [useEChartsGl]. A page that
 * needs another chart or coordinate system adds it to the array there.
 *
 * Two differences against some of the original options:
 *
 * - a label keeps its text properties flat (`label: { fontSize, borderWidth }`), the same shape as
 *   the 2D label; `echarts-gl` 2.x formats labels with the label API of ECharts 6.
 * - the `viewControl` of an example that only carries a commented-out projection is omitted instead
 *   of writing a no-op component.
 */

/**
 * Installs the GL charts and coordinate systems used by the examples of this file.
 *
 * Reading one of the installers also loads the `echarts-gl` core module, which registers the
 * post-update hook that paints the WebGL layer; without it `setOption` succeeds and the page stays
 * blank. `ECharts.use` deduplicates, so calling this once at startup is enough.
 */
internal fun useEChartsGl() {
    ECharts.use(
        arrayOf<EChartsExtensionInstaller>(
            Bar3DChart,
            Line3DChart,
            Lines3DChart,
            Scatter3DChart,
            SurfaceChart,
            Grid3DComponent,
            Geo3DComponent,
            GlobeComponent,
        ),
    )
}

private val gl = Category(Groups.GL, "examples/Gl.kt")

/** The `load` event of the picture of `image-surface-sushuang`. */
private val LOAD: EventType<Event> = EventType("load")

/** The eleven-colour palette the `visualMap` of the GL examples uses, dark blue to dark red. */
internal val glPalette =
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
    )

/** Hidden `visualMap` that colours a series by its third value, `min..max`. */
private fun glVisualMap(min: Double, max: Double): VisualMapOption =
    unsafeJso {
        show = false
        this.min = min
        this.max = max
        dimension = 2.0
        inRange = unsafeJso<VisualOptionUnit> { color = StringOrStringArray(glPalette) }
    }

/** Three value axes, the coordinate system of the surface and line pages. */
private fun EChartsGlOption.valueAxes() {
    xAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
    yAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
    zAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
}

/**
 * The `bar3D` items of the punch card: `[hour, day, commits]`.
 *
 * The source of the numbers is [punchCardValues], the same table the heatmap and the polar heatmap
 * expand; both 3D examples swap the first two coordinates so that hours run along the x axis.
 */
private fun punchBars(): Array<Bar3DDataItem?> =
    Array(punchCardValues.size * 24) { index ->
        val day = index / 24
        val hour = index % 24
        Bar3DDataItem(row(hour.toDouble(), day.toDouble(), punchCardValues[day][hour].toDouble()))
    }

/**
 * The `(1 + 0.25 cos 75t)` helix of `line3d-orthographic`: 25 turns of 1000 points each.
 */
private fun helixPoints(): Array<Line3DDataItem?> {
    val count = 25_000
    return Array(count) { index ->
        val t = index / 1000.0
        val radius = 1 + 0.25 * cos(75 * t)
        Line3DDataItem(row(radius * cos(t), radius * sin(t), t + 2.0 * sin(75 * t)))
    }
}

/**
 * `z = sin(pi x) sin(pi y)` of `simple-surface`.
 *
 * The original returns `'-'` around the origin to punch a hole into the sheet; `echarts-gl` skips a
 * quad as soon as one of its vertices is not a number, so the hole is a `NaN` here.
 */
private fun SurfaceEquationOption.sinProductSurface() {
    x = unsafeJso<SurfaceEquationDimensionOption> { step = 0.05 }
    y = unsafeJso<SurfaceEquationDimensionOption> { step = 0.05 }
    z =
        SurfaceFunction { x, y ->
            if (abs(x) < 0.1 && abs(y) < 0.1) Double.NaN else sin(x * PI) * sin(y * PI)
        }
}

/** `z = sin(x² + y²) x` over `-3..3` of `surface-wave`. */
private fun SurfaceEquationOption.rippledSurface() {
    x =
        unsafeJso<SurfaceEquationDimensionOption> {
            min = -3.0
            max = 3.0
            step = 0.05
        }
    y =
        unsafeJso<SurfaceEquationDimensionOption> {
            min = -3.0
            max = 3.0
            step = 0.05
        }
    z = SurfaceFunction { x, y -> sin(x * x + y * y) * x / PI }
}

/**
 * The surface and the per-vertex colours of `image-surface-sushuang`.
 *
 * Every pixel of the picture becomes one vertex `[column, height - row, luminance]`, where the
 * luminance `255 - (0.2125 r + 0.7154 g + 0.0721 b)` is shifted into `0..100`, and the vertex keeps
 * the RGB of its pixel as its colour, so the surface renders the photograph as a relief.
 *
 * Upstream inlines the picture as a base64 data URL; it is shipped as `data/img/gl/sushuang.jpg`
 * instead (see `data/SOURCE.md`).
 */
private fun sushuangOption(image: HTMLImageElement): EChartsGlOption {
    val width = image.width
    val height = image.height
    val canvas =
        document.createElement(HtmlTagName.canvas).apply {
            this.width = width
            this.height = height
        }
    val context =
        checkNotNull(canvas.getContext(CanvasRenderingContext2D.ID)) {
            "the image surface needs a 2d context"
        }
    context.drawImage(image.unsafeCast<CanvasImageSource>(), 0.0, 0.0, width.toDouble(), height.toDouble())
    val pixels = context.getImageData(0, 0, width, height).data
    val count = width * height
    val vertices = arrayOfNulls<SurfaceDataItem>(count)
    val colors = Array(count) { "" }
    for (index in 0 until count) {
        val red = pixels[index * 4].toInt() and 0xff
        val green = pixels[index * 4 + 1].toInt() and 0xff
        val blue = pixels[index * 4 + 2].toInt() and 0xff
        val luminance = 255 - (0.2125 * red + 0.7154 * green + 0.0721 * blue)
        vertices[index] =
            SurfaceDataItem(
                doubleRow((index % width).toDouble(), (height - index / width).toDouble(), (luminance - 125) / 10 + 50),
            )
        colors[index] = "rgb($red,$green,$blue)"
    }
    return unsafeJso {
        tooltip = one(unsafeJso<TooltipOption> {})
        backgroundColor = ZRColor("#fff")
        xAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
        yAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
        zAxis3D =
            one(
                unsafeJso<Axis3DOption> {
                    type = AxisTypes.value
                    min = AxisMinMax(0.0)
                    max = AxisMinMax(100.0)
                },
            )
        grid3D =
            one(
                unsafeJso<Grid3DOption> {
                    axisPointer = unsafeJso { show = false }
                    viewControl = unsafeJso { distance = 100.0 }
                    postEffect = unsafeJso<PostEffectOption> { enable = true }
                    light =
                        unsafeJso<LightOption> {
                            main = unsafeJso { shadow = true; intensity = 2.0 }
                            ambientCubemap =
                                unsafeJso<AmbientCubemapLightOption> {
                                    texture = "data/img/gl/canyon.hdr"
                                    exposure = 2.0
                                    diffuseIntensity = 0.2
                                    specularIntensity = 1.0
                                }
                        }
                },
            )
        series =
            one(
                surfaceSeries {
                    silent = true
                    wireframe = unsafeJso<SurfaceWireframeOption> { show = false }
                    typedItemStyle =
                        unsafeJso<GlItemStyleOption> {
                            color = GlColor(ZRColor { params -> colors[params.dataIndex] })
                        }
                    typedData = vertices
                },
            )
    }
}

/**
 * The `bar3D` items and the per-bar colours of `bar3d-myth`.
 *
 * Every pixel of the picture becomes one bar `[column, height - row, luminance]`, where the
 * luminance `0.2125 r + 0.7154 g + 0.0721 b` is shifted into `0..100`, and the bar keeps the RGB of
 * its pixel scaled by that luminance, so the bright parts of the picture glow. The colour callback
 * returns the channels as an `[r, g, b, a]` array, which only `echarts-gl`'s own `parseColor`
 * accepts.
 *
 * The original inlines the picture; it is shipped as `data/img/gl/sample.jpg` instead (see
 * `data/SOURCE.md`).
 */
private fun mythOption(image: HTMLImageElement): EChartsGlOption {
    val width = image.width
    val height = image.height
    val canvas =
        document.createElement(HtmlTagName.canvas).apply {
            this.width = width
            this.height = height
        }
    val context =
        checkNotNull(canvas.getContext(CanvasRenderingContext2D.ID)) {
            "the myth bars need a 2d context"
        }
    context.drawImage(image.unsafeCast<CanvasImageSource>(), 0.0, 0.0, width.toDouble(), height.toDouble())
    val pixels = context.getImageData(0, 0, width, height).data
    val count = width * height
    val bars = arrayOfNulls<Bar3DDataItem>(count)
    val colors = Array(count) { arrayOf(0.0, 0.0, 0.0, 1.0) }
    for (index in 0 until count) {
        val red = pixels[index * 4].toInt() and 0xff
        val green = pixels[index * 4 + 1].toInt() and 0xff
        val blue = pixels[index * 4 + 2].toInt() and 0xff
        val luminance = 0.2125 * red + 0.7154 * green + 0.0721 * blue
        bars[index] =
            Bar3DDataItem(
                doubleRow((index % width).toDouble(), (height - index / width).toDouble(), (luminance - 125) / 4 + 50),
            )
        val glow = luminance / 255 * 2
        colors[index] = arrayOf(red / 255.0 * glow, green / 255.0 * glow, blue / 255.0 * glow, 1.0)
    }
    return unsafeJso {
        tooltip = one(unsafeJso<TooltipOption> {})
        backgroundColor = ZRColor("#fff")
        xAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
        yAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
        zAxis3D =
            one(
                unsafeJso<Axis3DOption> {
                    type = AxisTypes.value
                    min = AxisMinMax(0.0)
                    max = AxisMinMax(100.0)
                },
            )
        grid3D =
            one(
                unsafeJso<Grid3DOption> {
                    show = false
                    boxDepth = 100.0
                    boxHeight = 20.0
                    environment = GlEnvironment("data/img/gl/starfield.jpg")
                    viewControl = unsafeJso { alpha = 70.0; beta = 0.0 }
                    postEffect =
                        unsafeJso<PostEffectOption> {
                            enable = true
                            depthOfField =
                                unsafeJso {
                                    enable = true
                                    blurRadius = 4.0
                                    fstop = 10.0
                                }
                        }
                    light =
                        unsafeJso<LightOption> {
                            main = unsafeJso { shadow = true; intensity = 2.0 }
                            ambientCubemap =
                                unsafeJso<AmbientCubemapLightOption> {
                                    texture = "data/img/gl/canyon.hdr"
                                    exposure = 2.0
                                    diffuseIntensity = 0.2
                                }
                        }
                },
            )
        series =
            one(
                bar3DSeries {
                    shading = ShadingTypes.lambert
                    barSize = DoubleOrDoubleArray(0.8)
                    silent = true
                    dimensions =
                        arrayOf(
                            DimensionDefinitionLoose("x"),
                            DimensionDefinitionLoose("y"),
                            DimensionDefinitionLoose("z"),
                        )
                    typedItemStyle = unsafeJso { color = GlColor { params -> colors[params.dataIndex] } }
                    typedData = bars
                },
            )
    }
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
        },
        gl.chart(
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
        },
        gl.interactive(
            "bar3d-dataset",
            "使用 dataset 为三维柱状图设置数据",
            "3D Bar with Dataset",
            "dataset 的列名同时当作三个坐标轴与配色维度：年份、国家、预期寿命与人口。",
            "The columns of a dataset feed all three axes at once: year, country, life expectancy " +
                "and population.",
        ) { chart ->
            loadJson("data/life-expectancy-table.json").then { json ->
                chart.setOption(
                    unsafeJso<EChartsGlOption> {
                        grid3D = one(unsafeJso<Grid3DOption> {})
                        tooltip = one(unsafeJso<TooltipOption> {})
                        xAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.category })
                        yAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.category })
                        zAxis3D = one(unsafeJso<Axis3DOption> {})
                        visualMap =
                            one(
                                unsafeJso<VisualMapOption> {
                                    max = 1e8
                                    typedDimension = DimensionLoose("Population")
                                },
                            )
                        dataset =
                            one(
                                unsafeJso<DatasetOption> {
                                    dimensions =
                                        arrayOf(
                                            DimensionDefinitionLoose("Income"),
                                            DimensionDefinitionLoose("Life Expectancy"),
                                            DimensionDefinitionLoose("Population"),
                                            DimensionDefinitionLoose("Country"),
                                            DimensionDefinitionLoose(
                                                unsafeJso<DimensionDefinition> {
                                                    name = "Year"
                                                    type = "ordinal"
                                                },
                                            ),
                                        )
                                    source = json.unsafeCast<DatasetSource>()
                                },
                            )
                        series =
                            one(
                                bar3DSeries {
                                    shading = ShadingTypes.lambert
                                    encode =
                                        unsafeJso {
                                            x = OptionEncodeValue(DimensionLoose("Year"))
                                            y = OptionEncodeValue(DimensionLoose("Country"))
                                            z = OptionEncodeValue(DimensionLoose("Life Expectancy"))
                                            tooltip =
                                                OptionEncodeValue(
                                                    arrayOf<DimensionLoose>(
                                                        DimensionLoose(0.0),
                                                        DimensionLoose(1.0),
                                                        DimensionLoose(2.0),
                                                        DimensionLoose(3.0),
                                                        DimensionLoose(4.0),
                                                    ),
                                                )
                                        }
                                },
                            )
                    },
                )
                null
            }
        },
        gl.chart(
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
        },
        gl.chart(
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
        },
        gl.chart(
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
        },
        gl.chart(
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
        },
        gl.interactive(
            "image-surface-sushuang",
            "Image Surface Sushuang",
            "Image Surface Sushuang",
            "把一张照片的每个像素当作曲面顶点：亮度决定高度，像素本身的颜色决定曲面颜色。",
            "Every pixel of a photograph becomes a surface vertex: its luminance is the height and " +
                "its colour is the colour of the surface.",
        ) { chart ->
            val picture = document.createElement(HtmlTagName.img)
            picture.listen(LOAD) { chart.setOption(sushuangOption(picture)) }
            picture.src = "data/img/gl/sushuang.jpg"
        },
        gl.interactive(
            "bar3d-myth",
            "Bar3D - 星云",
            "Bar3D - Myth",
            "把一张星云照片的每个像素变成一根细柱：高度取亮度并把视野压扁，柱子保留像素颜色，亮处像在发光。",
            "Every pixel of a nebula photograph becomes a thin bar: the luminance sets the height in a " +
                "squashed box, and the bars keep the colour of their pixel, so the bright parts glow.",
        ) { chart ->
            val picture = document.createElement(HtmlTagName.img)
            picture.listen(LOAD) { chart.setOption(mythOption(picture)) }
            picture.src = "data/img/gl/sample.jpg"
        },
    )
