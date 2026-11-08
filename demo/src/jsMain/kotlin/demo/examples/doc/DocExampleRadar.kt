package demo.examples.doc

import demo.CatalogEntry
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.radar.indicator
import demo.examples.shared.axisLabel
import demo.examples.sunburst.radius

private val docExampleRadarCategory = Category(Groups.DOC, "examples/doc/DocExampleRadar.kt")

val docExampleRadarEntry: CatalogEntry =
    docExampleRadarCategory.chart("doc-example/radar", "雷达图的坐标轴样式", "Radar") {
            unsafeJso<EChartsOption> {
                radar =
                    one(
                        unsafeJso<RadarOption> {
                            indicator =
                                arrayOf(
                                    unsafeJso<RadarIndicator> { name = "指标一" },
                                    unsafeJso<RadarIndicator> { name = "指标二" },
                                    unsafeJso<RadarIndicator> { name = "指标三" },
                                    unsafeJso<RadarIndicator> { name = "指标四" },
                                    unsafeJso<RadarIndicator> { name = "指标五" },
                                )
                            center = arrayOf(StringOrDouble("50%"), StringOrDouble("50%"))
                            radius = StringOrDouble("80%")
                            startAngle = 90.0
                            splitNumber = 4.0
                            shape = RadarShapes.circle
                            axisName =
                                unsafeJso<LabelOption> {
                                    formatter = LabelFormatter("【{value}】")
                                    color = "#72ACD1"
                                }
                            splitArea =
                                unsafeJso<SplitAreaOption> {
                                    areaStyle =
                                        unsafeJso<SplitAreaStyleOption> {
                                            typedColor =
                                                arrayOf(
                                                    ZRColor("#B8D3E4"),
                                                    ZRColor("#96C5E3"),
                                                    ZRColor("#7DB5DA"),
                                                    ZRColor("#72ACD1"),
                                                )
                                        }
                                }
                            axisTick =
                                unsafeJso<AxisTickOption> {
                                    show = BooleanOrString(true)
                                    lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("rgba(255, 255, 255, 0.8)") }
                                }
                            axisLabel =
                                unsafeJso<AxisLabelOption> {
                                    show = true
                                    color = "white"
                                }
                            axisLine = unsafeJso<AxisLineOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("rgba(255, 255, 255, 0.4)") } }
                            splitLine = unsafeJso<SplitLineOption> { lineStyle = unsafeJso<SplitLineStyleOption> { color = ZRColor("rgba(255, 255, 255, 0.4)") } }
                        },
                    )
            }
        }
