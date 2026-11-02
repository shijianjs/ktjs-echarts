package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import kotlin.js.JsAny
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.shared.legendNames

private val linePolar2Category = Category(Groups.LINE, "examples/line/LinePolar2.kt")

val linePolar2Entry: CatalogEntry =
    linePolar2Category.chart("line-polar2", "极坐标双数值轴（二）", "Two Value-Axes in Polar (2)") {
            unsafeJso<EChartsOption> {
                animationDuration = AnimationDuration(2000.0)
                title = one(unsafeJso<TitleOption> { text = "Two Value-Axes in Polar" })
                legend = one(unsafeJso<LegendOption> { data = legendNames("line") })
                polar =
                    one(
                        unsafeJso<PolarOption> {
                            center = arrayOf(StringOrDouble("50%"), StringOrDouble("54%"))
                        },
                    )
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.cross }
                        },
                    )
                angleAxis =
                    one(
                        unsafeJso<AngleAxisOption> {
                            type = AxisTypes.value
                            startAngle = 0.0
                        },
                    )
                radiusAxis = one(unsafeJso<RadiusAxisOption> { min = AxisMinMax(0.0) })
                series =
                    arrayOf<SeriesOption>(
                        lineSeries {
                            coordinateSystem = "polar"
                            name = "line"
                            showSymbol = false
                            typedData = polarRose()
                        },
                    )
            }
        }
