package demo.examples.heatmap

import demo.CatalogEntry
import demo.Groups
import echarts.options.EChartsOption
import echarts.options.TooltipOption
import echarts.options.VisualMapOption
import echarts.options.one
import js.objects.unsafeJso
import demo.examples.Category

private val heatmapLargeCategory = Category(Groups.HEATMAP, "examples/heatmap/HeatmapLarge.kt")

val heatmapLargeEntry: CatalogEntry =
    heatmapLargeCategory.chart(
            "heatmap-large",
            "热力图 - 2w 数据",
            "Heatmap - 20K data",
        ) {
            val (fieldXAxis, fieldYAxis) = fieldAxes()
            unsafeJso<EChartsOption> {
                tooltip = one(unsafeJso<TooltipOption> {})
                xAxis = one(fieldXAxis)
                yAxis = one(fieldYAxis)
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            min = 0.0
                            max = 1.0
                            calculable = true
                            realtime = false
                            inRange = fieldColors()
                        },
                    )
                series = fieldSeries()
            }
        }
