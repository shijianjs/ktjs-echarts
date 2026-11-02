package demo.examples.gl

import echarts.AxisTypes
import echarts.ECharts
import echarts.EChartsExtensionInstaller
import echarts.options.*
import echartsgl.Bar3DChart
import echartsgl.Geo3DComponent
import echartsgl.GraphGLChart
import echartsgl.GlColor
import echartsgl.GlEnvironment
import echartsgl.GlobeComponent
import echartsgl.Grid3DComponent
import echartsgl.Line3DChart
import echartsgl.Lines3DChart
import echartsgl.LinesGLChart
import echartsgl.Map3DChart
import echartsgl.Scatter3DChart
import echartsgl.ScatterGLChart
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
import demo.examples.chord.column
import demo.examples.custom.punchCardValues
import demo.examples.lines.coordinate
import demo.examples.matrix.dimension
import demo.examples.shared.doubleRow
import demo.examples.shared.row
import demo.examples.sunburst.radius

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
            Map3DChart,
            GraphGLChart,
            LinesGLChart,
            ScatterGLChart,
            Grid3DComponent,
            Geo3DComponent,
            GlobeComponent,
        ),
    )
}

/** The `load` event of the picture of `image-surface-sushuang`. */
internal val LOAD: EventType<Event> = EventType("load")

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
internal fun glVisualMap(min: Double, max: Double): VisualMapOption =
    unsafeJso {
        show = false
        this.min = min
        this.max = max
        dimension = 2.0
        inRange = unsafeJso<VisualOptionUnit> { color = StringOrStringArray(glPalette) }
    }

/** Three value axes, the coordinate system of the surface and line pages. */
internal fun EChartsGlOption.valueAxes() {
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
internal fun punchBars(): Array<Bar3DDataItem?> =
    Array(punchCardValues.size * 24) { index ->
        val day = index / 24
        val hour = index % 24
        Bar3DDataItem(row(hour.toDouble(), day.toDouble(), punchCardValues[day][hour].toDouble()))
    }

/**
 * The `(1 + 0.25 cos 75t)` helix of `line3d-orthographic`: 25 turns of 1000 points each.
 */
internal fun helixPoints(): Array<Line3DDataItem?> {
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
internal fun SurfaceEquationOption.sinProductSurface() {
    x = unsafeJso<SurfaceEquationDimensionOption> { step = 0.05 }
    y = unsafeJso<SurfaceEquationDimensionOption> { step = 0.05 }
    z =
        SurfaceFunction { x, y ->
            if (abs(x) < 0.1 && abs(y) < 0.1) Double.NaN else sin(x * PI) * sin(y * PI)
        }
}

/** `z = sin(x² + y²) x` over `-3..3` of `surface-wave`. */
internal fun SurfaceEquationOption.rippledSurface() {
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
internal fun sushuangOption(image: HTMLImageElement): EChartsGlOption {
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
internal fun mythOption(image: HTMLImageElement): EChartsGlOption {
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
