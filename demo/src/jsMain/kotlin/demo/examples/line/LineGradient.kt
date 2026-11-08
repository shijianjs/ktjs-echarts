package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.matrix.dimension

private val lineGradientCategory = Category(Groups.LINE, "examples/line/LineGradient.kt")

val lineGradientEntry: CatalogEntry =
    lineGradientCategory.chart("line-gradient", "折线图的渐变", "Line Gradient") {
            val dates = LINE_GRADIENT_DATA.map { it.first }.toTypedArray()
            val values = LINE_GRADIENT_DATA.map { it.second }.toDoubleArray()
            unsafeJso<EChartsOption> {
                // The first map colors by the value, the second by the position along the axis.
                visualMap =
                    arrayOf(
                        unsafeJso<VisualMapOption> {
                            show = false
                            type = VisualMapTypes.continuous
                            seriesIndex = IndexOrIndexArray(0.0)
                            min = 0.0
                            max = 400.0
                        },
                        unsafeJso<VisualMapOption> {
                            show = false
                            type = VisualMapTypes.continuous
                            seriesIndex = IndexOrIndexArray(1.0)
                            dimension = 0.0
                            min = 0.0
                            max = (LINE_GRADIENT_DATA.size - 1).toDouble()
                        },
                    )
                title =
                    arrayOf(
                        unsafeJso<TitleOption> { left = StringOrDouble("center"); text = "Gradient along the y axis" },
                        unsafeJso<TitleOption> {
                            top = StringOrDouble("55%")
                            left = StringOrDouble("center")
                            text = "Gradient along the x axis"
                        },
                    )
                tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.axis })
                xAxis =
                    arrayOf(
                        unsafeJso<XAxisOption> { data = strings(*dates) },
                        unsafeJso<XAxisOption> { data = strings(*dates); gridIndex = 1.0 },
                    )
                yAxis =
                    arrayOf(
                        unsafeJso<YAxisOption> {},
                        unsafeJso<YAxisOption> { gridIndex = 1.0 },
                    )
                grid =
                    arrayOf(
                        unsafeJso<GridOption> { bottom = StringOrDouble("60%") },
                        unsafeJso<GridOption> { top = StringOrDouble("60%") },
                    )
                series =
                    arrayOf<SeriesOption>(
                        lineSeries {
                            showSymbol = false
                            typedData = numbers(*values)
                        },
                        lineSeries {
                            showSymbol = false
                            typedData = numbers(*values)
                            xAxisIndex = 1.0
                            yAxisIndex = 1.0
                        },
                    )
            }
        }
