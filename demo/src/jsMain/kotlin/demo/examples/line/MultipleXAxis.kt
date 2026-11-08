package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category

private val multipleXAxisCategory = Category(Groups.LINE, "examples/line/MultipleXAxis.kt")

val multipleXAxisEntry: CatalogEntry =
    multipleXAxisCategory.chart("multiple-x-axis", "多 X 轴", "Multiple X Axes") {
            val colors = arrayOf(ZRColor("#5470C6"), ZRColor("#EE6666"))
            unsafeJso<EChartsOption> {
                color = colors
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.none
                            axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.cross }
                        },
                    )
                legend = one(unsafeJso<LegendOption> {})
                grid = one(unsafeJso<GridOption> { top = StringOrDouble(70.0); bottom = StringOrDouble(50.0) })
                xAxis =
                    arrayOf(
                        precipitationAxis(monthsOf("2016"), colors[1]),
                        precipitationAxis(monthsOf("2015"), colors[0]),
                    )
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                series =
                    arrayOf<SeriesOption>(
                        // The 2015 series is drawn on the second axis, the one labelled with 2015.
                        lineSeries {
                            name = "Precipitation(2015)"
                            xAxisIndex = 1.0
                            smooth = BooleanOrDouble(true)
                            emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }
                            typedData =
                                numbers(2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3)
                        },
                        lineSeries {
                            name = "Precipitation(2016)"
                            smooth = BooleanOrDouble(true)
                            emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }
                            typedData =
                                numbers(3.9, 5.9, 11.1, 18.7, 48.3, 69.2, 231.6, 46.6, 55.4, 18.4, 10.3, 0.7)
                        },
                    )
            }
        }
