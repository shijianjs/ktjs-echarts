package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.listen
import echarts.AxisTypes
import echarts.options.AxisMinMax
import echarts.options.DimensionDefinitionLoose
import echarts.options.DoubleOrDoubleArray
import echarts.options.TooltipOption
import echarts.options.one
import echartsgl.GlColor
import echartsgl.GlEnvironment
import echartsgl.NumberOrTexture
import echartsgl.ProjectionTypes
import echartsgl.QualityTypes
import echartsgl.ShadingTypes
import echartsgl.options.AmbientCubemapLightOption
import echartsgl.options.Axis3DOption
import echartsgl.options.Bar3DDataItem
import echartsgl.options.EChartsGlOption
import echartsgl.options.Grid3DOption
import echartsgl.options.LightOption
import echartsgl.options.PostEffectOption
import echartsgl.options.RealisticMaterialOption
import echartsgl.options.bar3DSeries
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
import zrender.ZRColor

/*
 * The two `bar3D` pages that rebuild a picture out of one bar per pixel.
 *
 * `image-to-bar3d` inverts the luminance into the bar height and colors each bar with the plain RGB
 * of its pixel. `bar3d-voxelize-image` downsamples the picture to 80 rows, lifts the bars out of a
 * reflective floor and colors them by `rgb * luminance * 1.2`, which exceeds `1` in the bright
 * parts: its color callback therefore returns an `[r, g, b, 1]` array, not a string.
 *
 * Two fields of the originals are left out. `image-to-bar3d` sets
 * `series.wireframe: { show: false }`, which `bar3D` does not read at all (`wireframe` belongs to
 * `surface`, `line3D`, `geo3D` and `globe`). `bar3d-voxelize-image` enables
 * `postEffect.screenSpaceReflection`, but with `echarts-gl` 2.1.0 that pass paints an entirely black
 * frame in Chrome, so the page keeps bloom, SSAO and depth of field and drops the reflection.
 *
 * Both pictures ship as static files (`data/img/gl/portrait.jpg` and `bitcoin.png`); the originals
 * inline the first as base64 and read the second from `data-gl/asset/`.
 */

private val glImageBars = Category(Groups.GL, "examples/GlImageBars.kt")

/** `load` as a typed event. */
private val LOAD: EventType<Event> = EventType("load")

/** The RGBA pixels of [image] drawn into a canvas of [width] × [height]. */
private fun pixelsOf(image: HTMLImageElement, width: Int, height: Int): ImageData {
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
private fun channel(pixels: ImageData, index: Int): Int = pixels.data[index].toInt() and 0xff

/** Luminance of the pixel at [index] of [pixels], the weight the original uses. */
private fun luminance(pixels: ImageData, index: Int): Double =
    0.2125 * channel(pixels, index * 4) + 0.7154 * channel(pixels, index * 4 + 1) +
        0.0721 * channel(pixels, index * 4 + 2)

/**
 * The `bar3D` items and the per-bar colors of `image-to-bar3d`.
 *
 * The picture is halved before it is sampled and its luminance is inverted, so the dark features of
 * the portrait stand out of the white background. Each color callback returns
 * `rgb(red, green, blue)` of the pixel it stands for.
 */
private fun imageToBar3DOption(image: HTMLImageElement): EChartsGlOption {
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
private fun voxelizeOption(image: HTMLImageElement): EChartsGlOption {
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

val glImageBarsEntries: List<CatalogEntry> =
    listOf(
        glImageBars.interactive(
            "image-to-bar3d",
            "图像转三维柱状图",
            "Image to Bar3D",
            "把一张肖像的亮度反过来变成柱高：深色头发立得最高，柱子的颜色就是它所在像素的颜色。",
            "The luminance of a portrait is inverted into the bar height, so the dark hair stands " +
                "tallest, and every bar keeps the color of the pixel it stands for.",
        ) { chart ->
            val picture = document.createElement(HtmlTagName.img)
            picture.listen(LOAD) { chart.setOption(imageToBar3DOption(picture)) }
            picture.src = "data/img/gl/portrait.jpg"
        },
        glImageBars.interactive(
            "bar3d-voxelize-image",
            "图像体素化",
            "Voxelize image",
            "一枚硬币的照片被压到八十行像素，变成一片金属质感的柱阵，亮处的颜色溢出成高光。",
            "A coin photo is squeezed to eighty rows of pixels and rebuilt as a metal bar field whose " +
                "bright pixels overflow into highlights.",
        ) { chart ->
            val picture = document.createElement(HtmlTagName.img)
            picture.listen(LOAD) { chart.setOption(voxelizeOption(picture)) }
            picture.src = "data/img/gl/bitcoin.png"
        },
    )
