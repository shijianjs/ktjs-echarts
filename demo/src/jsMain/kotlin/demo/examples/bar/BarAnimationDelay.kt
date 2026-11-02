package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import echarts.AnimationEasings
import echarts.options.AnimationDelay
import echarts.options.EChartsOption
import echarts.options.LegendOption
import echarts.options.SeriesOption
import echarts.options.SplitLineOption
import echarts.options.TitleOption
import echarts.options.TooltipOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.barSeries
import echarts.options.numbers
import echarts.options.one
import echarts.options.strings
import js.objects.unsafeJso
import kotlin.js.js
import demo.examples.Category
import demo.examples.shared.legendNames
import demo.examples.shared.toolbox

private val barAnimationDelayCategory = Category(Groups.BAR, "examples/bar/BarAnimationDelay.kt")

val barAnimationDelayEntry: CatalogEntry =
    barAnimationDelayCategory.chart("bar-animation-delay", "柱状图动画延迟", "Animation Delay") {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Bar Animation Delay" })
                legend = one(unsafeJso<LegendOption> { data = legendNames("bar", "bar2") })
                toolbox = toolbox(dataView = true, magicType = true, saveAsImage = true)
                tooltip = one(unsafeJso<TooltipOption> {})
                xAxis = one(unsafeJso<XAxisOption> { data = strings(*Array(100) { "A$it" }); splitLine = unsafeJso<SplitLineOption> { show = false } })
                yAxis = one(unsafeJso<YAxisOption> {})
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            name = "bar"
                            typedData = numbers(*delayedData(cosine = false))
                            emphasis = seriesFocus
                            animationDelay = AnimationDelay { index, _ -> index * 10.0 }
                        },
                        barSeries {
                            name = "bar2"
                            typedData = numbers(*delayedData(cosine = true))
                            emphasis = seriesFocus
                            animationDelay = AnimationDelay { index, _ -> index * 10.0 + 100.0 }
                        },
                    )
                animationEasing = AnimationEasings.elasticOut
                animationDelayUpdate = AnimationDelay { index, _ -> index * 5.0 }
            }
        }
