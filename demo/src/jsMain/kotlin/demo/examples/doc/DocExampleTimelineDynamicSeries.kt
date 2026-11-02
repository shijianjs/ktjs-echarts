package demo.examples.doc

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

private val docExampleTimelineDynamicSeriesCategory = Category(Groups.DOC, "examples/doc/DocExampleTimelineDynamicSeries.kt")

val docExampleTimelineDynamicSeriesEntry: CatalogEntry =
    docExampleTimelineDynamicSeriesCategory.chart(
            "doc-example/timeline-dynamic-series",
            "时间轴：切换系列数量",
            "Timeline: dynamic series",
        ) {
            unsafeJso<EChartsOption> {
                baseOption =
                    unsafeJso {
                        timeline =
                            unsafeJso<TimelineOption> {
                                axisType = "category"
                                autoPlay = false
                                playInterval = 1000.0
                                controlStyle = unsafeJso<TimelineControlStyle> { playIcon = PLAY_ICON }
                                // Without it a point with fewer series would merge into the previous
                                // one instead of dropping the extra series.
                                replaceMerge = StringOrStringArray("series")
                                data = strings("2 series", "3 series", "1 series")
                            }
                        tooltip =
                            one(
                                unsafeJso<TooltipOption> {
                                    trigger = TooltipTriggers.axis
                                    axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.shadow }
                                },
                            )
                        legend = one(unsafeJso<LegendOption> {})
                        grid =
                            one(
                                unsafeJso<GridOption> {
                                    top = StringOrDouble(80.0)
                                    bottom = StringOrDouble(100.0)
                                },
                            )
                        toolbox =
                            one(
                                unsafeJso<ToolboxOption> {
                                    left = StringOrDouble("center")
                                    top = StringOrDouble(30.0)
                                    feature =
                                        unsafeJso<ToolboxFeature> {
                                            dataZoom = unsafeJso<ToolboxDataZoomFeatureOption> {}
                                        }
                                },
                            )
                        xAxis =
                            one(
                                unsafeJso<XAxisOption> {
                                    type = AxisTypes.category
                                    data = strings("CityB", "CityT", "CityH", "CityS")
                                    splitLine = unsafeJso<SplitLineOption> { show = false }
                                },
                            )
                        yAxis =
                            one(
                                unsafeJso<YAxisOption> {
                                    type = AxisTypes.value
                                    name = "GDP"
                                },
                            )
                        series = arrayOf()
                    }
                options =
                    arrayOf(
                        unsafeJso {
                            series =
                                arrayOf<SeriesOption>(
                                    gdpBar("a", doubleArrayOf(12.0, 33.0, 44.0, 55.0)),
                                    gdpBar("b", doubleArrayOf(55.0, 66.0, 77.0, 88.0)),
                                )
                        },
                        unsafeJso {
                            series =
                                arrayOf<SeriesOption>(
                                    gdpBar("a", doubleArrayOf(22.0, 33.0, 44.0, 55.0)),
                                    gdpBar("b", doubleArrayOf(55.0, 66.0, 77.0, 88.0)),
                                    gdpBar("c", doubleArrayOf(55.0, 66.0, 77.0, 88.0)),
                                )
                        },
                        unsafeJso {
                            series =
                                arrayOf<SeriesOption>(
                                    gdpBar("b", doubleArrayOf(55.0, 66.0, 77.0, 88.0)),
                                )
                        },
                    )
            }
        }
