package demo.examples.radar

import demo.CatalogEntry
import demo.Groups
import echarts.RadarShapes
import echarts.options.AxisLineOption
import echarts.options.EChartsOption
import echarts.options.LabelOption
import echarts.options.LegendOption
import echarts.options.LineStyleOption
import echarts.options.RadarOption
import echarts.options.SelectionModes
import echarts.options.SeriesOption
import echarts.options.SplitAreaOption
import echarts.options.SplitLineOption
import echarts.options.SplitLineStyleOption
import echarts.options.StringOrDouble
import echarts.options.TextStyleOption
import echarts.options.TitleOption
import echarts.options.ZRColorOrArray
import echarts.options.one
import js.objects.unsafeJso
import kotlin.js.js
import zrender.ZRColor
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.shared.legendNames

private val radarAqiCategory = Category(Groups.RADAR, "examples/radar/RadarAqi.kt")

val radarAqiEntry: CatalogEntry =
    radarAqiCategory.chart("radar-aqi", "AQI - 雷达图", "AQI - Radar Chart") {
            unsafeJso<EChartsOption> {
                backgroundColor = ZRColor("#161627")
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "AQI - Radar"
                            left = StringOrDouble("center")
                            textStyle = unsafeJso<TextStyleOption> { color = "#eee" }
                        },
                    )
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            bottom = StringOrDouble(5.0)
                            data = legendNames("Beijing", "Shanghai", "Guangzhou")
                            itemGap = 20.0
                            textStyle = unsafeJso<LabelOption> { color = "#fff"; fontSize = StringOrDouble(14.0) }
                            selectedMode = SelectionModes.single
                        },
                    )
                radar =
                    one(
                        unsafeJso<RadarOption> {
                            indicator =
                                arrayOf(
                                    indicator("AQI", 300.0),
                                    indicator("PM2.5", 250.0),
                                    indicator("PM10", 300.0),
                                    indicator("CO", 5.0),
                                    indicator("NO2", 200.0),
                                    indicator("SO2", 100.0),
                                )
                            shape = RadarShapes.circle
                            splitNumber = 5.0
                            axisName = unsafeJso<LabelOption> { color = "rgb(238, 197, 102)" }
                            splitLine =
                                unsafeJso<SplitLineOption> {
                                    lineStyle =
                                        unsafeJso<SplitLineStyleOption> {
                                            typedColor =
                                                ZRColorOrArray(
                                                    arrayOf(
                                                        ZRColor("rgba(238, 197, 102, 1)"),
                                                        ZRColor("rgba(238, 197, 102, 0.8)"),
                                                        ZRColor("rgba(238, 197, 102, 0.6)"),
                                                        ZRColor("rgba(238, 197, 102, 0.4)"),
                                                        ZRColor("rgba(238, 197, 102, 0.2)"),
                                                        ZRColor("rgba(238, 197, 102, 0.1)"),
                                                    ),
                                                )
                                        }
                                }
                            splitArea = unsafeJso<SplitAreaOption> { show = false }
                            axisLine =
                                unsafeJso<AxisLineOption> {
                                    lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("rgba(238, 197, 102, 0.5)") }
                                }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        aqiSeries("Beijing", "#F9713C", 0.1, aqiRows(beijingTable)),
                        aqiSeries("Shanghai", "#B3E4A1", 0.05, aqiRows(shanghaiTable)),
                        aqiSeries("Guangzhou", "rgb(238, 197, 102)", 0.05, aqiRows(guangzhouTable)),
                    )
            }
        }
