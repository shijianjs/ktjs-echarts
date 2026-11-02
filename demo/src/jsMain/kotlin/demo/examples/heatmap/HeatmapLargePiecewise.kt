package demo.examples.heatmap

import demo.CatalogEntry
import demo.Groups
import echarts.VisualMapTypes
import echarts.options.EChartsOption
import echarts.options.GridOption
import echarts.options.StringOrDouble
import echarts.options.TooltipOption
import echarts.options.VisualMapOption
import echarts.options.one
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.gauge.center

private val heatmapLargePiecewiseCategory = Category(Groups.HEATMAP, "examples/heatmap/HeatmapLargePiecewise.kt")

val heatmapLargePiecewiseEntry: CatalogEntry =
    heatmapLargePiecewiseCategory.chart(
            "heatmap-large-piecewise",
            "热力图 - 颜色的离散映射",
            "Heatmap - Discrete Mapping of Color",
        ) {
            val (fieldXAxis, fieldYAxis) = fieldAxes()
            unsafeJso<EChartsOption> {
                tooltip = one(unsafeJso<TooltipOption> {})
                grid =
                    one(
                        unsafeJso<GridOption> {
                            right = StringOrDouble(140.0)
                            left = StringOrDouble(40.0)
                        },
                    )
                xAxis = one(fieldXAxis)
                yAxis = one(fieldYAxis)
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            type = VisualMapTypes.piecewise
                            min = 0.0
                            max = 1.0
                            left = StringOrDouble("right")
                            top = StringOrDouble("center")
                            calculable = true
                            realtime = false
                            splitNumber = 8.0
                            inRange = fieldColors()
                        },
                    )
                series = fieldSeries()
            }
        }
