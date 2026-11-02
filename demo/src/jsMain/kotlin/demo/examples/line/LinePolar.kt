package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import kotlin.js.JsAny
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.shared.legendNames

private val linePolarCategory = Category(Groups.LINE, "examples/line/LinePolar.kt")

val linePolarEntry: CatalogEntry =
    linePolarCategory.chart("line-polar", "极坐标双数值轴", "Two Value-Axes in Polar") {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Two Value-Axes in Polar" })
                legend = one(unsafeJso<LegendOption> { data = legendNames("line") })
                polar = one(unsafeJso<PolarOption> {})
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
                radiusAxis = one(unsafeJso<RadiusAxisOption> {})
                series =
                    arrayOf<SeriesOption>(
                        lineSeries {
                            coordinateSystem = "polar"
                            name = "line"
                            typedData = polarSpiral()
                        },
                    )
            }
        }
