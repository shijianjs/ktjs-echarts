package demo.examples.graphic

import demo.CatalogEntry
import demo.Groups
import echarts.options.EChartsOption
import echarts.options.GraphicComponentLooseOption
import echarts.options.GraphicComponentOption
import echarts.options.one
import js.objects.unsafeJso
import zrender.ZRColor
import demo.examples.Category
import demo.examples.shared.every

private val graphicWaveAnimationCategory = Category(Groups.GRAPHIC, "examples/graphic/GraphicWaveAnimation.kt")

val graphicWaveAnimationEntry: CatalogEntry =
    graphicWaveAnimationCategory.interactive(
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
        }
