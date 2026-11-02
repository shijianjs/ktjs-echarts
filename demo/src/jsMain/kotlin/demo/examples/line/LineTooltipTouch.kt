package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import kotlin.js.JsAny
import zrender.ZRColor
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.scatter.point
import demo.examples.shared.axisLabel
import demo.examples.shared.fixed
import demo.examples.shared.legendNames
import demo.examples.shared.toolbox

private val lineTooltipTouchCategory = Category(Groups.LINE, "examples/line/LineTooltipTouch.kt")

val lineTooltipTouchEntry: CatalogEntry =
    lineTooltipTouchCategory.chart("line-tooltip-touch", "移动端上的 dataZoom 和 tooltip", "Tooltip and DataZoom on Mobile") {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            left = StringOrDouble("center")
                            text = "Tootip and dataZoom on Mobile Device"
                        },
                    )
                legend = one(unsafeJso<LegendOption> { top = StringOrDouble("bottom"); data = legendNames("Intention") })
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            triggerOn = TooltipTriggerOns.none
                            // `[pt[0], 130]`: the box keeps the pointer's x and sits at a fixed height.
                            position =
                                TooltipPosition { point, _, _, _, _ ->
                                    TooltipPosition(
                                        arrayOf(StringOrDouble(point[0]), StringOrDouble(130.0)),
                                    )
                                }
                        },
                    )
                toolbox =
                    one(
                        unsafeJso<ToolboxOption> {
                            left = StringOrDouble("center")
                            itemSize = 25.0
                            top = StringOrDouble(55.0)
                            feature =
                                unsafeJso<ToolboxFeature> {
                                    dataZoom =
                                        unsafeJso<ToolboxDataZoomFeatureOption> {
                                            // `yAxisIndex: 'none'` keeps the vertical scale intact.
                                            yAxisIndex = ModelFinderIndexQuery(false)
                                        }
                                    restore = unsafeJso<ToolboxFeatureOption> {}
                                }
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.time
                            axisPointer =
                                unsafeJso<AxisPointerOption> {
                                    value = OptionDataValue("2016-10-7")
                                    snap = true
                                    lineStyle =
                                        unsafeJso<LineStyleOption> {
                                            color = ZRColor("#7581BD")
                                            width = 2.0
                                        }
                                    label =
                                        unsafeJso<AxisPointerLabelOption> {
                                            show = true
                                            formatter =
                                                LabelFormatter { params ->
                                                    ECharts.format.formatTime("yyyy-MM-dd", params.value)
                                                }
                                            backgroundColor = ZRColor("#7581BD")
                                        }
                                    handle =
                                        unsafeJso<AxisPointerHandleOption> {
                                            show = true
                                            color = "#7581BD"
                                        }
                                }
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            axisTick = unsafeJso<AxisTickOption> { inside = true }
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                            axisLabel =
                                unsafeJso<AxisLabelOption> {
                                    inside = true
                                    formatter = LabelFormatter("{value}\n")
                                }
                            z = 10.0
                        },
                    )
                grid =
                    one(
                        unsafeJso<GridOption> {
                            top = StringOrDouble(110.0)
                            left = StringOrDouble(15.0)
                            right = StringOrDouble(15.0)
                            height = StringOrDouble(160.0)
                        },
                    )
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.inside
                            throttle = 50.0
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        touchLine(
                            "Fake Data",
                            "#0770FF",
                            "rgba(58,77,233,0.8)",
                            "rgba(58,77,233,0.3)",
                            touchWalk(11, 300.0),
                        ),
                        touchLine(
                            "Fake Data",
                            "#F2597F",
                            "rgba(213,72,120,0.8)",
                            "rgba(213,72,120,0.3)",
                            touchWalk(29, 50.0),
                        ),
                    )
            }
        }
