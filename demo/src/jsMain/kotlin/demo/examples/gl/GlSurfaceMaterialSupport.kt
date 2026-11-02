package demo.examples.gl

import echarts.AxisTypes
import echarts.options.*
import echartsgl.BooleanOrAuto
import echartsgl.GlColor
import echartsgl.GlEnvironment
import echartsgl.NumberOrTexture
import echartsgl.QualityTypes
import echartsgl.ShadingTypes
import echartsgl.TextureSource
import echartsgl.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.sin
import kotlin.math.sqrt
import web.canvas.ID
import web.canvas.CanvasImageSource
import web.canvas.CanvasRenderingContext2D
import web.dom.document
import web.events.Event
import web.events.EventType
import web.html.HTMLImageElement
import web.html.HtmlTagName
import zrender.ZRColor
import demo.examples.chord.column
import demo.examples.lines.coordinate
import demo.examples.matrix.dimension
import demo.examples.scatter.point
import demo.examples.shared.every
import demo.examples.shared.row
import demo.examples.sunburst.radius

/** The `load` event of the height map image, which is what starts the leather page. */
internal val LOADGlSurfaceMaterial: EventType<Event> = EventType("load")

/** The eleven-colour ramp of the `visualMap` components that shade the surfaces by height. */
internal val surfaceRampColors =
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

/** Hidden `visualMap` that colours the surface by its third value, `minValue..maxValue`. */
internal fun surfaceRamp(minValue: Double, maxValue: Double): VisualMapOption =
    unsafeJso {
        show = false
        dimension = 2.0
        min = minValue
        max = maxValue
        inRange = unsafeJso<VisualOptionUnit> { color = StringOrStringArray(surfaceRampColors) }
    }

/** Three value axes, the coordinate system of every surface page. */
internal fun EChartsGlOption.surfaceAxes() {
    xAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
    yAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
    zAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
}

/** Environment light sampled from [url], with its diffuse and specular weights. */
internal fun cubemap(
    url: String,
    exposureValue: Double,
    diffuse: Double,
    specular: Double,
): AmbientCubemapLightOption =
    unsafeJso {
        texture = url
        exposure = exposureValue
        diffuseIntensity = diffuse
        specularIntensity = specular
    }

/** Physically based material with the two channels every page sets; [tiling] tiles its textures. */
internal fun realistic(
    roughnessValue: Double,
    metalnessValue: Double,
    tiling: Array<Double>? = null,
): RealisticMaterialOption =
    unsafeJso {
        roughness = NumberOrTexture(roughnessValue)
        metalness = NumberOrTexture(metalnessValue)
        if (tiling != null) textureTiling = DoubleOrDoubleArray(tiling)
    }

/** Wireframe-less parametric surface with a realistic material; the pages that show a tooltip pass [silent] `false`. */
internal fun realisticSurface(
    equation: SurfaceParametricEquationOption,
    material: RealisticMaterialOption,
    silent: Boolean = true,
): SurfaceSeriesOption =
    surfaceSeries {
        parametric = true
        this.silent = silent
        wireframe = unsafeJso { show = false }
        shading = ShadingTypes.realistic
        realisticMaterial = material
        parametricEquation = equation
    }

/**
 * The breather surface shared by `metal-surface` and `surface-breather`.
 *
 * `a = 0.4`, `r = 1 - a²`, `w = √r`; the three coordinates are the Bryant–Griffiths breather.
 */
