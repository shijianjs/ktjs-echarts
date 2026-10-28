package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.AnimationEasings
import echarts.options.AnimationKeyframe
import echarts.options.EChartsOption
import echarts.options.ElementKeyframeAnimationOption
import echarts.options.ElementKeyframeAnimationOptionOrArray
import echarts.options.GraphicComponentLooseOption
import echarts.options.GraphicComponentOption
import echarts.options.GraphicElement
import echarts.options.StringOrDouble
import echarts.options.one
import js.objects.unsafeJso
import zrender.ZRColor
import zrender.ZRFontWeights
import zrender.ZRLineType
import zrender.ZRShapeProps
import zrender.ZRStyleProps

/*
 * The `graphic` family of the official gallery: elements animated by keyframes instead of a series.
 *
 * All three pages put their timing into `keyframeAnimation`, the property the graphic component shares
 * with the elements a custom series returns, so no timer is needed — the animation runs inside ECharts.
 * `graphic-wave-animation` still asks the chart for its size, so it goes through
 * `Category.interactive`, and its upstream control panel (`app.config` plus the sliders around it) is
 * left out: the panel only feeds `onChange`, which re-issues the same option.
 */

private val graphicAnimations = Category(Groups.GRAPHIC, "examples/GraphicAnimations.kt")

/** The stroke animation of `graphic-stroke-animation`: text drawn by a dash that travels around it. */
private fun strokeAnimationElements(): Array<GraphicElement> =
    arrayOf(
        unsafeJso<GraphicElement> {
            type = "text"
            left = StringOrDouble("center")
            top = StringOrDouble("center")
            style =
                unsafeJso<ZRStyleProps> {
                    text = "Apache ECharts"
                    fontSize = StringOrDouble(80.0)
                    fontWeight = ZRFontWeights.bold
                    lineDash = ZRLineType(arrayOf(0.0, 200.0))
                    lineDashOffset = 0.0
                    fill = ZRColor("transparent")
                    stroke = ZRColor("#000")
                    lineWidth = 1.0
                }
            keyframeAnimation =
                ElementKeyframeAnimationOptionOrArray(
                    unsafeJso<ElementKeyframeAnimationOption> {
                        duration = 3000.0
                        loop = true
                        keyframes =
                            arrayOf(
                                unsafeJso<AnimationKeyframe> {
                                    percent = 0.7
                                    style =
                                        unsafeJso<ZRStyleProps> {
                                            fill = ZRColor("transparent")
                                            lineDashOffset = 200.0
                                            lineDash = ZRLineType(arrayOf(200.0, 0.0))
                                        }
                                },
                                // Stop for a while, then fill the outline.
                                unsafeJso<AnimationKeyframe> {
                                    percent = 0.8
                                    style = unsafeJso<ZRStyleProps> { fill = ZRColor("transparent") }
                                },
                                unsafeJso<AnimationKeyframe> {
                                    percent = 1.0
                                    style = unsafeJso<ZRStyleProps> { fill = ZRColor("black") }
                                },
                            )
                    },
                )
        },
    )

/**
 * The seven bars of `graphic-loading`.
 *
 * Upstream keeps the group at `left: 'center'` and offsets each bar with `x`, so the whole row scales
 * around its middle; every bar delays its loop by one fifth of the duration, which is what makes the
 * bars read as a wave.
 */
private fun loadingAnimationElements(): Array<GraphicElement> {
    val bars =
        Array(7) { bar ->
            unsafeJso<GraphicElement> {
                type = "rect"
                x = StringOrDouble(bar * 20.0)
                shape =
                    unsafeJso<ZRShapeProps> {
                        x = 0.0
                        y = -40.0
                        width = 10.0
                        height = 80.0
                    }
                style = unsafeJso<ZRStyleProps> { fill = ZRColor("#5470c6") }
                keyframeAnimation =
                    ElementKeyframeAnimationOptionOrArray(
                        unsafeJso<ElementKeyframeAnimationOption> {
                            duration = 1000.0
                            delay = bar * 200.0
                            loop = true
                            keyframes =
                                arrayOf(
                                    unsafeJso<AnimationKeyframe> {
                                        percent = 0.5
                                        scaleY = 0.3
                                        easing = AnimationEasings.cubicIn
                                    },
                                    unsafeJso<AnimationKeyframe> {
                                        percent = 1.0
                                        scaleY = 1.0
                                        easing = AnimationEasings.cubicOut
                                    },
                                )
                        },
                    )
            }
        }
    return arrayOf(
        unsafeJso<GraphicElement> {
            type = "group"
            left = StringOrDouble("center")
            top = StringOrDouble("center")
            children = bars
        },
    )
}

