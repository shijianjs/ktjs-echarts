package demo.examples.custom

import demo.CatalogEntry
import demo.Groups
import echarts.DataZoomTypes
import echarts.FilterModes
import echarts.TooltipTriggers
import echarts.VisualProps
import echarts.options.CustomPathOption
import echarts.options.DataZoomOption
import echarts.options.EChartsOption
import echarts.options.LegendOption
import echarts.options.OptionDataItem
import echarts.options.OptionDataValue
import echarts.options.SeriesOption
import echarts.options.TooltipOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.customSeries
import echarts.options.one
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.js
import zrender.ZRColor
import zrender.ZRStyleProps
import demo.examples.Category
import demo.examples.shared.doubleRow
import demo.examples.shared.legendNames
import demo.examples.shared.numeric
import demo.examples.shared.textOf

private val customCartesianPolygonCategory = Category(Groups.CUSTOM, "examples/custom/CustomCartesianPolygon.kt")

val customCartesianPolygonEntry: CatalogEntry =
    customCartesianPolygonCategory.chart("custom-cartesian-polygon", "自定义多边形图", "Custom Cartesian Polygon") {
            val random = sequence(20260611L)
            val points =
                List(7) {
                    doubleRow(kotlin.math.round(random() * 100), kotlin.math.round(random() * 400))
                }
            unsafeJso<EChartsOption> {
                tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.axis })
                legend = one(unsafeJso<LegendOption> { data = legendNames("bar", "error") })
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.slider
                            filterMode = FilterModes.none
                        },
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.inside
                            filterMode = FilterModes.none
                        },
                    )
                xAxis = one(unsafeJso<XAxisOption> {})
                yAxis = one(unsafeJso<YAxisOption> {})
                series =
                    arrayOf<SeriesOption>(
                        customSeries {
                            clip = true
                            typedData = Array<OptionDataItem?>(points.size) { OptionDataItem(points[it]) }
                            // The original draws the polygon only once, guarded by a flag it keeps in
                            // `params.context`; drawing it for the first data item has the same effect
                            // and does not mutate the shared render context.
                            renderItem = { params, api ->
                                if (params.dataIndex != 0.0) {
                                    null
                                } else {
                                    val color = textOf(api.visual(VisualProps.color))
                                    unsafeJso<CustomPathOption> {
                                        type = "polygon"
                                        shape =
                                            unsafeJso {
                                                this.points =
                                                    Array(points.size) { index ->
                                                        api.coord(
                                                            coord(
                                                                OptionDataValue(numeric(points[index][0])),
                                                                OptionDataValue(numeric(points[index][1])),
                                                            ),
                                                        )
                                                    }
                                            }
                                        style = api.style(unsafeJso<ZRStyleProps> { fill = ZRColor(color) })
                                    }
                                }
                            }
                        },
                    )
            }
        }