internal fun breatherEquation(): SurfaceParametricEquationOption {
    val a = 0.4
    val r = 1 - a * a
    val w = sqrt(r)
    fun denominator(u: Double, v: Double): Double = a * (w * cosh(a * u) * (w * cosh(a * u)) + a * square(sin(w * v)))
    return unsafeJso {
        u = unsafeJso { min = -13.2; max = 13.2; step = 0.2 }
        v = unsafeJso { min = -37.4; max = 37.4; step = 0.2 }
        x =
            SurfaceFunction { uValue, vValue ->
                -uValue + (2 * r * cosh(a * uValue) * sinh(a * uValue)) / denominator(uValue, vValue)
            }
        y =
            SurfaceFunction { uValue, vValue ->
                val denominator = denominator(uValue, vValue)
                (2 * w * cosh(a * uValue) * (-(w * cos(vValue) * cos(w * vValue)) - sin(vValue) * sin(w * vValue))) /
                    denominator
            }
        z =
            SurfaceFunction { uValue, vValue ->
                val denominator = denominator(uValue, vValue)
                (2 * w * cosh(a * uValue) * (-(w * sin(vValue) * cos(w * vValue)) + cos(vValue) * sin(w * vValue))) /
                    denominator
            }
    }
}

internal fun square(value: Double): Double = value * value

internal fun cosh(value: Double): Double = (exp(value) + exp(-value)) / 2

internal fun sinh(value: Double): Double = (exp(value) - exp(-value)) / 2

/** `a mod b` with a positive result, the `mod2` helper of the rose equations. */
internal fun mod2(a: Double, b: Double): Double {
    val c = a % b
    return if (c > 0) c else c + b
}

/**
 * One point of the rose, `[radius, height]` of the spiral parametrised by `x1 ∈ [0, 1]` and
 * `theta ∈ [-20π/9, 15π]`.
 */
internal class RoseShape(x1: Double, theta: Double) {
    private val phi = (PI / 2) * exp(-theta / (8 * PI))

    private val y1 =
        1.9565284531299512 * square(x1) * square(1.2768869870150188 * x1 - 1) * sin(phi)

    private val bulk = 1 - square(1.25 * square(1 - mod2(3.6 * theta, 2 * PI) / PI) - 0.25) / 2

    /** Distance of the point from the z axis. */
    val radius = bulk * (x1 * sin(phi) + y1 * cos(phi))

    /** Height of the point, the z coordinate of the rose. */
    val height = bulk * (x1 * cos(phi) - y1 * sin(phi))
}

/**
 * The rose surface of `surface-golden-rose`, `surface-theme-roses` and `parametric-surface-rose`,
 * shifted by `[dx, dy]` so that five of them form a bouquet.
 */
internal fun roseEquation(dx: Double, dy: Double): SurfaceParametricEquationOption {
    val thetaFrom = -(20.0 / 9.0) * PI
    val thetaTo = 15.0 * PI
    return unsafeJso {
        u = unsafeJso { min = 0.0; max = 1.0; step = 1.0 / 24.0 }
        v = unsafeJso {
            min = thetaFrom
            max = thetaTo
            step = (thetaTo - thetaFrom) / 575.0
        }
        x = SurfaceFunction { x1, theta -> RoseShape(x1, theta).radius * sin(theta) + dx }
        y = SurfaceFunction { x1, theta -> RoseShape(x1, theta).radius * cos(theta) + dy }
        z = SurfaceFunction { x1, theta -> RoseShape(x1, theta).height }
    }
}

/** The five shifted roses of the two bouquet pages. */
internal fun roseBouquet(
    material: RealisticMaterialOption,
    item: GlItemStyleOption?,
): Array<SeriesOption> {
    val shifts = arrayOf(1.5 to 1.5, -1.5 to -1.5, -1.5 to 1.5, 1.5 to -1.5, 0.0 to 0.0)
    return Array(shifts.size) { index ->
        val (dx, dy) = shifts[index]
        realisticSurface(roseEquation(dx, dy), material).apply {
            if (item != null) typedItemStyle = item
        }
    }
}

/**
 * The height-map sampler of `surface-leather`.
 *
 * Upstream draws `leather_height.jpg` into a canvas, reads its red channel back and bulges every
 * surface point by up to 5% (`1 + channel / 255 / 20`) depending on where the point falls in the
 * tiled image, which turns the smooth sphere into tooled leather.
 */
