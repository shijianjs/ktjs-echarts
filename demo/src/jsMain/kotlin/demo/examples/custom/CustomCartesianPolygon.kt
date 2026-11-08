package demo.examples.custom

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.DataZoomTypes
import io.github.shijianjs.ktjs.echarts.FilterModes
import io.github.shijianjs.ktjs.echarts.TooltipTriggers
import io.github.shijianjs.ktjs.echarts.VisualProps
import io.github.shijianjs.ktjs.echarts.options.CustomPathOption
import io.github.shijianjs.ktjs.echarts.options.DataZoomOption
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.LegendOption
import io.github.shijianjs.ktjs.echarts.options.OptionDataItem
import io.github.shijianjs.ktjs.echarts.options.OptionDataValue
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.XAxisOption
import io.github.shijianjs.ktjs.echarts.options.YAxisOption
import io.github.shijianjs.ktjs.echarts.options.customSeries
import io.github.shijianjs.ktjs.echarts.options.one
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.js
import io.github.shijianjs.ktjs.zrender.ZRColor
import io.github.shijianjs.ktjs.zrender.ZRStyleProps
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
