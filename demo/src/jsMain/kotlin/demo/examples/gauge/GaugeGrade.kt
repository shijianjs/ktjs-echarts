package demo.examples.gauge

import demo.CatalogEntry
import demo.Groups
import echarts.options.EChartsOption
import echarts.options.GaugeAxisLabelOption
import echarts.options.GaugeAxisLineOption
import echarts.options.GaugeAxisLineStyleOption
import echarts.options.GaugeAxisTickOption
import echarts.options.GaugeDetailOption
import echarts.options.GaugePointerOption
import echarts.options.GaugeSplitLineOption
import echarts.options.GaugeTitleOption
import echarts.options.ItemStyleOption
import echarts.options.LabelFormatter
import echarts.options.LineStyleOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.gaugeColorStop
import echarts.options.gaugeSeries
import js.objects.unsafeJso
import kotlin.js.js
import zrender.ZRColor
import demo.examples.Category
import demo.examples.shared.axisLabel
import demo.examples.sunburst.radius

private val gaugeGradeCategory = Category(Groups.GAUGE, "examples/gauge/GaugeGrade.kt")

val gaugeGradeEntry: CatalogEntry =
    gaugeGradeCategory.chart("gauge-grade", "等级仪表盘", "Grade Gauge") {
            unsafeJso<EChartsOption> {
                series =
                    arrayOf<SeriesOption>(
                        gaugeSeries {
                            startAngle = 180.0
                            endAngle = 0.0
                            center = center("50%", "75%")
                            radius = StringOrDouble("90%")
                            min = 0.0
                            max = 1.0
                            splitNumber = 8.0
                            axisLine =
                                unsafeJso<GaugeAxisLineOption> {
                                    lineStyle =
                                        unsafeJso<GaugeAxisLineStyleOption> {
                                            width = 6.0
                                            color =
                                                arrayOf(
                                                    gaugeColorStop(0.25, "#FF6E76"),
                                                    gaugeColorStop(0.5, "#FDDD60"),
                                                    gaugeColorStop(0.75, "#58D9F9"),
                                                    gaugeColorStop(1.0, "#7CFFB2"),
                                                )
                                        }
                                }
                            pointer =
                                unsafeJso<GaugePointerOption> {
                                    icon = "path://M12.8,0.7l12,40.1H0.7L12.8,0.7z"
                                    length = StringOrDouble("12%")
                                    width = 20.0
                                    offsetCenter = center("0", "-60%")
                                    itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("auto") }
                                }
                            axisTick =
                                unsafeJso<GaugeAxisTickOption> {
                                    length = 12.0
                                    lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("auto"); width = 2.0 }
                                }
                            splitLine =
                                unsafeJso<GaugeSplitLineOption> {
                                    length = 20.0
                                    lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("auto"); width = 5.0 }
                                }
                            axisLabel =
                                unsafeJso<GaugeAxisLabelOption> {
                                    color = "#464646"
                                    fontSize = StringOrDouble(20.0)
                                    distance = -60.0
                                    typedRotate = echarts.options.LabelRotates.tangential
                                    formatter = LabelFormatter(gradeLabels)
                                }
                            title = unsafeJso<GaugeTitleOption> { offsetCenter = center("0", "-10%"); fontSize = StringOrDouble(20.0) }
                            detail =
                                unsafeJso<GaugeDetailOption> {
                                    fontSize = StringOrDouble(30.0)
                                    offsetCenter = center("0", "-35%")
                                    valueAnimation = true
                                    formatter = LabelFormatter(roundedPercent)
                                    color = "inherit"
                                }
                            typedData = arrayOf(gaugeData("Grade Rating", 0.7))
                        },
                    )
            }
        }
