package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.gl.channel
import demo.examples.themeriver.days

private val barStackNormalizationAndVariationCategory = Category(Groups.BAR, "examples/bar/BarStackNormalizationAndVariation.kt")

val barStackNormalizationAndVariationEntry: CatalogEntry =
    barStackNormalizationAndVariationCategory.interactive(
            "bar-stack-normalization-and-variation",
            "堆叠柱状图的归一化和变化",
            "Stacked Bar Normalization and Variation",
            "归一化堆叠柱之外再加一层 graphic 多边形，把相邻两天的同渠道色带连起来，形状按图表的实际宽高算出。",
            "The normalized stacks plus a graphic layer that connects the same channel between two " +
                "neighbouring days; the polygons are laid out from the real width and height of the chart.",
        ) { chart ->
            val (xAxisOption, yAxisOption) = normalizationAxes()
            chart.setOption(
                unsafeJso<EChartsOption> {
                    legend = one(normalizationLegend())
                    grid =
                        one(
                            unsafeJso<GridOption> {
                                left = StringOrDouble(VARIATION_GRID_LEFT)
                                right = StringOrDouble(VARIATION_GRID_RIGHT)
                                top = StringOrDouble(VARIATION_GRID_TOP)
                                bottom = StringOrDouble(VARIATION_GRID_BOTTOM)
                            },
                        )
                    xAxis = one(xAxisOption)
                    yAxis = one(yAxisOption)
                    series = normalizedSeriesList()
                    graphic =
                        one(
                            GraphicComponentLooseOption(
                                unsafeJso<GraphicComponentOption> {
                                    elements = variationPolygons(chart.getWidth(), chart.getHeight())
                                },
                            ),
                        )
                },
            )
        }
