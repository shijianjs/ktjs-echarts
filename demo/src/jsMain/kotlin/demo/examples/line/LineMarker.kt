package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import kotlin.js.JsAny
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.shared.axisLabel
import demo.examples.shared.toolbox

private val lineMarkerCategory = Category(Groups.LINE, "examples/line/LineMarker.kt")

val lineMarkerEntry: CatalogEntry =
    lineMarkerCategory.chart("line-marker", "未来一周气温变化", "Temperature Change in the Coming Week") {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Temperature Change in the Coming Week" })
                tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.axis })
                legend = one(unsafeJso<LegendOption> {})
                toolbox =
                    one(
                        unsafeJso<ToolboxOption> {
                            show = true
                            feature =
                                unsafeJso<ToolboxFeature> {
                                    dataZoom =
                                        unsafeJso<ToolboxDataZoomFeatureOption> {
                                            // The upstream `yAxisIndex: 'none'` keeps the vertical scale intact.
                                            yAxisIndex = ModelFinderIndexQuery(false)
                                        }
                                    dataView = unsafeJso<DataViewOption> { readOnly = false }
                                    magicType = unsafeJso<MagicTypeOption> { type = arrayOf("line", "bar") }
                                    restore = unsafeJso<ToolboxFeatureOption> {}
                                    saveAsImage = unsafeJso<SaveAsImageOption> {}
                                }
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            boundaryGap = BooleanOrStringArray(false)
                            data = strings("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            axisLabel = unsafeJso<AxisLabelOption> { formatter = LabelFormatter("{value} °C") }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        lineSeries {
                            name = "Highest"
                            typedData = numbers(10.0, 11.0, 13.0, 11.0, 12.0, 12.0, 9.0)
                            markPoint =
                                unsafeJso<MarkPointOption> {
                                    data =
                                        arrayOf(
                                            unsafeJso<MarkPointDataItemOption> {
                                                type = MarkerStatisticTypes.max
                                                name = "Max"
                                            },
                                            unsafeJso<MarkPointDataItemOption> {
                                                type = MarkerStatisticTypes.min
                                                name = "Min"
                                            },
                                        )
                                }
                            markLine =
                                unsafeJso<MarkLineOption> {
                                    data =
                                        arrayOf(
                                            MarkLineDataItem(
                                                unsafeJso<MarkLine1DDataItemOption> {
                                                    type = MarkerStatisticTypes.average
                                                    name = "Avg"
                                                },
                                            ),
                                        )
                                }
                        },
                        lineSeries {
                            name = "Lowest"
                            typedData = numbers(1.0, -2.0, 2.0, 5.0, 3.0, 2.0, 0.0)
                            markPoint =
                                unsafeJso<MarkPointOption> {
                                    data =
                                        arrayOf(
                                            unsafeJso<MarkPointDataItemOption> {
                                                name = "周最低"
                                                value = StringOrDouble(-2.0)
                                                xAxis = ScaleDataValue(1.0)
                                                yAxis = ScaleDataValue(-1.5)
                                            },
                                        )
                                }
                            markLine =
                                unsafeJso<MarkLineOption> {
                                    data =
                                        arrayOf(
                                            MarkLineDataItem(
                                                unsafeJso<MarkLine1DDataItemOption> {
                                                    type = MarkerStatisticTypes.average
                                                    name = "Avg"
                                                },
                                            ),
                                            MarkLineDataItem(
                                                arrayOf(
                                                    unsafeJso<MarkLine2DDataItemDimOption> {
                                                        symbol = SymbolShape("none")
                                                        x = StringOrDouble("90%")
                                                        yAxis = ScaleDataValue("max")
                                                    },
                                                    unsafeJso<MarkLine2DDataItemDimOption> {
                                                        symbol = SymbolShape("circle")
                                                        label =
                                                            unsafeJso<MarkLineLabelOption> {
                                                                position = LabelPosition("start")
                                                                formatter = LabelFormatter("Max")
                                                            }
                                                        type = MarkerStatisticTypes.max
                                                        name = "最高点"
                                                    },
                                                ),
                                            ),
                                        )
                                }
                        },
                    )
            }
        }
