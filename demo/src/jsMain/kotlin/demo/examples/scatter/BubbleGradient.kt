package demo.examples.scatter

import demo.CatalogEntry
import demo.Groups
import echarts.options.EChartsOption
import echarts.options.GridOption
import echarts.options.LegendOption
import echarts.options.SeriesOption
import echarts.options.SplitLineStyleOption
import echarts.options.StringOrDouble
import echarts.options.TitleOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.one
import js.objects.unsafeJso
import kotlin.js.js
import zrender.ZRLineTypes
import zrender.gradientColorStop
import zrender.radialGradient
import demo.examples.Category
import demo.examples.shared.legendNames

private val bubbleGradientCategory = Category(Groups.SCATTER, "examples/scatter/BubbleGradient.kt")

val bubbleGradientEntry: CatalogEntry =
    bubbleGradientCategory.chart("bubble-gradient", "气泡图", "Bubble Chart") {
            unsafeJso<EChartsOption> {
                backgroundColor =
                    radialGradient(
                        0.3,
                        0.3,
                        0.8,
                        arrayOf(gradientColorStop(0.0, "#f7f8fa"), gradientColorStop(1.0, "#cdd0d5")),
                    )
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Life Expectancy and GDP by Country"
                            left = StringOrDouble("5%")
                            top = StringOrDouble("3%")
                        },
                    )
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            right = StringOrDouble("10%")
                            top = StringOrDouble("3%")
                            data = legendNames("1990", "2015")
                        },
                    )
                grid = one(unsafeJso<GridOption> { left = StringOrDouble("8%"); top = StringOrDouble("10%") })
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            splitLine = unsafeJso { lineStyle = unsafeJso<SplitLineStyleOption> { type = ZRLineTypes.dashed } }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            splitLine = unsafeJso { lineStyle = unsafeJso<SplitLineStyleOption> { type = ZRLineTypes.dashed } }
                            scale = true
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        bubbleSeries("1990", expectancy1990, "rgb(251, 118, 123)", "rgb(204, 46, 72)"),
                        bubbleSeries("2015", expectancy2015, "rgb(129, 227, 238)", "rgb(25, 183, 207)"),
                    )
            }
        }