/**
 * The dot field of `graphic-wave-animation`, sampled over the chart box.
 *
 * Upstream walks the canvas in 40 px steps and reads the noise at `x / frequency + offsetX` and
 * `y / frequency + offsetY`; the delay of every dot is `(rand - 1) * duration`, so a dot the field
 * places low starts immediately and the wave travels across the canvas.
 */
private fun waveAnimationElements(canvasWidth: Double, canvasHeight: Double): Array<GraphicElement> {
    val noise = PerlinNoise()
    noise.seed(0.42)
    val dots = mutableListOf<GraphicElement>()
    var dotX = 20.0
    while (dotX < canvasWidth) {
        var dotY = 20.0
        while (dotY < canvasHeight) {
            val rand = noise.perlin2(dotX / FREQUENCY, dotY / FREQUENCY + OFFSET_Y)
            dots.add(
                unsafeJso<GraphicElement> {
                    type = "circle"
                    x = StringOrDouble(dotX)
                    y = StringOrDouble(dotY)
                    shape = unsafeJso<ZRShapeProps> { r = MAX_RADIUS }
                    style = unsafeJso<ZRStyleProps> { fill = ZRColor(DARK_COLOR) }
                    keyframeAnimation =
                        ElementKeyframeAnimationOptionOrArray(
                            unsafeJso<ElementKeyframeAnimationOption> {
                                duration = WAVE_DURATION
                                loop = true
                                delay = (rand - 1) * WAVE_DURATION
                                keyframes =
                                    arrayOf(
                                        unsafeJso<AnimationKeyframe> {
                                            percent = 0.5
                                            easing = AnimationEasings.sinusoidalInOut
                                            style = unsafeJso<ZRStyleProps> { fill = ZRColor(LIGHT_COLOR) }
                                            scaleX = MIN_RADIUS / MAX_RADIUS
                                            scaleY = MIN_RADIUS / MAX_RADIUS
                                        },
                                        unsafeJso<AnimationKeyframe> {
                                            percent = 1.0
                                            easing = AnimationEasings.sinusoidalInOut
                                            style = unsafeJso<ZRStyleProps> { fill = ZRColor(DARK_COLOR) }
                                            scaleX = 1.0
                                            scaleY = 1.0
                                        },
                                    )
                            },
                        )
                },
            )
            dotY += STEP
        }
        dotX += STEP
    }
    return dots.toTypedArray()
}

private fun graphicOption(graphicElements: Array<GraphicElement>): EChartsOption =
    unsafeJso<EChartsOption> {
        graphic =
            one(
                GraphicComponentLooseOption(
                    unsafeJso<GraphicComponentOption> {
                        elements = graphicElements
                    },
                ),
            )
    }

/** Distance between two dots of `graphic-wave-animation`, in pixels. */
private const val STEP = 40.0

/** The `frequency` of the upstream control panel; the noise is sampled at `x / frequency`. */
private const val FREQUENCY = 500.0

/** The `offsetY` default of the upstream panel; `offsetX` is `0`, so the x term needs no offset. */
private const val OFFSET_Y = 100.0

private const val MIN_RADIUS = 5.0
private const val MAX_RADIUS = 22.0
private const val WAVE_DURATION = 4000.0
private const val LIGHT_COLOR = "#fff"
private const val DARK_COLOR = "#000"

/** Entries of this file, in sidebar order. */
val graphicAnimationEntries: List<CatalogEntry> =
    listOf(
        graphicAnimations.chart(
            "graphic-stroke-animation",
            "关键帧描边动画",
            "Stroke Animation",
        ) {
            graphicOption(strokeAnimationElements())
        },
        graphicAnimations.chart(
            "graphic-loading",
            "加载动画",
            "Customized Loading Animation",
        ) {
            graphicOption(loadingAnimationElements())
        },
        graphicAnimations.interactive(
            "graphic-wave-animation",
            "波浪动画",
            "Wave Animation",
            summaryZh = "噪点场驱动的圆点阵列，每颗点按噪声值错开关键帧动画的延迟。",
            summaryEn = "A dot field driven by perlin noise; every dot offsets its keyframe animation by the noise value.",
        ) { chart ->
            chart.setOption(
                unsafeJso<EChartsOption> {
                    backgroundColor = ZRColor(LIGHT_COLOR)
                    graphic =
                        one(
                            GraphicComponentLooseOption(
                                unsafeJso<GraphicComponentOption> {
                                    elements = waveAnimationElements(chart.getWidth(), chart.getHeight())
                                },
                            ),
                        )
                },
            )
        },
    )
