package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.listen
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

/*
 * The realistic-shading pages of the `gl/` directory of apache/echarts-examples.
 *
 * Every page samples a parametric equation and lights it with `shading: "realistic"`, an ambient
 * cubemap (`data/img/gl/canyon.hdr` / `pisa.hdr`) and one or two post effects, so the shape only
 * shows up when the materials and the light are right.
 *
 * Upstream themes these pages for a dark background (`axisLine.color: '#fff'`, a white surface
 * colour, `saveAsImage.backgroundColor: '#111'`). The gallery has its own light/dark switch, so the
 * colours that only made sense on black are left out where they would erase the drawing; the
 * toolbox keeps the default save-as-image background for that reason.
 *
 * `surface-breather` upstream adds an "Export OBJ" toolbox button that calls
 * `echarts.exportGL2PLY`. That export module is gone from `echarts-gl` 2.1.0 (it is not in
 * `lib/export/`), so the button is dropped instead of porting a call that throws.
 *
 * `parametric-surface-rose` upstream sets `realisticMaterial.baseTexture`, which no material of
 * `echarts-gl` 2.1.0 reads (only `globe.baseTexture` is used), so the field is left out.
 */

private val glSurface = Category(Groups.GL, "examples/GlSurfaceMaterial.kt")

/** The `load` event of the height map image, which is what starts the leather page. */
private val LOAD: EventType<Event> = EventType("load")

/** The eleven-colour ramp of the `visualMap` components that shade the surfaces by height. */
private val surfaceRampColors =
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
private fun surfaceRamp(minValue: Double, maxValue: Double): VisualMapOption =
    unsafeJso {
        show = false
        dimension = 2.0
        min = minValue
        max = maxValue
        inRange = unsafeJso<VisualOptionUnit> { color = StringOrStringArray(surfaceRampColors) }
    }

/** Three value axes, the coordinate system of every surface page. */
private fun EChartsGlOption.surfaceAxes() {
    xAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
    yAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
    zAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
}

