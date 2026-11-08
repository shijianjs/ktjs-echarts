package demo.examples.radar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.RadarShapes
import io.github.shijianjs.ktjs.echarts.options.AreaStyleOption
import io.github.shijianjs.ktjs.echarts.options.AxisLineOption
import io.github.shijianjs.ktjs.echarts.options.DoubleOrDoubleArray
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.EmphasisOption
import io.github.shijianjs.ktjs.echarts.options.LabelFormatter
import io.github.shijianjs.ktjs.echarts.options.LabelOption
import io.github.shijianjs.ktjs.echarts.options.LegendOption
import io.github.shijianjs.ktjs.echarts.options.LineStyleOption
import io.github.shijianjs.ktjs.echarts.options.RadarOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.SplitAreaOption
import io.github.shijianjs.ktjs.echarts.options.SplitAreaStyleOption
import io.github.shijianjs.ktjs.echarts.options.SplitLineOption
import io.github.shijianjs.ktjs.echarts.options.SplitLineStyleOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.SymbolShape
import io.github.shijianjs.ktjs.echarts.options.SymbolSize
import io.github.shijianjs.ktjs.echarts.options.TitleOption
import io.github.shijianjs.ktjs.echarts.options.dataItems
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.radarSeries
import js.objects.unsafeJso
import kotlin.js.js
import io.github.shijianjs.ktjs.zrender.ZRColor
import io.github.shijianjs.ktjs.zrender.ZRLineTypes
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.shared.row
import demo.examples.sunburst.radius

private val radarCustomCategory = Category(Groups.RADAR, "examples/radar/RadarCustom.kt")

val radarCustomEntry: CatalogEntry =
    radarCustomCategory.chart("radar-custom", "自定义雷达图", "Customized Radar Chart") {
            unsafeJso<EChartsOption> {
                color = arrayOf(ZRColor("#67F9D8"), ZRColor("#FFE434"), ZRColor("#56A3F1"), ZRColor("#FF917C"))
                title = one(unsafeJso<TitleOption> { text = "Customized Radar Chart" })
                legend = one(unsafeJso<LegendOption> {})
                radar =
                    arrayOf(
                        unsafeJso<RadarOption> {
                            indicator =
                                arrayOf(
                                    indicator("Indicator1"),
                                    indicator("Indicator2"),
                                    indicator("Indicator3"),
                                    indicator("Indicator4"),
                                    indicator("Indicator5"),
                                )
                            center = arrayOf(StringOrDouble("25%"), StringOrDouble("50%"))
                            radius = StringOrDouble(120.0)
                            startAngle = 90.0
                            splitNumber = 4.0
                            shape = RadarShapes.circle
                            axisName =
                                unsafeJso<LabelOption> {
                                    formatter = LabelFormatter("【{value}】")
                                    color = "#428BD4"
                                }
                            splitArea =
                                unsafeJso<SplitAreaOption> {
                                    areaStyle =
                                        unsafeJso<SplitAreaStyleOption> {
                                            typedColor =
                                                arrayOf(
                                                    ZRColor("#77EADF"),
                                                    ZRColor("#26C3BE"),
                                                    ZRColor("#64AFE9"),
                                                    ZRColor("#428BD4"),
                                                )
                                            shadowColor = ZRColor("rgba(0, 0, 0, 0.2)")
                                            shadowBlur = 10.0
                                        }
                                }
                            axisLine =
                                unsafeJso<AxisLineOption> {
                                    lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("rgba(211, 253, 250, 0.8)") }
                                }
                            splitLine =
                                unsafeJso<SplitLineOption> {
                                    lineStyle = unsafeJso<SplitLineStyleOption> { color = ZRColor("rgba(211, 253, 250, 0.8)") }
                                }
                        },
                        unsafeJso<RadarOption> {
                            indicator =
                                arrayOf(
                                    indicator("Indicator1", 150.0),
                                    indicator("Indicator2", 150.0),
                                    indicator("Indicator3", 150.0),
                                    indicator("Indicator4", 120.0),
                                    indicator("Indicator5", 108.0),
                                    indicator("Indicator6", 72.0),
                                )
                            center = arrayOf(StringOrDouble("75%"), StringOrDouble("50%"))
                            radius = StringOrDouble(120.0)
                            axisName =
                                unsafeJso<LabelOption> {
                                    color = "#fff"
                                    backgroundColor = ZRColor("#666")
                                    borderRadius = DoubleOrDoubleArray(3.0)
                                    padding = DoubleOrDoubleArray(arrayOf(3.0, 5.0))
                                }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        radarSeries {
                            emphasis = unsafeJso<EmphasisOption> { lineStyle = unsafeJso<LineStyleOption> { width = 4.0 } }
                            typedData =
                                dataItems(
                                    namedRadar("Data A", row(100.0, 8.0, 0.4, -80.0, 2000.0)),
                                    namedRadar("Data B", row(60.0, 5.0, 0.3, -100.0, 1500.0)) {
                                        areaStyle = unsafeJso<AreaStyleOption> { color = ZRColor("rgba(255, 228, 52, 0.6)") }
                                    },
                                )
                        },
                        radarSeries {
                            radarIndex = 1.0
                            typedData =
                                dataItems(
                                    namedRadar("Data C", row(120.0, 118.0, 130.0, 100.0, 99.0, 70.0)) {
                                        symbol = SymbolShape("rect")
                                        symbolSize = SymbolSize(12.0)
                                        lineStyle = unsafeJso<LineStyleOption> { type = ZRLineTypes.dashed }
                                        label =
                                            unsafeJso<LabelOption> {
                                                show = true
                                                formatter = LabelFormatter(rawValueFormatter)
                                            }
                                    },
                                    namedRadar("Data D", row(100.0, 93.0, 50.0, 90.0, 70.0, 60.0)) {
                                        areaStyle =
                                            unsafeJso<AreaStyleOption> {
                                                color =
                                                    io.github.shijianjs.ktjs.zrender.radialGradient(
                                                        0.1,
                                                        0.6,
                                                        1.0,
                                                        arrayOf(
                                                            io.github.shijianjs.ktjs.zrender.gradientColorStop(0.0, "rgba(255, 145, 124, 0.1)"),
                                                            io.github.shijianjs.ktjs.zrender.gradientColorStop(1.0, "rgba(255, 145, 124, 0.9)"),
                                                        ),
                                                    )
                                            }
                                    },
                                )
                        },
                    )
            }
        }