internal fun leatherScale(image: HTMLImageElement): (Double, Double) -> Double {
    val width = image.width
    val height = image.height
    val canvas =
        document.createElement(HtmlTagName.canvas).apply {
            this.width = width
            this.height = height
        }
    val context =
        checkNotNull(canvas.getContext(CanvasRenderingContext2D.ID)) {
            "the leather height map needs a 2d context"
        }
    context.drawImage(image.unsafeCast<web.canvas.CanvasImageSource>(), 0.0, 0.0, width.toDouble(), height.toDouble())
    val pixels = context.getImageData(0, 0, width, height).data
    return { u, v ->
        var column = ((u / PI) * 0.5 + 0.5) * LEATHER_TILING_U
        var row = (v / PI) * LEATHER_TILING_V
        column = floor((column - floor(column)) * (width - 1))
        row = floor((1 - row + floor(row)) * (height - 1))
        val index = (row * width + column).toInt()
        1 + (pixels[index * 4].toInt() and 0xff) / 255.0 / 20
    }
}

/** The `[4, 2]` tiling of the leather textures, in upstream `TILING` order. */
internal const val LEATHER_TILING_U = 4.0

internal const val LEATHER_TILING_V = 2.0

/** The leather sphere: `[sin v sin u, sin v cos u, cos v]` bulged by the height map. */
internal fun leatherEquation(scale: (Double, Double) -> Double): SurfaceParametricEquationOption =
    unsafeJso {
        u = unsafeJso { min = -PI; max = PI; step = PI / 100 }
        v = unsafeJso { min = 0.4; max = PI - 0.4; step = PI / 100 }
        x = SurfaceFunction { uValue, vValue -> sin(vValue) * sin(uValue) * scale(uValue, vValue) }
        y = SurfaceFunction { uValue, vValue -> sin(vValue) * cos(uValue) * scale(uValue, vValue) }
        z = SurfaceFunction { uValue, vValue -> cos(vValue) * scale(uValue, vValue) }
    }

/** The `surface-leather` option; it is built once the height map has loaded. */
internal fun leatherOption(scale: (Double, Double) -> Double): EChartsGlOption =
    unsafeJso {
        xAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value; min = AxisMinMax(-1.5); max = AxisMinMax(1.5) })
        yAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value; min = AxisMinMax(-1.5); max = AxisMinMax(1.5) })
        zAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value; min = AxisMinMax(-1.5); max = AxisMinMax(1.5) })
        grid3D =
            one(
                unsafeJso<Grid3DOption> {
                    show = false
                    environment = GlEnvironment("none")
                    axisPointer = unsafeJso { show = false }
                    postEffect =
                        unsafeJso<PostEffectOption> {
                            enable = true
                            screenSpaceAmbientOcclusion =
                                unsafeJso {
                                    enable = true
                                    radius = 10.0
                                    intensity = 2.0
                                    quality = QualityTypes.high
                                }
                        }
                    temporalSuperSampling = unsafeJso { enable = BooleanOrAuto(true) }
                    light =
                        unsafeJso<LightOption> {
                            main = unsafeJso { intensity = 2.0; shadow = true }
                            ambient = unsafeJso { intensity = 0.0 }
                            ambientCubemap = cubemap("data/img/gl/pisa.hdr", 1.0, 1.0, 2.0)
                        }
                    viewControl = unsafeJso { distance = 80.0 }
                },
            )
        series =
            one(
                realisticSurface(
                    leatherEquation(scale),
                    unsafeJso<RealisticMaterialOption> {
                        roughness = NumberOrTexture("data/img/gl/leather/leather_roughness.jpg")
                        normalTexture = TextureSource("data/img/gl/leather/leather_normal.jpg")
                        textureTiling = DoubleOrDoubleArray(arrayOf(LEATHER_TILING_U, LEATHER_TILING_V))
                    },
                ).apply { typedItemStyle = unsafeJso { color = GlColor(ZRColor("#300")) } },
            )
    }

/** `a^b` of the shell equation. */
internal fun pow(base: Double, exponent: Double): Double = exp(exponent * ln(base))