/** Environment light sampled from [url], with its diffuse and specular weights. */
private fun cubemap(
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
private fun realistic(
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
private fun realisticSurface(
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
private fun breatherEquation(): SurfaceParametricEquationOption {
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

private fun square(value: Double): Double = value * value

private fun cosh(value: Double): Double = (exp(value) + exp(-value)) / 2

private fun sinh(value: Double): Double = (exp(value) - exp(-value)) / 2

/** `a mod b` with a positive result, the `mod2` helper of the rose equations. */
private fun mod2(a: Double, b: Double): Double {
    val c = a % b
    return if (c > 0) c else c + b
}

/**
 * One point of the rose, `[radius, height]` of the spiral parametrised by `x1 ∈ [0, 1]` and
 * `theta ∈ [-20π/9, 15π]`.
 */
private class RoseShape(x1: Double, theta: Double) {
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
private fun roseEquation(dx: Double, dy: Double): SurfaceParametricEquationOption {
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
private fun roseBouquet(
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
private fun leatherScale(image: HTMLImageElement): (Double, Double) -> Double {
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
private const val LEATHER_TILING_U = 4.0
private const val LEATHER_TILING_V = 2.0

/** The leather sphere: `[sin v sin u, sin v cos u, cos v]` bulged by the height map. */
private fun leatherEquation(scale: (Double, Double) -> Double): SurfaceParametricEquationOption =
    unsafeJso {
        u = unsafeJso { min = -PI; max = PI; step = PI / 100 }
        v = unsafeJso { min = 0.4; max = PI - 0.4; step = PI / 100 }
        x = SurfaceFunction { uValue, vValue -> sin(vValue) * sin(uValue) * scale(uValue, vValue) }
        y = SurfaceFunction { uValue, vValue -> sin(vValue) * cos(uValue) * scale(uValue, vValue) }
        z = SurfaceFunction { uValue, vValue -> cos(vValue) * scale(uValue, vValue) }
    }

/** The `surface-leather` option; it is built once the height map has loaded. */
private fun leatherOption(scale: (Double, Double) -> Double): EChartsGlOption =
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

val glSurfaceMaterialEntries: List<CatalogEntry> =
    listOf(
        glSurface.chart(
            "metal-surface",
            "Metal Surface",
            "Metal Surface",
            "呼吸子曲面镀上一层金属：环境光取自峡谷全景图，泛光遮蔽让凹处变暗。",
            "The breather surface plated in metal: the canyon panorama lights it and screen-space " +
                "ambient occlusion darkens the folds.",
        ) {
            unsafeJso<EChartsGlOption> {
                tooltip = one(unsafeJso<TooltipOption> {})
                visualMap = one(surfaceRamp(-5.0, 5.0))
                surfaceAxes()
                grid3D =
                    one(
                        unsafeJso<Grid3DOption> {
                            show = false
                            postEffect =
                                unsafeJso<PostEffectOption> {
                                    enable = true
                                    screenSpaceAmbientOcclusion =
                                        unsafeJso {
                                            enable = true
                                            radius = 4.0
                                            quality = QualityTypes.high
                                            intensity = 1.5
                                        }
                                }
                            temporalSuperSampling = unsafeJso { enable = BooleanOrAuto(true) }
                            light =
                                unsafeJso<LightOption> {
                                    main = unsafeJso { intensity = 2.0; shadow = true }
                                    ambient = unsafeJso { intensity = 0.0 }
                                    ambientCubemap = cubemap("data/img/gl/canyon.hdr", 2.0, 0.2, 3.0)
                                }
                        },
                    )
                series = one(realisticSurface(breatherEquation(), realistic(0.2, 1.0)))
            }
        },
        glSurface.chart(
            "surface-breather",
            "Breather",
            "Breather",
            "呼吸子曲面：可见的坐标轴、青色到红色的高度色带，金属度为 0 的哑光表面。",
            "The breather surface with visible axes and a blue-to-red height ramp on a matte, " +
                "non-metallic material.",
        ) {
            unsafeJso<EChartsGlOption> {
                tooltip = one(unsafeJso<TooltipOption> {})
                visualMap = one(surfaceRamp(-3.0, 3.0))
                surfaceAxes()
                grid3D =
                    one(
                        unsafeJso<Grid3DOption> {
                            show = true
                            postEffect =
                                unsafeJso<PostEffectOption> {
                                    enable = true
                                    screenSpaceAmbientOcclusion = unsafeJso { enable = true; radius = 4.0 }
                                }
                            viewControl = unsafeJso { distance = 130.0; beta = 50.0 }
                            light =
                                unsafeJso<LightOption> {
                                    main = unsafeJso { intensity = 2.0; shadow = true }
                                    ambient = unsafeJso { intensity = 0.0 }
                                    ambientCubemap = cubemap("data/img/gl/canyon.hdr", 2.0, 0.2, 1.0)
                                }
                        },
                    )
                series = one(realisticSurface(breatherEquation(), realistic(0.3, 0.0), silent = false))
            }
        },
        glSurface.chart(
            "surface-mollusc-shell",
            "Mollusc Shell",
            "Mollusc Shell",
            "软体动物的螺壳：一条指数螺线绕成壳，高度色带从深蓝渐变到暗红。",
            "A mollusc shell: an exponential spiral coiled into a shell, shaded by a blue-to-red " +
                "height ramp.",
        ) {
            unsafeJso<EChartsGlOption> {
                tooltip = one(unsafeJso<TooltipOption> {})
                visualMap = one(surfaceRamp(-5.0, 0.0))
                surfaceAxes()
                grid3D =
                    one(
                        unsafeJso<Grid3DOption> {
                            show = true
                            postEffect = unsafeJso<PostEffectOption> { enable = true }
                            temporalSuperSampling = unsafeJso { enable = BooleanOrAuto(true) }
                            light =
                                unsafeJso<LightOption> {
                                    main = unsafeJso { intensity = 3.0; shadow = true }
                                    ambient = unsafeJso { intensity = 0.0 }
                                    ambientCubemap = cubemap("data/img/gl/canyon.hdr", 2.0, 1.0, 1.0)
                                }
                        },
                    )
                series =
                    one(
                        realisticSurface(
                            unsafeJso {
                                u = unsafeJso { min = -PI; max = PI; step = PI / 40 }
                                v = unsafeJso { min = -15.0; max = 6.0; step = 0.21 }
                                x =
                                    SurfaceFunction { uValue, vValue ->
                                        pow(1.16, vValue) * cos(vValue) * (1 + cos(uValue))
                                    }
                                y =
                                    SurfaceFunction { uValue, vValue ->
                                        -pow(1.16, vValue) * sin(vValue) * (1 + cos(uValue))
                                    }
                                z =
                                    SurfaceFunction { uValue, vValue ->
                                        -2 * pow(1.16, vValue) * (1 + sin(uValue))
                                    }
                            },
                            realistic(0.4, 0.0),
                            silent = false,
                        ),
                    )
            }
        },
        glSurface.chart(
            "surface-golden-rose",
            "Golden Rose",
            "Golden Rose",
            "五朵金色玫瑰拼成一束：金属度为 1 的镀金材质，焦外虚化让远景略微模糊。",
            "Five golden roses in one bunch: a fully metallic gold material, softened by a shallow " +
                "depth of field.",
        ) {
            unsafeJso<EChartsGlOption> {
                toolbox = toolbox(saveAsImage = true)
                surfaceAxes()
                grid3D =
                    one(
                        unsafeJso<Grid3DOption> {
                            show = false
                            boxWidth = 200.0
                            boxDepth = 200.0
                            axisPointer = unsafeJso { show = false }
                            postEffect =
                                unsafeJso<PostEffectOption> {
                                    enable = true
                                    screenSpaceAmbientOcclusion = unsafeJso {
                                        enable = true
                                        radius = 10.0
                                        intensity = 1.0
                                    }
                                    depthOfField = unsafeJso { enable = true }
                                }
                            temporalSuperSampling = unsafeJso { enable = BooleanOrAuto(true) }
                            light =
                                unsafeJso<LightOption> {
                                    main = unsafeJso { intensity = 1.0; shadow = true }
                                    ambient = unsafeJso { intensity = 0.0 }
                                    ambientCubemap = cubemap("data/img/gl/pisa.hdr", 1.0, 1.0, 1.0)
                                }
                            viewControl = unsafeJso {}
                        },
                    )
                series =
                    roseBouquet(
                        realistic(0.3, 1.0),
                        unsafeJso { color = GlColor(arrayOf(2.0, 1.5, 0.2)) },
                    )
            }
        },
        glSurface.chart(
            "surface-theme-roses",
            "Theme Roses",
            "Theme Roses",
            "同一束玫瑰换成哑光的默认配色，纹理平铺 200×20 次。",
            "The same bouquet in the default matte palette, with the material textures tiled " +
                "200×20 times.",
        ) {
            unsafeJso<EChartsGlOption> {
                toolbox = toolbox(saveAsImage = true)
                surfaceAxes()
                grid3D =
                    one(
                        unsafeJso<Grid3DOption> {
                            show = false
                            boxWidth = 200.0
                            boxDepth = 200.0
                            axisPointer = unsafeJso { show = false }
                            postEffect =
                                unsafeJso<PostEffectOption> {
                                    enable = true
                                    screenSpaceAmbientOcclusion = unsafeJso {
                                        enable = true
                                        radius = 10.0
                                        intensity = 1.0
                                    }
                                }
                            temporalSuperSampling = unsafeJso { enable = BooleanOrAuto(true) }
                            light =
                                unsafeJso<LightOption> {
                                    main = unsafeJso { intensity = 1.0; shadow = true }
                                    ambient = unsafeJso { intensity = 0.0 }
                                    ambientCubemap = cubemap("data/img/gl/pisa.hdr", 1.0, 1.0, 1.0)
                                }
                        },
                    )
                series = roseBouquet(realistic(0.7, 0.0, arrayOf(200.0, 20.0)), null)
            }
        },
        glSurface.chart(
            "parametric-surface-rose",
            "Parametric Surface Rose",
            "Parametric Surface Rose",
            "单朵白色玫瑰：边缘描线加泛光遮蔽，在暗色环境光下勾出花瓣的轮廓。",
            "A single white rose: the edge pass and ambient occlusion trace the outline of the petals.",
        ) {
            unsafeJso<EChartsGlOption> {
                toolbox = toolbox(saveAsImage = true)
                surfaceAxes()
                grid3D =
                    one(
                        unsafeJso<Grid3DOption> {
                            show = false
                            axisPointer = unsafeJso { show = false }
                            postEffect =
                                unsafeJso<PostEffectOption> {
                                    enable = true
                                    ssao = unsafeJso { enable = true; radius = 10.0; intensity = 2.0 }
                                    edge = unsafeJso { enable = true }
                                }
                            temporalSuperSampling = unsafeJso { enable = BooleanOrAuto(true) }
                            light =
                                unsafeJso<LightOption> {
                                    main = unsafeJso { intensity = 3.0; shadow = true }
                                    ambient = unsafeJso { intensity = 0.0 }
                                    ambientCubemap = cubemap("data/img/gl/canyon.hdr", 0.0, 1.0, 0.5)
                                }
                        },
                    )
                series =
                    one(
                        realisticSurface(
                            roseEquation(0.0, 0.0),
                            realistic(0.7, 0.0, arrayOf(200.0, 20.0)),
                        ).apply { typedItemStyle = unsafeJso { color = GlColor(ZRColor("#fff")) } },
                    )
            }
        },
        glSurface.interactive(
            "surface-leather",
            "皮革材质",
            "Leather Material",
            "皮革贴图压出凹凸的球体：高度图改变顶点位置，粗糙度与法线贴图补上细纹。",
            "A leather ball: a height map displaces the vertices and the roughness and normal maps " +
                "add the grain.",
        ) { chart ->
            val heightMap = document.createElement(HtmlTagName.img)
            heightMap.listen(LOAD) { chart.setOption(leatherOption(leatherScale(heightMap))) }
            heightMap.src = "data/img/gl/leather/leather_height.jpg"
        },
    )

/** `a^b` of the shell equation. */
private fun pow(base: Double, exponent: Double): Double = exp(exponent * ln(base))
