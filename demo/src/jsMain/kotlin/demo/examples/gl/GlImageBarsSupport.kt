package demo.examples.gl

import io.github.shijianjs.ktjs.echarts.AxisTypes
import io.github.shijianjs.ktjs.echarts.options.AxisMinMax
import io.github.shijianjs.ktjs.echarts.options.DimensionDefinitionLoose
import io.github.shijianjs.ktjs.echarts.options.DoubleOrDoubleArray
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.gl.GlColor
import io.github.shijianjs.ktjs.echarts.gl.GlEnvironment
import io.github.shijianjs.ktjs.echarts.gl.NumberOrTexture
import io.github.shijianjs.ktjs.echarts.gl.ProjectionTypes
import io.github.shijianjs.ktjs.echarts.gl.QualityTypes
import io.github.shijianjs.ktjs.echarts.gl.ShadingTypes
import io.github.shijianjs.ktjs.echarts.gl.options.AmbientCubemapLightOption
import io.github.shijianjs.ktjs.echarts.gl.options.Axis3DOption
import io.github.shijianjs.ktjs.echarts.gl.options.Bar3DDataItem
import io.github.shijianjs.ktjs.echarts.gl.options.EChartsGlOption
import io.github.shijianjs.ktjs.echarts.gl.options.Grid3DOption
import io.github.shijianjs.ktjs.echarts.gl.options.LightOption
import io.github.shijianjs.ktjs.echarts.gl.options.PostEffectOption
import io.github.shijianjs.ktjs.echarts.gl.options.RealisticMaterialOption
import io.github.shijianjs.ktjs.echarts.gl.options.bar3DSeries
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.math.roundToInt
import web.canvas.CanvasImageSource
import web.canvas.CanvasRenderingContext2D
import web.canvas.ID
import web.dom.document
import web.events.Event
import web.events.EventType
import web.html.HTMLImageElement
import web.html.HtmlTagName
import web.images.ImageData
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.shared.doubleRow
import demo.examples.shared.every
import demo.examples.sunburst.radius

/** `load` as a typed event. */
internal val LOADGlImageBars: EventType<Event> = EventType("load")

/** The RGBA pixels of [image] drawn into a canvas of [width] × [height]. */
internal fun pixelsOf(image: HTMLImageElement, width: Int, height: Int): ImageData {
    val canvas =
        document.createElement(HtmlTagName.canvas).apply {
            this.width = width
            this.height = height
        }
    val context =
        checkNotNull(canvas.getContext(CanvasRenderingContext2D.ID)) {
            "the pixel bars need a 2d context"
        }
    context.drawImage(image.unsafeCast<CanvasImageSource>(), 0.0, 0.0, width.toDouble(), height.toDouble())
    return context.getImageData(0, 0, width, height)
}

/** `red`, `green` and `blue` of the pixel at [index] of [pixels], each in `0..255`. */
internal fun channel(pixels: ImageData, index: Int): Int = pixels.data[index].toInt() and 0xff

/** Luminance of the pixel at [index] of [pixels], the weight the original uses. */
internal fun luminance(pixels: ImageData, index: Int): Double =
    0.2125 * channel(pixels, index * 4) + 0.7154 * channel(pixels, index * 4 + 1) +
        0.0721 * channel(pixels, index * 4 + 2)

/**
 * The `bar3D` items and the per-bar colors of `image-to-bar3d`.
 *
 * The picture is halved before it is sampled and its luminance is inverted, so the dark features of
 * the portrait stand out of the white background. Each color callback returns
 * `rgb(red, green, blue)` of the pixel it stands for.
 */
