package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.scatter.point
import demo.examples.shared.toolbox

private val areaTimeAxisCategory = Category(Groups.LINE, "examples/line/AreaTimeAxis.kt")

val areaTimeAxisEntry: CatalogEntry =
    areaTimeAxisCategory.chart("area-time-axis", "时间轴折线图", "Area Chart with Time Axis") {
            unsafeJso<EChartsOption> {
                // The title of the original is kept verbatim, including the misspelling.
                title = one(unsafeJso<TitleOption> { left = StringOrDouble("center"); text = "Large Ara Chart" })
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            // The tooltip follows the pointer horizontally and sits near the top.
                            position =
                                TooltipPosition(
                                    { point, _, _, _, _ ->
                                        TooltipPosition(arrayOf<StringOrDouble>(StringOrDouble(point[0]), StringOrDouble("10%")))
                                    },
                                )
                        },
                    )
                toolbox =
                    one(
                        unsafeJso<ToolboxOption> {
                            feature =
                                unsafeJso<ToolboxFeature> {
                                    // `yAxisIndex: 'none'` keeps the zoom horizontal.
                                    dataZoom = unsafeJso<ToolboxDataZoomFeatureOption> { yAxisIndex = ModelFinderIndexQuery("none") }
                                    restore = unsafeJso<ToolboxFeatureOption> {}
                                    saveAsImage = unsafeJso<SaveAsImageOption> {}
                                }
                        },
                    )
                xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.time; boundaryGap = BooleanOrStringArray(false) })
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            boundaryGap = BooleanOrStringArray(arrayOf(StringOrDouble(0.0), StringOrDouble("100%")))
                        },
                    )
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> { type = DataZoomTypes.inside; start = 0.0; end = 20.0 },
                        unsafeJso<DataZoomOption> { type = DataZoomTypes.slider; start = 0.0; end = 20.0 },
                    )
                series =
                    arrayOf<SeriesOption>(
                        lineSeries {
                            name = "Fake Data"
                            smooth = BooleanOrDouble(true)
                            symbol = SymbolShape("none")
                            areaStyle = unsafeJso<AreaStyleOption> {}
                            typedData = points(*timeAxisPoints())
                        },
                    )
            }
        }
