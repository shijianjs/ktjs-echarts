package demo.examples.radar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.RadarShapes
import io.github.shijianjs.ktjs.echarts.options.AxisLineOption
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.LabelOption
import io.github.shijianjs.ktjs.echarts.options.LegendOption
import io.github.shijianjs.ktjs.echarts.options.LineStyleOption
import io.github.shijianjs.ktjs.echarts.options.RadarOption
import io.github.shijianjs.ktjs.echarts.options.SelectionModes
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.SplitAreaOption
import io.github.shijianjs.ktjs.echarts.options.SplitLineOption
import io.github.shijianjs.ktjs.echarts.options.SplitLineStyleOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.TextStyleOption
import io.github.shijianjs.ktjs.echarts.options.TitleOption
import io.github.shijianjs.ktjs.echarts.options.ZRColorOrArray
import io.github.shijianjs.ktjs.echarts.options.one
import js.objects.unsafeJso
import kotlin.js.js
import io.github.shijianjs.ktjs.zrender.ZRColor
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