internal fun imageToBar3DOption(image: HTMLImageElement): EChartsGlOption {
    val width = image.width / 2
    val height = image.height / 2
    val pixels = pixelsOf(image, width, height)
    val count = width * height
    val bars = arrayOfNulls<Bar3DDataItem>(count)
    for (index in 0 until count) {
        val lum = 255 - luminance(pixels, index)
        bars[index] =
            Bar3DDataItem(
                doubleRow(
                    (index % width).toDouble(),
                    (height - index / width).toDouble(),
                    (lum - 125) / 20 + 50,
                ),
            )
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
                    boxDepth = 120.0
                    viewControl = unsafeJso { alpha = 20.0; beta = -30.0 }
                    postEffect = unsafeJso<PostEffectOption> { enable = true; ssao = unsafeJso { enable = true } }
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
                bar3DSeries {
                    shading = ShadingTypes.realistic
                    barSize = DoubleOrDoubleArray(1.0)
                    typedItemStyle =
                        unsafeJso {
                            color =
                                GlColor(
                                    ZRColor { params ->
                                        val index = params.dataIndex * 4
                                        "rgb(" +
                                            listOf(index, index + 1, index + 2)
                                                .joinToString(",") { channel(pixels, it).toString() } +
                                            ")"
                                    },
                                )
                        }
                    typedData = bars
                },
            )
    }
}

/**
 * The `bar3D` items and the per-bar colors of `bar3d-voxelize-image`.
 *
 * The picture is scaled down to at most 80 rows (`barNumber`), the luminance is multiplied by 0.3
 * around its midpoint, and every bar keeps the RGB of its pixel scaled by `luminance * 1.2`: the
 * channels of the bright parts reach past `1`, which is the HDR array form of `GlColor`.
 */
internal fun voxelizeOption(image: HTMLImageElement): EChartsGlOption {
    val height = minOf(80, image.height)
    val width = (height * (image.width.toDouble() / image.height)).roundToInt()
    val pixels = pixelsOf(image, width, height)
    val count = width * height
    val bars = arrayOfNulls<Bar3DDataItem>(count)
    val colors = Array(count) { arrayOf(0.0, 0.0, 0.0, 1.0) }
    for (index in 0 until count) {
        bars[index] =
            Bar3DDataItem(
                doubleRow(
                    (index % width).toDouble(),
                    (height - index / width).toDouble(),
                    (luminance(pixels, index) - 125) * 0.3 + 50,
                ),
            )
        val glow = luminance(pixels, index) / 255 * 1.2
        colors[index] =
            arrayOf(
                channel(pixels, index * 4) / 255.0 * glow,
                channel(pixels, index * 4 + 1) / 255.0 * glow,
                channel(pixels, index * 4 + 2) / 255.0 * glow,
                1.0,
            )
    }
    return unsafeJso {
        tooltip = one(unsafeJso<TooltipOption> {})
        backgroundColor = ZRColor("#000")
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
                    boxWidth = (100.0 / height) * width
                    boxDepth = 100.0
                    boxHeight = 20.0
                    environment = GlEnvironment("none")
                    viewControl =
                        unsafeJso {
                            projection = ProjectionTypes.perspective
                            alpha = 45.0
                            beta = -45.0
                            panSensitivity = DoubleOrDoubleArray(1.0)
                            rotateSensitivity = DoubleOrDoubleArray(1.0)
                            damping = 0.9
                            distance = 60.0
                        }
                    postEffect =
                        unsafeJso<PostEffectOption> {
                            enable = true
                            bloom = unsafeJso { intensity = 0.2 }
                            ssao =
                                unsafeJso {
                                    enable = true
                                    intensity = 1.5
                                    radius = 5.0
                                    quality = QualityTypes.high
                                }
                            dof =
                                unsafeJso {
                                    enable = true
                                    blurRadius = 4.0
                                    fstop = 10.0
                                    focalDistance = 55.0
                                }
                        }
                    light =
                        unsafeJso<LightOption> {
                            main = unsafeJso { shadow = true; intensity = 2.0 }
                            ambientCubemap =
                                unsafeJso<AmbientCubemapLightOption> {
                                    texture = "data/img/gl/pisa.hdr"
                                    exposure = 2.0
                                    diffuseIntensity = 0.2
                                    specularIntensity = 1.5
                                }
                        }
                },
            )
        series =
            one(
                bar3DSeries {
                    animation = false
                    shading = ShadingTypes.realistic
                    realisticMaterial =
                        unsafeJso<RealisticMaterialOption> {
                            roughness = NumberOrTexture(0.0)
                            metalness = NumberOrTexture(1.0)
                        }
                    barSize = DoubleOrDoubleArray(1.2)
                    bevelSize = 0.18
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
